package com.akfc.training.jni;

/**
 * Comprehensive demonstration of Java's Foreign Function & Memory API (Project Panama).
 * 
 * This demo runner showcases all the foreign function examples and provides
 * educational information about the API.
 * 
 * The Foreign Function & Memory API (FFM API) is a preview feature in Java 19-21
 * and became standard in Java 22. It provides:
 * 
 * 1. **Foreign Functions**: Call native functions from Java without JNI
 * 2. **Foreign Memory**: Manage off-heap memory safely and efficiently
 * 3. **Memory Layouts**: Describe the layout of foreign memory
 * 4. **Memory Segments**: Safe access to memory regions
 * 5. **Arenas**: Automatic memory management and cleanup
 * 
 * Key advantages over JNI:
 * - No need to write C/C++ wrapper code
 * - Better performance (no marshalling overhead)
 * - Memory safety through bounds checking
 * - Automatic resource management
 * - More expressive API for complex data structures
 * 
 * Usage: Run with --enable-preview flag if using Java 19-21
 * Example: java --enable-preview ForeignFunctionDemo
 */
public class ForeignFunctionDemo {
    
    public static void main(String[] args) {
        printHeader();
        
        try {
            // Check if FFM API is available
            checkFFMAvailability();
            
            System.out.println("Running Foreign Function & Memory API demonstrations...\n");
            
            // Run simple example first
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   SIMPLE EXAMPLE                            ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            SimpleForeignFunctionExample.main(args);
            
            // Run basic examples
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    BASIC EXAMPLES                           ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            ForeignFunctionExample.main(args);
            
            // Run advanced examples
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   ADVANCED EXAMPLES                         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            AdvancedForeignFunctionExample.main(args);
            
            // Run custom library examples
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                CUSTOM LIBRARY EXAMPLES                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            CustomLibraryExample.main(args);
            
            printSummary();
            
        } catch (Throwable e) {
            System.err.println("Error running demonstrations: " + e.getMessage());
            e.printStackTrace();
            
            if (e.getMessage() != null && e.getMessage().contains("preview")) {
                System.err.println("\nNote: If you're using Java 19-21, you may need to run with --enable-preview flag");
            }
        }
    }
    
    private static void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           Foreign Function & Memory API Demo                ║");
        System.out.println("║                  (Project Panama)                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Print Java version info
        String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        System.out.println("Java Version: " + javaVersion + " (" + javaVendor + ")");
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        System.out.println();
    }
    
    private static void checkFFMAvailability() {
        try {
            // Try to access FFM API classes to verify availability
            Class.forName("java.lang.foreign.MemorySegment");
            Class.forName("java.lang.foreign.Arena");
            Class.forName("java.lang.foreign.Linker");
            
            System.out.println("✓ Foreign Function & Memory API is available");
            
            // Check Java version
            String version = System.getProperty("java.version");
            if (version.startsWith("21") || version.startsWith("20") || version.startsWith("19")) {
                System.out.println("ℹ Note: You're using Java " + version.split("\\.")[0] + 
                                 " where FFM API is a preview feature");
                System.out.println("  Consider upgrading to Java 22+ for the final API");
            } else {
                System.out.println("✓ Using Java " + version.split("\\.")[0] + 
                                 " with standard FFM API");
            }
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Foreign Function & Memory API is not available. " +
                                     "Please use Java 19+ with --enable-preview or Java 22+", e);
        }
        
        System.out.println();
    }
    
    private static void printSummary() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        SUMMARY                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("The Foreign Function & Memory API demonstrations covered:");
        System.out.println();
        System.out.println("📚 BASIC CONCEPTS:");
        System.out.println("  • Loading and calling native functions");
        System.out.println("  • Memory allocation and management");
        System.out.println("  • Working with different data types");
        System.out.println("  • String handling between Java and native code");
        System.out.println("  • Struct-like data structures");
        System.out.println();
        System.out.println("🚀 ADVANCED FEATURES:");
        System.out.println("  • Callback functions (upcalls)");
        System.out.println("  • Function pointers");
        System.out.println("  • Complex data structures");
        System.out.println("  • Error handling patterns");
        System.out.println("  • Memory safety best practices");
        System.out.println();
        System.out.println("🔧 PRACTICAL APPLICATIONS:");
        System.out.println("  • Custom library integration");
        System.out.println("  • Resource management strategies");
        System.out.println("  • Performance considerations");
        System.out.println("  • Cross-platform compatibility");
        System.out.println();
        System.out.println("💡 KEY BENEFITS OVER JNI:");
        System.out.println("  • No C/C++ wrapper code needed");
        System.out.println("  • Better performance");
        System.out.println("  • Memory safety through bounds checking");
        System.out.println("  • Automatic resource management");
        System.out.println("  • More expressive API");
        System.out.println();
        System.out.println("🎯 NEXT STEPS:");
        System.out.println("  • Explore the official documentation");
        System.out.println("  • Try integrating with your own native libraries");
        System.out.println("  • Experiment with different memory layouts");
        System.out.println("  • Consider performance benchmarks vs JNI");
        System.out.println("  • Look into jextract tool for header file processing");
        System.out.println();
        System.out.println("Demo completed successfully! 🎉");
    }
}