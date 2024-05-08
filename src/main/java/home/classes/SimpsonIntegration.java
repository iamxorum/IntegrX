package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class SimpsonIntegration extends Integration {
    private static SimpsonIntegration instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private SimpsonIntegration() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static SimpsonIntegration getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new SimpsonIntegration();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda lui Simpson
    public double calculateSimpson(String function, String min, String max, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
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
