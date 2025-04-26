package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CampoDeTiroController extends Controller implements Initializable {

    @FXML
    private ImageView imgBalon;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane CampoTiro;
    @FXML
    private ImageView imgEquipo;
    @FXML
    private Label lblNombreEquipo;
    @FXML
    private Label lblTemporizador;
    @FXML
    private Button btnIniciar;
    @FXML
    private MFXButton btnVolver;
    @FXML
    private Label lblInstrucciones;
    @FXML
    private Label lblGanador;

    private final Deporte deporte = (Deporte) AppContext.getInstance().get("DEPORTE_PARA_BALON");
    private final Equipo equipo1 = (Equipo) AppContext.getInstance().get("EQUIPO1");
    private final Equipo equipo2 = (Equipo) AppContext.getInstance().get("EQUIPO2");

    private boolean turnoEquipo1 = true;
    private double balonInicialX, balonInicialY;
    private double offsetX, offsetY;

    private boolean puedeMoverBalon = false;

    private Timeline cronometro;
    private int segundosEquipo1 = 0;
    private int segundosEquipo2 = 0;

    private final List<Node> dianas = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setDisable(true);
        btnVolver.setVisible(false);
        imgBalon.setImage(deporte.getImagen());

        Platform.runLater(() -> {
            generarDianas(8);
            configurarArrastreBalon();
            actualizarVistaEquipo();
        });
        mostrarInstrucciones();

    }

    //Este método genera las dianas aleatoriamente por encima del balon y dentro del anchor pane de CampoTiro
    private void generarDianas(int cantidad) {
        Image imagenDiana = new Image(getClass().getResourceAsStream("/cr/ac/una/tarea_a/d/s/resources/diana.png"));
        double anchoDiana = 80;
        double altoDiana = 80;
        double limiteInferior = imgBalon.getLayoutY() - altoDiana;

        for (int i = 0; i < cantidad; i++) {
            ImageView diana = new ImageView(imagenDiana);
            diana.setPreserveRatio(true);
            diana.setFitWidth(anchoDiana);
            diana.setFitHeight(altoDiana);

            boolean posicionValida;
            double x = 0;
            double y = 0;

            do {
                posicionValida = true;
                x = Math.random() * (CampoTiro.getWidth() - anchoDiana);
                y = Math.random() * (limiteInferior > 0 ? limiteInferior : 0);

                for (var nodo : dianas) {
                    if (nodo instanceof ImageView imgDiana) {
                        double centroXNodo = imgDiana.getLayoutX() + imgDiana.getFitWidth()/2;
                        double centroYNodo = imgDiana.getLayoutY() + imgDiana.getFitHeight()/2;
                        double centroXNew = x + anchoDiana/2;
                        double centroYNew = y + altoDiana/2;

                        double dx = centroXNew - centroXNodo;
                        double dy = centroYNew - centroYNodo;
                        double distancia = Math.sqrt(dx * dx + dy * dy);

                        if (distancia < (anchoDiana + altoDiana)/2) {
                            posicionValida = false;
                            break;
                        }
                    }
                }
            } while (!posicionValida);

            diana.setLayoutX(x);
            diana.setLayoutY(y);
            dianas.add(diana);
            CampoTiro.getChildren().add(diana);
        }
    }
    
    //Este método hace posible que el balón se pueda arrastrar poniendole su limite en CampoTiro
    // para que no se salga y verifica si el balón colisiona con alguna diana y además verifica que ya no hayan dianas para terminar el turno
    private void configurarArrastreBalon() {
        balonInicialX = imgBalon.getLayoutX();
        balonInicialY = imgBalon.getLayoutY();

        imgBalon.setOnMousePressed(e -> {
            if (!puedeMoverBalon) return;
            offsetX = e.getSceneX() - imgBalon.getLayoutX();
            offsetY = e.getSceneY() - imgBalon.getLayoutY();
        });

        imgBalon.setOnMouseDragged(e -> {
            if (!puedeMoverBalon) return;

            double nuevoX = e.getSceneX() - offsetX;
            double nuevoY = e.getSceneY() - offsetY;

            if (nuevoX < 0) nuevoX = 0;
            if (nuevoY < 0) nuevoY = 0;
            if (nuevoX + imgBalon.getFitWidth() > CampoTiro.getWidth()) {
                nuevoX = CampoTiro.getWidth() - imgBalon.getFitWidth();
            }
            if (nuevoY + imgBalon.getFitHeight() > CampoTiro.getHeight()) {
                nuevoY = CampoTiro.getHeight() - imgBalon.getFitHeight();
            }

            imgBalon.setLayoutX(nuevoX);
            imgBalon.setLayoutY(nuevoY);
        });

        imgBalon.setOnMouseReleased(e -> {
            if (!puedeMoverBalon) return;
            boolean colisiono = false;

            for (Node nodo : new ArrayList<>(dianas)) {
                if (nodo instanceof ImageView) {
                    ImageView diana = (ImageView) nodo;
                    double dx = (imgBalon.getLayoutX() + imgBalon.getFitWidth()/2) - (diana.getLayoutX() + diana.getFitWidth()/2);
                    double dy = (imgBalon.getLayoutY() + imgBalon.getFitHeight()/2) - (diana.getLayoutY() + diana.getFitHeight()/2);
                    double distancia = Math.sqrt(dx * dx + dy * dy);

                    if (distancia <= diana.getFitWidth()/2) {
                        CampoTiro.getChildren().remove(diana);
                        dianas.remove(diana);
                        colisiono = true;
                        break;
                    }
                }
            }
            imgBalon.setLayoutX(balonInicialX);
            imgBalon.setLayoutY(balonInicialY);
            if (dianas.isEmpty()) {
                terminarTurno();
            }
        });
    }

    //Este método termina el turno del primer equipo y si no ha terminado entonces pasa con el del equipo 2 y luego verifica el ganador o si hay un empate
    private void terminarTurno() {
        detenerCronometro();
        puedeMoverBalon = false;
        btnIniciar.setDisable(false);

        if (!turnoEquipo1) {
            if (segundosEquipo1 == segundosEquipo2) {
                segundosEquipo1 = 0;
                segundosEquipo2 = 0;
                turnoEquipo1 = true;

                new Mensaje().show(Alert.AlertType.INFORMATION, "Empate", "¡Hay un empate! Se jugará una revancha.");
                actualizarVistaEquipo();
                generarDianas(8);

            } else {
                String ganador;
                if (segundosEquipo1 < segundosEquipo2) {
                    ganador = equipo1.getNombre();
                    AppContext.getInstance().set("GANADOR_DESEMPATE", equipo1);
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Ganador Desempate", "¡" + equipo1.getNombre() + " ganó el desempate! Se le agregará un gol por haber ganado");
                } else {
                    ganador = equipo2.getNombre();
                    AppContext.getInstance().set("GANADOR_DESEMPATE", equipo2);
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Ganador Desempate", "¡" + equipo2.getNombre() + " ganó el desempate! Se le agregará un gol por haber ganado");
                }
                lblGanador.setText("¡El ganador es: " + ganador + "!");
                btnIniciar.setDisable(true);
                btnVolver.setDisable(false);
                btnVolver.setVisible(true);
            }

        } else {
            turnoEquipo1 = false;
            actualizarVistaEquipo();
            generarDianas(8);
        }
    }

    //Este método cambia la vista para que salga el nombre y el escudo del siguiente equipo
    private void actualizarVistaEquipo() {
        Equipo actual = turnoEquipo1 ? equipo1 : equipo2;
        lblNombreEquipo.setText(actual.getNombre());
        imgEquipo.setImage(actual.getImagen());
        lblTemporizador.setText("00:00");
    }

    //Este método inicia el cronometro y carga el tiempo a las variables que son comparadas en otro método para determinar el ganador
    private void iniciarCronometro() {
        if (cronometro != null) {
            cronometro.stop();
        }

        cronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (turnoEquipo1) {
                segundosEquipo1++;
                lblTemporizador.setText(formatearTiempo(segundosEquipo1));
            } else {
                segundosEquipo2++;
                lblTemporizador.setText(formatearTiempo(segundosEquipo2));
            }
        }));
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    private void detenerCronometro() {
        if (cronometro != null) {
            cronometro.stop();
        }
    }

    //Este metodo convierte si hay 60 segundos en 1 minuto para que en el label se vean bien
    private String formatearTiempo(int totalSegundos) {
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    //Este método es para mostrar las instrucciones por 15 segundos como un label
    private void mostrarInstrucciones() {
        lblInstrucciones.setText("¡Bienvenido al campo de tiro!\n"
                + "Cada equipo debe arrastrar el balón a las dianas.\n"
                + "Gana quien lo haga en el menor tiempo.\n"
                + "¡Buena suerte!");
        lblInstrucciones.setVisible(true);
        new Timeline(new KeyFrame(Duration.seconds(15), e -> lblInstrucciones.setVisible(false))).play();
    }

    @FXML
    private void onActionBtnIniciar(ActionEvent event) {
        puedeMoverBalon = true;
        btnIniciar.setDisable(true);
        iniciarCronometro();
    }

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize() {
    }
}
