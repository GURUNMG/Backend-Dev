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

# Builder Pattern (GoF Creational Pattern)

---

# 1. Definition

> **Builder Pattern is a Creational Design Pattern that constructs a complex object step-by-step while separating the construction process from the object's representation.**

Unlike a constructor, a Builder allows us to create an object gradually by setting only the required fields before finally calling `build()`.

---

# 2. Why was Builder Pattern introduced?

## Problem Before Builder

Suppose we have a `User` class.

```java
public class User {

    private String name;
    private String email;
    private int age;
    private String city;
    private String country;

}
```

Using constructors:

```java
User user = new User(
    "Guru",
    "guru@gmail.com",
    24,
    "Chennai",
    "India"
);
```

As the number of fields increases:

- Constructor becomes difficult to read.
- Easy to swap parameters accidentally.
- Difficult to know which parameter represents what.

This is called the **Telescoping Constructor Problem**.

---

# 3. Problems with Telescoping Constructors

Example

```java
User user = new User(
        "Guru",
        "guru@gmail.com",
        24,
        "Chennai",
        "India",
        "9876543210",
        "Male",
        true,
        LocalDate.now()
);
```

Problems

- Poor readability
- Hard to maintain
- Parameter order mistakes
- Constructor explosion
- Difficult to support optional fields

---

# 4. JavaBeans Approach

Instead of constructors

```java
User user = new User();

user.setName("Guru");
user.setEmail("guru@gmail.com");
user.setAge(24);
```

Advantages

- Easy to read
- Supports optional fields

Disadvantages

- Mutable object
- Object may remain incomplete
- No guarantee object is valid
- Not thread-safe by default

---

# 5. Builder Pattern Solution

```java
User user = User.builder()
        .name("Guru")
        .email("guru@gmail.com")
        .age(24)
        .city("Chennai")
        .build();
```

Advantages

- Fluent API
- Easy to read
- Supports optional fields
- Validation before creation
- Can create immutable objects

---

# 6. Structure

```
Client
   │
   ▼
Builder
   │
Stores values
   │
   ▼
build()
   │
Creates object
   │
   ▼
Product
```

---

# 7. Components

## Product

The object being created.

```java
public class User {

}
```

---

## Builder

Collects values.

```java
User.Builder
```

---

## Fluent Methods

```java
.name()

.email()

.age()
```

Each returns

```java
return this;
```

for method chaining.

---

## build()

Creates the final object.

```java
public User build() {

    return new User(this);

}
```

---

# 8. Complete Manual Implementation

```java
public class User {

    private final String name;
    private final String email;
    private final int age;

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String email;
        private int age;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
```

---

# 9. Runtime Flow

```
User.builder()

↓

Builder Object Created

↓

name()

↓

email()

↓

age()

↓

build()

↓

User Object Created
```

---

# 10. Why return `this`?

```java
public Builder name(String name) {

    this.name = name;

    return this;

}
```

`this` refers to the current Builder object.

Returning `this` enables method chaining.

```java
User.builder()
    .name(...)
    .email(...)
    .age(...)
```

---

# 11. Why is Builder a Static Nested Class?

```java
public static class Builder
```

Without `static`

```java
User.Builder builder =
        user.new Builder();
```

A `User` object would be required before creating the Builder.

But the Builder itself is responsible for creating the `User`.

Using `static` removes this circular dependency.

---

# 12. Does `this` work inside a static nested Builder?

Yes.

`static` applies to the **Builder class**, not its instances.

```java
Builder builder = new Builder();
```

The Builder object is a normal object.

Inside

```java
this.name = name;
```

`this` refers to the Builder instance.

---

# 13. Mandatory vs Optional Fields

Suppose

```
name   → Mandatory

email  → Mandatory

age    → Optional

city   → Optional
```

Approach 1

```java
new Builder()
```

Validate later.

Approach 2

```java
new Builder(name,email)
```

Mandatory values enforced during Builder creation.

Preferred for domain models.

---

# 14. Validation

Validation should occur before object creation.

```java
public User build() {

    validate();

    return new User(this);

}
```

Example

```java
private void validate() {

    if(name == null)
        throw new IllegalStateException();

    if(email == null)
        throw new IllegalStateException();

}
```

This guarantees that invalid objects are never created.

---

# 15. Why validate inside build()?

Because only `build()` has access to the complete Builder state.

Example

```
password

confirmPassword
```

Validation requires both values.

`build()` is the ideal place.

---

# 16. builder() Method

Instead of

```java
new User.Builder()
```

Most libraries expose

```java
User.builder()
```

Implementation

```java
public static Builder builder() {

    return new Builder();

}
```

