package cr.ac.una.tarea_a.d.s;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;
import java.io.IOException;

/**
 *
 * @author dasly
 */
public class LoadData {
       private DeporteRepository repo = new DeporteRepository();

    public void load() throws IOException {
//        Deporte deporte1 = new Deporte(" ", "Futbol");
//        this.repo.save(deporte1);
  Deporte deporte1 = new Deporte(null, "Futbol", "imagenes/futbol.png", "Colectivo");
        repo.save(deporte1);
        
        Deporte deporte2 = new Deporte(null, "Tenis", "imagenes/tenis.png", "Individual");
        repo.save(deporte2);
    }
}
