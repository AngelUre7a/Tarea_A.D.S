package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegistroDeporteController extends Controller implements Initializable {

    @FXML
    private MFXButton btnRegistrar;
    @FXML
    private MFXTextField txtNombreDeporte;
    @FXML
    private MFXButton btnCargarImagen;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane root;

    private Deporte deporte;
    private boolean esEdicion = false;
    @FXML
    private MFXButton btnVolver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        limpiarFormulario();

        if (AppContext.getInstance().hasValue("DEPORTE_EDITAR")) {
            deporte = (Deporte) AppContext.getInstance().get("DEPORTE_EDITAR");
            txtNombreDeporte.setText(deporte.getNombre());
            imageView.setImage(deporte.getImagen());
            esEdicion = true;
        }

        DragAndDropForImageView();
    }

    private void DragAndDropForImageView() {
        imageView.setOnDragOver(event -> {
            if (event.getGestureSource() != imageView && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        imageView.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File archivoSeleccionado = db.getFiles().get(0);
                if (archivoSeleccionado != null) {
                    List<String> formatosPermitidos = Arrays.asList(".png", ".jpg", ".jpeg", ".bmp");

                    
                    String nombreArchivo = archivoSeleccionado.getName().toLowerCase();
                    boolean formatoValido = formatosPermitidos.stream()
                            .anyMatch(ext -> nombreArchivo.endsWith(ext));

                    if (formatoValido) {
                        try {
                            Image imagen = new Image(archivoSeleccionado.toURI().toString());
                            imageView.setImage(imagen);
                            success = true;
                        } catch (Exception e) {
                            System.err.println("Error al cargar la imagen: " + e.getMessage());
                        }
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Solo se permiten PNG, JPG, JPEG o BMP").show();
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    private void onActionBtnRegistrar(ActionEvent event) throws IOException {
        String nombre = txtNombreDeporte.getText();
        Image imagen = imageView.getImage();

        if (nombre.isEmpty() || imagen == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        DeporteRepository deporteRepo = new DeporteRepository();
        try {
            List<Deporte> deportesExistentes = deporteRepo.findAll();
            for (Deporte deporte1 : deportesExistentes) {
                if (deporte1.getNombre().equalsIgnoreCase(nombre.trim())) {
                    new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Ya hay un deporte con ese nombre.");
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CreacionTorneoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (esEdicion && deporte != null) {
            deporte.setNombre(nombre);
            deporte.setImagen(imagen);
            deporteRepo.save(deporte);
            limpiarFormulario();
        } else {
            String id = java.util.UUID.randomUUID().toString();
            deporte = new Deporte(id, nombre, imagen);
            deporteRepo.save(deporte);
            limpiarFormulario();
        }

        new Mensaje().show(Alert.AlertType.INFORMATION, "Registro exitoso", "Deporte registrado correctamente.");
        AppContext.getInstance().delete("DEPORTE_EDITAR");
        deporte = null;
        esEdicion = false;

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onActionBtnCargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.bmp"));

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            Image imagen = new Image(archivoSeleccionado.toURI().toString());
            imageView.setImage(imagen);
        }
    }

    @Override
    public void initialize() {
    }
    
    private void limpiarFormulario() {
        txtNombreDeporte.clear();
        imageView.setImage(null);
        deporte = null;
        esEdicion = false;
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
        Mensaje mensaje = new Mensaje();
        Boolean respuesta = mensaje.showConfirmation("BALLIVERSE", "¿Estás seguro que deseas salir de la ventana para crear deportes?");
        if (respuesta) {
            deporte = null;
            esEdicion = false;
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        } else {
            return;
        }

    }
}
