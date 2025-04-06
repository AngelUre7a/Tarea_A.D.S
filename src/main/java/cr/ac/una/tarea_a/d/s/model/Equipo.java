package cr.ac.una.tarea_a.d.s.model;
import javafx.scene.image.Image;

public class Equipo {
    
    private String id;
    private String nombre;
    private Image imagen;
    private String categoria; // Similar a "tipo" en la clase Deporte

    public Equipo(String id, String nombre, Image imagen, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
    }

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Equipo{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", imagen='" + imagen + '\''
                + ", categoria='" + categoria + '\''
                + '}';
    }
//
//    private String id;
//    private String nombre;
//    private String imagen;
//    private String categoria; // Similar a "tipo" en la clase Deporte
//
//    public Equipo(String id, String nombre, String imagen, String categoria) {
//        this.id = id;
//        this.nombre = nombre;
//        this.imagen = imagen;
//        this.categoria = categoria;
//    }
//
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
//    public String getCategoria() {
//        return categoria;
//    }
//
//    public void setCategoria(String categoria) {
//        this.categoria = categoria;
//    }
//
//    @Override
//    public String toString() {
//        return "Equipo{"
//                + "id='" + id + '\''
//                + ", nombre='" + nombre + '\''
//                + ", imagen='" + imagen + '\''
//                + ", categoria='" + categoria + '\''
//                + '}';
//    }
}
