/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PartidoController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label lbTiempo;
    @FXML
    private Label lbEquipo1;
    @FXML
    private Label lbMarcador1;
    @FXML
    private Label lbMarcador2;
    @FXML
    private Label lbEquipo2;
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
    private VBox fondoImgCancha;
    
    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    private final DeporteRepository deporteRepo = new DeporteRepository();
    String nombreDeporte = (String) AppContext.getInstance().get("DEPORTE");
    @FXML
    private ImageView ivEscudo1;
    @FXML
    private ImageView ivEscudo2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgCancha.fitHeightProperty().bind(fondoImgCancha.heightProperty());
        imgCancha.fitWidthProperty().bind(fondoImgCancha.widthProperty());
        cargarJson();
        cargarDatosPartido();
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnFinalizar(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
    
    private void cargarJson() {
    try {
        deportesLista.clear();  // Limpiar la lista antes de agregar nuevos datos
        for (Deporte d : deporteRepo.findAll()) {
            d.cargarImagenDesdeBase64();  // Asumiendo que cada deporte tiene su imagen en Base64
            deportesLista.add(d);  // Agregar el deporte a la lista observable
        }
        // Ahora deportesLista tiene todos los deportes cargados desde el JSON
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
        Equipo equipo1 = (Equipo) AppContext.getInstance().get("EQUIPO1");
        Equipo equipo2 = (Equipo) AppContext.getInstance().get("EQUIPO2");
        String nombreDeporte = (String) AppContext.getInstance().get("DEPORTE");
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

    // Mostrar datos de los equipos
        equipo1.cargarImagenDesdeBase64();
        equipo2.cargarImagenDesdeBase64();
         if (equipo1 != null && equipo2 != null) {
            lbEquipo1.setText(equipo1.getNombre());
            lbEquipo2.setText(equipo2.getNombre());

             if (equipo1.getImagen() != null) {
                imgEquipo1.setImage(equipo1.getImagen());
            }
            if (equipo2.getImagen() != null) {
                imgEquipo2.setImage(equipo2.getImagen());
            }
        }
    }
}
