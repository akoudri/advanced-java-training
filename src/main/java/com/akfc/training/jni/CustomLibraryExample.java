package com.akfc.training.jni;

import java.lang.foreign.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Example demonstrating how to work with custom native libraries using
 * the Foreign Function & Memory API.
 * 
 * This example shows:
 * 1. Loading custom shared libraries
 * 2. Working with the existing native calculator library
 * 3. Memory-safe operations
 * 4. Resource management best practices
 */
public class CustomLibraryExample {
    
    private static final Linker linker = Linker.nativeLinker();
    
    public static void main(String[] args) {
        System.out.println("=== Custom Native Library Example ===\n");
        
        try (Arena arena = Arena.ofConfined()) {
            // Example 1: Work with existing native calculator
            demonstrateNativeCalculator(arena);
            
            // Example 2: Show library loading concepts
            demonstrateLibraryLoading();
            
            // Example 3: Memory safety patterns
            demonstrateMemorySafety(arena);
            
            // Example 4: Safe string handling
            demonstrateSafeStringHandling(arena);
            
        } catch (Throwable e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates working with the existing native calculator library.
     */
    private static void demonstrateNativeCalculator(Arena arena) throws Throwable {
        System.out.println("1. Working with Native Calculator:");
        
        try {
            // Try to load the native calculator library
            // Note: This assumes the native library has been compiled
            Path libPath = Paths.get("src/main/native/nativecalc.c");
            System.out.println("  Native calculator source found at: " + libPath);
            System.out.println("  (Library would need to be compiled to use with FFM API)");
            
            // Show what the integration would look like if the library was compiled
            demonstrateHypotheticalNativeCalc(arena);
            
        } catch (Exception e) {
            System.out.println("  Native library not available: " + e.getMessage());
            System.out.println("  This is expected if the library hasn't been compiled.");
        }
        
        System.out.println();
    }
    
    /**
     * Shows what native calculator integration would look like.
     */
    private static void demonstrateHypotheticalNativeCalc(Arena arena) {
        System.out.println("  Hypothetical native calculator integration:");
        System.out.println("  ");
        System.out.println("  // If nativecalc was compiled as a shared library:");
        System.out.println("  // System.loadLibrary(\"nativecalc\");");
        System.out.println("  // SymbolLookup calcLib = SymbolLookup.libraryLookup(\"nativecalc\", arena);");
        System.out.println("  // MemorySegment addFunc = calcLib.find(\"add\").orElseThrow();");
        System.out.println("  // FunctionDescriptor addDesc = FunctionDescriptor.of(");
        System.out.println("  //     ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT);");
        System.out.println("  // MethodHandle add = linker.downcallHandle(addFunc, addDesc);");
        System.out.println("  // int result = (int) add.invoke(10, 20); // result = 30");
    }
    
    /**
     * Demonstrates library loading concepts and best practices.
     */
    private static void demonstrateLibraryLoading() {
        System.out.println("2. Library Loading Concepts:");
        
        System.out.println("  Standard approaches to load native libraries:");
        System.out.println("  ");
        System.out.println("  a) System libraries (already demonstrated):");
        System.out.println("     SymbolLookup stdlib = linker.defaultLookup();");
        System.out.println("  ");
        System.out.println("  b) Custom shared libraries:");
        System.out.println("     System.loadLibrary(\"mylib\");  // loads libmylib.so/.dll/.dylib");
        System.out.println("     SymbolLookup myLib = SymbolLookup.libraryLookup(\"mylib\", arena);");
        System.out.println("  ");
        System.out.println("  c) Libraries by path:");
        System.out.println("     Path libPath = Paths.get(\"/path/to/library.so\");");
        System.out.println("     SymbolLookup myLib = SymbolLookup.libraryLookup(libPath, arena);");
        System.out.println("  ");
        System.out.println("  d) Combining lookups:");
        System.out.println("     SymbolLookup combined = SymbolLookup.compose(stdlib, myLib);");
        System.out.println();
    }
    
    /**
     * Demonstrates memory safety patterns and best practices.
     */
    private static void demonstrateMemorySafety(Arena arena) throws Throwable {
        System.out.println("3. Memory Safety Patterns:");
        
        // Pattern 1: Confined arenas for automatic cleanup
        System.out.println("  Pattern 1: Confined Arena (automatic cleanup)");
        try (Arena localArena = Arena.ofConfined()) {
            MemorySegment buffer = localArena.allocate(1024);
            System.out.println("    Allocated 1024 bytes, will be auto-freed when arena closes");
            // Memory is automatically freed when the arena is closed
        }
        
        // Pattern 2: Shared arena for longer-lived memory
        System.out.println("  Pattern 2: Shared Arena (manual management)");
        Arena sharedArena = Arena.ofShared();
        try {
            MemorySegment sharedBuffer = sharedArena.allocate(2048);
            System.out.println("    Allocated 2048 bytes in shared arena");
            // This memory can be accessed from multiple threads
        } finally {
            sharedArena.close(); // Manual cleanup required
        }
        
        // Pattern 3: Global arena for static data
        System.out.println("  Pattern 3: Global Arena (never freed)");
        MemorySegment globalBuffer = Arena.global().allocate(512);
        System.out.println("    Allocated 512 bytes in global arena (never freed)");
        
        // Pattern 4: Safe memory access with bounds checking
        System.out.println("  Pattern 4: Safe Memory Access");
        MemorySegment safeBuffer = arena.allocate(ValueLayout.JAVA_INT.byteSize() * 10);
        
        try {
            // Safe access within bounds
            safeBuffer.setAtIndex(ValueLayout.JAVA_INT, 5, 42);
            int value = safeBuffer.getAtIndex(ValueLayout.JAVA_INT, 5);
            System.out.println("    Safe access: buffer[5] = " + value);
            
            // This would throw an exception (bounds check)
            // safeBuffer.setAtIndex(ValueLayout.JAVA_INT, 15, 99); // IndexOutOfBoundsException
            
        } catch (IndexOutOfBoundsException e) {
            System.out.println("    Bounds check prevented unsafe access: " + e.getMessage());
        }
        
        // Pattern 5: Memory segment slicing for structured data
        System.out.println("  Pattern 5: Memory Segment Slicing");
        MemorySegment largeBuffer = arena.allocate(1000);
        
        // Create slices for different purposes
        MemorySegment header = largeBuffer.asSlice(0, 100);
        MemorySegment data = largeBuffer.asSlice(100, 800);
        MemorySegment footer = largeBuffer.asSlice(900, 100);
        
        System.out.println("    Created slices: header(100), data(800), footer(100)");
        System.out.println("    Total size: " + (header.byteSize() + data.byteSize() + footer.byteSize()));
        
        System.out.println();
    }
    
    /**
     * Utility method to demonstrate safe string handling.
     */
    private static void demonstrateSafeStringHandling(Arena arena) {
        System.out.println("4. Safe String Handling:");
        
        // Safe way to create null-terminated strings
        String javaString = "Hello, FFM!";
        MemorySegment cString = arena.allocateUtf8String(javaString);
        
        System.out.println("  Java string: \"" + javaString + "\"");
        System.out.println("  C string size: " + cString.byteSize() + " bytes (includes null terminator)");
        
        // Safe way to read back strings
        String readBack = cString.getUtf8String(0);
        System.out.println("  Read back: \"" + readBack + "\"");
        
        // Working with string arrays
        String[] stringArray = {"First", "Second", "Third"};
        MemorySegment stringPtrArray = arena.allocate(ValueLayout.ADDRESS.byteSize() * stringArray.length);
        
        for (int i = 0; i < stringArray.length; i++) {
            MemorySegment str = arena.allocateUtf8String(stringArray[i]);
            stringPtrArray.setAtIndex(ValueLayout.ADDRESS, i, str);
        }
        
        System.out.println("  Created array of " + stringArray.length + " string pointers");
        System.out.println();
    }
}