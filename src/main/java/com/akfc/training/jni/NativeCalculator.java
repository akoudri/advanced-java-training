package com.akfc.training.jni;

/**
 * JNI Example: Native Calculator
 * 
 * This class demonstrates how to use JNI (Java Native Interface) to call
 * native C/C++ functions from Java code.
 * 
 * Features demonstrated:
 * - Loading native libraries
 * - Declaring native methods
 * - Passing primitive types to native code
 * - Returning values from native code
 * - Error handling with native code
 */
public class NativeCalculator {
    
    // Static block to load the native library
    // The library name should match the compiled shared library (.so, .dll, .dylib)
    static {
        try {
            // This will look for libnativecalc.so on Linux, nativecalc.dll on Windows
            System.loadLibrary("nativecalc");
            System.out.println("✅ Native library loaded successfully!");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("❌ Failed to load native library: " + e.getMessage());
            System.err.println("💡 Make sure the native library is compiled and in java.library.path");
        }
    }
    
    // Native method declarations
    // These methods will be implemented in C/C++
    
    /**
     * Add two integers using native code
     */
    public native int add(int a, int b);
    
    /**
     * Multiply two doubles using native code
     */
    public native double multiply(double a, double b);
    
    /**
     * Calculate factorial using native code (demonstrates recursion in native code)
     */
    public native long factorial(int n);
    
    /**
     * Check if a number is prime using native code
     */
    public native boolean isPrime(int number);
    
    /**
     * Get system information from native code
     */
    public native String getSystemInfo();
    
    /**
     * Demonstrate array processing in native code
     */
    public native int[] processArray(int[] input);
    
    /**
     * Demo method to show JNI capabilities
     */
    public void demonstrateJNI() {
        System.out.println("🚀 JNI Native Calculator Demo");
        System.out.println("=" .repeat(40));
        
        try {
            // Test basic arithmetic
            System.out.println("📊 Basic Arithmetic:");
            System.out.println("5 + 3 = " + add(5, 3));
            System.out.println("4.5 * 2.2 = " + multiply(4.5, 2.2));
            
            // Test factorial
            System.out.println("\n🔢 Factorial Calculation:");
            for (int i = 1; i <= 5; i++) {
                System.out.println(i + "! = " + factorial(i));
            }
            
            // Test prime checking
            System.out.println("\n🔍 Prime Number Check:");
            int[] testNumbers = {2, 3, 4, 17, 25, 29};
            for (int num : testNumbers) {
                System.out.println(num + " is " + (isPrime(num) ? "prime" : "not prime"));
            }
            
            // Test system info
            System.out.println("\n💻 System Information:");
            System.out.println(getSystemInfo());
            
            // Test array processing
            System.out.println("\n📋 Array Processing:");
            int[] input = {1, 2, 3, 4, 5};
            int[] result = processArray(input);
            System.out.print("Input:  [");
            for (int i = 0; i < input.length; i++) {
                System.out.print(input[i] + (i < input.length - 1 ? ", " : ""));
            }
            System.out.println("]");
            System.out.print("Output: [");
            for (int i = 0; i < result.length; i++) {
                System.out.print(result[i] + (i < result.length - 1 ? ", " : ""));
            }
            System.out.println("]");
            
        } catch (UnsatisfiedLinkError e) {
            System.err.println("❌ Native method call failed: " + e.getMessage());
            System.err.println("💡 This is expected if the native library is not compiled yet.");
        }
    }
    
    public static void main(String[] args) {
        NativeCalculator calculator = new NativeCalculator();
        calculator.demonstrateJNI();
    }
}