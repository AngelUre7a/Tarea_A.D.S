package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LlavesController extends Controller implements Initializable {

    private Torneo torneo1;

    @FXML
    private Label txfNombreTorneo;
    @FXML
    private VBox root;
    @FXML
    private HBox hboxLlaves;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");
        if (torneo1 != null) {
            generarEstructuraLlaves();
            llenarPrimerRonda();
        } else {
            System.out.println("Torneo null en LlavesController");
        }
    }

    private int siguientePotencia(int cantidadEquipos) {
        return (int) Math.pow(2, Math.ceil(Math.log(cantidadEquipos) / Math.log(2)));

    }

//    private List<Equipo> generarGanadores(List<Equipo> equiposConBye) {
//        List<Equipo> ganadores = new ArrayList<>();
//        for (int i = 0; i < equiposConBye.size(); i += 2) {
//            ganadores.add(equiposConBye.get(i));
//        }
//        return ganadores;
//    }

    private void generarEstructuraLlaves() {
    int cantidadEquipos = torneo1.getCantidadEquipos();
    int totalEquipos = siguientePotencia(cantidadEquipos);
    int rondas = (int) (Math.log(totalEquipos) / Math.log(2));
    hboxLlaves.getChildren().clear();

    List<Equipo> equipos = new ArrayList<>(torneo1.getEquiposInscritos());

    int partidosEnLaRonda = totalEquipos / 2;
    int indiceEquipo = 0;

    for (int r = 0; r < rondas; r++) {
        VBox rondaVBox = new VBox();
        rondaVBox.setSpacing((r * 100) + 20);
        rondaVBox.setAlignment(Pos.CENTER);

        for (int p = 0; p < partidosEnLaRonda; p++) {
            VBox partidoVBox = new VBox();
            partidoVBox.setSpacing(10);
            partidoVBox.setAlignment(Pos.CENTER);

            Label labelEquipo1 = new Label("POR DEFINIR");
            Label labelEquipo2 = new Label("POR DEFINIR");

            Equipo equipo1 = null;
            Equipo equipo2 = null;

            if (indiceEquipo < equipos.size()) {
                equipo1 = equipos.get(indiceEquipo++);
                labelEquipo1.setText(equipo1.getNombre());
            }
            if (indiceEquipo < equipos.size()) {
                equipo2 = equipos.get(indiceEquipo++);
                labelEquipo2.setText(equipo2.getNombre());
            }

            labelEquipo1.setStyle("-fx-text-fill: white;");
            labelEquipo2.setStyle("-fx-text-fill: white;");

            HBox equipoHBox1 = new HBox(labelEquipo1);
            equipoHBox1.setAlignment(Pos.CENTER);

            HBox equipoHBox2 = new HBox(labelEquipo2);
            equipoHBox2.setAlignment(Pos.CENTER);

            VBox marcadorVBox = new VBox();
            marcadorVBox.setAlignment(Pos.CENTER);

            Button btnIniciar = new Button("Iniciar");
            btnIniciar.setDisable(equipo1 == null || equipo2 == null);

            Equipo finalEquipo1 = equipo1;
            Equipo finalEquipo2 = equipo2;

            btnIniciar.setUserData(new Equipo[]{finalEquipo1, finalEquipo2});
            btnIniciar.setOnAction(e -> {
                Equipo[] datos = (Equipo[]) ((Button) e.getSource()).getUserData();

                if (datos[0] != null && datos[1] != null) {
                    AppContext.getInstance().set("EQUIPO1", datos[0]);
                    AppContext.getInstance().set("EQUIPO2", datos[1]);
                    AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());

                    FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                }
            });

            marcadorVBox.getChildren().add(btnIniciar);
            partidoVBox.getChildren().addAll(equipoHBox1, marcadorVBox, equipoHBox2);
            rondaVBox.getChildren().add(partidoVBox);
        }

        hboxLlaves.getChildren().add(rondaVBox);
        partidosEnLaRonda /= 2;
    }
}

    private void llenarPrimerRonda() {
        List<Equipo> equipos = new ArrayList<>(torneo1.getEquiposInscritos());
        int cantidadEquipos = equipos.size();
        int siguientePotencia = siguientePotencia(cantidadEquipos); // este ya lo tenés
        int numByes = siguientePotencia - cantidadEquipos;

        // Agregar BYEs al final si hace falta
        for (int i = 0; i < numByes; i++) {
            equipos.add(new Equipo("BYE"));
        }

        // Acceder al primer VBox de hboxRondas (la primera ronda)
        if (hboxLlaves.getChildren().isEmpty()) {
            return;
        }

        VBox primeraRondaVBox = (VBox) hboxLlaves.getChildren().get(0); // la primera ronda

        int indexPartido = 0;

        for (int i = 0; i < equipos.size(); i += 2) {
            Equipo equipo1 = equipos.get(i);
            Equipo equipo2 = equipos.get(i + 1);

            VBox partidoVBox = (VBox) primeraRondaVBox.getChildren().get(indexPartido++);

            // VBox del equipo 1
            HBox equipoHBox1 = (HBox) partidoVBox.getChildren().get(0);
            Label label1 = (Label) equipoHBox1.getChildren().get(0);
            label1.setText(equipo1.getNombre());

            // VBox del equipo 2
            HBox equipoHBox2 = (HBox) partidoVBox.getChildren().get(2);
            Label label2 = (Label) equipoHBox2.getChildren().get(0);
            label2.setText(equipo2.getNombre());

            VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
            Button btnIniciar = (Button) marcadorVBox.getChildren().get(0);
            if (!equipo1.getNombre().equals("BYE") && !equipo2.getNombre().equals("BYE")) {
                btnIniciar.setDisable(false);
            }
        }
    }

    
    public void onShow() {
        limpiarVista();
        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");
        if (torneo1 != null) {
            txfNombreTorneo.setText(torneo1.getNombre());
            generarEstructuraLlaves();
            llenarPrimerRonda();
        } else {
            System.out.println("Torneo null en LlavesController");
        }
    }
    
    private void limpiarVista() {
        txfNombreTorneo.setText("");
        hboxLlaves.getChildren().clear();
    }
    
    @Override
    public void initialize() {
    }
}
