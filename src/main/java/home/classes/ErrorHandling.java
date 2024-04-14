package home.classes;

import home.IntegrX;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

import java.net.URL;

public class ErrorHandling {

    private static ErrorHandling instance = null;

    private ErrorHandling() {
    }

    public static ErrorHandling getInstance() {
        if (instance == null) {
            instance = new ErrorHandling();
        }
        return instance;
    }

    public static void handleEmptyFields(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                showAlert("EMPTY FIELDS", "All fields must be filled.");
                return;
            }
        }
    }

    public static void handleRange(String min, String max) {
        if ("Inf".equals(max) && !("-Inf".equals(min)) && !("Inf".equals(min)) && "-Inf".equals(max)) {
            if (Double.parseDouble(min) >= Double.POSITIVE_INFINITY) {
                showAlert("Invalid Range", "The minimum value must be less than positive infinity.");
                return;
            }
        } else if ("-Inf".equals(min) && !("Inf".equals(max)) && !("-Inf".equals(max)) && "-Inf".equals(min)) {
            if (Double.parseDouble(max) <= Double.NEGATIVE_INFINITY) {
                showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
                return;
            }
        } else if (!("Inf".equals(min)) && !("-Inf".equals(max)) && !("Inf".equals(max)) && !("-Inf".equals(min))) {
            if (Double.parseDouble(min) > Double.parseDouble(max)) {
                showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
                return;
            } else if (Double.parseDouble(min) == Double.parseDouble(max)) {
                showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
                return;
            }
        } else if ("-Inf".equals(max)) {
            showAlert("Invalid Range", "The maximum value must be higher than negative infinity.");
            return;
        } else if ("Inf".equals(min)) {
            showAlert("Invalid Range", "The minimum value must be less than positive infinity.");
            return;
        } else if ("Inf".equals(min) && "Inf".equals(max)) {
            showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
            return;
        } else if ("-Inf".equals(min) && "-Inf".equals(max)) {
            showAlert("Invalid Range", "The minimum value must be less than the maximum value.");
            return;
        }
    }

    public static boolean handleRangeInf(String min, String max) {
        if ("-Inf".equals(min) || "Inf".equals(max)) {
            showAlert("Invalid Range", "No Infinity values allowed.");
            return true;
        }
        return false;
    }



    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply CSS styles to the dialog pane
        DialogPane dialogPane = alert.getDialogPane();

        // Load the CSS file
        alert.initStyle(StageStyle.UNDECORATED);
        URL cssURL = IntegrX.class.getResource("/Integrix/css/fullstyle.css");  // Make sure path is correct
        if (cssURL != null) {
            dialogPane.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("Could not load stylesheet");
        }

        // Add a class to the dialog pane
        dialogPane.getStyleClass().add("dialog-pane");

        // Add a class to the content text
        dialogPane.lookup(".content").getStyleClass().add("content-text");

        // Show the alert
        alert.showAndWait();
    }

}

