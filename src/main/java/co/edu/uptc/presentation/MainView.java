package co.edu.uptc.presentation;

import co.edu.uptc.business.GraphService;

import javafx.scene.layout.BorderPane;

/** Ventana principal: barra arriba y área de dibujo al centro. */
public class MainView extends BorderPane {

    private final GraphService service = new GraphService();

    public MainView() {
        GraphCanvas canvas = new GraphCanvas(service);
        CanvasMouseHandler handler = new CanvasMouseHandler(canvas, service);
        ModeToolbar toolbar = new ModeToolbar(handler::setMode, () -> {
            service.clear();
            canvas.refresh();
        });

        setTop(toolbar);
        setCenter(canvas);
        // setRight(...): espacio reservado para la tabla de distancias (Fase 3).
    }
}