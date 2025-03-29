/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.model;

/**
 *
 * @author dasly
 */
public class Equipo {
     private String nombreEquipo;
    private String ciudad;
    private String entrenador;

    // Constructor sin parámetros
    public Equipo() {
    }

    // Constructor con parámetros
    public Equipo(String nombreEquipo, String ciudad, String entrenador) {
        this.nombreEquipo = nombreEquipo;
        this.ciudad = ciudad;
        this.entrenador = entrenador;
    }

    // Métodos getter y setter
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(String entrenador) {
        this.entrenador = entrenador;
    }
}
