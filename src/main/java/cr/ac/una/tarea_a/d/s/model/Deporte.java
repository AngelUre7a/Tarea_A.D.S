package cr.ac.una.tarea_a.d.s.model;

import java.io.File;
import javafx.scene.image.Image;
//import lombok.Data;
import java.util.Base64;
import java.nio.file.Files;
import java.io.ByteArrayInputStream;

//@Data 
public class Deporte {

    private String id;
    private String nombre;
    private transient Image imagen; // Nuevo atributo
    private String rutaImagen;
    private String tipo;   // Nuevo atributo
    private String imagenBase64;

    // Constructor completo
    public Deporte(String id, String nombre, Image imagen, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.tipo = tipo;
    }

    public Deporte(String id, String nombre, String rutaImagen, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.rutaImagen = rutaImagen;
        File file = new File(rutaImagen);
        if (file.exists()) {
            this.imagen = new Image(file.toURI().toString());
        } else {
            System.out.println("No se encontro la imagen en:" + rutaImagen);
            this.imagen = null;
        }
        this.tipo = tipo;
    }

    public Deporte(String nombre) {
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

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
        File file = new File(rutaImagen);
        if (file.exists()) {
            this.imagen = new Image(file.toURI().toString());
        } else {
            System.out.println("No se encontró la imagen en: " + rutaImagen);
            this.imagen = null;
        }
    }

    @Override
    public String toString() {
        return "Deporte{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", imagen='" + imagen + '\''
                + ", tipo='" + tipo + '\''
                + '}';
    }

    public void cargarImagenComoBase64(String ruta) {
        try {
            File file = new File(ruta);
            this.rutaImagen = ruta;
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                this.imagenBase64 = Base64.getEncoder().encodeToString(bytes);
                this.imagen = new Image(file.toURI().toString());
            } else {
                System.out.println("No se encontró la imagen en: " + ruta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarImagenDesdeBase64() {
        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            byte[] bytes = Base64.getDecoder().decode(imagenBase64);
            this.imagen = new Image(new ByteArrayInputStream(bytes));
        }
    }

}
