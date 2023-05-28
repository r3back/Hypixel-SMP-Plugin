package me.reb4ck.smp.persist;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * Implementations that Handles Data Serialization
 */
public interface Persist {
    /**
     * Get File by name
     *
     * @param name File Name
     * @return File
     */
    File getFile(String name);

    /**
     * Get File by Class
     *
     * @param clazz Class
     * @return File by class
     */
    File getFile(Class<?> clazz);

    /**
     * Get File by instance
     *
     * @param instance Object instance
     * @return File by object
     */
    File getFile(Object instance);

    /**
     * Save Object
     *
     * @param instance Object Instance
     */
    void save(Object instance, PersistType persistType);

    /**
     * Save Object by file
     *
     * @param instance Object Instance
     * @param file File
     */
    void save(Object instance, File file, PersistType persistType);

    /**
     * Converts an object to a String
     *
     * @param instance Object to be converted
     * @return Object Serialized
     */
    String toString(Object instance, PersistType persistType);

    /**
     * Load a class
     *
     * @param clazz Class
     * @param <T>   Generic
     * @return T
     */
    <T> T load(Class<T> clazz, PersistType persistType);

    /**
     * Load a class
     *
     * @param clazz Class
     * @param file  File
     * @param <T>   Generic
     * @return T
     */
    <T> T load(Class<T> clazz, File file, PersistType persistType);

    /**
     * Load a class
     *
     * @param clazz Class
     * @param content Content
     * @param <T> Generic
     * @return T
     */
    <T> T load(Class<T> clazz, String content, PersistType persistType);

    @AllArgsConstructor
    @Getter
    enum PersistType{
        YAML(".yml", new YAMLFactory()),
        JSON(".json", new JsonFactory());

        private final String extension;
        private final JsonFactory factory;
    }
}
