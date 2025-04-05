package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DragAndDropForImageView();
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnRegistrar(ActionEvent event) throws IOException {
        String nombre = txtNombreDeporte.getText();
        Image imagen = imageView.getImage();

        if (nombre == null || nombre.isBlank() || imagen == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar un nombre y una imagen.");
            return;
        }

        // Crear el nuevo deporte
        String id = java.util.UUID.randomUUID().toString(); // Genera un ID único
        Deporte deporte = new Deporte(id, nombre, imagen, "balon");

        // Guardarlo en el AppContext
        AppContext.getInstance().set("DEPORTE_NUEVO", deporte);

        new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "Se agregó el deporte correctamente");

        // Limpiar los campos antes de cerrar la ventana
        txtNombreDeporte.clear();
        imageView.setImage(null);
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnCargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            Image imagen = new Image(archivoSeleccionado.toURI().toString());

            imageView.setImage(imagen);
        }
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
                    try {
                        Image imagen = new Image(archivoSeleccionado.toURI().toString());
                        imageView.setImage(imagen);
                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();

        });

    }

}
