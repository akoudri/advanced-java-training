package com.akfc.training.jni;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

/**
 * Comprehensive example demonstrating Java's Foreign Function & Memory API (Project Panama).
 * This API allows Java programs to interoperate with native libraries and manage off-heap memory.
 * 
 * Key concepts demonstrated:
 * 1. Loading native libraries
 * 2. Looking up native functions
 * 3. Calling native functions with different parameter types
 * 4. Memory management with MemorySegment
 * 5. Struct-like data handling
 * 
 * Note: This example uses standard C library functions available on most systems.
 */
public class ForeignFunctionExample {
    
    // Create a linker to link Java code with native functions
    private static final Linker linker = Linker.nativeLinker();
    
    // Create an arena for memory management (auto-cleanup)
    private static final Arena arena = Arena.ofConfined();
    
    public static void main(String[] args) {
        System.out.println("=== Foreign Function & Memory API Examples ===\n");
        
        try {
            // Example 1: Basic function calls
            demonstrateBasicFunctionCalls();
            
            // Example 2: String manipulation
            demonstrateStringManipulation();
            
            // Example 3: Memory allocation and management
            demonstrateMemoryManagement();
            
            // Example 4: Working with structs
            demonstrateStructHandling();
            
            // Example 5: Math functions
            demonstrateMathFunctions();
            
        } catch (Throwable e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up resources
            arena.close();
        }
    }
    
    /**
     * Demonstrates basic function calls to native C library functions.
     */
    private static void demonstrateBasicFunctionCalls() throws Throwable {
        System.out.println("1. Basic Function Calls:");
        
        // Look up the 'abs' function from the C standard library
        SymbolLookup stdlib = linker.defaultLookup();
        MemorySegment absFunction = stdlib.find("abs").orElseThrow();
        
        // Create a method handle for abs(int) -> int
        FunctionDescriptor absDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_INT,  // return type: int
            ValueLayout.JAVA_INT   // parameter type: int
        );
        MethodHandle abs = linker.downcallHandle(absFunction, absDescriptor);
        
        // Call the native abs function
        int result = (int) abs.invoke(-42);
        System.out.println("  abs(-42) = " + result);
        
        // Look up and call getpid() function (process ID)
        MemorySegment getpidFunction = stdlib.find("getpid").orElseThrow();
        FunctionDescriptor getpidDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT);
        MethodHandle getpid = linker.downcallHandle(getpidFunction, getpidDescriptor);
        
        int pid = (int) getpid.invoke();
        System.out.println("  Current process ID: " + pid);
        System.out.println();
    }
    
    /**
     * Demonstrates string manipulation with native functions.
     */
    private static void demonstrateStringManipulation() throws Throwable {
        System.out.println("2. String Manipulation:");
        
        SymbolLookup stdlib = linker.defaultLookup();
        
        // Look up strlen function
        MemorySegment strlenFunction = stdlib.find("strlen").orElseThrow();
        FunctionDescriptor strlenDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_LONG,    // return type: size_t (long)
            ValueLayout.ADDRESS       // parameter type: char* (address)
        );
        MethodHandle strlen = linker.downcallHandle(strlenFunction, strlenDescriptor);
        
        // Create a native string
        String javaString = "Hello, Foreign Functions!";
        MemorySegment nativeString = arena.allocateUtf8String(javaString);
        
        // Call strlen
        long length = (long) strlen.invoke(nativeString);
        System.out.println("  String: \"" + javaString + "\"");
        System.out.println("  Length from strlen(): " + length);
        System.out.println("  Java length(): " + javaString.length());
        System.out.println();
    }
    
    /**
     * Demonstrates memory allocation and management.
     */
    private static void demonstrateMemoryManagement() throws Throwable {
        System.out.println("3. Memory Management:");
        
        // Allocate memory for an array of integers
        int arraySize = 5;
        MemorySegment intArray = arena.allocate(ValueLayout.JAVA_INT.byteSize() * arraySize);
        
        // Fill the array with values
        for (int i = 0; i < arraySize; i++) {
            intArray.setAtIndex(ValueLayout.JAVA_INT, i, (i + 1) * 10);
        }
        
        // Read back the values
        System.out.print("  Array contents: [");
        for (int i = 0; i < arraySize; i++) {
            int value = intArray.getAtIndex(ValueLayout.JAVA_INT, i);
            System.out.print(value);
            if (i < arraySize - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        // Demonstrate memory segment properties
        System.out.println("  Memory segment size: " + intArray.byteSize() + " bytes");
        System.out.println("  Memory segment address: 0x" + 
                          Long.toHexString(intArray.address()));
        System.out.println();
    }
    
    /**
     * Demonstrates working with struct-like data.
     */
    private static void demonstrateStructHandling() throws Throwable {
        System.out.println("4. Struct-like Data Handling:");
        
        // Define a simple "Point" struct layout: {int x, int y}
        GroupLayout pointLayout = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("x"),
            ValueLayout.JAVA_INT.withName("y")
        ).withName("Point");
        
        // Allocate memory for a Point
        MemorySegment point = arena.allocate(pointLayout);
        
        // Set values using offsets
        VarHandle xHandle = pointLayout.varHandle(MemoryLayout.PathElement.groupElement("x"));
        VarHandle yHandle = pointLayout.varHandle(MemoryLayout.PathElement.groupElement("y"));
        
        xHandle.set(point, 100);  // x = 100
        yHandle.set(point, 200);  // y = 200
        
        // Read back the values
        int x = (int) xHandle.get(point);
        int y = (int) yHandle.get(point);
        
        System.out.println("  Point coordinates: (" + x + ", " + y + ")");
        System.out.println("  Struct size: " + pointLayout.byteSize() + " bytes");
        System.out.println();
    }
    
    /**
     * Demonstrates calling math functions from the C library.
     */
    private static void demonstrateMathFunctions() throws Throwable {
        System.out.println("5. Math Functions:");
        
        SymbolLookup stdlib = linker.defaultLookup();
        
        // Look up sqrt function
        MemorySegment sqrtFunction = stdlib.find("sqrt").orElseThrow();
        FunctionDescriptor sqrtDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_DOUBLE,  // return type: double
            ValueLayout.JAVA_DOUBLE   // parameter type: double
        );
        MethodHandle sqrt = linker.downcallHandle(sqrtFunction, sqrtDescriptor);
        
        // Call sqrt
        double input = 16.0;
        double result = (double) sqrt.invoke(input);
        System.out.println("  sqrt(" + input + ") = " + result);
        
        // Look up pow function
        MemorySegment powFunction = stdlib.find("pow").orElseThrow();
        FunctionDescriptor powDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_DOUBLE,  // return type: double
            ValueLayout.JAVA_DOUBLE,  // parameter 1: double (base)
            ValueLayout.JAVA_DOUBLE   // parameter 2: double (exponent)
        );
        MethodHandle pow = linker.downcallHandle(powFunction, powDescriptor);
        
        // Call pow
        double base = 2.0;
        double exponent = 8.0;
        double powResult = (double) pow.invoke(base, exponent);
        System.out.println("  pow(" + base + ", " + exponent + ") = " + powResult);
        System.out.println();
    }
}