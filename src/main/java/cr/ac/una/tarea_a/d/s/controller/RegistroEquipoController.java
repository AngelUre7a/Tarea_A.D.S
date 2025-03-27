/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistroEquipoController extends Controller implements Initializable {

    @FXML
    private MFXButton btnRegistrar;
    @FXML
    private MFXTextField txtNombreEquipo;
    @FXML
    private MFXButton btnAbrirCamera;
    @FXML
    private MFXButton btnTomarFoto;
    @FXML
    private MFXButton btnCargarImagen;
    @FXML
    private ImageView ImageView;

    private VideoCapture capture;
    private boolean isCameraRunning = false;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OpenCV.loadShared();
    }

    @FXML
    private void onActionBtnRegistrarEquipo(ActionEvent event) throws IOException {
       ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void OnActionBtnAbrirCamera(ActionEvent event) {
        if (!isCameraRunning) {
            capture = new VideoCapture(0);
            if (!capture.isOpened()) {
                System.out.println("No se pudo abrir la camara");
                return;
            }
            isCameraRunning = true;
            Thread cameraThread = new Thread(() -> {
                Mat Frame = new Mat();
                while (isCameraRunning) {
                    if (capture.read(Frame)) {
                        Imgproc.cvtColor(Frame, Frame, Imgproc.COLOR_BGR2RGB);
                        javafx.scene.image.Image image = matToImage(Frame);

                        Platform.runLater(() -> {
                            ImageView.setImage(image);
                        });
                    } else {
                        System.out.println("Errror");
                    }
                }
            });
            cameraThread.setDaemon(true);
            cameraThread.start();

        }
    }

    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
        if (capture != null && capture.isOpened()) {
            Mat Frame = new Mat();
            if (capture.read(Frame)) {
                Imgproc.cvtColor(Frame, Frame, Imgproc.COLOR_BGR2RGB);
                javafx.scene.image.Image image = matToImage(Frame);
                ImageView.setImage(image);
            }
        }
    }

    @FXML
    private void onActionBtnCargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            Image imagen = new Image(archivoSeleccionado.toURI().toString());

            ImageView.setImage(imagen);
        }
    }

    private javafx.scene.image.Image matToImage(Mat Frame) {
        int width = Frame.width();
        int height = Frame.height();

        byte[] data = new byte[width * height * (int) Frame.elemSize()];
        Frame.get(0, 0, data);

        javafx.scene.image.WritableImage writableImage = new javafx.scene.image.WritableImage(width, height);
        javafx.scene.image.PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = (i * width + j) * 3;

                int r = data[index] & 0xFF;
                int g = data[index + 1] & 0xFF;
                int b = data[index + 2] & 0xFF;

                pixelWriter.setColor(j, i, javafx.scene.paint.Color.rgb(r, g, b));
            }
        }

        return writableImage;
    }

    @Override
    public void initialize() {
    }

}
