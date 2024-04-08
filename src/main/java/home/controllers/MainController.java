package home.controllers;

import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;
import home.IntegrX;
import home.classes.NumericIntegration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;

import javafx.scene.text.Text;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.swing.*;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainController {
	@FXML private ToggleButton rectangularButton;
	@FXML private ToggleButton trapezoidalButton;
	@FXML private ToggleButton simpsonButton;
	@FXML private ToggleButton get_started_btn;
	@FXML private ToggleButton calculation_form_btn;
	@FXML private ToggleButton result_btn;
	@FXML private AnchorPane calculation_form;
	@FXML private AnchorPane result_window;
	@FXML private TextField plot_interval_id;
	@FXML private TextField func_id;
	@FXML private TextField min_id;
	@FXML private TextField max_id;
	@FXML private TextField int_id;
	@FXML private Label real_integral;
	@FXML private Label method_integral;
	@FXML private Label abs_err;
	@FXML private ImageView latex_integral1;
	@FXML private ImageView plot_function;
	@FXML private Text method_result_name;

	private static String latexFunction = "";

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

		// Implicitly toggle the get_started_btn
		get_started_btn.setSelected(true);

		// Call the method to handle the visibility of the AnchorPane
		handleCalculationFormVisibility();
		handleResultWindowVisibility();

		calculation_form_btn.setOnAction(event -> {
			// Toggle the visibility of the AnchorPane when the button is clicked
			handleCalculationFormVisibility();
		});

		result_btn.setOnAction(event -> {
			// Toggle the visibility of the AnchorPane when the button is clicked
			handleResultWindowVisibility();
		});

		// Add listener to toggleGroup2 selectedToggleProperty
		toggleGroup2.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			if (newToggle != null) {
				// If a new ToggleButton is selected, hide the AnchorPane
				calculation_form.setVisible(false);
				result_window.setVisible(false);
			}
		});
	}

	public static void setLatexFunction(String latexFunction) {
		Objects.requireNonNull(latexFunction);
		MainController.latexFunction = latexFunction;
	}

	private void handleCalculationFormVisibility() {
		// Toggle the visibility of the AnchorPane based on the state of calculation_form_btn
		calculation_form.setVisible(calculation_form_btn.isSelected());
	}

	private void handleResultWindowVisibility() {
		// Toggle the visibility of the AnchorPane based on the state of calculation_form_btn
		result_window.setVisible(result_btn.isSelected());
	}



	@FXML
	protected void onCloseButtonClicked(ActionEvent event) {
		System.exit(0);
	}

	public void calculateAction(ActionEvent actionEvent) throws MatlabExecutionException, MatlabSyntaxException {
		String function = func_id.getText();
		String min = min_id.getText();
		String max = max_id.getText();
		String interval = int_id.getText();
		String plot_interval = plot_interval_id.getText();

		// Define a map to store the conversions
		Map<String, String> conversionMap = new HashMap<>();
		conversionMap.put("pi", String.valueOf(Math.PI));
		conversionMap.put("e", String.valueOf(Math.E));
		conversionMap.put("-pi", String.valueOf(-Math.PI));
		conversionMap.put("phi", String.valueOf((1 + Math.sqrt(5)) / 2));
		conversionMap.put("-phi", String.valueOf(-(1 + Math.sqrt(5)) / 2));
		conversionMap.put("Inf", "Inf");
		conversionMap.put("-Inf", "-Inf");

		// Update min and max if they match any key in the map
		if (conversionMap.containsKey(min)) {
			min = conversionMap.get(min);
		}
		if (conversionMap.containsKey(max)) {
			max = conversionMap.get(max);
		}


		if (function.isEmpty() || min_id.getText().isEmpty() || max_id.getText().isEmpty()) {
			showAlert("Empty Fields", "All fields must be filled.");
			return;
		}
		if (int_id.getText().isEmpty()) {
			int_id.setText("0");
		}
		if ("Inf".equals(max) && !("-Inf".equals(min)) && !("Inf".equals(min)) && "-Inf".equals(max)) {
			if (Double.parseDouble(min) >= Double.POSITIVE_INFINITY) {
				showAlert("Invalid Range", "The minimum value must be less than positive infinity.");
				return;
			}
		} else if ("-Inf".equals(min) && !("Inf".equals(max)) && !("-Inf".equals(max)) && "-Inf".equals(min)){
			if (Double.parseDouble(max) <= Double.NEGATIVE_INFINITY) {
				showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
				return;
			}
		} else if (!("Inf".equals(min)) && !("-Inf".equals(max)) && !("Inf".equals(max)) && !("-Inf".equals(min)) ){
			if (Double.parseDouble(min) > Double.parseDouble(max)) {
				showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
				return;
			} else if (Double.parseDouble(min) == Double.parseDouble(max)) {
				showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
				return;
			}
		} else if ("-Inf".equals(max)) {
			showAlert("Invalid Range", "The maximum value must be higher than negative infinity.");
			return;
		} else if ("Inf".equals(min)) {
			showAlert("Invalid Range", "The minimum value must be less than positive infinity.");
			return;
		} else if ("Inf".equals(min) && "Inf".equals(max)) {
			showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
			return;
		} else if ("-Inf".equals(min) && "-Inf".equals(max)) {
			showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
			return;
		} else if ("-Inf".equals(min) && "Inf".equals(max)) {
			Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
			confirmationDialog.setTitle("Wide Range Detected");
			confirmationDialog.setHeaderText(null);
			confirmationDialog.setContentText("The range is too wide. Do you want to continue?");

			confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			ButtonType userChoice = confirmationDialog.showAndWait().orElse(ButtonType.NO);

			if (userChoice == ButtonType.NO) {
				return;
			}
		}

		double result = 0; // Initialize the result variable
		NumericIntegration ni = null;
		try {
			ni = NumericIntegration.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = ni.integrate(function, min, max, plot_interval);
		TeXFormula formula = new TeXFormula(latexFunction);
		Color smokeWhite = new Color(250, 250, 250);
		formula.createPNG(TeXConstants.STYLE_DISPLAY,
				100,
				"./src/main/resources/Integrix/plots/funct_latex.png",
				smokeWhite,
				Color.RED);
		latex_integral1.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_latex.png"));
		latex_integral1.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_latex.png"));
		latex_integral1.preserveRatioProperty();
		latex_integral1.fitWidthProperty().bind(result_window.widthProperty().divide(7));
		latex_integral1.fitHeightProperty().bind(result_window.heightProperty().divide(7));
		plot_function.setImage(new Image("file:./src/main/resources/Integrix/plots/funct_plot_1s.png"));
		plot_function.preserveRatioProperty();
		plot_function.fitWidthProperty().bind(result_window.widthProperty().divide(2.8));
		plot_function.fitHeightProperty().bind(result_window.heightProperty().divide(2.8));
		real_integral.setText(String.valueOf(result));
		if (rectangularButton.isSelected()) {
			result = ni.calculateRectangular(function, min, max, interval);
			method_integral.setText(String.valueOf(result));
			method_result_name.setText("RECTANGULAR METHOD");
		} /*else if (trapezoidalButton.isSelected()) {
			result = ni.calculateTrapezoidal(function, min, max,interval);
		} else if (simpsonButton.isSelected()) {
			result = calculateSimpson(function, min, max, interval);
		}*/
		double absolute_error;
		if (result > Double.parseDouble(real_integral.getText())) {
			absolute_error = result - Double.parseDouble(real_integral.getText());
		} else {
			absolute_error = Double.parseDouble(real_integral.getText()) - result;
		}
		abs_err.setText(String.valueOf(absolute_error));
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
