package home.clase;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class IntegrareRungeKutta extends Integrare {
    private static IntegrareRungeKutta instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private IntegrareRungeKutta() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static IntegrareRungeKutta getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new IntegrareRungeKutta();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda trapezelor
    public double integrate(String function, String min, String max, String plot_interval, String segments) {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + ")";
            // Inițializează scriptul de integrare
            String integrationScript = "x0 = " + min + ";";
            integrationScript += " y0 = 0;";
            integrationScript += " x_end = " + max + ";";
            integrationScript += " segments = " + segments + ";";

            // Definește funcția pe care vrei să o integrezi
            integrationScript += " f = @(x, y) " + integral_expr + ";";
            integrationScript += " h = (x_end - x0) / segments;";

            // Inițializează vectorii pentru stocarea valorilor calculate
            integrationScript += " x_values = x0;";
            integrationScript += " y_values = y0;";
            integrationScript += " x = x0;";
            integrationScript += " y = y0;";

            // Bucle pentru calculul RK4
            integrationScript += " while x < x_end";
            integrationScript += "    k1 = h * f(x, y);";
            integrationScript += "    k2 = h * f(x + h / 2, y + k1 / 2);";
            integrationScript += "    k3 = h * f(x + h / 2, y + k2 / 2);";
            integrationScript += "    k4 = h * f(x + h, y + k3);";
            integrationScript += "    y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;";
            integrationScript += "    x = x + h;";
            integrationScript += "    x_values = [x_values, x];";
            integrationScript += "    y_values = [y_values, y];";
            integrationScript += " end;";
            // Crearea și configurarea figurii pentru plotare
            integrationScript += " fig = figure('Visible', 'off');";
            integrationScript += " plot(x_values, y_values);";
            integrationScript += " xlabel('x');";
            integrationScript += " ylabel('y');";
            integrationScript += " title('Solution of dy/dx = " + function + " using RK4');";
            integrationScript += " grid on;";
            integrationScript += " hold on;";

            // Adăugarea unei zone colorate
            integrationScript += " x_fill = [x_values, fliplr(x_values)];";
            integrationScript += " y_fill = [y_values, zeros(size(y_values))];";
            integrationScript += " fill(x_fill, y_fill, 'yellow', 'FaceAlpha', 0.3);";
            integrationScript += " integralResult = y;";
            integrationScript += " hold on;";

            // Plotare funcției inițiale
            integrationScript += " x = " + min + ":" + plot_interval + ":" + max + ";";
            integrationScript += " y = " + function + ";";
            integrationScript += " plot(x, y, 'r', 'LineWidth', 1.5);";

            // Finalizarea plotării
            integrationScript += " hold off;";

            // Salvarea graficului
            integrationScript += " savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');";
            integrationScript += " saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');";
            engine.eval(integrationScript);
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Nu s-a reușit calculul integralei: " + ex.getMessage(), ex);
        }
        double integralResult;
        try {
            // Se obține rezultatul integralii din motorul MATLAB
            integralResult = engine.getVariable("integralResult");
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Nu s-a reușit obținerea rezultatului integralei: " + ex.getMessage(), ex);
        }
        return integralResult;
    }
}