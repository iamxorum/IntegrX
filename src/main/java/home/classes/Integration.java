package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import static home.controllers.MainController.setLatexFunction;

public class Integration implements home.interfaces.Integration {
	private static Integration instance;

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

	public double integrate(String function, String min, String max, String plot_interval) throws MatlabExecutionException, MatlabSyntaxException {
		String latexExpr;
		try {
			engine.eval("x = " + min + ":" + plot_interval + ":" + max + ";");
			engine.eval("y = " + function + ";");
			String modifiedScript = MATLAB_PLOT_SKELETON.replace("skeleton", "x,y");
			engine.eval(modifiedScript);
			engine.eval("syms x");
			String integral_expr = "(" + function + "), " + min + ", " + max;
			String integrationScript = "integralResult = integral(@(x) " + integral_expr + ");";
			engine.eval(integrationScript);

			// Convert the result to LaTeX
			String latexScript = "latexFunction = latex(" + function + ");";
			engine.eval(latexScript);

			// Retrieve the LaTeX expression
			Object latexFunction = engine.getVariable("latexFunction");
			latexExpr = "\\int_{" + min + "}^{" + max + "}" + latexFunction;

			// Now you can use the LaTeX expression
			System.out.println("Latex Expression: " + latexExpr);

		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
		double integralResult;
		try {
			integralResult = engine.getVariable("integralResult");
			setLatexFunction(latexExpr);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
		return integralResult;
	}

}
