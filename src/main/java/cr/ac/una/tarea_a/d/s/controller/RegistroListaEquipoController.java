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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private ComboBox<Deporte> cmbBox;

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
                btnEditar.setText("");
                ImageView icono = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/editar.png").toExternalForm()));
                icono.setFitWidth(40);
                icono.setFitHeight(40);
                btnEditar.setGraphic(icono);
                btnEditar.getStyleClass().add("boton-tabla-icono");
                btnEditar.setStyle("-fx-background-color: transparent;");
                btnEditar.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    if (equipo.getCantidadTorneos() > 0) {
                        new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede editar ya que actualmente está participando en un torneo.");
                    } else {
                        abrirFormularioEditar(equipo);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(btnEditar) {{
                    setAlignment(Pos.CENTER);
                }});
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
                btnEliminar.getStyleClass().add("boton-tabla-icono");
                btnEliminar.setStyle("-fx-background-color: transparent;");
                btnEliminar.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    if (new Mensaje().showConfirmation("Confirmación", "¿Está seguro de eliminar el equipo?")) {
                        try {
                            Equiporepo.deleteById(equipo.getId());
                            equiposLista.remove(equipo);
                            tableView.refresh();
                        } catch (IOException | IllegalArgumentException ex) {
                            new Mensaje().show(Alert.AlertType.WARNING, "Error al eliminar", ex.getMessage());
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(btnEliminar) {{
                    setAlignment(Pos.CENTER);
                }});
            }
        });

        cargarFormulario();
        cargarComboBox();
        aplicarFiltrosAvanzados();
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

    private void cargarComboBox() {
        try {
            List<Deporte> deportes = Deporterepo.findAll();
            if (deportes != null) {
                Deporte todos = new Deporte("Todos");
                todos.setNombre("Todos");
                cmbBox.getItems().clear();
                cmbBox.getItems().add(todos);
                cmbBox.getItems().addAll(deportes);
                cmbBox.getSelectionModel().selectFirst();
            }
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }

        cmbBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });

        cmbBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });
    }

    private void aplicarFiltrosAvanzados() {
        FilteredList<Equipo> filteredData = new FilteredList<>(equiposLista, p -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltroCombo(filteredData));
        cmbBox.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltroCombo(filteredData));

        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void aplicarFiltroCombo(FilteredList<Equipo> filteredData) {
        filteredData.setPredicate(equipo -> {
            String filtroNombre = filterField.getText();
            Deporte filtroDeporte = cmbBox.getSelectionModel().getSelectedItem();

            boolean coincideNombre = (filtroNombre == null || filtroNombre.isEmpty()) ||
                                     equipo.getNombre().toLowerCase().contains(filtroNombre.toLowerCase());

            boolean coincideDeporte = (filtroDeporte == null || "Todos".equalsIgnoreCase(filtroDeporte.getNombre())) ||
                                      equipo.getTipoDeporte().equalsIgnoreCase(filtroDeporte.getNombre());

            return coincideNombre && coincideDeporte;
        });
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) {
        abrirFormularioNuevo();
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

    @Override
    public void initialize() {
    }
}