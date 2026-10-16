package co.edu.uptc.presentation;

import co.edu.uptc.business.DijkstraAlgorithm;
import co.edu.uptc.business.GraphService;
import co.edu.uptc.model.DijkstraStep;
import co.edu.uptc.model.Node;

import java.util.List;
import javafx.animation.Animation;
import javafx.scene.control.Label;

/** Maneja los botones de ejecución y coordina el algoritmo, el grafo, la tabla y la animación. */
public class ExecutionHandler {

    private static final String DEFAULT_STATUS =
            "Elige el origen y el destino, y luego pulsa \"Siguiente paso\" o \"Animar\".";

    private final GraphService service;
    private final GraphCanvas canvas;
    private final CanvasMouseHandler mouseHandler;
    private final ModeToolbar modeToolbar;
    private final ExecutionToolbar toolbar;
    private final DistanceTableView table;
    private final Label statusLabel;

    private final DijkstraAlgorithm algorithm = new DijkstraAlgorithm();
    private final AlgorithmAnimator animator;

    private List<DijkstraStep> steps; // null = no hay ejecución
    private int index = -1;

    public ExecutionHandler(GraphService service, GraphCanvas canvas, CanvasMouseHandler mouseHandler,
                            ModeToolbar modeToolbar, ExecutionToolbar toolbar,
                            DistanceTableView table, Label statusLabel) {
        this.service = service;
        this.canvas = canvas;
        this.mouseHandler = mouseHandler;
        this.modeToolbar = modeToolbar;
        this.toolbar = toolbar;
        this.table = table;
        this.statusLabel = statusLabel;
        this.animator = new AlgorithmAnimator(this::onTick);

        toolbar.getPickSource().setOnAction(e -> startPicking(true));
        toolbar.getPickDestination().setOnAction(e -> startPicking(false));
        toolbar.getNext().setOnAction(e -> onNext());
        toolbar.getAnimate().setOnAction(e -> onAnimate());
        toolbar.getPause().setOnAction(e -> onPause());
        toolbar.getReset().setOnAction(e -> reset());
        toolbar.getSpeed().valueProperty().addListener((obs, old, now) -> animator.setRate(now.doubleValue()));

        // Si el grafo se redibuja (por una edición), cualquier resultado anterior deja de ser válido.
        canvas.setOnRefresh(this::reset);
        reset();
    }

    // ---------- Elegir origen y destino ----------

    private void startPicking(boolean isSource) {
        statusLabel.setText("Haz clic sobre un nodo para elegirlo como "
                + (isSource ? "origen" : "destino") + " (un clic en vacío cancela).");
        mouseHandler.pickNode(node -> {
            if (node == null) {
                statusLabel.setText("Selección cancelada.");
                return;
            }
            reset();
            if (isSource) {
                canvas.setSource(node);
            } else {
                canvas.setDestination(node);
            }
            statusLabel.setText((isSource ? "Origen" : "Destino") + " elegido: " + node.getName() + ".");
        });
    }

    // ---------- Ejecución ----------

    /** Inicia la ejecución si aún no existe. Devuelve false si faltan origen o destino. */
    private boolean start() {
        if (steps != null) {
            return true;
        }
        mouseHandler.pickNode(null);
        Node source = canvas.getSource();
        Node destination = canvas.getDestination();
        if (source == null || destination == null) {
            Alerts.warning("Para ejecutar el algoritmo debes elegir el nodo origen y el nodo destino.");
            return false;
        }
        steps = algorithm.run(service.getGraph(), source, destination).getSteps();
        index = 0;
        setLocked(true);
        showStep(steps.get(0)); // estado inicial: todos en ∞ y el origen en 0
        updateButtons();
        return true;
    }

    private void onNext() {
        if (steps == null) {
            start(); // la primera pulsación muestra el estado inicial
            return;
        }
        advance();
    }

    private void onAnimate() {
        if (!start() || isFinished()) {
            return;
        }
        animator.start();
        updateButtons();
    }

    private void onPause() {
        Animation.Status status = animator.getStatus();
        if (status == Animation.Status.RUNNING) {
            animator.pause();
        } else if (status == Animation.Status.PAUSED) {
            animator.resume();
        }
        updateButtons();
    }

    private void onTick() {
        advance();
    }

    private void advance() {
        if (steps == null || isFinished()) {
            return;
        }
        index++;
        showStep(steps.get(index));
        if (isFinished()) {
            finish();
        }
        updateButtons();
    }

    private void finish() {
        animator.stop();
        setLocked(false);
        statusLabel.setText(statusLabel.getText() + " Pulsa \"Reiniciar\" para volver al estado inicial.");
    }

    private void reset() {
        animator.stop();
        steps = null;
        index = -1;
        canvas.clearExecution();
        table.clear();
        statusLabel.setText(DEFAULT_STATUS);
        setLocked(false);
        updateButtons();
    }

    // ---------- Apoyo ----------

    private void showStep(DijkstraStep step) {
        canvas.applyStep(step);
        table.showStep(step);
        statusLabel.setText(step.getDescription());
    }

    private boolean isFinished() {
        return steps != null && index == steps.size() - 1;
    }

    private boolean isInProgress() {
        return steps != null && index < steps.size() - 1;
    }

    /** Bloquea o habilita la edición del grafo (RF-34). */
    private void setLocked(boolean locked) {
        mouseHandler.setLocked(locked);
        modeToolbar.setDisable(locked);
    }

    private void updateButtons() {
        Animation.Status status = animator.getStatus();
        boolean playing = status == Animation.Status.RUNNING;
        boolean paused = status == Animation.Status.PAUSED;
        boolean finished = isFinished();

        toolbar.getPickSource().setDisable(isInProgress());
        toolbar.getPickDestination().setDisable(isInProgress());
        toolbar.getNext().setDisable(finished || playing);
        toolbar.getAnimate().setDisable(finished || playing || paused);
        toolbar.getPause().setDisable(!(playing || paused));
        toolbar.getPause().setText(paused ? "Reanudar" : "Pausar");
    }
}