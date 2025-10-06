package com.github.JoacoGDev.idgenerator;

import java.io.*;
import java.util.*;



/**
 * Manages persistence of ID counters to a Properties file.
 * Stores counters in user's home directory
 */
public class PersistenceManager {

    private final String filePath;


    /**
     * Creates a new PersistenceManager
     * File location: user.home/.idgenerator/counters.properties
     */

    public PersistenceManager() {
        String userHome = System.getProperty("user.home");

        this.filePath = userHome + File.separator + ".idGenerator" +
                File.separator + "counters.properties";
    }

    /**
     * Checks if the persistence file exists
     *
     * @return true if file exists, false otherwise
     */
    public boolean exists() {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Creates the .idgenerator if it doesn't exist.
     *
     * @throws IOException if directory cannot be created
     */
    private void ensureDirectoryExists() throws IOException {
        File directory = new File(System.getProperty("user.home") +
                File.separator + ".idgenerator");

        if (!directory.exists()) {
            throw new IOException("Could not create directory: " + directory);
        }
    }


    /**
     * Loads counters from properties file.
     *
     * @return Map of counter names to their values
     * @throws IOException if file cannot be read
     */
    public Map<String, Integer> load() throws IOException {
        Map<String, Integer> counters = new HashMap<>();

        //Return empty Map if file doesn't exists
        if (!exists()) {
            return counters;
        }

        //Reads file using Properties
        Properties properties = new Properties();
        //This line autocloses the resource. PREVENTS RESOURCE LEAKS
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }

        //Transforms Properties into a Map<String, Integer>
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            counters.put(key, Integer.parseInt(value));
        }

        return counters;
    }


    public void save(Map<String, Integer> counters) throws IOException {

        ensureDirectoryExists();

        Properties properties = new Properties();
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue().toString());
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            properties.store(fos, "Id Generator Counters - DO NOT EDIT MANUALLY");
        }
    }
}
