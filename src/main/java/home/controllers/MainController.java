package home.controllers;

import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.IntegrX;
import home.classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainController {
	@FXML private ToggleButton rectangularButton;
	@FXML private ToggleButton trapezoidalButton;
	@FXML private ToggleButton simpsonButton;
	@FXML private ToggleButton get_started_btn;
	@FXML private Button start;
	@FXML private ToggleButton calculation_form_btn;
	@FXML private ToggleButton result_btn;
	@FXML private ToggleButton properties_btn;
	@FXML private AnchorPane calculation_form;
	@FXML private AnchorPane result_window;
	@FXML private AnchorPane properties_window;
	@FXML private AnchorPane get_started;
	@FXML private TextField plot_interval_id;
	@FXML private TextField func_id;
	@FXML private TextField min_id;
	@FXML private TextField max_id;
	@FXML private TextField int_id;
	@FXML private Label real_integral;
	@FXML private Label method_integral;
	@FXML private Label abs_err;
	@FXML private ImageView latex_integral;
	@FXML private ImageView plot_function;
	@FXML private ImageView plot_function2;
	@FXML private Text method_result_name;
	@FXML private HBox error_div;
	@FXML private HBox method_div;
	@FXML private HBox integral_div;
	@FXML private TextField abs_err_input;
	@FXML private Label isDiv_Conv2;
	@FXML private Label isDiv_Conv3;
	@FXML private ImageView latex_integral_prop;
	@FXML private ImageView latexFunction_diff1;
	@FXML private ImageView latexFunction_diff2;
	@FXML private Button zoom;

	@FXML
	private void initialize() {
		ToggleGroup toggleGroup1 = new ToggleGroup();
		rectangularButton.setToggleGroup(toggleGroup1);
		trapezoidalButton.setToggleGroup(toggleGroup1);
		simpsonButton.setToggleGroup(toggleGroup1);

		ToggleGroup toggleGroup2 = new ToggleGroup();
		get_started_btn.setToggleGroup(toggleGroup2);
		calculation_form_btn.setToggleGroup(toggleGroup2);
		result_btn.setToggleGroup(toggleGroup2);
		properties_btn.setToggleGroup(toggleGroup2);

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

		// Implicitly toggle the get_started_btn
		get_started_btn.setSelected(true);

		// Call the method to handle the visibility of the AnchorPane
		handleCalculationFormVisibility();
		handleResultWindowVisibility();
		handlePropertiesWindowVisibility();
		handleStartedWindowVisibility();

		calculation_form_btn.setOnAction(event -> {
			// Toggle the visibility of the AnchorPane when the button is clicked
			handleCalculationFormVisibility();
		});

		get_started_btn.setOnAction(event -> {
			// Toggle the visibility of the AnchorPane when the button is clicked
			handleStartedWindowVisibility();
		});

		result_btn.setOnAction(event -> {
			// Toggle the visibility of the AnchorPane when the button is clicked
			handleResultWindowVisibility();
		});

		properties_btn.setOnAction(event -> {
			// Toggle the visibility of the AnchorPane when the button is clicked
			handlePropertiesWindowVisibility();
		});

		// Add listener to toggleGroup2 selectedToggleProperty
		toggleGroup2.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			if (newToggle != null) {
				// If a new ToggleButton is selected, hide the AnchorPane
				get_started.setVisible(false);
				calculation_form.setVisible(false);
				result_window.setVisible(false);
				properties_window.setVisible(false);
			}
		});
		// Event listener to prevent un-toggling
		toggleGroup2.selectedToggleProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (isNowSelected == null) {
				wasSelected.setSelected(true);
			}
		});
	}

	private void handleCalculationFormVisibility() {
		// Toggle the visibility of the AnchorPane based on the state of calculation_form_btn
		calculation_form.setVisible(calculation_form_btn.isSelected());
	}

	private void handleStartedWindowVisibility() {
		// Toggle the visibility of the AnchorPane based on the state of calculation_form_btn
		get_started.setVisible(get_started_btn.isSelected());
	}

	private void handleResultWindowVisibility() {
		// Toggle the visibility of the AnchorPane based on the state of calculation_form_btn
		result_window.setVisible(result_btn.isSelected());
	}

	private void handlePropertiesWindowVisibility() {
		// Toggle the visibility of the AnchorPane based on the state of calculation_form_btn
		properties_window.setVisible(properties_btn.isSelected());
	}

	@FXML
	protected void onCloseButtonClicked(ActionEvent event) {
		System.exit(0);
	}

	public void showLatexIntegralProp() {
		Image image = latex_integral_prop.getImage(); // Assuming latex_integral_prop is accessible here
		openImage(image);
	}

	public void showLatexFunctionDiff1() {
		Image image = latexFunction_diff1.getImage(); // Assuming latex_integral_prop is accessible here
		openImage(image);
	}

	public void showLatexFunctionDiff2() {
		Image image = latexFunction_diff2.getImage(); // Assuming latex_integral_prop is accessible here
		openImage(image);
	}

	public void showLatexIntegral() {
		Image image = latex_integral.getImage(); // Assuming latex_integral_prop is accessible here
		openImage(image);
	}

	public void showPlotFunction() {
		Image image = plot_function.getImage(); // Assuming latex_integral_prop is accessible here
		Image image2 = plot_function2.getImage(); // Assuming latex_integral_prop is accessible here
		openImage(image);
		openImage(image2);
	}

	public void openImage(Image image) {
		// Open as a pop-up window
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
		get_started.setVisible(false);
		calculation_form.setVisible(true);
		calculation_form.setDisable(false);
		calculation_form_btn.setVisible(true);
		calculation_form_btn.setDisable(false);
		calculation_form_btn.setSelected(true);
		handleCalculationFormVisibility();
	}

	public void calculateAction() throws ExecutionException, InterruptedException {
		String function = func_id.getText();
		String min = min_id.getText();
		String max = max_id.getText();
		String interval = int_id.getText();
		String plot_interval = plot_interval_id.getText();
		String abs_err_usr = abs_err_input.getText();

		// Define a map to store the conversions
		Map<String, String> conversionMap = new HashMap<>();
		conversionMap.put("pi", String.valueOf(Math.PI));
		conversionMap.put("e", String.valueOf(Math.E));
		conversionMap.put("-pi", String.valueOf(-Math.PI));
		conversionMap.put("phi", String.valueOf((1 + Math.sqrt(5)) / 2));
		conversionMap.put("-phi", String.valueOf(-(1 + Math.sqrt(5)) / 2));
		conversionMap.put("Inf", "Inf");
		conversionMap.put("-Inf", "-Inf");

		ErrorHandling errorHandling = null;
		try {
			errorHandling = ErrorHandling.getInstance();
		} catch (Exception e) {
			errorHandling.showAlert("Error", e.getMessage());
			return;
		}

		errorHandling.handleEmptyFields(function, min, max, interval, abs_err_usr, plot_interval);
		errorHandling.handleRange(min, max);
		if(errorHandling.handleRangeInf(min, max)) return;
		if(!rectangularButton.isSelected() && !trapezoidalButton.isSelected() && !simpsonButton.isSelected()) {
			errorHandling.showAlert("Error", "Please select a method.");
			return;
		}

		// Update min and max if they match any key in the map
		if (conversionMap.containsKey(min)) {
			min = conversionMap.get(min);
		}
		if (conversionMap.containsKey(max)) {
			max = conversionMap.get(max);
		}

		double result = 0; // Initialize the result variable
		Integration ni = null;
		RectangularIntegration ri = null;
		TrapezoidalIntegration ti = null;
		SimpsonIntegration si = null;
		try {
			ni = Integration.getInstance();
			ri = RectangularIntegration.getInstance();
			ti = TrapezoidalIntegration.getInstance();
			si = SimpsonIntegration.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String isDivergent = ni.isDivergent(function);
		isDiv_Conv2.setVisible(true);
		isDiv_Conv2.setDisable(false);
		isDiv_Conv2.setText(isDivergent);
		isDiv_Conv3.setText(isDivergent);
		if (isDivergent.equals("The function is divergent")) {
			isDiv_Conv2.setStyle("-fx-background-color: #cc2424");
			isDiv_Conv3.setStyle("-fx-background-color: #cc2424");
		} else {
			isDiv_Conv2.setStyle("-fx-background-color: #315981");
			isDiv_Conv3.setStyle("-fx-background-color: #315981");
		}
		ni.plotting(function, min, max, plot_interval);
		ni.diff_latex(function);
		latex_integral.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_latex.png"));
		latex_integral_prop.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_latex.png"));
		latexFunction_diff1.setImage(new Image("file:./src/main/resources/Integrix/plots/latexExpr_diff1.png"));
		latexFunction_diff2.setImage(new Image("file:./src/main/resources/Integrix/plots/latexExpr_diff2.png"));
		plot_function.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_plot_1s.png"));

		try {
			result = ni.integrate(function, min, max);
		} catch (Exception e) {
			integral_div.setVisible(false);
			method_div.setVisible(false);
			error_div.setVisible(false);
			return;
		}
		integral_div.setVisible(true);
		method_div.setVisible(true);
		error_div.setVisible(true);

		real_integral.setText(String.valueOf(result));
		if (rectangularButton.isSelected()) {
			result = ri.calculateRectangular(function, min, max, interval);
			ni.setPlot_method(0);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("RECTANGULAR METHOD");
		} else if (simpsonButton.isSelected()) {
			ni.setPlot_method(1);
			result = si.calculateSimpson(function, min, max, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("SIMPSON METHOD");
		} else if (trapezoidalButton.isSelected()) {
			ni.setPlot_method(2);
			result = ti.calculateTrapezoid(function, min, max, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("TRAPEZOID METHOD");
		}
		ni.method_plotting(function, min, max, plot_interval, interval);
		plot_function2.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_plot_2s.png"));
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

		//Toggle the result window button
		result_btn.setVisible(true);
		result_btn.setDisable(false);
		result_btn.setSelected(true);
		result_window.setVisible(true);
		properties_btn.setVisible(true);
		properties_btn.setDisable(false);
	}


}
