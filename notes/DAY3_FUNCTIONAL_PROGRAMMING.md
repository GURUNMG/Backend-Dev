# Day 3 – Functional Programming

## Chapter 1 – Lambda Expressions (Interview Notes)

> **Topics Covered**
>
> * Why Lambda Expressions were introduced
> * Lambda Syntax
> * Functional Programming Basics
> * Target Typing
> * Variable Capture
> * `this` in Lambdas
> * JVM Implementation (`invokedynamic`)
> * Common Interview Questions

---

# Overview

Lambda Expressions were introduced in **Java 8** to bring **functional programming capabilities** into Java.

Instead of representing behavior using classes and anonymous inner classes, Java allows behavior to be represented as **objects** through lambda expressions.

Lambdas significantly reduce boilerplate code and form the foundation for the **Stream API**, **Functional Interfaces**, and modern Java programming.

---

# Why Were Lambda Expressions Introduced?

Before Java 8, behavior was represented by creating classes.

Example:

```java
Collections.sort(employees, new Comparator<Employee>() {

    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e1.getSalary(), e2.getSalary());
    }

});
```

Problems:

* Verbose code
* Boilerplate anonymous classes
* Difficult to compose behavior
* Harder to read and maintain

Java 8 introduced Lambda Expressions to represent behavior directly.

Equivalent code:

```java
employees.sort(
    (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())
);
```

The behavior remains the same while the syntax becomes much simpler.

---

# What is a Lambda Expression?

A Lambda Expression is an **anonymous function**.

Characteristics:

* No name
* No explicit return type
* No class declaration
* Can be passed as an argument
* Represents behavior

General Syntax

```java
(parameters) -> expression
```

or

```java
(parameters) -> {

    statements;

}
```

Example

```java
(a, b) -> a + b
```

This represents a function that accepts two values and returns their sum.

---

# Lambda Expressions are NOT Methods

One of the most common interview misconceptions.

This:

```java
(int x) -> x * x
```

is **not a method**.

It is an object representing behavior.

Example

```java
Function<Integer, Integer> square =
        x -> x * x;
```

The variable

```java
square
```

holds an object implementing the `Function` interface.

Execution happens through

```java
square.apply(5);
```

Output

```
25
```

---

# Lambda Expressions Represent Implementations

Suppose

```java
interface Printer {

    void print(String message);

}
```

Traditional approach

```java
class ConsolePrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.println(message);
    }

}
```

Using Lambda

```java
Printer printer =
        message -> System.out.println(message);
```

No class is explicitly created.

The JVM generates the implementation.

---

# Lambda Expressions Require Functional Interfaces

A Lambda Expression cannot exist independently.

Illegal

```java
x -> x + 1;
```

The compiler doesn't know which method this lambda is implementing.

Legal

```java
Runnable r =
        () -> System.out.println("Hello");
```

The compiler now knows

```
Runnable.run()
```

must be implemented.

Therefore,

**Every Lambda Expression must target a Functional Interface.**

---

# Target Typing

Java determines the lambda's type from its surrounding context.

Example

```java
Predicate<String> p =
        s -> s.isEmpty();
```

Compiler infers

```
Predicate<String>
```

contains

```java
boolean test(String value)
```

Therefore

```java
s -> s.isEmpty()
```

implements

```java
test()
```

This process is called

> **Target Typing**

---

# Lambda Syntax Variations

## No Parameters

```java
() -> System.out.println("Hello");
```

---

## Single Parameter

```java
x -> x * x
```

Parentheses are optional.

---

## Multiple Parameters

```java
(a, b) -> a + b
```

Parentheses become mandatory.

---

## Multiple Statements

```java
(a, b) -> {

    int sum = a + b;

    return sum;

}
```

Curly braces are required.

---

## Explicit Types

```java
(int a, int b) -> a + b
```

Usually unnecessary because Java performs type inference.

---

# Variable Capture

Example

```java
int bonus = 100;

Function<Integer, Integer> calculate =
        salary -> salary + bonus;
```

This works.

But

```java
bonus++;
```

causes a compilation error.

