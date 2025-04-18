package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoGenerales;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IEstadisticasEquipoGeneralesRepository {
    EstadisticasEquipoGenerales save(EstadisticasEquipoGenerales estadistica) throws IOException;
    List<EstadisticasEquipoGenerales> findAll() throws IOException;
    Optional<EstadisticasEquipoGenerales> findById(String idEquipo) throws IOException;
    boolean deleteById(String idEquipo) throws IOException;
    void update(EstadisticasEquipoGenerales estadistica) throws IOException;
}
