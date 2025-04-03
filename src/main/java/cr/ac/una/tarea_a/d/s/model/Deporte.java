package cr.ac.una.tarea_a.d.s.model;
import javafx.scene.image.Image;
//import lombok.Data;

/**
 *
 * @author dasly
 */

//@Data 
public class Deporte {
    private String nombreDeporte;
    private Image imagenDeporte;
    private String tipoDeporte;

    public Deporte() {
    }

    public Deporte(String nombreDeporte, Image imagenDeporte, String tipoDeporte) {
        this.nombreDeporte = nombreDeporte;
        this.imagenDeporte = imagenDeporte;
        this.tipoDeporte = tipoDeporte;
    }
    
    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public void setNombreDeporte(String nombreDeporte) {
        this.nombreDeporte = nombreDeporte;
    }

    public Image getImagenDeporte() {
        return imagenDeporte;
    }

    public void setImagenDeporte(Image imagenDeporte) {
        this.imagenDeporte = imagenDeporte;
    }

    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public void setTipoDeporte(String tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }

}
