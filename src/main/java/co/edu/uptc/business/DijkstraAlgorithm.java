package co.edu.uptc.business;

import co.edu.uptc.model.AppConstants;
import co.edu.uptc.model.DijkstraResult;
import co.edu.uptc.model.DijkstraStep;
import co.edu.uptc.model.Edge;
import co.edu.uptc.model.Graph;
import co.edu.uptc.model.Node;
import co.edu.uptc.model.NodeState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Algoritmo de Dijkstra. Calcula las distancias desde un origen hacia todos los nodos,
 * el camino hacia un destino y registra cada paso para que la interfaz pueda mostrarlo.
 */
public class DijkstraAlgorithm {

    /**
     * Ejecuta el algoritmo.
     *
     * @param graph       grafo sobre el que se calcula
     * @param source      nodo origen (obligatorio)
     * @param destination nodo destino; puede ser null si solo interesan las distancias
     */
    public DijkstraResult run(Graph graph, Node source, Node destination) {
        if (source == null || !graph.containsNode(source)) {
            throw new IllegalArgumentException("El origen no pertenece al grafo.");
        }
        if (destination != null && !graph.containsNode(destination)) {
            throw new IllegalArgumentException("El destino no pertenece al grafo.");
        }

        Map<Node, Integer> distances = new LinkedHashMap<>();
        Map<Node, Node> previous = new LinkedHashMap<>();
        Map<Node, NodeState> states = new LinkedHashMap<>();
        for (Node node : graph.getNodes()) {
            distances.put(node, AppConstants.INFINITY);
            previous.put(node, null);
            states.put(node, NodeState.UNVISITED);
        }
        distances.put(source, 0);

        List<DijkstraStep> steps = new ArrayList<>();
        steps.add(new DijkstraStep(null, distances, previous, states,
                "Inicio: el origen " + source.getName() + " tiene distancia 0."));

        while (true) {
            Node current = pickClosestUnvisited(distances, states);
            if (current == null) {
                break; // los que quedan son inalcanzables
            }
            states.put(current, NodeState.CURRENT);

            for (Edge edge : graph.getEdgesOf(current)) {
                Node neighbor = edge.getOther(current);
                if (states.get(neighbor) == NodeState.VISITED) {
                    continue;
                }
                int candidate = distances.get(current) + edge.getWeight();
                if (candidate < distances.get(neighbor)) {
                    distances.put(neighbor, candidate);
                    previous.put(neighbor, current);
                }
            }
            steps.add(new DijkstraStep(current, distances, previous, states,
                    "Se visita el nodo " + current.getName()
                            + " (distancia " + distances.get(current) + ") y se actualizan sus vecinos."));
            states.put(current, NodeState.VISITED);
        }

        steps.add(new DijkstraStep(null, distances, previous, states, "El algoritmo terminó."));

        DijkstraResult partial = new DijkstraResult(source, destination, distances, previous,
                new ArrayList<>(), steps);
        List<Node> path = destination == null ? new ArrayList<>() : partial.getPathTo(destination);
        return new DijkstraResult(source, destination, distances, previous, path, steps);
    }

    /** Nodo sin visitar con menor distancia finita; null si no queda ninguno alcanzable. */
    private Node pickClosestUnvisited(Map<Node, Integer> distances, Map<Node, NodeState> states) {
        Node best = null;
        for (Map.Entry<Node, Integer> entry : distances.entrySet()) {
            Node node = entry.getKey();
            int distance = entry.getValue();
            if (states.get(node) == NodeState.UNVISITED && distance != AppConstants.INFINITY
                    && (best == null || distance < distances.get(best))) {
                best = node;
            }
        }
        return best;
    }
}
