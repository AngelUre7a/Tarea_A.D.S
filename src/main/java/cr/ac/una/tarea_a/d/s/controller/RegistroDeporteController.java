/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.App;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistroDeporteController extends Controller implements Initializable {
    
    private Deporte deporte = new Deporte(); 
    
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DragAndDropForImageView();
    }


    @FXML
    private void onActionBtnRegistrar(ActionEvent event) throws IOException {
       
        deporte.setImagenDeporte(imageView.getImage());
        deporte.setNombreDeporte(txtNombreDeporte.getText());
        AppContext.getInstance().set("DEPORTE", deporte);
        Mensaje mensaje = new Mensaje();
        mensaje.show(Alert.AlertType.INFORMATION,"BALLIVERSE", "Se agregó el deporte correctamente");

        
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

    @Override
    public void initialize() {
    }

}
