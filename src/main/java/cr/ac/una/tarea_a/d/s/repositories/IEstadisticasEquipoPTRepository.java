package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoPT;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IEstadisticasEquipoPTRepository {
    
    EstadisticasEquipoPT save(EstadisticasEquipoPT estadistica) throws IOException;

    List<EstadisticasEquipoPT> findAll() throws IOException;

    Optional<EstadisticasEquipoPT> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;
}
