package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.Partida;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.repositories.PartidaRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LlavesController extends Controller implements Initializable {

    private final PartidaRepository partidaRepository = new PartidaRepository();
     private final EquipoRepository Equiporepo = new EquipoRepository();

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
        txfNombreTorneo.setFocusTraversable(true);
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
        int numByes = totalEquipos - equipos.size();
        // Crear una lista de equipos BYE
        List<Equipo> equiposBye = new ArrayList<>();
        for (int i = 0; i < numByes; i++) {
            equiposBye.add(new Equipo(java.util.UUID.randomUUID().toString(), "BYE", torneo1.getTipoDeporte()));
        }
        equipos = intercalarEquipos(equipos, equiposBye);
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

    private List<Equipo> intercalarEquipos(List<Equipo> equipos, List<Equipo> equiposBye) {
        List<Equipo> listaIntercalada = new ArrayList<>();
        int i = 0, j = 0;

        // Alternar entre equipos reales y BYEs
        while (i < equipos.size() || j < equiposBye.size()) {
            if (i < equipos.size()) {
                listaIntercalada.add(equipos.get(i++));
            }
            if (j < equiposBye.size()) {
                listaIntercalada.add(equiposBye.get(j++));
            }
        }
        Collections.reverse(listaIntercalada);
        return listaIntercalada;
    }

    private void llenarPrimerRonda() {
        List<Equipo> equipos = llavesPorRonda.get(0);
        VBox primeraRondaVBox = (VBox) hboxLlaves.getChildren().get(0);

        int index = 0;
        for (int i = 0; i < equipos.size(); i += 2) {

            Equipo eq2 = equipos.get(i);
            Equipo eq1 = equipos.get(i + 1);

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

                // Obtener los objetos Equipo de los datos
                Equipo equipo1 = (Equipo) data[0];
                Equipo equipo2 = (Equipo) data[1];

                // Depuración para verificar los valores de los equipos
                System.out.println("Equipo 1: " + equipo1);
                System.out.println("Equipo 2: " + equipo2);

                AppContext.getInstance().set("EQUIPO1", data[0]);
                AppContext.getInstance().set("EQUIPO2", data[1]);
                AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());

                Boolean partidoBye = false;

//                System.out.println("equipo1: " + AppContext.getInstance().get("EQUIPO1"));
//                System.out.println("equipo2: " + AppContext.getInstance().get("EQUIPO2"));
                // Verificar si alguno de los equipos tiene el nombre "BYE"
                if ("BYE".equalsIgnoreCase(equipo2.getNombre())) {
                    partidoBye = true;
                }
                if (partidoBye) {
                    Equipo ganador;
                    // Verificar cuál equipo tiene el nombre "BYE" y establecer el ganador
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
                            // Agregar la partida al torneo
                            List<Partida> partidasDelTorneo = torneo.getPartidas();

// Verificar si ya estaba esa partida (por si reanudaste)
                            Partida existente = partidasDelTorneo.stream()
                                    .filter(p -> (p.getIdEquipoA().equals(partida.getIdEquipoA()) && p.getIdEquipoB().equals(partida.getIdEquipoB()))
                                    || (p.getIdEquipoA().equals(partida.getIdEquipoB()) && p.getIdEquipoB().equals(partida.getIdEquipoA())))
                                    .findFirst().orElse(null);

                            if (existente != null) {
                                partidasDelTorneo.remove(existente); // reemplazamos
                            }

                            partidasDelTorneo.add(partida);

// Guardar el torneo actualizado
                            try {
                                new TorneoRepository().save(torneo);
                                System.out.println("✅ Torneo actualizado con nuevas partidas.");
                            } catch (IOException j) {
                                System.err.println("❌ No se pudo guardar el torneo actualizado: " + j.getMessage());
                            }

                            System.out.println("✅ Partida guardada exitosamente en JSON.");
                        } catch (IOException em) {
                            System.err.println("❌ Error al guardar la partida: " + em.getMessage());
                        }
                    }

                    // Mostrar alerta informando que no se jugará el partido
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Partido no jugado");
                    alert.setHeaderText(null);
                    alert.setContentText("Este partido no se jugó porque uno de los equipos tiene 'BYE'.\n\n"
                            + "¡" + ganador.getNombre() + " avanza automáticamente!");
                    alert.showAndWait();

                    // Procesar avance directamente
                    procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
                    return; // No continúa a la vista de partido

                }
                FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
            });
            // Crear Partida y agregarla al torneo
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
            Equipo eq2 = i + 1 < ganadores.size() ? ganadores.get(i + 1) : new Equipo(java.util.UUID.randomUUID().toString(), "BYE", torneo1.getTipoDeporte());

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
        limpiarVista();
        torneo1 = (Torneo) AppContext.getInstance().get("TORNEO");

        if (torneo1 != null) {
            txfNombreTorneo.setText(torneo1.getNombre());
            generarEstructuraLlaves(); // esto arma la estructura base visual

            // ⚠️ Si el torneo está en curso o finalizado, recuperar estado
            if ("enCurso".equalsIgnoreCase(torneo1.getEstado()) || "finalizado".equalsIgnoreCase(torneo1.getEstado())) {
                reconstruirDesdePartidas();
            } else {
                llenarPrimerRonda(); // solo si es nuevo
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

    private void reconstruirDesdePartidas() {
        try {
            PartidaRepository repo = new PartidaRepository();
            List<Partida> partidasGuardadas = repo.findAll();

            // filtrar solo las del torneo actual
            List<Partida> partidasTorneo = partidasGuardadas.stream()
                    .filter(p -> p.getIdTorneo().equals(torneo1.getId()))
                    .toList();

            // ordenar por ronda lógica: en tu caso es por orden de creación, así que no se ocupa mucho
            List<Equipo> rondaActual = new ArrayList<>(torneo1.getEquiposInscritos());
            while (rondaActual.size() % 2 != 0) {
                rondaActual.add(new Equipo(java.util.UUID.randomUUID().toString(), "BYE", torneo1.getTipoDeporte()));
            }

            int rondaIndex = 0;
            while (rondaActual.size() > 1) {
                if (rondaActual.size() % 2 != 0) {
                    rondaActual.add(new Equipo(java.util.UUID.randomUUID().toString(), "BYE", torneo1.getTipoDeporte()));
                }
                VBox rondaVBox = (VBox) hboxLlaves.getChildren().get(rondaIndex);
                List<Equipo> ganadores = new ArrayList<>();

                for (int i = 0; i < rondaActual.size(); i += 2) {
                    Equipo eq1 = rondaActual.get(i);
                    Equipo eq2 = rondaActual.get(i + 1);
                    VBox partidoVBox = (VBox) rondaVBox.getChildren().get(i / 2);

                    HBox hbox1 = (HBox) partidoVBox.getChildren().get(0);
                    HBox hbox2 = (HBox) partidoVBox.getChildren().get(2);
                    Label label1 = (Label) hbox1.getChildren().get(0);
                    Label label2 = (Label) hbox2.getChildren().get(0);

                    label1.setText(eq1.getNombre());
                    label2.setText(eq2.getNombre());

                    VBox marcadorVBox = (VBox) partidoVBox.getChildren().get(1);
                    Button btn = (Button) marcadorVBox.getChildren().get(0);

                    // buscar partida de este par
                    Partida partida = partidasTorneo.stream()
                            .filter(p -> (p.getIdEquipoA().equals(eq1.getId()) && p.getIdEquipoB().equals(eq2.getId()))
                            || (p.getIdEquipoA().equals(eq2.getId()) && p.getIdEquipoB().equals(eq1.getId())))
                            .findFirst().orElse(null);

                    if (partida != null && "finalizado".equals(partida.getEstado())) {
                        btn.setDisable(true);
                        Equipo ganador = torneo1.getEquiposInscritos().stream()
                                .filter(e -> e.getId().equals(partida.getGanadorId()))
                                .findFirst().orElse(null);
                        if (ganador != null) {
                            ganadores.add(ganador);
                        }
                    } else if (!eq1.getNombre().equals("BYE") && !eq2.getNombre().equals("BYE")) {
                        btn.setDisable(false);
                        btn.setUserData(new Object[]{eq1, eq2, rondaIndex, i / 2});
                        btn.setOnAction(e -> {
                            Object[] data = (Object[]) btn.getUserData();
                            AppContext.getInstance().set("EQUIPO1", data[0]);
                            AppContext.getInstance().set("EQUIPO2", data[1]);
                            AppContext.getInstance().set("DEPORTE", torneo1.getTipoDeporte());
                            FlowController.getInstance().goViewInWindowModal("Partido", ((Stage) root.getScene().getWindow()), false);
                            procesarGanadorDespuesDePartido((int) data[2], (int) data[3]);
                        });
                    } else {
                        btn.setDisable(true);

                        // Solo agregamos un ganador por BYE si no existe partida pendiente entre ellos
                        if (partida == null) {
                            ganadores.add(!eq1.getNombre().equals("BYE") ? eq1 : eq2);
                        }
                    }

                }

                llavesPorRonda.add(ganadores);
                rondaActual = ganadores;
                rondaIndex++;
            }
            boolean torneoCompleto = torneo1.getPartidas().stream()
                    .filter(p -> p.getEstado().equalsIgnoreCase("finalizado"))
                    .count() >= torneo1.getCantidadEquipos() - 1;

            if (torneoCompleto && rondaActual.size() == 1) {
                mostrarCampeon(rondaActual.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        equiposSinTorneo(torneo1.getEquiposInscritos());

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
    private void equiposSinTorneo(List<Equipo> equipos) {
        for (Equipo equipo : equipos) {
            equipo.setEnTorneo(false);
            try {
                Equiporepo.update(equipo);
            } catch (IOException e) {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al actualizar equipo", "No se pudo actualizar el estado del equipo: " + equipo.getNombre());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize() {
    }

}
