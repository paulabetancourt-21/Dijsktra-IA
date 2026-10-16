package co.edu.uptc.presentation;

import co.edu.uptc.model.AppConstants;

/** Convierte una distancia en texto; la distancia infinita se muestra como ∞. */
public final class DistanceFormat {

    private DistanceFormat() {
    }

    public static String of(int distance) {
        return distance == AppConstants.INFINITY ? "\u221E" : String.valueOf(distance);
    }
}