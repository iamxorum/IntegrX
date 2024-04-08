package home.classes;

import com.mathworks.engine.*;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.IntegrX;

import java.util.concurrent.ExecutionException;

import static home.controllers.MainController.setLatexFunction;

public class NumericIntegration {
	private static NumericIntegration instance;

	private static String MATLAB_PLOT_SKELETON =
			"fig = figure('Visible', 'off');\n" +
					"plot(skeleton)\n" +
					"xlabel('x')\n" +
					"ylabel('y')\n" +
					"saveas(gcf,'./src/main/resources/Integrix/plots/funct_plot_1s.png')"; // Save the plot as a PNG file
	private MatlabEngine engine = IntegrX.getEngine();

	// Private constructor to prevent instantiation of the class
	private NumericIntegration() {
	}

	// Static method to get the single instance of the class
	public static NumericIntegration getInstance() throws EngineException, InterruptedException {
		// If the instance is null, create a new instance
		if (instance == null) {
			instance = new NumericIntegration();
		}
		// Return the single instance
		return instance;
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

	public double calculateRectangular(String function, String min, String max, String interval) throws MatlabExecutionException, MatlabSyntaxException {
		try {
			engine.eval("syms x");
			String integral_expr = "(" + function + "), " + min + ", " + max;
			String integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; h = " + interval + "; n = (b-a)/h;" +
					"s = 0; for i = 0:n-1; xn = a + (i*h); s = s + f(xn); end; integralResult = h * s;";
			engine.eval(integrationScript);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
		double integralResult;
		try {
			integralResult = engine.getVariable("integralResult");
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
		return integralResult;
	}


}
