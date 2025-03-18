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
public class RegistroEquipoController implements Initializable {

    @FXML
    private MFXButton btnVolver;
    @FXML
    private MFXButton btnRegistrar;
    @FXML
    private MFXTextField txtNombreEquipo;
    @FXML
    private MFXButton btnAbrirCamera;
    @FXML
    private MFXButton btnTomarFoto;
    @FXML
    private MFXButton btnCargarImagen;
    @FXML
    private ImageView ImageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionBtnVolverEquipo(ActionEvent event) throws IOException  {
           App.setRoot("Menu");
    }

    @FXML
    private void onActionBtnRegistrarEquipo(ActionEvent event) throws IOException {
        App.setRoot("RegistroListaEquipo");
    }

    @FXML
    private void OnActionBtnAbrirCamera(ActionEvent event) {
    }

    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            Image imagen = new Image(archivoSeleccionado.toURI().toString());

            ImageView.setImage(imagen);
        }
    }
    
}
