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

# Day 3 – Functional Programming

## Chapter 3 – Predicate<T> (Interview Notes)

> **Topics Covered**
>
> * Why `Predicate` was introduced
> * What is `Predicate<T>`?
> * Generic Type (`T`)
> * `test()` Method
> * Target Typing
> * Why `Predicate<User>` instead of `Predicate`
> * Predicate Chaining (`and`, `or`, `negate`)
> * Static Methods (`isEqual`, `not`)
> * Stream API Integration
> * Interview Questions

---

# Overview

`Predicate<T>` is one of the most commonly used Functional Interfaces in Java.

It represents a **condition** or **boolean-valued function**.

A Predicate accepts **one input** and returns either:

* `true`
* `false`

It is primarily used for:

* Filtering
* Validation
* Searching
* Business Rules
* Authorization
* Conditional Processing

---

# Why Was Predicate Introduced?

Suppose we have a list of employees.

```java
Employee("John", 50000)
Employee("Alice", 70000)
Employee("Bob", 30000)
```

We want employees whose salary is greater than 50,000.

Traditional approach

```java
List<Employee> result = new ArrayList<>();

for (Employee employee : employees) {

    if (employee.getSalary() > 50000) {

        result.add(employee);

    }

}
```

Tomorrow the requirement changes.

Find

* employees older than 30
* employees from IT department
* employees whose name starts with A

Every time,

only the **condition changes**.

The loop remains exactly the same.

Java introduced **Predicate** so that the condition can be passed as an object.

---

# Definition

`Predicate<T>` is a Functional Interface.

```java
@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t);

}
```

It contains exactly one abstract method.

```java
boolean test(T t);
```

Meaning

> Given an object of type `T`, return `true` or `false`.

---

# Understanding Generic Type (T)

Example

```java
Predicate<Integer>
```

Compiler substitutes

```java
boolean test(Integer value);
```

---

Example

```java
Predicate<String>
```

becomes

```java
boolean test(String value);
```

---

Example

```java
Predicate<Employee>
```

becomes

```java
boolean test(Employee employee);
```

The generic type determines the type of the lambda parameter.

---

# Basic Example

```java
Predicate<Integer> isEven =
        number -> number % 2 == 0;
```

Execution

```java
isEven.test(10);
```

Output

```text
true
```

Execution

```java
isEven.test(15);
```

Output

```text
false
```

---

# String Example

```java
Predicate<String> isEmpty =
        value -> value.isEmpty();
```

Execution

```java
isEmpty.test("");
```

Output

```text
true
```

Execution

```java
isEmpty.test("Java");
```

Output

```text
false
```

---

# Employee Example

```java
Predicate<Employee> highSalary =
        employee -> employee.getSalary() > 50000;
```

Execution

```java
Employee employee =
        new Employee("John", 70000);

highSalary.test(employee);
```

Output

```text
true
```

---

# Target Typing

Example

```java
Predicate<Employee> highSalary =
        employee -> employee.getSalary() > 50000;
```

Compiler Steps

```text
Predicate<Employee>
        │
        ▼
Target Functional Interface
        │
        ▼
Abstract Method

boolean test(T)
        │
        ▼
Generic Substitution

T = Employee
        │
        ▼
Method Becomes

boolean test(Employee employee)
        │
        ▼
Lambda Parameter Type Inferred
        │
        ▼
Compilation Successful
```

---

# Why `Predicate<Employee>` Instead of `Predicate`?

Many interviewers ask this question.

Example

```java
Predicate<Employee> predicate =
        employee -> employee.getSalary() > 50000;
```

Compiler sees

```java
boolean test(Employee employee)
```

Therefore

```java
employee.getSalary()
```

is valid.

---

Now consider

```java
Predicate predicate =
        employee -> employee.getSalary() > 50000;
```

Here,

`Predicate` becomes a **raw type**.

Compiler treats it as

```java
boolean test(Object employee)
```

Now the lambda parameter is an `Object`.

Attempting

```java
employee.getSalary();
```

produces a compilation error.

Reason

`Object` does not contain

```java
getSalary()
```

---

# Raw Type vs Generic Type

| Generic Predicate     | Raw Predicate             |
| --------------------- | ------------------------- |
| `Predicate<Employee>` | `Predicate`               |
| `test(Employee)`      | `test(Object)`            |
| Type-safe             | Not type-safe             |
| No casting required   | Casting required          |
| Compile-time checking | Loses generic information |

---

# Reusable Predicates

Instead of writing conditions repeatedly,

create reusable Predicates.

```java
Predicate<Employee> highSalary =
        employee -> employee.getSalary() > 50000;

Predicate<Employee> developer =
        employee -> employee.getDepartment().equals("Developer");
```

These can be reused throughout the application.

---

# Predicate in Collections

Without Predicate

```java
for (Integer number : numbers) {

    if (number % 2 == 0) {

        System.out.println(number);

    }

}
```

Reusable approach

```java
Predicate<Integer> even =
        number -> number % 2 == 0;

for (Integer number : numbers) {

    if (even.test(number)) {

        System.out.println(number);

    }

}
```

Only the Predicate changes.

The loop remains reusable.

---

# Predicate Chaining

Suppose

```java
Predicate<Employee> highSalary =
        employee -> employee.getSalary() > 50000;

Predicate<Employee> developer =
        employee -> employee.getDepartment().equals("Developer");
```

Instead of

```java
employee ->
employee.getSalary() > 50000 &&
employee.getDepartment().equals("Developer")
```

Use

```java
Predicate<Employee> eligible =
        highSalary.and(developer);
```

Equivalent to

```java
highSalary.test(employee)
&&
developer.test(employee)
```

---

# OR Operation

```java
Predicate<Employee> manager =
        employee -> employee.getRole().equals("Manager");

Predicate<Employee> condition =
        developer.or(manager);
```

Equivalent

```java
developer.test(employee)
||
manager.test(employee)
```

---

# Negation

```java
Predicate<Employee> developer =
        employee -> employee.getDepartment().equals("Developer");
```

Negation

```java
Predicate<Employee> nonDeveloper =
        developer.negate();
```

Equivalent

```java
!developer.test(employee)
```

---

# Combining Multiple Predicates

```java
Predicate<Employee> experienced =
        employee -> employee.getExperience() >= 5;

Predicate<Employee> highSalary =
        employee -> employee.getSalary() > 50000;

Predicate<Employee> developer =
        employee -> employee.getDepartment().equals("Developer");
```

Combine

```java
Predicate<Employee> eligible =
        developer
                .and(experienced)
                .and(highSalary);
```

Produces readable and reusable business rules.

---

# Static Method - isEqual()

Example

```java
Predicate<String> javaLanguage =
        Predicate.isEqual("Java");
```

Execution

```java
javaLanguage.test("Java");
```

Output

```text
true
```

Execution

```java
javaLanguage.test("Python");
```

Output

```text
false
```

Internally behaves similarly to

```java
Objects.equals(a, b);
```

---

# Static Method - not() (Java 11)

Instead of

