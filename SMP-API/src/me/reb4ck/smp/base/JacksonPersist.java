package me.reb4ck.smp.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.reb4ck.smp.persist.Persist;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public final class JacksonPersist implements Persist {
    private final Map<PersistType, ObjectMapper> objectMapper;
    private final PersistType persistType;
    private final File file;

    @Inject
    public JacksonPersist(@Named("pluginFolder") File file) {
        this.file = file;
        this.objectMapper = ImmutableMap.<PersistType, ObjectMapper>builder()
                .put(PersistType.JSON, getObjectMapper(PersistType.JSON))
                .put(PersistType.YAML, getObjectMapper(PersistType.YAML))
                .build();

        this.persistType = PersistType.YAML;
    }

    private ObjectMapper getObjectMapper(PersistType persistType){
        JsonFactory jsonFactory = persistType.getFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        return mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
    }

    private static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object instance) {
        return getName(instance.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    @Override
    public File getFile(String name) {
        return new File(file, name + persistType.getExtension());
    }

    @Override
    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    @Override
    public File getFile(Object instance) {
        return getFile(getName(instance));
    }

    @Override
    public void save(Object instance, PersistType persistType) {
        save(instance, getFile(instance), persistType);
    }

    @Override
    public void save(Object instance, File file, PersistType persistType) {
        try {
            objectMapper.get(persistType).writeValue(file, instance);
        } catch (IOException e) {
            System.out.println("Failed to save " + file.toString() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString(Object instance, PersistType persistType) {
        try {
            return objectMapper.get(persistType).writeValueAsString(instance);
        } catch (IOException e) {
            System.out.println("Failed to save " + instance.toString() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public <T> T load(Class<T> clazz, PersistType persistType) {
        return load(clazz, getFile(clazz), persistType);
    }

    @Override
    public <T> T load(Class<T> clazz, File file, PersistType persistType) {
        if (file.exists()) {
            try {
                return objectMapper.get(persistType).readValue(file, clazz);
            } catch (IOException e) {
                System.out.println("Failed to parse " + file + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T load(Class<T> clazz, String content, PersistType persistType) {
        try {
            return objectMapper.get(persistType).readValue(content, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
