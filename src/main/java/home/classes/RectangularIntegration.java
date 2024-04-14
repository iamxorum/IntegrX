package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class RectangularIntegration extends Integration {
    private static RectangularIntegration instance = null;

    // Private constructor to prevent instantiation of the class
    private RectangularIntegration() {
    }

    // Static method to get the single instance of the class
    public static RectangularIntegration getInstance() throws EngineException, InterruptedException {
        // If the instance is null, create a new instance
        if (instance == null) {
            instance = new RectangularIntegration();
        }
        // Return the single instance
        return instance;
    }

    public double calculateRectangular(String function, String min, String max, String interval) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            engine.eval("syms x");
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
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
