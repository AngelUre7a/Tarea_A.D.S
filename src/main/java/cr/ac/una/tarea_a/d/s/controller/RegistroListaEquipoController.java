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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
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
    private TableColumn<Equipo, Void> colEditar;
    @FXML
    private TableColumn<Equipo, Void> colEliminar;
    @FXML
    private MFXButton btnActualizar;

    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository Deporterepo = new DeporteRepository();
    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDeporte.setCellValueFactory(new PropertyValueFactory<>("tipoDeporte"));
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
            private final MFXButton btnEditar = new MFXButton();

            {
                btnEditar.setText(""); // Elimina texto por defecto
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/editar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnEditar.setGraphic(icono);
                btnEditar.setStyle("-fx-background-color: transparent;");

                btnEditar.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
//                    Equiporepo.findById(equipo.getId()):
                    if (equipo.isEnTorneo()) {
                         new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede editar ya que actualmente esta participando en un torneo.");
                    
                     } else {
                    abrirFormularioEditar(equipo);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(btnEditar);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.setPrefWidth(Double.MAX_VALUE);
                    setGraphic(hbox);
                }
            }
        });

        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final MFXButton btnEliminar = new MFXButton();

            {
                btnEliminar.setText("");
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/borrar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnEliminar.setGraphic(icono);
                btnEliminar.setStyle("-fx-background-color: transparent;");

                btnEliminar.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());

                    if (new Mensaje().showConfirmation("Confirmación", "¿Está seguro de eliminar el equipo?")) {
                        try {
                            Equiporepo.deleteById(equipo.getId());
                            equiposLista.remove(equipo);
                            tableView.refresh();
                        } catch (IllegalArgumentException ex) {
                            new Mensaje().show(Alert.AlertType.WARNING, "No se pudo eliminar", ex.getMessage());
                        } catch (IOException ex) {
                            Logger.getLogger(RegistroListaEquipoController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                });
            }

            @Override

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(btnEliminar);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.setPrefWidth(Double.MAX_VALUE);
                    setGraphic(hbox);
                }
            }
        });

        cargarFormulario();
        aplicarFiltro();

    }

    private void cargarFormulario() {
        try {
            equiposLista.clear();
            for (Equipo e : Equiporepo.findAll()) {
                e.cargarImagenDesdeBase64();
                equiposLista.add(e);
            }
            tableView.setItems(equiposLista);
            tableView.refresh();
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar equipos", "No se pudo cargar la lista de equipos.");
            e.printStackTrace();
        }
    }

    private void aplicarFiltro() {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                tableView.setItems(equiposLista);
            } else {
                ObservableList<Equipo> filteredList = FXCollections.observableArrayList();
                for (Equipo equipo : equiposLista) {
                    if (equipo.getNombre().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(equipo);
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
        AppContext.getInstance().delete("EQUIPO_EDITAR");

        FlowController.getInstance().goViewInWindowModal("RegistroEquipo", ((Stage) root.getScene().getWindow()), false);
        cargarFormulario();

        if (AppContext.getInstance().containsItem("EQUIPO_EDITAR")) {
            Equipo nuevo = (Equipo) AppContext.getInstance().get("EQUIPO_EDITAR");

            try {
                Equiporepo.save(nuevo);
                nuevo.cargarImagenDesdeBase64();
                equiposLista.add(nuevo);
                tableView.refresh();
                cargarFormulario();
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al guardar equipo", "No se pudo guardar el nuevo equipo.");
            }

            AppContext.getInstance().delete("EQUIPO_EDITAR");
        }
    }

    private void abrirFormularioEditar(Equipo equipo) {
        AppContext.getInstance().set("EQUIPO_EDITAR", equipo);

        FlowController.getInstance().goViewInWindowModal("RegistroEquipo", ((Stage) root.getScene().getWindow()), false);
        cargarFormulario();

        AppContext.getInstance().delete("EQUIPO_EDITAR");
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        tableView.refresh();
    }
}
