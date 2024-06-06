package home;

import home.animatii.Incarcator;
import home.clase.Matlab_MultiThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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
	private static final int RESIZE_MARGIN = 10;
	private boolean isResizing = false;
	private Cursor cursorType = Cursor.DEFAULT;

	@Override
	public void start(Stage stage) {
		// Crearea unui obiect LoadingComponent
		Incarcator incarcator = new Incarcator();

		// Crearea unei scene cu LoadingComponent ca nod radacina
		Scene loadingScene = new Scene(incarcator, 500, 500);

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

					stage.setMinWidth(300);
					stage.setMinHeight(200);
					stage.setMaxWidth(screenWidth);
					stage.setMaxHeight(screenHeight);

					root.setOnMousePressed(event -> {
						xOffset = event.getSceneX();
						yOffset = event.getSceneY();
					});

					root.setOnMouseDragged(event -> {
						if (cursorType != Cursor.DEFAULT) {
							isResizing = true;
						}

						if (!isResizing) {
							stage.setX(event.getScreenX() - xOffset);
							stage.setY(event.getScreenY() - yOffset);
						} else {
							if (cursorType == Cursor.NW_RESIZE || cursorType == Cursor.N_RESIZE || cursorType == Cursor.NE_RESIZE) {
								double newHeight = stage.getHeight() - (event.getScreenY() - stage.getY());
								if (newHeight > stage.getMinHeight()) {
									stage.setHeight(newHeight);
									stage.setY(event.getScreenY());
								}
							}
							if (cursorType == Cursor.NW_RESIZE || cursorType == Cursor.W_RESIZE || cursorType == Cursor.SW_RESIZE) {
								double newWidth = stage.getWidth() - (event.getScreenX() - stage.getX());
								if (newWidth > stage.getMinWidth()) {
									stage.setWidth(newWidth);
									stage.setX(event.getScreenX());
								}
							}
							if (cursorType == Cursor.NE_RESIZE || cursorType == Cursor.E_RESIZE || cursorType == Cursor.SE_RESIZE) {
								double newWidth = event.getScreenX() - stage.getX();
								if (newWidth > stage.getMinWidth()) {
									stage.setWidth(newWidth);
								}
							}
							if (cursorType == Cursor.SE_RESIZE || cursorType == Cursor.S_RESIZE || cursorType == Cursor.SW_RESIZE) {
								double newHeight = event.getScreenY() - stage.getY();
								if (newHeight > stage.getMinHeight()) {
									stage.setHeight(newHeight);
								}
							}
						}
					});

					root.setOnMouseMoved(event -> {
						if (event.getX() < RESIZE_MARGIN && event.getY() < RESIZE_MARGIN) {
							cursorType = Cursor.NW_RESIZE;
						} else if (event.getX() > stage.getWidth() - RESIZE_MARGIN && event.getY() < RESIZE_MARGIN) {
							cursorType = Cursor.NE_RESIZE;
						} else if (event.getX() < RESIZE_MARGIN && event.getY() > stage.getHeight() - RESIZE_MARGIN) {
							cursorType = Cursor.SW_RESIZE;
						} else if (event.getX() > stage.getWidth() - RESIZE_MARGIN && event.getY() > stage.getHeight() - RESIZE_MARGIN) {
							cursorType = Cursor.SE_RESIZE;
						} else if (event.getX() < RESIZE_MARGIN) {
							cursorType = Cursor.W_RESIZE;
						} else if (event.getX() > stage.getWidth() - RESIZE_MARGIN) {
							cursorType = Cursor.E_RESIZE;
						} else if (event.getY() < RESIZE_MARGIN) {
							cursorType = Cursor.N_RESIZE;
						} else if (event.getY() > stage.getHeight() - RESIZE_MARGIN) {
							cursorType = Cursor.S_RESIZE;
						} else {
							cursorType = Cursor.DEFAULT;
						}
						root.setCursor(cursorType);
					});

					root.setOnMouseReleased(event -> isResizing = false);


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