package cr.ac.una.tarea_a.d.s;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;
import java.io.IOException;

/**
 *
 * @author dasly
 */
public class LoadData {
    private DeporteRepository deporteRepo = new DeporteRepository();
    private EquipoRepository equipoRepo = new EquipoRepository();

    public void load() throws IOException {
        // Cargar deportes
        Deporte deporte1 = new Deporte(null, "Futbol", "FOND.png", "Colectivo");
        deporteRepo.save(deporte1);
        
//        Deporte deporte2 = new Deporte(null, "Tenis", "imagenes/tenis.png", "Individual");
//        deporteRepo.save(deporte2);

        // Cargar equipos
//        Equipo equipo1 = new Equipo(null, "Real Madrid", "imagenes/realmadrid.png", "Futbol");
//        equipoRepo.save(equipo1);
//
//        Equipo equipo2 = new Equipo(null, "Los Lakers", "imagenes/lakers.png", "Baloncesto");
//        equipoRepo.save(equipo2);
    }
    
   

}
