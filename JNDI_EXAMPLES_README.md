# JNDI Examples - Java Naming and Directory Interface

This directory contains comprehensive examples demonstrating JNDI (Java Naming and Directory Interface) concepts and usage patterns in Java applications.

## Overview

JNDI is a Java API that provides naming and directory functionality to applications written in Java. It's commonly used in enterprise applications for:

- **Resource Lookup**: Finding DataSources, JMS destinations, EJBs
- **Configuration Management**: Accessing environment entries and properties
- **Service Location**: Locating distributed services and components
- **Directory Services**: Accessing LDAP, DNS, and other directory services

## Examples Included

### 1. JndiExample.java
**Basic JNDI Operations and Concepts**

Demonstrates fundamental JNDI operations:
- Creating and using InitialContext
- Binding and unbinding objects
- Looking up objects by name
- Working with subcontexts
- Context configuration and environment properties

**Key Learning Points:**
- Basic JNDI API usage
- Hierarchical naming structure
- Context lifecycle management
- Error handling patterns

**Run:** `java com.akfc.training.misc.JndiExample`

### 2. JndiDataSourceExample.java
**Enterprise JNDI Usage - DataSource and Resource Management**

Shows how JNDI is used in enterprise applications:
- Setting up enterprise JNDI environment (java:comp/env namespace)
- DataSource lookup for database connections
- Environment entries for configuration
- JMS resource management
- Service integration patterns

**Key Learning Points:**
- Standard Java EE naming conventions
- Resource injection patterns
- Enterprise application structure
- Configuration management
- Service layer integration

**Run:** `java com.akfc.training.misc.JndiDataSourceExample`

### 3. JndiUtils.java
**JNDI Utility Library and Best Practices**

Provides a utility library with common JNDI operations:
- Thread-safe context management
- Cached lookups for performance
- Safe lookup methods with error handling
- Automatic subcontext creation
- Standard naming pattern support
- Environment entry convenience methods

**Key Learning Points:**
- Production-ready JNDI utilities
- Performance optimization techniques
- Thread safety considerations
- Error handling strategies
- Code reusability patterns

**Run:** `java com.akfc.training.misc.JndiUtils`

## Technical Implementation

### In-Memory Context Implementation
Since these examples are designed to run without external JNDI providers, they include a custom `SimpleInMemoryContext` implementation that:

- Supports hierarchical naming (subcontexts)
- Handles compound names with colons and slashes
- Provides thread-safe operations
- Implements the full Context interface

### Key Features Demonstrated

1. **Naming Patterns**
   - Standard Java EE naming: `java:comp/env/...`
   - Hierarchical structure: `departments/engineering/manager`
   - Resource-specific patterns: `jdbc/primary`, `jms/queue/orders`

2. **Resource Types**
   - DataSources for database connections
   - Environment entries for configuration
   - JMS destinations for messaging
   - Custom business objects

3. **Best Practices**
   - Proper resource cleanup
   - Error handling and fallbacks
   - Performance optimization with caching
   - Thread-safe implementations

## Enterprise Usage Patterns

### DataSource Lookup
```java
// Typical enterprise pattern
DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/primary");
Connection conn = ds.getConnection();
```

### Configuration Management
```java
// Environment entries for configuration
String appName = (String) context.lookup("java:comp/env/app/name");
String dbUrl = (String) context.lookup("java:comp/env/database/url");
```

### Service Integration
```java
// Service layer using JNDI
public class DatabaseService {
    private Context jndiContext;
    
    public Connection getConnection(String dsName) throws Exception {
        DataSource ds = (DataSource) jndiContext.lookup(dsName);
        return ds.getConnection();
    }
}
```

## Learning Objectives

After studying these examples, you should understand:

1. **JNDI Fundamentals**
   - What JNDI is and why it's used
   - Basic API operations (bind, lookup, unbind)
   - Context hierarchy and naming

2. **Enterprise Integration**
   - How JNDI fits into Java EE applications
   - Standard naming conventions
   - Resource management patterns

3. **Best Practices**
   - Thread-safe context usage
   - Performance optimization
   - Error handling strategies
   - Resource cleanup

4. **Practical Applications**
   - Database connection management
   - Configuration externalization
   - Service location patterns
   - Dependency injection concepts

## Real-World Applications

JNDI is extensively used in:

- **Application Servers**: Tomcat, JBoss, WebLogic, WebSphere
- **Spring Framework**: JNDI template and integration
- **Java EE Applications**: EJB, JPA, JMS integration
- **Microservices**: Service discovery and configuration
- **Legacy System Integration**: Connecting to mainframes and directories

## Next Steps

To deepen your JNDI knowledge:

1. Study the source code of each example
2. Run the examples and observe the output
3. Modify the examples to add new resources
4. Integrate JNDI with a real application server
5. Explore LDAP integration for directory services
6. Learn about Spring's JNDI support and templates

## Dependencies

These examples use only standard Java libraries:
- `javax.naming.*` - Core JNDI API
- `javax.sql.DataSource` - Database connection interface
- `java.util.concurrent.*` - Thread-safe collections

No external dependencies are required, making them easy to run and understand.