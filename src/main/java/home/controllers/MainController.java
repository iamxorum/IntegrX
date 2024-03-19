package home.controllers;

import home.classes.NumericIntegration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Objects;

public class MainController {
	@FXML private ToggleButton rectangularButton;
	@FXML private ToggleButton trapezoidalButton;
	@FXML private ToggleButton simpsonButton;
	@FXML private TextField func_id;
	@FXML private TextField min_id;
	@FXML private TextField max_id;
	@FXML private TextField int_id;
	@FXML private Label real_integral;

	@FXML
	private void initialize() {
		ToggleGroup toggleGroup = new ToggleGroup();
		rectangularButton.setToggleGroup(toggleGroup);
		trapezoidalButton.setToggleGroup(toggleGroup);
		simpsonButton.setToggleGroup(toggleGroup);
	}

	@FXML
	protected void onCloseButtonClicked(ActionEvent event) {
		System.exit(0);
	}

	public void calculateAction(ActionEvent actionEvent) {
		String function = func_id.getText();
		String min = min_id.getText();
		String max = max_id.getText();

		if ("pi".equals(min)) {
			min = String.valueOf(Math.PI);
		} else if ("e".equals(min)) {
			min = String.valueOf(Math.E);
		} else if ("pi".equals(max)) {
			max = String.valueOf(Math.PI);
		} else if ("e".equals(max)) {
			max = String.valueOf(Math.E);
		} else if ("phi".equals(max)) {
			max = String.valueOf((1 + Math.sqrt(5)) / 2);
		} else if ("phi".equals(min)) {
			min = String.valueOf((1 + Math.sqrt(5)) / 2);
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

		if (function.isEmpty() || min_id.getText().isEmpty() || max_id.getText().isEmpty()) {
			showAlert("Empty Fields", "All fields must be filled.");
			return;
		}

		if (int_id.getText().isEmpty()) {
			int_id.setText("0");
		}
		double result = 0; // Initialize the result variable
		NumericIntegration ni = null;
		try {
			ni = NumericIntegration.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = ni.integrate(function, min, max);
		/*if (rectangularButton.isSelected()) {
			result = calculateRectangular(function, min, max);
		} else if (trapezoidalButton.isSelected()) {
			result = calculateTrapezoidal(function, min, max);
		} else if (simpsonButton.isSelected()) {
			result = calculateSimpson(function, min, max);
		}*/
		real_integral.setText(String.valueOf(result));
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
