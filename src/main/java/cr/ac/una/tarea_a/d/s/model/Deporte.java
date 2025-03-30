/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    // Constructor sin parámetros
    public Deporte() {
    }

    // Constructor con parámetros
    public Deporte(String nombreDeporte, Image imagenDeporte, String tipoDeporte) {
        this.nombreDeporte = nombreDeporte;
        this.imagenDeporte = imagenDeporte;
        this.tipoDeporte = tipoDeporte;
    }

    // Métodos getter y setter
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
