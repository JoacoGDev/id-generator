package com.github.JoacoGDev.idgenerator;

import java.io.*;
import java.util.*;
import java.nio.file.*;



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

        public PersistenceManager(){
            String userHome = System.getProperty("user.home");

            this.filePath = userHome + File.pathSeparator + ".idGenerator" +
                            File.separator + "counters.properties";
        }

        /**
         * Checks if the persistence file exists
         * @return true if file exists, false otherwise
         */
        public boolean exists() {
            File file = new File(filePath);
            return file.exists();
        }

        /**
         * Loads counters from properties file.
         * @return Map of counter names to their values
         * @throws IOException if file cannot be read
         */
        public Map<String, Integer> load() throws IOException{
            //
        return;
        }
    }
