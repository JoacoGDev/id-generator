package com.github.JoacoGDev.idgenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test class for IdGenerator.
 */
class IdGeneratorTest {

    @BeforeEach
    void setUp(){
        // Executes before every test
        // Resets the generator
        IdGenerator.getInstance().reset();
    }

    @Test
    @DisplayName("Should return the same instance (Singleton)")
    void testingSingletonInstance(){

        IdGenerator gen1 = IdGenerator.getInstance();
        IdGenerator gen2 = IdGenerator.getInstance();

        assertSame(gen1, gen2, "gen1 and gen2 should be the same instance");
    }

    @Test
    @DisplayName("Should generate sequential IDs starting from 0")
    void testSequentialIds() {

        IdGenerator gen = IdGenerator.getInstance();

        int id1 = gen.nextId();
        int id2 = gen.nextId();
        int id3 = gen.nextId();

        assertEquals(0, id1, "First ID should be 0");
        assertEquals(1, id2, "First ID should be 1");
        assertEquals(2, id3, "First ID should be 2");
    }

    @Test
    @DisplayName("Should return -1 as initial current ID")
    void testInitialCurrentId() {

        IdGenerator gen = IdGenerator.getInstance();

        int currentId = gen.getCurrentId();

        assertEquals(-1, currentId, "Initial current ID should be -1");
    }

    @Test
    @DisplayName("Should reset counter to initial state")
    void testReset(){

        IdGenerator gen = IdGenerator.getInstance();

        int id1 = gen.nextId();
        int id2 = gen.nextId();
        int id3 = gen.nextId();
        int id4 = gen.nextId();

        gen.reset();

        int currentId = gen.getCurrentId();
        int nextId = gen.nextId();

        assertEquals(-1, currentId, "Current ID should be -1");
        assertEquals(0, nextId, "Next ID should be 0");
    }

    @Test
    @DisplayName("Should maintain state across multiple references")
    void testSharedState(){

        IdGenerator gen1 = IdGenerator.getInstance();
        IdGenerator gen2 = IdGenerator.getInstance();

        int idGen1 = gen1.nextId();
        int idGen2 = gen2.getCurrentId();

        assertEquals(idGen1, idGen2, "idGen1 must be the same as idGen2");
    }




}
