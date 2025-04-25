package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoGenerales;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoPT;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import cr.ac.una.tarea_a.d.s.model.Partida;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoGeneralesRepository;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoPTRepository;
import cr.ac.una.tarea_a.d.s.repositories.PartidaRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.Animaciones;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import java.util.List;
import javafx.scene.layout.VBox;


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
    @FXML
    private Label lblGol;

    private Timeline timeline;
    private int tiempoRestante;
    private double offsetX;
    private double offsetY;

    Equipo equipo1 = (Equipo) AppContext.getInstance().get("EQUIPO1");
    Equipo equipo2 = (Equipo) AppContext.getInstance().get("EQUIPO2");
    String nombreDeporte = (String) AppContext.getInstance().get("DEPORTE");
    Torneo torneo = (Torneo) AppContext.getInstance().get("TORNEO");
    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository deporteRepo = new DeporteRepository();
    EstadisticasEquipoGeneralesRepository repoGen = new EstadisticasEquipoGeneralesRepository();

    int marcadorEquipo1;
    int marcadorEquipo2;
    String estadoPartida = "pendiente";
    private boolean enDesempate = false;
    @FXML
    private VBox Cancha;

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
        Finalizar();
    }

    private void Finalizar() {
        if ("finalizado".equals(estadoPartida)) {
            return;
        }
        EstadisticasEquipoPT estadisticasEquipo1 = (EstadisticasEquipoPT) AppContext.getInstance().get("ESTADISTICAS_" + equipo1.getNombre() + "_" + torneo.getId());
        EstadisticasEquipoPT estadisticasEquipo2 = (EstadisticasEquipoPT) AppContext.getInstance().get("ESTADISTICAS_" + equipo2.getNombre() + "_" + torneo.getId());
        EstadisticasEquipoGenerales estadisticasGenEquipo1;
        EstadisticasEquipoGenerales estadisticasGenEquipo2;

        try {
            estadisticasGenEquipo1 = repoGen.findById(equipo1.getId()).orElse(null);
            if (estadisticasGenEquipo1 == null) {
                estadisticasGenEquipo1 = new EstadisticasEquipoGenerales(equipo1.getId());
            }
            estadisticasGenEquipo2 = repoGen.findById(equipo2.getId()).orElse(null);
            if (estadisticasGenEquipo2 == null) {
                estadisticasGenEquipo2 = new EstadisticasEquipoGenerales(equipo2.getId());
            }
        } catch (IOException e) {
            System.err.println("Error cargando estadísticas generales: " + e.getMessage());
            estadisticasGenEquipo1 = new EstadisticasEquipoGenerales(equipo1.getId());
            estadisticasGenEquipo2 = new EstadisticasEquipoGenerales(equipo2.getId());
        }

        if (estadisticasEquipo1 == null) {
            estadisticasEquipo1 = new EstadisticasEquipoPT(equipo1.getId(), torneo.getId());
        }

        if (estadisticasEquipo2 == null) {
            estadisticasEquipo2 = new EstadisticasEquipoPT(equipo2.getId(), torneo.getId());
        }

        if (estadisticasGenEquipo1 == null) {
            estadisticasGenEquipo1 = new EstadisticasEquipoGenerales(equipo1.getId());
        }
        if (estadisticasGenEquipo2 == null) {
            estadisticasGenEquipo2 = new EstadisticasEquipoGenerales(equipo2.getId());
        }

        estadisticasEquipo1.setGolesAFavorPT(estadisticasEquipo1.getGolesAFavorPT() + marcadorEquipo1);
        estadisticasEquipo2.setGolesAFavorPT(estadisticasEquipo2.getGolesAFavorPT() + marcadorEquipo2);

        String ganadorId = null;
        if (marcadorEquipo1 > marcadorEquipo2) {
            ganadorId = equipo1.getId();
            estadisticasEquipo1.incrementarPartidosGanados();
            if (enDesempate == true) {
                estadisticasEquipo1.incrementarPuntosDesempate();
            } else {
                estadisticasEquipo1.incrementarPuntosGaneDirecto();
            }
            agregarEstadisticasPTAGeneral(estadisticasGenEquipo2, estadisticasEquipo2);
            
        } else if (marcadorEquipo2 > marcadorEquipo1) {
            ganadorId = equipo2.getId();
            estadisticasEquipo2.incrementarPartidosGanados();
            if (enDesempate == true) {
                estadisticasEquipo2.incrementarPuntosDesempate();
            } else {
                estadisticasEquipo2.incrementarPuntosGaneDirecto();
            }
            agregarEstadisticasPTAGeneral(estadisticasGenEquipo1, estadisticasEquipo1);
            
        } else {
            iniciarDesempate();
            return;
        }
        if (torneo != null) {
            estadoPartida = "finalizado";
            Partida partida = new Partida(
                    null,
                    torneo.getId(),
                    equipo1.getId(),
                    equipo2.getId(),
                    marcadorEquipo1,
                    marcadorEquipo2,
                    estadoPartida,
                    ganadorId,
                    tiempoRestante < 0 ? 0 : tiempoRestante
            );

            try {
                partidaRepository.save(partida);
                List<Partida> partidasDelTorneo = torneo.getPartidas();
                Partida existente = partidasDelTorneo.stream()
                        .filter(p -> (p.getIdEquipoA().equals(partida.getIdEquipoA()) && p.getIdEquipoB().equals(partida.getIdEquipoB()))
                        || (p.getIdEquipoA().equals(partida.getIdEquipoB()) && p.getIdEquipoB().equals(partida.getIdEquipoA())))
                        .findFirst().orElse(null);

                if (existente != null) {
                    partidasDelTorneo.remove(existente);
                }

                partidasDelTorneo.add(partida);

                try {
                    new TorneoRepository().save(torneo);
                    System.out.println("Torneo actualizado con nuevas partidas.");
                } catch (IOException e) {
                    System.err.println("No se pudo guardar el torneo actualizado: " + e.getMessage());
                }

                System.out.println("Partida guardada exitosamente en JSON.");
            } catch (IOException e) {
                System.err.println("Error al guardar la partida: " + e.getMessage());
            }
        }

        AppContext.getInstance().set("ESTADISTICAS_" + equipo1.getNombre() + "_" + torneo.getId(), estadisticasEquipo1);
        AppContext.getInstance().set("ESTADISTICAS_" + equipo2.getNombre() + "_" + torneo.getId(), estadisticasEquipo2);
        AppContext.getInstance().set("ESTADISTICAS_" + equipo1.getNombre(), estadisticasGenEquipo1);
        AppContext.getInstance().set("ESTADISTICAS_" + equipo2.getNombre(), estadisticasGenEquipo2);

        EstadisticasEquipoPTRepository estadisticasEquipo1Repo = new EstadisticasEquipoPTRepository();
        EstadisticasEquipoPTRepository estadisticasEquipo2Repo = new EstadisticasEquipoPTRepository();
        EstadisticasEquipoGeneralesRepository estadisticasGenEquipo1Repo = new EstadisticasEquipoGeneralesRepository();
        EstadisticasEquipoGeneralesRepository estadisticasGenEquipo2Repo = new EstadisticasEquipoGeneralesRepository();

        try {
            estadisticasEquipo1Repo.save(estadisticasEquipo1);
            estadisticasEquipo2Repo.save(estadisticasEquipo2);
            estadisticasGenEquipo1Repo.save(estadisticasGenEquipo1);
            estadisticasGenEquipo2Repo.save(estadisticasGenEquipo2);

        } catch (IOException e) {
            System.err.println("Error al guardar las estadísticas: " + e.getMessage());
        }

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void agregarEstadisticasPTAGeneral(EstadisticasEquipoGenerales generales, EstadisticasEquipoPT parciales) {
        generales.setGolesAFavor(generales.getGolesAFavor() + parciales.getGolesAFavorPT());
        generales.setPuntos(generales.getPuntos() + parciales.getPuntosPT());
        generales.setPartidosGanados(generales.getPartidosGanados() + parciales.getPartidosGanadosPT());
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
                AppContext.getInstance().set("DEPORTE_PARA_BALON", d);
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
            tiempoRestante = torneo.getTiempoPorPartida() * 60;
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
                Finalizar();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        Sonidos.silbato();

    }

    private void configurarMovimientoBalon() {
        imgBalon.setOnMousePressed(event -> {
            offsetX = event.getSceneX() - imgBalon.getLayoutX();
            offsetY = event.getSceneY() - imgBalon.getLayoutY();
        });

        imgBalon.setOnMouseDragged(event -> {
            double nuevaX = event.getSceneX() - offsetX;
            double nuevaY = event.getSceneY() - offsetY;

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
            marcadorEquipo1++;
            lblMarcador1.setText(String.valueOf(marcadorEquipo1));
            System.out.println("⚽ ¡GOL para el equipo 1!");
            Sonidos.aplausos();
            Animaciones.mostrarGolAnimado(lblGol);
            Animaciones.animarBalonGol(imgBalon); 
            resetearBalon();
        }

        if (x + imgBalon.getFitWidth() > fondoImgCancha.getWidth() - 50 && y > 150 && y < 250) {
            marcadorEquipo2++;
            lblMarcador2.setText(String.valueOf(marcadorEquipo2));
            System.out.println("⚽ ¡GOL para el equipo 2!");
            Sonidos.aplausos();
            Animaciones.mostrarGolAnimado(lblGol);
            Animaciones.animarBalonGol(imgBalon); 
            resetearBalon();
        }
    }

    private void resetearBalon() {
        imgBalon.setLayoutX(fondoImgCancha.getWidth() / 2 - imgBalon.getFitWidth() / 2);
        imgBalon.setLayoutY(fondoImgCancha.getHeight() / 2 - imgBalon.getFitHeight() / 2);
    }

    private void iniciarDesempate() {
        btnFinalizar.setDisable(true);
        btnFinalizar.setVisible(false);
        enDesempate = true;
        if (timeline != null) {
            timeline.stop();
        }
        FlowController.getInstance().goViewInWindowModal("CampoDeTiro", ((Stage) root.getScene().getWindow()), false);
        Equipo equipoGanador = (Equipo) AppContext.getInstance().get("GANADOR_DESEMPATE");
        if (equipoGanador != null) {
            if (equipoGanador.getId().equals(equipo1.getId())) {
                marcadorEquipo1++;
            } else if (equipoGanador.getId().equals(equipo2.getId())) {
                marcadorEquipo2++;
            }
        }
        new Mensaje().show(Alert.AlertType.INFORMATION, "Ganador Desempate", "¡" + equipoGanador.getNombre() + " ganó el desempate! Se le agregará un gol por haber ganado");
        Finalizar();
    }
}
