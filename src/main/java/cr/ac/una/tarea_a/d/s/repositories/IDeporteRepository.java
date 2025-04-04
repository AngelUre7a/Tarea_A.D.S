/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.tarea_a.d.s.repositories;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author dasly
 */
public interface IDeporteRepository {
      
    Deporte save(Deporte deporte) throws IOException;

    List<Deporte> findAll() throws IOException;

    Optional<Deporte> findById(String id) throws IOException;

    boolean deleteById(String id) throws IOException;
}
