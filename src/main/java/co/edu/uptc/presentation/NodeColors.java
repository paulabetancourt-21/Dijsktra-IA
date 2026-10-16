package co.edu.uptc.presentation;

import co.edu.uptc.model.NodeState;

import javafx.scene.paint.Color;

/** Un color de relleno para cada estado de nodo. */
public final class NodeColors {

    private NodeColors() {
    }

    public static Color fill(NodeState state) {
        return switch (state) {
            case UNVISITED -> Color.LIGHTSKYBLUE;
            case CURRENT -> Color.ORANGE;
            case VISITED -> Color.web("#c9ced6");
        };
    }
}