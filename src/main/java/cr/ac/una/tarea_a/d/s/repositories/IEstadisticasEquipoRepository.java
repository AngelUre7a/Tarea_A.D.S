package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IEstadisticasEquipoRepository {
    
    EstadisticasEquipo save(EstadisticasEquipo estadistica) throws IOException;

    List<EstadisticasEquipo> findAll() throws IOException;

    Optional<EstadisticasEquipo> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;
}
