# Day 8 — Design Patterns
# Topic 1: Singleton Pattern

---

# 1. What is a Design Pattern?

A **Design Pattern** is a **proven, reusable solution** to a commonly occurring software design problem.

- It is **not a library**.
- It is **not a framework**.
- It is **not a programming language feature**.
- It is a **design idea or blueprint**.

Just as Binary Search solves searching efficiently, the Singleton pattern solves the problem of ensuring that only one instance of a class exists.

---

# 2. What is Singleton?

## Definition

> Singleton ensures that only one instance of a class exists throughout the application's lifetime and provides a global access point to it.

Two key characteristics:

- Only one object is created.
- Everyone accesses the same object.

---

# 3. Why was Singleton introduced?

Without Singleton:

```java
Logger logger1 = new Logger();
Logger logger2 = new Logger();
Logger logger3 = new Logger();
```

Problems:

- Multiple objects consume unnecessary memory.
- Shared state becomes inconsistent.
- Multiple file handles or DB connections may be created.
- Configuration may differ between instances.

Singleton ensures:

```text
Application
     │
     ▼
 Singleton Instance
     │
 ┌───┴───┐
 │       │
User A User B User C
```

Everyone shares the same object.

---

# 4. Real-world Analogy

Think of a country's Prime Minister.

- Only one Prime Minister exists.
- Every citizen interacts with the same Prime Minister.

Similarly, Singleton ensures that only one object exists for the entire application.

---

# 5. Singleton Architecture

Normal Object Creation

```text
Client
   │
 new Object()
   │
 Heap
```

Singleton

```text
Client
   │
getInstance()
   │
Is object created?
   │
 ├── No → Create Object
 └── Yes → Return Existing Object
```

---

# 6. Basic Lazy Singleton

```java
public class Singleton {

    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {

        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }
}
```

---

# 7. Why each keyword?

## private constructor

```java
private Singleton() {}
```

Prevents external object creation.

Invalid:

```java
new Singleton(); // Compile Error
```

---

## static instance

```java
private static Singleton instance;
```

Static belongs to the class, not objects.

Only one copy exists.

---

## static getInstance()

```java
public static Singleton getInstance()
```

Since no object exists initially, we call:

```java
Singleton.getInstance();
```

instead of

```java
obj.getInstance();
```

---

## Lazy Initialization

Object is created only when requested.

```java
if(instance == null)
```

Benefits:

- Saves memory
- Delays object creation until needed

---

# 8. Execution Flow

First Call

```java
Singleton.getInstance();
```

Flow:

```text
instance == null

↓

Create Object

↓

Store Reference

↓

Return Object
```

Second Call

```java
Singleton.getInstance();
```

Flow:

```text
instance already exists

↓

Return Same Object
```

No new object is created.

---

# 9. Heap Representation

Initially

```text
instance

↓

null
```

After first call

```text
instance

↓

+----------------+
| Singleton Obj  |
+----------------+
```

All future calls return this object.

---

# 10. Verifying Singleton

```java
Singleton s1 = Singleton.getInstance();
Singleton s2 = Singleton.getInstance();

System.out.println(s1 == s2);
```

Output

```text
true
```

Both references point to the same object.

---

# 11. Problem with Basic Singleton

The basic implementation is **not thread-safe**.

Example:

Thread A

```text
instance == null
```

Before creating the object, CPU switches.

Thread B

```text
instance == null
```

Also creates an object.

Result:

```text
Object 1
Object 2
```

Singleton is broken.

---

# 12. Synchronized Singleton

```java
public static synchronized Singleton getInstance() {

    if(instance == null){
        instance = new Singleton();
    }

    return instance;
}
```

Advantages:

- Thread-safe

Disadvantages:

- Synchronization occurs on every call.
- Performance overhead.

---

# 13. Eager Initialization

```java
public class Singleton {

    private static final Singleton INSTANCE = new Singleton();

    private Singleton(){}

    public static Singleton getInstance(){
        return INSTANCE;
    }
}
```

Advantages:

- Simple
- Thread-safe
- No synchronization

Disadvantages:

- Object created even if never used.

---

# 14. Double Checked Locking

```java
public class Singleton {

    private static volatile Singleton instance;

    private Singleton(){}

    public static Singleton getInstance(){

        if(instance == null){

            synchronized (Singleton.class){

                if(instance == null){
                    instance = new Singleton();
                }

            }
        }

        return instance;
    }
}
```

