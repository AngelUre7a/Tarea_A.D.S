package cr.ac.una.tarea_a.d.s.model;
import javafx.scene.image.Image;
//import lombok.Data;

/**
 *
 * @author dasly
 */

//@Data 
public class Deporte {
     
    private String id;
    private String nombre;
    private Image imagen; // Nuevo atributo
    private String tipo;   // Nuevo atributo

    // Constructor completo
    public Deporte(String id, String nombre, Image imagen, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.tipo = tipo;
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

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Deporte{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", imagen='" + imagen + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
    //esto sirve en el json
//    
//    private String id;
//    private String nombre;
//    private String imagen; // Nuevo atributo
//    private String tipo;   // Nuevo atributo
//
//    // Constructor completo
//    public Deporte(String id, String nombre, String imagen, String tipo) {
//        this.id = id;
//        this.nombre = nombre;
//        this.imagen = imagen;
//        this.tipo = tipo;
//    }
//
//    // Getters y Setters
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
//
//    public String getImagen() {
//        return imagen;
//    }
//
//    public void setImagen(String imagen) {
//        this.imagen = imagen;
//    }
//
//    public String getTipo() {
//        return tipo;
//    }
//
//    public void setTipo(String tipo) {
//        this.tipo = tipo;
//    }
//
//    @Override
//    public String toString() {
//        return "Deporte{" +
//                "id='" + id + '\'' +
//                ", nombre='" + nombre + '\'' +
//                ", imagen='" + imagen + '\'' +
//                ", tipo='" + tipo + '\'' +
//                '}';
//    }

}
