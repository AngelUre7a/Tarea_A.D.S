package cr.ac.una.tarea_a.d.s.model;

public class EstadisticasEquipo {

    private String idEquipo;
    private int torneosGanados;
    private int partidosGanados;
    private int golesAFavor;
    private int puntos;
    private int partidosGanadosPT;
    private int golesAFavorPT;
    private int puntosPT;
    private boolean esGanadorDT;

    public EstadisticasEquipo() {
    }

    public EstadisticasEquipo(String idEquipo, int torneosGanados, int partidosGanados,
        int golesAFavor, int puntos, int partidosGanadosPT, int golesAFavorPT, int puntosPT, boolean esGanadorDT) {
        this.idEquipo = idEquipo;
        this.torneosGanados = torneosGanados;
        this.partidosGanados = partidosGanados;
        this.golesAFavor = golesAFavor;
        this.puntos = puntos;
        this.partidosGanadosPT = partidosGanadosPT;
        this.golesAFavorPT = golesAFavorPT;
        this.puntosPT = puntosPT;
        this.esGanadorDT = esGanadorDT;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getTorneosGanados() {
        return torneosGanados;
    }

    public void setTorneosGanados(int torneosGanados) {
        this.torneosGanados = torneosGanados;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public void setGolesAFavor(int golesAFavor) {
        this.golesAFavor = golesAFavor;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
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

    
}
