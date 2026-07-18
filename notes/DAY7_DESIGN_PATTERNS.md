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

# 7. Why private constructor?

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

# Day 8 — Design Patterns
# Factory Pattern Notes

---

# 1. Why was the Factory Pattern introduced?

Before Factory Pattern, object creation was done directly using the `new` keyword.

Example:

```java
Payment payment = new CreditCardPayment();
```

Although this works, it creates **tight coupling** because the client knows:

- The concrete class
- The constructor
- How the object is created

If the implementation changes, every client must also change.

Example:

```java
CreditCardPayment
```

becomes

```java
StripePayment
```

Every occurrence of

```java
new CreditCardPayment()
```

must be modified.

---

# 2. What is the Factory Pattern?

## Definition

> Factory Pattern is a creational design pattern that delegates object creation to a factory instead of allowing clients to instantiate objects directly.

The client requests an object.

The factory decides **which implementation** should be created.

---

# 3. What Problem Does Factory Pattern Solve?

Factory Pattern mainly solves:

- Tight Coupling
- Duplicate object creation logic
- Poor maintainability
- Difficult testing
- Violation of the Open/Closed Principle

Instead of:

```java
new CreditCardPayment();
```

Client writes:

```java
Payment payment =
        PaymentFactory.create("CARD");
```

The client depends only on the abstraction.

---

# 4. Architecture

Without Factory

```text
Client

↓

new CreditCardPayment()
```

With Factory

```text
Client

↓

Factory

↓

Creates Appropriate Object

↓

Returns Interface
```

---

# 5. Interview Definition

> Factory Pattern is a creational design pattern that centralizes object creation by delegating instantiation to a factory, reducing coupling between clients and concrete implementations while improving extensibility and maintainability.

---

# 6. Types of Factory Patterns

1. Simple Factory (Not GoF)
2. Factory Method (GoF)
3. Abstract Factory (GoF)

---

# 7. Simple Factory Pattern

## Definition

A Simple Factory is a class responsible for creating objects based on input and returning the appropriate implementation.

Example:

```java
Payment payment =
        PaymentFactory.create("UPI");
```

The client does not use `new`.

The factory creates the object.

---

## Structure

```text
Client

↓

PaymentFactory

↓

Card
UPI
PayPal
```

---

## Components

### Product Interface

```java
public interface Payment {

    void pay();

}
```

---

### Concrete Products

```text
CreditCardPayment

UpiPayment

PaypalPayment
```

---

### Factory

```java
public class PaymentFactory {

    public static Payment create(String type){

        if(type.equals("CARD"))
            return new CreditCardPayment();

        if(type.equals("UPI"))
            return new UpiPayment();

        if(type.equals("PAYPAL"))
            return new PaypalPayment();

        throw new IllegalArgumentException();
    }

}
```

---

## Advantages

- Centralized object creation
- Loose coupling
- Easier maintenance
- Less duplicate code
- Programming to interfaces

---

## Disadvantages

The factory grows as more products are added.

Every new product requires modifying the factory.

Violates the Open/Closed Principle.

---

## Is Simple Factory a GoF Pattern?

No.

Simple Factory is a design technique.

It is **not** one of the 23 Gang of Four patterns.

---

# 8. Factory Method Pattern (GoF)

## Definition

> Factory Method defines an interface for creating objects while allowing subclasses to decide which concrete object to instantiate.

Instead of one large factory,

every product gets its own factory.

---

## Structure

```text
PaymentFactory

▲

CardFactory

UpiFactory

PaypalFactory
```

Each factory creates only one product.

---

## Product Hierarchy

```text
Payment

▲

Card

UPI

PayPal
```

---

## Factory Hierarchy

```text
PaymentFactory

▲

CardFactory

UpiFactory

PaypalFactory
```

---

## Factory Interface

```java
public interface PaymentFactory {

    Payment createPayment();

}
```

---

## Concrete Factory

```java
public class CardFactory
        implements PaymentFactory {

    @Override
    public Payment createPayment() {
        return new CreditCardPayment();
    }

}
```

---

## Client

```java
PaymentFactory factory =
        new CardFactory();

Payment payment =
        factory.createPayment();

payment.pay();
```

---

## Advantages

- Better scalability
- Open/Closed Principle
- No giant if-else
- Better separation of responsibilities
- Loose coupling

