package cr.ac.una.tarea_a.d.s.model;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.nio.file.Files;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;

public class Deporte {

    private String id;
    private String nombre;
    private transient Image imagen;
    private String imagenBase64;
    private int torneosInscritos = 0;

    public Deporte(String id, String nombre, Image imagen) {
        this.id = id;
        this.nombre = nombre;
        setImagen(imagen); 
    }

    public Deporte(String nombre) {
        this.nombre = nombre;
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

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
        cargarImagenDesdeBase64(); 
    }

    public int getCantidadTorneosInscritos() {
        return torneosInscritos;
    }

    public void addTorneoInscrito() {
        torneosInscritos++;
    }

    public void deleteTorneoInscrito() {
        torneosInscritos--;
    }

    public void cargarImagenDesdeArchivo(String ruta) {
        try {
            File file = new File(ruta);
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                this.imagenBase64 = Base64.getEncoder().encodeToString(bytes);
                this.imagen = new Image(file.toURI().toString());
            } else {
                System.err.println("No se encontró la imagen en: " + ruta);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo la imagen desde: " + ruta);
            e.printStackTrace();
        }
    }

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
        return "Deporte{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", imagenCargada=" + (imagen != null)
                + '}';
    }
}