Advantages

- Cleaner API
- Hides Builder implementation
- Easier maintenance
- Consistent with Java libraries

---

# 17. Builder with Lombok

Simple data class

```java
@Getter
@Builder
public class User {

    private final String name;
    private final String email;
    private final int age;

}
```

Lombok generates

- Builder class
- builder()
- build()
- Fluent methods

---

# 18. Constructor-Level @Builder

Recommended when validation is required.

```java
@Getter
public class User {

    private final String name;
    private final String email;
    private final int age;

    @Builder
    public User(String name,
                String email,
                int age) {

        if(name == null)
            throw new IllegalArgumentException();

        if(email == null)
            throw new IllegalArgumentException();

        this.name = name;
        this.email = email;
        this.age = age;

    }

}
```

Builder calls the constructor.

Constructor performs validation.

---

# 19. Does Lombok Validate?

No.

```java
User.builder()
    .build();
```

Compiles successfully.

Validation must be implemented by

- Constructor
- Bean Validation
- Custom build() logic

---

# 20. How Lombok Works

Lombok is an **Annotation Processor**.

During compilation it generates

- Builder class
- builder()
- build()
- Fluent setter methods

It does **not** use reflection.

---

# 21. toBuilder()

```java
@Builder(toBuilder = true)
```

Allows

```java
User updated =
        user.toBuilder()
            .city("Bangalore")
            .build();
```

Useful for immutable objects.

Creates a new object while copying existing values.

---

# 22. Step Builder Pattern

Purpose

Move validation from runtime to compile time.

Construction flow

```
builder()

↓

name()

↓

email()

↓

optional fields

↓

build()
```

Implemented using multiple interfaces.

Advantages

- Compile-time safety
- Mandatory field enforcement
- Prevents invalid construction order

Disadvantages

- More boilerplate
- Rarely used in typical Spring Boot applications

---

# 23. Builder vs Factory

| Builder | Factory |
|----------|----------|
| How to build an object | Which object to create |
| Step-by-step construction | Immediate creation |
| Handles optional fields | Chooses implementation |
| Usually one product | May return different products |

---

# 24. Builder vs JavaBeans vs Constructors

| Feature | Constructor | JavaBeans | Builder |
|----------|-------------|-----------|----------|
| Readability | ❌ | ✅ | ✅ |
| Optional Fields | ❌ | ✅ | ✅ |
| Immutable | ✅ | ❌ | ✅ |
| Validation | Limited | Difficult | Excellent |
| Constructor Explosion | ❌ | ✅ | ✅ |

---

# 25. Real-World Examples

Java

```java
HttpRequest.newBuilder()
```

Spring

```java
ResponseEntity.ok()
```

AWS SDK

```java
PutObjectRequest.builder()
```

Lombok

```java
User.builder()
```

Elasticsearch

```java
SearchRequest.builder()
```

---

# 26. Advantages

- Eliminates telescoping constructors
- Fluent API
- Supports optional fields
- Validation before object creation
- Supports immutable objects
- Easy to read
- Easy to maintain

---

# 27. Disadvantages

- Additional Builder class
- More code without Lombok
- Slight overhead for very small objects
- Step Builder introduces significant boilerplate

---

# 28. When to Use

Use Builder when

- Object has many optional fields
- Constructor becomes unreadable
- Creating immutable objects
- Validation is required before creation
- Fluent API improves readability

Avoid Builder when

- Object has only two or three mandatory fields
- A constructor or static factory method is sufficient

---

# 29. Interview Questions

### What problem does Builder solve?

The telescoping constructor problem by allowing step-by-step object construction.

---

### Why use Builder instead of setters?

Builder supports immutable objects, centralized validation, and prevents partially initialized objects from being exposed.

---

### Why is the Builder class static?

To allow Builder creation without first creating the Product object.

---

### Why validate inside build()?

Because only `build()` has the complete object state required for validation.

---

### Does Lombok @Builder perform validation?

No.

It only generates Builder code.

---

### Does Lombok use reflection?

No.

It uses annotation processing during compilation.

---

### What is Step Builder?

An advanced Builder variation that uses Java interfaces to enforce mandatory construction steps at compile time.

---

### Difference between Builder and Factory?

Factory decides **which object** to create.

Builder decides **how an object should be constructed**.

---

# 30. Interview Ready Definition

> **The Builder Pattern is a creational design pattern that constructs complex objects step by step while separating object construction from object representation. It solves the telescoping constructor problem, provides a fluent API, supports optional parameters, centralizes validation, and is widely used for creating immutable objects in modern Java applications.**

# Strategy Pattern (GoF Behavioral Design Pattern)

