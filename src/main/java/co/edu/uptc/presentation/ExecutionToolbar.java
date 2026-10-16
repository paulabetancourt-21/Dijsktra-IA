package co.edu.uptc.presentation;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;

/** Barra de ejecución: elegir origen y destino, paso a paso, animación y velocidad. */
public class ExecutionToolbar extends ToolBar {

    private final Button pickSource = new Button("Elegir origen");
    private final Button pickDestination = new Button("Elegir destino");
    private final Button next = new Button("Siguiente paso");
    private final Button animate = new Button("Animar");
    private final Button pause = new Button("Pausar");
    private final Button reset = new Button("Reiniciar");
    private final Slider speed = new Slider(0.25, 4, 1);
    private final Label speedLabel = new Label("1.00x");

    public ExecutionToolbar() {
        speed.setPrefWidth(140);
        speed.valueProperty().addListener((obs, old, now) ->
                speedLabel.setText(String.format("%.2fx", now.doubleValue())));

        getItems().addAll(pickSource, pickDestination, new Separator(),
                next, animate, pause, reset, new Separator(),
                new Label("Velocidad:"), speed, speedLabel);
    }

    public Button getPickSource() {
        return pickSource;
    }

    public Button getPickDestination() {
        return pickDestination;
    }

    public Button getNext() {
        return next;
    }

    public Button getAnimate() {
        return animate;
    }

    public Button getPause() {
        return pause;
    }

    public Button getReset() {
        return reset;
    }

    public Slider getSpeed() {
        return speed;
    }
}