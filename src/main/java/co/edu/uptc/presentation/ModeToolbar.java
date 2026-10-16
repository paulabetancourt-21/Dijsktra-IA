package co.edu.uptc.presentation;

import co.edu.uptc.model.EditMode;

import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;

/** Barra de modos de edición y botón "Limpiar grafo". El modo activo se ve resaltado. */
public class ModeToolbar extends ToolBar {

    private static final String ACTIVE_STYLE =
            "-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-weight: bold;";

    private final ToggleGroup group = new ToggleGroup();

    public ModeToolbar(Consumer<EditMode> onModeChange, Runnable onClear) {
        ToggleButton first = addModeButton("Crear nodo", EditMode.CREATE_NODE);
        addModeButton("Crear arista", EditMode.CREATE_EDGE);
        addModeButton("Mover", EditMode.MOVE);
        addModeButton("Eliminar", EditMode.DELETE);

        Button clear = new Button("Limpiar grafo");
        clear.setOnAction(e -> onClear.run());
        getItems().addAll(new Separator(), clear);

        // Siempre debe haber un modo activo: no se permite desmarcar.
        group.selectedToggleProperty().addListener((obs, old, now) -> {
            if (now == null) {
                group.selectToggle(old);
            } else {
                onModeChange.accept((EditMode) now.getUserData());
            }
        });
        group.selectToggle(first);
    }

    private ToggleButton addModeButton(String text, EditMode mode) {
        ToggleButton button = new ToggleButton(text);
        button.setUserData(mode);
        button.setToggleGroup(group);
        button.selectedProperty().addListener(
                (obs, was, is) -> button.setStyle(is ? ACTIVE_STYLE : ""));
        getItems().add(button);
        return button;
    }
}