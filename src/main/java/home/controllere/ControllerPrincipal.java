package home.controllere;

import com.mathworks.engine.MatlabEngine;
import home.clase.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ControllerPrincipal {
	// Buton pentru selectarea metodei de integrare: dreptunghiulară
	@FXML private ToggleButton rectangularButton;

	// Buton pentru selectarea metodei de integrare: dreptunghiulară
	@FXML private ToggleButton rectangularButton_lft;

	// Buton pentru selectarea metodei de integrare: dreptunghiulară
	@FXML private ToggleButton rectangularButton_rgt;

	// Buton pentru selectarea metodei de integrare: trapezoidală
	@FXML private ToggleButton trapezoidalButton;

	// Buton pentru selectarea metodei de integrare: Simpson
	@FXML private ToggleButton simpsonButton;

	@FXML private ToggleButton RK_Button;

	// Buton pentru începerea procesului
	@FXML private ToggleButton get_started_btn;

	// Buton pentru pornirea procesului de integrare
	@FXML private Button start;

	// Buton pentru deschiderea formularului de calcul
	@FXML private ToggleButton calculation_form_btn;

	// Buton pentru deschiderea ferestrei de rezultate
	@FXML private ToggleButton result_btn;

	// Buton pentru deschiderea ferestrei de proprietăți
	@FXML private ToggleButton properties_btn;

	// Panou pentru formularul de calcul
	@FXML private AnchorPane calculation_form;

	// Panou pentru fereastra de rezultate
	@FXML private AnchorPane result_window;

	// Panou pentru fereastra de proprietăți
	@FXML private AnchorPane properties_window;

	// Panou pentru începutul procesului
	@FXML private AnchorPane get_started;

	// Câmp pentru intervalul de afișare pe grafic
	@FXML private TextField plot_interval_id;

	// Câmp pentru funcția de integrat
	@FXML private TextField func_id;

	// Câmp pentru limita inferioară a intervalului de integrare
	@FXML private TextField min_id;

	// Câmp pentru limita superioară a intervalului de integrare
	@FXML private TextField max_id;

	// Câmp pentru intervalul de divizare (în cazul metodei Simpson)
	@FXML private TextField int_id;

	// Etichetă pentru valoarea integrală reală
	@FXML private Label real_integral;

	// Etichetă pentru valoarea integrală calculată prin metodă
	@FXML private Label method_integral;

	// Etichetă pentru eroarea absolută
	@FXML private Label abs_err;

	// Etichetă pentru eroarea relativă
	@FXML private Label rel_err;

	// Imagine pentru reprezentarea integrala în format LaTeX
	@FXML private ImageView latex_integral;

	// Imagine pentru graficul funcției
	@FXML private ImageView plot_function;

	// Imagine pentru al doilea grafic al funcției
	@FXML private ImageView plot_function2;

	// Text pentru numele metodei de integrare folosite
	@FXML private Text method_result_name;

	// Container pentru afișarea erorii
	@FXML private HBox error_div;

	// Container pentru afișarea metodei de integrare folosite
	@FXML private HBox method_div;

	// Container pentru afișarea valorii integrale
	@FXML private HBox integral_div;

	// Câmp pentru introducerea erorii absolute
	@FXML private TextField abs_err_input;

	// Etichetă pentru informația dacă funcția este convergentă sau divergentă
	@FXML private Label isDiv_Conv2;

	// Etichetă pentru informația dacă funcția este convergentă sau divergentă (altă variantă)
	@FXML private Label isDiv_Conv3;

	// Imagine pentru reprezentarea integralei în format LaTeX (proprietăți)
	@FXML private ImageView latex_integral_prop;

	// Imagine pentru derivata primei funcții în format LaTeX
	@FXML private ImageView latexFunction_diff1;

	// Imagine pentru derivata a doua a funcției în format LaTeX
	@FXML private ImageView latexFunction_diff2;

	// Buton pentru a afisa imaginea cu integrala
	@FXML private Button zoom;

	@FXML private VBox sideDash;

	@FXML
	private void initialize() {
		// Grupuri de butoane pentru a asigura ca doar un buton din fiecare grup este selectat la un moment dat
		ToggleGroup toggleGroup1 = new ToggleGroup();
		rectangularButton.setToggleGroup(toggleGroup1);
		trapezoidalButton.setToggleGroup(toggleGroup1);
		simpsonButton.setToggleGroup(toggleGroup1);
		rectangularButton_lft.setToggleGroup(toggleGroup1);
		rectangularButton_rgt.setToggleGroup(toggleGroup1);
		RK_Button.setToggleGroup(toggleGroup1);

		ToggleGroup toggleGroup2 = new ToggleGroup();
		get_started_btn.setToggleGroup(toggleGroup2);
		calculation_form_btn.setToggleGroup(toggleGroup2);
		result_btn.setToggleGroup(toggleGroup2);
		properties_btn.setToggleGroup(toggleGroup2);

		// Ascunderea și dezactivarea inițială a anumitor elemente de interfață
		isDiv_Conv2.setVisible(false);
		isDiv_Conv2.setDisable(true);

		get_started.setDisable(false);
		get_started.setVisible(true);
		result_btn.setVisible(false);
		result_btn.setDisable(true);
		properties_btn.setVisible(false);
		properties_btn.setDisable(true);
		calculation_form_btn.setVisible(false);
		calculation_form_btn.setDisable(true);

		// Selectarea implicită a butonului de începere
		get_started_btn.setSelected(true);

		// Apelul metodelor pentru gestionarea vizibilității AnchorPane-urilor
		handleCalculationFormVisibility();
		handleResultWindowVisibility();
		handlePropertiesWindowVisibility();
		handleStartedWindowVisibility();

		// Adăugarea de acțiuni pentru butoanele de comutare
		calculation_form_btn.setOnAction(event -> {
			animateButton(calculation_form_btn);
			handleCalculationFormVisibility();
		});

		get_started_btn.setOnAction(event -> {
			animateButton(get_started_btn);
			handleStartedWindowVisibility();
		});

		result_btn.setOnAction(event -> {
			animateButton(result_btn);
			handleResultWindowVisibility();
		});

		properties_btn.setOnAction(event -> {
			animateButton(properties_btn);
			handlePropertiesWindowVisibility();
		});

		// Adăugarea unui ascultător pentru proprietatea selectedToggleProperty a toggleGroup2
		toggleGroup2.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			if (newToggle != null) {
				// Dacă este selectat un nou ToggleButton, ascunde AnchorPane-ul
				get_started.setVisible(false);
				calculation_form.setVisible(false);
				result_window.setVisible(false);
				properties_window.setVisible(false);
			}
		});

		// Ascultător pentru a preveni deselectarea butoanelor de comutare
		toggleGroup2.selectedToggleProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (isNowSelected == null) {
				wasSelected.setSelected(true);
			}
		});
	}

	private void animateButton(ToggleButton button) {
		ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
		st.setAutoReverse(true);
		st.setCycleCount(2);
		st.play();
	}

	private void animatePageChange(Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(300), node);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}

	private void handleCalculationFormVisibility() {
		calculation_form.setVisible(calculation_form_btn.isSelected());
		if (calculation_form_btn.isSelected()) {
			animatePageChange(calculation_form);
		}
	}

	private void handleStartedWindowVisibility() {
		get_started.setVisible(get_started_btn.isSelected());
		if (get_started_btn.isSelected()) {
			animatePageChange(get_started);
		}
	}

	private void handleResultWindowVisibility() {
		result_window.setVisible(result_btn.isSelected());
		if (result_btn.isSelected()) {
			animatePageChange(result_window);
		}
	}

	private void handlePropertiesWindowVisibility() {
		properties_window.setVisible(properties_btn.isSelected());
		if (properties_btn.isSelected()) {
			animatePageChange(properties_window);
		}
	}

	@FXML
	protected void onCloseButtonClicked(ActionEvent event) {
		// Acțiunea de închidere a aplicației
		System.exit(0);
	}

	public void showLatexIntegralProp() {
		// Metodă pentru afișarea imaginii integralei în format LaTeX (proprietăți)
		Image image = latex_integral_prop.getImage(); // Presupunând că latex_integral_prop este accesibil aici
		openImage(image);
	}

	public void showLatexFunctionDiff1() {
		// Metodă pentru afișarea imaginii primei derivate a funcției în format LaTeX
		Image image = latexFunction_diff1.getImage(); // Presupunând că latex_integral_prop este accesibil aici
		openImage(image);
	}

	public void showLatexFunctionDiff2() {
		// Metodă pentru afișarea imaginii celei de-a doua derivate a funcției în format LaTeX
		Image image = latexFunction_diff2.getImage(); // Presupunând că latex_integral_prop este accesibil aici
		openImage(image);
	}

	public void showLatexIntegral() {
		// Metodă pentru afișarea imaginii integralei în format LaTeX
		Image image = latex_integral.getImage(); // Presupunând că latex_integral este accesibil aici
		openImage(image);
	}

	public void showPlotFunction() throws ExecutionException, InterruptedException {
		Matlab_MultiThread multiThread = Matlab_MultiThread.getInstance();
		MatlabEngine engine = multiThread.getEngine();

		// Open the figure file
		engine.eval("openfig('./src/main/resources/Integrix/plots/funct_plot_2s.fig');");

		// Bring the figure to the front
		engine.eval("figure(gcf);");
	}

	public void openImage(Image image) {
		// Metodă pentru afișarea imaginii într-o fereastră pop-up
		Stage stage = new Stage();
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(imageView);
		scrollPane.setPannable(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		Scene scene = new Scene(scrollPane);
		stage.setScene(scene);
		stage.show();
	}

	public void getStarted() {
		// Metodă pentru începerea programului
		get_started.setVisible(false);
		// delete get_started_btn from sideDash
		sideDash.getChildren().remove(get_started_btn);
		calculation_form.setVisible(true);
		calculation_form.setDisable(false);
		calculation_form_btn.setVisible(true);
		calculation_form_btn.setDisable(false);
		calculation_form_btn.setSelected(true);
		handleCalculationFormVisibility();
	}

	public void calculateAction() throws ExecutionException, InterruptedException {
		// Obține valorile introduse în câmpuri
		String function = func_id.getText();
		String min = min_id.getText();
		String max = max_id.getText();
		String interval = int_id.getText();
		String plot_interval = plot_interval_id.getText();
		String abs_err_usr = abs_err_input.getText();

		// Definirea unui map pentru a stoca conversiile
		Map<String, String> conversionMap = new HashMap<>();
		conversionMap.put("pi", String.valueOf(Math.PI));
		conversionMap.put("e", String.valueOf(Math.E));
		conversionMap.put("-pi", String.valueOf(-Math.PI));
		conversionMap.put("phi", String.valueOf((1 + Math.sqrt(5)) / 2));
		conversionMap.put("-phi", String.valueOf(-(1 + Math.sqrt(5)) / 2));
		conversionMap.put("Inf", "Inf");
		conversionMap.put("-Inf", "-Inf");

		// Actualizarea lui min și max dacă sunt egale cu o cheie din map
		if (conversionMap.containsKey(min)) {
			min = conversionMap.get(min);
		}
		if (conversionMap.containsKey(max)) {
			max = conversionMap.get(max);
		}

		TratareErori tratareErori = null;
		try {
			tratareErori = TratareErori.getInstance();
		} catch (Exception e) {
			// Afișează o alertă în caz de eroare
			tratareErori.showAlert("Eroare", e.getMessage());
			return;
		}

		// Tratează cazurile în care unul dintre câmpuri este gol
		if (tratareErori.handleEmptyFields(function, min, max, interval, abs_err_usr, plot_interval)) {
			return;
		}

		// Verifică dacă intervalul de integrare este valid
		if (tratareErori.handleRange(min, max)) {
			return;
		}

		if (tratareErori.handleSingleVariableFunction(function)) {
			return;
		}

		// Verifică dacă a fost selectată o metodă de integrare
		if (!rectangularButton.isSelected() && !trapezoidalButton.isSelected() && !simpsonButton.isSelected() && !rectangularButton_lft.isSelected() && !rectangularButton_rgt.isSelected() && !RK_Button.isSelected()){
			tratareErori.showAlert("Eroare", "Vă rugăm să selectați o metodă.");
			return;
		}

		double result = 0; // Inițializează variabila de rezultat
		Integrare ni = null;
		IntegrareRectangulara ri = null;
		IntegrareTrapezoidala ti = null;
		IntegrareSimpson si = null;
		IntegrareRungeKutta rk = null;
		// catch: stop the execution and show an alert with the error message from tratareErori instance if an exception occurs
		try {
			// Inițializează obiectele pentru integrare
			ni = Integrare.getInstance();
			ri = IntegrareRectangulara.getInstance();
			ti = IntegrareTrapezoidala.getInstance();
			si = IntegrareSimpson.getInstance();
			rk = IntegrareRungeKutta.getInstance();
		} catch (Exception e) {
			// Afișează o alertă în caz de eroare
			tratareErori.showAlert("Eroare", e.getMessage());
			return;
		}

		// Realizează afișarea graficului și a derivatei funcției
		try {
			ni.plotting(function, min, max, plot_interval);
			ni.diff_latex(function);
		} catch (Exception e) {
			// Afișează o alertă în caz de eroare
			tratareErori.showAlert("Eroare", "A apărut o eroare la generarea graficului.\n" + e.getMessage());
			return;
		}
		latex_integral.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_latex.png"));
		latex_integral_prop.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_latex.png"));
		latexFunction_diff1.setImage(new Image("file:./src/main/resources/Integrix/plots/latexExpr_diff1.png"));
		latexFunction_diff2.setImage(new Image("file:./src/main/resources/Integrix/plots/latexExpr_diff2.png"));
		plot_function.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_plot_1s.png"));

		try {
			// Calculează integrala
			result = ni.integrate(function, min, max);
		} catch (Exception e) {
			// Tratează cazul în care apare o excepție și ascunde diviziunile
			integral_div.setVisible(false);
			method_div.setVisible(false);
			error_div.setVisible(false);
			return;
		}

		// Verifică dacă funcția este divergentă și actualizează vizibilitatea și stilul corespunzător
		String isDivergent = ni.isDivergent(function);
		isDiv_Conv2.setVisible(true);
		isDiv_Conv2.setDisable(false);
		isDiv_Conv2.setText(isDivergent);
		isDiv_Conv3.setText(isDivergent);
		if (isDivergent.equals("Integrala este divergentă")) {
			isDiv_Conv2.setStyle("-fx-background-color: #cc2424");
			isDiv_Conv3.setStyle("-fx-background-color: #cc2424");
		} else {
			isDiv_Conv2.setStyle("-fx-background-color: #315981");
			isDiv_Conv3.setStyle("-fx-background-color: #315981");
		}

		// Afisează diviziunile
		integral_div.setVisible(true);
		method_div.setVisible(true);
		error_div.setVisible(true);

		// Afișează rezultatul integralii
		real_integral.setText(String.valueOf(result));

		int type = 2; // 2 = mijloc, 1 = stanga, 0 = dreapta
		// Selectează și calculează integrala folosind metoda corespunzătoare
		if (rectangularButton.isSelected() || rectangularButton_lft.isSelected() || rectangularButton_rgt.isSelected()){
			if (rectangularButton.isSelected()) {
				method_result_name.setText("METODA DREPTUNGHIULARĂ MIJLOC");
			} else if (rectangularButton_lft.isSelected()) {
				method_result_name.setText("METODA DREPTUNGHIULARĂ STÂNGĂ");
				type = 1;
			} else if (rectangularButton_rgt.isSelected()) {
				method_result_name.setText("METODA DREPTUNGHIULARĂ DREAPTĂ");
				type = 0;
			}
			if(type == 2) {
				result = ri.integrate(function, min, max, plot_interval, interval);
			} else {
				result = ri.integrate(function, min, max, plot_interval, interval, type);
			}
			method_integral.setText(String.valueOf(result));
		} else if (simpsonButton.isSelected()) {
			result = si.integrate(function, min, max, plot_interval, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("METODA SIMPSON");
		} else if (trapezoidalButton.isSelected()) {
			result = ti.integrate(function, min, max, plot_interval, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("METODA TRAPEZOID");
		} else if (RK_Button.isSelected()) {
			result = rk.integrate(function, min, max, plot_interval, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("METODA RUNGE-KUTTA");
		}

		// Realizează afișarea graficului pentru metoda folosită
		plot_function2.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_plot_2s.png"));

		// Calculează eroarea absolută și afișează
		double absolute_error;
		if (result > Double.parseDouble(real_integral.getText())) {
			absolute_error = result - Double.parseDouble(real_integral.getText());
		} else {
			absolute_error = Double.parseDouble(real_integral.getText()) - result;
		}
		if (absolute_error > Double.parseDouble(abs_err_usr)) {
			error_div.setStyle("-fx-background-color: #cc2424");
		} else {
			error_div.setStyle("-fx-background-color: #318169");
		}
		abs_err.setText(String.valueOf(absolute_error));
		// rel_err witrh respect to real integral (treated with absolute value)
		rel_err.setText((Math.abs(absolute_error / Double.parseDouble(real_integral.getText())) * 100) + "%");

		// Activează butonul pentru afișarea rezultatului și a ferestrei de proprietăți

		result_btn.setVisible(true);
		result_btn.setDisable(false);
		result_btn.setSelected(true);
		handleResultWindowVisibility();
		properties_btn.setVisible(true);
		properties_btn.setDisable(false);
	}

}