Benefits:

- Lazy initialization
- Thread-safe
- Synchronization only during first creation
- High performance after initialization

---

# 15. Why volatile?

Without `volatile`, JVM may reorder instructions.

Expected order:

```text
1. Allocate memory
2. Initialize object
3. Assign reference
```

Possible reordered execution:

```text
1. Allocate memory
2. Assign reference
3. Initialize object
```

Another thread may receive a reference to a partially initialized object.

`volatile` prevents instruction reordering and guarantees visibility.

---

# 16. Bill Pugh Singleton

```java
public class Singleton {

    private Singleton(){}

    private static class Holder{
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance(){
        return Holder.INSTANCE;
    }
}
```

Advantages:

- Lazy initialization
- Thread-safe
- No synchronization overhead
- Relies on JVM class loading guarantees

Preferred lazy implementation.

---

# 17. Enum Singleton

```java
public enum Singleton {

    INSTANCE;

    public void doWork(){
        System.out.println("Working");
    }
}
```

Usage

```java
Singleton.INSTANCE.doWork();
```

Advantages:

- Thread-safe
- Serialization-safe
- Reflection-safe (in normal usage)
- Simplest implementation

---

# 18. Production Use Cases

Singleton is commonly used for:

- Logger
- Configuration Manager
- Cache Manager
- Thread Pool Manager
- Connection Pool Manager
- Metrics Collector

---

# 19. Spring and Singleton

Spring's default bean scope is:

```text
singleton
```

Important distinction:

**Java Singleton**

- One instance for the entire JVM (as implemented by the pattern).

**Spring Singleton**

- One instance per Spring IoC Container.

Spring manages singleton beans using its container rather than requiring manual Singleton implementations.

---

# 20. Advantages

- Controlled object creation.
- Saves memory.
- Global access point.
- Consistent shared state.
- Useful for shared resources.

---

# 21. Disadvantages

- Global state can make testing harder.
- Tight coupling.
- Harder to mock in unit tests.
- Can become a hidden dependency if overused.

---

# 22. Common Mistakes

❌ Public constructor

```java
public Singleton(){}
```

Allows multiple objects.

---

❌ Missing `static`

```java
Singleton instance;
```

Each object would have its own instance variable.

---

❌ Ignoring thread safety

Basic lazy implementation is unsafe in multithreaded environments.

---

❌ Overusing Singleton

Not every shared service should be a Singleton. Prefer dependency injection where appropriate.

---

# 23. Interview Questions

### Q1. What is Singleton?

Ensures only one object exists and provides a global access point.

---

### Q2. Why is the constructor private?

To prevent external instantiation.

---

### Q3. Why is getInstance() static?

Because no object exists initially, so it must be callable through the class.

---

### Q4. Difference between Lazy and Eager Singleton?

| Lazy | Eager |
|------|-------|
| Created on first request | Created during class loading |
| Saves memory | Simpler implementation |
| Requires thread-safety handling | Thread-safe by default |

---

### Q5. Why use volatile?

To prevent instruction reordering and ensure visibility across threads.

---

### Q6. Which implementation is preferred?

- **Bill Pugh Singleton** for lazy initialization.
- **Enum Singleton** for maximum robustness and simplicity.

---

# 24. Cheat Sheet

| Implementation | Thread Safe | Lazy | Synchronization | Recommended |
|----------------|------------|------|-----------------|-------------|
| Basic Lazy | ❌ | ✅ | ❌ | No |
| Synchronized | ✅ | ✅ | Every call | Rarely |
| Eager | ✅ | ❌ | No | Sometimes |
| Double Checked Locking | ✅ | ✅ | First creation only | Yes |
| Bill Pugh | ✅ | ✅ | No | Highly Recommended |
| Enum Singleton | ✅ | Depends on enum initialization | No | Highly Recommended |

---

# Key Takeaways

- Singleton ensures only one object exists.
- Constructor must be private.
- Object is accessed through a static `getInstance()` method.
- Basic lazy Singleton is **not thread-safe**.
- `volatile` prevents instruction reordering in Double Checked Locking.
- Bill Pugh is the preferred lazy implementation.
- Enum Singleton is the safest and simplest implementation.
- Spring uses singleton-scoped beans by default but manages them through the IoC container.