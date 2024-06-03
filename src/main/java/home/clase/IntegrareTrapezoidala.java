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
            String integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + segments + "; h = (b-a)/n;" +
                    "X = f(a)+f(b); R=0; for i = 1:1:n-1; xi=a+(i*h); R=R+f(xi); end; integralResult = (h/2)*(X+2*R);" +
                    "x_values = linspace(a, b, n+1); y_values = f(x_values);" + // Generate x and y values for plotting
                    "fig = figure('Visible', 'off');" +
                    "plot(x_values, y_values, 'r', 'LineWidth', 1.5); hold on;" + // Plot the function in red
                    "loop = 1;" +
                    "while loop <= length(x_values)-1" +
                    "    width = abs((b - a) / n);" +
                    "    height_left = y_values(loop);" +
                    "    height_right = y_values(loop+1);" +
                    "    x_coords = [x_values(loop), x_values(loop+1), x_values(loop+1), x_values(loop)];" +
                    "    y_coords = [0, 0, height_right, height_left];" +
                    "    fill(x_coords, y_coords, 'green', 'FaceAlpha', 0.3);" +
                    "    loop = loop + 1;" +
                    "end;" +
                    "hold on;" +
                    "x = " + min + ":" + plot_interval + ":" + max + "; y = " + function + ";" + // Generate x and y values for plotting
                    "plot(x, y, 'r', 'LineWidth', 1.5);" + // Plot the function in red
                    "xlabel('x'); ylabel('y'); title('Integrare prin metoda trapezelor'); grid on;" +
                    "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');" + // Save the figure as .fig
                    "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');"; // Save the figure as .png
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