Reason

Local variables captured by Lambdas must be

* final

or

* effectively final

Example

```java
int x = 10;
```

If `x` is never reassigned,

Java treats it as effectively final.

---

# Why Must Captured Variables Be Effectively Final?

Local variables live inside the **stack frame**.

Example

```java
void calculate() {

    int x = 10;

    Runnable r =
            () -> System.out.println(x);

}
```

Once the method finishes,

its stack frame disappears.

The Lambda object may continue to exist.

Therefore,

the JVM copies the value of `x` into the Lambda object.

If Java allowed modifying the local variable afterward,

there would be ambiguity regarding which value the Lambda should observe.

Making captured variables effectively final eliminates this problem and avoids synchronization issues.

---

# Accessing Instance Variables

Unlike local variables,

instance variables reside on the Heap.

Example

```java
class Employee {

    int salary = 50000;

    void display() {

        Runnable r =
                () -> System.out.println(salary);

    }

}
```

This is perfectly valid.

The Lambda simply accesses the current object's field.

---

# Accessing Static Variables

Example

```java
class Demo {

    static int count = 10;

    void display() {

        Runnable r =
                () -> System.out.println(count);

    }

}
```

Static variables belong to the class,

so Lambdas can access them directly.

---

# `this` Inside a Lambda

A common interview question.

Example

```java
class Demo {

    void show() {

        Runnable r =
                () -> System.out.println(this);

    }

}
```

Inside a Lambda,

```
this
```

refers to the enclosing object.

In this case,

```
Demo
```

---

## Anonymous Inner Class

```java
Runnable r = new Runnable() {

    @Override
    public void run() {
        System.out.println(this);
    }

};
```

Here,

```
this
```

refers to the anonymous `Runnable` object itself.

---

## Interview Difference

| Lambda                            | Anonymous Class                   |
| --------------------------------- | --------------------------------- |
| Does not create a new `this`      | Creates its own `this`            |
| `this` refers to enclosing object | `this` refers to anonymous object |

---

# JVM Implementation

Many developers believe Lambdas are compiled into anonymous inner classes.

This is **not true** in modern Java.

Instead,

the compiler generates an

```
invokedynamic
```

instruction.

At runtime,

the JVM uses

```
LambdaMetafactory
```

to generate an implementation of the target Functional Interface.

Advantages

* Better JVM optimization
* Less class generation
* Improved performance
* Runtime flexibility

---

# Internal Flow

```
Lambda Expression
        │
        ▼
Compiler
        │
Generates invokedynamic
        │
        ▼
JVM Bootstrap
        │
LambdaMetafactory
        │
Creates Functional Interface Implementation
        │
        ▼
Lambda Object
```

---

# Anonymous Inner Class vs Lambda

| Anonymous Inner Class    | Lambda                  |
| ------------------------ | ----------------------- |
| Generates separate class | Uses `invokedynamic`    |
| Verbose                  | Concise                 |
| Has its own `this`       | Shares enclosing `this` |
| More memory overhead     | Generally lighter       |
| Boilerplate code         | Minimal code            |

---

# Advantages of Lambdas

* Less boilerplate
* Improved readability
* Enables Functional Programming
* Supports Stream API
* Easier behavior composition
* Better JVM optimizations
* Encourages declarative programming

---

# Limitations

* Require a Functional Interface
* Cannot define constructors
* Cannot extend classes
* Captured local variables must be effectively final
* Debugging complex Lambdas can be more difficult

---

# Real-World Uses

Lambdas are widely used with:

* Stream API
* Collections Sorting
* Event Listeners
* Executors
* CompletableFuture
* Optional
* Predicate-based filtering
* Functional composition

---

# Frequently Asked Interview Questions

### What is a Lambda Expression?

An anonymous function that represents behavior and implements the single abstract method of a Functional Interface.

---

### Is a Lambda a Method?

No.

It is an object representing an implementation of a Functional Interface.

---

### Can Lambdas exist without Functional Interfaces?

No.

Every Lambda requires a target Functional Interface.

---

### What is Target Typing?

