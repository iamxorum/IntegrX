package home.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
	@FXML
	protected void onCloseButtonClicked(ActionEvent event) {
		System.exit(0);
	}

}