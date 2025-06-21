package com.akfc.training.jni;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

/**
 * Simple and complete example of Java's Foreign Function & Memory API.
 * This example demonstrates the most important concepts without complex callbacks.
 * 
 * Perfect for learning the fundamentals of:
 * - Calling native C library functions
 * - Memory management with Arena and MemorySegment
 * - Working with different data types
 * - Struct-like data structures
 * - String handling between Java and native code
 */
public class SimpleForeignFunctionExample {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Foreign Function & Memory API Example ===\n");
        
        // Use try-with-resources for automatic memory cleanup
        try (Arena arena = Arena.ofConfined()) {
            
            // 1. Basic function calls
            demonstrateBasicFunctions();
            
            // 2. Memory management
            demonstrateMemoryOperations(arena);
            
            // 3. String operations
            demonstrateStringOperations(arena);
            
            // 4. Struct-like data
            demonstrateStructOperations(arena);
            
            // 5. Array operations
            demonstrateArrayOperations(arena);
            
            System.out.println("✅ All examples completed successfully!");
            
        } catch (Throwable e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates calling basic C library functions.
     */
    private static void demonstrateBasicFunctions() throws Throwable {
        System.out.println("1. 🔧 Basic Function Calls:");
        
        Linker linker = Linker.nativeLinker();
        SymbolLookup stdlib = linker.defaultLookup();
        
        // Call abs() function
        MemorySegment absFunc = stdlib.find("abs").orElseThrow();
        FunctionDescriptor absDesc = FunctionDescriptor.of(
            ValueLayout.JAVA_INT,  // return int
            ValueLayout.JAVA_INT   // parameter int
        );
        MethodHandle abs = linker.downcallHandle(absFunc, absDesc);
        
        int result = (int) abs.invoke(-42);
        System.out.println("   abs(-42) = " + result);
        
        // Call sqrt() function
        MemorySegment sqrtFunc = stdlib.find("sqrt").orElseThrow();
        FunctionDescriptor sqrtDesc = FunctionDescriptor.of(
            ValueLayout.JAVA_DOUBLE,  // return double
            ValueLayout.JAVA_DOUBLE   // parameter double
        );
        MethodHandle sqrt = linker.downcallHandle(sqrtFunc, sqrtDesc);
        
        double sqrtResult = (double) sqrt.invoke(25.0);
        System.out.println("   sqrt(25.0) = " + sqrtResult);
        System.out.println();
    }
    
    /**
     * Demonstrates memory allocation and management.
     */
    private static void demonstrateMemoryOperations(Arena arena) {
        System.out.println("2. 💾 Memory Operations:");
        
        // Allocate memory for different data types
        MemorySegment intBuffer = arena.allocate(ValueLayout.JAVA_INT);
        MemorySegment doubleBuffer = arena.allocate(ValueLayout.JAVA_DOUBLE);
        MemorySegment byteArray = arena.allocate(10); // 10 bytes
        
        // Write values
        intBuffer.set(ValueLayout.JAVA_INT, 0, 12345);
        doubleBuffer.set(ValueLayout.JAVA_DOUBLE, 0, 3.14159);
        
        // Fill byte array
        for (int i = 0; i < 10; i++) {
            byteArray.set(ValueLayout.JAVA_BYTE, i, (byte) (i + 65)); // ASCII A-J
        }
        
        // Read values back
        int intValue = intBuffer.get(ValueLayout.JAVA_INT, 0);
        double doubleValue = doubleBuffer.get(ValueLayout.JAVA_DOUBLE, 0);
        
        System.out.println("   Integer value: " + intValue);
        System.out.println("   Double value: " + doubleValue);
        System.out.print("   Byte array: ");
        for (int i = 0; i < 10; i++) {
            char c = (char) byteArray.get(ValueLayout.JAVA_BYTE, i);
            System.out.print(c);
        }
        System.out.println();
        
        // Show memory properties
        System.out.println("   Memory segment size: " + byteArray.byteSize() + " bytes");
        System.out.println("   Memory address: 0x" + Long.toHexString(byteArray.address()));
        System.out.println();
    }
    
    /**
     * Demonstrates string operations between Java and native code.
     */
    private static void demonstrateStringOperations(Arena arena) throws Throwable {
        System.out.println("3. 📝 String Operations:");
        
        Linker linker = Linker.nativeLinker();
        SymbolLookup stdlib = linker.defaultLookup();
        
        // Create native string
        String javaString = "Hello, Panama!";
        MemorySegment nativeString = arena.allocateUtf8String(javaString);
        
        // Call strlen() to get string length
        MemorySegment strlenFunc = stdlib.find("strlen").orElseThrow();
        FunctionDescriptor strlenDesc = FunctionDescriptor.of(
            ValueLayout.JAVA_LONG,    // return size_t (long)
            ValueLayout.ADDRESS       // parameter char*
        );
        MethodHandle strlen = linker.downcallHandle(strlenFunc, strlenDesc);
        
        long nativeLength = (long) strlen.invoke(nativeString);
        
        System.out.println("   Java string: \"" + javaString + "\"");
        System.out.println("   Java length: " + javaString.length());
        System.out.println("   Native length: " + nativeLength);
        
        // Read string back from native memory
        String readBack = nativeString.getUtf8String(0);
        System.out.println("   Read back: \"" + readBack + "\"");
        System.out.println();
    }
    
    /**
     * Demonstrates working with struct-like data structures.
     */
    private static void demonstrateStructOperations(Arena arena) {
        System.out.println("4. 🏗️  Struct Operations:");
        
        // Define a "Rectangle" struct: {int width, int height, double area}
        GroupLayout rectLayout = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("width"),
            ValueLayout.JAVA_INT.withName("height"),
            ValueLayout.JAVA_DOUBLE.withName("area")
        ).withName("Rectangle");
        
        // Allocate memory for the struct
        MemorySegment rectangle = arena.allocate(rectLayout);
        
        // Get handles for struct fields
        VarHandle widthHandle = rectLayout.varHandle(MemoryLayout.PathElement.groupElement("width"));
        VarHandle heightHandle = rectLayout.varHandle(MemoryLayout.PathElement.groupElement("height"));
        VarHandle areaHandle = rectLayout.varHandle(MemoryLayout.PathElement.groupElement("area"));
        
        // Set values
        int width = 15;
        int height = 10;
        double area = width * height;
        
        widthHandle.set(rectangle, width);
        heightHandle.set(rectangle, height);
        areaHandle.set(rectangle, area);
        
        // Read values back
        int readWidth = (int) widthHandle.get(rectangle);
        int readHeight = (int) heightHandle.get(rectangle);
        double readArea = (double) areaHandle.get(rectangle);
        
        System.out.println("   Rectangle struct:");
        System.out.println("     Width: " + readWidth);
        System.out.println("     Height: " + readHeight);
        System.out.println("     Area: " + readArea);
        System.out.println("     Struct size: " + rectLayout.byteSize() + " bytes");
        System.out.println();
    }
    
