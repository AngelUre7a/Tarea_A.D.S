//package cr.ac.una.tarea_a.d.s.controller;
//
//import cr.ac.una.Tarea_A.D.S.util.Sonidos;
//import cr.ac.una.tarea_a.d.s.util.Animaciones;
//import cr.ac.una.tarea_a.d.s.util.FlowController;
//import io.github.palexdev.materialfx.controls.MFXButton;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//
//public class AnimacionFinalController extends Controller {
//
//    @FXML
//    private Label lblConfeti;
//    @FXML
//    private ImageView imgEquipo;
//    @FXML
//    private ImageView imgBalon;
//    @FXML
//    private Label lblCampeon;
//    @FXML
//    private AnchorPane root;
//    @FXML
//    private MFXButton btnSalir;
//    @FXML
//    private MFXButton btnCertificado;
//
//    public void mostrarAnimacion(String base64Escudo, String base64Balon, String nombreEquipo) {
//        Image imagenEscudo = Animaciones.convertirBase64AImage(base64Escudo);
//        Image imagenBalon = Animaciones.convertirBase64AImage(base64Balon);
//
//        if (imagenEscudo != null) {
//            imgEquipo.setImage(imagenEscudo);
//        }
//        if (imagenBalon != null) {
//            imgBalon.setImage(imagenBalon);
//        }
//
//        lblCampeon.setText("¡" + nombreEquipo + " CAMPEÓN!");
//        Sonidos.detener();
//        Sonidos.ganador();
//
//        Animaciones.animarEquipo(imgEquipo);
//        Animaciones.animarBalon(imgBalon);
//        Animaciones.animarTexto(lblCampeon);
//        Animaciones.animarConfeti(lblConfeti);
//    }
//
//    @FXML
//    private void onActionBtnSalir(ActionEvent event) {
//        FlowController.getInstance().goView("ListaTorneo");
//        Stage stage = (Stage) root.getScene().getWindow();
//        stage.close();
//    }
//
//    @FXML
//    private void OnActionBtnCertificado(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea_a/d/s/view/MostrarCertificado.fxml"));
//            Parent rootCert = loader.load();
//            MostrarCertificadoController controller = loader.getController();
//
//            Stage stage = new Stage();
//            stage.setTitle("Certificado del equipo");
//            stage.setScene(new Scene(rootCert));
//            stage.setResizable(false);
//            stage.show();
//
//            Stage actual = (Stage) root.getScene().getWindow();
//            actual.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void initialize() {
//    }
//
//}
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.util.Animaciones;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimacionFinalController extends Controller {

    @FXML
    private ImageView imgEquipo;
    @FXML
    private ImageView imgBalon;
    @FXML
    private Label lblCampeon;
    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnSalir;
    @FXML
    private MFXButton btnCertificado;
    @FXML
    private Pane paneConfeti; // NUEVO: contenedor para los confetis

    public void mostrarAnimacion(String base64Escudo, String base64Balon, String nombreEquipo) {
        Image imagenEscudo = Animaciones.convertirBase64AImage(base64Escudo);
        Image imagenBalon = Animaciones.convertirBase64AImage(base64Balon);

        if (imagenEscudo != null) {
            imgEquipo.setImage(imagenEscudo);
        }
        if (imagenBalon != null) {
            imgBalon.setImage(imagenBalon);
        }

        lblCampeon.setText("¡" + nombreEquipo + " CAMPEÓN!");
        Sonidos.detener();
        Sonidos.ganador();

        Animaciones.animarEquipo(imgEquipo);
        Animaciones.animarBalon(imgBalon);
        Animaciones.animarTexto(lblCampeon);

        lanzarConfetiVisual(); // NUEVO: animar confeti
    }

   private void lanzarConfetiVisual() {
    Platform.runLater(() -> {
        for (int i = 0; i < 40; i++) {
            ImageView confeti = new ImageView(new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/estrellap.png").toExternalForm()));
            confeti.setFitWidth(50);
            confeti.setFitHeight(50);
            confeti.setLayoutX(Math.random() * paneConfeti.getWidth());
            confeti.setLayoutY(-30);
            paneConfeti.getChildren().add(confeti);

            TranslateTransition anim = new TranslateTransition(Duration.seconds(3 + Math.random()), confeti);
            anim.setToY(paneConfeti.getHeight() + 50);
            anim.setOnFinished(e -> paneConfeti.getChildren().remove(confeti));
            anim.play();

            RotateTransition rotar = new RotateTransition(Duration.seconds(2 + Math.random()), confeti);
            rotar.setByAngle(360);
            rotar.setCycleCount(Animation.INDEFINITE);
            rotar.play();
        }
    });
}


    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        FlowController.getInstance().goView("ListaTorneo");
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void OnActionBtnCertificado(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea_a/d/s/view/MostrarCertificado.fxml"));
            Parent rootCert = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Certificado del equipo");
            stage.setScene(new Scene(rootCert));
            stage.setResizable(false);
            stage.show();

            Stage actual = (Stage) root.getScene().getWindow();
            actual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
    }
}
