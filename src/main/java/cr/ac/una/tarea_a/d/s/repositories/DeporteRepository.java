package cr.ac.una.tarea_a.d.s.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tarea_a.d.s.model.Deporte;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class  DeporteRepository implements IDeporteRepository  {
    private static final String DEFAULT_FILE_NAME = "Deporte.json";
    private final String filePath;
    private final Gson gson;

    public DeporteRepository() {
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
                    writer.write(gson.toJson(new ArrayList<Deporte>()));
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file: " + filePath, e);
            }
        }
    }

    @Override
    public Deporte save(Deporte deporte) throws IOException {
        List<Deporte> deportes = findAll();

        if (deporte.getId() == null) {
           
            deporte.setId(UUID.randomUUID().toString());
        } else {
      
            deportes.removeIf(p -> p.getId().equals(deporte.getId()));
        }

        deportes.add(deporte);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(deportes, writer);
        }

        return deporte;
    }

    @Override
    public List<Deporte> findAll() throws IOException {
        File file = new File(filePath);
        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Deporte>>() {
            }.getType();
            return gson.fromJson(reader, type);
        }
    }

    @Override
    public Optional<Deporte> findById(String id) throws IOException {
        List<Deporte> deportes = findAll();
        return deportes.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        List<Deporte> deportes = findAll();
        boolean removed = deportes.removeIf(deporte -> deporte.getId().equals(id));

        if (removed) {
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(deportes, writer);
            }
        }

        return removed;
    }
    public void update(Deporte deporte) throws IOException {
        List<Deporte> deportes = findAll();
        deportes.removeIf(e -> e.getId().equals(deporte.getId())); // Eliminar el equipo anterior
        deportes.add(deporte); // Agregar el equipo actualizado

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(deportes, writer);
        }
    }

}
