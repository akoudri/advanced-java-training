package com.akfc.training.misc;

import javax.naming.*;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple JNDI (Java Naming and Directory Interface) Example
 * 
 * JNDI provides a unified interface to multiple naming and directory services.
 * It allows Java applications to discover and look up data and objects via a name.
 * 
 * Key Concepts:
 * - Context: A set of name-to-object bindings
 * - InitialContext: The starting point for name resolution
 * - Binding: Associates a name with an object
 * - Lookup: Retrieves an object by its name
 */
public class JndiExample {
    
    /**
     * Simple data class to demonstrate object binding
     */
    static class Employee {
        private String name;
        private String department;
        private int id;
        
        public Employee(int id, String name, String department) {
            this.id = id;
            this.name = name;
            this.department = department;
        }
        
        @Override
        public String toString() {
            return String.format("Employee{id=%d, name='%s', department='%s'}", 
                               id, name, department);
        }
        
        // Getters
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public int getId() { return id; }
    }
    
    /**
     * Simple in-memory context implementation for demonstration
     */
    static class SimpleInMemoryContext implements Context {
        private final ConcurrentHashMap<String, Object> bindings = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Context> subcontexts = new ConcurrentHashMap<>();
        
        @Override
        public Object lookup(String name) throws NamingException {
            if (bindings.containsKey(name)) {
                return bindings.get(name);
            }
            if (subcontexts.containsKey(name)) {
                return subcontexts.get(name);
            }
            // Check for compound names (with / or :)
            if (name.contains("/") || name.contains(":")) {
                String[] parts;
                String contextKey;
                if (name.contains(":")) {
                    parts = name.split(":", 2);
                    contextKey = parts[0] + ":"; // Keep the colon for context key
                } else {
                    parts = name.split("/", 2);
                    contextKey = parts[0];
                }
                Context subcontext = subcontexts.get(contextKey);
                if (subcontext != null) {
                    return subcontext.lookup(parts[1]);
                }
            }
            throw new NameNotFoundException("Name not found: " + name);
        }
        
        @Override
        public void bind(String name, Object obj) throws NamingException {
            if (name.contains("/") || name.contains(":")) {
                String[] parts;
                String contextKey;
                if (name.contains(":")) {
                    parts = name.split(":", 2);
                    contextKey = parts[0] + ":"; // Keep the colon for context key
                } else {
                    parts = name.split("/", 2);
                    contextKey = parts[0];
                }
                Context subcontext = subcontexts.get(contextKey);
                if (subcontext == null) {
                    subcontext = createSubcontext(contextKey);
                }
                subcontext.bind(parts[1], obj);
            } else {
                bindings.put(name, obj);
            }
        }
        
        @Override
        public void unbind(String name) throws NamingException {
            if (name.contains("/") || name.contains(":")) {
                String[] parts;
                String contextKey;
                if (name.contains(":")) {
                    parts = name.split(":", 2);
                    contextKey = parts[0] + ":"; // Keep the colon for context key
                } else {
                    parts = name.split("/", 2);
                    contextKey = parts[0];
                }
                Context subcontext = subcontexts.get(contextKey);
                if (subcontext != null) {
                    subcontext.unbind(parts[1]);
                }
            } else {
                bindings.remove(name);
            }
        }
        
        @Override
        public void rebind(String name, Object obj) throws NamingException {
            bind(name, obj); // Simple implementation
        }
        
        @Override
        public Context createSubcontext(String name) throws NamingException {
            SimpleInMemoryContext subcontext = new SimpleInMemoryContext();
            subcontexts.put(name, subcontext);
            return subcontext;
        }
        
        @Override
        public void destroySubcontext(String name) throws NamingException {
            subcontexts.remove(name);
        }
        
        @Override
        public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
            return new SimpleNamingEnumeration();
        }
        
        private class SimpleNamingEnumeration implements NamingEnumeration<NameClassPair> {
            private final java.util.Iterator<String> iterator = bindings.keySet().iterator();
            
            @Override
            public boolean hasMore() { return iterator.hasNext(); }
            
            @Override
            public NameClassPair next() {
                String name = iterator.next();
                Object obj = bindings.get(name);
                return new NameClassPair(name, obj.getClass().getName());
            }
            
            @Override
            public boolean hasMoreElements() { return hasMore(); }
            
            @Override
            public NameClassPair nextElement() { return next(); }
            
            @Override
            public void close() {}
        }
        
