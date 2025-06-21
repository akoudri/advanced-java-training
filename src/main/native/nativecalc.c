/*
 * JNI Implementation for NativeCalculator
 * 
 * This C file implements the native methods declared in NativeCalculator.java
 * 
 * To compile this file:
 * 1. First generate the header file: javac -h . NativeCalculator.java
 * 2. Then compile the C code: gcc -shared -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -o libnativecalc.so nativecalc.c
 */

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/utsname.h>
#include <stdbool.h>

// Include the generated header file (will be created after compiling Java class)
// #include "com_akfc_training_jni_NativeCalculator.h"

/*
 * Class:     com_akfc_training_jni_NativeCalculator
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_akfc_training_jni_NativeCalculator_add
  (JNIEnv *env, jobject obj, jint a, jint b) {
    printf("Native: Adding %d + %d\n", a, b);
    return a + b;
}

/*
 * Class:     com_akfc_training_jni_NativeCalculator
 * Method:    multiply
 * Signature: (DD)D
 */
JNIEXPORT jdouble JNICALL Java_com_akfc_training_jni_NativeCalculator_multiply
  (JNIEnv *env, jobject obj, jdouble a, jdouble b) {
    printf("Native: Multiplying %.2f * %.2f\n", a, b);
    return a * b;
}

/*
 * Class:     com_akfc_training_jni_NativeCalculator
 * Method:    factorial
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_akfc_training_jni_NativeCalculator_factorial
  (JNIEnv *env, jobject obj, jint n) {
    if (n < 0) return -1; // Error case
    if (n <= 1) return 1;
    
    jlong result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    
    printf("Native: Factorial of %d = %ld\n", n, result);
    return result;
}

/*
 * Class:     com_akfc_training_jni_NativeCalculator
 * Method:    isPrime
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_akfc_training_jni_NativeCalculator_isPrime
  (JNIEnv *env, jobject obj, jint number) {
    if (number < 2) return JNI_FALSE;
    if (number == 2) return JNI_TRUE;
    if (number % 2 == 0) return JNI_FALSE;
    
    for (int i = 3; i * i <= number; i += 2) {
        if (number % i == 0) {
            return JNI_FALSE;
        }
    }
    
    printf("Native: %d is prime\n", number);
    return JNI_TRUE;
}

/*
 * Class:     com_akfc_training_jni_NativeCalculator
 * Method:    getSystemInfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_akfc_training_jni_NativeCalculator_getSystemInfo
  (JNIEnv *env, jobject obj) {
    struct utsname sys_info;
    char info_buffer[512];
    
    if (uname(&sys_info) == 0) {
        snprintf(info_buffer, sizeof(info_buffer), 
                "System: %s\nNode: %s\nRelease: %s\nVersion: %s\nMachine: %s",
                sys_info.sysname, sys_info.nodename, sys_info.release, 
                sys_info.version, sys_info.machine);
    } else {
        strcpy(info_buffer, "Unable to retrieve system information");
    }
    
    return (*env)->NewStringUTF(env, info_buffer);
}

/*
 * Class:     com_akfc_training_jni_NativeCalculator
 * Method:    processArray
 * Signature: ([I)[I
 */
JNIEXPORT jintArray JNICALL Java_com_akfc_training_jni_NativeCalculator_processArray
  (JNIEnv *env, jobject obj, jintArray input) {
    // Get array length
    jsize length = (*env)->GetArrayLength(env, input);
    
    // Get array elements
    jint *elements = (*env)->GetIntArrayElements(env, input, NULL);
    if (elements == NULL) {
        return NULL; // Out of memory
    }
    
    // Create output array
    jintArray result = (*env)->NewIntArray(env, length);
    if (result == NULL) {
        (*env)->ReleaseIntArrayElements(env, input, elements, 0);
        return NULL; // Out of memory
    }
    
    // Process elements (square each element)
    jint *output = malloc(length * sizeof(jint));
    for (int i = 0; i < length; i++) {
        output[i] = elements[i] * elements[i];
        printf("Native: %d^2 = %d\n", elements[i], output[i]);
    }
    
    // Set the output array
    (*env)->SetIntArrayRegion(env, result, 0, length, output);
    
    // Clean up
    (*env)->ReleaseIntArrayElements(env, input, elements, 0);
    free(output);
    
    return result;
}