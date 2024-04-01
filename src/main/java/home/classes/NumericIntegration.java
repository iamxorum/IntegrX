package home.classes;

import com.mathworks.engine.*;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.util.concurrent.ExecutionException;

import static home.controllers.MainController.setLatexFunction;

public class NumericIntegration {
	private static NumericIntegration instance;

	private static String MATLAB_PLOT_SKELETON =
			"plot(skeleton)\n" +
					"xlabel('Z')\n" +
					"ylabel('function')\n" +
					"title('Plot of function')\n" +
					"saveas(gcf,'./src/main/resources/Integrix/plots/funct_plot_1s.png')"; // Save the plot as a PNG file
	MatlabEngine engine;

	// Private constructor to prevent instantiation from outside
	private NumericIntegration() throws EngineException, InterruptedException {
		engine = MatlabEngine.startMatlab();
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

	public double integrate(String function, String min, String max) throws MatlabExecutionException, MatlabSyntaxException {
		String latexExpr;
		try {
			String modifiedScript = MATLAB_PLOT_SKELETON.replace("skeleton", function)
					.replace("z", min + ":0.1:" + max)
					.replace("function", function)
					.replace("Z", "z");
			engine.eval(modifiedScript);
			engine.eval("syms z");
			String integral_expr = "(" + function + "), " + min + ", " + max;
			String integrationScript = "integralResult = integral(@(z) " + integral_expr + ");";
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