---

# 1. Definition

> **The Strategy Pattern is a Behavioral Design Pattern that defines a family of algorithms, encapsulates each algorithm into a separate class, and makes them interchangeable at runtime without changing the client code.**

The Strategy Pattern focuses on **how an operation is performed** rather than **which object is created**.

---

# 2. Why was Strategy Pattern introduced?

Consider an E-Commerce application.

Initially, only Card payment is supported.

```java
public class OrderService {

    public void checkout(double amount) {

        // Card Payment Logic

    }

}
```

After a few months:

- UPI
- PayPal
- Net Banking
- Apple Pay

are introduced.

Now the code becomes

```java
if(paymentType.equals("CARD")){

}
else if(paymentType.equals("UPI")){

}
else if(paymentType.equals("PAYPAL")){

}
```

Eventually

```java
checkout()
```

becomes hundreds of lines long.

---

# 3. Problems Before Strategy

## Problem 1

Large if-else / switch statements

---

## Problem 2

Violates Open/Closed Principle

Every new payment method requires modifying the existing class.

---

## Problem 3

Violates Single Responsibility Principle

One class contains every payment algorithm.

---

## Problem 4

Hard to Test

Individual algorithms cannot be tested independently.

---

## Problem 5

Poor Maintainability

Adding new algorithms increases complexity.

---

# 4. Solution

Move every algorithm into its own class.

Instead of

```
OrderService

↓

if(Card)

↓

if(UPI)

↓

if(PayPal)
```

Use

```
OrderService

↓

PaymentStrategy

↓

Card

UPI

PayPal
```

Each algorithm becomes a separate class.

---

# 5. Structure

```
                Client

                   │

                   ▼

              Context

                   │

                   ▼

            Strategy Interface

         ▲        ▲        ▲

         │        │        │

      Card      UPI     PayPal
```

---

# 6. Components

## Strategy

Common interface

```java
public interface PaymentStrategy {

    void pay(double amount);

}
```

---

## Concrete Strategy

Implements one algorithm.

```java
public class CardPaymentStrategy
        implements PaymentStrategy {

    @Override
    public void pay(double amount) {

        System.out.println("Card Payment");

    }

}
```

---

Another strategy

```java
public class UpiPaymentStrategy
        implements PaymentStrategy {

    @Override
    public void pay(double amount) {

        System.out.println("UPI Payment");

    }

}
```

---

## Context

Uses the strategy.

```java
public class OrderService {

    private final PaymentStrategy paymentStrategy;

    public OrderService(
            PaymentStrategy paymentStrategy
    ) {

        this.paymentStrategy = paymentStrategy;

    }

    public void checkout(double amount) {

        paymentStrategy.pay(amount);

        System.out.println("Order Completed");

    }

}
```

---

## Client

Chooses the strategy.

```java
PaymentStrategy strategy =
        new CardPaymentStrategy();

OrderService service =
        new OrderService(strategy);

service.checkout(1000);
```

---

# 7. Runtime Flow

```
Client

↓

Creates Strategy

↓

Creates Context

↓

checkout()

↓

paymentStrategy.pay()

↓

Concrete Strategy Executes
```

---

# 8. Runtime Memory

```
Heap

+----------------------+
| CardPaymentStrategy  |
+----------------------+
          ▲
          │
+----------------------+
| OrderService         |
| paymentStrategy -----+
+----------------------+
```

OrderService **has a** PaymentStrategy.

This is **Composition**.

---

# 9. Why Composition?

Bad

```
OrderService extends CardPaymentStrategy
```

OrderService is NOT a payment strategy.

Good

```
OrderService HAS A PaymentStrategy
```

This follows

> **Favor Composition Over Inheritance**

---

# 10. SOLID Principles Used

## Single Responsibility Principle (SRP)

Each strategy has one responsibility.

```
CardPaymentStrategy

↓

Only Card Logic
```

---

## Open/Closed Principle (OCP)

New strategies can be added without modifying existing code.

```
ApplePaymentStrategy

↓

No change to OrderService
```

---

## Dependency Inversion Principle (DIP)

Context depends on

```
PaymentStrategy
```

instead of

```
CardPaymentStrategy
```

---

# 11. Strategy + Factory

Strategy answers

```
How should the algorithm execute?
```

Factory answers

```
Which strategy should I return?
```

Example

```java
PaymentStrategy strategy =
        factory.getStrategy("CARD");
```

Then

```java
strategy.pay(amount);
```

Responsibilities remain separated.

---

# 12. Factory Implementation

