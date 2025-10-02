package com.github.JoacoGDev.idgenerator;

/**
 * Thread-safe Singleton ID Generator.
 * Generates unique sequential IDs starting from 0.
 *
 * @author JoacoGDev
 * @version 1.0.0
 */
public final class IdGenerator {

    private static IdGenerator instance;
    private int counter = -1;

    private IdGenerator(){}

    /**
     * Returns the single instance of IdGenerator.
     * Thread-safe implementation using double-checked locking.
     *
     * @return the singleton instance
     */
    public static synchronized IdGenerator getInstance(){
        if(instance == null){
            instance = new IdGenerator();
        }
        return instance;
    }

    /**
     * Generates and returns the next unique ID.
     *
     * @return the next sequential ID (0, 1, 2, ...)
     */
    public synchronized int nextId() {
        return ++counter;
    }

    /**
     * Returns the current ID without generating a new one.
     *
     * @return the current ID value, or -1 if no ID has been generated yet
     */
    public int getCurrentId() {
        return counter;
    }

    /**
     * Resets the counter to its initial state.
     * The next call to nextId() will return 0.
     */
    public synchronized void reset(){
        counter = -1;
    }
}
