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
	private static String MATLAB_PLOT_SKELETON =
			"fig = figure('Visible', 'off');\n" +
					"plot(skeleton)\n" +
					"xlabel('x')\n" +
					"ylabel('y')\n" +
					"saveas(gcf,'./src/main/resources/Integrix/plots/funct_plot_1s.png')"; // Save the plot as a PNG file

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
	public void plotting(String function, String min, String max, String plot_interval) throws ExecutionException, InterruptedException {
		String latexExpr;
		try {
			engine.eval("x = " + min + ":" + plot_interval + ":" + max + ";");
			engine.eval("y = " + function + ";");
			String modifiedScript = MATLAB_PLOT_SKELETON.replace("skeleton", "x,y");
			engine.eval(modifiedScript);
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
