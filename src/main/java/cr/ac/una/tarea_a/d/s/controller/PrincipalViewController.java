/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;


import cr.ac.una.tarea_a.d.s.App;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PrincipalViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnJugar;
    @FXML
    private MFXButton btnSalir;
    @FXML
    private AnchorPane root;


    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void OnActionBtnJugar(ActionEvent event) throws IOException {
        App.setRoot("Menu");
    }

    @FXML
    private void OnActionBtnSalir(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
        
    }

    @Override
    public void initialize() {
    }

  
}
