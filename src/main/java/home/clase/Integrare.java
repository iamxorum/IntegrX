package home.clase;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.interfete.Interfata_Integrare;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Integrare implements Interfata_Integrare {
	private static Integrare instance = null;
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
					// Change background color to #1f2d40
					"set(gcf, 'color', [0.1216 0.1765 0.2510]);\n" +
					"saveas(gcf,'./src/main/resources/Integrix/plots/funct_plot_1s.png')";

	// Constructor privat pentru a preveni instanțierea clasei
	protected Integrare() {
	}

	// Metodă statică pentru a obține unica instanță a clasei
	public static Integrare getInstance() throws EngineException, InterruptedException {
		// Dacă instanța este null, se creează o nouă instanță
		if (instance == null) {
			instance = new Integrare();
		}
		// Se returnează unica instanță
		return instance;
	}

	@Override
	public double shrinkDecimal(double value) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(6);
		// Verifică dacă valoarea este sub pragul de precizie
		if (value < Integrare.THRESHOLD) {
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
			Color newBlue = new Color(31, 45, 64);
			Color smokeWhite = new Color(250, 250, 250);
			formula.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/funct_latex.png", // Salvarea imaginii LaTeX
					newBlue,
					smokeWhite);
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
			// Definirea culorii de fundal pentru imaginea LaTeX cu #1f2d40
			Color newBlue = new Color(31, 45, 64);
			Color smokeWhite = new Color(250, 250, 250);
			formula.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/latexExpr_diff1.png", // Salvarea imaginii LaTeX pentru prima derivată
					newBlue,
					smokeWhite);

			TeXFormula formula2 = new TeXFormula(latexExpr_diff2);
			formula2.createPNG(TeXConstants.STYLE_DISPLAY,
					100,
					"./src/main/resources/Integrix/plots/latexExpr_diff2.png", // Salvarea imaginii LaTeX pentru a doua derivată
					newBlue,
					smokeWhite);
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