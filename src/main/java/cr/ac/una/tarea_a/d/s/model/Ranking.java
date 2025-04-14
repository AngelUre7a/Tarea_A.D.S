package cr.ac.una.tarea_a.d.s.model;

/**
 *
 * @author dasly
 */
import java.util.List;

public class Ranking {
    private Long torneoId;
    private Long equipoId;
    private int goles;
    private int posicion;
    private List<String> resultados; // Ej: ["vs X: gane 2-1", "vs Y: desempate 3-2"]

    public Ranking() {}

    public Ranking(Long torneoId, Long equipoId, int goles, int posicion, List<String> resultados) {
        this.torneoId = torneoId;
        this.equipoId = equipoId;
        this.goles = goles;
        this.posicion = posicion;
        this.resultados = resultados;
    }

    // Getters y Setters
}
