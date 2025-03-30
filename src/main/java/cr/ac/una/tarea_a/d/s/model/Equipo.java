/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.model;
import javafx.scene.image.Image;
/**
 *
 * @author dasly
 */public class Equipo {
    private String nombreEquipo;
    private Image imagenEquipo;

    // Constructor sin parámetros
    public Equipo() {
    }

    // Constructor con parámetros
    public Equipo(String nombreEquipo, Image imagenEquipo) {
        this.nombreEquipo = nombreEquipo;
        this.imagenEquipo = imagenEquipo;
    }

    // Métodos getter y setter
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public Image getImagenEquipo() {
        return imagenEquipo;
    }

    public void setImagenEquipo(Image imagenEquipo) {
        this.imagenEquipo = imagenEquipo;
    }
}
