/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MenuController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnRegistroDeportes;
    @FXML
    private MFXButton BtnRegistroEquipos;
    @FXML
    private MFXButton btnEstadisticas;
    @FXML
    private MFXButton btnVolverMenuPrincipal;
    @FXML
    private MFXButton btnCrearTorneo;
    @FXML
    private MFXButton btnRanking;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionBtnRegistroDeportes(ActionEvent event) throws IOException {
          App.setRoot("RegistroListaDeporteBalon");
    }

    @FXML
    private void onActionBtnRegistroEquipos(ActionEvent event)throws IOException {
         App.setRoot("RegistroListaEquipo");
    }

    @FXML
    private void onActionBtnEstadisticas(ActionEvent event) throws IOException {
        App.setRoot("Estadisticas");
    }

    @FXML
    private void onActionBtnVolverMenuPrincipal(ActionEvent event) throws IOException {
        App.setRoot("PrincipalView");
    }

    @FXML
    private void onActionBtnCrearTorneo(ActionEvent event) {
    }

    @FXML
    private void onActionBtnRanking(ActionEvent event) {
    }
    
}
