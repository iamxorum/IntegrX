package home;

import home.classes.NumericIntegration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
	// Variables to store mouse cursor position
	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Integrix/main.fxml"));

		Parent root = fxmlLoader.load();

		// Get the primary screen dimensions
		Screen screen = Screen.getPrimary();
		double screenWidth = screen.getBounds().getWidth();
		double screenHeight = screen.getBounds().getHeight();

		// Create a Scene with full screen dimensions
		Scene scene = new Scene(root, 0.75 * screenWidth, 0.75 * screenHeight);

		stage.setTitle("IntegrX");
		stage.setScene(scene);
		stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/Integrix/maths.png")));
		// Set the stage style to undecorated
		stage.initStyle(StageStyle.UNDECORATED);

		// Set event handlers for mouse pressed, dragged, and released
		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		root.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});

		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}