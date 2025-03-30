/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.util.AppContext;

import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistroListaDeporteBalonController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private Label lbNombre;
    @FXML
    private ImageView imgImagen;

     private Deporte deporte = new Deporte();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
      if(AppContext.getInstance().containsItem("DEPORTE")){
      deporte = (Deporte) AppContext.getInstance().get("DEPORTE");
      lbNombre.setText(deporte.getNombreDeporte());
      imgImagen.setImage(deporte.getImagenDeporte());
      }
        // TODO
    }    
    @Override
    public void initialize() {
      if(AppContext.getInstance().containsItem("DEPORTE")){
      deporte = (Deporte) AppContext.getInstance().get("DEPORTE");
      lbNombre.setText(deporte.getNombreDeporte());
      imgImagen.setImage(deporte.getImagenDeporte());
      }
    }
    
    @FXML
    private void onActionBtnAgregar(ActionEvent event) throws IOException {
        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
    }

    
}
