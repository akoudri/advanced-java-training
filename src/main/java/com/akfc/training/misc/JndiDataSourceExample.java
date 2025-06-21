package com.akfc.training.misc;

import javax.naming.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced JNDI Example - DataSource and Resource Management
 * 
 * This example demonstrates how JNDI is commonly used in enterprise applications
 * for managing resources like database connections, JMS queues, and other services.
 * 
 * In real enterprise environments (like application servers), JNDI is used to:
 * - Look up DataSources for database connections
 * - Access JMS destinations (queues, topics)
 * - Retrieve EJB references
 * - Access environment entries and configuration
 */
public class JndiDataSourceExample {
    
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
     * Mock DataSource implementation for demonstration
     * In real applications, this would be provided by the application server
     */
    static class MockDataSource implements DataSource {
        private String url;
        private String username;
        private String password;
        
        public MockDataSource(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }
        
        @Override
        public Connection getConnection() throws SQLException {
            // In real implementation, this would return actual database connection
            System.out.println("Mock: Getting connection to " + url + " as " + username);
            return null; // Mock implementation
        }
        
        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            System.out.println("Mock: Getting connection to " + url + " as " + username);
            return null; // Mock implementation
        }
        
        // Other DataSource methods would be implemented here...
        @Override public java.io.PrintWriter getLogWriter() { return null; }
        @Override public void setLogWriter(java.io.PrintWriter out) {}
        @Override public void setLoginTimeout(int seconds) {}
        @Override public int getLoginTimeout() { return 0; }
        @Override public java.util.logging.Logger getParentLogger() { return null; }
        @Override public <T> T unwrap(Class<T> iface) { return null; }
        @Override public boolean isWrapperFor(Class<?> iface) { return false; }
        