```java
developer.negate()
```

Java 11 introduced

```java
Predicate.not(developer)
```

Commonly used in Streams.

Example

```java
employees.stream()
         .filter(Predicate.not(Employee::isRetired))
         .toList();
```

---

# Predicate in Stream API

This is the most common real-world usage.

```java
employees.stream()
         .filter(employee -> employee.getSalary() > 50000)
         .toList();
```

Question

What is

```java
employee -> employee.getSalary() > 50000
```

Answer

```java
Predicate<Employee>
```

because

```java
filter()
```

expects

```java
Predicate<? super T>
```

Method Signature

```java
Stream<T> filter(Predicate<? super T> predicate)
```

---

# Predicate vs Function

| Predicate           | Function                |
| ------------------- | ----------------------- |
| Returns `boolean`   | Returns any type        |
| Used for conditions | Used for transformation |
| Method = `test()`   | Method = `apply()`      |

Predicate

```java
Predicate<Integer> even =
        number -> number % 2 == 0;
```

Function

```java
Function<Integer, String> convert =
        number -> "Value : " + number;
```

---

# Predicate vs Consumer

| Predicate         | Consumer           |
| ----------------- | ------------------ |
| Returns `boolean` | Returns `void`     |
| Tests a condition | Performs an action |
| `test()`          | `accept()`         |

Predicate answers

> "Should I keep this object?"

Consumer answers

> "What should I do with this object?"

---

# JVM Perspective

Example

```java
Predicate<Employee> highSalary =
        employee -> employee.getSalary() > 50000;
```

Compilation Flow

```text
Lambda Expression
        │
        ▼
Target Functional Interface
(Predicate<Employee>)
        │
        ▼
Locate Abstract Method

boolean test(T)
        │
        ▼
Generic Substitution

T = Employee
        │
        ▼
Method Becomes

boolean test(Employee)
        │
        ▼
Compiler Generates invokedynamic
        │
        ▼
LambdaMetafactory
        │
        ▼
Predicate Object
```

---

# Real-World Uses

`Predicate` is commonly used in

* Stream API (`filter()`)
* Validation Frameworks
* Authorization Rules
* Business Rule Engines
* Dynamic Query Building
* Search APIs
* Feature Flags
* Conditional Processing

Example

```java
Predicate<User> activeUser =
        User::isActive;

Predicate<User> adminUser =
        user -> user.getRole().equals("ADMIN");

Predicate<User> activeAdmin =
        activeUser.and(adminUser);
```

---

# Advantages

* Encourages reusable conditions
* Improves readability
* Eliminates duplicate filtering logic
* Works seamlessly with Streams
* Supports functional composition (`and`, `or`, `negate`)
* Promotes declarative programming

---

# Limitations

* Accepts only one input parameter
* Returns only a boolean value
* Should ideally avoid side effects
* Complex business logic may require combining multiple Predicates

---

# Frequently Asked Interview Questions

### What is `Predicate<T>`?

A Functional Interface representing a condition that accepts one input and returns a boolean result.

---

### What is the abstract method of Predicate?

```java
boolean test(T t);
```

---

### Why does Predicate return a boolean?

Because it represents a logical condition or test.

---

### Why write `Predicate<Employee>` instead of `Predicate`?

`Predicate<Employee>` preserves generic type information, allowing the compiler to infer that the lambda parameter is an `Employee`.

Using the raw type `Predicate` causes the parameter to become an `Object`, losing type safety.

---

### Can Predicates be combined?

Yes.

Using

* `and()`
* `or()`
* `negate()`

---

### Which Stream method accepts a Predicate?

```java
filter()
```

---

### Difference between Predicate and Function?

Predicate evaluates a condition and returns a boolean.

Function transforms one value into another.

---

### Can Predicate have side effects?

Technically yes.

However, Predicates should ideally be **pure functions** that depend only on their input and do not modify external state.

---

### What is `Predicate.isEqual()`?

A static factory method that creates a Predicate to test equality using `Objects.equals()` semantics.

---

### What is `Predicate.not()`?

A Java 11 static method that returns the logical negation of an existing Predicate.

---

# Key Takeaways

* `Predicate<T>` represents a **boolean-valued condition**.
* It contains one abstract method: `boolean test(T t)`.
* The generic type (`T`) determines the type of the lambda parameter.
* Using `Predicate<T>` provides compile-time type safety, while the raw `Predicate` type treats the parameter as an `Object`.
* Predicates can be composed using `and()`, `or()`, and `negate()`.
* `Stream.filter()` is the most common consumer of a `Predicate`.
* Predicates should ideally be reusable, composable, and free of side effects.
* `Predicate` is one of the core building blocks of Java's Functional Programming model.

# Day 3 – Functional Programming

## Chapter 4 – Function<T, R> (Interview Notes)

> **Topics Covered**
>
> * Why `Function` was introduced
> * What is `Function<T, R>`?
> * Generic Types (`T` and `R`)
> * `apply()` Method
> * Target Typing
> * Function Composition (`andThen`, `compose`)
> * `identity()` Method
> * Stream API Integration
> * Interview Questions

---

# Overview

`Function<T, R>` is one of the core Functional Interfaces provided by Java.

It represents a **transformation**.

A Function accepts **one input** and returns **one output**.

Unlike `Predicate`, which returns only a boolean, `Function` can return **any type**.

It is primarily used for:

* Data Transformation
* Mapping
* DTO Conversion
* Value Extraction
* Formatting
* Stream Processing

---

# Why Was Function Introduced?

Suppose we have a list of employees.

```java
Employee("John", 50000)
Employee("Alice", 70000)
Employee("Bob", 30000)
```

Now suppose we need

* Employee → Name
* Employee → Salary
* Employee → Department
* Employee → Email

Traditional approach

```java
List<String> names = new ArrayList<>();

for (Employee employee : employees) {

    names.add(employee.getName());

}
```

Tomorrow

Need salaries.

```java
List<Double> salaries = new ArrayList<>();

for (Employee employee : employees) {

    salaries.add(employee.getSalary());

}
```

Again,

Need emails.

Again,

Need departments.

Notice

The loop never changes.

Only the transformation changes.

Java extracted this transformation into a Functional Interface called **Function**.

---

# Definition

`Function<T, R>` is a Functional Interface.

```java
@FunctionalInterface
public interface Function<T, R> {

    R apply(T t);

}
```

It contains exactly one abstract method.

```java
R apply(T t);
```

Meaning

> Accept an object of type `T` and transform it into an object of type `R`.

---

# Understanding Generic Types

Unlike Predicate,

Function contains **two generic parameters**.

```text
Function<T, R>

T → Input Type

R → Result (Return Type)
```

Visual Representation

```text
Input (T)
     │
     ▼
 Function
     │
     ▼
Output (R)
```

---

# Generic Type Examples

Example

```java
Function<Integer, String>
```

Compiler substitutes

```java
String apply(Integer number);
```

Meaning

```text
Integer

↓

String
```

---

Example

```java
Function<Employee, String>
```

becomes

