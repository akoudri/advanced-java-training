# Foreign Function & Memory API Examples

This directory contains comprehensive examples demonstrating Java's Foreign Function & Memory API (Project Panama), which allows Java programs to interoperate with native libraries and manage off-heap memory safely and efficiently.

## Files Overview

### 1. `SimpleForeignFunctionExample.java`
**Perfect starting point - simple and complete:**
- Basic function calls (`abs`, `sqrt`)
- Memory allocation and management
- String operations with native code
- Struct-like data structures (Rectangle example)
- Array operations and slicing
- Clean, easy-to-understand examples

### 2. `ForeignFunctionExample.java`
**Basic examples covering fundamental concepts:**
- Loading and calling native C library functions (`abs`, `strlen`, `sqrt`, `pow`)
- Memory allocation and management with `MemorySegment`
- String handling between Java and native code
- Working with struct-like data structures
- Basic memory operations and layouts

### 2. `AdvancedForeignFunctionExample.java`
**Advanced features and patterns:**
- Callback functions (upcalls from native to Java)
- Function pointers and higher-order functions
- Complex data structures (arrays of structs)
- Error handling with native functions
- Using `qsort` with custom Java comparators

### 3. `CustomLibraryExample.java`
**Working with custom native libraries:**
- Library loading strategies
- Integration patterns with existing native code
- Memory safety best practices
- Resource management with different Arena types
- Safe string and buffer handling

### 5. `ForeignFunctionDemo.java`
**Comprehensive demo runner:**
- Runs all examples in sequence
- Provides educational information about the API
- Checks for API availability and Java version compatibility
- Includes detailed summary of features and benefits

## Requirements

- **Java 19+** (with `--enable-preview` flag for Java 19-21)
- **Java 22+** (FFM API is standard, no preview flag needed)
- **Operating System**: Linux, macOS, or Windows with standard C library

## Running the Examples

### Option 1: Run the Complete Demo
```bash
# For Java 19-21 (preview feature)
java --enable-preview -cp target/classes com.akfc.training.misc.ForeignFunctionDemo

# For Java 22+ (standard feature)
java -cp target/classes com.akfc.training.misc.ForeignFunctionDemo
```

### Option 2: Run Individual Examples
```bash
# Simple example (recommended starting point)
java --enable-preview --enable-native-access=ALL-UNNAMED -cp target/classes com.akfc.training.misc.SimpleForeignFunctionExample

# Basic examples
java --enable-preview --enable-native-access=ALL-UNNAMED -cp target/classes com.akfc.training.misc.ForeignFunctionExample

# Advanced examples
java --enable-preview -cp target/classes com.akfc.training.misc.AdvancedForeignFunctionExample

# Custom library examples
java --enable-preview -cp target/classes com.akfc.training.misc.CustomLibraryExample
```

### Option 3: Using Maven
```bash
# Compile first
mvn compile

# Run with Maven exec plugin (if configured)
mvn exec:java -Dexec.mainClass="com.akfc.training.misc.ForeignFunctionDemo" -Dexec.args="--enable-preview"
```

## Key Concepts Demonstrated

### 1. **Foreign Functions**
- Calling native C library functions without JNI
- Function descriptors and method handles
- Parameter and return type mapping
- Symbol lookup from native libraries

### 2. **Memory Management**
- `MemorySegment` for safe memory access
- `Arena` for automatic resource cleanup
- Memory layouts for structured data
- Bounds checking and memory safety

### 3. **Data Interoperability**
- Primitive type mapping (int, double, etc.)
- String conversion between Java and C
- Struct-like data structures
- Arrays and pointers

### 4. **Advanced Features**
- Callback functions (Java functions called from native code)
- Function pointers and higher-order functions
- Complex memory layouts
- Error handling patterns

## Benefits Over JNI

1. **No C/C++ Wrapper Code**: Direct calls to native functions
2. **Better Performance**: Reduced marshalling overhead
3. **Memory Safety**: Automatic bounds checking
4. **Resource Management**: Automatic cleanup with Arenas
5. **Expressive API**: Rich type system for complex data structures

## Common Use Cases

- **System Programming**: Access OS APIs and system calls
- **Performance-Critical Code**: Leverage optimized native libraries
- **Legacy Integration**: Interface with existing C/C++ libraries
- **Scientific Computing**: Use specialized mathematical libraries
- **Hardware Access**: Direct hardware manipulation

## Troubleshooting

### Preview Feature Errors
If you get errors about preview features:
```bash
# Make sure to use --enable-preview flag for Java 19-21
java --enable-preview YourClass
```

### Library Loading Issues
- Ensure the native library is in your system's library path
- Use absolute paths when loading custom libraries
- Check that the library architecture matches your JVM (32-bit vs 64-bit)

### Memory Access Errors
- Always use try-with-resources or explicit Arena cleanup
- Check array bounds before accessing memory segments
- Use appropriate ValueLayout for your data types

## Further Reading

- [JEP 424: Foreign Function & Memory API (Preview)](https://openjdk.org/jeps/424)
- [JEP 434: Foreign Function & Memory API (Second Preview)](https://openjdk.org/jeps/434)
- [JEP 442: Foreign Function & Memory API (Third Preview)](https://openjdk.org/jeps/442)
- [JEP 454: Foreign Function & Memory API](https://openjdk.org/jeps/454) (Final)
- [Project Panama Documentation](https://openjdk.org/projects/panama/)