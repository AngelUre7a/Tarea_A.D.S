
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import cr.ac.una.unaplanilla.util.Formato;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class CreacionTorneoController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXTextField txtNombreTorneo;
    @FXML
    private TableView<Equipo> tableView;
    @FXML
    private TableColumn<Equipo, String> colAgregar;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, Image> colEscudo;
    @FXML
    private MFXTextField txtCantidadEquipos;
    @FXML
    private MFXTextField txtTiempoPartido;
    @FXML
    private ComboBox<Deporte> ComboBoxDeportes;
    @FXML
    private MFXButton btnJugarTorneo;
    @FXML
    private MFXButton btnActTabla;

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //SOLO PERMITIR NUMEROS
        txtCantidadEquipos.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("\\d")) {
                keyEvent.consume();
            }
        });
        txtTiempoPartido.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("\\d")) {
                keyEvent.consume();
            }
        });//MANEJO TABLEVIEW
        colAgregar.setCellFactory(column -> new javafx.scene.control.TableCell<Equipo, String>() {
            private final MFXCheckbox btnAgregar = new MFXCheckbox("");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnAgregar.setOnAction(event -> {

                    });
                    setGraphic(btnAgregar);
                }
            }
        });
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEscudo.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
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
                    System.out.println("No se cargo la imagen");
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });
        try {
            equiposLista.addAll(Equiporepo.findAll());
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los equipos.");
        }

        //MANEJO DE DEPORTES PARA EL COMBOBOX
        List<Deporte> deportes = null;
        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            deportes = deporteRepo.findAll(); // carga desde el JSON
            AppContext.getInstance().set("LISTA_DEPORTES", deportes); // guardamos en el contexto
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace(); // opcional
        }
        if (deportes != null) {
            ComboBoxDeportes.getItems().addAll(deportes);
        }

        // Configura cómo mostrar los nombres
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

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionComboBoxDeportes(ActionEvent event) {
        Deporte deporteSeleccionado = ComboBoxDeportes.getValue();
if (deporteSeleccionado == null) {
    // Mostrás una alerta o simplemente salís del método
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Advertencia");
    alert.setHeaderText("Debe seleccionar un deporte.");
    alert.setContentText("Por favor, seleccione un deporte antes de guardar el torneo.");
    alert.showAndWait();
    return;
}
        ObservableList<Equipo> equiposFiltrados = equiposLista.filtered(equipo -> equipo.getTipoDeporte().equals(deporteSeleccionado.getNombre()));
        tableView.setItems(equiposFiltrados);
    }

    @FXML
    private void onActionBtnJugarTorneo(ActionEvent event) {
    }

    @FXML
    private void onActionBtnActTabla(ActionEvent event) {
    }

    @FXML
    private void onActionBtnGuardarTorneo(ActionEvent event) {
        String deporte = ComboBoxDeportes.getValue() != null ? ComboBoxDeportes.getValue().getNombre() : null;
        String textCantidadEquipos = txtCantidadEquipos.getText();
        String textTiempoPorPartida = txtTiempoPartido.getText();
        if (deporte == null || textCantidadEquipos == null || textCantidadEquipos.isBlank() || textTiempoPorPartida == null || textTiempoPorPartida.isBlank()) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar un tipo de deporte, cantidad de equipos y tiempo de cada partido.");
            return;

        }
        int cantidadEquipos = Integer.parseInt(txtCantidadEquipos.getText());
        int tiempoPorPartida = Integer.parseInt(txtTiempoPartido.getText());
        String id = java.util.UUID.randomUUID().toString();

        Torneo torneo = new Torneo(id,deporte, cantidadEquipos, tiempoPorPartida);

        AppContext.getInstance().set("TORNEO_NUEVO", torneo);
        // Añadir el deporte a la lista global de deportes
        if (!AppContext.getInstance().containsItem("LISTA_TORNEOS")) {
            ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList();
            AppContext.getInstance().set("LISTA_TORNEOS", listaTorneos);
        }

        List<Torneo> torneos = (List<Torneo>) AppContext.getInstance().get("LISTA_TORNEOS");
        ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList(torneos);
        listaTorneos.add(torneo);

        new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "Torneo guardado correctamente");

        txtCantidadEquipos.clear();
        txtTiempoPartido.clear();
        ComboBoxDeportes.setValue(null);
        ((Stage) root.getScene().getWindow()).close();

    }

}
