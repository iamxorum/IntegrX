package home.clase;

import home.IntegrX;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

import java.net.URL;

public class TratareErori {

    // Atribut static pentru a stoca instanța clasei ErrorHandling
    private static TratareErori instance = null;

    // Constructorul este privat pentru a preveni instantierea obiectelor în afara clasei
    private TratareErori() {
    }

    // Metodă statică pentru a obține o instanță a clasei ErrorHandling (Singleton pattern)
    public static TratareErori getInstance() {
        if (instance == null) {
            instance = new TratareErori();
        }
        return instance;
    }

    // Metodă statică pentru a gestiona cazurile în care câmpurile sunt goale
    public static boolean handleEmptyFields(String... fields) {
        // Verificarea primului câmp pentru numere complexe
        if (fields[0].isEmpty()) {
            return true;
        }
        if (handleComplexNumber(fields[0])) { // Verificarea primului câmp pentru a fi un număr complex
            return true;
        }

        // Verificarea celorlalte câmpuri pentru a fi goale
        for (int i = 1; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                showAlert("Campuri goale", "Toate câmpurile trebuie completate.");
                return true;
            }
        }
        return false;
    }

    // Metodă statică pentru a gestiona cazurile în care intervalul este invalid
    public static Boolean handleRange(String min, String max) {
        if (handleRangeInf(min, max)) { // Verificarea intervalului pentru a fi infinit
            return true;
        }

        if (handleRangeEqual(min, max)) { // Verificarea intervalului pentru a fi egal
            return true;
        }

        if (handleRangeInvalid(min, max)) { // Verificarea intervalului pentru a fi invalid
            return true;
        }
        return false;
    }

    // Metodă statică pentru a gestiona cazurile în care minimul este mai mare decât maximul
    public static boolean handleRangeInvalid(String min, String max) {
        if (Double.parseDouble(min) > Double.parseDouble(max)) {
            return true;
        }
        return false;
    }

    // Metodă statică pentru a gestiona cazurile în care minimul și maximul sunt egale
    public static boolean handleRangeEqual(String min, String max) {
        if (Double.parseDouble(min) == Double.parseDouble(max)) {
            return true;
        }
        return false;
    }

    // Metodă statică pentru a gestiona cazurile în care intervalul este infinit
    public static boolean handleRangeInf(String min, String max) {
        // Verificarea dacă min sau max sunt infinit
        if ("-Inf".equals(min) || "Inf".equals(max) || "Inf".equals(min) || "-Inf".equals(max)){
            showAlert("Interval Invalid", "Intervalul nu poate fi infinit.");
            return true;
        }
        return false;
    }

    // Metodă statică pentru a gestiona cazurile în care funcția conține mai mult de o variabilă
    public static boolean handleSingleVariableFunction(String function) {
        // Verificarea dacă funcția conține mai mult de o variabilă
        if (function.contains("y") || function.contains("z")) {
            showAlert("Funcție Invalidă", "Funcția trebuie să conțină o singură variabilă.");
            return true;
        }
        return false;
    }

    // Metodă statică pentru a gestiona cazurile în care funcția conține numere complexe
    public static boolean handleComplexNumber(String... fields) {
        // Verificarea dacă există numere complexe în câmpurile date
        for (String field : fields) {
            // Numai daca e "i" separat, sa nu ia in calcul si "i" din "sin" spre exemplu (sa ia doar i-urile care au un spatii inainte si dupa)
            if (field.contains(" i ") || field.contains("i ") || field.contains(" i")) {
                showAlert("Numar Complex", "Numerele complexe nu sunt acceptate.");
                return true;
            }
        }
        return false;
    }

    // Metodă statică pentru a afișa un dialog de avertizare
    public static void showAlert(String title, String message) {
        // Creează un nou dialog de avertizare cu tipul WARNING
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title); // Setează titlul dialogului
        alert.setHeaderText(null); // Nu se folosește antetul dialogului
        alert.setContentText(message); // Setează mesajul de conținut al dialogului

        // Aplică stiluri CSS la panoul de dialog
        DialogPane dialogPane = alert.getDialogPane();

        // Inițializează stilul dialogului
        alert.initStyle(StageStyle.UNDECORATED);

        // Încarcă fișierul CSS pentru stilizare
        URL cssURL = IntegrX.class.getResource("/Integrix/css/fullstyle.css");  // Asigură-te că calea este corectă
        if (cssURL != null) {
            dialogPane.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("Nu s-a putut găsi fișierul CSS.");
        }

        // Adaugă o clasă la panoul dialogului
        dialogPane.getStyleClass().add("dialog-pane");

        // Adaugă o clasă la textul conținutului
        dialogPane.lookup(".content").getStyleClass().add("content-text");

        // Afișează dialogul și așteaptă până când utilizatorul interacționează cu acesta
        alert.showAndWait();
    }
}

