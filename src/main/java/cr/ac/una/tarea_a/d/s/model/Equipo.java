package cr.ac.una.tarea_a.d.s.model;

import java.io.File;
import javafx.scene.image.Image;

public class Equipo {

    private String id;
    private String nombre;
    private transient Image imagen;
    private String categoria; // Similar a "tipo" en la clase Deporte
    private String rutaImagen;
    private String imagenBase64;

    public Equipo(String id, String nombre, Image imagen, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public Equipo(String id, String nombre, String rutaImagen, String tipo) {
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

    public void cargarImagenComoBase64(String ruta) {
        try {
            File file = new File(ruta);
            this.rutaImagen = ruta;
            if (file.exists()) {
                byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                this.imagenBase64 = java.util.Base64.getEncoder().encodeToString(bytes);
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
            byte[] bytes = java.util.Base64.getDecoder().decode(imagenBase64);
            this.imagen = new Image(new java.io.ByteArrayInputStream(bytes));
        }
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

}