        // Minimal implementations for other Context methods
        @Override public Object lookup(Name name) throws NamingException { return lookup(name.toString()); }
        @Override public void bind(Name name, Object obj) throws NamingException { bind(name.toString(), obj); }
        @Override public void rebind(Name name, Object obj) throws NamingException { rebind(name.toString(), obj); }
        @Override public void unbind(Name name) throws NamingException { unbind(name.toString()); }
        @Override public void rename(String oldName, String newName) throws NamingException {}
        @Override public void rename(Name oldName, Name newName) throws NamingException {}
        @Override public NamingEnumeration<NameClassPair> list(Name name) throws NamingException { return list(name.toString()); }
        @Override public NamingEnumeration<Binding> listBindings(String name) throws NamingException { return null; }
        @Override public NamingEnumeration<Binding> listBindings(Name name) throws NamingException { return null; }
        @Override public Context createSubcontext(Name name) throws NamingException { return createSubcontext(name.toString()); }
        @Override public void destroySubcontext(Name name) throws NamingException { destroySubcontext(name.toString()); }
        @Override public Object lookupLink(String name) throws NamingException { return lookup(name); }
        @Override public Object lookupLink(Name name) throws NamingException { return lookup(name); }
        @Override public NameParser getNameParser(String name) throws NamingException { return null; }
        @Override public NameParser getNameParser(Name name) throws NamingException { return null; }
        @Override public String composeName(String name, String prefix) throws NamingException { return prefix + "/" + name; }
        @Override public Name composeName(Name name, Name prefix) throws NamingException { return null; }
        @Override public Object addToEnvironment(String propName, Object propVal) throws NamingException { return null; }
        @Override public Object removeFromEnvironment(String propName) throws NamingException { return null; }
        @Override public Hashtable<?,?> getEnvironment() throws NamingException { return new Hashtable<>(); }
        @Override public void close() throws NamingException {}
        @Override public String getNameInNamespace() throws NamingException { return ""; }
    }
    
    /**
     * Create a simple in-memory context for demonstration
     */
    private static Context createInMemoryContext() {
        return new SimpleInMemoryContext();
    }
    
    public static void main(String[] args) {
        System.out.println("=== JNDI Example ===\n");
        
        try {
            // Example 1: Basic JNDI operations with in-memory context
            demonstrateBasicJndi();
            
            System.out.println("\n" + "=".repeat(50) + "\n");
            
            // Example 2: Subcontext operations
            demonstrateSubcontexts();
            
            System.out.println("\n" + "=".repeat(50) + "\n");
            
            // Example 3: Environment properties and context configuration
            demonstrateContextConfiguration();
            
        } catch (NamingException e) {
            System.err.println("JNDI Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates basic JNDI operations: bind, lookup, unbind
     */
    private static void demonstrateBasicJndi() throws NamingException {
        System.out.println("1. Basic JNDI Operations");
        System.out.println("-".repeat(25));
        
        // Create initial context with in-memory implementation
        System.out.println("ℹ Using simple in-memory context for demonstration");
        Context context = createInMemoryContext();
        
        // Create some sample objects
        Employee emp1 = new Employee(101, "Alice Johnson", "Engineering");
        Employee emp2 = new Employee(102, "Bob Smith", "Marketing");
        String companyName = "Tech Solutions Inc.";
        
        // Bind objects to names
        context.bind("employee/101", emp1);
        context.bind("employee/102", emp2);
        context.bind("company/name", companyName);
        
        System.out.println("✓ Objects bound to JNDI context");
        
        // Lookup objects by name
        Employee foundEmp1 = (Employee) context.lookup("employee/101");
        Employee foundEmp2 = (Employee) context.lookup("employee/102");
        String foundCompanyName = (String) context.lookup("company/name");
        
        System.out.println("✓ Objects retrieved from JNDI context:");
        System.out.println("  - " + foundEmp1);
        System.out.println("  - " + foundEmp2);
        System.out.println("  - Company: " + foundCompanyName);
        
        // List all bindings
        System.out.println("\n✓ All bindings in context:");
        NamingEnumeration<NameClassPair> bindings = context.list("");
        while (bindings.hasMore()) {
            NameClassPair pair = bindings.next();
            System.out.println("  - Name: " + pair.getName() + 
                             ", Class: " + pair.getClassName());
        }
        
        // Unbind an object
        context.unbind("employee/102");
        System.out.println("\n✓ Unbound employee/102");
        
        // Try to lookup unbound object (will throw exception)
        try {
            context.lookup("employee/102");
        } catch (NameNotFoundException e) {
            System.out.println("✓ Confirmed: employee/102 no longer exists");
        }
        
        // Clean up
        context.unbind("employee/101");
        context.unbind("company/name");
        context.close();
    }
    
    /**
     * Demonstrates working with subcontexts (nested naming contexts)
     */
     private static void demonstrateSubcontexts() throws NamingException {
        System.out.println("2. Subcontext Operations");
        System.out.println("-".repeat(25));
        
        Context rootContext = createInMemoryContext();
        
        // Create subcontexts
        Context departmentContext = rootContext.createSubcontext("departments");
        Context engineeringContext = departmentContext.createSubcontext("engineering");
        Context marketingContext = departmentContext.createSubcontext("marketing");
        
        System.out.println("✓ Created subcontexts: departments/engineering, departments/marketing");
        
        // Bind objects in different subcontexts
        engineeringContext.bind("manager", new Employee(201, "Carol Davis", "Engineering"));
        engineeringContext.bind("lead", new Employee(202, "David Wilson", "Engineering"));
        
        marketingContext.bind("manager", new Employee(301, "Eve Brown", "Marketing"));
        marketingContext.bind("coordinator", new Employee(302, "Frank Miller", "Marketing"));
        
        System.out.println("✓ Bound employees to respective department contexts");
        
        // Lookup using absolute paths
        Employee engManager = (Employee) rootContext.lookup("departments/engineering/manager");
        Employee mktManager = (Employee) rootContext.lookup("departments/marketing/manager");
        
        System.out.println("✓ Retrieved managers:");
        System.out.println("  - Engineering Manager: " + engManager);
        System.out.println("  - Marketing Manager: " + mktManager);
        
        // List contents of a subcontext
        System.out.println("\n✓ Engineering department staff:");
        NamingEnumeration<NameClassPair> engBindings = engineeringContext.list("");
        while (engBindings.hasMore()) {
            NameClassPair pair = engBindings.next();
            Employee emp = (Employee) engineeringContext.lookup(pair.getName());
            System.out.println("  - " + pair.getName() + ": " + emp);
        }
        
        // Clean up subcontexts (must unbind contents first)
        engineeringContext.unbind("manager");
        engineeringContext.unbind("lead");
        marketingContext.unbind("manager");
        marketingContext.unbind("coordinator");
        
        departmentContext.destroySubcontext("engineering");
        departmentContext.destroySubcontext("marketing");
        rootContext.destroySubcontext("departments");
        
        System.out.println("✓ Cleaned up all subcontexts");
        
        rootContext.close();
    }
    
    /**
     * Demonstrates context configuration with environment properties
     */
    private static void demonstrateContextConfiguration() throws NamingException {
        System.out.println("3. Context Configuration");
        System.out.println("-".repeat(25));
        
        // Create environment properties
        Hashtable<String, Object> env = new Hashtable<>();
        
        // Set initial context factory (using default for this example)
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        
        // Note: The above factory might not be available in all environments
        // For demonstration, we'll use our in-memory context
        System.out.println("ℹ Custom context factory not available, using in-memory context");
        
        // Demonstrate environment properties access
        Context defaultContext = createInMemoryContext();
        
        // Bind a properties object
        Properties appConfig = new Properties();
        appConfig.setProperty("app.name", "JNDI Demo Application");
        appConfig.setProperty("app.version", "1.0.0");
        appConfig.setProperty("database.url", "jdbc:h2:mem:testdb");
        
        defaultContext.bind("config/application", appConfig);
        
        // Retrieve and use configuration
        Properties retrievedConfig = (Properties) defaultContext.lookup("config/application");
        System.out.println("✓ Application configuration retrieved:");
        System.out.println("  - App Name: " + retrievedConfig.getProperty("app.name"));
        System.out.println("  - Version: " + retrievedConfig.getProperty("app.version"));
        System.out.println("  - Database URL: " + retrievedConfig.getProperty("database.url"));
        
        // Demonstrate rebind (update existing binding)
        appConfig.setProperty("app.version", "1.0.1");
        defaultContext.rebind("config/application", appConfig);
        
        Properties updatedConfig = (Properties) defaultContext.lookup("config/application");
        System.out.println("✓ Configuration updated via rebind:");
        System.out.println("  - New Version: " + updatedConfig.getProperty("app.version"));
        
        // Clean up
        defaultContext.unbind("config/application");
        defaultContext.close();
        
        System.out.println("\n=== JNDI Example Complete ===");
        System.out.println("\nKey JNDI Concepts Demonstrated:");
        System.out.println("• InitialContext - Entry point for JNDI operations");
        System.out.println("• bind/unbind - Associate/disassociate names with objects");
        System.out.println("• lookup - Retrieve objects by name");
        System.out.println("• rebind - Update existing name-object associations");
        System.out.println("• Subcontexts - Hierarchical naming structure");
        System.out.println("• Environment configuration - Customize context behavior");
    }
}