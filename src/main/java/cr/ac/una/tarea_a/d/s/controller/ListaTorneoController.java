package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
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
    private MFXButton btnActualizar;

    private final ObservableList<Torneo> torneoLista = FXCollections.observableArrayList();
    private final TorneoRepository Torneorepo = new TorneoRepository();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("tipoDeporte"));
        colEquiposRegistrados.setCellValueFactory(new PropertyValueFactory<>("cantidadEquipos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoPorPartida"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colIniciar.setCellFactory(column -> new javafx.scene.control.TableCell<Torneo, String>() {
            private final MFXButton btnIniciar = new MFXButton("");

            {// Configuración visual del botón
                btnIniciar.setText(""); // Eliminar texto
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/iniciar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnIniciar.setGraphic(icono);
                btnIniciar.getStyleClass().add("boton-tabla-icono");
                btnIniciar.setStyle("-fx-background-color: transparent;");

                // Acción del botón
                btnIniciar.setOnAction(event -> {
                    for(int i=0;i<50;i++){
                        System.out.println();
                    }
                    Torneo torneo = getTableView().getItems().get(getIndex());

                    if ("finalizado".equalsIgnoreCase(torneo.getEstado())) {
                        new Mensaje().show(Alert.AlertType.INFORMATION, "Torneo finalizado", "Este torneo ya terminó y no se puede volver a jugar.");
                        return;
                    }

                    AppContext.getInstance().set("TORNEO", torneo);
                    FlowController.getInstance().goView("Llaves");
                    LlavesController controller = (LlavesController) FlowController.getInstance().getController("Llaves");

                    if (controller != null) {
                        controller.onShow();
                    }
                });

            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Torneo torneo = getTableView().getItems().get(getIndex());
                    if ("finalizado".equalsIgnoreCase(torneo.getEstado())) {
                        btnIniciar.setDisable(true); // bloquear botón si ya terminó
                    } else {
                        btnIniciar.setDisable(false); // habilitar si está pendiente o en curso
                    }

                    HBox hbox = new HBox(btnIniciar);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.setPrefWidth(Double.MAX_VALUE);
                    setGraphic(hbox);
                }
            }

        });
        cargarFormulario();

        FilteredList<Torneo> filteredData = new FilteredList<>(torneoLista, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Torneo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Torneo.getTipoDeporte().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Torneo> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) throws IOException {
        FlowController.getInstance().goViewInWindowModal("CreacionTorneo", ((Stage) root.getScene().getWindow()), false);
        if (AppContext.getInstance().containsItem("TORNEO_NUEVO")) {
            // Obtener el nuevo deporte desde el AppContext

            Torneo nuevo = (Torneo) AppContext.getInstance().get("TORNEO_NUEVO");
            // Asegúrate de que la lista no se reinicie al agregar
            if (nuevo != null) {
                // Guardar el nuevo deporte en el repositorio
                Torneorepo.save(nuevo);
                List<Torneo> torneos = (List<Torneo>) AppContext.getInstance().get("LISTA_TORNEOS");

                // Agregar el nuevo deporte a la lista sin eliminar los anteriores
                torneoLista.add(nuevo);
                torneoLista.clear();
                torneoLista.addAll(torneos);
                cargarFormulario();
                AppContext.getInstance().delete("TORNEO_NUEVO"); // Limpiar el contexto
            }
        }
    }

    @Override
    public void initialize() {
        cargarFormulario();
    }

    private void cargarFormulario() {

        try {
            torneoLista.clear();
            for (Torneo t : Torneorepo.findAll()) {
                torneoLista.add(t);
            }
            tableView.setItems(torneoLista);
            tableView.refresh();
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar torneos", "No se pudo cargar la lista de torneos.");
            e.printStackTrace();

        }
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
    }

    
}