        @Override
        public String toString() {
            return String.format("MockDataSource{url='%s', username='%s'}", url, username);
        }
    }
    
    /**
     * Service class that uses JNDI to look up resources
     */
    static class DatabaseService {
        private Context jndiContext;
        
        public DatabaseService(Context context) throws NamingException {
            this.jndiContext = context;
        }
        
        /**
         * Get database connection using JNDI lookup
         */
        public Connection getConnection(String dataSourceName) throws NamingException, SQLException {
            DataSource dataSource = (DataSource) jndiContext.lookup(dataSourceName);
            return dataSource.getConnection();
        }
        
        /**
         * Get configuration property using JNDI
         */
        public String getConfigProperty(String propertyName) throws NamingException {
            return (String) jndiContext.lookup("java:comp/env/" + propertyName);
        }
        
        public void close() throws NamingException {
            if (jndiContext != null) {
                jndiContext.close();
            }
        }
    }
    
    /**
     * Create a simple in-memory context for demonstration
     */
    private static Context createInMemoryContext() {
        return new SimpleInMemoryContext();
    }
    
    public static void main(String[] args) {
        System.out.println("=== Advanced JNDI Example - DataSource & Resources ===\n");
        
        try {
            // Setup enterprise-like JNDI environment
            Context rootContext = setupEnterpriseJndiEnvironment();
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            // Demonstrate resource lookup patterns
            demonstrateResourceLookup(rootContext);
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            // Demonstrate service integration
            demonstrateServiceIntegration(rootContext);
            
            // Clean up the JNDI environment
            cleanupJndiEnvironment(rootContext);
            
        } catch (Exception e) {
            System.err.println("Error in JNDI example: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sets up a JNDI environment similar to what you'd find in an application server
     */
    private static Context setupEnterpriseJndiEnvironment() throws NamingException {
        System.out.println("1. Setting up Enterprise JNDI Environment");
        System.out.println("-".repeat(45));
        
        Context rootContext = createInMemoryContext();
        
        // Create standard Java EE JNDI structure
        Context javaContext = rootContext.createSubcontext("java:");
        Context compContext = javaContext.createSubcontext("comp");
        Context envContext = compContext.createSubcontext("env");
        Context jdbcContext = envContext.createSubcontext("jdbc");
        
        // Bind DataSources (typical in application servers)
        DataSource primaryDS = new MockDataSource(
            "jdbc:postgresql://localhost:5432/myapp", 
            "app_user", 
            "app_password"
        );
        
        DataSource readOnlyDS = new MockDataSource(
            "jdbc:postgresql://localhost:5432/myapp_readonly", 
            "readonly_user", 
            "readonly_password"
        );
        
        jdbcContext.bind("primary", primaryDS);
        jdbcContext.bind("readonly", readOnlyDS);
        
        System.out.println("✓ DataSources bound:");
        System.out.println("  - java:comp/env/jdbc/primary");
        System.out.println("  - java:comp/env/jdbc/readonly");
        
        // Bind environment entries (configuration)
        envContext.bind("app/name", "Advanced Java Training App");
        envContext.bind("app/version", "2.0.0");
        envContext.bind("app/debug", "true");
        envContext.bind("database/pool/maxConnections", "20");
        envContext.bind("database/pool/timeout", "30000");
        
        System.out.println("✓ Environment entries bound:");
        System.out.println("  - java:comp/env/app/*");
        System.out.println("  - java:comp/env/database/pool/*");
        
        // Create additional contexts for other resources
        Context jmsContext = envContext.createSubcontext("jms");
        jmsContext.bind("queue/orders", "Mock Order Queue");
        jmsContext.bind("topic/notifications", "Mock Notification Topic");
        
        System.out.println("✓ JMS resources bound:");
        System.out.println("  - java:comp/env/jms/queue/orders");
        System.out.println("  - java:comp/env/jms/topic/notifications");
        
        return rootContext;
    }
    
    /**
     * Demonstrates typical resource lookup patterns used in enterprise applications
     */
    private static void demonstrateResourceLookup(Context context) throws NamingException, SQLException {
        System.out.println("2. Resource Lookup Patterns");
        System.out.println("-".repeat(30));
        
        // DataSource lookup (most common JNDI usage)
        System.out.println("📊 DataSource Lookup:");
        DataSource primaryDS = (DataSource) context.lookup("java:comp/env/jdbc/primary");
        DataSource readOnlyDS = (DataSource) context.lookup("java:comp/env/jdbc/readonly");
        
        System.out.println("  - Primary DS: " + primaryDS);
        System.out.println("  - ReadOnly DS: " + readOnlyDS);
        
        // Simulate getting connections
        try {
            Connection conn1 = primaryDS.getConnection();
            Connection conn2 = readOnlyDS.getConnection();
            // In real code, you'd use these connections and close them
        } catch (SQLException e) {
            // Expected with mock implementation
        }
        
        // Configuration lookup
        System.out.println("\n⚙️ Configuration Lookup:");
        String appName = (String) context.lookup("java:comp/env/app/name");
        String appVersion = (String) context.lookup("java:comp/env/app/version");
        String debugMode = (String) context.lookup("java:comp/env/app/debug");
        String maxConnections = (String) context.lookup("java:comp/env/database/pool/maxConnections");
        
        System.out.println("  - Application: " + appName + " v" + appVersion);
        System.out.println("  - Debug Mode: " + debugMode);
        System.out.println("  - Max DB Connections: " + maxConnections);
        
        // JMS resource lookup
        System.out.println("\n📨 JMS Resource Lookup:");
        String orderQueue = (String) context.lookup("java:comp/env/jms/queue/orders");
        String notificationTopic = (String) context.lookup("java:comp/env/jms/topic/notifications");
        
        System.out.println("  - Order Queue: " + orderQueue);
        System.out.println("  - Notification Topic: " + notificationTopic);
    }
    
    /**
     * Demonstrates how services integrate with JNDI for resource management
     */
    private static void demonstrateServiceIntegration(Context rootContext) throws Exception {
        System.out.println("3. Service Integration with JNDI");
        System.out.println("-".repeat(35));
        
        // Create a service that uses JNDI
        DatabaseService dbService = new DatabaseService(rootContext);
        
        System.out.println("🔧 Service using JNDI for resource lookup:");
        
        try {
            // Service looks up DataSource via JNDI
            Connection conn = dbService.getConnection("java:comp/env/jdbc/primary");
            System.out.println("  ✓ Successfully looked up primary DataSource");
            
            // Service looks up configuration via JNDI
            String appName = dbService.getConfigProperty("app/name");
            String debugMode = dbService.getConfigProperty("app/debug");
            
            System.out.println("  ✓ Configuration retrieved:");
            System.out.println("    - App Name: " + appName);
            System.out.println("    - Debug Mode: " + debugMode);
            
        } catch (SQLException e) {
            // Expected with mock DataSource
            System.out.println("  ✓ DataSource lookup successful (connection mock)");
        }
        
        // Demonstrate error handling for missing resources
        System.out.println("\n🚫 Error Handling:");
        try {
            dbService.getConnection("java:comp/env/jdbc/nonexistent");
        } catch (NameNotFoundException e) {
            System.out.println("  ✓ Properly handled missing resource: " + e.getMessage());
        }
        
        dbService.close();
        
        System.out.println("\n=== Advanced JNDI Example Complete ===");
        System.out.println("\nEnterprise JNDI Usage Patterns:");
        System.out.println("• DataSource lookup for database connections");
        System.out.println("• Environment entries for configuration");
        System.out.println("• JMS resource lookup for messaging");
        System.out.println("• Standard java:comp/env namespace");
        System.out.println("• Service integration with dependency injection");
        System.out.println("• Proper resource cleanup and error handling");
    }
    
    /**
     * Clean up the JNDI environment
     */
    private static void cleanupJndiEnvironment(Context rootContext) throws NamingException {
        try {
            // Clean up in reverse order of creation
            Context javaContext = (Context) rootContext.lookup("java:");
            Context compContext = (Context) javaContext.lookup("comp");
            Context envContext = (Context) compContext.lookup("env");
            
            // Unbind all resources
            Context jdbcContext = (Context) envContext.lookup("jdbc");
            jdbcContext.unbind("primary");
            jdbcContext.unbind("readonly");
            envContext.destroySubcontext("jdbc");
            
            Context jmsContext = (Context) envContext.lookup("jms");
            jmsContext.unbind("queue/orders");
            jmsContext.unbind("topic/notifications");
            envContext.destroySubcontext("jms");
            
            // Unbind environment entries
            envContext.unbind("app/name");
            envContext.unbind("app/version");
            envContext.unbind("app/debug");
            envContext.unbind("database/pool/maxConnections");
            envContext.unbind("database/pool/timeout");
            
            // Destroy contexts
            compContext.destroySubcontext("env");
            javaContext.destroySubcontext("comp");
            rootContext.destroySubcontext("java:");
            
            System.out.println("✓ JNDI environment cleaned up");
            
        } catch (NameNotFoundException e) {
            // Some resources might already be cleaned up
        }
        
        rootContext.close();
    }
}