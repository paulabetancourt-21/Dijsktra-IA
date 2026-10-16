package co.edu.uptc.presentation;

import co.edu.uptc.model.Node;
import co.edu.uptc.model.NodeState;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/** Dibujo de un nodo: círculo con su letra, distancia encima y marca de origen/destino debajo. */
public class NodeShape extends Group {

    private static final Color DEFAULT_STROKE = Color.DARKSLATEBLUE;
    private static final Color SOURCE_COLOR = Color.web("#1b7f3b");
    private static final Color DESTINATION_COLOR = Color.web("#c0392b");

    private final Node node;
    private final Circle circle = new Circle(GraphCanvas.NODE_RADIUS);
    private final Text label;
    private final Text distanceText = new Text();
    private final Text roleText = new Text();

    public NodeShape(Node node) {
        this.node = node;
        circle.setFill(NodeColors.fill(NodeState.UNVISITED));
        circle.setStroke(DEFAULT_STROKE);
        circle.setStrokeWidth(2);

        label = new Text(node.getName());
        label.setFont(Font.font("System", FontWeight.BOLD, 16));

        distanceText.setFont(Font.font("System", FontWeight.BOLD, 14));
        distanceText.setFill(Color.web("#8e2b1f"));
        distanceText.setVisible(false);

        roleText.setFont(Font.font("System", FontWeight.BOLD, 11));
        roleText.setVisible(false);

        getChildren().addAll(circle, label, distanceText, roleText);
        setMouseTransparent(true);
        update();
    }

    public Node getNode() {
        return node;
    }

    /** Reubica el dibujo según la posición actual del nodo. */
    public void update() {
        double x = node.getX();
        double y = node.getY();
        circle.setCenterX(x);
        circle.setCenterY(y);
        label.setX(x - label.getLayoutBounds().getWidth() / 2);
        label.setY(y + 5.5);
        distanceText.setX(x - distanceText.getLayoutBounds().getWidth() / 2);
        distanceText.setY(y - GraphCanvas.NODE_RADIUS - 6);
        roleText.setX(x - roleText.getLayoutBounds().getWidth() / 2);
        roleText.setY(y + GraphCanvas.NODE_RADIUS + 14);
    }

    public void setState(NodeState state) {
        circle.setFill(NodeColors.fill(state));
    }

    public void setDistance(String text) {
        distanceText.setText(text);
        distanceText.setVisible(true);
        update();
    }

    /** Quita los colores y las distancias de la ejecución (no toca la marca de origen/destino). */
    public void clearExecution() {
        circle.setFill(NodeColors.fill(NodeState.UNVISITED));
        distanceText.setVisible(false);
    }

    /** Marca visualmente el nodo como origen, destino, ambos o ninguno. */
    public void setRole(boolean isSource, boolean isDestination) {
        Color color = DEFAULT_STROKE;
        String text = "";
        if (isSource && isDestination) {
            color = Color.PURPLE;
            text = "Origen y destino";
        } else if (isSource) {
            color = SOURCE_COLOR;
            text = "Origen";
        } else if (isDestination) {
            color = DESTINATION_COLOR;
            text = "Destino";
        }
        circle.setStroke(color);
        circle.setStrokeWidth(text.isEmpty() ? 2 : 4);
        roleText.setText(text);
        roleText.setFill(color);
        roleText.setVisible(!text.isEmpty());
        update();
    }
}