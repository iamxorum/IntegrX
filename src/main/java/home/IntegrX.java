package home;

import home.animations.LoadingComponent;
import home.classes.Matlab_MultiThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class IntegrX extends Application {
	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) {
		LoadingComponent loadingComponent = new LoadingComponent();

		// Create a scene with the loading component as the root node
		Scene loadingScene = new Scene(loadingComponent, 500, 500);

		// Set the loading screen as the initial scene
		stage.setScene(loadingScene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();

		// Start the thread to initialize MatlabEngine
		Matlab_MultiThread multiThread = Matlab_MultiThread.getInstance();
		multiThread.start();

		// Wait for the thread to finish initializing the engine
		new Thread(() -> {
			try {
				multiThread.join();
				// Once engine initialization is complete, load the main UI
				Platform.runLater(() -> {
					FXMLLoader fxmlLoader = new FXMLLoader(IntegrX.class.getResource("/Integrix/main.fxml"));

					Parent root;
					try {
						root = fxmlLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}

					// Get the primary screen dimensions
					Screen screen = Screen.getPrimary();
					double screenWidth = screen.getBounds().getWidth();
					double screenHeight = screen.getBounds().getHeight();

					// Create a Scene with full screen dimensions
					Scene scene = new Scene(root, 0.75 * screenWidth, 0.75 * screenHeight);

					stage.setTitle("IntegrX");
					stage.setScene(scene);
					stage.getIcons().add(new Image(IntegrX.class.getResourceAsStream("/Integrix/maths.png")));

					// Set the event handlers for dragging the window
					root.setOnMousePressed(event -> {
						xOffset = event.getSceneX();
						yOffset = event.getSceneY();
					});

					root.setOnMouseDragged(event -> {
						stage.setX(event.getScreenX() - xOffset);
						stage.setY(event.getScreenY() - yOffset);
					});

					stage.setScene(scene);
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}