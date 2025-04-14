/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label lblNombreEquipo;
    @FXML
    private Label lblTorneosGanados;
    @FXML
    private Label lblGolesTotales;
    @FXML
    private Label lblPuntosTotales;
    @FXML
    private ComboBox<Torneo> cmbTorneos;
    @FXML
    private Label lblGolesTorneo;
    @FXML
    private Label lblPuntosTorneo;
    
    private final ObservableList<Torneo> torneoLista = FXCollections.observableArrayList();
    private final TorneoRepository Torneorepo = new TorneoRepository();
    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();
    private Equipo equipoSeleccionado;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        equipoSeleccionado = (Equipo) AppContext.getInstance().get("EQUIPO_SELECCIONADO");

        if (equipoSeleccionado != null) {
            lblNombreEquipo.setText(equipoSeleccionado.getNombre());
//            lblTorneosGanados.setText(String.valueOf(equipoSeleccionado.getTorneosGanados()));
//            lblGolesTotales.setText(String.valueOf(equipoSeleccionado.getGolesTotales()));
//            lblPuntosTotales.setText(String.valueOf(equipoSeleccionado.getPuntosTotales()));

        try {
            cargarTorneosPorDeporte(equipoSeleccionado.getTipoDeporte());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        cmbTorneos.setOnAction(this::onActionCmbTorneos);
    }    }    

    @Override
    public void initialize() {
    }

    public void setEquipo(Equipo equipo) throws IOException {
        this.equipoSeleccionado = equipo;

        lblNombreEquipo.setText(equipo.getNombre());
        //Faltan por poner atributos
//        lblTorneosGanados.setText(String.valueOf(equipo.getTorneosGanados()));
//        lblGolesTotales.setText(String.valueOf(equipo.getGolesTotales()));
//        lblPuntosTotales.setText(String.valueOf(equipo.getPuntosTotales()));

        cargarTorneosPorDeporte(equipo.getTipoDeporte());
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

    if (torneo != null) {
//      int goles = equipoSeleccionado.getGolesPorTorneo(torneo.getId()); 
//      int puntos = equipoSeleccionado.getPuntosPorTorneo(torneo.getId());

//      lblGolesTorneo.setText(String.valueOf(goles));
        lblGolesTorneo.setText(String.valueOf(50)); //prueba
//      lblPuntosTorneo.setText(String.valueOf(puntos));
        lblPuntosTorneo.setText(String.valueOf(100)); //prueba
    }
    }
    
}
