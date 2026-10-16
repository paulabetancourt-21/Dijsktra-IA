package co.edu.uptc.presentation;

import co.edu.uptc.model.Node;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/** Dibujo de un nodo: un círculo con su letra. */
public class NodeShape extends Group {

    private final Node node;
    private final Circle circle = new Circle(GraphCanvas.NODE_RADIUS);
    private final Text label;

    public NodeShape(Node node) {
        this.node = node;
        circle.setFill(Color.LIGHTSKYBLUE);
        circle.setStroke(Color.DARKSLATEBLUE);
        circle.setStrokeWidth(2);
        label = new Text(node.getName());
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        getChildren().addAll(circle, label);
        setMouseTransparent(true);
        update();
    }

    public Node getNode() {
        return node;
    }

    /** Reubica el dibujo según la posición actual del nodo. */
    public void update() {
        circle.setCenterX(node.getX());
        circle.setCenterY(node.getY());
        label.setX(node.getX() - label.getLayoutBounds().getWidth() / 2);
        label.setY(node.getY() + 5.5);
    }
}