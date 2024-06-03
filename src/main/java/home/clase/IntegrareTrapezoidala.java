package home.clase;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class IntegrareTrapezoidala extends Integrare {
    private static IntegrareTrapezoidala instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private IntegrareTrapezoidala() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static IntegrareTrapezoidala getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new IntegrareTrapezoidala();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda trapezelor
    public double integrate(String function, String min, String max, String plot_interval, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "f = @(x) " + integral_expr + "; ";
            integrationScript += "a = " + min + "; ";
            integrationScript += "b = " + max + "; ";
            integrationScript += "n = " + segments + "; ";
            integrationScript += "h = (b-a)/n; ";

            // Calcularea valorii integralei folosind metoda trapezelor
            integrationScript += "X = f(a)+f(b); ";
            integrationScript += "R=0; ";
            integrationScript += "for i = 1:1:n-1; xi=a+(i*h); R=R+f(xi); end; ";
            integrationScript += "integralResult = (h/2)*(X+2*R); ";

            // Generarea și plotarea funcției
            integrationScript += "x_values = linspace(a, b, n+1); ";
            integrationScript += "y_values = f(x_values); ";
            integrationScript += "fig = figure('Visible', 'off'); ";
            integrationScript += "plot(x_values, y_values, 'r', 'LineWidth', 1.5); hold on; ";

            // Plotarea trapezelor
            integrationScript += "loop = 1; ";
            integrationScript += "while loop <= length(x_values)-1 ";
            integrationScript += "    width = abs((b - a) / n); ";
            integrationScript += "    height_left = y_values(loop); ";
            integrationScript += "    height_right = y_values(loop+1); ";
            integrationScript += "    x_coords = [x_values(loop), x_values(loop+1), x_values(loop+1), x_values(loop)]; ";
            integrationScript += "    y_coords = [0, 0, height_right, height_left]; ";
            integrationScript += "    fill(x_coords, y_coords, 'green', 'FaceAlpha', 0.3); ";
            integrationScript += "    loop = loop + 1; ";
            integrationScript += "end; ";
            integrationScript += "hold on; ";

            // Plotarea funcției originale
            integrationScript += "x = " + min + ":" + plot_interval + ":" + max + "; ";
            integrationScript += "y = " + function + "; ";
            integrationScript += "plot(x, y, 'r', 'LineWidth', 1.5); ";

            // Adăugarea titlului și etichetelor
            integrationScript += "xlabel('x'); ";
            integrationScript += "ylabel('y'); ";
            integrationScript += "title('Integrare prin metoda trapezelor'); ";
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