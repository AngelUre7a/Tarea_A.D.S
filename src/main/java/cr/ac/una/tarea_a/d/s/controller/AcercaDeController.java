package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.util.Animaciones;
import cr.ac.una.tarea_a.d.s.util.Animaciones;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AcercaDeController extends Controller implements Initializable {

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblVersion;

    @FXML
    private Label lblAutores;

    @FXML
    private ImageView imgLogo;

    @FXML
    private MFXButton btnVolver;

    @FXML
    private AnchorPane root;

    @FXML
    private VBox vboxCreditos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
          Sonidos.acercaDe();

        lblTitulo.setText("BALLIVERSE");
        lblDescripcion.setText("Aplicación JavaFX para gestionar deportes, equipos, torneos, partidos y estadísticas.\n\n- Programación II - UNA Sede Brunca.");
        lblVersion.setText("Versión 1.0");
        lblAutores.setText("Desarrollado por: Daslyn Vargas, Saul Chinchilla, Angel Ureña");

        try {
            imgLogo.setImage(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/balliverse_logo.png").toExternalForm()));
        } catch (Exception e) {
            System.out.println("Logo no cargado: " + e.getMessage());
        }

        Animaciones.animarCreditos(vboxCreditos, 300, -400, 15);
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void OnActionBtnVolver(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("PantallaInicial");
        ((Stage) root.getScene().getWindow()).close();
    }
}
