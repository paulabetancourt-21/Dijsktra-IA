package co.edu.uptc.model;

/** Error de regla de negocio del grafo. */
public class MaxNodesException extends RuntimeException {

    public MaxNodesException() {
        super("No se pueden crear más de 26 nodos.");
    }

    public MaxNodesException(String message) {
        super(message);
    }
}
