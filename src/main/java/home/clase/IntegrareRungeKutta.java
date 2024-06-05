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
    public double calculateRK(String function, String min, String max, String plot_interval, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + ")";
            String integrationScript = "x0 = " + min + "; y0 = 0; x_end = " + max + "; segments = " + segments + ";" +
                    "f = @(x, y) " + integral_expr + "; h = (x_end - x0) / segments;" +
                    "x_values = x0; y_values = y0; x = x0; y = y0;" +
                    "while x < x_end" +
                    "    k1 = h * f(x, y);" +
                    "    k2 = h * f(x + h / 2, y + k1 / 2);" +
                    "    k3 = h * f(x + h / 2, y + k2 / 2);" +
                    "    k4 = h * f(x + h, y + k3);" +
                    "    y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;" +
                    "    x = x + h;" +
                    "    x_values = [x_values, x];" +
                    "    y_values = [y_values, y];" +
                    "end;" +
                    "fig = figure('Visible', 'off');" +
                    "plot(x_values, y_values);" +
                    "xlabel('x');" +
                    "ylabel('y');" +
                    "title('Solution of dy/dx = " + function + " using RK4');" +
                    "grid on;" +
                    "hold on;" +
                    "x_fill = [x_values, fliplr(x_values)];" +
                    "y_fill = [y_values, zeros(size(y_values))];" +
                    "fill(x_fill, y_fill, 'yellow', 'FaceAlpha', 0.3);" +
                    "integralResult = y;" +
                    "hold on;" +
                    "x = " + min + ":" + plot_interval + ":" + max + "; y = " + function + ";" + // Generate x and y values for plotting
                    "plot(x, y, 'r', 'LineWidth', 1.5);" + // Plot the function in red
                    "hold off;" +
                    "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');" +
                    "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');";
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