```java
String apply(Employee employee);
```

---

Example

```java
Function<Employee, Double>
```

becomes

```java
Double apply(Employee employee);
```

---

Example

```java
Function<String, Integer>
```

becomes

```java
Integer apply(String value);
```

---

# Basic Example

```java
Function<Integer, Integer> square =
        number -> number * number;
```

Execution

```java
square.apply(5);
```

Output

```text
25
```

---

# String Example

```java
Function<String, Integer> length =
        text -> text.length();
```

Execution

```java
length.apply("Java");
```

Output

```text
4
```

Input

String

Output

Integer

---

# Employee Example

```java
Function<Employee, String> getName =
        employee -> employee.getName();
```

Execution

```java
getName.apply(employee);
```

Output

```text
John
```

---

Salary Example

```java
Function<Employee, Double> getSalary =
        Employee::getSalary;
```

Execution

```java
getSalary.apply(employee);
```

Output

```text
70000.0
```

The same Employee object can be transformed into different values.

---

# Why Function Has Two Generic Types

Predicate

```java
Predicate<Employee>
```

always returns

```text
boolean
```

Therefore only one generic parameter is required.

Function is different.

Input

↓

Output can be

* String
* Integer
* Double
* Address
* DTO
* Any Object

Therefore

```java
Function<T, R>
```

requires

* one generic type for the input
* one generic type for the output

---

# Target Typing

Example

```java
Function<Employee, String> getName =
        employee -> employee.getName();
```

Compiler Steps

```text
Function<Employee, String>
        │
        ▼
Target Functional Interface
        │
        ▼
Locate Abstract Method

R apply(T)
        │
        ▼
Generic Substitution

T = Employee

R = String
        │
        ▼
Method Becomes

String apply(Employee employee)
        │
        ▼
Lambda Parameter Type Inferred
        │
        ▼
Compilation Successful
```

---

# Why Function<Employee, String> Instead of Function?

Example

```java
Function<Employee, String> getName =
        employee -> employee.getName();
```

Compiler sees

```java
String apply(Employee employee);
```

Therefore

```java
employee.getName();
```

is valid.

---

Now consider

```java
Function getName =
        employee -> employee.getName();
```

Here,

`Function` becomes a **raw type**.

Compiler treats it as

```java
Object apply(Object employee);
```

Now

```java
employee.getName();
```

fails.

Reason

`Object` does not contain

```java
getName()
```

The generic type provides compile-time type safety.

---

# Raw Type vs Generic Type

| Generic Function             | Raw Function              |
| ---------------------------- | ------------------------- |
| `Function<Employee, String>` | `Function`                |
| `String apply(Employee)`     | `Object apply(Object)`    |
| Type-safe                    | Not type-safe             |
| No casting required          | Casting required          |
| Compile-time checking        | Loses generic information |

---

# Reusable Functions

Instead of repeatedly extracting values,

create reusable Functions.

```java
Function<Employee, String> getName =
        Employee::getName;

Function<Employee, Double> getSalary =
        Employee::getSalary;

Function<Employee, String> getDepartment =
        Employee::getDepartment;
```

These Functions can be reused throughout the application.

---

# Function Composition

One of the strongest features of Function.

Suppose

```java
Function<String, String> trim =
        String::trim;
```

```java
Function<String, Integer> length =
        String::length;
```

Instead of writing

```java
text -> text.trim().length()
```

compose them.

---

# andThen()

```java
Function<String, Integer> result =
        trim.andThen(length);
```

Execution

```text
Input

↓

trim()

↓

length()

↓

Output
```

Example

```java
result.apply("  Java  ");
```

Output

```text
4
```

---

Another Example

```java
Function<Integer, Integer> multiply =
        number -> number * 2;

Function<Integer, Integer> add =
        number -> number + 5;

Function<Integer, Integer> result =
        multiply.andThen(add);
```

Execution

```text
10

↓

20

↓

25
```

Output

```text
25
```

---

# compose()

Performs the reverse order.

```java
Function<Integer, Integer> result =
        multiply.compose(add);
```

Execution

```text
10

↓

15

↓

30
```

Output

```text
30
```

---

# compose() vs andThen()

| compose()                        | andThen()                       |
| -------------------------------- | ------------------------------- |
| Executes supplied Function first | Executes current Function first |
| Reverse order                    | Natural order                   |

Example

```text
compose()

Input
 ↓
add()
 ↓
multiply()
```

```text
andThen()

Input
 ↓
multiply()
 ↓
add()
```

---

# identity()

Function provides a static method

```java
Function.identity();
```

Example

```java
Function<String, String> identity =
        Function.identity();
```

Execution

```java
identity.apply("Java");
```

Output

```text
Java
```

Equivalent to

```java
value -> value
```

No transformation occurs.

---

# Function in Stream API

The most common real-world usage.

```java
employees.stream()
         .map(Employee::getName)
         .toList();
```

Question

What is

```java
Employee::getName
```

Answer

```java
Function<Employee, String>
```

because

```java
map()
```

expects

```java
Function<? super T, ? extends R>
```

Method Signature

```java
<R> Stream<R> map(
        Function<? super T, ? extends R> mapper
)
```

---

# Function vs Predicate

| Function                | Predicate          |
| ----------------------- | ------------------ |
| Returns any type        | Returns boolean    |
| Used for transformation | Used for filtering |
| Method = `apply()`      | Method = `test()`  |

Predicate

```java
employee ->
employee.getSalary() > 50000
```

Function

```java
employee ->
employee.getSalary()
```

---

# Function vs Consumer

| Function        | Consumer           |
| --------------- | ------------------ |
| Returns a value | Returns nothing    |
| Transformation  | Performs an action |
| `apply()`       | `accept()`         |

Example

Function

```java
employee ->
employee.getSalary()
```

Consumer

```java
employee ->
System.out.println(employee)
```

---

# Function vs Supplier

| Function                   | Supplier                      |
| -------------------------- | ----------------------------- |
| Requires input             | No input                      |
| Produces output from input | Produces output without input |

Example

Function

```text
Employee

↓

Salary
```

Supplier

```text
↓

Creates Employee
```

---

# JVM Perspective

Example

```java
Function<Employee, String> getName =
        Employee::getName;
```

Compilation Flow

```text
Lambda / Method Reference
        │
        ▼
Target Functional Interface
(Function<Employee, String>)
        │
        ▼
Locate Abstract Method

R apply(T)
        │
        ▼
Generic Substitution

T = Employee

R = String
        │
        ▼
Method Becomes

String apply(Employee)
        │
        ▼
Compiler Generates invokedynamic
        │
        ▼
LambdaMetafactory
        │
        ▼
Function Object
```

---

# Real-World Uses

`Function` is heavily used in

* Stream API (`map()`)
* DTO Mapping
* Entity → DTO Conversion
* JSON Transformation
* Formatting
* Data Extraction
* Cache Value Conversion
* Business Object Transformation

Example

```java
Function<User, UserDTO> mapper =
        user -> new UserDTO(
                user.getId(),
                user.getName()
        );
```

