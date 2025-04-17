package cr.ac.una.tarea_a.d.s.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.tarea_a.d.s.model.Equipo;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author dasly
 */
public class EquipoRepository implements IEquipoRepository {

    private static final String DEFAULT_FILE_NAME = "Equipo.json";
    private final String filePath;
    private final Gson gson;

    public EquipoRepository() {
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
                    writer.write(gson.toJson(new ArrayList<Equipo>()));
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file: " + filePath, e);
            }
        }
    }

    @Override
    public Equipo save(Equipo equipo) throws IOException {
        List<Equipo> deportes = findAll();

        if (equipo.getId() == null) {
            // Generate ID for new products
            equipo.setId(UUID.randomUUID().toString());
        } else {
            // Remove existing product if updating
            deportes.removeIf(p -> p.getId().equals(equipo.getId()));
        }

        deportes.add(equipo);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(deportes, writer);
        }

        return equipo;
    }

    @Override
    public List<Equipo> findAll() throws IOException {
        File file = new File(filePath);
        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Equipo>>() {
            }.getType();
            return gson.fromJson(reader, type);
        }
    }

    @Override
    public Optional<Equipo> findById(String id) throws IOException {
        List<Equipo> deportes = findAll();
        return deportes.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        List<Equipo> equipos = findAll();

        // Verificar si el equipo existe
        Optional<Equipo> equipoOptional = equipos.stream()
                .filter(equipo -> equipo.getId().equals(id))
                .findFirst();

        if (equipoOptional.isPresent()) {
            Equipo equipo = equipoOptional.get();

            // Verificar si el equipo está en un torneo
            if (equipo.isEnTorneo()) {
                throw new IllegalArgumentException("El equipo no puede ser eliminado porque está participando en un torneo.");
            }

            boolean removed = equipos.removeIf(e -> e.getId().equals(id));

            if (removed) {
                try (FileWriter writer = new FileWriter(filePath)) {
                    gson.toJson(equipos, writer);
                }
            }

            return removed;
        } else {
            throw new IllegalArgumentException("Equipo no encontrado.");
        }
    }

    public void update(Equipo equipo) throws IOException {
        List<Equipo> equipos = findAll();
        equipos.removeIf(e -> e.getId().equals(equipo.getId())); // Eliminar el equipo anterior
        equipos.add(equipo); // Agregar el equipo actualizado

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(equipos, writer);
        }
    }
}
