package cr.ac.una.tarea_a.d.s.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipoGenerales;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EstadisticasEquipoGeneralesRepository implements IEstadisticasEquipoGeneralesRepository {

    private static final String DEFAULT_FILE_NAME = "EstadisticasEquipoGenerales.json";
    private final String filePath;
    private final Gson gson;

    public EstadisticasEquipoGeneralesRepository() {
        this.filePath = resolveFilePath();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        createFileIfNotExists();
    }

    //Resuelve la ruta del json
    private String resolveFilePath() {
        String dataDir = "src/main/java/cr/ac/una/tarea_a/d/s/dataJson";
        File directory = new File(dataDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return dataDir + File.separator + DEFAULT_FILE_NAME;
    }

    //Crea el json si no existe 
    private void createFileIfNotExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(gson.toJson(new ArrayList<EstadisticasEquipoGenerales>()));
                }
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo: " + filePath, e);
            }
        }
    }

    //Guarda las estadisticas generales por equipo y si ya estaban entonces se actualizan
    @Override
    public EstadisticasEquipoGenerales save(EstadisticasEquipoGenerales estadistica) throws IOException {
        List<EstadisticasEquipoGenerales> lista = findAll();
        lista.removeIf(e -> e.getIdEquipo().equals(estadistica.getIdEquipo()));
        lista.add(estadistica);
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(lista, writer);
        }
        return estadistica;
    }

    //Obtiene todos los datos del json
    @Override
    public List<EstadisticasEquipoGenerales> findAll() throws IOException {
        File file = new File(filePath);
        if (file.length() == 0) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<EstadisticasEquipoGenerales>>() {}.getType();
            return gson.fromJson(reader, type);
        }
    }

    //Busca estadisticas por equipo generales por ID
    @Override
    public Optional<EstadisticasEquipoGenerales> findById(String idEquipo) throws IOException {
        return findAll().stream()
                .filter(e -> e.getIdEquipo().equals(idEquipo))
                .findFirst();
    }

    //Borra estadisticas por equipo generales por ID
    @Override
    public boolean deleteById(String idEquipo) throws IOException {
        List<EstadisticasEquipoGenerales> lista = findAll();
        boolean removed = lista.removeIf(e -> e.getIdEquipo().equals(idEquipo));
        if (removed) {
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(lista, writer);
            }
        }
        return removed;
    }

    //Actualiza las estadisticas generales de un equipo, las elimina primero y luego agrega las nuevas
    @Override
    public void update(EstadisticasEquipoGenerales estadistica) throws IOException {
        List<EstadisticasEquipoGenerales> lista = findAll();
        lista.removeIf(e -> e.getIdEquipo().equals(estadistica.getIdEquipo()));
        lista.add(estadistica);
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(lista, writer);
        }
    }
}
