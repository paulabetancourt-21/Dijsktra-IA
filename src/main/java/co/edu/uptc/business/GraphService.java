package co.edu.uptc.business;

import co.edu.uptc.model.AppConstants;
import co.edu.uptc.model.DuplicateEdgeException;
import co.edu.uptc.model.Edge;
import co.edu.uptc.model.Graph;
import co.edu.uptc.model.InvalidWeightException;
import co.edu.uptc.model.MaxNodesException;
import co.edu.uptc.model.Node;
import co.edu.uptc.model.SelfLoopException;

/** Operaciones sobre el grafo, aplicando las reglas de negocio RN-01 a RN-07. */
public class GraphService {

    private final Graph graph = new Graph();

    public Graph getGraph() {
        return graph;
    }

    /** Crea un nodo con la primera letra libre (RN-05, RN-07). */
    public Node addNode(double x, double y) {
        if (graph.getNodes().size() >= AppConstants.MAX_NODES) {
            throw new MaxNodesException();
        }
        Node node = new Node(firstFreeName(), x, y);
        graph.addNode(node);
        return node;
    }

    /** Elimina un nodo junto con todas sus aristas (RN-06). */
    public void removeNode(Node node) {
        requireNode(node);
        for (Edge edge : graph.getEdgesOf(node)) {
            graph.removeEdge(edge);
        }
        graph.removeNode(node);
    }

    /** Crea una arista no dirigida (RN-01) validando bucle, peso y repetición (RN-02, RN-03, RN-04). */
    public Edge addEdge(Node a, Node b, int weight) {
        requireNode(a);
        requireNode(b);
        if (a == b) {
            throw new SelfLoopException();
        }
        validateWeight(weight);
        if (graph.findEdge(a, b) != null) {
            throw new DuplicateEdgeException();
        }
        Edge edge = new Edge(a, b, weight);
        graph.addEdge(edge);
        return edge;
    }

    public void removeEdge(Edge edge) {
        graph.removeEdge(edge);
    }

    public void changeWeight(Edge edge, int newWeight) {
        validateWeight(newWeight);
        edge.setWeight(newWeight);
    }

    public void clear() {
        graph.clear();
    }

    /**
     * Convierte el texto de un campo de peso en un entero positivo (RN-02).
     * Rechaza vacío, texto, decimales, cero y negativos.
     */
    public int parseWeight(String text) {
        if (text == null) {
            throw new InvalidWeightException();
        }
        int value;
        try {
            value = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new InvalidWeightException();
        }
        validateWeight(value);
        return value;
    }

    private void validateWeight(int weight) {
        if (weight <= 0) {
            throw new InvalidWeightException();
        }
    }

    private void requireNode(Node node) {
        if (node == null || !graph.containsNode(node)) {
            throw new IllegalArgumentException("El nodo no pertenece al grafo.");
        }
    }

    private String firstFreeName() {
        for (int i = 0; i < AppConstants.MAX_NODES; i++) {
            String name = String.valueOf((char) (AppConstants.FIRST_LETTER + i));
            if (graph.findNode(name) == null) {
                return name;
            }
        }
        throw new MaxNodesException();
    }
}
