package com.akfc.training.misc;

import javax.naming.*;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * JNDI Utility Class - Best Practices and Common Patterns
 * 
 * This utility class demonstrates:
 * - JNDI lookup caching for performance
 * - Safe resource lookup with error handling
 * - Common JNDI naming patterns
 * - Context management best practices
 */
public class JndiUtils {
    
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
    
    // Cache for JNDI lookups to improve performance
    private static final ConcurrentMap<String, Object> lookupCache = new ConcurrentHashMap<>();
    
    // Thread-local context for thread safety
    private static final ThreadLocal<Context> contextHolder = new ThreadLocal<>();
    
    /**
     * Get or create JNDI context for current thread
     */
    public static Context getContext() throws NamingException {
        Context context = contextHolder.get();
        if (context == null) {
            // Use in-memory context for demonstration
            context = createInMemoryContext();
            contextHolder.set(context);
        }
        return context;
    }
    
    /**
     * Create a simple in-memory context for demonstration
     */
    private static Context createInMemoryContext() {
        return new SimpleInMemoryContext();
    }
    
    /**
     * Clean up JNDI context for current thread
     */
    public static void closeContext() {
        Context context = contextHolder.get();
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                // Log error in real application
                System.err.println("Error closing JNDI context: " + e.getMessage());
            } finally {
                contextHolder.remove();
            }
        }
    }
    
    /**
     * Perform JNDI lookup with caching
     * 
     * @param jndiName The JNDI name to lookup
     * @param useCache Whether to use cache for this lookup
     * @return The object bound to the JNDI name
     */
    @SuppressWarnings("unchecked")
    public static <T> T lookup(String jndiName, boolean useCache) throws NamingException {
        if (useCache && lookupCache.containsKey(jndiName)) {
            return (T) lookupCache.get(jndiName);
        }
        
        Context context = getContext();
        T result = (T) context.lookup(jndiName);
        
        if (useCache && result != null) {
            lookupCache.put(jndiName, result);
        }
        
        return result;
    }
    
    /**
     * Perform JNDI lookup without caching
     */
    public static <T> T lookup(String jndiName) throws NamingException {
        return lookup(jndiName, false);
    }
    
    /**
     * Safe JNDI lookup that returns null instead of throwing exception
     */
    public static <T> T safeLookup(String jndiName, boolean useCache) {
        try {
            return lookup(jndiName, useCache);
        } catch (NamingException e) {
            System.err.println("JNDI lookup failed for '" + jndiName + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Safe JNDI lookup without caching
     */
    public static <T> T safeLookup(String jndiName) {
        return safeLookup(jndiName, false);
    }
    
    /**
     * Lookup DataSource with standard naming patterns
     */
    public static DataSource lookupDataSource(String dataSourceName) throws NamingException {
        // Try different common naming patterns
        String[] patterns = {
            "java:comp/env/jdbc/" + dataSourceName,
            "jdbc/" + dataSourceName,
            dataSourceName
        };
        
        for (String pattern : patterns) {
            try {
                return lookup(pattern, true); // Cache DataSources
            } catch (NameNotFoundException e) {
                // Try next pattern
            }
        }
        
        throw new NameNotFoundException("DataSource not found: " + dataSourceName);
    }
    
    /**
     * Lookup environment entry (configuration value)
     */
    public static String lookupEnvEntry(String entryName) throws NamingException {
        return lookup("java:comp/env/" + entryName, true);
    }
    
    /**
     * Safe environment entry lookup with default value
     */
    public static String lookupEnvEntry(String entryName, String defaultValue) {
        String value = safeLookup("java:comp/env/" + entryName, true);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Bind object to JNDI with automatic subcontext creation
     */
    public static void bindWithSubcontexts(String jndiName, Object object) throws NamingException {
        Context context = getContext();
        
        // Handle compound names properly
        String[] parts;
        if (jndiName.contains(":")) {
            // Split by colon first, then by slash
            String[] colonParts = jndiName.split(":", 2);
            String contextPart = colonParts[0] + ":";
            String[] slashParts = colonParts[1].split("/");
            parts = new String[slashParts.length + 1];
            parts[0] = contextPart;
            System.arraycopy(slashParts, 0, parts, 1, slashParts.length);
        } else {
            parts = jndiName.split("/");
        }
        
        Context currentContext = context;
        
        // Create subcontexts if they don't exist
        for (int i = 0; i < parts.length - 1; i++) {
            try {
                currentContext = (Context) currentContext.lookup(parts[i]);
            } catch (NameNotFoundException e) {
                currentContext = currentContext.createSubcontext(parts[i]);
            }
        }
        
        // Bind the object
        currentContext.bind(parts[parts.length - 1], object);
    }
    
    /**
     * Clear the lookup cache
     */
    public static void clearCache() {
        lookupCache.clear();
    }
    
    /**
     * Get cache statistics
     */
    public static String getCacheStats() {
        return String.format("JNDI Cache: %d entries", lookupCache.size());
    }
    
    /**
     * Demonstration of JNDI utility usage
     */
    public static void main(String[] args) {
        System.out.println("=== JNDI Utilities Demo ===\n");
        
        try {
            // Initialize context first to ensure consistency
            getContext();
            
            // Setup test environment
            setupTestEnvironment();
            
            // Demonstrate utility methods
            demonstrateUtilities();
            
        } catch (Exception e) {
            System.err.println("Error in JNDI utilities demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always clean up
            closeContext();
        }
    }
    
    private static void setupTestEnvironment() throws NamingException {
        System.out.println("Setting up test environment...");
        
        // Use utility method to bind with automatic subcontext creation
        bindWithSubcontexts("java:comp/env/app/name", "JNDI Utils Demo");
        bindWithSubcontexts("java:comp/env/app/version", "1.0");
        bindWithSubcontexts("java:comp/env/database/url", "jdbc:h2:mem:testdb");
        bindWithSubcontexts("java:comp/env/database/timeout", "5000");
        
        System.out.println("✓ Test environment ready\n");
    }
    
    private static void demonstrateUtilities() throws NamingException {
        System.out.println("1. Basic Lookup Operations");
        System.out.println("-".repeat(30));
        
        // Regular lookup
        String appName = JndiUtils.<String>lookup("java:comp/env/app/name");
        System.out.println("App Name: " + appName);
        
        // Cached lookup
        String appVersion = JndiUtils.<String>lookup("java:comp/env/app/version", true);
        System.out.println("App Version: " + appVersion);
        
        // Environment entry lookup
        String dbUrl = lookupEnvEntry("database/url");
        System.out.println("Database URL: " + dbUrl);
        
        // Safe lookup with default
        String timeout = lookupEnvEntry("database/timeout", "3000");
        String missing = lookupEnvEntry("database/missing", "default-value");
        System.out.println("Timeout: " + timeout);
        System.out.println("Missing (with default): " + missing);
        
        System.out.println("\n2. Cache Operations");
        System.out.println("-".repeat(20));
        
        // Show cache stats
        System.out.println(getCacheStats());
        
        // Perform cached lookup again (should be faster)
        String cachedVersion = JndiUtils.<String>lookup("java:comp/env/app/version", true);
        System.out.println("Cached lookup result: " + cachedVersion);
        System.out.println(getCacheStats());
        
        System.out.println("\n3. Error Handling");
        System.out.println("-".repeat(20));
        
        // Safe lookup of non-existent resource
        String nonExistent = JndiUtils.<String>safeLookup("java:comp/env/does/not/exist");
        System.out.println("Non-existent resource: " + nonExistent);
        
        // Try DataSource lookup (will fail gracefully)
        DataSource ds = safeLookup("java:comp/env/jdbc/testds");
        System.out.println("DataSource lookup result: " + ds);
        
        System.out.println("\n=== JNDI Utilities Demo Complete ===");
        System.out.println("\nUtility Features Demonstrated:");
        System.out.println("• Cached lookups for performance");
        System.out.println("• Thread-safe context management");
        System.out.println("• Safe lookups with error handling");
        System.out.println("• Automatic subcontext creation");
        System.out.println("• Standard naming pattern support");
        System.out.println("• Environment entry convenience methods");
    }
}