/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        Deporte deporte1 = new Deporte(" ", "Futbol");
        this.repo.save(deporte1);
    }
}
