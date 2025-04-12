package cr.ac.una.tarea_a.d.s.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class AppContext {

    private static AppContext INSTANCE = null;
    private final HashMap<String, Object> context = new HashMap<>();

    private AppContext() {
        //cargarPropiedades();
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AppContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppContext();
                }
            }
        }
    }

    public static AppContext getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    private void cargarPropiedades() {
        try {
            FileInputStream configFile = new FileInputStream("config/properties.ini");
            Properties appProperties = new Properties();
            appProperties.load(configFile);
            configFile.close();

            // Ejemplo para cargar propiedades si fueran necesarias
            // if (appProperties.getProperty("propiedades.rutalog") != null) {
            //     this.set("rutalog", appProperties.getProperty("propiedades.rutalog"));
            // }

        } catch (IOException io) {
            System.out.println("Archivo de configuración no encontrado.");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Object get(String parameter) {
        return context.get(parameter);
    }

    public void set(String nombre, Object valor) {
        context.put(nombre, valor);
    }

    public void delete(String parameter) {
        context.remove(parameter);
    }

    public boolean containsItem(String name) {
        return context.containsKey(name);
    }

    public boolean hasValue(String key) {
        return context.containsKey(key) && context.get(key) != null;
    }



    public HashMap<String, Object> getData() {
        return context;
    }
}
