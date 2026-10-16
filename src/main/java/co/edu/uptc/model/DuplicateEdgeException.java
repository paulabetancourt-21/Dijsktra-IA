package co.edu.uptc.model;

/** Error de regla de negocio del grafo. */
public class DuplicateEdgeException extends RuntimeException {

    public DuplicateEdgeException() {
        super("Ya existe una arista entre esos dos nodos.");
    }

    public DuplicateEdgeException(String message) {
        super(message);
    }
}
