package cr.ac.una.tarea_a.d.s.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.util.Animaciones;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MostrarCertificadoController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label lblNombreEquipo;
    @FXML
    private Label lblPuntos;
    @FXML
    private Label lblPartidosGanados;
    @FXML
    private Label lblDeporteJugado;
    @FXML
    private ImageView imgEscudo;
    @FXML
    private MFXButton btnImprimirCert;
    @FXML
    private VBox ContainerSinBotones;
    @FXML
    private MFXButton btnVolver;
    @FXML
    private MFXButton btnSalir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnImprimirCert(ActionEvent event) {
        try {
            // 1. Tomamos snapshot del panel completo
            WritableImage snapshot = ContainerSinBotones.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            // 2. Convertimos BufferedImage a byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] imageInBytes = baos.toByteArray();
            baos.close();

            // 3. Guardamos en PDF usando iText
            File file = new File("certificado_equipo.pdf"); // Cambiar ruta si se quiere
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(
                    com.itextpdf.io.image.ImageDataFactory.create(imageInBytes)
            );
            document.add(pdfImage);

            document.close();
            System.out.println("✅ Certificado exportado exitosamente a: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
        // Cerrás la ventana actual del certificado
        Stage stageActual = (Stage) root.getScene().getWindow();
        stageActual.close();

        // Y abrís la vista de la animación final como modal
        FlowController.getInstance().goViewInWindowModal("AnimacionFinal", stageActual, Boolean.FALSE);
    }

    @FXML
    private void OnActionBtnSalir(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void cargarDatos(Equipo equipo, Deporte deporte) {
        lblNombreEquipo.setText(equipo.getNombre());
        lblDeporteJugado.setText(deporte.getNombre());
//        lblPuntos.setText(String.valueOf(equipo.getPuntos())); // ejemplo

        Image escudo = Animaciones.convertirBase64AImage(equipo.getImagenBase64());
        if (escudo != null) {
            imgEscudo.setImage(escudo);
        }
    }

}
