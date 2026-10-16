package co.edu.uptc.presentation;

import co.edu.uptc.business.GraphService;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/** Ventana principal: barras arriba, área de dibujo al centro, tabla a la derecha y mensajes abajo. */
public class MainView extends BorderPane {

    private final GraphService service = new GraphService();

    public MainView() {
        GraphCanvas canvas = new GraphCanvas(service);
        CanvasMouseHandler mouseHandler = new CanvasMouseHandler(canvas, service);
        ModeToolbar modeToolbar = new ModeToolbar(mouseHandler::setMode, () -> {
            service.clear();
            canvas.refresh();
        });

        ExecutionToolbar executionToolbar = new ExecutionToolbar();
        DistanceTableView table = new DistanceTableView();
        Label status = new Label();
        status.setPadding(new Insets(6, 10, 6, 10));
        status.setWrapText(true);

        new ExecutionHandler(service, canvas, mouseHandler, modeToolbar,
                executionToolbar, table, status);

        setTop(new VBox(modeToolbar, executionToolbar));
        setCenter(canvas);
        setRight(table);
        setBottom(status);
    }
}