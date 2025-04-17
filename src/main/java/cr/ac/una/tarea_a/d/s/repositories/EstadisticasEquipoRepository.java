package cr.ac.una.tarea_a.d.s.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tarea_a.d.s.model.EstadisticasEquipo;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstadisticasEquipoRepository implements IEstadisticasEquipoRepository {

    private static final String DEFAULT_FILE_NAME = "EstadisticasEquipo.json";
    private final String filePath;
    private final Gson gson;

    public EstadisticasEquipoRepository() {
        this.filePath = resolveFilePath();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        createFileIfNotExists();
    }

    private String resolveFilePath() {
        String dataDir = "src/main/java/cr/ac/una/tarea_a/d/s/dataJson";
        File directory = new File(dataDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return dataDir + File.separator + DEFAULT_FILE_NAME;
    }

    private void createFileIfNotExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(gson.toJson(new ArrayList<EstadisticasEquipo>()));
                }
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo: " + filePath, e);
            }
        }
    }

    @Override
    public EstadisticasEquipo save(EstadisticasEquipo estadistica) throws IOException {
        List<EstadisticasEquipo> estadisticas = findAll();

        // Eliminar solo si coincide equipo y torneo
        estadisticas.removeIf(e -> 
            e.getIdEquipo().equals(estadistica.getIdEquipo()) &&
            e.getIdTorneo().equals(estadistica.getIdTorneo())
        );

        estadisticas.add(estadistica);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(estadisticas, writer);
        }

        return estadistica;
}

    @Override
    public List<EstadisticasEquipo> findAll() throws IOException {
        File file = new File(filePath);
        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<EstadisticasEquipo>>() {}.getType();
            return gson.fromJson(reader, type);
        }
    }

    @Override
    public Optional<EstadisticasEquipo> findById(String idEquipo) throws IOException {
        List<EstadisticasEquipo> estadisticas = findAll();
        return estadisticas.stream()
                .filter(e -> e.getIdEquipo().equals(idEquipo))
                .findFirst();
    }

    @Override
    public boolean deleteById(String idEquipo) throws IOException {
        List<EstadisticasEquipo> estadisticas = findAll();
        boolean removed = estadisticas.removeIf(e -> e.getIdEquipo().equals(idEquipo));

        if (removed) {
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(estadisticas, writer);
            }
        }

        return removed;
    }
}
