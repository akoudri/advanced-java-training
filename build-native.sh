#!/bin/bash

# JNI Build Script for NativeCalculator
# This script compiles the Java class and builds the native library

echo "🚀 Building JNI Native Calculator"
echo "=================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if JAVA_HOME is set
if [ -z "$JAVA_HOME" ]; then
    echo -e "${RED}❌ JAVA_HOME is not set. Please set it to your JDK installation directory.${NC}"
    echo "Example: export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64"
    exit 1
fi

echo -e "${YELLOW}📍 Using JAVA_HOME: $JAVA_HOME${NC}"

# Create directories
mkdir -p target/classes
mkdir -p target/native

# Step 1: Compile Java class
echo -e "${YELLOW}📝 Step 1: Compiling Java class...${NC}"
javac -d target/classes src/main/java/com/akfc/training/jni/NativeCalculator.java

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Java compilation successful${NC}"
else
    echo -e "${RED}❌ Java compilation failed${NC}"
    exit 1
fi

# Step 2: Generate JNI header
echo -e "${YELLOW}🔧 Step 2: Generating JNI header...${NC}"
javac -h target/native -cp target/classes -d target/classes src/main/java/com/akfc/training/jni/NativeCalculator.java

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ JNI header generated${NC}"
else
    echo -e "${RED}❌ JNI header generation failed${NC}"
    exit 1
fi

# Step 3: Compile native library
echo -e "${YELLOW}🔨 Step 3: Compiling native library...${NC}"

# Detect OS and set appropriate flags
OS=$(uname -s)
case $OS in
    Linux*)
        LIB_NAME="libnativecalc.so"
        COMPILE_FLAGS="-shared -fPIC"
        JAVA_INCLUDE_OS="linux"
        ;;
    Darwin*)
        LIB_NAME="libnativecalc.dylib"
        COMPILE_FLAGS="-shared -fPIC"
        JAVA_INCLUDE_OS="darwin"
        ;;
    CYGWIN*|MINGW*|MSYS*)
        LIB_NAME="nativecalc.dll"
        COMPILE_FLAGS="-shared"
        JAVA_INCLUDE_OS="win32"
        ;;
    *)
        echo -e "${RED}❌ Unsupported OS: $OS${NC}"
        exit 1
        ;;
esac

echo -e "${YELLOW}🖥️  Detected OS: $OS${NC}"
echo -e "${YELLOW}📚 Building library: $LIB_NAME${NC}"

# Compile the native library
gcc $COMPILE_FLAGS \
    -I"$JAVA_HOME/include" \
    -I"$JAVA_HOME/include/$JAVA_INCLUDE_OS" \
    -I"target/native" \
    -o "target/native/$LIB_NAME" \
    src/main/native/nativecalc.c

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Native library compiled successfully${NC}"
    echo -e "${GREEN}📁 Library location: target/native/$LIB_NAME${NC}"
else
    echo -e "${RED}❌ Native library compilation failed${NC}"
    echo -e "${YELLOW}💡 Make sure you have gcc installed and JAVA_HOME is correct${NC}"
    exit 1
fi

# Step 4: Test the library
echo -e "${YELLOW}🧪 Step 4: Testing the native library...${NC}"

# Run the Java program with the native library in the path
java -Djava.library.path=target/native -cp target/classes com.akfc.training.jni.NativeCalculator

echo -e "${GREEN}🎉 Build completed successfully!${NC}"
echo ""
echo -e "${YELLOW}📋 To run the example:${NC}"
echo "java -Djava.library.path=target/native -cp target/classes com.akfc.training.jni.NativeCalculator"
echo ""
echo -e "${YELLOW}📋 To use with Maven:${NC}"
echo "Add -Djava.library.path=target/native to your JVM arguments"