package cr.ac.una.tarea_a.d.s.model;

/**
 *
 * @author dasly
 */
public class Enfrentamiento {
    private Long id;
    private Long partidaId;
    private Long ganadorId; // id del equipo ganador
    private String fase;    // Ej: "Cuartos", "Semifinal", "Final"

    public Enfrentamiento() {}

    public Enfrentamiento(Long id, Long partidaId, Long ganadorId, String fase) {
        this.id = id;
        this.partidaId = partidaId;
        this.ganadorId = ganadorId;
        this.fase = fase;
    }

    // Getters y Setters
}
