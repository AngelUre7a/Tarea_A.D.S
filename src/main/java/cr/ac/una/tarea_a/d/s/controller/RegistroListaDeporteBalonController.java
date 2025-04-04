package cr.ac.una.tarea_a.d.s.controller;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.util.AppContext;

import cr.ac.una.tarea_a.d.s.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistroListaDeporteBalonController extends Controller implements Initializable {

//    private Deporte deporte = new Deporte();
//     
    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnAgregar;
    @FXML
    private MFXTextField filterField;
    @FXML
    private TableView<Deporte> tableView;
    @FXML
    private TableColumn<Deporte, String> colID;
    @FXML
    private TableColumn<Deporte, String> colNombre;
    @FXML
    private TableColumn<Deporte, String> colImagen;
    @FXML
    private TableColumn<Deporte, String> colEditar;
    @FXML
    private TableColumn<Deporte, String> colEliminar;

    private final ObservableList<Deporte> deportesLista = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        colEditar.setCellValueFactory(new PropertyValueFactory<>(""));
        colEliminar.setCellValueFactory(new PropertyValueFactory<>(""));

        Deporte dep1 = new Deporte("1", "Futbol","","");
        Deporte dep2 = new Deporte("2", "Golf","","");
        Deporte dep3 = new Deporte("3", "Bask","","");

        deportesLista.addAll(dep1, dep2, dep3);

        FilteredList<Deporte> filteredData = new FilteredList<>(deportesLista, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Deporte -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (Deporte.getNombre().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }else
                    return false;
            });
        });

        SortedList<Deporte> sortedData = new SortedList<>(filteredData);
        
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        
        tableView.setItems(sortedData);
//      if(AppContext.getInstance().containsItem("DEPORTE")){
//      deporte = (Deporte) AppContext.getInstance().get("DEPORTE");
//      lbNombreD.setText(deporte.getNombreDeporte());
//      imgImagenD.setImage(deporte.getImagenDeporte());
//      }
        // TODO
    }    
    @Override
    public void initialize() {
//      if(AppContext.getInstance().containsItem("DEPORTE")){
//      deporte = (Deporte) AppContext.getInstance().get("DEPORTE");
//      lbNombreD.setText(deporte.getNombreDeporte());
//      imgImagenD.setImage(deporte.getImagenDeporte());
//      }
    }
    
    @FXML
    private void onActionBtnAgregar(ActionEvent event) throws IOException {
        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
    }

    private void onActionBtnEditar(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("RegistroDeporte", ((Stage) root.getScene().getWindow()), false);
    }


}
