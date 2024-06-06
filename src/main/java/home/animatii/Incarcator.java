package home.animatii;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import static javafx.scene.paint.Color.rgb;

public class Incarcator extends StackPane {

    public Incarcator() {
        // Titlul aplicației
        Label title = new Label("IntegrX");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 36));
        title.setEffect(new DropShadow(5, Color.BLACK));
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setMargin(title, new Insets(75, 0, 0, 0)); // Margin top

        // Eticheta de încărcare
        Label loading = new Label("Se Incarca...");
        loading.setTextFill(Color.WHITE);
        loading.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        loading.setEffect(new DropShadow(5, Color.BLACK));
        StackPane.setAlignment(loading, Pos.BOTTOM_CENTER);
        StackPane.setMargin(loading, new Insets(0, 0, 60, 0)); // Margin Bottom

        // Cercul de fundal
        Circle backgroundCircle = new Circle(100, 100, 50, rgb(249, 217, 73)); // Cercul galben de fundal
        backgroundCircle.setEffect(new DropShadow(10, Color.BLACK));

        // Componentul de fundal
        Pane backgroundComponent = new Pane();
        backgroundComponent.setPrefSize(200, 200);
        backgroundComponent.setStyle("-fx-background-color: linear-gradient(to bottom, rgb(49,59,87), rgb(9,13,19));");

        // Indicatorul de progres
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(150, 300);
        progressIndicator.setProgress(-1); // Setare pe indeterminat

        // Aplicarea unei animații de rotație la indicatorul de progres
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), progressIndicator);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();

        // Aplicarea unei animații de fade in/out componentului de încărcare
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), this);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();

        // Plasarea elementelor în straturi pe StackPane
        StackPane.setAlignment(backgroundComponent, Pos.CENTER);
        StackPane.setAlignment(backgroundCircle, Pos.CENTER);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        // Adăugarea elementelor la StackPane
        getChildren().addAll(backgroundComponent, backgroundCircle, progressIndicator, title, loading);
    }
}
