package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoGenerales;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoPT;
import cr.ac.una.tarea_a.d.s.model.Partida;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoGeneralesRepository;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoPTRepository;
import cr.ac.una.tarea_a.d.s.repositories.PartidaRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LlavesController extends Controller implements Initializable {

    private final PartidaRepository partidaRepository = new PartidaRepository();
    private final EquipoRepository Equiporepo = new EquipoRepository();
    private final Mensaje mensaje = new Mensaje();

    private Torneo torneo1;
    private List<List<Equipo>> llavesPorRonda = new ArrayList<>();
    private List<Equipo> ganadoresTemporales = new ArrayList<>();

    @FXML
    private Label txfNombreTorneo;
    @FXML
    private VBox root;
    @FXML
    private HBox hboxLlaves;
    @FXML
    private StackPane stackPaneLlaves;
    @FXML
    private Pane linePane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txfNombreTorneo.setFocusTraversable(true);
        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");
        if (torneo1 != null) {
            generarEstructuraLlaves();
            llenarPrimerRonda();
            conectarPartidosConLineas();
        } else {
            System.out.println("Torneo null en LlavesController");
        }
    }

    
//    Este metodo solo lo usamos para saber si hay que agregar equipos "BYE "
//    para completar las llaves, y si cuantos hay que agregar
    private int siguientePotencia(int cantidadEquipos) {
        return (int) Math.pow(2, Math.ceil(Math.log(cantidadEquipos) / Math.log(2)));
    }

    
