package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.App;
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
import javafx.scene.control.ComboBox;
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
public class EstadisticasController extends Controller implements Initializable {

    @FXML
    private MFXTextField filterField;
    @FXML
    private ComboBox<Deporte> ComboBoxDeportes;
    @FXML
    private TableView<Equipo> tableView;
    @FXML
    private TableColumn<Equipo, Image> colImagen;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, String> colDeporte;
    @FXML
    private TableColumn<Equipo, Void> colVerStats;

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository equipoRepo = new EquipoRepository();
    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository deporteRepo = new DeporteRepository();
    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnActualizar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        colDeporte.setCellValueFactory(new PropertyValueFactory<>("tipoDeporte"));

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
                setGraphic(empty || item == null ? null : imageView);
                if (item != null) {
                    imageView.setImage(item);
                }
            }
        });

        agregarBotonVerStats();
        cargarJson();
        Filtrar();

    }

    private void cargarJson() {
        try {
            equiposLista.clear();
            for (Equipo e : equipoRepo.findAll()) {
                e.cargarImagenDesdeBase64();
                equiposLista.add(e);
            }
            tableView.setItems(equiposLista);
            tableView.refresh();
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }
    }

    private void Filtrar() {
        FilteredList<Equipo> filteredData = new FilteredList<>(equiposLista, p -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(filteredData);
        });

        ComboBoxDeportes.valueProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(filteredData);
        });

        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            List<Deporte> deportes = deporteRepo.findAll();

            if (deportes != null) {
                Deporte todos = new Deporte("Todos");
                todos.setNombre("Todos");
                ComboBoxDeportes.getItems().clear();
                ComboBoxDeportes.getItems().add(todos);
                ComboBoxDeportes.getItems().addAll(deportes);
                ComboBoxDeportes.getSelectionModel().select(todos); // seleccionar por defecto
                AppContext.getInstance().set("LISTA_DEPORTES", deportes);
            }
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }

        ComboBoxDeportes.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });

        ComboBoxDeportes.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });

        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

    }

    private void aplicarFiltro(FilteredList<Equipo> filteredData) {
        filteredData.setPredicate(equipo -> {
            String filtroNombre = filterField.getText();
            Deporte filtroDeporte = ComboBoxDeportes.getSelectionModel().getSelectedItem();

            boolean coincideNombre = (filtroNombre == null || filtroNombre.isEmpty()) || equipo.getNombre().toLowerCase().contains(filtroNombre.toLowerCase());
            boolean coincideDeporte = (filtroDeporte == null || "Todos".equalsIgnoreCase(filtroDeporte.getNombre())) || equipo.getTipoDeporte().equalsIgnoreCase(filtroDeporte.getNombre());

            return coincideNombre && coincideDeporte;
        });
    }

    private void agregarBotonVerStats() {
        colVerStats.setCellFactory(param -> new javafx.scene.control.TableCell<Equipo, Void>() {

            private final MFXButton btnVer = new MFXButton();

            {

                btnVer.setText(""); // Por si acaso, eliminar texto
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/ojo.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnVer.setGraphic(icono);
                btnVer.setStyle("-fx-background-color: transparent;");
                
                btnVer.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    AppContext.getInstance().set("EQUIPO_SELECCIONADO", equipo);
                    FlowController.getInstance().goViewInWindowModal("EstadisticasEquipo", ((Stage) root.getScene().getWindow()), false);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
              
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(btnVer);
                    hbox.setAlignment(Pos.CENTER); // <- centra el botón
                    hbox.setPrefWidth(Double.MAX_VALUE); // opcional para forzar ancho
                    setGraphic(hbox);
                }
            }
        });
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        cargarJson();
        Filtrar();
    }

}
