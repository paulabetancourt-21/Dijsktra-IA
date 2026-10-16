package co.edu.uptc;

import co.edu.uptc.presentation.MainView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new MainView(), 1000, 650);
        stage.setTitle("Visualizador del Algoritmo de Dijkstra");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}