---

# Advantages

* Encourages reusable transformations
* Eliminates duplicate mapping logic
* Improves readability
* Supports Function composition
* Integrates naturally with Stream API
* Promotes declarative programming

---

# Limitations

* Accepts only one input parameter
* Complex transformations may require chaining multiple Functions
* Should ideally avoid side effects

---

# Frequently Asked Interview Questions

### What is `Function<T, R>`?

A Functional Interface that accepts one input and returns one output.

---

### What is the abstract method?

```java
R apply(T t);
```

---

### Why does Function have two generic types?

`T` represents the input type.

`R` represents the return type.

Unlike `Predicate`, the return type is not fixed.

---

### Why write `Function<Employee, String>` instead of `Function`?

The generic parameters allow the compiler to infer:

```java
String apply(Employee employee);
```

Without generics,

the raw type becomes

```java
Object apply(Object employee);
```

which loses compile-time type safety.

---

### Difference between Predicate and Function?

Predicate evaluates a condition and returns a boolean.

Function transforms one value into another.

---

### Difference between Function and Consumer?

Function returns a value.

Consumer performs an action and returns nothing.

---

### Difference between compose() and andThen()?

`compose()` executes the supplied Function first.

`andThen()` executes the current Function first.

---

### What does `Function.identity()` return?

A Function that returns its input unchanged.

Equivalent to

```java
value -> value
```

---

### Which Stream method accepts Function?

```java
map()
```

---

# Key Takeaways

* `Function<T, R>` represents a **transformation** from one type to another.
* It contains one abstract method: `R apply(T t)`.
* `T` is the input type, and `R` is the result type.
* Generic parameters enable compile-time type safety and lambda parameter inference.
* Functions can be composed using `andThen()` and `compose()`.
* `Function.identity()` returns the input unchanged.
* `Stream.map()` is the most common consumer of a `Function`.
* `Function` is heavily used in DTO mapping, data transformation, and modern Java applications.

# Day 3 – Functional Programming

## Chapter 5 – Consumer<T> (Interview Notes)

> **Topics Covered**
>
> * Why `Consumer` was introduced
> * What is `Consumer<T>`?
> * Generic Type (`T`)
> * `accept()` Method
> * Target Typing
> * Why `Consumer<Employee>` instead of `Consumer`
> * Consumer Chaining (`andThen`)
> * Side Effects
> * Stream API Integration
> * Interview Questions

---

# Overview

`Consumer<T>` is one of the core Functional Interfaces provided by Java.

It represents an **action**.

A Consumer accepts **one input** and performs an operation **without returning a value**.

Unlike `Predicate`, which evaluates a condition, or `Function`, which transforms data, a Consumer simply performs work.

It is primarily used for:

* Printing
* Logging
* Database Operations
* Sending Emails
* Notifications
* Publishing Messages
* Updating External Systems

---

# Why Was Consumer Introduced?

Suppose we have a list of employees.

```java
Employee("John", 50000)
Employee("Alice", 70000)
Employee("Bob", 30000)
```

Now suppose we want to

* Print each employee
* Save each employee
* Send an email to each employee
* Log employee details
* Publish employee events

Notice

We are not

* filtering employees
* transforming employees

We are simply performing an action.

Java introduced **Consumer** to represent this behavior.

---

# Definition

`Consumer<T>` is a Functional Interface.

```java
@FunctionalInterface
public interface Consumer<T> {

    void accept(T t);

}
```

It contains exactly one abstract method.

```java
void accept(T t);
```

Meaning

> Accept an object of type `T` and perform an action without returning a value.

---

# Understanding Generic Type (T)

Example

```java
Consumer<String>
```

Compiler substitutes

```java
void accept(String value);
```

---

Example

```java
Consumer<Employee>
```

becomes

```java
void accept(Employee employee);
```

Unlike `Function`, Consumer requires only one generic parameter because its return type is always `void`.

---

# Basic Example

```java
Consumer<String> printer =
        text -> System.out.println(text);
```

Execution

```java
printer.accept("Java");
```

Output

```text
Java
```

Nothing is returned.

---

# Integer Example

```java
Consumer<Integer> display =
        number -> System.out.println(number);
```

Execution

```java
display.accept(100);
```

Output

```text
100
```

---

# Employee Example

```java
Consumer<Employee> printEmployee =
        employee -> System.out.println(employee);
```

Execution

```java
printEmployee.accept(employee);
```

Output

```text
Employee{name='John', salary=70000}
```

---

# Consumer Performs Actions

Typical Consumer implementations include

```java
employee -> logger.info(employee.toString())
```

```java
employee -> employeeRepository.save(employee)
```

```java
employee -> emailService.send(employee)
```

```java
employee -> cache.put(employee.getId(), employee)
```

The object is consumed by performing some work.

---

# Target Typing

Example

```java
Consumer<Employee> printer =
        employee -> System.out.println(employee);
```

Compiler Steps

```text
Consumer<Employee>
        │
        ▼
Target Functional Interface
        │
        ▼
Locate Abstract Method

void accept(T)
        │
        ▼
Generic Substitution

T = Employee
        │
        ▼
Method Becomes

void accept(Employee employee)
        │
        ▼
Lambda Parameter Type Inferred
        │
        ▼
Compilation Successful
```

---

# Why Consumer<Employee> Instead of Consumer?

Example

```java
Consumer<Employee> consumer =
        employee -> System.out.println(employee.getName());
```

Compiler sees

```java
void accept(Employee employee);
```

Therefore

```java
employee.getName();
```

is valid.

---

Now consider

```java
Consumer consumer =
        employee -> System.out.println(employee.getName());
```

Here,

`Consumer` becomes a **raw type**.

Compiler treats it as

```java
void accept(Object employee);
```

Now

```java
employee.getName();
```

fails.

Reason

`Object` does not contain

```java
getName()
```

The generic type provides compile-time type safety.

---

# Raw Type vs Generic Type

| Generic Consumer      | Raw Consumer              |
| --------------------- | ------------------------- |
| `Consumer<Employee>`  | `Consumer`                |
| `accept(Employee)`    | `accept(Object)`          |
| Type-safe             | Not type-safe             |
| No casting required   | Casting required          |
| Compile-time checking | Loses generic information |

---

# Reusable Consumers

Instead of repeatedly writing actions,

create reusable Consumers.

```java
Consumer<Employee> print =
        System.out::println;

Consumer<Employee> save =
        employeeRepository::save;

Consumer<Employee> sendMail =
        emailService::send;
```

These Consumers can be reused throughout the application.

---

# Consumer Chaining

Suppose

```java
Consumer<String> upper =
        text -> System.out.println(text.toUpperCase());
```

```java
Consumer<String> length =
        text -> System.out.println(text.length());
```

Instead of

```java
upper.accept("Java");

length.accept("Java");
```

Chain them

```java
Consumer<String> result =
        upper.andThen(length);
```

Execution

```java
result.accept("Java");
```

Output

```text
JAVA
4
```

---

