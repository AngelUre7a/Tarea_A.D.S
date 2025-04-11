package cr.ac.una.tarea_a.d.s;

import cr.ac.una.tarea_a.d.s.model.Deporte;
import cr.ac.una.tarea_a.d.s.repositories.DeporteRepository;

import cr.ac.una.tarea_a.d.s.model.Equipo;
import cr.ac.una.tarea_a.d.s.repositories.EquipoRepository;

import cr.ac.una.tarea_a.d.s.model.Torneo;
import cr.ac.una.tarea_a.d.s.repositories.TorneoRepository;

import java.io.IOException;


public class LoadData {
    private DeporteRepository deporteRepo = new DeporteRepository();
    private EquipoRepository equipoRepo = new EquipoRepository();

    private TorneoRepository torneoRepo = new TorneoRepository();

    public void load() throws IOException {
      
    }
}
