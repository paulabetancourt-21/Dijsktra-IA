package co.edu.uptc.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Foto del algoritmo en un paso: nodo actual, distancias, previos y estado de cada nodo.
 * Es inmutable: guarda copias de los datos.
 */
public class DijkstraStep {

    private final Node current;
    private final Map<Node, Integer> distances;
    private final Map<Node, Node> previous;
    private final Map<Node, NodeState> states;
    private final String description;

    public DijkstraStep(Node current, Map<Node, Integer> distances, Map<Node, Node> previous,
                        Map<Node, NodeState> states, String description) {
        this.current = current;
        this.distances = Collections.unmodifiableMap(new LinkedHashMap<>(distances));
        this.previous = Collections.unmodifiableMap(new LinkedHashMap<>(previous));
        this.states = Collections.unmodifiableMap(new LinkedHashMap<>(states));
        this.description = description;
    }

    /** Nodo que se procesa en este paso; null en el paso inicial y en el final. */
    public Node getCurrent() {
        return current;
    }

    /** Distancia acumulada de cada nodo; AppConstants.INFINITY si aún no se alcanza. */
    public Map<Node, Integer> getDistances() {
        return distances;
    }

    /** Nodo previo de cada nodo; null si no tiene. */
    public Map<Node, Node> getPrevious() {
        return previous;
    }

    public Map<Node, NodeState> getStates() {
        return states;
    }

    /** Texto breve en español que explica el paso. */
    public String getDescription() {
        return description;
    }
}
