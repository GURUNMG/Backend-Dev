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
