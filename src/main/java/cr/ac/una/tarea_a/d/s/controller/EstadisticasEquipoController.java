/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Torneo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class EstadisticasEquipoController extends Controller implements Initializable {

    @FXML
    private Label lblNombreEquipo;
    @FXML
    private Label lblTorneosGanados;
    @FXML
    private Label lblGolesTotales;
    @FXML
    private Label lblPuntosTotales;
    @FXML
    private ComboBox<Torneo> cmbTorneos;
    @FXML
    private Label lblGolesTorneo;
    @FXML
    private Label lblPuntosTorneo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {
    }
    
}
