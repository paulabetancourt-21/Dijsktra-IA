package co.edu.uptc.presentation;

import co.edu.uptc.business.GraphService;
import co.edu.uptc.model.InvalidWeightException;

import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/** Ventana emergente para pedir el peso de una arista (al crearla y al editarla). */
public final class WeightDialog {

    private WeightDialog() {
    }

    /**
     * Pide el peso hasta que sea válido o el usuario cancele.
     *
     * @param initial texto inicial del campo (vacío al crear, el peso actual al editar)
     * @return el peso validado, o vacío si el usuario canceló
     */
    public static Optional<Integer> ask(GraphService service, String header, String initial) {
        String text = initial;
        while (true) {
            TextInputDialog dialog = new TextInputDialog(text);
            dialog.setTitle("Peso de la arista");
            dialog.setHeaderText(header);
            dialog.setContentText("Peso (entero positivo):");
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Aceptar");
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");

            Optional<String> answer = dialog.showAndWait();
            if (answer.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(service.parseWeight(answer.get()));
            } catch (InvalidWeightException e) {
                Alerts.warning(e.getMessage());
                text = answer.get();
            }
        }
    }
}