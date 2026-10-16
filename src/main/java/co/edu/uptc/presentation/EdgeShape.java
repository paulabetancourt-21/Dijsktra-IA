package co.edu.uptc.presentation;

import co.edu.uptc.model.Edge;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/** Dibujo de una arista: una línea con su peso visible en el centro. */
public class EdgeShape extends Group {

    private final Edge edge;
    private final Line line = new Line();
    private final Rectangle background = new Rectangle();
    private final Text label = new Text();

    public EdgeShape(Edge edge) {
        this.edge = edge;
        line.setStroke(Color.DIMGRAY);
        line.setStrokeWidth(2.5);
        background.setFill(Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);
        background.setArcWidth(6);
        background.setArcHeight(6);
        label.setFont(Font.font("System", 13));
        getChildren().addAll(line, background, label);
        setMouseTransparent(true);
        update();
    }

    public Edge getEdge() {
        return edge;
    }

    /** Reubica la línea y el peso según la posición actual de sus nodos. */
    public void update() {
        double ax = edge.getNodeA().getX();
        double ay = edge.getNodeA().getY();
        double bx = edge.getNodeB().getX();
        double by = edge.getNodeB().getY();
        line.setStartX(ax);
        line.setStartY(ay);
        line.setEndX(bx);
        line.setEndY(by);

        double mx = (ax + bx) / 2;
        double my = (ay + by) / 2;
        label.setText(String.valueOf(edge.getWeight()));
        double w = label.getLayoutBounds().getWidth();
        double h = label.getLayoutBounds().getHeight();
        label.setX(mx - w / 2);
        label.setY(my + h / 4);
        background.setX(mx - w / 2 - 4);
        background.setY(my - h / 2 - 1);
        background.setWidth(w + 8);
        background.setHeight(h + 2);
    }
}