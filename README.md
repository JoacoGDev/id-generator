# ID Generator

A powerful, thread-safe ID generator library using the Singleton pattern with **persistent storage** and **prefix support**. Perfect for learning design patterns and building production-ready Java applications.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![Version](https://img.shields.io/badge/Version-2.0.0-blue.svg)](https://github.com/JoacoGDev/id-generator/releases)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## Features

- **Singleton Pattern** - Only one instance across your application
- **Thread-Safe** - Safe to use in multi-threaded environments
- **Persistent Storage** - IDs survive application restarts (NEW in V2.0)
- **Multiple Counters** - Independent sequences with prefixes (NEW in V2.0)
- **Formatted IDs** - Generate IDs like "USER_0001", "ORDER_0042" (NEW in V2.0)
- **Backward Compatible** - V1.0 code still works
- **Zero Dependencies** - Uses Java Properties for persistence
- **Well-Tested** - Comprehensive JUnit test suite
- **JavaDoc** - Fully documented API

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.JoacoGDev</groupId>
    <artifactId>id-generator</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Manual Installation

1. Download the JAR from [Releases](https://github.com/JoacoGDev/id-generator/releases)
2. Add it to your project's classpath

## Quick Start

### Basic Usage (V1.0 Compatible)

```java
import com.github.JoacoGDev.idgenerator.IdGenerator;

public class Example {
    public static void main(String[] args) {
        // Get the singleton instance
        IdGenerator generator = IdGenerator.getInstance();
        
        // Generate sequential IDs
        int id1 = generator.nextId();  // 0
        int id2 = generator.nextId();  // 1
        int id3 = generator.nextId();  // 2
        
        // Check current ID
        System.out.println("Current ID: " + generator.getCurrentId());  // 2
        
        // Reset counter
        generator.reset();
        int newId = generator.nextId();  // 0 (starts over)
    }
}
```

### Advanced Usage with Prefixes (V2.0)

```java
import com.github.JoacoGDev.idgenerator.IdGenerator;

public class AdvancedExample {
    public static void main(String[] args) {
        IdGenerator gen = IdGenerator.getInstance();
        
        // Generate IDs with prefixes
        String userId1 = gen.nextId("USER");      // USER_0000
        String userId2 = gen.nextId("USER");      // USER_0001
        String orderId1 = gen.nextId("ORDER");    // ORDER_0000
        String orderId2 = gen.nextId("ORDER");    // ORDER_0001
        
        // Each prefix has its own counter
        System.out.println(gen.getCurrentId("USER"));   // 1
        System.out.println(gen.getCurrentId("ORDER"));  // 1
        
        // Reset specific counter
        gen.reset("USER");
        String userId3 = gen.nextId("USER");      // USER_0000 (reset)
        
        // Restart application - IDs persist!
        // Next USER ID will be USER_0001 (continues from where it left off)
    }
}
```

### Persistence Example

```java
// First run
IdGenerator gen = IdGenerator.getInstance();
gen.nextId("ORDER");  // ORDER_0000
gen.nextId("ORDER");  // ORDER_0001
gen.nextId("ORDER");  // ORDER_0002
// Application closes... Counters saved to disk

// Second run (after restart)
IdGenerator gen = IdGenerator.getInstance();
gen.nextId("ORDER");  // ORDER_0003 - Continues from where it left off!
```

## API Documentation

### Core Methods

#### `getInstance()`
Returns the singleton instance of the ID generator.

```java
IdGenerator generator = IdGenerator.getInstance();
```

---

### V1.0 Methods (Backward Compatible)

#### `nextId()`
Generates and returns the next unique sequential ID.

```java
int id = generator.nextId();  // Returns 0, 1, 2, 3...
```

#### `getCurrentId()`
Returns the last generated ID without generating a new one.

```java
int current = generator.getCurrentId();  // Returns -1 if no ID generated yet
```

#### `reset()`
Resets the default counter to its initial state.

```java
generator.reset();
```

---

### V2.0 Methods (NEW)

#### `nextId(String prefix)`
Generates and returns a formatted ID with the specified prefix.

```java
String userId = generator.nextId("USER");       // "USER_0000"
String orderId = generator.nextId("ORDER");     // "ORDER_0000"
String productId = generator.nextId("PRODUCT"); // "PRODUCT_0000"
```

**Format:** `PREFIX_XXXX` (4 digits with zero-padding)

#### `getCurrentId(String prefix)`
Returns the last generated ID for a specific prefix.

```java
int currentUser = generator.getCurrentId("USER");
```

#### `reset(String prefix)`
Resets a specific counter to its initial state.

```java
generator.reset("USER");  // Only resets USER counter
```

#### `resetAll()`
Resets ALL counters (including default) to their initial state.

```java
generator.resetAll();
```

---

### Persistence (Automatic)

Counters are automatically saved to:
```
~/.idgenerator/counters.properties
```

- **Windows:** `C:/Users/YourName/.idgenerator/counters.properties`
- **Linux/Mac:** `/home/yourname/.idgenerator/counters.properties`

No configuration needed - it just works!

## Why Singleton?

The Singleton pattern is essential for an ID generator because:

- **Prevents duplicate IDs**: Multiple instances would generate conflicting IDs
- **Centralized state**: All parts of your application see the same counters
- **Thread-safe**: Synchronized methods ensure correct behavior in concurrent environments
- **Memory efficient**: Only one instance exists in the entire application
- **Persistent**: IDs survive application restarts (V2.0)

### Problem without Singleton:
```java
// ❌ BAD - Multiple instances = duplicate IDs
IdGenerator gen1 = new IdGenerator();  // If this were allowed
IdGenerator gen2 = new IdGenerator();
gen1.nextId();  // Returns 0
gen2.nextId();  // Returns 0 again! 💥 DUPLICATE!
```

### Solution with Singleton:
```java
// ✅ GOOD - Single instance = unique IDs
IdGenerator gen1 = IdGenerator.getInstance();
IdGenerator gen2 = IdGenerator.getInstance();
System.out.println(gen1 == gen2);  // true - same instance!
gen1.nextId();  // Returns 0
gen2.nextId();  // Returns 1 ✅ Unique!
```

### Why Persistence Matters (V2.0):

```java
// Day 1 - Generate some orders
gen.nextId("ORDER");  // ORDER_0000
gen.nextId("ORDER");  // ORDER_0001
// Application closes

// Day 2 - Application restarts
// WITH persistence (V2.0)
gen.nextId("ORDER");  // ORDER_0002 - Continues correctly!

// WITHOUT persistence (V1.0)  
gen.nextId("ORDER");  // ORDER_0000 - DUPLICATE!
```

## Building from Source

```bash
# Clone the repository
git clone https://github.com/JoacoGDev/id-generator.git
cd id-generator

# Build with Maven
mvn clean install

# Run tests
mvn test

# Generate JAR
mvn package
```

The JAR file will be in `target/id-generator-1.0.0.jar`

## Running Tests

```bash
mvn test
```

Test coverage includes:
- Singleton instance verification
- Sequential ID generation
- Initial state validation
- Reset functionality
- Shared state across references

## Project Structure

```
id-generator/
├── src/
│   ├── main/java/com/github/JoacoGDev/idgenerator/
│   │   ├── IdGenerator.java
│   │   └── PersistenceManager.java (NEW in V2.0)
│   └── test/java/com/github/JoacoGDev/idgenerator/
│       └── IdGeneratorTest.java
├── pom.xml
├── README.md
└── LICENSE
```

## Roadmap

### Version 1.0 (Released)
- Basic sequential ID generation
- Singleton pattern implementation
- Thread-safe operations
- Comprehensive tests

### Version 2.0 (Current)
- File persistence (IDs survive application restart)
- ID prefixes ("USER_0001", "ORDER_0002")
- Multiple independent ID sequences
- Backward compatible with V1.0

### Version 3.0 (Planned)
- Enhanced thread safety (double-checked locking)
- Configurable ID formats (hex, custom padding)
- Timestamp-based IDs
- Custom persistence backends

### Version 4.0 (Future)
- REST API with Spring Boot
- Web dashboard for monitoring
- Distributed ID generation
- Metrics and analytics

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**JoacoGDev**

- GitHub: [@JoacoGDev](https://github.com/JoacoGDev)

## Show your support

Give a star if this project helped you learn about design patterns!

## Changelog

### [2.0.0] - 2024-10-07
#### Added
- Persistent storage using Java Properties
- Multiple independent counters with prefix support
- Formatted IDs (e.g., "USER_0001")
- `nextId(String prefix)` method
- `getCurrentId(String prefix)` method
- `reset(String prefix)` method
- `resetAll()` method
- `PersistenceManager` class for file operations
- Automatic directory creation in user home

#### Changed
- Internal counter structure (single int → Map<String, Integer>)
- Improved thread safety
- Enhanced JavaDoc documentation

#### Maintained
- Full backward compatibility with V1.0
- All V1.0 methods work identically

### [1.0.0] - 2024-10-01
- Initial release
- Basic ID generation functionality
- Singleton pattern implementation
- Thread-safe operations
- JUnit test suite