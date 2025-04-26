package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ListaTorneoController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXTextField filterField;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private TableView<Torneo> tableView;
    @FXML
    private TableColumn<Torneo, String> colID;
    @FXML
    private TableColumn<Torneo, String> colNombre;
    @FXML
    private TableColumn<Torneo, String> colCategoria;
    @FXML
    private TableColumn<Torneo, Integer> colEquiposRegistrados;
    @FXML
    private TableColumn<Torneo, Integer> colTiempo;
    @FXML
    private TableColumn<Torneo, String> colIniciar;
    @FXML
    private TableColumn<Torneo, String> colEstado;
    @FXML
    private TableColumn<Torneo, Void> colEliminar;
    @FXML
    private MFXButton btnActualizar;

    private final ObservableList<Torneo> torneoLista = FXCollections.observableArrayList();
    private final TorneoRepository Torneorepo = new TorneoRepository();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarBotones();
        configurarFiltro();
        cargarFormulario();
    }

    private void configurarColumnas() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("tipoDeporte"));
        colEquiposRegistrados.setCellValueFactory(new PropertyValueFactory<>("cantidadEquipos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoPorPartida"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarBotones() {
        colIniciar.setCellFactory(column -> new TableCell<>() {
            private final MFXButton btnIniciar = new MFXButton("");

            {
                 Sonidos.click();
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/iniciar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnIniciar.setGraphic(icono);
                btnIniciar.getStyleClass().add("boton-tabla-icono");
                btnIniciar.setStyle("-fx-background-color: transparent;");

                btnIniciar.setOnAction(event -> {
                     Sonidos.click();
                    Torneo torneo = getTableView().getItems().get(getIndex());
                    if ("finalizado".equalsIgnoreCase(torneo.getEstado())) {
                        new Mensaje().show(Alert.AlertType.INFORMATION, "Torneo finalizado", "Este torneo ya terminó y no se puede volver a jugar.");
                        return;
                    }
                    AppContext.getInstance().set("TORNEO", torneo);
                    FlowController.getInstance().goView("Llaves");
                    LlavesController controller = (LlavesController) FlowController.getInstance().getController("Llaves");
                    if (controller != null) controller.onShow();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Torneo torneo = getTableView().getItems().get(getIndex());
                    btnIniciar.setDisable("finalizado".equalsIgnoreCase(torneo.getEstado()));
                    HBox hbox = new HBox(btnIniciar);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void configurarFiltro() {
        FilteredList<Torneo> filteredData = new FilteredList<>(torneoLista, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(torneo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return (torneo.getId() != null && torneo.getId().toLowerCase().contains(lowerCaseFilter)) ||
                       (torneo.getNombre() != null && torneo.getNombre().toLowerCase().contains(lowerCaseFilter)) ||
                       (torneo.getTipoDeporte() != null && torneo.getTipoDeporte().toLowerCase().contains(lowerCaseFilter)) ||
                       (torneo.getEstado() != null && torneo.getEstado().toLowerCase().contains(lowerCaseFilter));
            });
        });

        SortedList<Torneo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void cargarFormulario() {
        try {
            torneoLista.clear();
            torneoLista.addAll(Torneorepo.findAll());
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar torneos", "No se pudo cargar la lista de torneos.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) throws IOException {
         Sonidos.click();
        FlowController.getInstance().goViewInWindowModal("CreacionTorneo", ((Stage) root.getScene().getWindow()), false);
        if (AppContext.getInstance().containsItem("TORNEO_NUEVO")) {
            Torneo nuevo = (Torneo) AppContext.getInstance().get("TORNEO_NUEVO");
            if (nuevo != null) {
                Torneorepo.save(nuevo);
                torneoLista.add(nuevo);
                AppContext.getInstance().delete("TORNEO_NUEVO");
            }
        }
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
