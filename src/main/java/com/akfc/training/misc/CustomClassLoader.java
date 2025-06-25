package com.akfc.training.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
    private String directory;

    public CustomClassLoader(String directory, ClassLoader parent) {
        super(parent);
        this.directory = directory;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = directory + "/" + name.replace('.', '/') + ".class";
        try {
            byte[] classBytes = Files.readAllBytes(Paths.get(path));
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class " + name, e);
        }
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // Délégation au parent sauf pour notre package cible
        if (name.startsWith("com.example.dynamic")) {
            Class<?> c = findClass(name);
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
        return super.loadClass(name, resolve);
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoader loader = new CustomClassLoader("/plugins", CustomClassLoader.class.getClassLoader());
        Class<?> pluginClass = loader.loadClass("com.example.dynamic.Plugin");
        Object pluginInstance = pluginClass.getDeclaredConstructor().newInstance();
        pluginClass.getMethod("run").invoke(pluginInstance);
    }
}

