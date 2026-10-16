package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Resultado final de Dijkstra: distancias, previos, camino al destino y lista de pasos. */
public class DijkstraResult {

    private final Node source;
    private final Node destination;
    private final Map<Node, Integer> distances;
    private final Map<Node, Node> previous;
    private final List<Node> path;
    private final List<DijkstraStep> steps;

    public DijkstraResult(Node source, Node destination, Map<Node, Integer> distances,
                          Map<Node, Node> previous, List<Node> path, List<DijkstraStep> steps) {
        this.source = source;
        this.destination = destination;
        this.distances = Collections.unmodifiableMap(new LinkedHashMap<>(distances));
        this.previous = Collections.unmodifiableMap(new LinkedHashMap<>(previous));
        this.path = Collections.unmodifiableList(new ArrayList<>(path));
        this.steps = Collections.unmodifiableList(new ArrayList<>(steps));
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public Map<Node, Integer> getDistances() {
        return distances;
    }

    public Map<Node, Node> getPrevious() {
        return previous;
    }

    /** Camino del origen al destino (ambos incluidos); vacío si el destino es inalcanzable o no hay destino. */
    public List<Node> getPath() {
        return path;
    }

    public List<DijkstraStep> getSteps() {
        return steps;
    }

    public int getDistance(Node node) {
        return distances.get(node);
    }

    public boolean isReachable(Node node) {
        return distances.get(node) != AppConstants.INFINITY;
    }

    /** Camino del origen hacia cualquier nodo; vacío si es inalcanzable. */
    public List<Node> getPathTo(Node target) {
        List<Node> result = new ArrayList<>();
        if (!isReachable(target)) {
            return result;
        }
        for (Node n = target; n != null; n = previous.get(n)) {
            result.add(0, n);
        }
        return result;
    }
}
