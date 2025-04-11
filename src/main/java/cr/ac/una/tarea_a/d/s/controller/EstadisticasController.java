package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.App;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private TableColumn<Equipo, Integer> colPuntos;
    @FXML
    private TableColumn<Equipo, Integer> colGoles;

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    // --- Configuración de columnas ---
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
    colDeporte.setCellValueFactory(new PropertyValueFactory<>("categoria"));

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
            if (item != null) imageView.setImage(item);
        }
    });

    // --- Lista observable y carga de equipos ---
    try {
        equiposLista.addAll(Equiporepo.findAll());
    } catch (IOException e) {
        new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los deportes.");
    }

    // --- Filtro combinado (nombre + deporte) ---
    FilteredList<Equipo> filteredData = new FilteredList<>(equiposLista, p -> true);

    filterField.textProperty().addListener((obs, oldVal, newVal) -> {
        aplicarFiltro(filteredData);
    });

    ComboBoxDeportes.valueProperty().addListener((obs, oldVal, newVal) -> {
        aplicarFiltro(filteredData);
    });

    SortedList<Equipo> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(tableView.comparatorProperty());
    tableView.setItems(sortedData);

    // --- Cargar deportes con opción "Todos" ---
    try {
        DeporteRepository deporteRepo = new DeporteRepository();
        List<Deporte> deportes = deporteRepo.findAll();

        if (deportes != null) {
            Deporte todos = new Deporte("Todos");
            todos.setNombre("Todos");
            ComboBoxDeportes.getItems().add(todos);
            ComboBoxDeportes.getItems().addAll(deportes);
            ComboBoxDeportes.getSelectionModel().select(todos); // seleccionar por defecto
            AppContext.getInstance().set("LISTA_DEPORTES", deportes);
        }
    } catch (IOException e) {
        new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
        e.printStackTrace();
    }

    // --- Mostrar nombres en ComboBox ---
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
    
    @Override
    public void initialize() {
    }
    
}
