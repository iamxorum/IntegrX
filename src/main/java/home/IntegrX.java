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
	// Variabile pentru stocarea offset-ului la mutarea ferestrei
	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) {
		// Crearea unui obiect LoadingComponent
		LoadingComponent loadingComponent = new LoadingComponent();

		// Crearea unei scene cu LoadingComponent ca nod radacina
		Scene loadingScene = new Scene(loadingComponent, 500, 500);

		// Setarea scenei de incarcare ca scena initiala
		stage.setScene(loadingScene);
		stage.initStyle(StageStyle.UNDECORATED); // Eliminarea decorului ferestrei
		stage.show();

		// Inceperea unui fir de executie pentru initializarea MatlabEngine
		Matlab_MultiThread multiThread = Matlab_MultiThread.getInstance();
		multiThread.start();

		// Asteptarea finalizarii firului de executie pentru initializarea motorului
		new Thread(() -> {
			try {
				multiThread.join();
				// Odata ce initializarea motorului este completa, incarca interfata UI principala
				Platform.runLater(() -> {
					FXMLLoader fxmlLoader = new FXMLLoader(IntegrX.class.getResource("/Integrix/main.fxml"));

					Parent root;
					try {
						root = fxmlLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}

					// Obtinerea dimensiunilor ecranului principal
					Screen screen = Screen.getPrimary();
					double screenWidth = screen.getBounds().getWidth();
					double screenHeight = screen.getBounds().getHeight();

					// Crearea unei Scene cu dimensiuni de ecran complet
					Scene scene = new Scene(root, 0.75 * screenWidth, 0.75 * screenHeight);

					stage.setTitle("IntegrX");
					stage.setScene(scene);
					stage.getIcons().add(new Image(IntegrX.class.getResourceAsStream("/Integrix/maths.png")));

					// Setarea gestionarilor de evenimente pentru mutarea ferestrei
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

	// Metoda principala a clasei, porneste aplicatia JavaFX
	public static void main(String[] args) {
		launch(args);
	}
}