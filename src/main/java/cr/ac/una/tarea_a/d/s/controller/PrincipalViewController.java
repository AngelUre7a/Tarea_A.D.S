/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;


import io.github.palexdev.materialfx.controls.MFXButton;
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
public class PrincipalViewController implements Initializable {

    @FXML
    private MFXButton btnJugar;
    @FXML
    private MFXButton btnSalir;
    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane apJugar;


    /** 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void OnActionBtnJugar(ActionEvent event) {
        apJugar.toFront();
    }

    @FXML
    private void OnActionBtnSalir(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
        
    }

  
}
