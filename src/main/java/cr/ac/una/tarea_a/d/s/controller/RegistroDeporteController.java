package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.util.AppContext;
import cr.ac.una.tarea_a.d.s.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegistroDeporteController extends Controller implements Initializable {

    @FXML
    private MFXButton btnRegistrar;
    @FXML
    private MFXTextField txtNombreDeporte;
    @FXML
    private MFXButton btnCargarImagen;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Limpiar los campos de texto y la imagen
        txtNombreDeporte.clear();
        imageView.setImage(null);

        // Verificar si hay un deporte para editar
        if (AppContext.getInstance().containsItem("DEPORTE_EDITAR")) {
            Deporte deporte = (Deporte) AppContext.getInstance().get("DEPORTE_EDITAR");
            txtNombreDeporte.setText(deporte.getNombre());  // Cargar nombre
            imageView.setImage(deporte.getImagen());         // Cargar imagen
        }
        DragAndDropForImageView();  // Restablecer la funcionalidad de drag-and-drop
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnRegistrar(ActionEvent event) throws IOException {
           String nombre = txtNombreDeporte.getText();
    Image imagen = imageView.getImage();

    if (nombre == null || nombre.isBlank() || imagen == null) {
        new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar un nombre y una imagen.");
        return;
    }

    // Crear el nuevo deporte o actualizar el existente
    String id = java.util.UUID.randomUUID().toString(); // Genera un ID único si es nuevo
    Deporte deporte;

    if (AppContext.getInstance().containsItem("DEPORTE_EDITAR")) {
        // Si es un deporte que se está editando, actualiza el objeto
        deporte = (Deporte) AppContext.getInstance().get("DEPORTE_EDITAR");
        deporte.setNombre(nombre);
        deporte.setImagen(imagen);
        AppContext.getInstance().delete("DEPORTE_EDITAR");  // Limpiar el contexto
    } else {
        // Si es un deporte nuevo, crea un objeto nuevo
        deporte = new Deporte(id, nombre, imagen, "balon");
    }

    // Guardar el nuevo deporte en el AppContext
    AppContext.getInstance().set("DEPORTE_NUEVO", deporte);

    // Añadir el deporte a la lista global de deportes
    if (!AppContext.getInstance().containsItem("LISTA_DEPORTES")) {
        // Si la lista no existe, crea una nueva y la guarda en AppContext
        ObservableList<Deporte> listaDeportes = FXCollections.observableArrayList();
        AppContext.getInstance().set("LISTA_DEPORTES", listaDeportes);
    }

    // Recupera la lista y agrega el deporte registrado
    ObservableList<Deporte> listaDeportes = (ObservableList<Deporte>) AppContext.getInstance().get("LISTA_DEPORTES");
    listaDeportes.add(deporte);

    // Mostrar mensaje de éxito
    new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "Deporte guardado correctamente");

    // Limpiar los campos antes de cerrar la ventana
    txtNombreDeporte.clear();
    imageView.setImage(null);

    // Cerrar la ventana actual
    ((Stage) root.getScene().getWindow()).close();
//        String nombre = txtNombreDeporte.getText();
//        Image imagen = imageView.getImage();
//
//        if (nombre == null || nombre.isBlank() || imagen == null) {
//            new Mensaje().show(Alert.AlertType.WARNING, "BALLIVERSE", "Debe ingresar un nombre y una imagen.");
//            return;
//        }
//
//        // Crear el nuevo deporte o actualizar el existente
//        String id = java.util.UUID.randomUUID().toString(); // Genera un ID único si es nuevo
//        Deporte deporte;
//
//        if (AppContext.getInstance().containsItem("DEPORTE_EDITAR")) {
//            // Si es un deporte que se está editando, actualiza el objeto
//            deporte = (Deporte) AppContext.getInstance().get("DEPORTE_EDITAR");
//            deporte.setNombre(nombre);
//            deporte.setImagen(imagen);
//            AppContext.getInstance().delete("DEPORTE_EDITAR");  // Limpiar el contexto
//        } else {
//            // Si es un deporte nuevo, crea un objeto nuevo
//            deporte = new Deporte(id, nombre, imagen, "balon");
//        }
//
//        // Guardarlo en el AppContext
//        AppContext.getInstance().set("DEPORTE_NUEVO", deporte);
//
//        new Mensaje().show(Alert.AlertType.INFORMATION, "BALLIVERSE", "Deporte guardado correctamente");
//
//        // Limpiar los campos antes de cerrar la ventana
//        txtNombreDeporte.clear();
//        imageView.setImage(null);
//
//        // Cerrar la ventana actual
//        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void onActionBtnCargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            Image imagen = new Image(archivoSeleccionado.toURI().toString());

            imageView.setImage(imagen);
        }
    }

    private void DragAndDropForImageView() {
        imageView.setOnDragOver(event -> {
            if (event.getGestureSource() != imageView && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        imageView.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File archivoSeleccionado = db.getFiles().get(0);
                if (archivoSeleccionado != null) {
                    try {
                        Image imagen = new Image(archivoSeleccionado.toURI().toString());
                        imageView.setImage(imagen);
                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();

        });

    }

}
