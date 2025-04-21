package cr.ac.una.tarea_a.d.s.model;

/**
 *
 * @author dasly
 */
public class Partida {

    private String id;
    private String idTorneo;
    private String idEquipoA;
    private String idEquipoB;
    private int golesEquipoA;
    private int golesEquipoB;
    private EstadoPartida estado; // "pendiente", "enJuego", "finalizado"
    private String ganadorId;
    private int tiempoRestante;

    public Partida() {
    }

    public Partida(String id, String idTorneo, String idEquipoA, String idEquipoB, int golesEquipoA, int golesEquipoB, EstadoPartida estado, String ganadorId, int tiempoRestante) {
        this.id = id;
        this.idTorneo = idTorneo;
        this.idEquipoA = idEquipoA;
        this.idEquipoB = idEquipoB;
        this.golesEquipoA = golesEquipoA;
        this.golesEquipoB = golesEquipoB;
        this.estado = estado;
        this.ganadorId = ganadorId;
        this.tiempoRestante = tiempoRestante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(String idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getIdEquipoA() {
        return idEquipoA;
    }

    public void setIdEquipoA(String idEquipoA) {
        this.idEquipoA = idEquipoA;
    }

    public String getIdEquipoB() {
        return idEquipoB;
    }

    public void setIdEquipoB(String idEquipoB) {
        this.idEquipoB = idEquipoB;
    }

    public int getGolesEquipoA() {
        return golesEquipoA;
    }

    public void setGolesEquipoA(int golesEquipoA) {
        this.golesEquipoA = golesEquipoA;
    }

    public int getGolesEquipoB() {
        return golesEquipoB;
    }

    public void setGolesEquipoB(int golesEquipoB) {
        this.golesEquipoB = golesEquipoB;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public String getGanadorId() {
        return ganadorId;
    }

    public void setGanadorId(String ganadorId) {
        this.ganadorId = ganadorId;
    }
    
    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
}
