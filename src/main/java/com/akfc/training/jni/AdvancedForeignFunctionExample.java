package com.akfc.training.jni;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.util.function.IntBinaryOperator;

/**
 * Advanced Foreign Function & Memory API examples demonstrating:
 * 1. Callback functions (upcalls from native to Java)
 * 2. Function pointers
 * 3. Complex data structures
 * 4. Error handling with native functions
 * 5. Performance considerations
 */
public class AdvancedForeignFunctionExample {
    
    private static final Linker linker = Linker.nativeLinker();
    private static final Arena arena = Arena.ofConfined();
    
    public static void main(String[] args) {
        System.out.println("=== Advanced Foreign Function Examples ===\n");
        
        try {
            // Example 1: Callback functions (qsort with custom comparator)
            demonstrateCallbacks();
            
            // Example 2: Function pointers and higher-order functions
            demonstrateFunctionPointers();
            
            // Example 3: Complex data structures (arrays of structs)
            demonstrateComplexStructures();
            
            // Example 4: Error handling with errno
            demonstrateErrorHandling();
            
        } catch (Throwable e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            arena.close();
        }
    }
    
    /**
     * Demonstrates callback functions using qsort from C library.
     * This shows how Java functions can be called from native code.
     */
    private static void demonstrateCallbacks() throws Throwable {
        System.out.println("1. Callback Functions (qsort example):");
        
        SymbolLookup stdlib = linker.defaultLookup();
        
        // Look up qsort function: void qsort(void *base, size_t nmemb, size_t size, int (*compar)(const void *, const void *))
        MemorySegment qsortFunction = stdlib.find("qsort").orElseThrow();
        FunctionDescriptor qsortDescriptor = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS,      // base pointer
            ValueLayout.JAVA_LONG,    // number of elements
            ValueLayout.JAVA_LONG,    // size of each element
            ValueLayout.ADDRESS       // comparator function pointer
        );
        MethodHandle qsort = linker.downcallHandle(qsortFunction, qsortDescriptor);
        
        // Create an array of integers to sort
        int[] javaArray = {64, 34, 25, 12, 22, 11, 90};
        MemorySegment nativeArray = arena.allocate(ValueLayout.JAVA_INT.byteSize() * javaArray.length);
        
        // Copy Java array to native memory
        for (int i = 0; i < javaArray.length; i++) {
            nativeArray.setAtIndex(ValueLayout.JAVA_INT, i, javaArray[i]);
        }
        
        System.out.print("  Before sorting: ");
        printIntArray(nativeArray, javaArray.length);
        
        // Create a Java comparator function
        IntBinaryOperator comparator = (a, b) -> Integer.compare(a, b);
        
