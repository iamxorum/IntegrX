package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class TrapezoidalIntegration extends Integration {
    private static TrapezoidalIntegration instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private TrapezoidalIntegration() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static TrapezoidalIntegration getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new TrapezoidalIntegration();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda trapezelor
    public double calculateTrapezoid(String function, String min, String max, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
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
            // Se obține rezultatul integralii din motorul MATLAB
            integralResult = engine.getVariable("integralResult");
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
        return integralResult;
    }
}