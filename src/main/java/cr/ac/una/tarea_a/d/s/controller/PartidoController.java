/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
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
public class PartidoController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView iVEquipo1;
    @FXML
    private ImageView iVEquipo2;
    @FXML
    private ImageView iVBalon;
    @FXML
    private Label lbTiempo;
    @FXML
    private ImageView ivEscudo1;
    @FXML
    private Label lbEquipo1;
    @FXML
    private Label lbMarcador1;
    @FXML
    private Label lbMarcador2;
    @FXML
    private Label lbEquipo2;
    @FXML
    private ImageView ivEscudo2;
    @FXML
    private MFXButton btnFinalizar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("prueba");
        // TODO
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnFinalizar(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

}
