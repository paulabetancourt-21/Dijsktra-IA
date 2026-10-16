package co.edu.uptc.presentation;

import co.edu.uptc.model.DijkstraStep;
import co.edu.uptc.model.Node;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/** Tabla con la distancia y el nodo previo de cada nodo en el paso actual. */
public class DistanceTableView extends TableView<DistanceTableView.Row> {

    public record Row(String node, String distance, String previous) {
    }

    public DistanceTableView() {
        TableColumn<Row, String> nodeColumn = new TableColumn<>("Nodo");
        nodeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().node()));

        TableColumn<Row, String> distanceColumn = new TableColumn<>("Distancia");
        distanceColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().distance()));

        TableColumn<Row, String> previousColumn = new TableColumn<>("Previo");
        previousColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().previous()));

        for (TableColumn<Row, String> column : List.of(nodeColumn, distanceColumn, previousColumn)) {
            column.setSortable(false);
            column.setPrefWidth(75);
            getColumns().add(column);
        }

        setPlaceholder(new Label("Sin ejecución"));
        setPrefWidth(250);
        setMinWidth(250);
    }

    public void showStep(DijkstraStep step) {
        List<Row> rows = new ArrayList<>();
        for (Node node : step.getDistances().keySet()) {
            Node previous = step.getPrevious().get(node);
            rows.add(new Row(
                    node.getName(),
                    DistanceFormat.of(step.getDistances().get(node)),
                    previous == null ? "-" : previous.getName()));
        }
        getItems().setAll(rows);
    }

    public void clear() {
        getItems().clear();
    }
}