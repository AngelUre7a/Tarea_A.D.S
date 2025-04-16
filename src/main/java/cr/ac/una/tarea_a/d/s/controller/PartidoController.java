package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipo;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import cr.ac.una.tarea_a.d.s.model.Partida;
import cr.ac.una.tarea_a.d.s.repositories.PartidaRepository;

public class PartidoController extends Controller implements Initializable {

    private final PartidaRepository partidaRepository = new PartidaRepository();

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
    private AnchorPane fondoImgCancha;
    @FXML
    private Label lblTiempo;
    @FXML
    private ImageView imgEscudo1;
    @FXML
    private ImageView imgEscudo2;

    private Timeline timeline;
    private int tiempoRestante;
    private double offsetX;
    private double offsetY;

    Equipo equipo1 = (Equipo) AppContext.getInstance().get("EQUIPO1");
    Equipo equipo2 = (Equipo) AppContext.getInstance().get("EQUIPO2");
    String nombreDeporte = (String) AppContext.getInstance().get("DEPORTE");
    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository deporteRepo = new DeporteRepository();
    EstadisticasEquipo estadisticasEquipo;

    int marcadorEquipo1;
    int marcadorEquipo2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgCancha.fitHeightProperty().bind(fondoImgCancha.heightProperty());
        imgCancha.fitWidthProperty().bind(fondoImgCancha.widthProperty());
        cargarJson();
        cargarDatosPartido();
        configurarMovimientoBalon();
        iniciarCuentaAtras();
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnFinalizar(ActionEvent event) {
        EstadisticasEquipo estadisticasEquipo1 = (EstadisticasEquipo) AppContext.getInstance().get("ESTADISTICAS_" + equipo1.getNombre());
        EstadisticasEquipo estadisticasEquipo2 = (EstadisticasEquipo) AppContext.getInstance().get("ESTADISTICAS_" + equipo2.getNombre());

        if (estadisticasEquipo1 == null) {
            estadisticasEquipo1 = new EstadisticasEquipo(equipo1.getId());
        }

        if (estadisticasEquipo2 == null) {
            estadisticasEquipo2 = new EstadisticasEquipo(equipo2.getId());
        }

        estadisticasEquipo1.setGolesAFavorPT(estadisticasEquipo1.getGolesAFavorPT() + marcadorEquipo1);
        estadisticasEquipo2.setGolesAFavorPT(estadisticasEquipo2.getGolesAFavorPT() + marcadorEquipo2);

        String ganadorId = null;
        if (marcadorEquipo1 > marcadorEquipo2) {
            ganadorId = equipo1.getId();
            estadisticasEquipo1.incrementarPartidosGanados();
            estadisticasEquipo1.incrementarPuntosGaneDirecto();
            agregarEstadisticasPTAGlobal(estadisticasEquipo2);
        } else if (marcadorEquipo2 > marcadorEquipo1) {
            ganadorId = equipo2.getId();
            estadisticasEquipo2.incrementarPartidosGanados();
            estadisticasEquipo2.incrementarPuntosGaneDirecto();
            agregarEstadisticasPTAGlobal(estadisticasEquipo1);
        } else {
            // Empate (si querés implementar algo más aquí)
        }

        // ✅ Crear y guardar partida
        Torneo torneo = (Torneo) AppContext.getInstance().get("TORNEO");
        if (torneo != null) {
            Partida partida = new Partida(
                    null, // id será generado por el repositorio
                    torneo.getId(),
                    equipo1.getId(),
                    equipo2.getId(),
                    marcadorEquipo1,
                    marcadorEquipo2,
                    "finalizado",
                    ganadorId,
                    tiempoRestante < 0 ? 0 : tiempoRestante
            );

            try {
                partidaRepository.save(partida);
                System.out.println("✅ Partida guardada exitosamente en JSON.");
            } catch (IOException e) {
                System.err.println("❌ Error al guardar la partida: " + e.getMessage());
            }
        }

        AppContext.getInstance().set("ESTADISTICAS_" + equipo1.getNombre(), estadisticasEquipo1);
        AppContext.getInstance().set("ESTADISTICAS_" + equipo2.getNombre(), estadisticasEquipo2);

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

//    @FXML
//    private void onActionBtnFinalizar(ActionEvent event) {
//
//        EstadisticasEquipo estadisticasEquipo1 = (EstadisticasEquipo) AppContext.getInstance().get("ESTADISTICAS_" + equipo1.getNombre());
//        EstadisticasEquipo estadisticasEquipo2 = (EstadisticasEquipo) AppContext.getInstance().get("ESTADISTICAS_" + equipo2.getNombre());
//
//        if (estadisticasEquipo1 == null) {
//            estadisticasEquipo1 = new EstadisticasEquipo(equipo1.getId());
//        }
//
//        if (estadisticasEquipo2 == null) {
//            estadisticasEquipo2 = new EstadisticasEquipo(equipo2.getId());
//        }
//
//        estadisticasEquipo1.setGolesAFavorPT(estadisticasEquipo1.getGolesAFavorPT() + marcadorEquipo1);
//        estadisticasEquipo2.setGolesAFavorPT(estadisticasEquipo2.getGolesAFavorPT() + marcadorEquipo2);
//        if (marcadorEquipo1 > marcadorEquipo2) {
//            estadisticasEquipo1.incrementarPartidosGanados();
//            estadisticasEquipo1.incrementarPuntosGaneDirecto();
//
//            agregarEstadisticasPTAGlobal(estadisticasEquipo2);
//        }
//        if (marcadorEquipo1 < marcadorEquipo2) {
//            estadisticasEquipo2.incrementarPartidosGanados();
//            estadisticasEquipo2.incrementarPuntosGaneDirecto();
//
//            agregarEstadisticasPTAGlobal(estadisticasEquipo1);
//        }
//        if (marcadorEquipo1 == marcadorEquipo2) {
//            //INNOVACION
//        }
//
//        System.out.println("Estadisticas del equipo 1, goles: " + estadisticasEquipo1.getGolesAFavorPT() + " puntos: " + estadisticasEquipo1.getPuntosPT() + " partidos ganados: " + estadisticasEquipo1.getPartidosGanadosPT());
//        System.out.println("Estadisticas del equipo 2, goles: " + estadisticasEquipo2.getGolesAFavorPT() + " puntos: " + estadisticasEquipo2.getPuntosPT() + " partidos ganados: " + estadisticasEquipo2.getPartidosGanadosPT());
//        System.out.println("Estadisticas globales del equipo 1, goles: " + estadisticasEquipo1.getGolesAFavor() + " puntos: " + estadisticasEquipo1.getPuntos() + " partidos ganados: " + estadisticasEquipo1.getPartidosGanados());
//        System.out.println("Estadisticas globales del equipo 2, goles: " + estadisticasEquipo2.getGolesAFavor() + " puntos: " + estadisticasEquipo2.getPuntos() + " partidos ganados: " + estadisticasEquipo2.getPartidosGanados());
//
//        AppContext.getInstance().set("ESTADISTICAS_" + equipo1.getNombre(), estadisticasEquipo1);
//        AppContext.getInstance().set("ESTADISTICAS_" + equipo2.getNombre(), estadisticasEquipo2);
//
//        Stage stage = (Stage) root.getScene().getWindow();
//        stage.close();
//
//    }
    private void agregarEstadisticasPTAGlobal(EstadisticasEquipo est) {
        est.setGolesAFavor(est.getGolesAFavor() + est.getGolesAFavorPT());
        est.setPuntos(est.getPuntos() + est.getPuntosPT());
        est.setPartidosGanados(est.getPartidosGanados() + est.getPartidosGanadosPT());
    }

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

    public void iniciarCuentaAtras() {
        Torneo torneo = (Torneo) AppContext.getInstance().get("TORNEO");
        if (torneo != null) {
            tiempoRestante = torneo.getTiempoPorPartida() * 60;  // Convertir a segundos
        } else {
            System.out.println("Torneo no encontrado");
            return;
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int minutos = tiempoRestante / 60;
            int segundos = tiempoRestante % 60;
            lblTiempo.setText(String.format("%02d:%02d", minutos, segundos));
            tiempoRestante--;

            if (tiempoRestante < 0) {
                timeline.stop();
                lblTiempo.setText("¡Tiempo finalizado!");
                // Aquí puedes hacer algo más, como bloquear botones o mostrar una alerta
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);  // Corre hasta que lo detengas
        timeline.play();  // Iniciar
    }

    private void configurarMovimientoBalon() {
        imgBalon.setOnMousePressed(event -> {
            offsetX = event.getSceneX() - imgBalon.getLayoutX();
            offsetY = event.getSceneY() - imgBalon.getLayoutY();
        });

        imgBalon.setOnMouseDragged(event -> {
            double nuevaX = event.getSceneX() - offsetX;
            double nuevaY = event.getSceneY() - offsetY;

            // Limitar movimiento dentro del área de juego
            if (nuevaX >= 0 && nuevaX <= fondoImgCancha.getWidth() - imgBalon.getFitWidth()) {
                imgBalon.setLayoutX(nuevaX);
            }
            if (nuevaY >= 0 && nuevaY <= fondoImgCancha.getHeight() - imgBalon.getFitHeight()) {
                imgBalon.setLayoutY(nuevaY);
            }
        });

        imgBalon.setOnMouseReleased(event -> {
            verificarGol();
        });
    }

    private void verificarGol() {
        double x = imgBalon.getLayoutX();
        double y = imgBalon.getLayoutY();

        if (x < 50 && y > 150 && y < 250) {
            marcadorEquipo2++;
            lblMarcador2.setText(String.valueOf(marcadorEquipo2));
            System.out.println("⚽ ¡GOL para el equipo 2!");
            resetearBalon();
        }

        if (x + imgBalon.getFitWidth() > fondoImgCancha.getWidth() - 50 && y > 150 && y < 250) {
            marcadorEquipo1++;
            lblMarcador1.setText(String.valueOf(marcadorEquipo1));
            System.out.println("⚽ ¡GOL para el equipo 1!");
            resetearBalon();
        }
    }

    private void resetearBalon() {
        // Reposiciona el balón al centro
        imgBalon.setLayoutX(fondoImgCancha.getWidth() / 2 - imgBalon.getFitWidth() / 2);
        imgBalon.setLayoutY(fondoImgCancha.getHeight() / 2 - imgBalon.getFitHeight() / 2);
    }

}
