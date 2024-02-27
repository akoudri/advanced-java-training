package fr.cenotelie.training.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Introspection {
    public static void main(String[] args) {
        String className = "java.util.ArrayList"; // Fully qualified class name

        try {
            // Load the class
            Class<?> clazz = Class.forName(className);

            // Display class information
            System.out.println("Class Name: " + clazz.getName());
            System.out.println("Package Name: " + clazz.getPackage().getName());

            // Display superclass information
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                System.out.println("Superclass: " + superclass.getName());
            }

            // Display implemented interfaces
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                System.out.print("Implemented Interfaces: ");
                for (Class<?> intf : interfaces) {
                    System.out.print(intf.getName() + " ");
                }
                System.out.println();
            }

            // Display constructors
            Constructor<?>[] constructors = clazz.getConstructors();
            System.out.println("Constructors:");
            for (Constructor<?> constructor : constructors) {
                System.out.println(constructor);
            }

            // Display fields
            Field[] fields = clazz.getDeclaredFields();
            System.out.println("Fields:");
            for (Field field : fields) {
                System.out.println(field.getType().getName() + " " + field.getName());
            }

            // Display methods
            Method[] methods = clazz.getDeclaredMethods();
            System.out.println("Methods:");
            for (Method method : methods) {
                System.out.println(method.getReturnType().getName() + " " + method.getName() + parameterTypesToString(method.getParameterTypes()));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String parameterTypesToString(Class<?>[] parameterTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (Class<?> paramType : parameterTypes) {
            stringBuilder.append(paramType.getName()).append(", ");
        }
        if (parameterTypes.length > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
