
package cr.ac.una.tarea_a.d.s.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tarea_a.d.s.model.Partida;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class PartidaRepository implements IPartidaRepository {
    private static final String DEFAULT_FILE_NAME = "Partida.json";
    private final String filePath;
    private final Gson gson;

    public PartidaRepository() {
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
                    writer.write(gson.toJson(new ArrayList<Partida>()));
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file: " + filePath, e);
            }
        }
    }

    //Guarda las partidas
    @Override
    public Partida save(Partida partida) throws IOException {
        List<Partida> partidas = findAll();

        if (partida.getId() == null) {
            partida.setId(UUID.randomUUID().toString());
        } else {
            partidas.removeIf(p -> p.getId().equals(partida.getId()));
        }

        partidas.add(partida);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(partidas, writer);
        }

        return partida;
    }

    //Obtiene todas las partidas
    @Override
    public List<Partida> findAll() throws IOException {
        File file = new File(filePath);
        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Partida>>() {}.getType();
            return gson.fromJson(reader, type);
        }
    }

    //Busca las partidas por id
    @Override
    public Optional<Partida> findById(String id) throws IOException {
        List<Partida> partidas = findAll();
        return partidas.stream()
                .filter(partida -> partida.getId().equals(id))
                .findFirst();
    }

    //Borra las partidas por ID
    @Override
    public boolean deleteById(String id) throws IOException {
        List<Partida> partidas = findAll();
        boolean removed = partidas.removeIf(partida -> partida.getId().equals(id));

        if (removed) {
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(partidas, writer);
            }
        }

        return removed;
    }
}
