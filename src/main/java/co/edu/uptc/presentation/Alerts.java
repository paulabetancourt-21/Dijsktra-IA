package co.edu.uptc.presentation;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/** Avisos en español para el usuario. */
public final class Alerts {

    private Alerts() {
    }

    public static void warning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Aceptar");
        alert.showAndWait();
    }
}