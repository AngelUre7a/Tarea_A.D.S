package cr.ac.una.tarea_a.d.s.model;

public class EstadisticasEquipoPT {

    private String idTorneo;
    private String idEquipo;
    private int partidosGanadosPT;
    private int golesAFavorPT;
    private int puntosPT;
    private boolean esGanadorDT;

    public EstadisticasEquipoPT(String idEquipo, String idTorneo) {
        this.idEquipo = idEquipo;
        this.idTorneo = idTorneo;
        this.partidosGanadosPT = 0;
        this.golesAFavorPT = 0;
        this.puntosPT = 0;
        this.esGanadorDT = false;
    }

    public EstadisticasEquipoPT( String idEquipo, int torneosGanados, int partidosGanados, int golesAFavor, int puntos, int partidosGanadosPT, int golesAFavorPT, int puntosPT, boolean esGanadorDT) {
        this.idEquipo = idEquipo;
        this.partidosGanadosPT = partidosGanadosPT;
        this.golesAFavorPT = golesAFavorPT;
        this.puntosPT = puntosPT;
        this.esGanadorDT = esGanadorDT;
    }
 // Getters y Setters

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getPartidosGanadosPT() {
        return partidosGanadosPT;
    }

    public void setPartidosGanadosPT(int partidosGanadosPT) {
        this.partidosGanadosPT = partidosGanadosPT;
    }

    public int getGolesAFavorPT() {
        return golesAFavorPT;
    }

    public void setGolesAFavorPT(int golesAFavorPT) {
        this.golesAFavorPT = golesAFavorPT;
    }

    public int getPuntosPT() {
        return puntosPT;
    }

    public void setPuntosPT(int puntosPT) {
        this.puntosPT = puntosPT;
    }

    public boolean esGanadorDT() {
        return esGanadorDT;
    }

    public void setEsGanadorDT(boolean esGanadorDT) {
        this.esGanadorDT = esGanadorDT;
    }

    public void incrementarPartidosGanados(){
        this.partidosGanadosPT++;
    }
    
    public void incrementarPuntosGaneDirecto(){
        this.puntosPT = this.puntosPT + 3;
    }
    
    public void incrementarPuntosDesempate(){
        this.puntosPT = this.puntosPT + 2;
    }

    public String getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(String idTorneo) {
        this.idTorneo = idTorneo;
    }
    
}