        // Create an upcall stub (Java function callable from native code)
        FunctionDescriptor comparatorDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_INT,     // return type: int
            ValueLayout.ADDRESS,      // const void *a
            ValueLayout.ADDRESS       // const void *b
        );
        
        MethodHandle comparatorHandle = createComparatorHandle();
        MemorySegment comparatorStub = linker.upcallStub(comparatorHandle, comparatorDescriptor, arena);
        
        // Call qsort with our comparator
        qsort.invoke(
            nativeArray,                    // array to sort
            (long) javaArray.length,        // number of elements
            (long) ValueLayout.JAVA_INT.byteSize(),  // size of each element
            comparatorStub                  // comparator function
        );
        
        System.out.print("  After sorting:  ");
        printIntArray(nativeArray, javaArray.length);
        System.out.println();
    }
    
    /**
     * Creates a method handle for the comparator function.
     */
    private static MethodHandle createComparatorHandle() throws Throwable {
        // This method will be called from native code
        class Comparator {
            static int compare(MemorySegment a, MemorySegment b) {
                int valueA = a.get(ValueLayout.JAVA_INT, 0);
                int valueB = b.get(ValueLayout.JAVA_INT, 0);
                return Integer.compare(valueA, valueB);
            }
        }
        
        return java.lang.invoke.MethodHandles.lookup()
            .findStatic(Comparator.class, "compare", 
                java.lang.invoke.MethodType.methodType(int.class, MemorySegment.class, MemorySegment.class));
    }
    
    /**
     * Demonstrates function pointers and higher-order functions.
     */
    private static void demonstrateFunctionPointers() throws Throwable {
        System.out.println("2. Function Pointers:");
        
        // Create different mathematical operation functions
        MethodHandle addHandle = createMathOperationHandle("add", (a, b) -> a + b);
        MethodHandle multiplyHandle = createMathOperationHandle("multiply", (a, b) -> a * b);
        
        // Create function descriptors
        FunctionDescriptor mathOpDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT
        );
        
        // Create upcall stubs
        MemorySegment addStub = linker.upcallStub(addHandle, mathOpDescriptor, arena);
        MemorySegment multiplyStub = linker.upcallStub(multiplyHandle, mathOpDescriptor, arena);
        
        // Demonstrate calling through function pointers
        int a = 15, b = 7;
        
        System.out.println("  Using function pointers:");
        System.out.println("    " + a + " + " + b + " = " + callMathOperation(addStub, mathOpDescriptor, a, b));
        System.out.println("    " + a + " * " + b + " = " + callMathOperation(multiplyStub, mathOpDescriptor, a, b));
        System.out.println();
    }
    
    /**
     * Creates a method handle for mathematical operations.
     */
    private static MethodHandle createMathOperationHandle(String name, IntBinaryOperator operation) throws Throwable {
        class MathOps {
            static int add(int a, int b) { return a + b; }
            static int multiply(int a, int b) { return a * b; }
        }
        
        return java.lang.invoke.MethodHandles.lookup()
            .findStatic(MathOps.class, name, 
                java.lang.invoke.MethodType.methodType(int.class, int.class, int.class));
    }
    
    /**
     * Calls a mathematical operation through a function pointer.
     */
    private static int callMathOperation(MemorySegment functionPtr, FunctionDescriptor descriptor, int a, int b) throws Throwable {
        MethodHandle handle = linker.downcallHandle(functionPtr, descriptor);
        return (int) handle.invoke(a, b);
    }
    
    /**
     * Demonstrates complex data structures (array of structs).
     */
    private static void demonstrateComplexStructures() throws Throwable {
        System.out.println("3. Complex Data Structures (Array of Structs):");
        
        // Define a "Person" struct: {int age, char name[20]}
        GroupLayout personLayout = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("age"),
            MemoryLayout.sequenceLayout(20, ValueLayout.JAVA_BYTE).withName("name")
        ).withName("Person");
        
        // Create an array of 3 persons
        int personCount = 3;
        MemorySegment personArray = arena.allocate(personLayout.byteSize() * personCount);
        
        // Data to populate
        String[] names = {"Alice", "Bob", "Charlie"};
        int[] ages = {25, 30, 35};
        
        // Populate the array
        VarHandle ageHandle = personLayout.varHandle(MemoryLayout.PathElement.groupElement("age"));
        
        for (int i = 0; i < personCount; i++) {
            MemorySegment person = personArray.asSlice(i * personLayout.byteSize(), personLayout.byteSize());
            
            // Set age
            ageHandle.set(person, ages[i]);
            
            // Set name (copy string bytes)
            MemorySegment nameSegment = person.asSlice(4, 20); // name starts at offset 4
            byte[] nameBytes = names[i].getBytes();
            for (int j = 0; j < Math.min(nameBytes.length, 19); j++) {
                nameSegment.set(ValueLayout.JAVA_BYTE, j, nameBytes[j]);
            }
            nameSegment.set(ValueLayout.JAVA_BYTE, Math.min(nameBytes.length, 19), (byte) 0); // null terminator
        }
        
        // Read back and display
        System.out.println("  Person array contents:");
        for (int i = 0; i < personCount; i++) {
            MemorySegment person = personArray.asSlice(i * personLayout.byteSize(), personLayout.byteSize());
            
            int age = (int) ageHandle.get(person);
            
            // Read name
            MemorySegment nameSegment = person.asSlice(4, 20);
            StringBuilder nameBuilder = new StringBuilder();
            for (int j = 0; j < 20; j++) {
                byte b = nameSegment.get(ValueLayout.JAVA_BYTE, j);
                if (b == 0) break;
                nameBuilder.append((char) b);
            }
            
            System.out.println("    Person " + (i + 1) + ": " + nameBuilder + ", age " + age);
        }
        System.out.println();
    }
    
    /**
     * Demonstrates error handling with native functions.
     */
    private static void demonstrateErrorHandling() throws Throwable {
        System.out.println("4. Error Handling:");
        
        SymbolLookup stdlib = linker.defaultLookup();
        
        try {
            // Try to look up a function that doesn't exist
            stdlib.find("nonexistent_function").orElseThrow(() -> 
                new RuntimeException("Function not found"));
        } catch (RuntimeException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }
        
        // Demonstrate errno handling with a file operation
        try {
            MemorySegment fopenFunction = stdlib.find("fopen").orElseThrow();
            FunctionDescriptor fopenDescriptor = FunctionDescriptor.of(
                ValueLayout.ADDRESS,      // FILE* return
                ValueLayout.ADDRESS,      // filename
                ValueLayout.ADDRESS       // mode
            );
            MethodHandle fopen = linker.downcallHandle(fopenFunction, fopenDescriptor);
            
            // Try to open a non-existent file
            MemorySegment filename = arena.allocateUtf8String("/nonexistent/path/file.txt");
            MemorySegment mode = arena.allocateUtf8String("r");
            
            MemorySegment filePtr = (MemorySegment) fopen.invoke(filename, mode);
            
            if (filePtr.address() == 0) {
                System.out.println("  File operation failed as expected (file not found)");
            } else {
                System.out.println("  Unexpected: file opened successfully");
                // Would need to call fclose here in real code
            }
            
        } catch (Throwable e) {
            System.out.println("  Error in file operation: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Helper method to print an integer array from native memory.
     */
    private static void printIntArray(MemorySegment array, int length) {
        System.out.print("[");
        for (int i = 0; i < length; i++) {
            int value = array.getAtIndex(ValueLayout.JAVA_INT, i);
            System.out.print(value);
            if (i < length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}