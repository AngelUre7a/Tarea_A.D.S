package cr.ac.una.tarea_a.d.s.model;
import javafx.scene.image.Image;
//import lombok.Data;

/**
 *
 * @author dasly
 */

//@Data 
public class Deporte {
      // Atributos
    private String id;
    private String nombre;

    // Constructor
    public Deporte(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Deporte{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
//    private String nombreDeporte;
//    private Image imagenDeporte;
//    private String tipoDeporte;
//
//    public Deporte() {
//    }
//
//    public Deporte(String nombreDeporte, Image imagenDeporte, String tipoDeporte) {
//        this.nombreDeporte = nombreDeporte;
//        this.imagenDeporte = imagenDeporte;
//        this.tipoDeporte = tipoDeporte;
//    }
//    
//    public String getNombreDeporte() {
//        return nombreDeporte;
//    }
//
//    public void setNombreDeporte(String nombreDeporte) {
//        this.nombreDeporte = nombreDeporte;
//    }
//
//    public Image getImagenDeporte() {
//        return imagenDeporte;
//    }
//
//    public void setImagenDeporte(Image imagenDeporte) {
//        this.imagenDeporte = imagenDeporte;
//    }
//
//    public String getTipoDeporte() {
//        return tipoDeporte;
//    }
//
//    public void setTipoDeporte(String tipoDeporte) {
//        this.tipoDeporte = tipoDeporte;
//    }

}
