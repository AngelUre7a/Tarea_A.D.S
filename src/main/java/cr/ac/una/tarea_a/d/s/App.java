package cr.ac.una.tarea_a.d.s;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.util.FlowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        
        FlowController.getInstance().InitializeFlow(stage,null);
        stage.getIcons().add(new Image("cr/ac/una/tarea_a/d/s/resources/Logo-Principal-View.png"));
        stage.setTitle("BALLIVERSE");
        FlowController.getInstance().goViewInWindow("PantallaInicial");
           LoadData data = new LoadData();
        data.load();
    }

   

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
//        Deporte deporte = new Deporte();
//        
        launch();
    }

}
