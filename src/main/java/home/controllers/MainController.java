package home.controllers;

import home.classes.NumericIntegration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

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
		NumericIntegration ni = null;
		try {
			ni = NumericIntegration.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String function = func_id.getText();
		double min = Double.parseDouble(min_id.getText());
		double max = Double.parseDouble(max_id.getText());
		double result = 0;
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
}
