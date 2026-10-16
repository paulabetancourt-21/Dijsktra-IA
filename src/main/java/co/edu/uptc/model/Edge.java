package co.edu.uptc.model;

/** Arista no dirigida entre dos nodos, con un peso entero positivo. */
public class Edge {

    private final Node nodeA;
    private final Node nodeB;
    private int weight;

    public Edge(Node nodeA, Node nodeB, int weight) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.weight = weight;
    }

    public Node getNodeA() {
        return nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /** Indica si la arista une esos dos nodos, en cualquier orden. */
    public boolean connects(Node a, Node b) {
        return (nodeA == a && nodeB == b) || (nodeA == b && nodeB == a);
    }

    /** Indica si la arista toca al nodo dado. */
    public boolean touches(Node node) {
        return nodeA == node || nodeB == node;
    }

    /** Devuelve el nodo del otro extremo, o null si el nodo no pertenece a la arista. */
    public Node getOther(Node node) {
        if (node == nodeA) {
            return nodeB;
        }
        if (node == nodeB) {
            return nodeA;
        }
        return null;
    }

    @Override
    public String toString() {
        return nodeA + "-" + nodeB + " (" + weight + ")";
    }
}
