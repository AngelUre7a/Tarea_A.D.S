package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

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
    @FXML
    private AnchorPane root;
    @FXML
    private Label lbRegistroE;
    @FXML
    private ComboBox<Deporte> ComboBoxDeportes;
    @FXML
    private MFXButton btnVolver;

    private VideoCapture capture;
    private boolean isCameraRunning = false;
    private Equipo equipo;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnTomarFoto.setDisable(true);
        btnTomarFoto.setVisible(false);
        btnTomarFoto.setManaged(false);;
        limpiarFormulario();
        OpenCV.loadLocally();
        List<Deporte> deportes = null;
        if (AppContext.getInstance().hasValue("EQUIPO_EDITAR")) {
            equipo = (Equipo) AppContext.getInstance().get("EQUIPO_EDITAR");
            txtNombreEquipo.setText(equipo.getNombre());
            ImageView.setImage(equipo.getImagen());
            esEdicion = true;
        }
        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            deportes = deporteRepo.findAll();
            AppContext.getInstance().set("LISTA_DEPORTES", deportes);
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }
        if (deportes != null) {
            ComboBoxDeportes.getItems().addAll(deportes);
        }
        ComboBoxDeportes.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });
        ComboBoxDeportes.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });
    }

    @FXML
    private void onActionBtnRegistrarEquipo(ActionEvent event) throws IOException {
         Sonidos.click();
        String rutaImagen = (String) AppContext.getInstance().get("IMAGEN_TOMADA");
        String nombre = txtNombreEquipo.getText();
        Image imagen;
        String deporte = ComboBoxDeportes.getValue() != null ? ComboBoxDeportes.getValue().getNombre() : null;
        if (rutaImagen != null) {
            imagen = new Image(new File(rutaImagen).toURI().toString());
            AppContext.getInstance().delete("IMAGEN_TOMADA");
        } else {
            imagen = ImageView.getImage();
        }
        if (nombre == null || nombre.isBlank() || imagen == null || deporte == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar un nombre, una imagen y un tipo de deporte.");
            return;
        }
        EquipoRepository equipoRepo = new EquipoRepository();
        try {
            List<Equipo> equiposExistentes = equipoRepo.findAll();
            for (Equipo equipo1 : equiposExistentes) {
                if (equipo1.getNombre().equalsIgnoreCase(nombre.trim())) {
                    new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Ya hay un deporte con ese nombre.");
                    return;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CreacionTorneoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (esEdicion && equipo != null) {
            equipo = (Equipo) AppContext.getInstance().get("EQUIPO_EDITAR");
            equipo.setNombre(nombre);
            equipo.setImagen(imagen);
            equipo.setTipoDeporte(deporte);
            equipoRepo.save(equipo);
        } else {
            String id = java.util.UUID.randomUUID().toString();
            equipo = new Equipo(id, nombre, imagen, deporte);
            equipoRepo.save(equipo);
            limpiarFormulario();
        }

        new Mensaje().show(Alert.AlertType.INFORMATION, "Registro exitoso", "Equipo registrado correctamente.");
        AppContext.getInstance().delete("EQUIPO_EDITAR");
        equipo = null;
        esEdicion = false;
        cerrarCamara();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    // Abre la cámara y empieza a mostrar en vivo
    @FXML
    private void OnActionBtnAbrirCamera(ActionEvent event) {
         Sonidos.click();
        btnAbrirCamera.setDisable(true);
        btnAbrirCamera.setVisible(false);
        btnAbrirCamera.setManaged(false);
        btnTomarFoto.setDisable(false);
        btnTomarFoto.setVisible(true);
        btnTomarFoto.setManaged(true);
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
    
     //taqui oma una foto usando la cámara y guarda la imagen en un archivo local

    @FXML
    private void onActionBtnTomarFoto(ActionEvent event) {
         Sonidos.click();
        btnTomarFoto.setDisable(true);
        btnTomarFoto.setManaged(false);
        btnTomarFoto.setVisible(false);
        btnAbrirCamera.setDisable(false);
        btnAbrirCamera.setManaged(true);
        btnAbrirCamera.setVisible(true);
        if (capture != null && capture.isOpened()) {
            Mat Frame = new Mat();
            if (capture.read(Frame)) {
                Imgproc.cvtColor(Frame, Frame, Imgproc.COLOR_BGR2RGB);
                javafx.scene.image.Image image = matToImage(Frame);
                try {
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                    File carpeta = new File("imagenes");
                    if (!carpeta.exists()) {
                        carpeta.mkdirs();
                    }
                    File outputFile = new File(carpeta, "equipo_" + System.currentTimeMillis() + ".png");
                    ImageIO.write(bufferedImage, "png", outputFile);
                    Image imageFromFile = new Image(outputFile.toURI().toString());
                    ImageView.setImage(imageFromFile);
                    AppContext.getInstance().set("IMAGEN_TOMADA", outputFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    new Mensaje().show(Alert.AlertType.ERROR, "Error", "No se pudo guardar la imagen tomada.");
                }
            }
        }
        cerrarCamara();

    }

    @FXML
    private void onActionBtnCargarImagen(ActionEvent event) {
         Sonidos.click();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.bmp"));
        File archivoSeleccionado = fileChooser.showOpenDialog(null);
        if (archivoSeleccionado != null) {
            Image imagen = new Image(archivoSeleccionado.toURI().toString());
            ImageView.setImage(imagen);
        }
    }
//aquic se Convierte un Mat de OpenCV en una Image de JavaFX.
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

    private void cerrarCamara() {
         Sonidos.click();
        if (isCameraRunning && capture != null && capture.isOpened()) {
            isCameraRunning = false;
            capture.release();
            System.out.println("Cámara cerrada");
        }
    }

    private void limpiarFormulario() {
        txtNombreEquipo.clear();
        ImageView.setImage(null);
        ComboBoxDeportes.setValue(null);
        equipo = null;
        esEdicion = false;
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
         Sonidos.click();
        Mensaje mensaje = new Mensaje();
        Boolean respuesta = mensaje.showConfirmation("BALLIVERSE", "¿Estás seguro que deseas salir de la ventana para crear Equipos??");
        if (respuesta) {
            cerrarCamara();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        } else {
            return;
        }

    }

}
