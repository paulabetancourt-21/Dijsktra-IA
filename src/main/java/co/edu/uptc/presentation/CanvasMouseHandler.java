package co.edu.uptc.presentation;

import co.edu.uptc.business.GraphService;
import co.edu.uptc.model.DuplicateEdgeException;
import co.edu.uptc.model.EditMode;
import co.edu.uptc.model.Edge;
import co.edu.uptc.model.InvalidWeightException;
import co.edu.uptc.model.MaxNodesException;
import co.edu.uptc.model.Node;
import co.edu.uptc.model.SelfLoopException;

import java.util.Optional;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/** Maneja el mouse sobre el área de dibujo según el modo activo. Toda acción pasa por GraphService. */
public class CanvasMouseHandler {

    private final GraphCanvas canvas;
    private final GraphService service;
    private EditMode mode = EditMode.CREATE_NODE;

    private Node dragNode;      // nodo que se mueve (modo Mover)
    private Node edgeSource;    // nodo de inicio (modo Crear arista)
    private boolean leftSource; // el arrastre ya salió del nodo de inicio

    public CanvasMouseHandler(GraphCanvas canvas, GraphService service) {
        this.canvas = canvas;
        this.service = service;
        canvas.setOnMousePressed(this::onPressed);
        canvas.setOnMouseDragged(this::onDragged);
        canvas.setOnMouseReleased(this::onReleased);
        canvas.setOnMouseClicked(this::onClicked);
    }

    public void setMode(EditMode mode) {
        this.mode = mode;
        dragNode = null;
        edgeSource = null;
        canvas.hidePreview();
    }

    private void onPressed(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        double x = e.getX();
        double y = e.getY();
        Node node = canvas.nodeAt(x, y);

        switch (mode) {
            case CREATE_NODE -> {
                if (node == null && canvas.edgeAt(x, y) == null) {
                    try {
                        service.addNode(x, y);
                        canvas.refresh();
                    } catch (MaxNodesException ex) {
                        Alerts.warning(ex.getMessage());
                    }
                }
            }
            case CREATE_EDGE -> {
                edgeSource = node;
                leftSource = false;
            }
            case MOVE -> dragNode = node;
            case DELETE -> {
                if (node != null) {
                    service.removeNode(node);
                    canvas.refresh();
                } else {
                    Edge edge = canvas.edgeAt(x, y);
                    if (edge != null) {
                        service.removeEdge(edge);
                        canvas.refresh();
                    }
                }
            }
        }
    }

    private void onDragged(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        if (mode == EditMode.MOVE && dragNode != null) {
            double r = GraphCanvas.NODE_RADIUS;
            double maxX = Math.max(r, canvas.getWidth() - r);
            double maxY = Math.max(r, canvas.getHeight() - r);
            dragNode.setX(Math.max(r, Math.min(maxX, e.getX())));
            dragNode.setY(Math.max(r, Math.min(maxY, e.getY())));
            canvas.updateGeometry();
        } else if (mode == EditMode.CREATE_EDGE && edgeSource != null) {
            if (canvas.nodeAt(e.getX(), e.getY()) != edgeSource) {
                leftSource = true;
            }
            canvas.showPreview(edgeSource, e.getX(), e.getY());
        }
    }

    private void onReleased(MouseEvent e) {
        if (mode == EditMode.MOVE) {
            dragNode = null;
        } else if (mode == EditMode.CREATE_EDGE) {
            finishEdge(e);
        }
    }

    private void finishEdge(MouseEvent e) {
        Node source = edgeSource;
        edgeSource = null;
        canvas.hidePreview();
        if (source == null) {
            return;
        }
        Node target = canvas.nodeAt(e.getX(), e.getY());
        if (target == null) {
            return;
        }
        if (target == source) {
            if (leftSource) { // arrastró y volvió al mismo nodo
                Alerts.warning(new SelfLoopException().getMessage());
            }
            return;
        }
        if (service.getGraph().findEdge(source, target) != null) {
            Alerts.warning(new DuplicateEdgeException().getMessage());
            return;
        }

        Optional<Integer> weight = WeightDialog.ask(service,
                "Peso de la arista " + source.getName() + "-" + target.getName(), "");
        if (weight.isPresent()) {
            try {
                service.addEdge(source, target, weight.get());
            } catch (DuplicateEdgeException | SelfLoopException | InvalidWeightException ex) {
                Alerts.warning(ex.getMessage());
            }
            canvas.refresh();
        }
    }

    /** Doble clic sobre una arista: editar su peso. */
    private void onClicked(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY || e.getClickCount() != 2) {
            return;
        }
        if (canvas.nodeAt(e.getX(), e.getY()) != null) {
            return;
        }
        Edge edge = canvas.edgeAt(e.getX(), e.getY());
        if (edge == null) {
            return;
        }
        Optional<Integer> weight = WeightDialog.ask(service,
                "Nuevo peso de la arista " + edge.getNodeA().getName() + "-" + edge.getNodeB().getName(),
                String.valueOf(edge.getWeight()));
        if (weight.isPresent()) {
            try {
                service.changeWeight(edge, weight.get());
            } catch (InvalidWeightException ex) {
                Alerts.warning(ex.getMessage());
            }
            canvas.refresh();
        }
    }
}