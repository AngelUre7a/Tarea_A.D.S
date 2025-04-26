package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IEquipoRepository {
     Equipo save(Equipo equipo) throws IOException;

    List<Equipo> findAll() throws IOException;

    Optional<Equipo> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;
}
