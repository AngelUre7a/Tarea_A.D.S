/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PantallaInicialController extends Controller implements Initializable {

    @FXML
    private MFXButton btnJugar;
    @FXML
    private MFXButton btnSalir;
    @FXML
    private AnchorPane root;
    @FXML
    private Label title;


    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setFocusTraversable(true);
    }    

    @FXML
    private void OnActionBtnJugar(ActionEvent event) throws IOException {
        FlowController.getInstance().goMain();
       ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void OnActionBtnSalir(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
    }

    @Override
    public void initialize() {
    }

  
}
