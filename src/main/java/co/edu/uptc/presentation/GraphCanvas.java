package co.edu.uptc.presentation;

import co.edu.uptc.business.GraphService;
import co.edu.uptc.model.Edge;
import co.edu.uptc.model.Node;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/** Área de dibujo del grafo. Solo dibuja lo que hay en GraphService; nunca lo modifica. */
public class GraphCanvas extends Pane {

    public static final double NODE_RADIUS = 20;
    private static final double EDGE_HIT_DISTANCE = 6;

    private final GraphService service;
    private final Map<Node, NodeShape> nodeShapes = new LinkedHashMap<>();
    private final Map<Edge, EdgeShape> edgeShapes = new LinkedHashMap<>();
    private final Line previewLine = new Line();

    public GraphCanvas(GraphService service) {
        this.service = service;
        setStyle("-fx-background-color: white;");

        previewLine.setStroke(Color.GRAY);
        previewLine.getStrokeDashArray().addAll(8.0, 6.0);
        previewLine.setStrokeWidth(2);
        previewLine.setVisible(false);
        previewLine.setMouseTransparent(true);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);

        refresh();
    }

    /** Vuelve a dibujar todo el grafo desde cero. */
    public void refresh() {
        getChildren().clear();
        nodeShapes.clear();
        edgeShapes.clear();

        for (Edge edge : service.getGraph().getEdges()) {
            EdgeShape shape = new EdgeShape(edge);
            edgeShapes.put(edge, shape);
            getChildren().add(shape);
        }
        for (Node node : service.getGraph().getNodes()) {
            NodeShape shape = new NodeShape(node);
            nodeShapes.put(node, shape);
            getChildren().add(shape);
        }
        getChildren().add(previewLine);
    }

    /** Reubica los dibujos existentes (se usa mientras se arrastra un nodo). */
    public void updateGeometry() {
        for (EdgeShape shape : edgeShapes.values()) {
            shape.update();
        }
        for (NodeShape shape : nodeShapes.values()) {
            shape.update();
        }
    }

    /** Nodo que está bajo el punto (x, y); null si no hay ninguno. */
    public Node nodeAt(double x, double y) {
        List<Node> nodes = service.getGraph().getNodes();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            if (Math.hypot(x - node.getX(), y - node.getY()) <= NODE_RADIUS) {
                return node;
            }
        }
        return null;
    }

    /** Arista que pasa cerca del punto (x, y); null si no hay ninguna. */
    public Edge edgeAt(double x, double y) {
        for (Edge edge : service.getGraph().getEdges()) {
            double d = distanceToSegment(x, y,
                    edge.getNodeA().getX(), edge.getNodeA().getY(),
                    edge.getNodeB().getX(), edge.getNodeB().getY());
            if (d <= EDGE_HIT_DISTANCE) {
                return edge;
            }
        }
        return null;
    }

    public void showPreview(Node from, double toX, double toY) {
        previewLine.setStartX(from.getX());
        previewLine.setStartY(from.getY());
        previewLine.setEndX(toX);
        previewLine.setEndY(toY);
        previewLine.setVisible(true);
    }

    public void hidePreview() {
        previewLine.setVisible(false);
    }

    private static double distanceToSegment(double px, double py,
                                            double ax, double ay, double bx, double by) {
        double dx = bx - ax;
        double dy = by - ay;
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared == 0) {
            return Math.hypot(px - ax, py - ay);
        }
        double t = ((px - ax) * dx + (py - ay) * dy) / lengthSquared;
        t = Math.max(0, Math.min(1, t));
        return Math.hypot(px - (ax + t * dx), py - (ay + t * dy));
    }
}