# Execution Order

```text
Input
   │
   ▼
upper.accept()
   │
   ▼
length.accept()
```

`andThen()` executes Consumers sequentially.

---

# Exception Behavior

Suppose

```java
Consumer<String> first =
        text -> {
            throw new RuntimeException();
        };
```

```java
Consumer<String> second =
        text -> System.out.println(text);
```

Now

```java
Consumer<String> combined =
        first.andThen(second);
```

Execution

```java
combined.accept("Java");
```

Result

* `first.accept()` executes
* Exception is thrown
* `second.accept()` is **not executed**

---

# Side Effects

A **side effect** is any operation that modifies external state or interacts with the outside world.

Examples

* Printing to console
* Saving to database
* Writing to a file
* Sending an email
* Making an API call
* Updating shared variables
* Publishing Kafka messages

Consumer is specifically designed for operations that produce side effects.

Example

```java
Consumer<Employee> save =
        employee -> employeeRepository.save(employee);
```

This is an appropriate use of Consumer.

---

# Function vs Consumer

Although Java allows side effects inside a `Function`, it is generally discouraged.

Example

```java
Function<Employee, Employee> save =
        employee -> {

            employeeRepository.save(employee);

            return employee;
        };
```

This compiles successfully.

However,

the Function now performs both

* an action (saving)
* a transformation (returning)

A Consumer expresses the intent more clearly.

```java
Consumer<Employee> save =
        employeeRepository::save;
```

---

# Consumer in Stream API

The most common real-world usage.

```java
employees.stream()
         .forEach(System.out::println);
```

Question

What is

```java
System.out::println
```

Answer

```java
Consumer<Employee>
```

because

```java
forEach()
```

expects

```java
Consumer<? super T>
```

Method Signature

```java
void forEach(
        Consumer<? super T> action
)
```

---

# Consumer vs Predicate

| Consumer            | Predicate             |
| ------------------- | --------------------- |
| Performs an action  | Evaluates a condition |
| Returns `void`      | Returns `boolean`     |
| Method = `accept()` | Method = `test()`     |

Predicate answers

> Should I keep this object?

Consumer answers

> What should I do with this object?

---

# Consumer vs Function

| Consumer            | Function                  |
| ------------------- | ------------------------- |
| Performs an action  | Performs a transformation |
| Returns `void`      | Returns any type          |
| Method = `accept()` | Method = `apply()`        |

Example

Consumer

```java
employee ->
System.out.println(employee);
```

Function

```java
employee ->
employee.getSalary();
```

---

# Consumer vs Supplier

| Consumer               | Supplier              |
| ---------------------- | --------------------- |
| Consumes input         | Produces output       |
| Requires one parameter | Requires no parameter |
| Returns `void`         | Returns a value       |

Example

Consumer

```java
employee ->
System.out.println(employee);
```

Supplier

```java
() ->
new Employee();
```

---

# JVM Perspective

Example

```java
Consumer<Employee> printer =
        System.out::println;
```

Compilation Flow

```text
Lambda / Method Reference
        │
        ▼
Target Functional Interface
(Consumer<Employee>)
        │
        ▼
Locate Abstract Method

void accept(T)
        │
        ▼
Generic Substitution

T = Employee
        │
        ▼
Method Becomes

void accept(Employee)
        │
        ▼
Compiler Generates invokedynamic
        │
        ▼
LambdaMetafactory
        │
        ▼
Consumer Object
```

---

# Real-World Uses

Consumer is commonly used in

* Stream API (`forEach()`)
* Logging
* Database Persistence
* Email Sending
* Notification Services
* Kafka Producers
* RabbitMQ Publishers
* Audit Logging
* Metrics Collection
* Cache Updates

Example

```java
Consumer<Order> publishEvent =
        kafkaProducer::send;
```

---

# Advantages

* Represents actions clearly
* Encourages reusable operations
* Integrates naturally with `forEach()`
* Supports chaining using `andThen()`
* Ideal for operations with side effects
* Improves code readability

---

# Limitations

* Returns no value
* Accepts only one input parameter
* Cannot directly transform data
* Chained Consumers stop executing if an earlier Consumer throws an exception

---

# Frequently Asked Interview Questions

### What is `Consumer<T>`?

A Functional Interface that accepts one input and performs an action without returning a result.

---

### What is the abstract method?

```java
void accept(T t);
```

---

### Why does Consumer have only one generic parameter?

Because its return type is fixed as `void`.

Only the input type needs to be specified.

---

### Why write `Consumer<Employee>` instead of `Consumer`?

The generic parameter allows the compiler to infer

```java
void accept(Employee employee);
```

Without generics,

the raw type becomes

```java
void accept(Object employee);
```

which loses compile-time type safety.

---

### Can a Consumer have side effects?

Yes.

Consumer is specifically designed for operations that produce side effects.

---

### Can a Function also have side effects?

Yes.

Java allows it.

However,

using a Consumer better communicates the intent when the primary goal is to perform an action.

---

### Which Stream method accepts Consumer?

```java
forEach()
```

---

### How are Consumers chained?

Using

```java
andThen()
```

which executes Consumers sequentially.

---

### What happens if the first Consumer throws an exception?

Subsequent Consumers in the chain are not executed.

---

# Key Takeaways

* `Consumer<T>` represents an **action** performed on an object.
* It contains one abstract method: `void accept(T t)`.
* Its return type is always `void`.
* The generic parameter (`T`) determines the type of the lambda parameter.
* Consumers are expected to produce **side effects**, such as logging, saving, or sending notifications.
* `Consumer.andThen()` allows sequential execution of multiple Consumers.
* `Stream.forEach()` is the most common consumer of a `Consumer`.
* Use `Consumer` when the primary intent is to **perform work**, not to **compute** or **transform** a value.

# Day 3 – Functional Programming

## Chapter 6 – Supplier<T> (Interview Notes)

> **Topics Covered**
>
> * Why `Supplier` was introduced
> * What is `Supplier<T>`?
> * Generic Type (`T`)
> * `get()` Method
> * Target Typing
> * Why `Supplier<Employee>` instead of `Supplier`
> * Lazy Evaluation
> * Variable Capture & Effectively Final
> * Stream API Integration
> * Interview Questions

---

# Overview

`Supplier<T>` is one of the four core Functional Interfaces provided by Java.

It represents a **producer**.

A Supplier **accepts no input** and **returns one value**.

Unlike

* `Predicate` → evaluates
* `Function` → transforms
* `Consumer` → performs an action

`Supplier` simply **supplies** or **creates** a value.

It is primarily used for

* Object Creation
* Lazy Initialization
* Random Value Generation
* Configuration Lookup
* Current Date/Time
* Stream Generation
* Factory Methods

---

# Why Was Supplier Introduced?

Suppose your application needs an `Employee`.

There are multiple ways to obtain it.

* Create a new object
* Read from a database
* Read from a cache
* Generate a UUID
* Read configuration
* Get current time

Notice

There is **no input**.

You simply ask

> Give me a value.

