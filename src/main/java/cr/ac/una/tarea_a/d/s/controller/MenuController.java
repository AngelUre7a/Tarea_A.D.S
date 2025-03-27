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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MenuController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnEstadisticas;
    @FXML
    private MFXButton btnCrearTorneo;
    @FXML
    private MFXButton btnRegistroDeporte;
    @FXML
    private MFXButton btnRegistroEquipo;
    @FXML
    private MFXButton btnVolver;
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
    private void onActionBtnRegistroDeporte(ActionEvent event) throws IOException {
        FlowController.getInstance().goView("RegistroListaDeporteBalon");

    }

    @FXML
    private void onActionBtnRegistroEquipo(ActionEvent event)throws IOException {
        FlowController.getInstance().goView("RegistroListaEquipo");
    }

    @FXML
    private void onActionBtnEstadisticas(ActionEvent event) throws IOException {
        FlowController.getInstance().goView("Estadisticas");
    }
    
    @FXML
    private void onActionBtnCrearTorneo(ActionEvent event) {
        FlowController.getInstance().goView("CreacionTorneo");
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("PantallaInicial");
        ((Stage) root.getScene().getWindow()).close();
    }
    
     @FXML
    private void onActionBtnRanking(ActionEvent event) {
        FlowController.getInstance().goView("Ranking");
    }

    @Override
    public void initialize() {
    }
}
