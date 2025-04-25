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
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();
    private final ObservableList<Equipo> equiposInscritos = FXCollections.observableArrayList();
    private final TorneoRepository Torneorepo = new TorneoRepository();
    @FXML
    private MFXButton btnVolver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtCantidadEquipos.textProperty().addListener((obs, oldText, newText) -> {
            equiposInscritos.clear();
            tableView.refresh();

        });
        txtCantidadEquipos.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("\\d")) {
                keyEvent.consume();
            }
        });
        txtTiempoPartido.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("\\d")) {
                keyEvent.consume();
            }
        });
        colAgregar.setCellFactory(column -> new javafx.scene.control.TableCell<Equipo, String>() {
            private final MFXCheckbox checkbox = new MFXCheckbox("");
            {
                checkbox.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    if (checkbox.isSelected()) {
                        if (!equiposInscritos.contains(equipo)) {
                            equiposInscritos.add(equipo);
                        }
                    } else {
                        equiposInscritos.remove(equipo);
                    }
                    if (!txtCantidadEquipos.getText().isBlank()) {
                        int max = Integer.parseInt(txtCantidadEquipos.getText());
                        tableView.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    checkbox.setSelected(equiposInscritos.contains(equipo));

                    if (!txtCantidadEquipos.getText().isBlank()) {
                        int max = Integer.parseInt(txtCantidadEquipos.getText());
                        checkbox.setDisable(!checkbox.isSelected() && equiposInscritos.size() >= max);
                    }

                    setGraphic(checkbox);
                }
            }
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEscudo.setCellFactory(column -> new javafx.scene.control.TableCell<Equipo, Image>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Equipo equipo = getTableView().getItems().get(getIndex());
                String base64 = equipo.getImagenBase64();

                if (base64 != null && !base64.isBlank()) {
                    try {
                        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64);
                        Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error decodificando imagen Base64: " + e.getMessage());
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            }
        });
        try {
            equiposLista.addAll(Equiporepo.findAll());
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los equipos.");
        }
        
        List<Deporte> deportes = null;
        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            deportes = deporteRepo.findAll();
            AppContext.getInstance().set("LISTA_DEPORTES", deportes);
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }
        if (deportes != null) {
            ComboBoxDeportes.getItems().addAll(deportes);
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
        txtCantidadEquipos.textProperty().addListener((obs, oldVal, newVal) -> tableView.refresh());

    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionComboBoxDeportes(ActionEvent event) {
        Deporte deporteSeleccionado = ComboBoxDeportes.getValue();

        ObservableList<Equipo> equiposFiltrados = equiposLista.filtered(equipo -> equipo.getTipoDeporte().equals(deporteSeleccionado.getNombre()));
        tableView.setItems(equiposFiltrados);
    }

    @FXML
    private void onActionBtnJugarTorneo(ActionEvent event) {
        Deporte deporte = ComboBoxDeportes.getValue();
        if (deporte == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe seleccionar un tipo de deporte.");
            return;
        }
        String nombre = txtNombreTorneo.getText();
        String textCantidadEquipos = txtCantidadEquipos.getText();
        String textTiempoPorPartida = txtTiempoPartido.getText();
        if (nombre == null || nombre.isBlank() || textCantidadEquipos == null || textCantidadEquipos.isBlank() || textTiempoPorPartida == null || textTiempoPorPartida.isBlank()) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar el nombre, cantidad de equipos y tiempo de cada partido.");
            return;
        }

        try {
            List<Torneo> torneosExistentes = Torneorepo.findAll();
            for (Torneo torneo : torneosExistentes) {
                if (torneo.getNombre().equalsIgnoreCase(nombre.trim())) {
                    new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Ya hay un torneo con ese nombre.");
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CreacionTorneoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        int cantidadEquipos = Integer.parseInt(txtCantidadEquipos.getText());
        int tiempoPorPartida = Integer.parseInt(txtTiempoPartido.getText());
        String id = java.util.UUID.randomUUID().toString();

        if (equiposInscritos.size() == 1) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear un torneo con un solo Equipo, agrega mas equipos para crearlo.");
            return;
        }
        if (equiposInscritos.size() <= 0) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear un torneo sin equipos, agrega equipos para crearlo.");
            return;
        }
        if (tiempoPorPartida <= 0) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear el torneo, el tiempo debe de ser mayor a 0.");
            return;
        }

        Torneo torneo = new Torneo(id, nombre, deporte.getNombre(), cantidadEquipos, tiempoPorPartida, new ArrayList<>(equiposInscritos));
        equiposAddTorneo(equiposInscritos);
        AppContext.getInstance().set("TORNEO_NUEVO", torneo);
        if (!AppContext.getInstance().containsItem("LISTA_TORNEOS")) {
            ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList();
            AppContext.getInstance().set("LISTA_TORNEOS", listaTorneos);
        }

        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            deporte.addTorneoInscrito();

            System.out.println(deporte.getCantidadTorneosInscritos());
            deporteRepo.update(deporte);

        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al actualizar deporte", "No se pudo actualizar el deporte: " + deporte.getNombre());
            e.printStackTrace();
        }
        
        List<Torneo> torneos = (List<Torneo>) AppContext.getInstance().get("LISTA_TORNEOS");
        ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList(torneos);
        listaTorneos.add(torneo);

        torneo.setEstado("enCurso");

        txtCantidadEquipos.clear();
        txtTiempoPartido.clear();

        AppContext.getInstance().set("TORNEO", torneo);
        FlowController.getInstance().goView("Llaves");
        LlavesController controller = (LlavesController) FlowController.getInstance().getController("Llaves");

        if (controller != null) {
            controller.onShow();
        }
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnGuardarTorneo(ActionEvent event) {
        Deporte deporte = ComboBoxDeportes.getValue();
        if (deporte == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe seleccionar un tipo de deporte.");
            return;
        }
        String nombre = txtNombreTorneo.getText();
        String textCantidadEquipos = txtCantidadEquipos.getText();
        String textTiempoPorPartida = txtTiempoPartido.getText();
        if (nombre == null || nombre.isBlank() || textCantidadEquipos == null || textCantidadEquipos.isBlank() || textTiempoPorPartida == null || textTiempoPorPartida.isBlank()) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar el nombre, cantidad de equipos y tiempo de cada partido.");
            return;
        }
        try {
            List<Torneo> torneosExistentes = Torneorepo.findAll();
            for (Torneo torneo : torneosExistentes) {
                if (torneo.getNombre().equalsIgnoreCase(nombre.trim())) {
                    new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Ya hay un torneo con ese nombre.");
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CreacionTorneoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        int cantidadEquipos = Integer.parseInt(txtCantidadEquipos.getText());
        int tiempoPorPartida = Integer.parseInt(txtTiempoPartido.getText());

        String id = java.util.UUID.randomUUID().toString();

        if (equiposInscritos.size() == 1) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear un torneo con un solo Equipo, agrega mas equipos para crearlo.");
            return;
        }
        if (equiposInscritos.size() <= 0) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear un torneo sin equipos, agrega equipos para crearlo.");
            return;
        }
        if (tiempoPorPartida <= 0) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear el torneo, el tiempo debe de ser mayor a 0.");
            return;
        }
        if (cantidadEquipos != equiposInscritos.size()) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "No se puede crear el torneo, debe agregar " + (cantidadEquipos - equiposInscritos.size()) + " equipos mas para poder crearlo.");
            return;
        }
        Torneo torneo = new Torneo(id, nombre, deporte.getNombre(), cantidadEquipos, tiempoPorPartida, new ArrayList<>(equiposInscritos));
        equiposAddTorneo(equiposInscritos);
        AppContext.getInstance().set("TORNEO_NUEVO", torneo);

        if (!AppContext.getInstance().containsItem("LISTA_TORNEOS")) {
            ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList();
            AppContext.getInstance().set("LISTA_TORNEOS", listaTorneos);
        }

        List<Torneo> torneos = (List<Torneo>) AppContext.getInstance().get("LISTA_TORNEOS");
        ObservableList<Torneo> listaTorneos = FXCollections.observableArrayList(torneos);
        listaTorneos.add(torneo);
        System.out.println("torneo guarda");
        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            deporte.addTorneoInscrito();

            System.out.println(deporte.getCantidadTorneosInscritos());
            deporteRepo.update(deporte);

        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al actualizar deporte", "No se pudo actualizar el deporte: " + deporte.getNombre());
            e.printStackTrace();
        }

        new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "Torneo guardado correctamente");
        torneo.setEstado("pendiente");

        txtCantidadEquipos.clear();
        txtTiempoPartido.clear();

        ((Stage) root.getScene().getWindow()).close();

    }

    private void equiposAddTorneo(List<Equipo> equipos) {
        for (Equipo equipo : equipos) {
            equipo.addTorneo();
            try {
                Equiporepo.update(equipo);
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al actualizar equipo", "No se pudo actualizar el estado del equipo: " + equipo.getNombre());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
        Mensaje mensaje = new Mensaje();
        Boolean respuesta = mensaje.showConfirmation("BALLIVERSE", "¿Estás seguro que deseas salir de la ventana para crear Torneos?");
        if (respuesta) {
            txtCantidadEquipos.clear();
            txtTiempoPartido.clear();

            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        } else {
            return;
        }

    }

}