Java extracted this behavior into the Functional Interface called **Supplier**.

---

# Definition

`Supplier<T>` is a Functional Interface.

```java
@FunctionalInterface
public interface Supplier<T> {

    T get();

}
```

It contains exactly one abstract method.

```java
T get();
```

Meaning

> Produce and return an object of type `T`.

---

# Understanding Generic Type (T)

Unlike `Function`

```text
Function<T, R>
```

Supplier has only one generic parameter.

```text
Supplier<T>
```

Reason

Supplier has

* No input parameter
* One return value

The generic type represents only the return type.

---

# Generic Type Examples

Example

```java
Supplier<String>
```

Compiler substitutes

```java
String get();
```

---

Example

```java
Supplier<Employee>
```

Compiler substitutes

```java
Employee get();
```

---

Example

```java
Supplier<Integer>
```

Compiler substitutes

```java
Integer get();
```

---

# Visual Representation

```text
No Input
    │
    ▼
 Supplier
    │
    ▼
Output (T)
```

Supplier always produces something.

---

# Basic Example

```java
Supplier<String> greeting =
        () -> "Hello Java";
```

Execution

```java
String value = greeting.get();
```

Output

```text
Hello Java
```

No input is required.

---

# Integer Example

```java
Supplier<Integer> luckyNumber =
        () -> 7;
```

Execution

```java
int number = luckyNumber.get();
```

Output

```text
7
```

---

# Employee Example

```java
Supplier<Employee> employeeSupplier =
        () -> new Employee("John", 70000);
```

Execution

```java
Employee employee =
        employeeSupplier.get();
```

Every call creates and returns an Employee.

---

# Supplier Can Create New Objects

```java
Supplier<List<String>> listSupplier =
        ArrayList::new;
```

Execution

```java
List<String> list =
        listSupplier.get();
```

Each call creates a **new** `ArrayList`.

This is one of the most common real-world uses.

---

# Target Typing

Example

```java
Supplier<Employee> supplier =
        () -> new Employee();
```

Compiler Steps

```text
Supplier<Employee>
        │
        ▼
Target Functional Interface
        │
        ▼
Locate Abstract Method

T get()
        │
        ▼
Generic Substitution

T = Employee
        │
        ▼
Method Becomes

Employee get()
        │
        ▼
Compiler Validates Return Type
        │
        ▼
Compilation Successful
```

The compiler knows the lambda must return an `Employee`.

---

# Why Supplier<Employee> Instead of Supplier?

Example

```java
Supplier<Employee> supplier =
        Employee::new;
```

Compiler infers

```java
Employee get();
```

---

Now consider

```java
Supplier supplier =
        Employee::new;
```

This is a **raw type**.

Compiler treats it as

```java
Object get();
```

Now every retrieval requires casting.

```java
Employee employee =
        (Employee) supplier.get();
```

Using generics provides

* Compile-time type safety
* No casting
* Better readability

---

# Raw Type vs Generic Type

| Generic Supplier      | Raw Supplier              |
| --------------------- | ------------------------- |
| `Supplier<Employee>`  | `Supplier`                |
| `Employee get()`      | `Object get()`            |
| Type-safe             | Not type-safe             |
| No casting            | Casting required          |
| Compile-time checking | Loses generic information |

---

# Lazy Evaluation

One of the biggest advantages of Supplier.

Instead of immediately creating an object

```java
Employee employee =
        new Employee();
```

delay creation.

```java
Supplier<Employee> supplier =
        Employee::new;
```

Nothing is created yet.

Object creation happens only when

```java
supplier.get();
```

is called.

This is known as **Lazy Evaluation**.

---

# Supplier May Return the Same Object

Example

```java
Supplier<String> supplier =
        () -> "Hello";
```

Execution

```java
String a = supplier.get();
String b = supplier.get();
```

Both references point to the same String literal.

```java
a == b
```

Output

```text
true
```

because string literals come from the String Pool.

---

# Supplier May Return New Objects

Example

```java
Supplier<String> supplier =
        () -> new String("Hello");
```

Execution

```java
String a = supplier.get();
String b = supplier.get();
```

Result

```java
a == b
```

Output

```text
false
```

Each call creates a new object.

Supplier itself does **not** guarantee whether it returns the same object or a new one.

That depends entirely on the implementation.

---

# Variable Capture

Example

```java
String message = "Hello";

Supplier<String> supplier =
        () -> message;
```

This compiles because `message` is **effectively final**.

---

Attempting to modify the variable later

```java
message = "Hi";
```

causes a compile-time error.

```
Local variable message defined in an enclosing scope
must be final or effectively final
```

---

# Why Effectively Final?

Local variables live on the **stack**.

Lambda objects live on the **heap**.

When a lambda captures a local variable,

it captures the **value of the reference**, not the stack variable itself.

Conceptually

```java
class GeneratedSupplier
        implements Supplier<String> {

    private final String capturedValue;

    GeneratedSupplier(String value) {
        this.capturedValue = value;
    }

    @Override
    public String get() {
        return capturedValue;
    }

}
```

Therefore,

the captured local variable cannot later be reassigned.

---

# Capturing Object References

Example

```java
Employee employee =
        new Employee("John");

Supplier<Employee> supplier =
        () -> employee;
```

This captures the **reference**.

Modifying the object is allowed.

```java
employee.setName("Alice");
```

Execution

```java
supplier.get().getName();
```

Output

```text
Alice
```

Changing the reference itself

```java
employee =
        new Employee("Bob");
```

causes a compile-time error because the local variable is no longer effectively final.

---

# Supplier in Stream API

The most common Stream usage.

```java
Stream.generate(Math::random)
      .limit(5)
      .forEach(System.out::println);
```

Question

What is

```java
Math::random
```

Answer

```java
Supplier<Double>
```

because

```java
Stream.generate()
```

expects

```java
Supplier<? extends T>
```

Method Signature

```java
static <T> Stream<T> generate(
        Supplier<? extends T> supplier
)
```

---

# Supplier vs Function

| Supplier         | Function                   |
| ---------------- | -------------------------- |
| No input         | One input                  |
| Produces output  | Transforms input to output |
| Method = `get()` | Method = `apply()`         |

Example

Supplier

```java
() -> new Employee()
```

Function

```java
employee ->
employee.getSalary()
```

---

# Supplier vs Consumer

| Supplier        | Consumer       |
| --------------- | -------------- |
| Produces data   | Consumes data  |
| No input        | Requires input |
| Returns a value | Returns `void` |

Supplier

```java
() -> UUID.randomUUID()
```

Consumer

```java
employee ->
System.out.println(employee);
```

---

# Supplier vs Predicate

| Supplier         | Predicate             |
| ---------------- | --------------------- |
| Produces a value | Evaluates a condition |
| No input         | Requires input        |
| Returns any type | Returns boolean       |

---

# Side Effects

A Supplier is often implemented as a pure function.

```java
Supplier<String> message =
        () -> "Hello";
```

However,

Java allows side effects.

Example