//    Este metodo genera la estructura de las llaves en base del total de equipos
//    incluso con los "BYE"
    private void generarEstructuraLlaves() {
        hboxLlaves.getChildren().clear();
        llavesPorRonda.clear();

        List<Equipo> equipos = new ArrayList<>(torneo1.getEquiposInscritos());
        int totalEquipos = siguientePotencia(equipos.size());
        int rondas = (int) (Math.log(totalEquipos) / Math.log(2));
        int numByes = totalEquipos - equipos.size();

        List<Equipo> equiposBye = new ArrayList<>();
        for (int i = 0; i < numByes; i++) {
            equiposBye.add(new Equipo(java.util.UUID.randomUUID().toString(), "BYE", torneo1.getTipoDeporte()));
        }
        equipos = intercalarEquipos(equipos, equiposBye);
        torneo1.setEquiposInscritos(equipos);
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
    
    
//    Este metodo solo conecta un VBox con el siguiente Vbox con una linea en L
//    es como una utilidad para conectarPartidosConLineas()
    private void dibujarLineaEnL(Button origen, Button destino) {
        Bounds boundsOrigenScene = origen.localToScene(origen.getBoundsInLocal());
        Bounds boundsDestinoScene = destino.localToScene(destino.getBoundsInLocal());

        Bounds origenLocal = linePane.sceneToLocal(boundsOrigenScene);
        Bounds destinoLocal = linePane.sceneToLocal(boundsDestinoScene);

        double startX = origenLocal.getMinX() + origenLocal.getWidth();
        double startY = origenLocal.getMinY() + origenLocal.getHeight() / 2;
        double endX = destinoLocal.getMinX();
        double endY = destinoLocal.getMinY() + destinoLocal.getHeight() / 2;

        double midX = (startX + endX) / 2;

        Line lineaH1 = new Line(startX, startY, midX, startY);
        Line lineaV = new Line(midX, startY, midX, endY);
        Line lineaH2 = new Line(midX, endY, endX, endY);

        for (Line l : List.of(lineaH1, lineaV, lineaH2)) {
            l.setStrokeWidth(3);
            l.setStyle("-fx-stroke: black;");
            linePane.getChildren().add(l);
        }
    }

    
//    Este metodo basicamente va recorriendo los Vbox de dos en dos para usar
//    el anterior metodo para ir conectando toda la estructura con las lineas
    private void conectarPartidosConLineas() {
        Platform.runLater(() -> {
            linePane.getChildren().clear();

            for (int ronda = 0; ronda < hboxLlaves.getChildren().size() - 1; ronda++) {

                VBox rondaActual = (VBox) hboxLlaves.getChildren().get(ronda);
                VBox siguienteRonda = (VBox) hboxLlaves.getChildren().get(ronda + 1);

                for (int i = 0; i < siguienteRonda.getChildren().size(); i++) {
                    VBox partidaSiguiente = (VBox) siguienteRonda.getChildren().get(i);
                    VBox partida1 = (VBox) rondaActual.getChildren().get(i * 2);
                    VBox partida2 = (VBox) rondaActual.getChildren().get(i * 2 + 1);

                    Button btn1 = (Button) ((VBox) partida1.getChildren().get(1)).getChildren().get(0);
                    Button btn2 = (Button) ((VBox) partida2.getChildren().get(1)).getChildren().get(0);
                    Button btnDestino = (Button) ((VBox) partidaSiguiente.getChildren().get(1)).getChildren().get(0);

                    dibujarLineaEnL(btn1, btnDestino);
                    dibujarLineaEnL(btn2, btnDestino);
                }
            }
        });
    }
    
       
//    Este metodo recorre la lista total de equipos con los BYE incluidos
//    y separa los "BYE" para que no hayan partidos de "BYE" vs "BYE"
    private List<Equipo> intercalarEquipos(List<Equipo> equipos, List<Equipo> equiposBye) {
        List<Equipo> listaIntercalada = new ArrayList<>(equipos);
        int insertIndex = listaIntercalada.size();

        for (int i = 0; i < equiposBye.size(); i++) {
            listaIntercalada.add(insertIndex, equiposBye.get(i));
            insertIndex--;
        }

        return listaIntercalada;
    }

    
//    Este metodo llena la primer ronda con el total de equipos
//    se llama solo cuando se crea el torneo por primera vez
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

            if (!eq1.getNombre().equals("POR DEFINIR") && !eq2.getNombre().equals("POR DEFINIR")) {
                btn.setDisable(false);
            }

            btn.setUserData(new Object[]{eq1, eq2, 0, index});
            btn.setOnAction(e -> {

                Object[] data = (Object[]) btn.getUserData();

                Equipo equipo1 = (Equipo) data[0];
                Equipo equipo2 = (Equipo) data[1];

                AppContext.getInstance().set("EQUIPO1", data[0]);
                AppContext.getInstance().set("EQUIPO2", data[1]);
                AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());

                Boolean partidoBye = false;

                if ("BYE".equalsIgnoreCase(equipo2.getNombre())) {
                    partidoBye = true;
                }
                if (partidoBye) {
                    Equipo ganador;
                    if ("BYE".equalsIgnoreCase(equipo2.getNombre())) {
                        ganador = equipo1;
                        System.out.println("EQUIPO 1 AVANZA POR BYE");
                    } else {
                        ganador = equipo1;
                        System.out.println("EQUIPO 2 AVANZA POR BYE");
                    }
                    AppContext.getInstance().set("EQUIPO_GANADOR_BYE", ganador);
                    Torneo torneo = (Torneo) AppContext.getInstance().get("TORNEO");
                    if (torneo != null) {
                        Partida partida = new Partida(
                                null, // id será generado por el repositorio
                                torneo.getId(),
                                equipo1.getId(),
                                equipo2.getId(),
                                0,
                                0,
                                "finalizado",
                                ganador.getId(),
                                0
                        );

                        try {
                            partidaRepository.save(partida);
                            List<Partida> partidasDelTorneo = torneo.getPartidas();

                            Partida existente = partidasDelTorneo.stream()
                                    .filter(p -> (p.getIdEquipoA().equals(partida.getIdEquipoA()) && p.getIdEquipoB().equals(partida.getIdEquipoB()))
                                    || (p.getIdEquipoA().equals(partida.getIdEquipoB()) && p.getIdEquipoB().equals(partida.getIdEquipoA())))
                                    .findFirst().orElse(null);

                            if (existente != null) {
                                partidasDelTorneo.remove(existente); // reemplazamos
                            }

                            partidasDelTorneo.add(partida);

                            try {
                                new TorneoRepository().save(torneo);
                                System.out.println("Torneo actualizado con nuevas partidas.");
                            } catch (IOException j) {
                                System.err.println("No se pudo guardar el torneo actualizado: " + j.getMessage());
                            }

                            System.out.println("Partida guardada exitosamente en JSON.");
                        } catch (IOException em) {
                            System.err.println("Error al guardar la partida: " + em.getMessage());
                        }
                    }
                    mostrarAlertaBye(ganador);
                    procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
                    return;
                }
                FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);

            });

            Partida partida = new Partida();
            partida.setId(java.util.UUID.randomUUID().toString());
            partida.setIdTorneo(torneo1.getId());
            partida.setIdEquipoA(eq1.getId());
            partida.setIdEquipoB(eq2.getId());
            partida.setEstado("pendiente");
            partida.setGolesEquipoA(0);
            partida.setGolesEquipoB(0);
            partida.setTiempoRestante(torneo1.getTiempoPorPartida());
            index++;
        }

    }

    
