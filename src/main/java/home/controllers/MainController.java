package home.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
	@FXML
	protected void onCloseButtonClicked(ActionEvent event) {
		System.exit(0);
	}

}