```java
Supplier<Employee> supplier = () -> {

    logger.info("Creating Employee");

    return new Employee();

};
```

This compiles successfully.

Java validates only the method signature.

It does not enforce functional purity.

---

# JVM Perspective

Example

```java
Supplier<Employee> supplier =
        Employee::new;
```

Compilation Flow

```text
Lambda / Method Reference
        │
        ▼
Target Functional Interface
(Supplier<Employee>)
        │
        ▼
Locate Abstract Method

T get()
        │
        ▼
Generic Substitution

T = Employee
        │
        ▼
Method Becomes

Employee get()
        │
        ▼
Compiler Generates invokedynamic
        │
        ▼
LambdaMetafactory
        │
        ▼
Supplier Object
```

---

# Real-World Uses

Supplier is heavily used for

* Object Creation
* Factory Methods
* Lazy Initialization
* UUID Generation
* Configuration Retrieval
* Current Time
* Random Values
* Stream Generation
* Dependency Injection
* Optional Default Values

Examples

```java
Supplier<UUID> uuid =
        UUID::randomUUID;
```

```java
Supplier<LocalDateTime> now =
        LocalDateTime::now;
```

```java
Supplier<List<String>> listFactory =
        ArrayList::new;
```

---

# Advantages

* Represents value creation clearly
* Supports lazy initialization
* Eliminates unnecessary object creation
* Integrates naturally with Stream API
* Encourages reusable factories
* Provides compile-time type safety

---

# Limitations

* Accepts no input
* Produces only one value
* May create unnecessary objects if used incorrectly
* Does not guarantee whether a new or existing object is returned

---

# Frequently Asked Interview Questions

### What is `Supplier<T>`?

A Functional Interface that accepts no input and supplies a value.

---

### What is the abstract method?

```java
T get();
```

---

### Why does Supplier have only one generic parameter?

Because it has no input parameter.

The generic type represents only the return type.

---

### Why write `Supplier<Employee>` instead of `Supplier`?

The generic parameter allows the compiler to infer

```java
Employee get();
```

Without generics,

the raw type becomes

```java
Object get();
```

which loses compile-time type safety.

---

### Which Stream method accepts Supplier?

```java
Stream.generate()
```

---

### Can a Supplier have side effects?

Yes.

Java allows side effects.

However,

Suppliers are generally used to provide values or create objects.

---

### What is Lazy Evaluation?

Creating or computing a value only when `get()` is called rather than immediately.

---

### Why must captured local variables be effectively final?

Because lambdas capture the **value of the reference** into the generated lambda object.

Reassigning the local variable would create ambiguity, so Java requires captured local variables to be final or effectively final.

---

### Does a Supplier always create a new object?

No.

A Supplier may

* return the same object,
* return a cached object,
* or create a new object.

The behavior depends entirely on its implementation.

---

# Key Takeaways

* `Supplier<T>` represents a **producer** of values.
* It contains one abstract method: `T get()`.
* It accepts **no input** and returns **one output**.
* The generic parameter (`T`) determines the return type.
* `Supplier` is ideal for object creation, lazy initialization, and factory methods.
* Captured local variables inside lambdas must be **final or effectively final** because lambdas capture the **reference value**, not the stack variable itself.
* `Stream.generate()` is the primary Stream API method that accepts a `Supplier`.
* `Supplier` does not guarantee whether it returns a new object or an existing one; that behavior depends on the implementation.

# Day 3 – Functional Programming

## Chapter 7 – Method References (`::`) (Interview Notes)

> **Topics Covered**
>
> * Why Method References were introduced
> * What is a Method Reference?
> * `::` Operator
> * Target Typing
> * Lambda vs Method Reference
> * Four Types of Method References
> * Constructor References
> * Parameter Matching
> * Stream API Integration
> * JVM Internals
> * Interview Questions

---

# Overview

Method References are a shorthand syntax introduced in Java 8 to make lambda expressions more concise.

Whenever a lambda simply calls an existing method or constructor without adding extra logic, it can usually be replaced with a method reference.

Method references improve:

* Readability
* Maintainability
* Expressiveness

They do **not** improve performance.

Internally, both lambdas and method references are implemented using `invokedynamic` and `LambdaMetafactory`.

---

# Why Were Method References Introduced?

Suppose we have

```java
Function<String, Integer> length =
        text -> text.length();
```

This lambda

* accepts a String
* immediately calls `length()`
* returns the result

There is no additional logic.

Java allows us to replace it with

```java
Function<String, Integer> length =
        String::length;
```

Both behave identically.

Method references simply eliminate unnecessary boilerplate.

---

# Definition

A Method Reference is a shorthand notation for a lambda expression that directly invokes an existing method or constructor.

General Syntax

```java
ClassName::staticMethod
```

```java
ClassName::instanceMethod
```

```java
objectReference::instanceMethod
```

```java
ClassName::new
```

The `::` operator is called the **Method Reference Operator**.

---

# Lambda vs Method Reference

Lambda

```java
Function<String, Integer> length =
        text -> text.length();
```

Method Reference

```java
Function<String, Integer> length =
        String::length;
```

Equivalent behavior.

---

Another example

Lambda

```java
Consumer<String> printer =
        text -> System.out.println(text);
```

Method Reference

```java
Consumer<String> printer =
        System.out::println;
```

---

# Method References Require a Functional Interface

A method reference cannot exist independently.

This is illegal.

```java
String::length;
```

Compiler Error.

Reason

Java does not know which method signature should be implemented.

Correct

```java
Function<String, Integer> length =
        String::length;
```

The Functional Interface provides the target type.

---

# Target Typing

Example

```java
Function<String, Integer> length =
        String::length;
```

Compiler Steps

```text
Function<String, Integer>
        │
        ▼
Target Functional Interface
        │
        ▼
Locate SAM

R apply(T)
        │
        ▼
Generic Substitution

T = String
R = Integer
        │
        ▼
Method becomes

Integer apply(String)
        │
        ▼
Can String::length satisfy it?

Yes

Compilation Successful
```

---

# Equivalent Lambda

Method Reference

```java
String::length
```

Compiler treats it as

```java
text -> text.length()
```

Exactly the same behavior.

---

# Four Types of Method References

Java supports four types.

| Type                                   | Example          |
| -------------------------------------- | ---------------- |
| Static Method                          | `Math::max`      |
| Instance Method of an Arbitrary Object | `String::length` |
| Instance Method of a Particular Object | `printer::print` |
| Constructor Reference                  | `Employee::new`  |

---

# 1. Static Method Reference

Suppose

```java
class Calculator {

    static Integer square(Integer number) {
        return number * number;
    }

}
```

Lambda

```java
Function<Integer, Integer> square =
        number -> Calculator.square(number);
```

Method Reference

```java
Function<Integer, Integer> square =
        Calculator::square;
```

---

# 2. Instance Method of an Arbitrary Object

Example

```java
Function<String, Integer> length =
        String::length;
```

There is no String object yet.

The object will be supplied later.

Execution

```java
length.apply("Java");
```

Conceptually becomes

