package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class SimpsonIntegration extends Integration {
    private static SimpsonIntegration instance = null;

    // Private constructor to prevent instantiation of the class
    private SimpsonIntegration() {
    }

    // Static method to get the single instance of the class
    public static SimpsonIntegration getInstance() throws EngineException, InterruptedException {
        // If the instance is null, create a new instance
        if (instance == null) {
            instance = new SimpsonIntegration();
        }
        // Return the single instance
        return instance;
    }

    public double calculateSimpson(String function, String min, String max, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            engine.eval("syms x");
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + segments + "; h = (b-a)/n;" +
                    "X = f(a)+f(b); Odd = 0; Even = 0; for i = 1:2:n-1; xi=a+(i*h); Odd=Odd+f(xi); end; for i = 2:2:n-2; xi=a+(i*h); Even=Even+f(xi); end; integralResult = (h/3)*(X+4*Odd+2*Even);";
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
