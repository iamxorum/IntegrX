package home;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class IntegrX extends Application {
	private static MatlabEngine engine;
	private double xOffset = 0;
	private double yOffset = 0;

	static {
		try {
			engine = MatlabEngine.startMatlab();
		} catch (EngineException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void start(Stage stage) throws IOException, EngineException, InterruptedException {
		FXMLLoader fxmlLoader = new FXMLLoader(IntegrX.class.getResource("/Integrix/main.fxml"));

		Parent root = fxmlLoader.load();

		// Get the primary screen dimensions
		Screen screen = Screen.getPrimary();
		double screenWidth = screen.getBounds().getWidth();
		double screenHeight = screen.getBounds().getHeight();

		// Create a Scene with full screen dimensions
		Scene scene = new Scene(root, 0.75 * screenWidth, 0.75 * screenHeight);

		stage.setTitle("IntegrX");
		stage.setScene(scene);
		stage.getIcons().add(new Image(IntegrX.class.getResourceAsStream("/Integrix/maths.png")));
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

	public static MatlabEngine getEngine() {
		return engine;
	}
	public static void main(String[] args) {
		launch(args);
	}
}