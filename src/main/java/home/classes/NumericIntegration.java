package home.classes;

import com.mathworks.engine.*;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.util.concurrent.ExecutionException;

public class NumericIntegration {
	private static NumericIntegration instance;

	private static final String MATLAB_PLOT_SCRIPT =
			"plot(sin(-10:0.1:10))\n" +
					"xlabel('x')\n" +
					"ylabel('sin(x)')\n" +
					"title('Plot of sin(x)')\n" +
					"saveas(gcf,'plot.png')"; // Save the plot as a PNG file
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
			engine.eval(MATLAB_PLOT_SCRIPT);
			engine.eval("integralResult = integral(@(x) " + function + ", " + min + ", " + max + ")");
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
