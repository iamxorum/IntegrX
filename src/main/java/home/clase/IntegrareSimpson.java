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
            String integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + segments + "; h = (b-a)/n;" +
                    "X = f(a) + f(b); Odd = 0; Even = 0; for i = 1:2:n-1; xi = a + (i*h); Odd = Odd + f(xi); end; for i = 2:2:n-2; xi = a + (i*h); Even = Even + f(xi); end; integralResult = (h/3)*(X + 4*Odd + 2*Even);" +
                    "x = linspace(a, b, 1000); y = f(x); fig = figure('Visible', 'off'); plot(x, y, 'b'); hold on; " +
                    "for i = 0:2:n-2; x_segment = linspace(a + i*h, a + (i+2)*h, 100); " +
                    "y_segment = interp1([a + i*h, a + (i+1)*h, a + (i+2)*h], [f(a + i*h), f(a + (i+1)*h), f(a + (i+2)*h)], x_segment, 'pchip'); " +
                    // plot the line segment blue
                    "plot(x_segment, y_segment, 'b', 'LineWidth', 1.5); " +
                    "patch([x_segment, fliplr(x_segment)], [zeros(size(y_segment)), fliplr(y_segment)], 'r', 'FaceAlpha', 0.3, 'EdgeColor', 'none'); end; " +
                    "hold on;" +
                    "x = " + min + ":" + plot_interval + ":" + max + "; y = " + function + ";" + // Generate x and y values for plotting
                    "plot(x, y, 'r', 'LineWidth', 1.5);" + // Plot the function in red
                    "hold off; title('Integrare prin metoda lui Simpson 1/3'); xlabel('x'); ylabel('y'); grid on;" +
                    "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');" + // Save the figure as .fig
                    "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');"; // Save the figure as .png
            engine.eval(integrationScript);
        } catch (InterruptedException | ExecutionException ex) {
            TratareErori tratareErori = TratareErori.getInstance();
            tratareErori.showAlert("Eroare", "A apărut o eroare la calcularea integralei.\n" + ex.getMessage());
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
