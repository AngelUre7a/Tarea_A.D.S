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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class RegistroListaDeporteBalonController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
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
    private TableColumn<Deporte, String> colEditar;
    @FXML
    private TableColumn<Deporte, String> colEliminar;

    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository Deporterepo = new DeporteRepository();

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();

    @FXML
    private MFXButton btnAgregar;
    @FXML
    private MFXButton btnActualizar;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));

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
//                    System.out.println("No se cargo la imagen");
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });
        try {
            equiposLista.addAll(Equiporepo.findAll());
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar equipos", "No se pudieron cargar los equipos.");
        }
        //configuracion boton de eliminar
        colEliminar.setCellFactory(column -> new javafx.scene.control.TableCell<Deporte, String>() {
            private final MFXButton btnEliminar = new MFXButton("Eliminar");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnEliminar.setOnAction(event -> {
                        Deporte deporteSeleccionado = getTableView().getItems().get(getIndex());
                        String nombreDeporte = deporteSeleccionado.getNombre();

                        
                        boolean tieneEquiposAsociados = equiposLista.stream()
                                .anyMatch(equipo -> equipo.getTipoDeporte().equals(nombreDeporte));
                        
                        if (tieneEquiposAsociados) {
                            new Mensaje().show(Alert.AlertType.ERROR, "Error al eliminar deporte", "No se puede eliminar el deporte porque tiene equipos asociados.");
                        } else {
                            // Eliminar de la lista observable (UI)
                            deportesLista.remove(deporteSeleccionado);

                            // Eliminar del repositorio (persistencia)
                            try {
                                Deporterepo.deleteById(deporteSeleccionado.getId());
                            } catch (IOException e) {
                                new Mensaje().show(Alert.AlertType.ERROR, "Error al eliminar deporte", "No se pudo eliminar el deporte.");
                                return;
                            }

                            // Mensaje de éxito
                            new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "El deporte se ha eliminado correctamente.");
                        }
                    });
                    setGraphic(btnEliminar);
                }
            }
        });
        // Configuracion del botón de editar para cada fila
        colEditar.setCellFactory(column -> new javafx.scene.control.TableCell<Deporte, String>() {
            private final MFXButton btnEditar = new MFXButton("Editar");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btnEditar.setOnAction(event -> {
                        // Obtener el deporte seleccionado
                        Deporte deporteSeleccionado = getTableView().getItems().get(getIndex());

                        // Guardar el deporte seleccionado en el AppContext para cargarlo en el formulario de registro
                        AppContext.getInstance().set("DEPORTE_EDITAR", deporteSeleccionado);

                        // Abre la ventana de registro para editar
                        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
                    });
                    setGraphic(btnEditar);
                }
            }
        });

//        try {
//            deportesLista.addAll(Deporterepo.findAll());
//        } catch (IOException e) {
//            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los deportes.");
//        }
        try {
            for (Deporte d : Deporterepo.findAll()) {
                d.cargarImagenDesdeBase64(); // ← Aquí reconstruimos la imagen
                deportesLista.add(d);
            }
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los deportes.");
        }

        FilteredList<Deporte> filteredData = new FilteredList<>(deportesLista, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Deporte -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Deporte.getNombre().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Deporte> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(deportesLista);
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnAgregar(ActionEvent event) throws IOException {
        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
        // Verificar si hay un nuevo deporte creado
        if (AppContext.getInstance().containsItem("DEPORTE_NUEVO")) {
            // Obtener el nuevo deporte desde el AppContext

            Deporte nuevo = (Deporte) AppContext.getInstance().get("DEPORTE_NUEVO");
            // Asegúrate de que la lista no se reinicie al agregar
            if (nuevo != null) {
                // Guardar el nuevo deporte en el repositorio
                Deporterepo.save(nuevo);

                // Agregar el nuevo deporte a la lista sin eliminar los anteriores
                deportesLista.add(nuevo);
                AppContext.getInstance().delete("DEPORTE_NUEVO"); // Limpiar el contexto
            }
        }
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        tableView.refresh();
    }

}
