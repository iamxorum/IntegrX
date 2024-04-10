package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class TrapezoidalIntegration extends Integration{
    private static TrapezoidalIntegration instance;

    // Private constructor to prevent instantiation of the class
    private TrapezoidalIntegration() {
    }

    // Static method to get the single instance of the class
    public static TrapezoidalIntegration getInstance() throws EngineException, InterruptedException {
        // If the instance is null, create a new instance
        if (instance == null) {
            instance = new TrapezoidalIntegration();
        }
        // Return the single instance
        return instance;
    }

    public double calculateTrapezoid(String function, String min, String max, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            engine.eval("syms x");
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + segments + "; h = (b-a)/n;" +
                    "X = f(a)+f(b); R=0; for i = 1:1:n-1; xi=a+(i*h); R=R+f(xi); end; integralResult = (h/2)*(X+2*R);";
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