    /**
     * Demonstrates working with arrays in native memory.
     */
    private static void demonstrateArrayOperations(Arena arena) {
        System.out.println("5. 📊 Array Operations:");
        
        // Create an array of integers
        int[] javaArray = {10, 20, 30, 40, 50};
        MemorySegment nativeArray = arena.allocate(ValueLayout.JAVA_INT.byteSize() * javaArray.length);
        
        // Copy Java array to native memory
        for (int i = 0; i < javaArray.length; i++) {
            nativeArray.setAtIndex(ValueLayout.JAVA_INT, i, javaArray[i]);
        }
        
        System.out.print("   Original array: [");
        for (int i = 0; i < javaArray.length; i++) {
            System.out.print(javaArray[i]);
            if (i < javaArray.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        // Modify array in native memory (multiply each element by 2)
        for (int i = 0; i < javaArray.length; i++) {
            int value = nativeArray.getAtIndex(ValueLayout.JAVA_INT, i);
            nativeArray.setAtIndex(ValueLayout.JAVA_INT, i, value * 2);
        }
        
        // Read modified array back
        System.out.print("   Modified array: [");
        for (int i = 0; i < javaArray.length; i++) {
            int value = nativeArray.getAtIndex(ValueLayout.JAVA_INT, i);
            System.out.print(value);
            if (i < javaArray.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        // Demonstrate array slicing
        MemorySegment firstHalf = nativeArray.asSlice(0, ValueLayout.JAVA_INT.byteSize() * 2);
        MemorySegment secondHalf = nativeArray.asSlice(ValueLayout.JAVA_INT.byteSize() * 2);
        
        System.out.println("   Array slicing:");
        System.out.println("     First half size: " + firstHalf.byteSize() + " bytes");
        System.out.println("     Second half size: " + secondHalf.byteSize() + " bytes");
        System.out.println();
    }
}