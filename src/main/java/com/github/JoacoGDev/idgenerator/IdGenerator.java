package com.github.JoacoGDev.idgenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe Singleton ID Generator.
 * Generates unique sequential IDs starting from 0.
 *
 * @author JoacoGDev
 * @version 1.0.0
 */
public final class IdGenerator {

    private static IdGenerator instance;
    private Map<String, Integer> counters;
    private final PersistenceManager persistence;
    private static final String DEFAULT_PREFIX = "default";

    private IdGenerator() {
        this.counters = new HashMap<>();
        this.persistence = new PersistenceManager();
        loadState();
    }

    /**
     * Loads counters from persistence file.
     */
    private void loadState() {
        try {
            if (persistence.exists()) {
                this.counters = persistence.load();
                System.out.println("Loaded counters from file");
            } else {
                this.counters.put(DEFAULT_PREFIX, -1);
                System.out.println("Initialized new counters");
            }

        } catch (IOException error) {
            System.err.println("Could not load counters" + error.getMessage());
            this.counters.put(DEFAULT_PREFIX, -1);
        }
    }

    /**
     * Saves counters to persistence file.
     */
    private void saveState() {
        try {
            persistence.save(counters);
        } catch (IOException error) {
            System.err.println("Could not save counters" + error.getMessage());
        }
    }


    /**
     * Returns the single instance of IdGenerator.
     * Thread-safe implementation using double-checked locking.
     *
     * @return the singleton instance
     */
    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    /**
     * Generates and returns the next unique ID.
     * Uses default counter for backward compatibility with v1.0.
     *
     * @return the next sequential ID (0, 1, 2, ...)
     */
    public synchronized int nextId() {

        int currentValue = counters.getOrDefault(DEFAULT_PREFIX, -1);

        currentValue++;

        counters.put(DEFAULT_PREFIX, currentValue);

        saveState();

        return currentValue;
    }

    /**
     * Generates and returns a formatted ID with the specified prefix.
     * Format: PREFIX_XXXX (4 digits with zero-padding)
     *
     * @param prefix the prefix for the ID (e.g., "USER", "ORDER")
     * @return formatted ID string (e.g., "USER_0001")
     * @throws IllegalArgumentException if prefix is null or empty
     */

    public synchronized String nextId(String prefix) {

        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }

        int currentValue = counters.getOrDefault(prefix, -1);

        currentValue++;

        counters.put(prefix, currentValue);

        saveState();

        return String.format("%s_%04d", prefix, currentValue);
    }


    /**
     * Returns the current ID without generating a new one.
     * Uses default counter for backward compatibility.
     *
     * @return the current ID value, or -1 if no ID has been generated yet
     */
    public int getCurrentId() {
        return counters.getOrDefault(DEFAULT_PREFIX, -1);
    }

    /**
     * Returns the current ID for a specific prefix without generating a new one.
     *
     * @param prefix the prefix to check.
     * @return the current ID value, or -1 if no ID has been generated yet
     */
    public int getCurrentId(String prefix) {
        return counters.getOrDefault(prefix, -1);
    }


    /**
     * Resets the default counter to its initial state.
     * The next call to nextId() will return 0.
     */
    public synchronized void reset() {
        counters.put(DEFAULT_PREFIX, -1);
        saveState();
    }

    /**
     * Resets a specific counter to its initial state.
     * The next call to nextId(prefix) will return PREFIX_0000.
     *
     * @param prefix the prefix to reset
     */
    public synchronized void reset(String prefix){
        counters.put(prefix, -1);
        saveState();
    }

    /**
     * Clears all counters and sets default to itd initial state.
     * The next call to nextId(prefix) will return PREFIX_0000.
     */
    public synchronized void resetAll(){
        counters.clear();
        counters.put(DEFAULT_PREFIX, -1);
        saveState();
    }

    /**
     * Resets the singleton instance for testing purposes.
     * Package-private - only accessible within the same package (tests).
     * DO NOT USE IN PRODUCTION CODE.
     */
    static void resetInstanceForTesting() {
        instance = null;

    }
}
