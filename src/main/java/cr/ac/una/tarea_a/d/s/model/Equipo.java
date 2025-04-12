package cr.ac.una.tarea_a.d.s.model;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.nio.file.Files;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;

public class Equipo {

    private String id;
    private String nombre;
    private transient Image imagen; // No se serializa
    private String tipoDeporte;
    private String imagenBase64;

    public Equipo() {
    }

    // Constructor con imagen
    public Equipo(String id, String nombre, Image imagen, String tipoDeporte) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDeporte = tipoDeporte;
        setImagen(imagen); // Guarda la imagen y genera imagenBase64
    }

    // Constructor solo con nombre, útil para combos o búsqueda
    public Equipo(String nombre) {
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
        if (imagen != null && imagen.getUrl() != null && imagen.getUrl().startsWith("file:")) {
            try {
                File file = new File(new java.net.URI(imagen.getUrl()));
                byte[] bytes = Files.readAllBytes(file.toPath());
                this.imagenBase64 = Base64.getEncoder().encodeToString(bytes);
            } catch (Exception e) {
                System.err.println("Error al convertir imagen a base64 desde URL: " + imagen.getUrl());
                e.printStackTrace();
            }
        }
    }

    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public void setTipoDeporte(String tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
        cargarImagenDesdeBase64(); // Reconstruir imagen automáticamente
    }

    // Reconstruir imagen desde base64
    public void cargarImagenDesdeBase64() {
        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            try {
                byte[] bytes = Base64.getDecoder().decode(imagenBase64);
                this.imagen = new Image(new ByteArrayInputStream(bytes));
            } catch (IllegalArgumentException e) {
                System.err.println("Error decodificando imagenBase64.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Equipo{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", tipoDeporte='" + tipoDeporte + '\''
                + ", imagenCargada=" + (imagen != null)
                + '}';
    }
}
