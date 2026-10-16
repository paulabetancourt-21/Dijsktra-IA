package co.edu.uptc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase de arranque de la aplicación.
 * Fase 0: solo abre una ventana vacía con título en español.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(), 900, 600);
        stage.setTitle("Visualizador del Algoritmo de Dijkstra");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
