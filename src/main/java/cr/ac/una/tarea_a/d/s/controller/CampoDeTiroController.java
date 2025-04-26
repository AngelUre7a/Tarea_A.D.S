package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
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

    private final List<Circle> dianas = new ArrayList<>();
    @FXML
    private Label lblGanador;
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setDisable(true);
        btnVolver.setVisible(false);
        imgBalon.setImage(deporte.getImagen());

        Platform.runLater(() -> {
            generarDianas(6);
            configurarArrastreBalon();
            actualizarVistaEquipo();
        });
        mostrarInstrucciones();

    }

    private void generarDianas(int cantidad) {
        double radio = 30;
        double limiteInferior = imgBalon.getLayoutY() - radio * 2;

        for (int i = 0; i < cantidad; i++) {
            Circle diana = new Circle(radio);
            diana.setFill(Color.RED);
            diana.setStroke(Color.BLACK);

            boolean posicionValida;
            double x = 0;
            double y = 0;

            do {
                posicionValida = true;
                x = Math.random() * (CampoTiro.getWidth() - radio * 2);
                y = Math.random() * (limiteInferior > 0 ? limiteInferior : 0);
                for (var nodo : dianas) {
                    double dx = (x + radio) - nodo.getLayoutX();
                    double dy = (y + radio) - nodo.getLayoutY();
                    double distancia = Math.sqrt(dx * dx + dy * dy);
                    if (distancia < radio * 2) {
                        posicionValida = false;
                        break;
                    }
                }
            } while (!posicionValida);

            diana.setLayoutX(x + radio);
            diana.setLayoutY(y + radio);
            dianas.add(diana);
            CampoTiro.getChildren().add(diana);
        }
    }

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
            imgBalon.setLayoutX(e.getSceneX() - offsetX);
            imgBalon.setLayoutY(e.getSceneY() - offsetY);
        });

        imgBalon.setOnMouseReleased(e -> {
            if (!puedeMoverBalon) return;
            boolean colisiono = false;

            for (Circle diana : new ArrayList<>(dianas)) {
                double dx = (imgBalon.getLayoutX() + imgBalon.getFitWidth() / 2) - diana.getLayoutX();
                double dy = (imgBalon.getLayoutY() + imgBalon.getFitHeight() / 2) - diana.getLayoutY();
                double distancia = Math.sqrt(dx * dx + dy * dy);

                if (distancia <= diana.getRadius()) {
                    CampoTiro.getChildren().remove(diana);
                    dianas.remove(diana);
                    colisiono = true;
                    break;
                }
            }
            imgBalon.setLayoutX(balonInicialX);
            imgBalon.setLayoutY(balonInicialY);
            if (dianas.isEmpty()) {
                terminarTurno();
            }
        });
    }

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
                generarDianas(6);

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
            generarDianas(6);
        }
        }

        private void actualizarVistaEquipo() {
            Equipo actual = turnoEquipo1 ? equipo1 : equipo2;
            lblNombreEquipo.setText(actual.getNombre());
            imgEquipo.setImage(actual.getImagen());
            lblTemporizador.setText("00:00");
    }

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

    private String formatearTiempo(int totalSegundos) {
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
    
    private void mostrarInstrucciones() {
        lblInstrucciones.setText("¡Bienvenido al campo de tiro!\n"
                + "Cada equipo debe arrastrar el balón a las dianas rojas.\n"
                + "Gana quien lo haga en el menor tiempo.\n"
                + "¡Buena suerte!");
        lblInstrucciones.setVisible(true);
        new Timeline(new KeyFrame(Duration.seconds(10), e -> lblInstrucciones.setVisible(false))).play();
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
