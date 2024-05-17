package home.classes;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.interfaces.Integration_Interface;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Integration implements Integration_Interface {
	private static Integration instance = null;
	private int plot_method = 0;

	// Metodă pentru a obține metoda de plotare
	public int getPlot_method() {
		return plot_method;
	}

	// Metodă pentru a seta metoda de plotare
	public void setPlot_method(int plot_method) {
		this.plot_method = plot_method;
	}

	// Șablonul pentru graficul MATLAB simplu
	private static String MATLAB_PLOT_SKELETON =
			"fig = figure('Visible', 'off');\n" +
					"plot(x,y)\n" +
					"xlabel('x')\n" +
					"ylabel('y')\n" +
					"saveas(gcf,'./src/main/resources/Integrix/plots/funct_plot_1s.png')";

	// Șablonul pentru graficul MATLAB cu metoda de integrare evidențiată
	private static String MATLAB_PLOT_SKELETON_METHOD =
					"%s" +  // Spațiu rezervat pentru comenzile de afișare a zonei
					"hold off;\n" +
					"saveas(gcf, './src/main/resources/Integrix/plots/funct_plot_2s.png');\n";

	// Constructor privat pentru a preveni instanțierea clasei
	protected Integration() {
	}

	// Metodă statică pentru a obține unica instanță a clasei
	public static Integration getInstance() throws EngineException, InterruptedException {
		// Dacă instanța este null, se creează o nouă instanță
		if (instance == null) {
			instance = new Integration();
		}
		// Se returnează unica instanță
		return instance;
	}

	@Override
	public double shrinkDecimal(double value) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(6);
		// Verifică dacă valoarea este sub pragul de precizie
		if (value < Integration.THRESHOLD) {
			df.setGroupingUsed(false);
		}
		// Returnează valoarea cu numărul de zecimale redus conform formaterului
		return Double.parseDouble(df.format(value));
	}

	@Override
	public String isDivergent(String function) throws ExecutionException, InterruptedException {
		// Se evaluează simbolul 'x' în motorul MATLAB
		engine.eval("syms x");
		// Se elimină punctele din funcție pentru a evita erorile de sintaxă
		String functionWithoutDots = function.replaceAll("\\.", "");
		// Se evaluează funcția în motorul MATLAB
		engine.eval("fct = " + functionWithoutDots + ";");
		// Se calculează suma simbolică a funcției
		engine.eval("y = vpa(symsum(fct, x, 1, inf));");
		// Se verifică dacă rezultatul este infinit
		engine.eval("check = isinf(y);");
		// Se obține rezultatul verificării
		Object check = engine.getVariable("check");
		// Se afișează rezultatul verificării în consolă (pentru debug)
		System.out.println(check.toString());
		// Se returnează mesajul corespunzător în funcție de rezultatul verificării
		if (check.toString().equals("true")) {
			return "Funcția este divergentă";
		} else {
			return "Funcția este convergentă";
		}
	}

	@Override
	public void method_plotting(String function, String min, String max, String plot_interval, String interval, int type) throws ExecutionException, InterruptedException {
		engine.eval("clear;"); // Șterge orice obiecte sau grafice existente în mediul de lucru MATLAB
		String area_plotting_code = ""; // Inițializează șirul de caractere pentru codul de plasare a ariei sub grafic
		engine.eval("x = " + min + ":" + plot_interval + ":" + max + ";"); // Definirea punctelor x
		engine.eval("y = " + function + ";");
		engine.eval("fig = figure('Visible', 'off');"); // Creează o nouă figură
		engine.eval("hold on;"); // Păstrează graficul pe aceeași figură
		engine.eval("plot(x, y, 'Color', [1 0 0]);"); // Plasează graficul funcției
		engine.eval("xlabel('x');"); // Eticheta axei x
		engine.eval("ylabel('y');"); // Eticheta axei y

		// Evaluarea expresiilor MATLAB pentru generarea punctelor x și y
		engine.eval("x = " + min + ":((" + max + "-" + min + ")/" + interval + ")" + ":" + max + ";");
		engine.eval("y = " + function + ";");

		// Selectarea metodei de plasare a ariei sub grafic
		switch (plot_method) {
			case 0:  // Metoda Rectangular
				if (type == 0) {
					area_plotting_code = "loop = 1;\n" +
							"% Define the width of each rectangle\n" +
							"width = abs((" + max + " - " + min + ") / " + interval + ");\n" +
							"% Loop through each interval\n" +
							"while loop <= length(x) - 1\n" +
							"    % Calculate the midpoint of the current interval\n" +
							"    midpoint = (x(loop) + x(loop + 1)) / 2;\n" +
							"    % Calculate the height of the rectangle at the midpoint\n" +
							"    height = " + function.replace("x", "midpoint") + ";\n" +
							"    % Define the vertices of the rectangle\n" +
							"    x_coords = [x(loop), x(loop) + width, x(loop) + width, x(loop)];\n" +
							"    y_coords = [0, 0, height, height];\n" +
							"    % Create a polygon representing the rectangle\n" +
							"    rect_poly = polyshape(x_coords, y_coords);\n" +
							"    % Plot the rectangle\n" +
							"    plot(rect_poly, 'FaceColor', [0 0.4470 0.7410]);\n" +
							"    hold on;\n" +
							"    % Increment the loop counter\n" +
							"    loop = loop + 1;\n" +
							"end;\n" +
							"hold off;\n";
				} else if (type == 1) {
					area_plotting_code = "loop = 1;\n" +
							"% Execută bucla până când loop este mai mic decât lungimea lui x minus 1\n" +
							"while loop <= length(x)-1\n" +
							"    % Calculează lățimea\n" +
							"    width = abs((" + max + " - " + min + ") / " + interval + ");\n" +
							"    % Calculează înălțimea\n" +
							"    height = y(loop);\n" +
							"    % Definirea vârfurilor dreptunghiului\n" +
							"    x_coords = [x(loop), x(loop) + width, x(loop) + width, x(loop)];\n" +
							"    y_coords = [0, 0, height, height];\n" +
							"    % Creează un poligon reprezentând dreptunghiul\n" +
							"    rect_poly = polyshape(x_coords, y_coords);\n" +
							"    % Plasează dreptunghiul\n" +
							"    plot(rect_poly, 'FaceColor', [0 0.4470 0.7410]);\n" +
							"    hold on;\n" +
							"    % Incrementarea contorului buclei\n" +
							"    loop = loop + 1;\n" +
							"end;\n" +
							"hold off;\n";
				} else if (type == 2){
					area_plotting_code = "loop = 1;\n" +
							"% Execută bucla până când loop este mai mic decât lungimea lui x minus 1\n" +
							"while loop <= length(x)-1\n" +
							"    % Calculează lățimea\n" +
							"    width = abs((" + max + " - " + min + ") / " + interval + ");\n" +
							"    % Calculează înălțimea\n" +
							"    height = y(loop + 1);\n" +
							"    % Definirea vârfurilor dreptunghiului\n" +
							"    x_coords = [x(loop), x(loop) + width, x(loop) + width, x(loop)];\n" +
							"    y_coords = [0, 0, height, height];\n" +
							"    % Creează un poligon reprezentând dreptunghiul\n" +
							"    rect_poly = polyshape(x_coords, y_coords);\n" +
							"    % Plasează dreptunghiul\n" +
							"    plot(rect_poly, 'FaceColor', [0 0.4470 0.7410]);\n" +
							"    hold on;\n" +
							"    % Incrementarea contorului buclei\n" +
							"    loop = loop + 1;\n" +
							"end;\n" +
							"hold off;\n";
				}
				break;
			case 1:  // Metoda Simpson
				area_plotting_code = "func = @(x) " + function + ";\n" +
						"    x = linspace(" + min + ", " + max + ", " + interval + ");\n" +
						"    a = " + min + ";\n" +
						"    b = " + max + ";\n" +
						"    n = " + interval + ";\n" +
						"    x_nodes = linspace(a, b, n+1);\n" +
						"    y_nodes = func(x_nodes);\n" +
						"    Int = 0;\n" +
						"    for i = 1:n+1\n" +
						"        L = @(x) 1;\n" +
						"        for j = 1:n+1\n" +
						"            if j ~= i\n" +
						"                L = @(x) L(x) .* (x - x_nodes(j)) / (x_nodes(i) - x_nodes(j));\n" +
						"            end\n" +
						"        end\n" +
						"        Int_L = integral(L, a, b);\n" +
						"        Int = Int + y_nodes(i) * Int_L;\n" +
						"    end\n" +
						"    x_coords = linspace(a, b, 1000);\n" +
						"    y_coords = arrayfun(@(x) func(x), x_coords);\n" +
						"    plot(x_coords, y_coords, 'Color', [0 1 0]);\n" +
						"    hold on;\n" +
						"    scatter(x_nodes, y_nodes, 'r', 'filled', 'MarkerFaceColor', [0 0.4470 0.7410]);\n" +
						"    hold off;\n";
				break;
			case 2:  // Metoda Trapezoidal
				area_plotting_code = "loop = 1;\n" +
						"% Execută bucla până când loop este mai mic decât lungimea lui x minus 1\n" +
						"while loop <= length(x)-1\n" +
						"    % Calculează lățimea\n" +
						"    width = abs((" + max + " - " + min + ") / " + interval + ");\n" +
						"    % Calculează înălțimile punctelor din stânga și din dreapta\n" +
						"    height_left = y(loop);\n" +
						"    height_right = y(loop+1);\n" +
						"    % Definirea vârfurilor trapezului\n" +
						"    x_coords = [x(loop), x(loop+1), x(loop+1), x(loop)];\n" +
						"    y_coords = [0, 0, height_right, height_left];\n" +
						"    % Plasează trapezul\n" +
						"    plot(polyshape(x_coords, y_coords), 'FaceColor', [0 0.4470 0.7410]);\n" +
						"    hold on;\n" +
						"    % Incrementarea contorului buclei\n" +
						"    loop = loop + 1;\n" +
						"end;\n" +
						"hold off;\n";
				break;
		}

		// Creează scriptul de plasare a ariei sub grafic și îl evaluează folosind engine.eval()
		String plottingScript = String.format(MATLAB_PLOT_SKELETON_METHOD, area_plotting_code);
		engine.eval(plottingScript);
	}

	@Override
	public void plotting(String function, String min, String max, String plot_interval) throws ExecutionException, InterruptedException {
		String latexExpr;
		try {
			engine.eval("clear;"); // Șterge orice obiecte sau grafice existente în mediul de lucru MATLAB
			engine.eval("x = " + min + ":" + plot_interval + ":" + max + ";"); // Definirea punctelor x
			engine.eval("y = " + function + ";"); // Evaluarea funcției pentru punctele x date
			engine.eval(MATLAB_PLOT_SKELETON); // Plasarea graficului

			engine.eval("syms x"); // Definirea lui x ca simbol

			// Convertirea rezultatului în LaTeX
			String latexScript = "latexFunction = latex(" + function + ");";
			engine.eval(latexScript);

			// Obținerea expresiei LaTeX
			Object latexFunction = engine.getVariable("latexFunction");
			latexExpr = "\\int_{" + min + "}^{" + max + "}" + latexFunction; // Construirea expresiei LaTeX pentru integrala definită

			TeXFormula formula = new TeXFormula(latexExpr);
			Color smokeWhite = new Color(250, 250, 250);
			formula.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/funct_latex.png", // Salvarea imaginii LaTeX
					smokeWhite,
					Color.RED);
		} catch (MatlabExecutionException | MatlabSyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void diff_latex(String function) throws ExecutionException, InterruptedException {
		String latexExpr_diff1;
		String latexExpr_diff2;
		try {
			engine.eval("syms x"); // Definirea lui x ca simbol
			engine.eval("df1 = diff(" + function + ", x);"); // Calculul primei derivate
			engine.eval("df2 = diff(df1, x);"); // Calculul celei de-a doua derivate

			// Convertirea rezultatelor în LaTeX
			String latexScript_diff1 = "latexFunction_diff1 = latex(df1);";
			engine.eval(latexScript_diff1);
			String latexScript_diff2 = "latexFunction_diff2 = latex(df2);";
			engine.eval(latexScript_diff2);

			// Obținerea expresiilor LaTeX
			Object latexFunction = engine.getVariable("latexFunction_diff1");
			latexExpr_diff1 = latexFunction.toString();
			Object latexFunction2 = engine.getVariable("latexFunction_diff2");
			latexExpr_diff2 = latexFunction2.toString();

			// Construirea expresiilor LaTeX pentru derivate
			TeXFormula formula = new TeXFormula(latexExpr_diff1);
			Color smokeWhite = new Color(250, 250, 250);
			formula.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/latexExpr_diff1.png", // Salvarea imaginii LaTeX pentru prima derivată
					smokeWhite,
					Color.RED);

			TeXFormula formula2 = new TeXFormula(latexExpr_diff2);
			formula2.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/latexExpr_diff2.png", // Salvarea imaginii LaTeX pentru a doua derivată
					smokeWhite,
					Color.RED);
		} catch (MatlabExecutionException | MatlabSyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	public double integrate(String function, String min, String max) throws MatlabExecutionException, MatlabSyntaxException {
		try {
			engine.eval("syms x"); // Definirea lui x ca simbol
			// Formatarea corectă a expresiei de integrare pentru a pasa funcția și limitele
			String integral_expr = function + ", " + min + ", " + max;
			String integrationScript = "integralResult = integral(@(x) " + integral_expr + ");";
			engine.eval(integrationScript);

		} catch (InterruptedException | ExecutionException ex) {
			throw new RuntimeException("Eroare în timpul executării MATLAB: " + ex.getMessage(), ex);
		}

		double integralResult;
		try {
			integralResult = ((Double) engine.getVariable("integralResult")).doubleValue(); // Obținerea rezultatului integralei
		} catch (InterruptedException | ExecutionException ex) {
			throw new RuntimeException("Nu s-a reușit obținerea rezultatului integralei: " + ex.getMessage(), ex);
		}
		return integralResult;
	}
}
