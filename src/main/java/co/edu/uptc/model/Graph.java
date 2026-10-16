package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Grafo no dirigido: lista de nodos y de aristas.
 * Solo guarda datos; las reglas las aplica GraphService.
 */
public class Graph {

    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }

    public boolean containsNode(Node node) {
        return nodes.contains(node);
    }

    public Node findNode(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    /** Busca la arista entre dos nodos (en cualquier orden); null si no existe. */
    public Edge findEdge(Node a, Node b) {
        for (Edge edge : edges) {
            if (edge.connects(a, b)) {
                return edge;
            }
        }
        return null;
    }

    /** Aristas conectadas a un nodo. */
    public List<Edge> getEdgesOf(Node node) {
        List<Edge> result = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.touches(node)) {
                result.add(edge);
            }
        }
        return result;
    }
}
