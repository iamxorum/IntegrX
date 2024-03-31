package home.classes;

import com.mathworks.engine.*;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.util.concurrent.ExecutionException;

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

	public double integrate(String function, String min, String max) {
		try {
			String modifiedScript = MATLAB_PLOT_SKELETON.replace("skeleton", function)
					.replace("z", min + ":0.1:" + max)
					.replace("function", function)
					.replace("Z", "z");
			System.out.println(modifiedScript);
			engine.eval(modifiedScript);
			String integrationScript = "integralResult = integral(@(z) (" + function + "), " + min + ", " + max + ");";
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
