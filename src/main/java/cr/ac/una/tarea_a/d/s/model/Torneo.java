package cr.ac.una.tarea_a.d.s.model;

import java.util.ArrayList;
import java.util.List;

public class Torneo {

    private String id;
    private String nombre;
    private String tipoDeporte;
    private int cantidadEquipos;
    private int tiempoPorPartida; // en minutos
    private List<Equipo> equiposInscritos;
    private List<Partida> partidas; // 🔥 lista de partidas
    private String estado; // "pendiente", "enCurso", "finalizado"

    // ✅ Constructor principal SIN partidas (se inicializa vacía)
    public Torneo(String id, String nombre, String tipoDeporte, int cantidadEquipos, int tiempoPorPartida, List<Equipo> equiposInscritos) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDeporte = tipoDeporte;
        this.cantidadEquipos = cantidadEquipos;
        this.tiempoPorPartida = tiempoPorPartida;
        this.equiposInscritos = equiposInscritos;
        this.partidas = new ArrayList<>();
        this.estado = "pendiente";
    }

    // (Opcional) Constructor completo con partidas si lo ocupás en el futuro
    public Torneo(String id, String nombre, String tipoDeporte, int cantidadEquipos, int tiempoPorPartida, List<Equipo> equiposInscritos, List<Partida> partidas) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDeporte = tipoDeporte;
        this.cantidadEquipos = cantidadEquipos;
        this.tiempoPorPartida = tiempoPorPartida;
        this.equiposInscritos = equiposInscritos;
        this.partidas = partidas;
        this.estado = "pendiente";
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

    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public void setTipoDeporte(String tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }

    public int getCantidadEquipos() {
        return cantidadEquipos;
    }

    public void setCantidadEquipos(int cantidadEquipos) {
        this.cantidadEquipos = cantidadEquipos;
    }

    public int getTiempoPorPartida() {
        return tiempoPorPartida;
    }

    public void setTiempoPorPartida(int tiempoPorPartida) {
        this.tiempoPorPartida = tiempoPorPartida;
    }

    public List<Equipo> getEquiposInscritos() {
        return equiposInscritos;
    }

    public void setEquiposInscritos(List<Equipo> equiposInscritos) {
        this.equiposInscritos = equiposInscritos;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Torneo{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoDeporte='" + tipoDeporte + '\'' +
                ", cantidadEquipos=" + cantidadEquipos +
                ", tiempoPorPartida=" + tiempoPorPartida +
                ", estado='" + estado + '\'' +
                '}';
    }
}
