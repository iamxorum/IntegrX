package home.clase;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class IntegrareQGL extends Integrare {
    private static IntegrareQGL instance = null;

    // Constructor privat pentru a preveni instanțierea clasei
    private IntegrareQGL() {
    }

    // Metodă statică pentru a obține unica instanță a clasei
    public static IntegrareQGL getInstance() throws EngineException, InterruptedException {
        // Dacă instanța este null, se creează o nouă instanță
        if (instance == null) {
            instance = new IntegrareQGL();
        }
        // Se returnează unica instanță
        return instance;
    }

    // Metodă pentru calculul integralei folosind quadratura Gauss-Legendre
    public double integrate(String function, String min, String max, String plot_interval, String segments) throws MatlabExecutionException, MatlabSyntaxException {
        try {
            if (!Objects.equals(min, "-1") || !Objects.equals(max, "1")) {
                throw new IllegalArgumentException("Intervalul de integrare pentru metoda Gauss-Legendre trebuie să fie [-1, 1].");
            }
            // Se evaluează simbolul 'x' în motorul MATLAB
            engine.eval("syms x;");

            // Se definește expresia integrală și scriptul de integrare
            String integral_expr = function;
            engine.eval("f = @(x) " + integral_expr + ";");
            engine.eval("n = " + segments + ";");

            String integrationScript = "p(1,1)=1; p(2,1:2)=[1 0];";
            integrationScript += "for k=2:n; p(k+1,1:k+1)=((2*k-1)*[p(k,1:k) 0]-(k-1)*[0 0 p(k-1,1:k-1)])/k; end;";
            integrationScript += "x = roots(p(n+1,:));";

            integrationScript += "a = " + min + "; b = " + max + ";";
            integrationScript += "x=((b-a)./2).*x+(b+a)./2;";
            integrationScript += "dx=(b-a)./2;";
            integrationScript += "y = feval(f, x) * dx;";

            integrationScript += "pn = polyder(p(n+1,:));";
            integrationScript += "C = zeros(1, n);"; // Ensure C is preallocated
            integrationScript += "for i = 1:n;";
            integrationScript += "C(i) = 2./((1 - x(i).^2).*((polyval(pn, x(i))).^2));";
            integrationScript += "end;";

            integrationScript += "integralResult = dot(C, y);"; // Use y instead of G

            // Generate x and y values for plotting
            integrationScript += "x_values = linspace(" + min + ", " + max + ", 1/" + plot_interval + ");";
            integrationScript += "y_values = feval(f, x_values);";

            integrationScript += "fig = figure('Visible', 'off');";
            integrationScript += "plot(x_values, y_values, 'r', 'LineWidth', 1); hold on;";

            // Plot the Gauss-Legendre points
            integrationScript += "for i = 1:n;";
            integrationScript += "plot(x(i), feval(f, x(i)), 'bo', 'MarkerSize', 10);";
            integrationScript += "end;";

            integrationScript += "xlabel('x'); ylabel('y');";
            integrationScript += "title('Integrare prin quadratura Gauss-Legendre');";
            integrationScript += "grid on;";

            // Save the figure
            integrationScript += "savefig(fig, './src/main/resources/Integrix/plots/funct_plot_2s.fig');";
            integrationScript += "saveas(fig, './src/main/resources/Integrix/plots/funct_plot_2s.png');";

            engine.eval(integrationScript);
        } catch (InterruptedException | ExecutionException ex) {
            TratareErori tratareErori = TratareErori.getInstance();
            tratareErori.showAlert("Eroare", "A apărut o eroare la calcularea integralei.\n" + ex.getMessage());
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            TratareErori tratareErori = TratareErori.getInstance();
            tratareErori.showAlert("Eroare", ex.getMessage());
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
