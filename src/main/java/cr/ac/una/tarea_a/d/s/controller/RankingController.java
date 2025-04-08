/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RankingController extends Controller implements Initializable {

    @FXML
    private MFXTextField filterField;
    @FXML
    private TableView<Equipo> tableView;
    @FXML
    private ComboBox<Deporte> ComboBoxDeportes;
    @FXML
    private TableColumn<Equipo, Integer> colRango;
    @FXML
    private TableColumn<Equipo, String> colNombre;
    @FXML
    private TableColumn<Equipo, Image> colImagen;
    @FXML
    private TableColumn<Equipo, Integer> colPuntos;
    @FXML
    private TableColumn<Equipo, String> colDeporte;

    private final ObservableList<Equipo> equiposLista = FXCollections.observableArrayList();
    private final EquipoRepository Equiporepo = new EquipoRepository();
    @FXML
    private MFXButton btnActualizar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    

        
//        colRango.setCellValueFactory(new PropertyValueFactory<>("rango"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        colDeporte.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        
        colImagen.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    System.out.println("No se cargo la imagen del equipo");
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });
        
        FilteredList<Equipo> filteredData = new FilteredList<>(equiposLista, b -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(filteredData);
        });

        ComboBoxDeportes.valueProperty().addListener((obs, oldVal, newVal) -> {
            aplicarFiltro(filteredData);
        });
        
        try {
            equiposLista.addAll(Equiporepo.findAll());
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar datos", "No se pudieron cargar los deportes.");
        }

        SortedList<Equipo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
        
                List<Deporte> deportes = null;
        try {
            DeporteRepository deporteRepo = new DeporteRepository();
            deportes = deporteRepo.findAll();
            AppContext.getInstance().set("LISTA_DEPORTES", deportes);
        } catch (IOException e) {
            new Mensaje().show(Alert.AlertType.ERROR, "Error al cargar deportes", "No se pudo cargar la lista de deportes.");
            e.printStackTrace();
        }
        
        if (deportes != null) {
            Deporte todos = new Deporte("","","","");
            todos.setNombre("Todos");

            ComboBoxDeportes.getItems().add(todos);
            ComboBoxDeportes.getItems().addAll(deportes);
            ComboBoxDeportes.getSelectionModel().select(todos);
        }

        // Configura cómo mostrar los nombres
        ComboBoxDeportes.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });

        ComboBoxDeportes.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Deporte item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getNombre());
            }
        });
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnActualizar(ActionEvent event) {
        tableView.refresh();
    }
    
    private void aplicarFiltro(FilteredList<Equipo> filteredData) {
    filteredData.setPredicate(equipo -> {
        String filtroNombre = filterField.getText();
        Deporte filtroDeporte = ComboBoxDeportes.getSelectionModel().getSelectedItem();

        // Filtro por nombre
        boolean coincideNombre = (filtroNombre == null || filtroNombre.isEmpty()) || equipo.getNombre().toLowerCase().contains(filtroNombre.toLowerCase());

        // Filtro por deporte
        boolean coincideDeporte = (filtroDeporte == null || "Todos".equalsIgnoreCase(filtroDeporte.getNombre())) || equipo.getCategoria().equalsIgnoreCase(filtroDeporte.getNombre());
        
        return coincideNombre && coincideDeporte;
        });
    }
}
