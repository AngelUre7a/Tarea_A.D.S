package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
    private FilteredList<Deporte> deportesFiltrados;

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
            private final MFXButton btnEditar = new MFXButton();

            {
                 Sonidos.click();
                btnEditar.setText("");
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/editar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnEditar.setGraphic(icono);
                btnEditar.getStyleClass().add("boton-tabla-icono");
                btnEditar.setStyle("-fx-background-color: transparent;");

                btnEditar.setOnAction(event -> {
                     Sonidos.click();
                    Deporte deporte = getTableView().getItems().get(getIndex());
                    if (deporte.getCantidadTorneosInscritos() > 0) {
                        new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede editar ya que actualmente esta participando en un torneo.");

                    } else {
                        System.out.println("entrando a editar deporte " + deporte.getCantidadTorneosInscritos());
                        abrirFormularioEditar(deporte);
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
                 Sonidos.click();
                btnEliminar.setText("");
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/borrar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnEliminar.setGraphic(icono);
                btnEliminar.getStyleClass().add("boton-tabla-icono");
                btnEliminar.setStyle("-fx-background-color: transparent;");
                btnEliminar.setOnAction(event -> {
                    Deporte deporte = getTableView().getItems().get(getIndex());
                    String nombreDeporte = deporte.getNombre();
                    System.out.println("Nombre del deporte a eliminar: " + nombreDeporte);
                    try {
                        equiposLista.clear();
                        equiposLista.addAll(Equiporepo.findAll());
                    } catch (IOException e) {
                        new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar equipos", "No se pudo cargar la lista de equipos.");
                        e.printStackTrace();
                    }
                    boolean siTieneEquiposAsociados = equiposLista.stream().anyMatch(equipo -> equipo.getTipoDeporte().equals(nombreDeporte));
                    if (siTieneEquiposAsociados) {
                        new Mensaje().show(Alert.AlertType.ERROR, "Error al eliminar deporte", "No se puede eliminar el deporte porque tiene equipos asociados.");
                    } else {
                        if (new Mensaje().showConfirmation("Confirmación", "¿Está seguro de eliminar el deporte?")) {
                            try {
                                Deporterepo.deleteById(deporte.getId());
                            } catch (IOException ex) {
                                Logger.getLogger(RegistroListaDeporteBalonController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            deportesLista.remove(deporte);
                            tableView.refresh();
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
            deportesLista.clear();
            for (Deporte d : Deporterepo.findAll()) {
                d.cargarImagenDesdeBase64();
                deportesLista.add(d);
            }
            deportesFiltrados = new FilteredList<>(deportesLista, p -> true);
            tableView.setItems(deportesFiltrados);
            tableView.refresh();
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }
    }

    private void aplicarFiltro() {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            deportesFiltrados.setPredicate(deporte -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                return deporte.getNombre().toLowerCase().contains(newValue.toLowerCase());
            });
        });
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) {
         Sonidos.click();
        abrirFormularioNuevo();
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
         Sonidos.click();
        cargarFormulario();
    }

    @Override
    public void initialize() {
    }
}
