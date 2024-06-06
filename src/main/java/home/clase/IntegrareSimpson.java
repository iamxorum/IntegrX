package home.clase;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class IntegrareSimpson extends Integrare {
    private static IntegrareSimpson instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private IntegrareSimpson() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static IntegrareSimpson getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new IntegrareSimpson();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda lui Simpson
    public double integrate(String function, String min, String max, String plot_interval, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + "), " + min + ", " + max;
            // Definirea funcției de integrare și a intervalului de integrare
            String integrationScript = "f = @(x) " + integral_expr + "; ";
            integrationScript += "a = " + min + "; ";
            integrationScript += "b = " + max + "; ";
            integrationScript += "n = " + segments + "; ";
            integrationScript += "h = (b-a)/n; ";

            // Calcularea valorii integralei folosind metoda Simpson 1/3
            integrationScript += "X = f(a) + f(b); ";
            integrationScript += "Odd = 0; ";
            integrationScript += "Even = 0; ";
            integrationScript += "for i = 1:2:n-1; xi = a + (i*h); Odd = Odd + f(xi); end; ";
            integrationScript += "for i = 2:2:n-2; xi = a + (i*h); Even = Even + f(xi); end; ";
            integrationScript += "integralResult = (h/3)*(X + 4*Odd + 2*Even); ";

            // Generarea și plotarea funcției
            integrationScript += "x = linspace(a, b, 1000); ";
            integrationScript += "y = f(x); ";
            integrationScript += "fig = figure('Visible', 'off'); ";
            integrationScript += "plot(x, y, 'b'); hold on; ";

            // Plotarea segmentelor de interpolare pentru metoda Simpson 1/3
            integrationScript += "for i = 0:2:n-2; ";
            integrationScript += "x_segment = linspace(a + i*h, a + (i+2)*h, 100); ";
            integrationScript += "y_segment = interp1([a + i*h, a + (i+1)*h, a + (i+2)*h], [f(a + i*h), f(a + (i+1)*h), f(a + (i+2)*h)], x_segment, 'pchip'); ";
            integrationScript += "plot(x_segment, y_segment, 'b', 'LineWidth', 1.5); ";
            integrationScript += "patch([x_segment, fliplr(x_segment)], [zeros(size(y_segment)), fliplr(y_segment)], 'r', 'FaceAlpha', 0.3, 'EdgeColor', 'none'); end; ";
            integrationScript += "hold on; ";

            // Plotarea funcției originale
            integrationScript += "x = " + min + ":" + plot_interval + ":" + max + "; ";
            integrationScript += "y = " + function + "; ";
            integrationScript += "plot(x, y, 'r', 'LineWidth', 1.5); ";
            integrationScript += "hold off; ";

            // Adăugarea titlului și etichetelor
            integrationScript += "title('Integrare prin metoda lui Simpson 1/3'); ";
            integrationScript += "xlabel('x'); ";
            integrationScript += "ylabel('y'); ";
            integrationScript += "grid on; ";

            // Salvarea figurii ca .fig și .png
            integrationScript += "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig'); ";
            integrationScript += "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png'); ";

            engine.eval(integrationScript);
        } catch (InterruptedException | ExecutionException ex) {
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
