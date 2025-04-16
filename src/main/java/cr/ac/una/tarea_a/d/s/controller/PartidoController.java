package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PartidoController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label lblEquipo1;
    @FXML
    private Label lblMarcador1;
    @FXML
    private Label lblMarcador2;
    @FXML
    private Label lblEquipo2;
    @FXML
    private MFXButton btnFinalizar;
    @FXML
    private ImageView imgEquipo1;
    @FXML
    private ImageView imgEquipo2;
    @FXML
    private ImageView imgBalon;
    @FXML
    private ImageView imgCancha;
    @FXML
    private VBox fondoImgCancha;
    @FXML
    private Label lblTiempo;
    @FXML
    private ImageView imgEscudo1;
    @FXML
    private ImageView imgEscudo2;

    
    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository deporteRepo = new DeporteRepository();
    String nombreDeporte = (String) AppContext.getInstance().get("DEPORTE");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgCancha.fitHeightProperty().bind(fondoImgCancha.heightProperty());
        imgCancha.fitWidthProperty().bind(fondoImgCancha.widthProperty());
        cargarJson();
        cargarDatosPartido();
    }

    @Override
    public void initialize() {
    }
    /*//////////////////////////////////////////////////////////////////*/
    private void mostrarAnimacionDesdeRecursos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea_a/d/s/view/AnimacionFinal.fxml"));
            Parent root = loader.load();
            AnimacionFinalController controller = loader.getController();

            // Cargar imágenes desde /resources/images/
            Image escudo = new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/escudo.png").toExternalForm());
            Image balon = new Image(getClass().getResource("/cr/ac/una/tarea_a/d/s/resources/balon-de-futbol.png").toExternalForm());

            // Convertir a Base64 para usar el mismo método
            String escudoBase64 = convertirImageABase64(escudo);
            String balonBase64 = convertirImageABase64(balon);

            controller.mostrarAnimacion(escudoBase64, balonBase64, "¡Real Java FC!");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("🏆 Campeón");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertirImageABase64(Image image) throws Exception {
        File file = new File(new java.net.URI(image.getUrl()));
        byte[] bytes = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(bytes);
    }
    @FXML
    private void onActionBtnFinalizar(ActionEvent event) {
        
    mostrarAnimacionDesdeRecursos(); // 🏆 Probá la animación
    
    }
    
    
   
//    
//    @FXML
//    private void onActionBtnFinalizar(ActionEvent event) {
//        Stage stage = (Stage) root.getScene().getWindow();
//        stage.close();
//    }
    
    private void cargarJson() {
    try {
        deportesLista.clear();  
        for (Deporte d : deporteRepo.findAll()) {
            d.cargarImagenDesdeBase64();  
            deportesLista.add(d);  
        }
       
    } catch (IOException e) {
        new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
        e.printStackTrace();
    }
}

    public Deporte buscarDeportePorNombre(String nombreDeporte) {
        for (Deporte d : deportesLista) {
            if (d.getNombre().equalsIgnoreCase(nombreDeporte)) {
            return d;
            }
        }
        return null;
    }
    
    public void cargarDatosPartido() {
        Equipo equipo1 = (Equipo) AppContext.getInstance().get("EQUIPO1");
        Equipo equipo2 = (Equipo) AppContext.getInstance().get("EQUIPO2");
        String nombreDeporte = (String) AppContext.getInstance().get("DEPORTE");
        Deporte deporte = buscarDeportePorNombre(nombreDeporte);
        if (deporte != null) {
            deporte.cargarImagenDesdeBase64();
            if (deporte.getImagen() != null) {
                imgBalon.setImage(deporte.getImagen());
            } else {
                System.out.println("Imagen del deporte es null después de cargarla.");
            }
        } else {
            System.out.println("No se encontró el deporte con nombre: " + nombreDeporte);
        }

        equipo1.cargarImagenDesdeBase64();
        equipo2.cargarImagenDesdeBase64();
        if (equipo1 != null && equipo2 != null) {
            lblEquipo1.setText(equipo1.getNombre());
            lblEquipo2.setText(equipo2.getNombre());

            if (equipo1.getImagen() != null) {
                imgEscudo1.setImage(equipo1.getImagen());
                imgEquipo1.setImage(equipo1.getImagen());
            }
            if (equipo2.getImagen() != null) {
                imgEscudo2.setImage(equipo2.getImagen());
                imgEquipo2.setImage(equipo2.getImagen());
            }
        }
    }
}
