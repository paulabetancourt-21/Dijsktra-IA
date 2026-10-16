package co.edu.uptc.model;

/** Error de regla de negocio del grafo. */
public class InvalidWeightException extends RuntimeException {

    public InvalidWeightException() {
        super("El peso debe ser un número entero positivo.");
    }

    public InvalidWeightException(String message) {
        super(message);
    }
}
