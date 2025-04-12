package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegistroListaEquipoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnAgregar;
    @FXML
    private AnchorPane root;
    @FXML
    private MFXTextField filterField;
    @FXML
    private TableView<Equipo> tableView;
    @FXML
    private TableColumn<Equipo, String> colID;
    @FXML
    private TableColumn<Equipo, String> colDeporte;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, Image> colImagen;
    @FXML
    private TableColumn<Equipo, String> colEditar;
    @FXML
    private TableColumn<Equipo, String> colEliminar;

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    @FXML
    private MFXButton btnActualizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDeporte.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));

        colImagen.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    System.out.println("No se cargo la imagen del equipo");
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });

        colEliminar.setCellFactory(column -> new javafx.scene.control.TableCell<Equipo, String>() {
            private final MFXButton btnEliminar = new MFXButton("Eliminar");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnEliminar.setOnAction(event -> {
                        Equipo equipoSeleccionado = getTableView().getItems().get(getIndex());
                        equiposLista.remove(equipoSeleccionado);
                        try {
                            Equiporepo.deleteById(equipoSeleccionado.getId());
                        } catch (IOException ex) {
                            new Mensaje().show(Alert.AlertType.ERROR, "Error al eliminar equipo", "No se pudo eliminar el equipo.");
                        }
                        AppContext.getInstance().delete("EQUIPO_" + equipoSeleccionado.getId());
                        new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "El Equipo se ha eliminado correctamente.");
                    });
                    setGraphic(btnEliminar);
                }
            }
        });

        colEditar.setCellFactory(column -> new javafx.scene.control.TableCell<Equipo, String>() {
            private final MFXButton btnEditar = new MFXButton("Editar");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnEditar.setOnAction(event -> {
                        Equipo equipoSeleccionado = getTableView().getItems().get(getIndex());
                        AppContext.getInstance().set("EQUIPO_EDITAR", equipoSeleccionado);
                        FlowController.getInstance().goViewInWindowModal("RegistroEquipo", ((Stage) root.getScene().getWindow()), false);
                    });
                    setGraphic(btnEditar);
                }
            }
        });

        FilteredList<Equipo> filteredData = new FilteredList<>(equiposLista, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(equipo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return equipo.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
        });

        try {
            for (Equipo e : Equiporepo.findAll()) {
                e.cargarImagenDesdeBase64(); // ← reconstruye la imagen en memoria
                equiposLista.add(e);
            }
           equiposLista.addAll(Equiporepo.findAll());
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los equipos.");
        }

        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) throws IOException {
        FlowController.getInstance().goViewInWindowModal("RegistroEquipo", ((Stage) root.getScene().getWindow()), false);

        if (AppContext.getInstance().containsItem("EQUIPO_NUEVO")) {
            Equipo nuevo = (Equipo) AppContext.getInstance().get("EQUIPO_NUEVO");
            if (nuevo != null) {
                Equiporepo.save(nuevo);

                equiposLista.add(nuevo);
                AppContext.getInstance().delete("EQUIPO_NUEVO");
            }
        }
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        tableView.refresh();
    }
}
