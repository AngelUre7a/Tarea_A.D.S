//package cr.ac.una.tarea_a.d.s.controller;
//
//import cr.ac.una.tarea_a.d.s.model.Equipo;
//import cr.ac.una.tarea_a.d.s.model.Torneo;
//import cr.ac.una.tarea_a.d.s.util.AppContext;
//import cr.ac.una.tarea_a.d.s.util.FlowController;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class LlavesController extends Controller implements Initializable {
//
//    private Torneo torneo1;
//
//    @FXML
//    private Label txfNombreTorneo;
//    @FXML
//    private VBox root;
//    @FXML
//    private HBox hboxLlaves;
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");
//        if (torneo1 != null) {
//            generarEstructuraLlaves();
//            llenarPrimerRonda();
//        } else {
//            System.out.println("Torneo null en LlavesController");
//        }
//    }
//
//    private int siguientePotencia(int cantidadEquipos) {
//        return (int) Math.pow(2, Math.ceil(Math.log(cantidadEquipos) / Math.log(2)));
//
//    }
//
////    private List<Equipo> generarGanadores(List<Equipo> equiposConBye) {
////        List<Equipo> ganadores = new ArrayList<>();
////        for (int i = 0; i < equiposConBye.size(); i += 2) {
////            ganadores.add(equiposConBye.get(i));
////        }
////        return ganadores;
////    }
//
//    private void generarEstructuraLlaves() {
//    int cantidadEquipos = torneo1.getCantidadEquipos();
//    int totalEquipos = siguientePotencia(cantidadEquipos);
//    int rondas = (int) (Math.log(totalEquipos) / Math.log(2));
//    hboxLlaves.getChildren().clear();
//
//    List<Equipo> equipos = new ArrayList<>(torneo1.getEquiposInscritos());
//
//    int partidosEnLaRonda = totalEquipos / 2;
//    int indiceEquipo = 0;
//
//    for (int r = 0; r < rondas; r++) {
//        VBox rondaVBox = new VBox();
//        rondaVBox.setSpacing((r * 100) + 20);
//        rondaVBox.setAlignment(Pos.CENTER);
//
//        for (int p = 0; p < partidosEnLaRonda; p++) {
//            VBox partidoVBox = new VBox();
//            partidoVBox.setSpacing(10);
//            partidoVBox.setAlignment(Pos.CENTER);
//
//            Label labelEquipo1 = new Label("POR DEFINIR");
//            Label labelEquipo2 = new Label("POR DEFINIR");
//
//            Equipo equipo1 = null;
//            Equipo equipo2 = null;
//
//            if (indiceEquipo < equipos.size()) {
//                equipo1 = equipos.get(indiceEquipo++);
//                labelEquipo1.setText(equipo1.getNombre());
//            }
//            if (indiceEquipo < equipos.size()) {
//                equipo2 = equipos.get(indiceEquipo++);
//                labelEquipo2.setText(equipo2.getNombre());
//            }
//
//            labelEquipo1.setStyle("-fx-text-fill: white;");
//            labelEquipo2.setStyle("-fx-text-fill: white;");
//
//            HBox equipoHBox1 = new HBox(labelEquipo1);
//            equipoHBox1.setAlignment(Pos.CENTER);
//
//            HBox equipoHBox2 = new HBox(labelEquipo2);
//            equipoHBox2.setAlignment(Pos.CENTER);
//
//            VBox marcadorVBox = new VBox();
//            marcadorVBox.setAlignment(Pos.CENTER);
//
//            Button btnIniciar = new Button("Iniciar");
//            btnIniciar.setDisable(equipo1 == null || equipo2 == null);
//
//            Equipo finalEquipo1 = equipo1;
//            Equipo finalEquipo2 = equipo2;
//
//            btnIniciar.setUserData(new Equipo[]{finalEquipo1, finalEquipo2});
//            btnIniciar.setOnAction(e -> {
//                Equipo[] datos = (Equipo[]) ((Button) e.getSource()).getUserData();
//
//                if (datos[0] != null && datos[1] != null) {
//                    AppContext.getInstance().set("EQUIPO1", datos[0]);
//                    AppContext.getInstance().set("EQUIPO2", datos[1]);
//                    AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());
//
//                    FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
//                }
//            });
//
//            marcadorVBox.getChildren().add(btnIniciar);
//            partidoVBox.getChildren().addAll(equipoHBox1, marcadorVBox, equipoHBox2);
//            rondaVBox.getChildren().add(partidoVBox);
//        }
//
//        hboxLlaves.getChildren().add(rondaVBox);
//        partidosEnLaRonda /= 2;
//    }
//}
//
//    private void llenarPrimerRonda() {
//        List<Equipo> equipos = new ArrayList<>(torneo1.getEquiposInscritos());
//        int cantidadEquipos = equipos.size();
//        int siguientePotencia = siguientePotencia(cantidadEquipos); // este ya lo tenés
//        int numByes = siguientePotencia - cantidadEquipos;
//
//        // Agregar BYEs al final si hace falta
//        for (int i = 0; i < numByes; i++) {
//            equipos.add(new Equipo("BYE"));
//        }
//
//        // Acceder al primer VBox de hboxRondas (la primera ronda)
//        if (hboxLlaves.getChildren().isEmpty()) {
//            return;
//        }
//
//        VBox primeraRondaVBox = (VBox) hboxLlaves.getChildren().get(0); // la primera ronda
//
//        int indexPartido = 0;
//
//        for (int i = 0; i < equipos.size(); i += 2) {
//            Equipo equipo1 = equipos.get(i);
//            Equipo equipo2 = equipos.get(i + 1);
//
//            VBox partidoVBox = (VBox) primeraRondaVBox.getChildren().get(indexPartido++);
//
//            // VBox del equipo 1
//            HBox equipoHBox1 = (HBox) partidoVBox.getChildren().get(0);
//            Label label1 = (Label) equipoHBox1.getChildren().get(0);
//            label1.setText(equipo1.getNombre());
//
//            // VBox del equipo 2
//            HBox equipoHBox2 = (HBox) partidoVBox.getChildren().get(2);
//            Label label2 = (Label) equipoHBox2.getChildren().get(0);
//            label2.setText(equipo2.getNombre());
//
//            VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
//            Button btnIniciar = (Button) marcadorVBox.getChildren().get(0);
//            if (!equipo1.getNombre().equals("BYE") && !equipo2.getNombre().equals("BYE")) {
//                btnIniciar.setDisable(false);
//            }
//        }
//    }
//
//    
//    public void onShow() {
//        limpiarVista();
//        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");
//        if (torneo1 != null) {
//            txfNombreTorneo.setText(torneo1.getNombre());
//            generarEstructuraLlaves();
//            llenarPrimerRonda();
//        } else {
//            System.out.println("Torneo null en LlavesController");
//        }
//    }
//    
//    private void limpiarVista() {
//        txfNombreTorneo.setText("");
//        hboxLlaves.getChildren().clear();
//    }
//    
//    @Override
//    public void initialize() {
//    }
//}
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.Partida;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.PartidaRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LlavesController extends Controller implements Initializable {

    private Torneo torneo1;
    private List<List<Equipo>> llavesPorRonda = new ArrayList<>();
    private List<Equipo> ganadoresTemporales = new ArrayList<>();

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

    private void generarEstructuraLlaves() {
        hboxLlaves.getChildren().clear();
        llavesPorRonda.clear();

        List<Equipo> equipos = new ArrayList<>(torneo1.getEquiposInscritos());
        int totalEquipos = siguientePotencia(equipos.size());
        int rondas = (int) (Math.log(totalEquipos) / Math.log(2));

        while (equipos.size() < totalEquipos) {
            equipos.add(new Equipo("BYE"));
        }
        llavesPorRonda.add(new ArrayList<>(equipos));

        int partidosEnLaRonda = totalEquipos / 2;
        for (int r = 0; r < rondas; r++) {
            VBox rondaVBox = new VBox();
            rondaVBox.setSpacing((r * 100) + 20);
            rondaVBox.setAlignment(Pos.CENTER);

            for (int p = 0; p < partidosEnLaRonda; p++) {
                VBox partidoVBox = new VBox(10);
                partidoVBox.setAlignment(Pos.CENTER);

                Label label1 = new Label("POR DEFINIR");
                Label label2 = new Label("POR DEFINIR");
                label1.setStyle("-fx-text-fill: white;");
                label2.setStyle("-fx-text-fill: white;");

                HBox hbox1 = new HBox(label1);
                HBox hbox2 = new HBox(label2);
                hbox1.setAlignment(Pos.CENTER);
                hbox2.setAlignment(Pos.CENTER);

                VBox marcadorVBox = new VBox();
                marcadorVBox.setAlignment(Pos.CENTER);

                Button btnIniciar = new Button("Iniciar");
                btnIniciar.setDisable(true);

                marcadorVBox.getChildren().add(btnIniciar);
                partidoVBox.getChildren().addAll(hbox1, marcadorVBox, hbox2);
                rondaVBox.getChildren().add(partidoVBox);
            }
            hboxLlaves.getChildren().add(rondaVBox);
            partidosEnLaRonda /= 2;
        }
    }

    private void llenarPrimerRonda() {
        List<Equipo> equipos = llavesPorRonda.get(0);
        VBox primeraRondaVBox = (VBox) hboxLlaves.getChildren().get(0);

        int index = 0;
        for (int i = 0; i < equipos.size(); i += 2) {
            Equipo eq1 = equipos.get(i);
            Equipo eq2 = equipos.get(i + 1);

            VBox partidoVBox = (VBox) primeraRondaVBox.getChildren().get(index);
            HBox hbox1 = (HBox) partidoVBox.getChildren().get(0);
            Label label1 = (Label) hbox1.getChildren().get(0);
            label1.setText(eq1.getNombre());

            HBox hbox2 = (HBox) partidoVBox.getChildren().get(2);
            Label label2 = (Label) hbox2.getChildren().get(0);
            label2.setText(eq2.getNombre());

            VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
            Button btn = (Button) marcadorVBox.getChildren().get(0);

            if (!eq1.getNombre().equals("BYE") && !eq2.getNombre().equals("BYE")) {
                btn.setDisable(false);
            }
            btn.setUserData(new Object[]{eq1, eq2, 0, index});
            btn.setOnAction(e -> {
                Object[] data = (Object[]) btn.getUserData();
                AppContext.getInstance().set("EQUIPO1", data[0]);
                AppContext.getInstance().set("EQUIPO2", data[1]);
                AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());
                FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
            });
            index++;
        }
    }

    private void procesarGanadorDespuesDePartido(int rondaActual, int partidoIndex) {
        try {
            PartidaRepository repo = new PartidaRepository();
            List<Partida> partidas = repo.findAll();
            if (partidas.isEmpty()) {
                return;
            }

            Partida ultima = partidas.get(partidas.size() - 1);
            String idGanador = ultima.getGanadorId();

            Equipo ganador = torneo1.getEquiposInscritos().stream()
                    .filter(e -> e.getId().equals(idGanador))
                    .findFirst().orElse(null);

            if (ganador == null) {
                return;
            }
            ganadoresTemporales.add(ganador);

            VBox rondaVBox = (VBox) hboxLlaves.getChildren().get(rondaActual);
            VBox partidoVBox = (VBox) rondaVBox.getChildren().get(partidoIndex);
            VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
            Button btn = (Button) marcadorVBox.getChildren().get(0);
            btn.setDisable(true);

            if (ganadoresTemporales.size() == rondaVBox.getChildren().size()) {
                if (rondaActual + 1 < hboxLlaves.getChildren().size()) {
                    llenarRondaSiguiente(rondaActual + 1, new ArrayList<>(ganadoresTemporales));
                    ganadoresTemporales.clear();
                } else {
                    mostrarCampeon(ganador);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarRondaSiguiente(int rondaIndex, List<Equipo> ganadores) {
        VBox rondaVBox = (VBox) hboxLlaves.getChildren().get(rondaIndex);
        int index = 0;
        for (int i = 0; i < ganadores.size(); i += 2) {
            Equipo eq1 = ganadores.get(i);
            Equipo eq2 = i + 1 < ganadores.size() ? ganadores.get(i + 1) : new Equipo("BYE");

            VBox partidoVBox = (VBox) rondaVBox.getChildren().get(index);
            HBox hbox1 = (HBox) partidoVBox.getChildren().get(0);
            Label label1 = (Label) hbox1.getChildren().get(0);
            label1.setText(eq1.getNombre());

            HBox hbox2 = (HBox) partidoVBox.getChildren().get(2);
            Label label2 = (Label) hbox2.getChildren().get(0);
            label2.setText(eq2.getNombre());

            VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
            Button btn = (Button) marcadorVBox.getChildren().get(0);

            if (!eq1.getNombre().equals("BYE") && !eq2.getNombre().equals("BYE")) {
                btn.setDisable(false);
            }
            btn.setUserData(new Object[]{eq1, eq2, rondaIndex, index});
            btn.setOnAction(e -> {
                Object[] data = (Object[]) btn.getUserData();
                AppContext.getInstance().set("EQUIPO1", data[0]);
                AppContext.getInstance().set("EQUIPO2", data[1]);
                AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());
                FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
            });
            index++;
        }
        llavesPorRonda.add(ganadores);
    }

    public void onShow() {
        if (!"enCurso".equalsIgnoreCase(torneo1.getEstado())) {
            torneo1.setEstado("enCurso");
            try {
                new TorneoRepository().save(torneo1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void limpiarVista() {
        txfNombreTorneo.setText("");
        hboxLlaves.getChildren().clear();
        llavesPorRonda.clear();
        ganadoresTemporales.clear();
    }

    private void mostrarAnimacionDelCampeon(Equipo campeon, Deporte deporte) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea_a/d/s/view/AnimacionFinal.fxml"));
            Parent root = loader.load();
            AnimacionFinalController controller = loader.getController();

            String escudoBase64 = campeon.getImagenBase64();
            String balonBase64 = deporte != null ? deporte.getImagenBase64() : "";

            controller.mostrarAnimacion(escudoBase64, balonBase64, campeon.getNombre());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("🏆 Campeón del Torneo");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarCampeon(Equipo campeon) {
        System.out.println("🏆 CAMPEÓN: " + campeon.getNombre());

        campeon.cargarImagenDesdeBase64();
        Deporte deporte = torneo1.getTipoDeporte() != null ? buscarDeportePorNombre(torneo1.getTipoDeporte()) : null;
        if (deporte != null) {
            deporte.cargarImagenDesdeBase64();
        }

        mostrarAnimacionDelCampeon(campeon, deporte);

        torneo1.setEstado("finalizado");

        try {
            TorneoRepository torneoRepo = new TorneoRepository();
            torneoRepo.save(torneo1); // Asumiendo que tu repositorio actualiza si ya existe
            System.out.println("✅ Torneo marcado como finalizado.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Deporte buscarDeportePorNombre(String nombreDeporte) {
        try {
            DeporteRepository repo = new DeporteRepository();
            for (Deporte d : repo.findAll()) {
                if (d.getNombre().equalsIgnoreCase(nombreDeporte)) {
                    return d;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize() {
    }
}
