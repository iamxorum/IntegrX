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

    public static boolean handleEmptyFields(String... fields) {
        // Check the first field for complex number
        if (fields[0].isEmpty()) {
            showAlert("EMPTY FIELDS", "All fields must be filled.");
            return true;
        }
        if (handleComplexNumber(fields[0])) {
            return true;
        }

        // Check the rest of the fields only for being empty
        for (int i = 1; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                showAlert("EMPTY FIELDS", "All fields must be filled.");
                return true;
            }
        }
        return false;
    }


    public static Boolean handleRange(String min, String max) {
        if (handleRangeInf(min, max)) {
            return true;
        }
        
        if (handleRangeEqual(min, max)) {
            return true;
        }

        if (handleRangeInvalid(min, max)) {
            return true;
        }
        return false;
    }

    public static boolean handleRangeInvalid(String min, String max) {
        if (Double.parseDouble(min) > Double.parseDouble(max)) {
            showAlert("Invalid Range", "Minimum value must be less than Maximum value.");
            return true;
        }
        return false;
    }

    public static boolean handleRangeEqual(String min, String max) {
        if (Double.parseDouble(min) == Double.parseDouble(max)) {
            showAlert("Invalid Range", "Minimum value must be different from Maximum value.");
            return true;
        }
        return false;
    }

    public static boolean handleRangeInf(String min, String max) {
        if ("-Inf".equals(min) || "Inf".equals(max) || "Inf".equals(min) || "-Inf".equals(max)){
            showAlert("Invalid Range", "No Infinity values allowed.");
            return true;
        }
        return false;
    }

    public static boolean handleComplexNumber(String... fields) {
        for (String field : fields) {
            if (field.contains("i")) {
                showAlert("Complex Number", "Complex numbers are not supported.");
                return true;
            }
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

