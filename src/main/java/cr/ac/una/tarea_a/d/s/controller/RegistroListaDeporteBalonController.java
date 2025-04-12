package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
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
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RegistroListaDeporteBalonController extends Controller implements Initializable {

    @FXML
    private MFXTextField filterField;
    @FXML
    private TableView<Deporte> tableView;
    @FXML
    private TableColumn<Deporte, String> colID;
    @FXML
    private TableColumn<Deporte, String> colNombre;
    @FXML
    private TableColumn<Deporte, Image> colImagen;
    @FXML
    private TableColumn<Deporte, Void> colEditar;
    @FXML
    private TableColumn<Deporte, Void> colEliminar;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private MFXButton btnActualizar;
    @FXML
    private AnchorPane root;

    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository Deporterepo = new DeporteRepository();
    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));

        colImagen.setCellFactory(column -> new TableCell<>() {
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
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });

        colEditar.setCellFactory(param -> new TableCell<>() {
            private final MFXButton btnEditar = new MFXButton("Editar");

            {
                btnEditar.setOnAction(event -> {
                    Deporte deporte = getTableView().getItems().get(getIndex());
                    abrirFormularioEditar(deporte);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEditar);
                }
            }
        });

        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final MFXButton btnEliminar = new MFXButton("Eliminar");

            {
                btnEliminar.setOnAction(event -> {
                    Deporte deporte = getTableView().getItems().get(getIndex());

                    if (new Mensaje().showConfirmation("Confirmación", "¿Está seguro de eliminar el deporte?")) {
                        try {
                            Deporterepo.deleteById(deporte.getId());
                        } catch (IOException ex) {
                            Logger.getLogger(RegistroListaDeporteBalonController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        deportesLista.remove(deporte);
                        tableView.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });

        cargarFormulario();
        aplicarFiltro();
    }

    private void cargarFormulario() {
        try {
            deportesLista.clear();
            for (Deporte d : Deporterepo.findAll()) {
                d.cargarImagenDesdeBase64();
                deportesLista.add(d);
            }
            tableView.setItems(deportesLista);
            tableView.refresh();
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }
    }

    private void aplicarFiltro() {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                tableView.setItems(deportesLista);
            } else {
                ObservableList<Deporte> filteredList = FXCollections.observableArrayList();
                for (Deporte deporte : deportesLista) {
                    if (deporte.getNombre().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(deporte);
                    }
                }
                tableView.setItems(filteredList);
            }
        });
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) {
        abrirFormularioNuevo();
    }

    @Override
    public void initialize() {
    }

    private void abrirFormularioNuevo() {
        AppContext.getInstance().delete("DEPORTE_EDITAR");

        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
        cargarFormulario();

        if (AppContext.getInstance().containsItem("DEPORTE_EDITAR")) {
            Deporte nuevo = (Deporte) AppContext.getInstance().get("DEPORTE_EDITAR");

            try {
                Deporterepo.save(nuevo);
                nuevo.cargarImagenDesdeBase64();
                deportesLista.add(nuevo);
                tableView.refresh();
                cargarFormulario();
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al guardar deporte", "No se pudo guardar el nuevo deporte.");
            }

            AppContext.getInstance().delete("DEPORTE_EDITAR");
        }
    }

    private void abrirFormularioEditar(Deporte deporte) {
        AppContext.getInstance().set("DEPORTE_EDITAR", deporte);

        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
        cargarFormulario();

        AppContext.getInstance().delete("DEPORTE_EDITAR");

    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        cargarFormulario();
    }
}
