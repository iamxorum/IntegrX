package home.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class MainController {
	@FXML
	private ToggleButton rectangularButton;

	@FXML
	private ToggleButton trapezoidalButton;

	@FXML
	private ToggleButton simpsonButton;

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


}