---

## Disadvantages

More classes.

One factory per product.

---

# 9. Factory Method vs Simple Factory

| Simple Factory | Factory Method |
|----------------|----------------|
| One factory | Multiple factories |
| Uses if/switch | Uses inheritance |
| Factory modified for new products | Add a new factory |
| Not GoF | Official GoF Pattern |

---

# 10. Abstract Factory Pattern

## Definition

> Abstract Factory provides an interface for creating families of related objects without specifying their concrete classes.

Unlike Factory Method,

it creates **multiple related products**.

---

## Product Family Example

Windows

- WindowsButton
- WindowsCheckbox

Mac

- MacButton
- MacCheckbox

Each family is compatible.

---

## Factory Interface

```java
public interface GUIFactory {

    Button createButton();

    Checkbox createCheckbox();

}
```

---

## Windows Factory

```java
public class WindowsFactory
        implements GUIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }

}
```

---

## Client

```java
GUIFactory factory =
        new WindowsFactory();

Button button =
        factory.createButton();

Checkbox checkbox =
        factory.createCheckbox();
```

---

## Architecture

```text
GUIFactory

▲

WindowsFactory

MacFactory

↓

Creates

Button

Checkbox
```

---

## Advantages

- Creates compatible product families
- Open/Closed Principle
- Loose coupling
- Easy platform switching
- Centralized family creation

---

## Disadvantages

Large number of classes.

Adding a new product type requires updating every factory.

---

# 11. Factory Method vs Abstract Factory

| Factory Method | Abstract Factory |
|----------------|------------------|
| Creates one product | Creates a family of products |
| One creation method | Multiple creation methods |
| One factory per product | One factory per product family |
| Focuses on one object | Focuses on compatible objects |

---

# 12. Overall Comparison

| Feature | Simple Factory | Factory Method | Abstract Factory |
|----------|----------------|----------------|------------------|
| GoF Pattern | ❌ | ✅ | ✅ |
| Number of Factories | One | Many | One per family |
| Products Created | One | One | Multiple |
| Uses if/switch | Yes | No | No |
| Uses Inheritance | No | Yes | Yes |
| Uses Polymorphism | Yes | Yes | Yes |
| Open/Closed Principle | Weak | Strong | Strong |
| Best Use Case | Small applications | Frequently adding product types | Creating compatible product families |

---

# 13. Production Examples

## Simple Factory

- Notification creation
- Payment gateway selection
- Parser selection

---

## Factory Method

- Spring Bean creation
- Logging framework implementations
- JDBC Driver loading
- Document exporters (PDF, Excel, Word)

---

## Abstract Factory

- Cross-platform GUI toolkits
- Database drivers
- Cloud provider SDKs
- Theme/UI frameworks

---

# 14. Interview Questions

### Q1. What problem does Factory Pattern solve?

It separates object creation from business logic, reducing tight coupling and improving maintainability.

---

### Q2. Why is using `new` everywhere a problem?

Because the client becomes tightly coupled to concrete implementations.

---

### Q3. Difference between Simple Factory and Factory Method?

Simple Factory uses one factory with conditional logic.

Factory Method uses specialized factories and inheritance.

---

### Q4. Difference between Factory Method and Abstract Factory?

Factory Method creates one object.

Abstract Factory creates an entire family of related objects.

---

### Q5. Is Simple Factory a GoF pattern?

No.

---

### Q6. Which Factory pattern follows the Open/Closed Principle best?

Factory Method and Abstract Factory.

---

# 15. Cheat Sheet

| Pattern | Creates | Best Use Case |
|----------|---------|---------------|
| Simple Factory | One Product | Small applications |
| Factory Method | One Product | Frequently adding new product types |
| Abstract Factory | Family of Products | Multiple compatible product families |

---

# Key Takeaways

- Factory Pattern centralizes object creation.
- Clients depend on interfaces rather than concrete classes.
- **Simple Factory** centralizes object creation in one class but is not an official GoF pattern.
- **Factory Method** delegates object creation to specialized factories and follows the Open/Closed Principle more closely.
- **Abstract Factory** creates families of related, compatible objects through a single factory.
- Spring, JDBC, and many enterprise frameworks apply these concepts to manage object creation and decouple client code from implementations.