```java
"Java".length();
```

---

Another example

```java
Predicate<String> empty =
        String::isEmpty;
```

Equivalent lambda

```java
text -> text.isEmpty();
```

---

# 3. Instance Method of a Particular Object

Suppose

```java
Printer printer =
        new Printer();
```

Lambda

```java
Consumer<String> consumer =
        text -> printer.print(text);
```

Method Reference

```java
Consumer<String> consumer =
        printer::print;
```

The object already exists.

---

# 4. Constructor Reference

Suppose

```java
class Employee {

    Employee() {}

}
```

Lambda

```java
Supplier<Employee> supplier =
        () -> new Employee();
```

Method Reference

```java
Supplier<Employee> supplier =
        Employee::new;
```

Equivalent behavior.

---

# Constructor References with Parameters

Method references are **not limited** to no-argument constructors.

The constructor must match the target Functional Interface.

---

## No-Argument Constructor

Constructor

```java
Employee()
```

Target Interface

```java
Supplier<Employee>
```

SAM

```java
Employee get();
```

Method Reference

```java
Supplier<Employee> supplier =
        Employee::new;
```

---

## One-Parameter Constructor

Constructor

```java
Employee(String name)
```

Target Interface

```java
Function<String, Employee>
```

SAM

```java
Employee apply(String)
```

Method Reference

```java
Function<String, Employee> creator =
        Employee::new;
```

Equivalent lambda

```java
name -> new Employee(name)
```

---

## Two-Parameter Constructor

Constructor

```java
Employee(String name, Integer salary)
```

Target Interface

```java
BiFunction<String, Integer, Employee>
```

SAM

```java
Employee apply(String, Integer)
```

Method Reference

```java
BiFunction<String, Integer, Employee> creator =
        Employee::new;
```

Equivalent lambda

```java
(name, salary) ->
        new Employee(name, salary)
```

---

## Three or More Parameters

Java does **not** provide

* `TriFunction`
* `QuadFunction`

Create a custom Functional Interface.

```java
@FunctionalInterface
interface TriFunction<A, B, C, R> {

    R apply(A a, B b, C c);

}
```

Now

```java
TriFunction<String,
            Integer,
            String,
            Employee> creator =
                    Employee::new;
```

works correctly.

The limitation is the available Functional Interfaces, **not** Method References.

---

# Method References with Static Methods

Suppose

```java
class Calculator {

    static Integer add(Integer a,
                       Integer b) {

        return a + b;

    }

}
```

Method Reference

```java
BiFunction<Integer,
           Integer,
           Integer> add =
                    Calculator::add;
```

Execution

```java
add.apply(5, 5);
```

Conceptually becomes

```java
Calculator.add(5, 5);
```

---

# Parameter Matching Rule

The compiler does not match only by parameter count.

It matches the **entire SAM signature**.

It compares

* Parameter Count
* Parameter Types
* Return Type

If compatible,

the Method Reference compiles.

Visual Representation

```text
Functional Interface
        │
        ▼
Single Abstract Method
        │
        ▼
Compiler compares signature
        │
        ▼
Existing Method / Constructor
        │
        ▼
Compatible?
        │
     Yes ▼
Compilation Successful
```

---

# Method References in Stream API

Without Method References

```java
employees.stream()
         .map(employee -> employee.getName())
         .forEach(name -> System.out.println(name));
```

With Method References

```java
employees.stream()
         .map(Employee::getName)
         .forEach(System.out::println);
```

Much cleaner.

---

# Method References and Functional Interfaces

Examples

Predicate

```java
Predicate<String> empty =
        String::isEmpty;
```

---

Function

```java
Function<Employee, String> name =
        Employee::getName;
```

---

Consumer

```java
Consumer<Employee> printer =
        System.out::println;
```

---

Supplier

```java
Supplier<Employee> supplier =
        Employee::new;
```

Method references always implement a Functional Interface.

---

# JVM Perspective

Example

```java
Function<String, Integer> length =
        String::length;
```

Compilation Flow

```text
Method Reference
        │
        ▼
Target Functional Interface
(Function<String,Integer>)
        │
        ▼
Locate SAM

Integer apply(String)
        │
        ▼
Compiler validates compatibility
        │
        ▼
Generate invokedynamic
        │
        ▼
LambdaMetafactory
        │
        ▼
Function Object
```

No anonymous class is generated.

---

# Lambda vs Method Reference

| Lambda                          | Method Reference              |
| ------------------------------- | ----------------------------- |
| More flexible                   | More concise                  |
| Can contain multiple statements | Calls an existing method only |
| Can contain custom logic        | Cannot add new logic          |
| More verbose                    | More readable                 |

---

# Advantages

* Reduces boilerplate
* Improves readability
* Makes Stream pipelines cleaner
* Encourages reusable methods
* Works naturally with Functional Interfaces
* Same runtime performance as lambdas

---

# Limitations

* Cannot contain additional logic
* Requires a compatible Functional Interface
* May be less readable if overused with overloaded methods

---

# Frequently Asked Interview Questions

### What is a Method Reference?

A shorthand syntax for a lambda expression that directly invokes an existing method or constructor.

---

### What is the Method Reference operator?

```java
::
```

---

### Can a Method Reference exist without a Functional Interface?

No.

It requires a target Functional Interface so the compiler can determine the Single Abstract Method.

---

### What are the four types of Method References?

* Static Method Reference
* Instance Method of an Arbitrary Object
* Instance Method of a Particular Object
* Constructor Reference

---

### Are Method References faster than Lambdas?

No.

Both compile to nearly identical bytecode using `invokedynamic` and `LambdaMetafactory`.

Method References improve readability, not performance.

---

### Can Method References be used with constructors having parameters?

Yes.

The constructor signature must match the target Functional Interface.

Examples

* `Supplier` → No parameters
* `Function` → One parameter
* `BiFunction` → Two parameters
* Custom `TriFunction` → Three parameters

---

### Can Method References work with methods having multiple parameters?

Yes.

Example

```java
BiFunction<Integer,
           Integer,
           Integer> add =
                    Calculator::add;
```

Execution

```java
add.apply(5, 5);
```

Conceptually becomes

```java
Calculator.add(5, 5);
```

---

### How does the compiler resolve a Method Reference?

The compiler compares the Functional Interface's Single Abstract Method with the referenced method or constructor.

It checks

* Parameter count
* Parameter types
* Return type

If the signatures are compatible, the Method Reference is accepted.

---

# Key Takeaways

* Method References are a concise form of lambda expressions.
* They require a target Functional Interface.
* The compiler resolves them using the Functional Interface's Single Abstract Method (SAM).
* Java supports four types of Method References:

    * Static Method
    * Instance Method of an Arbitrary Object
    * Instance Method of a Particular Object
    * Constructor Reference
* Method References can target methods and constructors with **any number of parameters**, provided a compatible Functional Interface exists.
* The compiler matches the complete method signature, not just the parameter count.
* Internally, Method References use `invokedynamic` and `LambdaMetafactory`, just like lambda expressions.
