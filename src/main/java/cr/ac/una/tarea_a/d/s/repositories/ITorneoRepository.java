package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.Torneo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author dasly
 */
public interface ITorneoRepository {

    Torneo save(Torneo torneo) throws IOException;

    List<Torneo> findAll() throws IOException;

    Optional<Torneo> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;
}
