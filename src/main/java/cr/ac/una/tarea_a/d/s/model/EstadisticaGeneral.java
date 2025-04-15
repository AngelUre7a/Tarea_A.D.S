package cr.ac.una.tarea_a.d.s.model;

public class EstadisticaGeneral {
    private String idEquipo;
    private int partidosGanados;
    private int partidosPerdidos;
    private int golesAFavor;
    private int golesEnContra;
    private int puntos;

    public EstadisticaGeneral(String idEquipo) {
        this.idEquipo = idEquipo;
        this.partidosGanados = 0;
        this.partidosPerdidos = 0;
        this.golesAFavor = 0;
        this.golesEnContra = 0;
        this.puntos = 0;
    }

    public void actualizarDesdePartida(Partida partida) {
        boolean esEquipo1 = partida.getEquipo1Id().toString().equals(idEquipo);
        boolean esEquipo2 = partida.getEquipo2Id().toString().equals(idEquipo);

        if (esEquipo1 || esEquipo2) {
            int puntosPropios = esEquipo1 ? partida.getPuntosPrimerEquipo() : partida.getPuntosSegundoEquipo();
            int puntosRival = esEquipo1 ? partida.getPuntosSegundoEquipo() : partida.getPuntosPrimerEquipo();

            golesAFavor += puntosPropios;
            golesEnContra += puntosRival;

            boolean fueEmpate = partida.getPuntosPrimerEquipo() == partida.getPuntosSegundoEquipo();
            boolean gano = partida.getGanadorId() != null && partida.getGanadorId().toString().equals(idEquipo);

            if (gano) {
                partidosGanados++;
                puntos += fueEmpate ? 2 : 3;  // 2 si fue por desempate, 3 si ganó directo
            } else {
                partidosPerdidos++;
            }
        }
    }

    public void reiniciar() {
        partidosGanados = 0;
        partidosPerdidos = 0;
        golesAFavor = 0;
        golesEnContra = 0;
        puntos = 0;
    }

    // Getters y Setters

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public int getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public void setPartidosPerdidos(int partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public void setGolesAFavor(int golesAFavor) {
        this.golesAFavor = golesAFavor;
    }

    public int getGolesEnContra() {
        return golesEnContra;
    }

    public void setGolesEnContra(int golesEnContra) {
        this.golesEnContra = golesEnContra;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
