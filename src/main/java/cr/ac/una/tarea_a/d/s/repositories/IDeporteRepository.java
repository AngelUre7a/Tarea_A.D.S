package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface IDeporteRepository {
      
    Deporte save(Deporte deporte) throws IOException;

    List<Deporte> findAll() throws IOException;

    Optional<Deporte> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;
}
