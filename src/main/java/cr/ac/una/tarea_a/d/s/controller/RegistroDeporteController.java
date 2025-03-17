/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistroDeporteController implements Initializable {

    @FXML
    private MFXButton btnVolver;
    @FXML
    private MFXButton btnRegistrar;
    @FXML
    private MFXTextField txtNombreDeporte;
    @FXML
    private MFXButton btnCargarImagen;
    @FXML
    private ImageView imageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DragAndDropForImageView();
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) throws IOException {
        App.setRoot("RegistroListaDeporteBalon");
    }

    @FXML
    private void onActionBtnRegistrar(ActionEvent event) throws IOException {
        App.setRoot("RegistroListaDeporteBalon");
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
