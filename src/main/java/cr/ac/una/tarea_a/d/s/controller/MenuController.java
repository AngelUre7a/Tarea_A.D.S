package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private MFXButton btnRegistroDeporte;
    @FXML
    private MFXButton btnRegistroEquipo;
    @FXML
    private MFXButton btnVolver;
    @FXML
    private MFXButton btnRanking;
    @FXML
    private Label title;
    @FXML
    private Label title1;
    @FXML
    private MFXButton btnTorneos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setFocusTraversable(true);
    }

    @FXML
    private void onActionBtnRegistroDeporte(ActionEvent event) throws IOException {
        Sonidos.click();
        FlowController.getInstance().goView("RegistroListaDeporteBalon");
    }

    @FXML
    private void onActionBtnRegistroEquipo(ActionEvent event) throws IOException {
       Sonidos.click();
        FlowController.getInstance().goView("RegistroListaEquipo");
    }

    @FXML
    private void onActionBtnTorneos(ActionEvent event) {
        Sonidos.click();
        FlowController.getInstance().goView("ListaTorneo");
    }

    @FXML
    private void onActionBtnEstadisticas(ActionEvent event) throws IOException {
          Sonidos.click();
        FlowController.getInstance().goView("Estadisticas");
    }

    @FXML
    private void onActionBtnRanking(ActionEvent event) {
        Sonidos.click();
        FlowController.getInstance().goView("Ranking");
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
         Sonidos.click();
        FlowController.getInstance().goViewInWindow("PantallaInicial");
        ((Stage) root.getScene().getWindow()).close();
    }

    @Override
    public void initialize() {
    }
}