The compiler determines which Functional Interface a Lambda implements based on the surrounding context.

---

### What is Variable Capture?

The process by which a Lambda accesses variables from its enclosing scope.

Only final or effectively final local variables can be captured.

---

### Why are captured variables effectively final?

Because local variables live on the stack, while Lambda objects may outlive the method that created them. The JVM captures a copy of the value, preventing ambiguity and concurrency issues.

---

### Can Lambdas access instance variables?

Yes.

Instance variables reside on the Heap and remain available through the enclosing object reference.

---

### Can Lambdas access static variables?

Yes.

Static variables belong to the class and remain accessible while the class is loaded.

---

### What does `this` refer to inside a Lambda?

The enclosing object.

Unlike anonymous inner classes, Lambdas do not introduce a new `this`.

---

### How are Lambdas implemented internally?

The compiler emits an `invokedynamic` instruction.

At runtime, the JVM uses `LambdaMetafactory` to generate an implementation of the target Functional Interface.

---

# Key Takeaways

* Lambda Expressions represent behavior as objects.
* Every Lambda targets exactly one Functional Interface.
* Lambdas reduce boilerplate compared to anonymous inner classes.
* The compiler uses Target Typing to infer the Functional Interface.
* Local variables captured by Lambdas must be effectively final.
* `this` inside a Lambda refers to the enclosing object.
* Modern Lambdas are implemented using `invokedynamic` and `LambdaMetafactory`, not anonymous inner classes.
* Lambdas are the foundation of Functional Programming in Java and enable APIs such as Streams, Optional, and CompletableFuture.

# Day 3 – Functional Programming

## Chapter 2 – Functional Interfaces (Interview Notes)

> **Topics Covered**
>
> * What is a Functional Interface?
> * Why Functional Interfaces were introduced
> * Single Abstract Method (SAM)
> * Target Typing
> * `@FunctionalInterface`
> * Default, Static, and Private Methods
> * Inheritance Rules
> * Generic Functional Interfaces
> * JVM Perspective
> * Common Interview Questions

---

# Overview

A **Functional Interface** is the foundation of Functional Programming in Java.

Lambda Expressions and Method References **cannot exist independently**. They require a Functional Interface to provide a target type.

A Functional Interface defines **exactly one abstract method**, allowing the compiler to determine which method a Lambda Expression is implementing.

---

# Why Were Functional Interfaces Introduced?

A Lambda Expression only contains an implementation.

Example

```java
(a, b) -> a + b
```

Notice that it does **not** specify:

* Method name
* Interface name
* Return type explicitly

Suppose Java had these interfaces:

```java
interface Calculator {

    int add(int a, int b);

}
```

```java
interface Operation {

    int execute(int a, int b);

}
```

```java
interface Sum {

    int calculate(int a, int b);

}
```

Question:

Which method should the Lambda implement?

The compiler has no way to determine this.

Therefore Java requires every Lambda Expression to target a **Functional Interface**.

---

# Definition

A Functional Interface is

> **An interface that contains exactly one abstract method.**

Example

```java
@FunctionalInterface
interface Printer {

    void print(String message);

}
```

Valid Lambda

```java
Printer printer =
        message -> System.out.println(message);
```

The Lambda implements

```java
print()
```

---

# Single Abstract Method (SAM)

A Functional Interface is also known as a

> **SAM Interface**

SAM stands for

> **Single Abstract Method**

Examples

```java
Runnable
```

Contains

```java
void run();
```

---

```java
Comparator<T>
```

Contains

```java
int compare(T o1, T o2);
```

---

```java
Callable<T>
```

Contains

```java
T call();
```

---

```java
Consumer<T>
```

Contains

```java
void accept(T t);
```

All are Functional Interfaces because each contains only one abstract method.

---

# Why Exactly One Abstract Method?

Consider

```java
interface Operations {

    int add(int a, int b);

    String format(String name);

}
```

Now write

```java
Operations op =
        (a, b) -> a + b;
```

Compilation fails.

Compiler Error (conceptually)

```text
Operations is not a functional interface
multiple non-overriding abstract methods found
```

