package com.github.JoacoGDev.idgenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IdGenerator (V1.0 + V2.0 tests).
 */
class IdGeneratorTest {

    @BeforeEach
    void setUp() {
        // Reset singleton before each test
        IdGenerator.resetInstanceForTesting();
    }

    @AfterEach
    void tearDown() {
        // Clean up persistence file after each test
        String userHome = System.getProperty("user.home");
        File file = new File(userHome + File.separator + ".idgenerator" +
                File.separator + "counters.properties");
        if (file.exists()) {
            file.delete();
        }
    }

    // ========================================
    // V1.0 TESTS (Backward Compatibility)
    // ========================================

    @Test
    @DisplayName("Should return the same instance (Singleton)")
    void testSingletonInstance() {
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
        assertEquals(1, id2, "Second ID should be 1");
        assertEquals(2, id3, "Third ID should be 2");
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
    void testReset() {
        IdGenerator gen = IdGenerator.getInstance();

        gen.nextId();
        gen.nextId();
        gen.nextId();
        gen.nextId();

        gen.reset();

        int currentId = gen.getCurrentId();
        int nextId = gen.nextId();

        assertEquals(-1, currentId, "Current ID should be -1 after reset");
        assertEquals(0, nextId, "Next ID should be 0 after reset");
    }

    @Test
    @DisplayName("Should maintain state across multiple references")
    void testSharedState() {
        IdGenerator gen1 = IdGenerator.getInstance();
        IdGenerator gen2 = IdGenerator.getInstance();

        int idGen1 = gen1.nextId();
        int idGen2 = gen2.getCurrentId();

        assertEquals(idGen1, idGen2, "gen1 and gen2 should see the same current ID");
    }



    /**
     * V2.0 TESTS
     */



    @Test
    @DisplayName("Should generate IDs with prefix in correct format")
    void testNextIdWithPrefix() {
        IdGenerator gen = IdGenerator.getInstance();

        String userId1 = gen.nextId("USER");
        String userId2 = gen.nextId("USER");
        String userId3 = gen.nextId("USER");

        assertEquals("USER_0000", userId1, "First USER ID should be USER_0000");
        assertEquals("USER_0001", userId2, "Second USER ID should be USER_0001");
        assertEquals("USER_0002", userId3, "Third USER ID should be USER_0002");
    }

    @Test
    @DisplayName("Should maintain independent counters for different prefixes")
    void testMultipleCounters() {
        IdGenerator gen = IdGenerator.getInstance();

        String user1 = gen.nextId("USER");      // USER_0000
        String order1 = gen.nextId("ORDER");    // ORDER_0000
        String user2 = gen.nextId("USER");      // USER_0001
        String order2 = gen.nextId("ORDER");    // ORDER_0001
        String product1 = gen.nextId("PRODUCT");// PRODUCT_0000

        assertEquals("USER_0000", user1);
        assertEquals("ORDER_0000", order1);
        assertEquals("USER_0001", user2);
        assertEquals("ORDER_0001", order2);
        assertEquals("PRODUCT_0000", product1);
    }

    @Test
    @DisplayName("Should format IDs with 4-digit zero padding")
    void testPrefixFormat() {
        IdGenerator gen = IdGenerator.getInstance();

        // Generate enough IDs to test padding
        for (int i = 0; i < 10; i++) {
            gen.nextId("TEST");
        }

        String id = gen.nextId("TEST");
        assertEquals("TEST_0010", id, "ID should have 4-digit padding");
    }

    @Test
    @DisplayName("Should return current ID for specific prefix")
    void testGetCurrentIdWithPrefix() {
        IdGenerator gen = IdGenerator.getInstance();

        gen.nextId("USER");  // 0
        gen.nextId("USER");  // 1
        gen.nextId("ORDER"); // 0

        assertEquals(1, gen.getCurrentId("USER"), "USER current should be 1");
        assertEquals(0, gen.getCurrentId("ORDER"), "ORDER current should be 0");
        assertEquals(-1, gen.getCurrentId("PRODUCT"), "Non-existent prefix should return -1");
    }

    @Test
    @DisplayName("Should reset only specific counter")
    void testResetSpecificCounter() {
        IdGenerator gen = IdGenerator.getInstance();

        gen.nextId("USER");   // USER_0000
        gen.nextId("USER");   // USER_0001
        gen.nextId("ORDER");  // ORDER_0000
        gen.nextId("ORDER");  // ORDER_0001

        gen.reset("USER");

        assertEquals(-1, gen.getCurrentId("USER"), "USER should be reset to -1");
        assertEquals(1, gen.getCurrentId("ORDER"), "ORDER should remain at 1");

        String nextUser = gen.nextId("USER");
        String nextOrder = gen.nextId("ORDER");

        assertEquals("USER_0000", nextUser, "USER should start from 0 again");
        assertEquals("ORDER_0002", nextOrder, "ORDER should continue from 2");
    }

    @Test
    @DisplayName("Should reset all counters")
    void testResetAll() {
        IdGenerator gen = IdGenerator.getInstance();

        gen.nextId();         // default: 0
        gen.nextId("USER");   // USER_0000
        gen.nextId("ORDER");  // ORDER_0000

        gen.resetAll();

        assertEquals(-1, gen.getCurrentId(), "Default should be reset");
        assertEquals(-1, gen.getCurrentId("USER"), "USER should be reset");
        assertEquals(-1, gen.getCurrentId("ORDER"), "ORDER should be reset");
    }

    @Test
    @DisplayName("Should persist counters across instance resets")
    void testPersistenceSimulation() {
        // Simulate first run
        IdGenerator gen1 = IdGenerator.getInstance();
        gen1.nextId("USER");   // USER_0000
        gen1.nextId("USER");   // USER_0001
        gen1.nextId("ORDER");  // ORDER_0000

        // Simulate application restart
        IdGenerator.resetInstanceForTesting();

        // Simulate second run
        IdGenerator gen2 = IdGenerator.getInstance();

        // Should load from file
        assertEquals(1, gen2.getCurrentId("USER"), "USER counter should persist");
        assertEquals(0, gen2.getCurrentId("ORDER"), "ORDER counter should persist");

        // Next IDs should continue from persisted values
        String nextUser = gen2.nextId("USER");
        String nextOrder = gen2.nextId("ORDER");

        assertEquals("USER_0002", nextUser, "Should continue from persisted value");
        assertEquals("ORDER_0001", nextOrder, "Should continue from persisted value");
    }

    @Test
    @DisplayName("Should maintain backward compatibility with V1.0")
    void testBackwardCompatibility() {
        IdGenerator gen = IdGenerator.getInstance();

        // V1.0 methods should still work
        int id1 = gen.nextId();
        int id2 = gen.nextId();

        assertEquals(0, id1);
        assertEquals(1, id2);

        gen.reset();
        int id3 = gen.nextId();

        assertEquals(0, id3, "Reset should work with V1.0 methods");

        // V2.0 should not interfere with V1.0
        gen.nextId("USER");  // USER_0000
        int id4 = gen.nextId();

        assertEquals(1, id4, "V1.0 counter should be independent");
    }

    @Test
    @DisplayName("Should throw exception for null or empty prefix")
    void testInvalidPrefix() {
        IdGenerator gen = IdGenerator.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            gen.nextId(null);
        }, "Should throw exception for null prefix");

        assertThrows(IllegalArgumentException.class, () -> {
            gen.nextId("");
        }, "Should throw exception for empty prefix");

        assertThrows(IllegalArgumentException.class, () -> {
            gen.nextId("   ");
        }, "Should throw exception for whitespace-only prefix");
    }

    @Test
    @DisplayName("Should handle large counter values with correct padding")
    void testLargeCounterValues() {
        IdGenerator gen = IdGenerator.getInstance();

        // Generate 9999 IDs
        for (int i = 0; i < 9999; i++) {
            gen.nextId("TEST");
        }

        String id = gen.nextId("TEST");
        assertEquals("TEST_9999", id, "Should handle 4-digit max correctly");

        // Next one goes to 5 digits (overflow)
        String overflow = gen.nextId("TEST");
        assertEquals("TEST_10000", overflow, "Should handle overflow correctly");
    }
}