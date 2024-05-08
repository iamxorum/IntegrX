package home.controllers;

import home.classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainController {
	// Buton pentru selectarea metodei de integrare: dreptunghiulară
	@FXML private ToggleButton rectangularButton;

	// Buton pentru selectarea metodei de integrare: trapezoidală
	@FXML private ToggleButton trapezoidalButton;

	// Buton pentru selectarea metodei de integrare: Simpson
	@FXML private ToggleButton simpsonButton;

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

	@FXML
	private void initialize() {
		// Grupuri de butoane pentru a asigura ca doar un buton din fiecare grup este selectat la un moment dat
		ToggleGroup toggleGroup1 = new ToggleGroup();
		rectangularButton.setToggleGroup(toggleGroup1);
		trapezoidalButton.setToggleGroup(toggleGroup1);
		simpsonButton.setToggleGroup(toggleGroup1);

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
			// Gestionarea vizibilității AnchorPane-ului când butonul este apăsat
			handleCalculationFormVisibility();
		});

		get_started_btn.setOnAction(event -> {
			// Gestionarea vizibilității AnchorPane-ului când butonul este apăsat
			handleStartedWindowVisibility();
		});

		result_btn.setOnAction(event -> {
			// Gestionarea vizibilității AnchorPane-ului când butonul este apăsat
			handleResultWindowVisibility();
		});

		properties_btn.setOnAction(event -> {
			// Gestionarea vizibilității AnchorPane-ului când butonul este apăsat
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

	private void handleCalculationFormVisibility() {
		// Comută vizibilitatea AnchorPane-ului în funcție de starea butonului calculation_form_btn
		calculation_form.setVisible(calculation_form_btn.isSelected());
	}

	private void handleStartedWindowVisibility() {
		// Comută vizibilitatea AnchorPane-ului în funcție de starea butonului get_started_btn
		get_started.setVisible(get_started_btn.isSelected());
	}

	private void handleResultWindowVisibility() {
		// Comută vizibilitatea AnchorPane-ului în funcție de starea butonului result_btn
		result_window.setVisible(result_btn.isSelected());
	}

	private void handlePropertiesWindowVisibility() {
		// Comută vizibilitatea AnchorPane-ului în funcție de starea butonului properties_btn
		properties_window.setVisible(properties_btn.isSelected());
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

	public void showPlotFunction() {
		// Metodă pentru afișarea imaginii graficului funcției
		Image image = plot_function.getImage(); // Presupunând că plot_function este accesibil aici
		Image image2 = plot_function2.getImage(); // Presupunând că plot_function2 este accesibil aici
		openImage(image);
		openImage(image2);
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

		ErrorHandling errorHandling = null;
		try {
			errorHandling = ErrorHandling.getInstance();
		} catch (Exception e) {
			// Afișează o alertă în caz de eroare
			errorHandling.showAlert("Eroare", e.getMessage());
			return;
		}

		// Tratează cazurile în care unul dintre câmpuri este gol
		if (errorHandling.handleEmptyFields(function, min, max, interval, abs_err_usr, plot_interval)) {
			return;
		}

		// Verifică dacă intervalul de integrare este valid
		if (errorHandling.handleRange(min, max)) {
			return;
		}

		// Verifică dacă a fost selectată o metodă de integrare
		if (!rectangularButton.isSelected() && !trapezoidalButton.isSelected() && !simpsonButton.isSelected()) {
			errorHandling.showAlert("Eroare", "Vă rugăm să selectați o metodă.");
			return;
		}

		double result = 0; // Inițializează variabila de rezultat
		Integration ni = null;
		RectangularIntegration ri = null;
		TrapezoidalIntegration ti = null;
		SimpsonIntegration si = null;
		try {
			// Inițializează obiectele pentru integrare
			ni = Integration.getInstance();
			ri = RectangularIntegration.getInstance();
			ti = TrapezoidalIntegration.getInstance();
			si = SimpsonIntegration.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Verifică dacă funcția este divergentă și actualizează vizibilitatea și stilul corespunzător
		String isDivergent = ni.isDivergent(function);
		isDiv_Conv2.setVisible(true);
		isDiv_Conv2.setDisable(false);
		isDiv_Conv2.setText(isDivergent);
		isDiv_Conv3.setText(isDivergent);
		if (isDivergent.equals("Funcția este divergentă")) {
			isDiv_Conv2.setStyle("-fx-background-color: #cc2424");
			isDiv_Conv3.setStyle("-fx-background-color: #cc2424");
		} else {
			isDiv_Conv2.setStyle("-fx-background-color: #315981");
			isDiv_Conv3.setStyle("-fx-background-color: #315981");
		}

		// Realizează afișarea graficului și a derivatei funcției
		ni.plotting(function, min, max, plot_interval);
		ni.diff_latex(function);
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

		// Afisează diviziunile
		integral_div.setVisible(true);
		method_div.setVisible(true);
		error_div.setVisible(true);

		// Afișează rezultatul integralii
		real_integral.setText(String.valueOf(result));

		// Selectează și calculează integrala folosind metoda corespunzătoare
		if (rectangularButton.isSelected()) {
			result = ri.calculateRectangular(function, min, max, interval);
			ni.setPlot_method(0);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("METODA DREPTUNGHIULARĂ");
		} else if (simpsonButton.isSelected()) {
			ni.setPlot_method(1);
			result = si.calculateSimpson(function, min, max, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("METODA SIMPSON");
		} else if (trapezoidalButton.isSelected()) {
			ni.setPlot_method(2);
			result = ti.calculateTrapezoid(function, min, max, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("METODA TRAPEZOID");
		}

		// Realizează afișarea graficului pentru metoda folosită
		ni.method_plotting(function, min, max, plot_interval, interval);
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

		// Activează butonul pentru afișarea rezultatului și a ferestrei de proprietăți
		result_btn.setVisible(true);
		result_btn.setDisable(false);
		result_btn.setSelected(true);
		result_window.setVisible(true);
		properties_btn.setVisible(true);
		properties_btn.setDisable(false);
	}
}
