package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoGenerales;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoPT;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoGeneralesRepository;
import cr.ac.una.tarea_a.d.s.repositories.EstadisticasEquipoPTRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class EstadisticasEquipoController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label lblNombreEquipo;
    @FXML
    private Label lblTorneosGanados;
    @FXML
    private Label lblGolesTotales;
    @FXML
    private Label lblPuntosTotales;
    @FXML
    private Label lblPartidosGanados;
    @FXML
    private ComboBox<Torneo> cmbTorneos;
    @FXML
    private Label lblGolesTorneo;
    @FXML
    private Label lblPuntosTorneo;
    @FXML
    private Label lblPartidosGanadosTorneo;
    @FXML
    private Label lblEsGanador;
    
    private Equipo equipoSeleccionado;
    private EstadisticasEquipoPT equipoStats;
    private EstadisticasEquipoGenerales equipoGenStats;
    
    private final ObservableList<Torneo> torneoLista = FXCollections.observableArrayList();
    private final TorneoRepository Torneorepo = new TorneoRepository();
    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();
    private final ObservableList<EstadisticasEquipoPT> estadisticasLista = FXCollections.observableArrayList();
    private final EstadisticasEquipoPTRepository EstadisticasRepo = new EstadisticasEquipoPTRepository();
    private final ObservableList<EstadisticasEquipoGenerales> estadisticasGeneralesLista = FXCollections.observableArrayList();
    private final EstadisticasEquipoGeneralesRepository estadisticasGenRepo = new EstadisticasEquipoGeneralesRepository();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<EstadisticasEquipoPT> lista;
        try {
            lista = EstadisticasRepo.findAll();
            estadisticasLista.setAll(lista);
        } catch (IOException ex) {
            Logger.getLogger(EstadisticasEquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            List<EstadisticasEquipoGenerales> listaGenerales = estadisticasGenRepo.findAll();
            estadisticasGeneralesLista.setAll(listaGenerales);
        } catch (IOException ex) {
            Logger.getLogger(EstadisticasEquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        equipoSeleccionado = (Equipo) AppContext.getInstance().get("EQUIPO_SELECCIONADO");
        
        if (equipoSeleccionado != null) {
            for (EstadisticasEquipoPT est : estadisticasLista) {
                if (est.getIdEquipo().equals(equipoSeleccionado.getId())) {
                    equipoStats = est;
                    break;
                }
            }
            
        }

        if (equipoStats != null) {
            AppContext.getInstance().set("ESTADISTICAS_" + equipoSeleccionado.getNombre(), equipoStats);
        }

        for (EstadisticasEquipoGenerales estGen : estadisticasGeneralesLista) {
                if (estGen.getIdEquipo().equals(equipoSeleccionado.getId())) {
                    equipoGenStats = estGen;
                    break;
                }
            }
        
        lblNombreEquipo.setText(equipoSeleccionado.getNombre());
        
        if (equipoGenStats != null) {
            lblTorneosGanados.setText(String.valueOf(equipoGenStats.getTorneosGanados()));
            lblGolesTotales.setText(String.valueOf(equipoGenStats.getGolesAFavor()));
            lblPuntosTotales.setText(String.valueOf(equipoGenStats.getPuntos()));
            lblPartidosGanados.setText(String.valueOf(equipoGenStats.getPartidosGanados()));
        } else {
            lblTorneosGanados.setText("0");
            lblGolesTotales.setText("0");
            lblPuntosTotales.setText("0");
            lblPartidosGanados.setText("0");
        }
        
        try {
            cargarTorneosPorDeporte(equipoSeleccionado.getTipoDeporte());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        cmbTorneos.setOnAction(this::onActionCmbTorneos);
            
    }
    
    @Override
    public void initialize() {
    }
    
    private void cargarTorneosPorDeporte(String deporte) throws IOException {
        torneoLista.clear();

        for (Torneo torneo : Torneorepo.findAll()) {
            if (torneo.getTipoDeporte().equalsIgnoreCase(deporte)) {
            torneoLista.add(torneo);
        }
    }

    cmbTorneos.setItems(torneoLista);

    // Mostrar solo el nombre del torneo en el ComboBox
    cmbTorneos.setCellFactory(lv -> new javafx.scene.control.ListCell<Torneo>() {
        @Override
        protected void updateItem(Torneo item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getNombre());
        }
    });

    cmbTorneos.setButtonCell(new javafx.scene.control.ListCell<Torneo>() {
        @Override
        protected void updateItem(Torneo item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getNombre());
        }
    });
    }
    @FXML
    private void onActionCmbTorneos(ActionEvent event) {
        Torneo torneo = cmbTorneos.getValue();
        if (torneo != null && equipoSeleccionado != null) {
            EstadisticasEquipoPT estadisticaPorTorneo = null;

            for (EstadisticasEquipoPT est : estadisticasLista) {
                if (est.getIdEquipo().equals(equipoSeleccionado.getId()) &&
                    est.getIdTorneo().equals(torneo.getId())) {
                    estadisticaPorTorneo = est;
                    break;
                }
            }

            if (estadisticaPorTorneo != null) {
                lblGolesTorneo.setText(String.valueOf(estadisticaPorTorneo.getGolesAFavorPT()));
                lblPuntosTorneo.setText(String.valueOf(estadisticaPorTorneo.getPuntosPT()));
                lblPartidosGanadosTorneo.setText(String.valueOf(estadisticaPorTorneo.getPartidosGanadosPT()));
                lblEsGanador.setText(estadisticaPorTorneo.esGanadorDT() ? "Sí" : "No");
            } else {
                lblGolesTorneo.setText("0");
                lblPuntosTorneo.setText("0");
                lblPartidosGanadosTorneo.setText("0");
                lblEsGanador.setText("No");
            }
        }
    }
    
}
