package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.interfaces.Integration_Interface;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Integration implements Integration_Interface {
	private static Integration instance = null;
	private int plot_method = 0;

	public int getPlot_method() {
		return plot_method;
	}

	public void setPlot_method(int plot_method) {
		this.plot_method = plot_method;
	}

	private static String MATLAB_PLOT_SKELETON =
			"fig = figure('Visible', 'off');\n" +
					"plot(x,y)\n" +
					"xlabel('x')\n" +
					"ylabel('y')\n" +
					"saveas(gcf,'./src/main/resources/Integrix/plots/funct_plot_1s.png')";

	private static String MATLAB_PLOT_SKELETON_METHOD =
			"fig = figure('Visible', 'off');\n" +
					"hold on;\n" +
					"plot(x, y, 'Color', [0 0.4470 0.7410]);\n" +  // Function line in blue
					"xlabel('x');\n" +
					"ylabel('y');\n" +
					"%s" +  // Placeholder for the area plotting commands
					"title('Integration Visualization');\n" +
					"hold off;\n" +
					"saveas(gcf, './src/main/resources/Integrix/plots/funct_plot_2s.png');\n";

	// Private constructor to prevent instantiation of the class
	protected Integration() {
	}

	// Static method to get the single instance of the class
	public static Integration getInstance() throws EngineException, InterruptedException {
		// If the instance is null, create a new instance
		if (instance == null) {
			instance = new Integration();
		}
		// Return the single instance
		return instance;
	}

	@Override
	public double shrinkDecimal(double value) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(6);
		if (value < Integration.THRESHOLD) {
			df.setGroupingUsed(false);
		}
        return Double.parseDouble(df.format(value));
	}

	@Override
	public String isDivergent(String function) throws ExecutionException, InterruptedException {
		engine.eval("syms x");
		String functionWithoutDots = function.replaceAll("\\.", "");
		engine.eval("fct = " + functionWithoutDots + ";");
		engine.eval("y = vpa(symsum(fct, x, 1, inf));");
		engine.eval("check = isinf(y);");
		Object check = engine.getVariable("check");
		System.out.println(check.toString());
		if (check.toString().equals("true")) {
			return "The function is divergent";
		} else {
			return "The function is convergent";
		}
	}

	@Override
	public void method_plotting(String function, String min, String max, String plot_interval, String interval) throws ExecutionException, InterruptedException {
		engine.eval("clear;");
		String area_plotting_code = "";
		engine.eval("x = " + min + ":((" + max + "-" + min + ")/" + interval + ")" + ":" + max + ";");
		engine.eval("y = " + function + ";");

		switch (plot_method) {
			case 0:  // Rectangular
				area_plotting_code = "loop = 1;\n" +
						"% Loop until loop is less than the length of x minus 1\n" +
						"while loop <= length(x)-1\n" +
						"    % Calculate width\n" +
						"    width = abs((" + max + " - " + min + ") / " + interval + ");\n" +
						"    % Calculate height\n" +
						"    height = y(loop);\n" +
						"    % Define vertices of the rectangle\n" +
						"    x_coords = [x(loop), x(loop) + width, x(loop) + width, x(loop)];\n" +
						"    y_coords = [0, 0, height, height];\n" +
						"    % Create a polyshape representing the rectangle\n" +
						"    rect_poly = polyshape(x_coords, y_coords);\n" +
						"    % Plot the rectangle\n" +
						"    plot(rect_poly, 'FaceColor', [0 0.4470 0.7410]);\n" +
						"    hold on;\n" +
						"    % Increment loop counter\n" +
						"    loop = loop + 1;\n" +
						"end;\n" +
						"hold off;\n";
				break;
			case 1:  // Simpson's
				area_plotting_code = "loop = 1;\n" +
						"% Plot the vertical lines for Simpson's segments\n" +
						"while loop <= length(x)-2\n" +
						"    x_segment = [x(loop), x(loop), x(loop+1), x(loop+1)];\n" +
						"    y_segment = [0, y(loop), y(loop+1), 0];\n" +
						"    plot(x_segment, y_segment, 'Color', [0 0.4470 0.7410]);\n" +
						"    hold on;\n" +
						"    loop = loop + 2;\n" +
						"end;\n" +
						"% Plot the last vertical line if there's an odd number of points\n" +
						"if mod(length(x), 2) == 0\n" +
						"    plot([x(end), x(end)], [0, y(end)], 'Color', [0 0.4470 0.7410]);\n" +
						"end;\n" +
						"% Plot the horizontal line at y=0\n" +
						"plot([x(1), x(end)], [0, 0], 'Color', [0 0.4470 0.7410]);\n" +
						"hold off;\n";
				break;
			case 2:  // Trapezoidal
				area_plotting_code = "loop = 1;\n" +
						"% Loop until loop is less than the length of x minus 1\n" +
						"while loop <= length(x)-1\n" +
						"    % Calculate width\n" +
						"    width = abs((" + max + " - " + min + ") / " + interval + ");\n" +
						"    % Calculate heights of the left and right points\n" +
						"    height_left = y(loop);\n" +
						"    height_right = y(loop+1);\n" +
						"    % Define vertices of the trapezoid\n" +
						"    x_coords = [x(loop), x(loop+1), x(loop+1), x(loop)];\n" +
						"    y_coords = [0, 0, height_right, height_left];\n" +
						"    % Plot trapezoid\n" +
						"    plot(polyshape(x_coords, y_coords), 'FaceColor', [0 0.4470 0.7410]);\n" +
						"    hold on;\n" +
						"    % Increment loop counter\n" +
						"    loop = loop + 1;\n" +
						"end;\n" +
						"hold off;\n";
				break;
		}

		String plottingScript = String.format(MATLAB_PLOT_SKELETON_METHOD, area_plotting_code);
		engine.eval(plottingScript);
	}

	@Override
	public void plotting(String function, String min, String max, String plot_interval) throws ExecutionException, InterruptedException {
		String latexExpr;
		try {
			engine.eval("clear;");
			engine.eval("x = " + min + ":" + plot_interval + ":" + max + ";");
			engine.eval("y = " + function + ";");
			engine.eval(MATLAB_PLOT_SKELETON);
			engine.eval("syms x");

			// Convert the result to LaTeX
			String latexScript = "latexFunction = latex(" + function + ");";
			engine.eval(latexScript);

			// Retrieve the LaTeX expression
			Object latexFunction = engine.getVariable("latexFunction");
			latexExpr = "\\int_{" + min + "}^{" + max + "}" + latexFunction;

			TeXFormula formula = new TeXFormula(latexExpr);
			Color smokeWhite = new Color(250, 250, 250);
			formula.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/funct_latex.png",
					smokeWhite,
					Color.RED);
		} catch (MatlabExecutionException | MatlabSyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void diff_latex(String function) throws ExecutionException, InterruptedException {
		String latexExpr_diff1;
		String latexExpr_diff2;
		try {
			engine.eval("syms x");
			engine.eval("df1 = diff(" + function + ", x);");
			engine.eval("df2 = diff(df1, x);");

			// Convert the result to LaTeX
			String latexScript_diff1 = "latexFunction_diff1 = latex(df1);";
			engine.eval(latexScript_diff1);
			String latexScript_diff2 = "latexFunction_diff2 = latex(df2);";
			engine.eval(latexScript_diff2);

			// Retrieve the LaTeX expression
			Object latexFunction = engine.getVariable("latexFunction_diff1");
			latexExpr_diff1 = latexFunction.toString();
			Object latexFunction2 = engine.getVariable("latexFunction_diff2");
			latexExpr_diff2 = latexFunction2.toString();

			TeXFormula formula = new TeXFormula(latexExpr_diff1);
			Color smokeWhite = new Color(250, 250, 250);
			formula.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/latexExpr_diff1.png",
					smokeWhite,
					Color.RED);

			TeXFormula formula2 = new TeXFormula(latexExpr_diff2);
			formula2.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/latexExpr_diff2.png",
					smokeWhite,
					Color.RED);
		} catch (MatlabExecutionException | MatlabSyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

        public double integrate(String function, String min, String max) throws MatlabExecutionException, MatlabSyntaxException {
		try {
			engine.eval("syms x");
			// Properly format the integration string to pass function, limits
			String integral_expr = function + ", " + min + ", " + max;
			String integrationScript = "integralResult = integral(@(x) " + integral_expr + ");";
			engine.eval(integrationScript);

		} catch (InterruptedException | ExecutionException ex) {
			throw new RuntimeException("Error during MATLAB execution: " + ex.getMessage(), ex);
		}

        double integralResult;
		try {
			integralResult = ((Double) engine.getVariable("integralResult")).doubleValue();
		} catch (InterruptedException | ExecutionException ex) {
			throw new RuntimeException("Failed to retrieve the integral result: " + ex.getMessage(), ex);
		}
		return integralResult;
	}


}
