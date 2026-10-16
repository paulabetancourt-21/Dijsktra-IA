package co.edu.uptc.presentation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Reloj de la animación: avanza un paso por tick (1 s a velocidad 1x).
 * Permite pausar, reanudar y cambiar la velocidad en cualquier momento.
 */
public class AlgorithmAnimator {

    private final Timeline timeline;

    public AlgorithmAnimator(Runnable onTick) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> onTick.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        timeline.playFromStart();
    }

    public void pause() {
        timeline.pause();
    }

    public void resume() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    /** 1.0 = normal; 2.0 = el doble de rápido; 0.5 = la mitad. */
    public void setRate(double rate) {
        timeline.setRate(rate);
    }

    public Animation.Status getStatus() {
        return timeline.getStatus();
    }
}