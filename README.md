# ID Generator

A simple, thread-safe ID generator library using the Singleton pattern. Perfect for learning design patterns and building reliable Java applications.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## Features

- **Singleton Pattern** - Only one instance across your application
- **Thread-Safe** - Safe to use in multi-threaded environments
- **Sequential IDs** - Generates predictable IDs starting from 0
- **Lightweight** - Zero external dependencies
- **Well-Tested** - Comprehensive JUnit test suite
- **JavaDoc** - Fully documented API

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.JoacoGDev</groupId>
    <artifactId>id-generator</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Manual Installation

1. Download the JAR from [Releases](https://github.com/JoacoGDev/id-generator/releases)
2. Add it to your project's classpath

## Quick Start

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

## API Documentation

### `getInstance()`
Returns the singleton instance of the ID generator.

```java
IdGenerator generator = IdGenerator.getInstance();
```

### `nextId()`
Generates and returns the next unique sequential ID.

```java
int id = generator.nextId();  // Returns 0, 1, 2, 3...
```

### `getCurrentId()`
Returns the last generated ID without generating a new one.

```java
int current = generator.getCurrentId();  // Returns -1 if no ID generated yet
```

### `reset()`
Resets the counter to its initial state. The next `nextId()` call will return 0.

```java
generator.reset();
```

## Why Singleton?

The Singleton pattern is essential for an ID generator because:

- **Prevents duplicate IDs**: Multiple instances would generate conflicting IDs
- **Centralized state**: All parts of your application see the same counter
- **Thread-safe**: Synchronized methods ensure correct behavior in concurrent environments
- **Memory efficient**: Only one instance exists in the entire application

### Problem without Singleton:
```java
// BAD - Multiple instances = duplicate IDs
IdGenerator gen1 = new IdGenerator();  // If this were allowed
IdGenerator gen2 = new IdGenerator();
gen1.nextId();  // Returns 0
gen2.nextId();  // Returns 0 again! 💥 DUPLICATE!
```

### Solution with Singleton:
```java
// GOOD - Single instance = unique IDs
IdGenerator gen1 = IdGenerator.getInstance();
IdGenerator gen2 = IdGenerator.getInstance();
System.out.println(gen1 == gen2);  // true - same instance!
gen1.nextId();  // Returns 0
gen2.nextId();  // Returns 1 ✅ Unique!
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

## 🧪 Running Tests

```bash
mvn test
```

Test coverage includes:
- ✅ Singleton instance verification
- ✅ Sequential ID generation
- ✅ Initial state validation
- ✅ Reset functionality
- ✅ Shared state across references

## Project Structure

```
id-generator/
├── src/
│   ├── main/java/com/github/JoacoGDev/idgenerator/
│   │   └── IdGenerator.java
│   └── test/java/com/github/JoacoGDev/idgenerator/
│       └── IdGeneratorTest.java
├── pom.xml
├── README.md
└── LICENSE
```

## Roadmap

### Version 1.0 (Current) ✅
- Basic sequential ID generation
- Singleton pattern implementation
- Thread-safe operations
- Comprehensive tests

### Version 2.0 (Planned)
- File persistence (IDs survive application restart)
- ID prefixes (e.g., "USER_001", "ORDER_002")
- Multiple ID sequences

### Version 3.0 (Future)
- Enhanced thread safety (double-checked locking)
- Hexadecimal and custom formats
- Timestamp-based IDs

### Version 4.0 (Future)
- REST API with Spring Boot
- Web dashboard
- Distributed ID generation

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

Give a ⭐️ if this project helped you learn about design patterns!

## Changelog

### [1.0.0] - 2025-10-01
- Initial release
- Basic ID generation functionality
- Singleton pattern implementation
- Thread-safe operations
- JUnit test suite