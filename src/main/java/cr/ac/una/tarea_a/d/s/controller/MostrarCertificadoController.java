package cr.ac.una.tarea_a.d.s.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoPT;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoPTRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    private ImageView imgFondo;
    @FXML
    private Label lblGoles;

    private Equipo equipoCampeon;
    private Torneo torneo;
    
    private EstadisticasEquipoPTRepository estadisticasRepo;
    private final ObservableList<EstadisticasEquipoPT> estadisticasLista = FXCollections.observableArrayList();
    private EstadisticasEquipoPT statsPTCampeon;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgFondo.fitHeightProperty().bind(root.heightProperty());
        imgFondo.fitWidthProperty().bind(root.widthProperty());
        equipoCampeon = (Equipo) AppContext.getInstance().get("EQUIPO_CAMPEON");
        torneo = (Torneo) AppContext.getInstance().get("TORNEO_ACTUAL");
        estadisticasRepo = new EstadisticasEquipoPTRepository();

        List<EstadisticasEquipoPT> listaStatsPT = null;

        try {
            listaStatsPT = estadisticasRepo.findAll();
            estadisticasLista.setAll(listaStatsPT);
        } catch (IOException ex) {
            Logger.getLogger(RankingController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (listaStatsPT != null) {
            for (EstadisticasEquipoPT stat : listaStatsPT) {
                if (stat.getIdEquipo().equals(equipoCampeon.getId()) && stat.getIdTorneo().equals(torneo.getId())) {
                    statsPTCampeon = stat;
                    break;
                }
            }
        }

        if (equipoCampeon != null && statsPTCampeon != null) {
            lblNombreEquipo.setText(equipoCampeon.getNombre());
            lblDeporteJugado.setText(equipoCampeon.getTipoDeporte());
            lblPuntos.setText(String.valueOf(statsPTCampeon.getPuntosPT()));
            lblGoles.setText(String.valueOf(statsPTCampeon.getGolesAFavorPT()));
            lblPartidosGanados.setText(String.valueOf(statsPTCampeon.getPartidosGanadosPT()));

            if (equipoCampeon.getImagen() != null) {
                imgEscudo.setImage(equipoCampeon.getImagen());
            } else if (equipoCampeon.getImagenBase64() != null) {
                equipoCampeon.cargarImagenDesdeBase64();
                imgEscudo.setImage(equipoCampeon.getImagen());
            }
        }
    }

  
    @FXML
    private void onActionBtnImprimirCert(ActionEvent event) {
         Sonidos.click();
        ImprimirCertificado();
    }
    
    //Este método toma captura del root pane, luego la convierte, crea el pdf y lo guarda (se ocultan los botones para tomar la captura)
    private void ImprimirCertificado(){
        Sonidos.click();
        btnVolver.setVisible(false);
        btnImprimirCert.setVisible(false);
        try {
            WritableImage snapshot = root.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] imageInBytes = baos.toByteArray();
            baos.close();

            File file = new File("Certificado_ganador_del_torneo_" + torneo.getNombre()+ ".pdf");
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(
                    com.itextpdf.io.image.ImageDataFactory.create(imageInBytes)
            );
            document.add(pdfImage);

            document.close();
            new Mensaje().show(Alert.AlertType.INFORMATION, "Certificado guardado", "¡El certificado se ha guardado correctamente!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnVolver.setVisible(true);
        btnImprimirCert.setVisible(true);
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
         Sonidos.click();
        Stage stageActual = (Stage) root.getScene().getWindow();
        stageActual.close();
    }

    
      @Override
    public void initialize() {
    }

}