```java
public class PaymentStrategyFactory {

    public PaymentStrategy getStrategy(
            String paymentType
    ) {

        if(paymentType.equalsIgnoreCase("CARD")) {
            return new CardPaymentStrategy();
        }

        if(paymentType.equalsIgnoreCase("UPI")) {
            return new UpiPaymentStrategy();
        }

        throw new IllegalArgumentException();

    }

}
```

---

# 13. Factory + Strategy Runtime Flow

```
Client

↓

Factory

↓

Returns Strategy

↓

OrderService

↓

pay()
```

Factory creates.

Strategy executes.

---

# 14. Advantages

- Eliminates large if-else statements
- Supports runtime algorithm selection
- Follows Open/Closed Principle
- Easy to test individual algorithms
- Easy to extend
- Promotes Composition over Inheritance
- Reduces coupling

---

# 15. Disadvantages

- More classes
- More interfaces
- Slightly higher complexity
- Overkill for simple applications

---

# 16. When to Use

Use Strategy when

- Multiple algorithms solve the same problem
- Algorithms may grow over time
- Runtime selection is required
- Different implementations are independent
- Frequent business changes are expected

Examples

- Payment Methods
- Notification Channels
- Authentication
- Compression Algorithms
- Tax Calculation
- Shipping Methods
- Discount Calculation

---

# 17. When NOT to Use

## Only one algorithm exists

```
Only Card Payment
```

No Strategy required.

---

## Algorithm never changes

Example

```
Rectangle Area
```

Simple method is enough.

---

## Tiny if-else

```java
if(age >= 18)
```

No Strategy required.

---

## No runtime selection

Always

```
Card Payment
```

No Strategy required.

---

## Over-engineering

Avoid creating

```
AnimalStrategy

DogStrategy

CatStrategy
```

when there is no real variation.

---

# 18. Strategy vs Factory

| Strategy | Factory |
|-----------|----------|
| Defines algorithms | Creates objects |
| Focuses on behavior | Focuses on object creation |
| Executes logic | Returns implementation |
| Runtime behavior | Object instantiation |

---

# 19. Strategy vs State

| Strategy | State |
|-----------|-------|
| Client chooses algorithm | Object changes behavior based on internal state |
| Behavior selected externally | Behavior changes automatically |
| Independent algorithms | State transitions |

---

# 20. Strategy vs Template Method

| Strategy | Template Method |
|-----------|-----------------|
| Uses composition | Uses inheritance |
| Runtime algorithm selection | Compile-time customization |
| Flexible | Fixed algorithm structure |

---

# 21. UML Diagram

```
                  +----------------------+
                  |   PaymentStrategy    |
                  +----------------------+
                  | + pay(amount)        |
                  +----------▲-----------+
                             |
         +-------------------+-------------------+
         |                   |                   |
         |                   |                   |
+----------------+ +----------------+ +----------------+
| CardStrategy   | | UpiStrategy    | | PaypalStrategy |
+----------------+ +----------------+ +----------------+
| pay()          | | pay()          | | pay()          |
+----------------+ +----------------+ +----------------+

                  used by

                      ▲

                      |

            +----------------------+
            |    OrderService      |
            +----------------------+
            | paymentStrategy      |
            | checkout()           |
            +----------------------+
```

---

# 22. Real-World Examples

- Payment Gateway
- Notification Service
- Shipping Provider
- Tax Calculation
- Discount Engine
- File Compression
- Image Processing
- Encryption Algorithms

---

# 23. Common Mistakes

❌ Replacing every if-else with Strategy

❌ Using Strategy when only one algorithm exists

❌ Creating unnecessary interfaces

❌ Mixing object creation with algorithm execution

---

# 24. Interview Questions

### What problem does Strategy solve?

It removes large conditional logic by encapsulating interchangeable algorithms into separate classes that can be selected at runtime.

---

### Which SOLID principles are used?

- SRP
- OCP
- DIP

---

### Why use composition instead of inheritance?

OrderService **has a** PaymentStrategy.

It **is not a** PaymentStrategy.

---

### Why combine Factory and Strategy?

Factory decides **which strategy** to create.

Strategy defines **how the work is performed**.

---

### What is the biggest disadvantage?

It introduces more classes and abstraction, making it unnecessary for simple, stable problems.

---

### When should Strategy be avoided?

When there is only one algorithm, the logic is simple and stable, or runtime selection is not required.

---

# 25. Interview Ready Definition

> **The Strategy Pattern is a Behavioral Design Pattern that encapsulates a family of algorithms into separate classes and makes them interchangeable at runtime. It eliminates large conditional statements, promotes composition over inheritance, follows the Open/Closed Principle, and allows applications to add new behaviors without modifying existing client code.**

