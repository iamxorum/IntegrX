package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class RectangularIntegration extends Integration {
    private static RectangularIntegration instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private RectangularIntegration() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static RectangularIntegration getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new RectangularIntegration();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda dreptunghiulară
    public double calculateRectangular(String function, String min, String max, String interval, int type) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "";
            if (type == 1) {
                // Se calculează integrala folosind metoda dreptunghiulară stanga
                integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
                        "s = 0; for i = 0:n-1; xn = a + (i*h); s = s + f(xn); end; integralResult = h * s;";
                engine.eval(integrationScript);
            } else if (type == 0) {
                // Se calculează integrala folosind metoda dreptunghiulară mijloc
                integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
                        "s = 0; for i = 0:n-1; xn = a + (i*h) + (h/2); s = s + f(xn); end; integralResult = h * s;";
                engine.eval(integrationScript);
            } else if (type == 2) {
                // Se calculează integrala folosind metoda dreptunghiulară dreapta
                integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
                        "s = 0; for i = 0:n-1; xn = a + (i*h) + h; s = s + f(xn); end; integralResult = h * s;";
                engine.eval(integrationScript);
            }
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
