package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoGenerales;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoGeneralesRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RankingController extends Controller implements Initializable {

    @FXML
    private MFXTextField filterField;
    @FXML
    private TableView<Equipo> tableView;
    @FXML
    private ComboBox<Deporte> ComboBoxDeportes;
    @FXML
    private TableColumn<Equipo, Integer> colRango;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, Image> colImagen;
    @FXML
    private TableColumn<Equipo, Integer> colPuntos;
    @FXML
    private TableColumn<Equipo, String> colDeporte;

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository equipoRepo = new EquipoRepository();
    private final DeporteRepository deporteRepo = new DeporteRepository();
    private final ObservableList<EstadisticasEquipoGenerales> estadisticasLista = FXCollections.observableArrayList();
    private final EstadisticasEquipoGeneralesRepository estadisticasRepo = new EstadisticasEquipoGeneralesRepository();

    @FXML
    private MFXButton btnActualizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarEstadisticas();

        colRango.setCellValueFactory(cellData -> {
            int rango = equiposLista.indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(rango).asObject();
        });
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
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });
        colPuntos.setCellValueFactory(cellData -> {
            Equipo equipo = cellData.getValue();
            Integer puntos = estadisticasLista.stream()
                    .filter(e -> e.getIdEquipo().equals(equipo.getId()))
                    .map(EstadisticasEquipoGenerales::getPuntos)
                    .findFirst()
                    .orElse(0);
            return new javafx.beans.property.SimpleIntegerProperty(puntos).asObject();
        });

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

    //Este método aplica los filtros en la tableView
    private void Filtrar() {
        FilteredList<Equipo> filteredData = new FilteredList<>(equiposLista, b -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(filteredData);
        });

        ComboBoxDeportes.valueProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(filteredData);
        });

        try {
            Deporte todos = new Deporte("Todos");
            todos.setNombre("Todos");
            List<Deporte> deportes = deporteRepo.findAll();

            ComboBoxDeportes.getItems().clear();
            ComboBoxDeportes.getItems().add(todos);
            ComboBoxDeportes.getItems().addAll(deportes);
            ComboBoxDeportes.getSelectionModel().select(todos);
            AppContext.getInstance().set("LISTA_DEPORTES", deportes);
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

        SortedList<Equipo> sortedData = new SortedList<>(filteredData, (e1, e2) -> {
            int puntos1 = estadisticasLista.stream()
                    .filter(e -> e.getIdEquipo().equals(e1.getId()))
                    .map(EstadisticasEquipoGenerales::getPuntos)
                    .findFirst()
                    .orElse(0);

            int puntos2 = estadisticasLista.stream()
                    .filter(e -> e.getIdEquipo().equals(e2.getId()))
                    .map(EstadisticasEquipoGenerales::getPuntos)
                    .findFirst()
                    .orElse(0);

            return Integer.compare(puntos2, puntos1);
        });

        tableView.setItems(sortedData);
        colRango.setCellValueFactory(cellData -> {
            int rango = tableView.getItems().indexOf(cellData.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(rango).asObject();
        });
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
         Sonidos.click();
        cargarEstadisticas();
        cargarJson();
        Filtrar();
    }

    //Este metodo carga las estadisticas generales para obtener los puntos luego
    private void cargarEstadisticas() {
        try {
            List<EstadisticasEquipoGenerales> listaGenerales = estadisticasRepo.findAll();
            estadisticasLista.setAll(listaGenerales);
        } catch (IOException ex) {
            Logger.getLogger(RankingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Se aplican los 2 filtros, el del combobox y el del text field para que funcionen simultaneamente
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
