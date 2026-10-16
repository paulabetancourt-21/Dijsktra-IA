package co.edu.uptc.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uptc.model.AppConstants;
import co.edu.uptc.model.DijkstraResult;
import co.edu.uptc.model.Node;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DijkstraAlgorithmTest {

    private GraphService service;
    private DijkstraAlgorithm algorithm;

    @BeforeEach
    void setUp() {
        service = new GraphService();
        algorithm = new DijkstraAlgorithm();
    }

    /** Crea n nodos (A, B, C...) y los devuelve en orden. */
    private List<Node> createNodes(int n) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(service.addNode(i * 10, 0));
        }
        return nodes;
    }

    private List<String> names(List<Node> path) {
        return path.stream().map(Node::getName).toList();
    }

    /**
     * Grafo conocido: A-B 4, A-C 2, B-C 1, B-D 5, C-D 8, C-E 10, D-E 2, D-F 6, E-F 3.
     * Distancias desde A: A=0, B=3, C=2, D=8, E=10, F=13.
     */
    private List<Node> buildKnownGraph() {
        List<Node> n = createNodes(6); // A..F
        service.addEdge(n.get(0), n.get(1), 4);
        service.addEdge(n.get(0), n.get(2), 2);
        service.addEdge(n.get(1), n.get(2), 1);
        service.addEdge(n.get(1), n.get(3), 5);
        service.addEdge(n.get(2), n.get(3), 8);
        service.addEdge(n.get(2), n.get(4), 10);
        service.addEdge(n.get(3), n.get(4), 2);
        service.addEdge(n.get(3), n.get(5), 6);
        service.addEdge(n.get(4), n.get(5), 3);
        return n;
    }

    @Test
    void shortestPathInSmallGraph() {
        List<Node> n = buildKnownGraph();
        DijkstraResult result = algorithm.run(service.getGraph(), n.get(0), n.get(5));

        assertEquals(List.of("A", "C", "B", "D", "E", "F"), names(result.getPath()));
        assertEquals(13, result.getDistance(n.get(5)));
        assertFalse(result.getSteps().isEmpty());
    }

    @Test
    void cheapestPathWinsEvenWithMoreEdges() {
        List<Node> n = createNodes(4); // A, B, C, D
        service.addEdge(n.get(0), n.get(3), 10); // directo: costo 10
        service.addEdge(n.get(0), n.get(1), 2);
        service.addEdge(n.get(1), n.get(2), 2);
        service.addEdge(n.get(2), n.get(3), 2);  // A-B-C-D: costo 6

        DijkstraResult result = algorithm.run(service.getGraph(), n.get(0), n.get(3));

        assertEquals(6, result.getDistance(n.get(3)));
        assertEquals(List.of("A", "B", "C", "D"), names(result.getPath()));
    }

    @Test
    void distancesToAllNodes() {
        List<Node> n = buildKnownGraph();
        DijkstraResult result = algorithm.run(service.getGraph(), n.get(0), null);

        int[] expected = {0, 3, 2, 8, 10, 13};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.getDistance(n.get(i)), "Distancia a " + n.get(i));
        }
        assertTrue(result.getPath().isEmpty()); // sin destino no hay camino
    }

    @Test
    void unreachableNodeHasInfiniteDistance() {
        List<Node> n = createNodes(3); // A, B, C
        service.addEdge(n.get(0), n.get(1), 5); // C queda aislado

        DijkstraResult result = algorithm.run(service.getGraph(), n.get(0), n.get(2));

        assertEquals(AppConstants.INFINITY, result.getDistance(n.get(2)));
        assertFalse(result.isReachable(n.get(2)));
        assertTrue(result.getPath().isEmpty());
        assertEquals(5, result.getDistance(n.get(1)));
    }

    @Test
    void sourceEqualsDestinationHasDistanceZero() {
        List<Node> n = createNodes(2);
        service.addEdge(n.get(0), n.get(1), 7);

        DijkstraResult result = algorithm.run(service.getGraph(), n.get(0), n.get(0));

        assertEquals(0, result.getDistance(n.get(0)));
        assertEquals(List.of("A"), names(result.getPath()));
    }
}
