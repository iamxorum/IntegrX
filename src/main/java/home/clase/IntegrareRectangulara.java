package home.clase;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import java.util.concurrent.ExecutionException;

public class IntegrareRectangulara extends Integrare {
    private static IntegrareRectangulara instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private IntegrareRectangulara() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static IntegrareRectangulara getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new IntegrareRectangulara();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind metoda dreptunghiulară mijloc
    public double integrate(String function, String min, String max, String plot_interval, String interval) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "";
            // Se calculează integrala folosind metoda dreptunghiulară mijloc
            integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
                "s = 0; for i = 0:n-1; xn = a + (i*h) + (h/2); s = s + f(xn); end; integralResult = h * s;" +
                "x_values = linspace(a, b, 1000); y_values = f(x_values);" + // Generate x and y values for plotting
                "fig = figure('Visible', 'off');" +
                "plot(x_values, y_values, 'r', 'LineWidth', 1.5); hold on;" + // Plot the function with a red line
                "for i = 0:n-1" +
                "    xn_mid = a + (i*h) + (h/2);" +
                "    height_mid = f(xn_mid);" +
                "    x_coords = [a + (i*h), a + (i*h) + h, a + (i*h) + h, a + (i*h)];" +
                "    y_coords = [0, 0, height_mid, height_mid];" +
                "    fill(x_coords, y_coords, 'cyan', 'FaceAlpha', 0.3);" + // Fill the rectangle
                "end;" +
                "hold on;" +
                "x = " + min + ":" + plot_interval + ":" + max + "; y = " + function + ";" + // Generate x and y values for plotting
                "plot(x, y, 'r', 'LineWidth', 1.5);" + // Plot the function in red
                "xlabel('x'); ylabel('y'); title('Integrare prin metoda dreptunghiulara mijloc'); grid on;" +
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

    // Metodă pentru calculul integralei folosind metoda dreptunghiulară stanga sau dreapta
    public double integrate(String function, String min, String max, String plot_interval, String interval, int type) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x");
            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = "(" + function + "), " + min + ", " + max;
            String integrationScript = "";
            if (type == 1) {
                // Se calculează integrala folosind metoda dreptunghiulară stanga
                integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
                        "s = 0; for i = 0:n-1; xn = a + (i*h); s = s + f(xn); end; integralResult = h * s;" +
                        "x_values = linspace(a, b, 1000); y_values = f(x_values);" + // Generate x and y values for plotting
                        "fig = figure('Visible', 'off');" +
                        "plot(x_values, y_values, 'r', 'LineWidth', 1.5); hold on;" + // Plot the function with a red line
                        "for i = 0:n-1" +
                        "    xn_left = a + (i*h);" +
                        "    height_left = f(xn_left);" +
                        "    x_coords = [xn_left, xn_left + h, xn_left + h, xn_left];" +
                        "    y_coords = [0, 0, height_left, height_left];" +
                        "    fill(x_coords, y_coords, 'cyan', 'FaceAlpha', 0.3);" + // Fill the rectangle
                        "end;" +
                        "hold on;" +
                        "x = " + min + ":" + plot_interval + ":" + max + "; y = " + function + ";" + // Generate x and y values for plotting
                        "plot(x, y, 'r', 'LineWidth', 1.5);" + // Plot the function in red
                        "xlabel('x'); ylabel('y'); title('Integrare prin metoda dreptunghiulara stanga'); grid on;" +
                        "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');" + // Save the figure as .fig
                        "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');"; // Save the figure as .png
                engine.eval(integrationScript);
            } else if (type == 0) {
                // Se calculează integrala folosind metoda dreptunghiulară dreapta
                integrationScript = "f = @(x) " + integral_expr + "; a = " + min + "; b = " + max + "; n = " + interval + "; h = (b-a)/n;" +
                        "s = 0; for i = 1:n; xn = a + (i*h); s = s + f(xn); end; integralResult = h * s;" +
                        "x_values = linspace(a, b, 1000); y_values = f(x_values);" + // Generate x and y values for plotting
                        "fig = figure('Visible', 'off');" +
                        "plot(x_values, y_values, 'r', 'LineWidth', 1.5); hold on;" + // Plot the function with a red line
                        "for i = 1:n" +
                        "    xn_right = a + (i*h);" +
                        "    height_right = f(xn_right);" +
                        "    x_coords = [xn_right - h, xn_right, xn_right, xn_right - h];" +
                        "    y_coords = [0, 0, height_right, height_right];" +
                        "    fill(x_coords, y_coords, 'cyan', 'FaceAlpha', 0.3);" + // Fill the rectangle
                        "end;" +
                        "hold on;" +
                        "x = " + min + ":" + plot_interval + ":" + max + "; y = " + function + ";" + // Generate x and y values for plotting
                        "plot(x, y, 'r', 'LineWidth', 1.5);" + // Plot the function in red
                        "xlabel('x'); ylabel('y'); title('Integrare prin metoda dreptunghiulara dreapta'); grid on;" +
                        "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');" + // Save the figure as .fig
                        "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');"; // Save the figure as .png
                engine.eval(integrationScript);
            }
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
