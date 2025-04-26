package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
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

public class PantallaInicialController extends Controller implements Initializable {

    @FXML
    private MFXButton btnJugar;
    @FXML
    private MFXButton btnSalir;
    @FXML
    private AnchorPane root;
    @FXML
    private Label title;
    @FXML
    private MFXButton btnAcercaDe;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setFocusTraversable(true);
    }

    @FXML
    private void OnActionBtnJugar(ActionEvent event) throws IOException {
        Sonidos.click();
        FlowController.getInstance().goMain();
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void OnActionBtnSalir(ActionEvent event) {
        Sonidos.click();
        Mensaje mensaje = new Mensaje();
        Boolean respuesta = mensaje.showConfirmation("BALLIVERSE", "¿Estás seguro que deseas cerrar BALLIVERSE?");
        if (respuesta) {
            ((Stage) root.getScene().getWindow()).close();
        } else {
            return;
        }
    }

    @FXML
    private void OnActionBtnAcercaDe(ActionEvent event) throws IOException {
        Sonidos.click();
        FlowController.getInstance().goMain2();
        ((Stage) root.getScene().getWindow()).close();
    }

    @Override
    public void initialize() {
    }
}
