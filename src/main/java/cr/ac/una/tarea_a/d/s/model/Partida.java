package cr.ac.una.tarea_a.d.s.model;

/**
 *
 * @author dasly
 */
public class Partida {
    private Long id;
    private Long torneoId;
    private Long equipo1Id;
    private Long equipo2Id;
    private int puntosPrimerEquipo;
    private int puntosSegundoEquipo;
    private String duracionPartida;
    
    private Long ganadorId;  // Reemplaza lógica de Enfrentamiento
    private String fase;     // Ej: "Cuartos", "Semifinal", "Final"

    public Partida() {}

    public Partida(Long id, Long torneoId, Long equipo1Id, Long equipo2Id,
                   int puntosPrimerEquipo, int puntosSegundoEquipo,
                   String duracionPartida, Long ganadorId, String fase) {
        this.id = id;
        this.torneoId = torneoId;
        this.equipo1Id = equipo1Id;
        this.equipo2Id = equipo2Id;
        this.puntosPrimerEquipo = puntosPrimerEquipo;
        this.puntosSegundoEquipo = puntosSegundoEquipo;
        this.duracionPartida = duracionPartida;
        this.ganadorId = ganadorId;
        this.fase = fase;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }

    public Long getEquipo1Id() { return equipo1Id; }
    public void setEquipo1Id(Long equipo1Id) { this.equipo1Id = equipo1Id; }

    public Long getEquipo2Id() { return equipo2Id; }
    public void setEquipo2Id(Long equipo2Id) { this.equipo2Id = equipo2Id; }

    public int getPuntosPrimerEquipo() { return puntosPrimerEquipo; }
    public void setPuntosPrimerEquipo(int puntosPrimerEquipo) { this.puntosPrimerEquipo = puntosPrimerEquipo; }

    public int getPuntosSegundoEquipo() { return puntosSegundoEquipo; }
    public void setPuntosSegundoEquipo(int puntosSegundoEquipo) { this.puntosSegundoEquipo = puntosSegundoEquipo; }

    public String getDuracionPartida() { return duracionPartida; }
    public void setDuracionPartida(String duracionPartida) { this.duracionPartida = duracionPartida; }

    public Long getGanadorId() { return ganadorId; }
    public void setGanadorId(Long ganadorId) { this.ganadorId = ganadorId; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
}