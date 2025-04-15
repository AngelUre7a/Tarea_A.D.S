package cr.ac.una.tarea_a.d.s.repositories;
import cr.ac.una.tarea_a.d.s.model.Partida;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author dasly
 */
public interface IPartidaRepository {

    Partida save(Partida partida) throws IOException;

    List<Partida> findAll() throws IOException;

    Optional<Partida> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;

}