//    Este metodo se llama cada que termina un partido, y guarda al ganador en
//    una lista de ganadores que se usara en llenarRondaSiguiente()
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
            mostrarAnimacionGanadorPartido(ganador);

            if (ganador == null) {
                return;
            }
            ganadoresTemporales.add(ganador);

            VBox rondaVBox = (VBox) hboxLlaves.getChildren().get(rondaActual);
            VBox partidoVBox = (VBox) rondaVBox.getChildren().get(partidoIndex);
            VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
            Button btn = (Button) marcadorVBox.getChildren().get(0);
            btn.setText(ultima.getGolesEquipoA() + " - " + ultima.getGolesEquipoB());
            btn.setDisable(true);

            if (ganadoresTemporales.size() == rondaVBox.getChildren().size()) {
                if (rondaActual + 1 < hboxLlaves.getChildren().size()) {
                    llenarRondaSiguiente(rondaActual + 1, new ArrayList<>(ganadoresTemporales));
                    ganadoresTemporales.clear();

                } else {
                    mostrarCampeon(ganador);
                }
            }
            onShow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
//    Este metodo setea los ganadores guardados en procesarGanadorDespuesDePartido()
//    en la siguieente ronda
    private void llenarRondaSiguiente(int rondaIndex, List<Equipo> ganadores) {
        VBox rondaVBox = (VBox) hboxLlaves.getChildren().get(rondaIndex);
        int index = 0;
        for (int i = 0; i < ganadores.size(); i += 2) {
            Equipo eq1 = ganadores.get(i);
            if (ganadores.size() % 2 != 0) {
                return;
            }
            Equipo eq2 = ganadores.get(i + 1);

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
        onShow();
    }

    
//    Este metodo basicamente actualiza o refresca las llaves para que 
//    se muestren los partidos o rondas actualizadas
    public void onShow() {

        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");

        if (torneo1 != null) {
            txfNombreTorneo.setText(torneo1.getNombre());
            generarEstructuraLlaves();

            if ("enCurso".equalsIgnoreCase(torneo1.getEstado()) || "finalizado".equalsIgnoreCase(torneo1.getEstado())) {
                reconstruirDesdePartidas();
                hboxLlaves.applyCss();
                hboxLlaves.layout();
                conectarPartidosConLineas();

            } else {
                llenarPrimerRonda();
                conectarPartidosConLineas();
                torneo1.setEstado("enCurso");
                try {
                    new TorneoRepository().save(torneo1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Torneo null en LlavesController");
        }
    }

    
//    Es el metodo principal para que funcione la persistencia
//    aqui se llaman todos los metodos necesarios para reconstruir
//    llaves, partidas jugadas, y partidas por jugar, es el metodo
//    mas importante del controller
    private void reconstruirDesdePartidas() {
        try {
            PartidaRepository repo = new PartidaRepository();
            List<Partida> partidasGuardadas = repo.findAll();

            List<Partida> partidasTorneo = partidasGuardadas.stream()
                    .filter(p -> p.getIdTorneo().equals(torneo1.getId()))
                    .toList();

            List<Equipo> rondaActual = new ArrayList<>(torneo1.getEquiposInscritos());

            int rondaIndex = 0;

            while (rondaActual.size() > 1) {
                if (rondaIndex > 0) {

                    List<Equipo> rondaAnterior = llavesPorRonda.get(rondaIndex - 1);
                    if (rondaAnterior.size() / 2 != rondaActual.size()) {
                        return;
                    }
                }

                VBox rondaVBox = (VBox) hboxLlaves.getChildren().get(rondaIndex);
                List<Equipo> ganadores = new ArrayList<>();

                for (int i = 0; i < rondaActual.size(); i += 2) {
                    Equipo eq1 = rondaActual.get(i);
                    Equipo eq2 = rondaActual.get(i + 1);

                    if (i / 2 >= rondaVBox.getChildren().size()) {
                        System.err.println("Error: El partido #" + (i / 2) + " no existe en la ronda " + rondaIndex);
                        continue;
                    }

                    VBox partidoVBox = (VBox) rondaVBox.getChildren().get(i / 2);

                    HBox hbox1 = (HBox) partidoVBox.getChildren().get(0);
                    HBox hbox2 = (HBox) partidoVBox.getChildren().get(2);
                    Label label1 = (Label) hbox1.getChildren().get(0);
                    Label label2 = (Label) hbox2.getChildren().get(0);
                    label1.setText(eq1.getNombre());
                    label2.setText(eq2.getNombre());

                    VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
                    Button btn = (Button) marcadorVBox.getChildren().get(0);

                    Partida partida = partidasTorneo.stream()
                            .filter(p -> (p.getIdEquipoA().equals(eq1.getId()) && p.getIdEquipoB().equals(eq2.getId()))
                            || (p.getIdEquipoA().equals(eq2.getId()) && p.getIdEquipoB().equals(eq1.getId())))
                            .findFirst().orElse(null);

                    if (partida != null && "finalizado".equals(partida.getEstado())) {
                        btn.setText(partida.getGolesEquipoA() + " - " + partida.getGolesEquipoB());
                        btn.setDisable(true);
                        Equipo ganador = buscarEquipoPorId(partida.getGanadorId());
                        if (ganador != null) {
                            ganadores.add(ganador);
                        }
                    } else {
                        btn.setDisable(false);
                        btn.setUserData(new Object[]{eq1, eq2, rondaIndex, i / 2});
                        btn.setOnAction(e -> {
                            Object[] data = (Object[]) btn.getUserData();
                            AppContext.getInstance().set("EQUIPO1", data[0]);
                            AppContext.getInstance().set("EQUIPO2", data[1]);
                            AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());

                            Equipo equipo1 = (Equipo) data[0];
                            Equipo equipo2 = (Equipo) data[1];
                            boolean partidoBye = "BYE".equalsIgnoreCase(equipo1.getNombre()) || "BYE".equalsIgnoreCase(equipo2.getNombre());

                            if (partidoBye) {
                                Equipo ganador = !"BYE".equalsIgnoreCase(equipo1.getNombre()) ? equipo1 : equipo2;
                                AppContext.getInstance().set("EQUIPO_GANADOR_BYE", ganador);
                                guardarPartidaBye(equipo1, equipo2, ganador);
                                mostrarAlertaBye(ganador);
                                ganadoresTemporales.add(ganador);
                                procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
                                return;
                            }

                            FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                            procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
                        });
                    }
                }

                llavesPorRonda.add(ganadores);
                rondaActual = ganadores;

                ganadoresTemporales.clear();
                ganadoresTemporales.addAll(ganadores);
                rondaIndex++;
            }

            boolean torneoCompleto = torneo1.getPartidas().stream()
                    .filter(p -> p.getEstado().equalsIgnoreCase("finalizado"))
                    .count() >= torneo1.getEquiposInscritos().size() - 1;

            if (torneoCompleto) {
                Partida ultimaPartida = partidasTorneo.get(partidasTorneo.size() - 1);
                if (ultimaPartida != null && "finalizado".equalsIgnoreCase(ultimaPartida.getEstado()) && ultimaPartida.getGanadorId() != null) {
                    Equipo campeon = buscarEquipoPorId(ultimaPartida.getGanadorId());
                    if (campeon != null) {
                        mostrarCampeon(campeon);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
//    este metodo se llama cuando hay un partidoBYE osea uno de los equipos pasa 
//    directo, lo hicimos en un metodo por aparte por motivos de 
//    funcionalidad
    private void guardarPartidaBye(Equipo equipo1, Equipo equipo2, Equipo ganador) {
        try {
            Partida partidaBye = new Partida(
                    null,
                    torneo1.getId(),
                    equipo1.getId(),
                    equipo2.getId(),
                    0, 0,
                    "finalizado",
                    ganador.getId(),
                    0
            );

            partidaRepository.save(partidaBye);
            List<Partida> partidasDelTorneo = torneo1.getPartidas();
            partidasDelTorneo.removeIf(p
                    -> (p.getIdEquipoA().equals(partidaBye.getIdEquipoA()) && p.getIdEquipoB().equals(partidaBye.getIdEquipoB()))
                    || (p.getIdEquipoA().equals(partidaBye.getIdEquipoB()) && p.getIdEquipoB().equals(partidaBye.getIdEquipoA())));
            partidasDelTorneo.add(partidaBye);
            new TorneoRepository().save(torneo1);

        } catch (IOException e) {
            System.err.println("Error al guardar partida BYE: " + e.getMessage());
        }
    }

    private void mostrarAlertaBye(Equipo ganador) {
        mensaje.show(Alert.AlertType.INFORMATION, "Partido no jugado",
                "Este partido no se jugó porque uno de los equipos tiene 'BYE'.\n\n¡" + ganador.getNombre() + " avanza automáticamente!");

    }

    private void limpiarVista() {
        txfNombreTorneo.setText("");
        hboxLlaves.getChildren().clear();
        llavesPorRonda.clear();
        ganadoresTemporales.clear();

        if (linePane != null) {
            linePane.getChildren().clear();
        }

    }

    private void mostrarAnimacionGanadorPartido(Equipo ganador) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea_a/d/s/view/AnimacionPorPartido.fxml"));
            Parent root = loader.load();
            AnimacionPorPartidoController controller = loader.getController();

            String escudoBase64 = ganador.getImagenBase64();
            String balonBase64 = (torneo1.getTipoDeporte() != null) ? buscarDeportePorNombre(torneo1.getTipoDeporte()).getImagenBase64() : "";

            controller.mostrarAnimacion(escudoBase64, balonBase64, ganador.getNombre());

            Stage stage = new Stage();
            stage.setTitle("¡Ganador de la partida!");

            stage.initStyle(StageStyle.UNDECORATED); //quitar equis en modales
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(Duration.seconds(4));
            delay.setOnFinished(e -> stage.close());
            delay.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAnimacionDelCampeon(Equipo campeon, Deporte deporte) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea_a/d/s/view/AnimacionFinal.fxml"));
            Parent root = loader.load();
            AnimacionFinalController controller = loader.getController();

            String escudoBase64 = campeon.getImagenBase64();
            String balonBase64 = (deporte != null) ? deporte.getImagenBase64() : "";

            controller.mostrarAnimacion(escudoBase64, balonBase64, campeon.getNombre());

            Stage stage = new Stage();
            stage.setTitle("Campeón del Torneo");

            stage.initStyle(StageStyle.UNDECORATED); //quitar equis en modales
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarCampeon(Equipo campeon) throws IOException {

        guardarEstadisticasCampeon(campeon.getId(), torneo1.getId());
        boolean esGanador = (boolean) AppContext.getInstance().get("ES_GANADOR");
        if (esGanador == true) {
            return;
        }
        campeon.cargarImagenDesdeBase64();
        Deporte deporte = torneo1.getTipoDeporte() != null ? buscarDeportePorNombre(torneo1.getTipoDeporte()) : null;
        if (deporte != null) {
            deporte.cargarImagenDesdeBase64();
        }

        mostrarAnimacionDelCampeon(campeon, deporte);

        torneo1.setEstado("finalizado");
        equiposDeleteTorneo(torneo1.getEquiposInscritos());

        List<Deporte> listaDeportes = null;
        String deporteUsado = torneo1.getTipoDeporte();
        DeporteRepository deporteRepo = new DeporteRepository();
        listaDeportes = deporteRepo.findAll();
        for (Deporte deporteActual : listaDeportes) {
            if (deporteActual.getNombre().equals(deporteUsado)) {
                System.out.println("borrando torneo" + deporteActual.getCantidadTorneosInscritos());
                deporteActual.deleteTorneoInscrito();
                deporteRepo.update(deporteActual);
                System.out.println(deporteActual.getCantidadTorneosInscritos());
            }
        }

        try {
            TorneoRepository torneoRepo = new TorneoRepository();
            torneoRepo.save(torneo1);
            System.out.println(" Torneo marcado como finalizado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppContext.getInstance().set("EQUIPO_CAMPEON", campeon);
        AppContext.getInstance().set("TORNEO_ACTUAL", torneo1);

    }

    private void guardarEstadisticasCampeon(String idCampeon, String idTorneoActual) throws IOException {
        EstadisticasEquipoPTRepository repoPT = new EstadisticasEquipoPTRepository();
        List<EstadisticasEquipoPT> listaStatsPT = repoPT.findAll(); // o filtrar por torneo si es necesario

        EstadisticasEquipoPT statsPTCampeon = null;
        for (EstadisticasEquipoPT stat : listaStatsPT) {
            if (stat.getIdEquipo().equals(idCampeon) && stat.getIdTorneo().equals(idTorneoActual)) {
                statsPTCampeon = stat;
                break;
            }
        }

        if (statsPTCampeon == null) {
            System.out.println("No se encontraron estadísticas por torneo para el campeón");
            return;
        }
        AppContext.getInstance().set("ES_GANADOR", statsPTCampeon.esGanadorDT());
        if (statsPTCampeon.esGanadorDT()) {
            System.out.println("Estadísticas ya marcadas como ganadoras, se omite actualización.");
            return;
        }

        EstadisticasEquipoGeneralesRepository repoGen = new EstadisticasEquipoGeneralesRepository();
        EstadisticasEquipoGenerales statsGenerales;
        try {
            statsGenerales = repoGen.findById(idCampeon).orElse(new EstadisticasEquipoGenerales(idCampeon));

            statsGenerales.setGolesAFavor(statsGenerales.getGolesAFavor() + statsPTCampeon.getGolesAFavorPT());
            statsGenerales.setPartidosGanados(statsGenerales.getPartidosGanados() + statsPTCampeon.getPartidosGanadosPT());
            statsGenerales.setPuntos(statsGenerales.getPuntos() + statsPTCampeon.getPuntosPT());
            statsGenerales.setTorneosGanados(statsGenerales.getTorneosGanados() + 1);
            statsPTCampeon.setEsGanadorDT(true);

            repoPT.save(statsPTCampeon);
            repoGen.save(statsGenerales);

            System.out.println("Estadísticas generales del campeón actualizadas correctamente");
        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas generales del campeón: " + e.getMessage());
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

    
//    recorre la lista de equipos y les setea el atributo de que ya no esta en torneo
//    para efectos de puedan ser eliminados/editados en la lista de Equipos en otra vista
    private void equiposDeleteTorneo(List<Equipo> equipos) {
        for (Equipo equipo : equipos) {
            equipo.deleteTorneo();
            try {

                Equiporepo.update(equipo);
                if ("BYE".equals(equipo.getNombre())) {
                    Equiporepo.deleteById(equipo.getId());
                    System.out.println("BYE ELIMINADO");
                }
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al actualizar equipo", "No se pudo actualizar el estado del equipo: " + equipo.getNombre());
                e.printStackTrace();
            }
        }
    }

    private Equipo buscarEquipoPorId(String id) {
        return torneo1.getEquiposInscritos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void initialize() {
    }

}