Although the Lambda appears to match

```java
add(int, int)
```

the compiler **does not attempt to guess**.

It first checks

> "Is the target interface a Functional Interface?"

Since the answer is **No**, compilation stops immediately.

---

# Another Example

```java
interface Demo {

    void show();

    int calculate(int value);

}
```

Lambda

```java
Demo demo =
        () -> System.out.println("Hello");
```

Compilation Error

Reason:

Two abstract methods exist.

The compiler cannot determine which one should be implemented.

---

# Method Overloading Does Not Help

```java
interface Printer {

    void print(String message);

    void print(int number);

}
```

Lambda

```java
Printer p =
        value -> System.out.println(value);
```

Compilation fails.

Even though parameter types are different,

the interface still contains **two abstract methods**.

Therefore it is **not** a Functional Interface.

---

# Target Typing

The compiler determines the Lambda's target from the left-hand side.

Example

```java
@FunctionalInterface
interface Printer {

    void print(String message);

}
```

```java
Printer printer =
        message -> System.out.println(message);
```

Compiler Steps

```text
Lambda Expression
        │
        ▼
Target Type
(Printer)
        │
        ▼
Verify Functional Interface
        │
        ▼
Exactly One Abstract Method
        │
        ▼
Bind Lambda to print()
        │
        ▼
Validate Parameters
        │
        ▼
Compilation Successful
```

---

# Target Typing with Multiple Abstract Methods

Example

```java
interface Printer {

    void print(String message);

    void display(String message);

}
```

Compiler Steps

```text
Target Type
        │
        ▼
Printer
        │
        ▼
Abstract Methods = 2
        │
        ▼
Not a Functional Interface
        │
        ▼
Compilation Error
```

The compiler never attempts to compare the Lambda with individual methods.

---

# @FunctionalInterface Annotation

Java provides

```java
@FunctionalInterface
```

Example

```java
@FunctionalInterface
interface Printer {

    void print(String message);

}
```

The annotation is

**optional**.

This also works

```java
interface Printer {

    void print(String message);

}
```

---

# Why Use @FunctionalInterface?

Suppose today

```java
@FunctionalInterface
interface Printer {

    void print(String message);

}
```

Later another developer adds

```java
void display();
```

Now

```java
@FunctionalInterface
interface Printer {

    void print(String message);

    void display();

}
```

Compilation fails immediately.

Without the annotation,

the interface would silently stop being functional.

The annotation provides compile-time safety.

---

# Default Methods

A Functional Interface may contain multiple default methods.

Example

```java
@FunctionalInterface
interface Printer {

    void print(String message);

    default void log() {

        System.out.println("Logging");

    }

}
```

Still a Functional Interface.

Reason:

Only

```java
print()
```

is abstract.

---

# Static Methods

Example

```java
@FunctionalInterface
interface Printer {

    void print(String message);

    static void display() {

        System.out.println("Display");

    }

}
```

Still functional.

Static methods do not count as abstract methods.

---

# Private Methods (Java 9)

Interfaces may contain private methods.

Example

```java
@FunctionalInterface
interface Printer {

    void print(String message);

    private void helper() {

    }

}
```

Still functional.

Private methods are implementation details.

---

# Methods Inherited from Object

Example

```java
@FunctionalInterface
interface Demo {

    void execute();

    String toString();

}
```

Still a Functional Interface.

Reason

Methods matching public methods of `Object`, such as

* `toString()`
* `equals(Object)`
* `hashCode()`

are ignored when determining whether an interface is functional.

---

# Functional Interface Inheritance

Example

```java
interface Animal {

    void eat();

}
```

```java
@FunctionalInterface
interface Dog extends Animal {

}
```

Still functional.

Only one abstract method exists.

---

Now

```java
interface Animal {

    void eat();

}
```

```java
interface Pet {

    void play();

}
```

```java
interface Dog extends Animal, Pet {

}
```

Now

```java
eat()

play()
```

Two abstract methods exist.

Not a Functional Interface.

---

# Generic Functional Interfaces

Example

```java
@FunctionalInterface
interface Operation<T> {

    T operate(T a, T b);

}
```

