package com.github.JoacoGDev.idgenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PersistenceManager.
 */
class PersistenceManagerTest {

    private PersistenceManager persistence;
    private String testFilePath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        // Usar un directorio temporal para tests
        testFilePath = tempDir.resolve("test-counters.properties").toString();
        persistence = new PersistenceManager(testFilePath);
    }

    @AfterEach
    void tearDown() {
        // Limpiar archivo de test si existe
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Should save and load counters correctly")
    void testSaveAndLoad() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("default", 5);
        counters.put("USER", 10);
        counters.put("ORDER", 3);

        // ACT
        persistence.save(counters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(3, loaded.size(), "Should load 3 counters");
        assertEquals(5, loaded.get("default"), "Default counter should be 5");
        assertEquals(10, loaded.get("USER"), "USER counter should be 10");
        assertEquals(3, loaded.get("ORDER"), "ORDER counter should be 3");
    }

    @Test
    @DisplayName("Should return empty map when file does not exist")
    void testLoadNonExistentFile() throws IOException {
        // ACT
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertNotNull(loaded, "Loaded map should not be null");
        assertTrue(loaded.isEmpty(), "Loaded map should be empty");
    }

    @Test
    @DisplayName("Should handle empty counters map")
    void testSaveEmptyCounters() throws IOException {
        // ARRANGE
        Map<String, Integer> emptyCounters = new HashMap<>();

        // ACT
        persistence.save(emptyCounters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertNotNull(loaded, "Loaded map should not be null");
        assertTrue(loaded.isEmpty(), "Loaded map should be empty");
    }

    @Test
    @DisplayName("Should handle multiple counters with different values")
    void testMultipleCounters() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("PRODUCT", 100);
        counters.put("INVOICE", 500);
        counters.put("CUSTOMER", 25);
        counters.put("TRANSACTION", 1000);

        // ACT
        persistence.save(counters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(4, loaded.size());
        assertEquals(100, loaded.get("PRODUCT"));
        assertEquals(500, loaded.get("INVOICE"));
        assertEquals(25, loaded.get("CUSTOMER"));
        assertEquals(1000, loaded.get("TRANSACTION"));
    }

    @Test
    @DisplayName("Should overwrite existing file when saving")
    void testOverwriteExistingFile() throws IOException {
        // ARRANGE
        Map<String, Integer> firstCounters = new HashMap<>();
        firstCounters.put("USER", 5);

        Map<String, Integer> secondCounters = new HashMap<>();
        secondCounters.put("USER", 10);
        secondCounters.put("ORDER", 3);

        // ACT
        persistence.save(firstCounters);
        persistence.save(secondCounters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(2, loaded.size(), "Should have 2 counters");
        assertEquals(10, loaded.get("USER"), "USER should be updated to 10");
        assertEquals(3, loaded.get("ORDER"), "ORDER should be 3");
    }

    @Test
    @DisplayName("Should return false when file does not exist")
    void testExistsReturnsFalse() {
        // ACT & ASSERT
        assertFalse(persistence.exists(), "File should not exist initially");
    }

    @Test
    @DisplayName("Should return true after saving file")
    void testExistsReturnsTrue() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("default", 0);

        // ACT
        persistence.save(counters);

        // ASSERT
        assertTrue(persistence.exists(), "File should exist after saving");
    }

    @Test
    @DisplayName("Should handle negative counter values")
    void testNegativeValues() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("default", -1);
        counters.put("USER", -5);

        // ACT
        persistence.save(counters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(-1, loaded.get("default"));
        assertEquals(-5, loaded.get("USER"));
    }

    @Test
    @DisplayName("Should handle zero values")
    void testZeroValues() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("USER", 0);
        counters.put("ORDER", 0);

        // ACT
        persistence.save(counters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(0, loaded.get("USER"));
        assertEquals(0, loaded.get("ORDER"));
    }

    @Test
    @DisplayName("Should handle large counter values")
    void testLargeValues() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("USER", Integer.MAX_VALUE);
        counters.put("ORDER", 999999999);

        // ACT
        persistence.save(counters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(Integer.MAX_VALUE, loaded.get("USER"));
        assertEquals(999999999, loaded.get("ORDER"));
    }

    @Test
    @DisplayName("Should handle prefixes with special characters")
    void testPrefixesWithSpecialCharacters() throws IOException {
        // ARRANGE
        Map<String, Integer> counters = new HashMap<>();
        counters.put("USER_ADMIN", 5);
        counters.put("ORDER-2024", 10);
        counters.put("PRODUCT.V2", 3);

        // ACT
        persistence.save(counters);
        Map<String, Integer> loaded = persistence.load();

        // ASSERT
        assertEquals(5, loaded.get("USER_ADMIN"));
        assertEquals(10, loaded.get("ORDER-2024"));
        assertEquals(3, loaded.get("PRODUCT.V2"));
    }
}