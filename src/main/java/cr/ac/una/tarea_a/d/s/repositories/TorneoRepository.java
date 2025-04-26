package cr.ac.una.tarea_a.d.s.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tarea_a.d.s.model.Torneo;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TorneoRepository implements ITorneoRepository {
    private static final String DEFAULT_FILE_NAME = "Torneo.json";
    private final String filePath;
    private final Gson gson;

    public TorneoRepository() {
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
                    writer.write(gson.toJson(new ArrayList<Torneo>()));
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file: " + filePath, e);
            }
        }
    }

    //Guarda los torneos
    @Override
    public Torneo save(Torneo torneo) throws IOException {
        List<Torneo> torneos = findAll();

        if (torneo.getId() == null) {
            torneo.setId(UUID.randomUUID().toString());
        } else {
            torneos.removeIf(p -> p.getId().equals(torneo.getId()));
        }

        torneos.add(torneo);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(torneos, writer);
        }

        return torneo;
    }

    //Obtiene todos los datos del los torneos
    @Override
    public List<Torneo> findAll() throws IOException {
        File file = new File(filePath);
        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Torneo>>() {
            }.getType();
            return gson.fromJson(reader, type);
        }
    }

    //Busca torneos por id
    @Override
    public Optional<Torneo> findById(String id) throws IOException {
        List<Torneo> torneos = findAll();
        return torneos.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    //Borra torneos por id
    @Override
    public boolean deleteById(String id) throws IOException {
        List<Torneo> torneos = findAll();
        boolean removed = torneos.removeIf(torneo -> torneo.getId().equals(id));

        if (removed) {
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(torneos, writer);
            }
        }

        return removed;
    }

}