Usage

```java
Operation<Integer> add =
        (a, b) -> a + b;
```

Another example

```java
Operation<String> join =
        (a, b) -> a + b;
```

The same interface works for multiple data types.

---

# Built-in Functional Interfaces

Java provides many Functional Interfaces in

```java
java.util.function
```

Common examples

| Interface           | Abstract Method       | Purpose                                     |
| ------------------- | --------------------- | ------------------------------------------- |
| `Predicate<T>`      | `boolean test(T t)`   | Returns a boolean result                    |
| `Function<T,R>`     | `R apply(T t)`        | Transforms one value into another           |
| `Consumer<T>`       | `void accept(T t)`    | Consumes a value without returning anything |
| `Supplier<T>`       | `T get()`             | Produces a value                            |
| `UnaryOperator<T>`  | `T apply(T t)`        | Operates on one value of the same type      |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | Operates on two values of the same type     |

These are heavily used by the Stream API.

---

# JVM Perspective

Example

```java
Runnable runnable =
        () -> System.out.println("Hello");
```

Compilation Flow

```text
Lambda Expression
        │
        ▼
Target Functional Interface
        │
        ▼
Verify Exactly One Abstract Method
        │
        ▼
Compiler Generates invokedynamic
        │
        ▼
LambdaMetafactory
        │
        ▼
Creates Runnable Implementation
        │
        ▼
Runnable Object
```

Without a Functional Interface,

the compiler cannot determine which method the Lambda should implement.

---

# Advantages

* Enables Lambda Expressions
* Enables Method References
* Foundation of Stream API
* Reduces boilerplate code
* Encourages Functional Programming
* Improves code readability
* Allows behavior to be passed as data

---

# Limitations

* Must contain exactly one abstract method
* Cannot target interfaces with multiple abstract methods
* Lambdas cannot determine which method to implement without a Functional Interface

---

# Frequently Asked Interview Questions

### What is a Functional Interface?

An interface that contains exactly one abstract method and serves as the target type for Lambda Expressions and Method References.

---

### What is a SAM Interface?

SAM stands for **Single Abstract Method**.

A Functional Interface is also called a SAM Interface.

---

### Why does a Lambda require a Functional Interface?

A Lambda provides only an implementation.

The Functional Interface tells the compiler **which single abstract method** the Lambda should implement.

---

### Is `@FunctionalInterface` mandatory?

No.

It is optional but recommended because it provides compile-time validation.

---

### Can a Functional Interface have default methods?

Yes.

Default methods are already implemented and do not count as abstract methods.

---

### Can a Functional Interface have static methods?

Yes.

Static methods do not affect the Functional Interface rule.

---

### Can a Functional Interface have private methods?

Yes.

Since Java 9, private methods are allowed in interfaces and do not count as abstract methods.

---

### Can a Functional Interface extend another interface?

Yes.

As long as the resulting interface still has exactly one abstract method.

---

### Are methods inherited from `Object` counted?

No.

Methods such as `toString()`, `equals(Object)`, and `hashCode()` are ignored when determining whether an interface is functional.

---

### What happens if an interface has two abstract methods?

The compiler reports that the interface is **not a Functional Interface**, and no Lambda Expression can target it.

---

### How does the compiler resolve a Lambda?

1. Determine the target interface.
2. Verify it is a Functional Interface.
3. Locate the single abstract method.
4. Bind the Lambda to that method.
5. Validate parameter and return types.
6. Generate `invokedynamic` for runtime implementation.

---

# Key Takeaways

* A Functional Interface contains exactly **one abstract method**.
* It is also called a **SAM (Single Abstract Method) Interface**.
* Lambdas require a Functional Interface because they only provide an implementation, not a method name.
* The compiler first verifies that the target interface is functional before attempting to bind the Lambda.
* Default, static, private, and `Object` methods do **not** count toward the single abstract method rule.
* `@FunctionalInterface` is optional but provides valuable compile-time protection.
* Functional Interfaces are the backbone of Lambdas, Method References, Streams, and the entire `java.util.function` package.

