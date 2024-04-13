package home.animations;

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

public class LoadingComponent extends StackPane {

    public LoadingComponent() {
        // Title
        Label title = new Label("IntegrX");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 36));
        title.setEffect(new DropShadow(5, Color.BLACK));
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setMargin(title, new Insets(75, 0, 0, 0)); // Margin top

        Label loading = new Label("Loading...");
        loading.setTextFill(Color.WHITE);
        loading.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        loading.setEffect(new DropShadow(5, Color.BLACK));
        StackPane.setAlignment(loading, Pos.BOTTOM_CENTER);
        StackPane.setMargin(loading, new Insets(0, 0, 60, 0)); // Margin Bottom

        // Background circle
        Circle backgroundCircle = new Circle(100, 100, 50, rgb(249, 217, 73));
        backgroundCircle.setEffect(new DropShadow(10, Color.BLACK));

        // Background component
        Pane backgroundComponent = new Pane();
        backgroundComponent.setPrefSize(200, 200);
        backgroundComponent.setStyle("-fx-background-color: linear-gradient(to bottom, rgb(49,59,87), rgb(9,13,19));");

        // Progress indicator
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.setProgress(-1); // Set to indeterminate

        // Apply rotation animation to progress indicator
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), progressIndicator);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();

        // Apply fade in/out animation to loading component
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), this);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();

        // Stack elements in layers
        StackPane.setAlignment(backgroundComponent, Pos.CENTER);
        StackPane.setAlignment(backgroundCircle, Pos.CENTER);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        // Add elements to the stack pane
        getChildren().addAll(backgroundComponent, backgroundCircle, progressIndicator, title, loading);
    }
}
