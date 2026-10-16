package co.edu.uptc.model;

/** Error de regla de negocio del grafo. */
public class SelfLoopException extends RuntimeException {

    public SelfLoopException() {
        super("No se permiten aristas de un nodo hacia sí mismo.");
    }

    public SelfLoopException(String message) {
        super(message);
    }
}
