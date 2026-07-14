# Day 7 - Module 1: Optional (Part 1)

## What is Optional?

`Optional<T>` is a **final generic container class** introduced in **Java 8** to explicitly represent the **presence or absence of a value**.

Instead of returning `null`, methods can return an `Optional`.

```java
Optional<User> findUser(int id);
```

This tells the caller that the method **may not return a value**.

---

# Why was Optional Introduced?

## Problem Before Java 8

Before Optional, methods commonly returned `null`.

```java
public User findUser(int id) {
    return null;
}
```

Caller:

```java
User user = findUser(1);
System.out.println(user.getName());
```

Output:

```
NullPointerException
```

The problem was **not `null` itself**, but that **the method signature did not indicate that the value could be absent**.

```java
public User findUser(int id)
```

Looking at this method, you cannot determine whether:

- It always returns a User
- It may return null
- A null check is required

This ambiguity often leads to runtime errors.

---

# Solution

Optional makes the possibility of absence explicit.

```java
Optional<User> findUser(int id)
```

The caller immediately knows that the result must be handled carefully.

---

# Optional Does NOT Remove Null

A common misconception is that Optional eliminates null.

This is false.

Internally, Optional still stores a reference that may be null.

Conceptually:

```
Optional
----------------
value = null
----------------
```

The difference is that **the null is encapsulated inside the Optional object**, so developers interact with methods such as:

- isPresent()
- ifPresent()
- orElse()
- orElseGet()

instead of directly checking for null.

---

# Why Didn't Java Remove Null?

Java already had decades of existing applications.

Removing null would have broken backward compatibility.

Instead, Java introduced Optional as a wrapper class.

---

# Internal Structure

Conceptually:

```java
public final class Optional<T> {

    private final T value;

}
```

Optional contains only **one instance variable**.

```java
private final T value;
```

This field stores:

- the actual object
- or null

---

# Why is Optional Final?

```java
public final class Optional<T>
```

Reasons:

- Prevent inheritance
- Maintain predictable behavior
- Preserve immutability
- Avoid unexpected subclasses

---

# Why is value Final?

```java
private final T value;
```

Once an Optional object is created, its value cannot be changed.

```java
Optional<String> name = Optional.of("Java");
```

This is impossible:

```java
name.value = "Spring";
```

Instead, create a new Optional.

---

# Optional is Immutable

```
Optional
    |
    +---- value = "Java"
```

The value reference never changes after construction.

Benefits:

- Thread-safe (with respect to its own state)
- Predictable behavior
- Easier reasoning

---

# Factory Methods

## Optional.of()

Use when the value is guaranteed to be non-null.

```java
Optional<String> name =
        Optional.of("Guru");
```

Passing null:

```java
Optional.of(null);
```

throws

```
NullPointerException
```

---

## Optional.ofNullable()

Use when the value may be null.

```java
String name = repository.findName();

Optional<String> optional =
        Optional.ofNullable(name);
```

If name is null:

```
Optional.empty()
```

is returned.

---

## Optional.empty()

Creates an empty Optional.

```java
Optional<User> user =
        Optional.empty();
```

Conceptually:

```
Optional
-----------
value = null
-----------
```

---

# Comparison

| Method | Accepts null | Result |
|----------|-------------|--------|
| Optional.of() | ❌ No | Throws NullPointerException |
| Optional.ofNullable() | ✅ Yes | Empty Optional if null |
| Optional.empty() | N/A | Empty Optional |

---

# Real Backend Example

Repository:

```java
Optional<User> findById(Long id);
```

Service:

```java
Optional<User> user = repository.findById(id);

user.ifPresent(System.out::println);
```

The method signature clearly communicates that the user may not exist.

---

# Best Practices

✅ Return Optional from methods when a value may be absent.

✅ Use Optional to improve API readability.

✅ Prefer Optional over returning null from service/repository methods.

✅ Use Optional as a return type only.

---

# Key Points

- Introduced in Java 8
- Represents optional values
- Reduces accidental NullPointerExceptions
- Makes APIs self-documenting
- Immutable
- Final class
- Contains only one field: value
- Internally may still contain null
- Does not replace null throughout Java

---

# Interview Questions

### Why was Optional introduced?

To explicitly represent the absence of a value and reduce accidental NullPointerExceptions.

---

### Does Optional remove null?

No.

It wraps a reference that may internally be null.

---

### Why is Optional immutable?

Because its value field is final and cannot be modified after creation.

---

### Difference between of() and ofNullable()?

**of()**

- Accepts only non-null values.
- Throws NullPointerException if null is passed.

**ofNullable()**

- Accepts both null and non-null values.
- Returns Optional.empty() if the value is null.

---

### Is Optional a replacement for null?

No.

It is a wrapper that makes the possibility of absence explicit.

# Day 7 - Module 1: Optional (Part 2)

# Working with Optional

Optional provides several methods to safely work with values that may or may not exist.

---

# 1. isPresent()

## Purpose

Checks whether the Optional contains a value.

```java
Optional<String> name = Optional.of("Guru");

System.out.println(name.isPresent());
```

Output

```
true
```

Empty Optional

```java
Optional<String> name = Optional.empty();

System.out.println(name.isPresent());
```

Output

```
false
```

---

## Internal Implementation (Conceptual)

```java
public boolean isPresent() {
    return value != null;
}
```

Only one null comparison is performed.

Time Complexity

```
O(1)
```

---

## When to Use

Useful when you absolutely need conditional logic.

```java
if(optional.isPresent()){
    System.out.println(optional.get());
}
```

However, in most cases `ifPresent()` is preferred.

---

# 2. isEmpty()

Introduced in Java 11.

Instead of

```java
if(!optional.isPresent())
```

write

```java
if(optional.isEmpty())
```

Conceptual implementation

```java
public boolean isEmpty() {
    return value == null;
}
```

Equivalent to

```java
!isPresent()
```

---

# 3. get()

Returns the contained value.

```java
Optional<String> name =
        Optional.of("Guru");

System.out.println(name.get());
```

Output

```
Guru
```

---

Empty Optional

```java
Optional<String> name =
        Optional.empty();

name.get();
```

Throws

```
NoSuchElementException
```

---

## Internal Implementation (Conceptual)

```java
public T get() {

    if(value == null){
        throw new NoSuchElementException();
    }

    return value;
}
```

---

## Why is get() Discouraged?

Using

```java
User user = optional.get();
```

without checking defeats the purpose of Optional.

It simply replaces

```
NullPointerException
```

with

```
NoSuchElementException
```

---

## Better Alternatives

```java
optional.orElse(defaultValue);

optional.orElseGet(...);

optional.orElseThrow();

optional.ifPresent(...);
```

---

# 4. ifPresent()

Executes code only when a value exists.

Instead of

```java
if(optional.isPresent()){

    User user = optional.get();

    System.out.println(user);

}
```

write

```java
optional.ifPresent(System.out::println);
```

---

## Internal Implementation (Conceptual)

```java
public void ifPresent(Consumer<T> action){

    if(value != null){

        action.accept(value);

    }

}
```

---

## Parameter

```
Consumer<T>
```

Example

```java
optional.ifPresent(user ->
    System.out.println(user.getName())
);
```

If the Optional is empty

- Nothing happens
- No exception
- No null check required

---

# Why Prefer ifPresent()

Instead of asking

```
Do you have a value?
```

you simply say

```
If you have one,
execute this code.
```

This is functional programming style.

---

# 5. map()

Transforms the contained value while preserving the Optional.

Example

```java
Optional<User> user =
        repository.findById(id);

Optional<String> name =
        user.map(User::getName);
```

Transformation

```
Optional<User>

↓

Optional<String>
```

---

## Internal Implementation (Conceptual)

```java
public<U> Optional<U> map(
        Function<T,U> mapper){

    if(value == null){

        return Optional.empty();

    }

    return Optional.ofNullable(

        mapper.apply(value)

    );

}
```

---

## Parameter

```
Function<T,R>
```

Because

```
User

↓

String
```

is a transformation.

---

## Real Backend Example

```java
repository.findById(id)

          .map(User::getAddress)

          .map(Address::getCity)

          .map(City::getPinCode);
```

No nested null checks.

---

## Time Complexity

```
O(1)
```

---

## Important

map() does not modify the existing Optional.

It returns a **new Optional**.

---

# 6. flatMap()

Used when the mapping function already returns an Optional.

Suppose

```java
Optional<User>
```

and

```java
User.getAddress()
```

returns

```java
Optional<Address>
```

Using map()

```
Optional<Optional<Address>>
```

Nested Optional.

Using flatMap()

```
Optional<Address>
```

---

## Internal Implementation (Conceptual)

```java
public<U> Optional<U> flatMap(

Function<T, Optional<U>> mapper){

    ...
}
```

The mapper already returns an Optional.

Therefore Java does not wrap it again.

---

# map() vs flatMap()

| map() | flatMap() |
|--------|-----------|
| Mapper returns a normal object | Mapper returns Optional |
| Wraps result into Optional | Returns mapper's Optional directly |
| Can create nested Optional | Prevents nested Optional |

Example

```
map()

User

↓

String

↓

Optional<String>
```

```
flatMap()

User

↓

Optional<Address>

↓

Optional<Address>
```

---

# Best Practices

✅ Prefer ifPresent() over isPresent() + get()

✅ Use map() to transform values

✅ Use flatMap() when mapper already returns Optional

✅ Avoid calling get() directly

---

# Common Mistakes

❌

```java
if(optional.isPresent()){

    optional.get();

}
```

Prefer

```java
optional.ifPresent(...);
```

---

❌

```java
Optional<Optional<User>>
```

Use

```java
flatMap()
```

instead.

---

# Key Points

- isPresent() checks for existence.
- isEmpty() checks for absence.
- get() may throw NoSuchElementException.
- ifPresent() executes only when a value exists.
- map() transforms values.
- flatMap() prevents nested Optional.
- Optional is immutable.
- Every transformation creates a new Optional.

---

# Interview Questions

### Why is get() discouraged?

Because it throws NoSuchElementException when empty and defeats the purpose of Optional.

---

### Difference between isPresent() and ifPresent()?

- isPresent() only checks for a value.
- ifPresent() executes a Consumer when a value exists.

---

### Difference between map() and flatMap()?

map()

- Mapper returns a normal object.
- Java wraps the result in an Optional.

flatMap()

- Mapper returns an Optional.
- Java returns it directly without creating Optional<Optional<T>>.

---

### Does map() modify the existing Optional?

No.

It returns a new immutable Optional.

---

### What functional interfaces are used?

| Method | Functional Interface |
|---------|----------------------|
| ifPresent() | Consumer<T> |
| map() | Function<T,R> |
| flatMap() | Function<T, Optional<R>> |

# Day 7 - Module 1: Optional (Part 3)

# Default Values, Exception Handling & Best Practices

This section covers the remaining Optional operations that are heavily used in production applications and frequently asked in Java interviews.

---

# 1. filter()

## Purpose

Filters the contained value based on a condition.

If the condition is true

```
Keep the value
```

If the condition is false

```
Return Optional.empty()
```

---

## Example

```java
Optional<Integer> number =
        Optional.of(20);

Optional<Integer> result =
        number.filter(n -> n > 10);

System.out.println(result);
```

Output

```
Optional[20]
```

---

Another example

```java
Optional<Integer> result =
        number.filter(n -> n > 50);

System.out.println(result);
```

Output

```
Optional.empty
```

---

## Internal Implementation (Conceptual)

```java
public Optional<T> filter(
        Predicate<T> predicate){

    if(value == null){
        return this;
    }

    if(predicate.test(value)){
        return this;
    }

    return Optional.empty();
}
```

---

## Functional Interface Used

```
Predicate<T>
```

Reason

```
Predicate

↓

Returns boolean
```

---

## Real Backend Example

```java
repository.findById(id)
          .filter(User::isActive)
          .ifPresent(System.out::println);
```

Inactive users are automatically ignored.

---

# 2. orElse()

## Purpose

Returns the value if present.

Otherwise returns a default value.

Example

```java
Optional<String> city =
        Optional.empty();

String result =
        city.orElse("Chennai");

System.out.println(result);
```

Output

```
Chennai
```

---

When value exists

```java
Optional<String> city =
        Optional.of("Bangalore");

String result =
        city.orElse("Chennai");
```

Output

```
Bangalore
```

---

## Internal Implementation (Conceptual)

```java
public T orElse(T other){

    return value != null
            ? value
            : other;
}
```

---

## Important

`orElse()` performs **eager evaluation**.

Example

```java
String result =
optional.orElse(createDefault());
```

Even if Optional contains a value,

```
createDefault()
```

is executed.

Reason

Java evaluates method arguments before calling a method.

Conceptually

```
Step 1

createDefault()

↓

Step 2

orElse(result)
```

---

# 3. orElseGet()

## Purpose

Returns the value if present.

Otherwise calls a Supplier to create the default value.

Example

```java
String result =
optional.orElseGet(() -> createDefault());
```

Unlike `orElse()`, the supplier is executed **only if the Optional is empty**.

---

## Internal Implementation (Conceptual)

```java
public T orElseGet(
        Supplier<T> supplier){

    if(value != null){
        return value;
    }

    return supplier.get();
}
```

---

## Functional Interface Used

```
Supplier<T>
```

---

## Lazy Evaluation

```
orElse()

↓

Always evaluates default value
```

```
orElseGet()

↓

Evaluates only when Optional is empty
```

---

## When to Use

Use `orElse()` for

- Constants
- Small objects
- Cheap values

Example

```java
optional.orElse("Unknown");
```

---

Use `orElseGet()` for

- Database queries
- API calls
- File reading
- Object creation
- Expensive computations

Example

```java
optional.orElseGet(() -> repository.loadDefaultUser());
```

---

# orElse() vs orElseGet()

| orElse() | orElseGet() |
|-----------|-------------|
| Eager evaluation | Lazy evaluation |
| Always evaluates default | Evaluates only when needed |
| Accepts object | Accepts Supplier |
| Can waste resources | Better for expensive operations |

---

# 4. orElseThrow()

## Purpose

Returns the value if present.

Otherwise throws an exception.

Example

```java
User user =
repository.findById(id)
          .orElseThrow();
```

Throws

```
NoSuchElementException
```

if empty.

---

## Internal Implementation (Conceptual)

```java
public T orElseThrow(){

    if(value == null){
        throw new NoSuchElementException();
    }

    return value;
}
```

---

## Custom Exception

```java
User user =
repository.findById(id)
          .orElseThrow(
                () -> new UserNotFoundException()
          );
```

---

## Functional Interface Used

```
Supplier<Exception>
```

The exception object is created only when required.

---

## Spring Boot Example

```java
User user =
userRepository.findById(id)
              .orElseThrow(
                    () -> new ResourceNotFoundException("User not found")
              );
```

This is the recommended production approach.

---

# 5. ifPresentOrElse()

Introduced in Java 9.

Executes one action if a value exists.

Executes another action if it does not.

Example

```java
optional.ifPresentOrElse(

        System.out::println,

        () -> System.out.println("Empty")

);
```

---

Internally it uses

```
Consumer<T>

+

Runnable
```

---

# 6. or()

Introduced in Java 9.

Provides another Optional when the first one is empty.

Example

```java
repo1.findById(id)
     .or(() -> repo2.findById(id));
```

Useful for fallback repositories or cache lookups.

---

# Performance Summary

| Method | Functional Interface | Lazy |
|---------|----------------------|------|
| filter() | Predicate | Yes |
| map() | Function | Yes |
| flatMap() | Function | Yes |
| ifPresent() | Consumer | Yes |
| orElse() | None | No |
| orElseGet() | Supplier | Yes |
| orElseThrow() | Supplier | Yes |
| ifPresentOrElse() | Consumer + Runnable | Yes |
| or() | Supplier<Optional<T>> | Yes |

---

# Common Mistakes

## 1. Using Optional as Entity Fields

❌

```java
class User{

    Optional<String> name;

}
```

Problems

- Extra object creation
- More memory usage
- Serialization issues
- JPA/Hibernate compatibility problems

Correct

```java
class User{

    String name;

}
```

---

## 2. Using Optional as Method Parameters

❌

```java
void save(Optional<User> user)
```

Correct

```java
void save(User user)
```

Optional is intended for **return types**, not parameters.

---

## 3. Returning null Instead of Optional

Wrong

```java
Optional<User> find(){

    return null;

}
```

Correct

```java
return Optional.empty();
```

---

## 4. Calling get() Directly

Wrong

```java
optional.get();
```

Better

```java
optional.orElseThrow();
```

or

```java
optional.ifPresent(...);
```

---

# Best Practices

✅ Use Optional only as a return type.

✅ Prefer `ifPresent()` over `isPresent() + get()`.

✅ Use `map()` to transform values.

✅ Use `flatMap()` when mapper already returns Optional.

✅ Use `filter()` to remove unwanted values.

✅ Use `orElseGet()` for expensive default values.

✅ Use `orElseThrow()` for mandatory values.

✅ Return `Optional.empty()` instead of `null`.

---

# Key Takeaways

- `filter()` conditionally keeps the value.
- `orElse()` eagerly evaluates its argument.
- `orElseGet()` lazily evaluates its supplier.
- `orElseThrow()` throws an exception if empty.
- `ifPresentOrElse()` handles both present and absent cases.
- `or()` provides an alternative Optional.
- Avoid Optional in entity fields and method parameters.
- Prefer Optional for method return types only.

---

# Interview Questions

## Why is `orElseGet()` better than `orElse()`?

`orElseGet()` uses lazy evaluation through a `Supplier`, so the default value is created only when the Optional is empty. `orElse()` always evaluates its argument, even when it isn't needed.

---

## Why shouldn't Optional be used as an entity field?

- Creates unnecessary wrapper objects.
- Increases memory usage.
- Causes issues with serialization and ORM frameworks.
- Optional is designed for method return values, not object state.

---

## Which functional interfaces are used?

| Method | Functional Interface |
|---------|----------------------|
| filter() | Predicate<T> |
| map() | Function<T,R> |
| flatMap() | Function<T, Optional<R>> |
| ifPresent() | Consumer<T> |
| orElseGet() | Supplier<T> |
| orElseThrow() | Supplier<? extends Throwable> |
| ifPresentOrElse() | Consumer<T> + Runnable |
| or() | Supplier<Optional<T>> |

---

# Production Usage

Typical Spring Boot service method

```java
public User getUser(Long id) {

    return userRepository.findById(id)
            .filter(User::isActive)
            .orElseThrow(
                () -> new ResourceNotFoundException("User not found")
            );
}
```

This combines filtering, absence handling, and custom exception handling into a clean, readable, production-ready pipeline.

# Day 7 - Module 2: JVM Architecture

# JVM Architecture (Deep Dive)

## Goal

Understand what happens inside the JVM from the moment a Java program starts until it terminates.

---

# Why Was JVM Introduced?

Before Java, programs were **platform dependent**.

Example

```
C Source Code

↓

Windows Compiler

↓

Windows.exe
```

The executable generated for Windows cannot run on Linux or macOS.

To run the same application on Linux, it must be compiled again using a Linux compiler.

Problems:

- Platform dependent
- Multiple binaries
- Difficult maintenance
- Difficult deployment

---

# Java's Solution

Java introduced the concept of

```
Write Once, Run Anywhere (WORA)
```

Instead of compiling directly into machine code, Java compiles into **Bytecode**.

```
Java Source (.java)

↓

javac

↓

Bytecode (.class)

↓

JVM

↓

Machine Code
```

Every operating system has its own JVM implementation.

```
Same Bytecode

↓

Windows JVM

↓

Windows Machine Code
```

```
Same Bytecode

↓

Linux JVM

↓

Linux Machine Code
```

```
Same Bytecode

↓

macOS JVM

↓

macOS Machine Code
```

Therefore the same `.class` file can execute on any platform that has a compatible JVM.

---

# What is JVM?

**JVM (Java Virtual Machine)** is a software-based virtual machine responsible for executing Java bytecode.

It acts as an abstraction layer between Java applications and the operating system.

```
Java Program

↓

JVM

↓

Operating System

↓

Hardware
```

---

# Responsibilities of JVM

The JVM performs several important tasks:

- Loads classes into memory.
- Verifies bytecode for safety.
- Creates runtime memory areas.
- Executes bytecode.
- Performs Just-In-Time (JIT) compilation.
- Manages memory.
- Performs Garbage Collection.
- Provides security.
- Supports multithreading.
- Enables platform independence.

---

# JDK vs JRE vs JVM

This is one of the most common interview questions.

---

## JVM (Java Virtual Machine)

Responsibilities

- Executes bytecode.
- Manages memory.
- Performs Garbage Collection.
- Uses Interpreter and JIT Compiler.
- Provides runtime environment.

```
JVM

↓

Executes Java Bytecode
```

---

## JRE (Java Runtime Environment)

Contains

- JVM
- Java Standard Libraries
- Runtime resources

Purpose

Used to **run Java applications**.

Cannot compile Java source code.

```
JRE

├── JVM
└── Java Libraries
```

---

## JDK (Java Development Kit)

Contains

- JRE
- JVM
- javac compiler
- javadoc
- jar
- jdb
- Other development tools

Purpose

Used to **develop and run Java applications**.

```
JDK

├── JRE
│   ├── JVM
│   └── Libraries
│
├── javac
├── jar
├── javadoc
└── jdb
```

---

# Relationship

```
JDK

↓

JRE

↓

JVM
```

Remember

```
JDK ⊃ JRE ⊃ JVM
```

---

# What Happens When We Run a Java Program?

Example

```java
public class Main {

    public static void main(String[] args) {

        System.out.println("Hello JVM");

    }

}
```

Command

```bash
java Main
```

Execution Flow

---

## Step 1

The Operating System creates a new JVM process.

```
Operating System

↓

JVM Process
```

---

## Step 2

The Class Loader loads required classes.

Examples

```
Main.class

Object.class

String.class

System.class
```

---

## Step 3

Bytecode Verification

The JVM verifies that the bytecode is valid.

Checks include

- Correct instructions
- Type safety
- Proper stack usage
- No illegal memory access

If verification fails

```
VerifyError
```

is thrown.

---

## Step 4

The JVM creates Runtime Data Areas.

```
Runtime Data Areas

├── Heap
├── Stack
├── Metaspace
├── Program Counter Register
└── Native Method Stack
```

These memory areas are used during program execution.

---

## Step 5

The JVM creates the Main Thread.

Each thread receives

- One Stack
- One Program Counter Register

---

## Step 6

Execution Engine starts executing bytecode.

Initially

```
Interpreter
```

executes bytecode instruction by instruction.

Frequently executed code ("hot code") is then compiled by the

```
JIT Compiler
```

into native machine code for better performance.

---

## Step 7

Garbage Collector starts running in the background.

Responsibilities

- Finds unreachable objects.
- Reclaims unused heap memory.
- Prevents memory exhaustion.

---

# High-Level JVM Architecture

```
                Java Source
                     │
                  javac
                     │
              Bytecode (.class)
                     │
              Class Loader
                     │
           Bytecode Verifier
                     │
        Runtime Data Areas
                     │
        Execution Engine
                     │
      Native Machine Code
                     │
           Operating System
                     │
                Hardware
```

---

# Runtime Data Areas

```
JVM Runtime

├── Heap
├── Stack
├── Metaspace
├── PC Register
└── Native Method Stack
```

We will study each area separately in the upcoming modules.

---

# Major Components of JVM

## 1. Class Loader

Loads `.class` files into memory.

---

## 2. Bytecode Verifier

Checks bytecode safety before execution.

---

## 3. Runtime Data Areas

Stores

- Objects
- Local variables
- Class metadata
- Method execution data

---

## 4. Execution Engine

Responsible for executing bytecode.

Contains

- Interpreter
- JIT Compiler

---

## 5. Garbage Collector

Automatically removes unreachable objects from Heap memory.

---

## 6. Java Native Interface (JNI)

Allows Java code to call native C/C++ libraries.

---

# Why Backend Engineers Should Know JVM

Understanding JVM internals helps in

- Diagnosing `OutOfMemoryError`
- Diagnosing `StackOverflowError`
- Debugging memory leaks
- Optimizing application performance
- Tuning Garbage Collection
- Understanding multithreading
- Explaining JVM behavior in interviews

---

# Key Takeaways

- JVM executes Java bytecode.
- Java achieves platform independence through bytecode and JVM.
- JDK contains JRE.
- JRE contains JVM.
- JVM creates runtime memory areas.
- JVM verifies bytecode before execution.
- Execution Engine consists of Interpreter and JIT Compiler.
- Garbage Collector automatically manages heap memory.

---

# Interview Questions

## What is JVM?

A software-based virtual machine that executes Java bytecode, manages memory, performs garbage collection, verifies bytecode, and optimizes execution using JIT compilation.

---

## Why is Java platform independent?

Java source code is compiled into platform-independent bytecode. Each operating system provides its own JVM, which converts the bytecode into native machine code.

---

## Difference between JDK, JRE, and JVM?

| JDK | JRE | JVM |
|-----|-----|-----|
| Development Kit | Runtime Environment | Virtual Machine |
| Contains compiler and tools | Contains JVM and libraries | Executes bytecode |
| Used for development | Used for execution | Used for runtime execution |

---

## What happens when `java Main` is executed?

1. JVM process starts.
2. Class Loader loads required classes.
3. Bytecode is verified.
4. Runtime memory areas are created.
5. Main thread is created.
6. Execution Engine executes bytecode.
7. Garbage Collector starts managing memory.

---

## Important Formula

```
Java Source

↓

javac

↓

Bytecode (.class)

↓

JVM

↓

Machine Code
```

# Day 7 - Module 3: Stack Memory (Deep Dive)

# Stack Memory

## Goal

Understand:

- What Stack Memory is
- Why every thread has its own stack
- Stack Frames
- Local Variable Table
- Operand Stack
- Return Address
- Method execution
- StackOverflowError
- Stack vs Heap

---

# Why Do We Need Stack Memory?

Whenever a Java program runs, methods are continuously called.

Example

```java
public class Main {

    public static void main(String[] args) {

        greet();

    }

    static void greet() {

        System.out.println("Hello");

    }

}
```

Questions:

- Which method is currently executing?
- Where should execution return after `greet()` finishes?
- Where are local variables stored?
- How are nested method calls managed?

The JVM solves these problems using **Stack Memory**.

---

# What is Stack Memory?

Stack Memory is a **thread-private runtime memory area** used to manage **method execution**.

It stores:

- Method calls
- Stack Frames
- Local variables
- Method parameters
- Operand Stack
- Return addresses
- Frame metadata

---

# Stack Follows LIFO

Stack uses

```
LIFO

Last In

First Out
```

Example

```
Top

Method C

Method B

Method A

Bottom
```

Execution order

```
Method C finishes

↓

Method B finishes

↓

Method A finishes
```

The last method called is always the first one to return.

---

# Why Does Every Thread Have Its Own Stack?

Suppose two threads share one stack.

Thread 1

```
main()

↓

calculate()

↓

save()
```

Thread 2

```
main()

↓

login()

↓

authenticate()
```

If both shared the same stack

```
main()

calculate()

login()

save()

authenticate()
```

Problems

- Method executions become mixed.
- Return addresses become incorrect.
- Local variables interfere.
- Execution becomes unpredictable.

---

## Solution

Each thread gets its own private stack.

```
Thread 1

Stack

main()

calculate()

save()
```

```
Thread 2

Stack

main()

login()

authenticate()
```

Benefits

- Thread safety
- No synchronization required
- Independent execution
- Faster access

---

# Stack Creation

Whenever a thread starts

```
new Thread()

↓

JVM

↓

Creates a new Stack
```

When the thread finishes

```
Stack

↓

Destroyed automatically
```

No Garbage Collection is required.

---

# What is a Stack Frame?

Every method call creates one Stack Frame.

Example

```java
public class Demo {

    public static void main(String[] args) {

        add(10,20);

    }

    static int add(int a,int b){

        int sum = a + b;

        return sum;

    }

}
```

Execution

```
main()

↓

add()
```

Stack

```
Top

add()

main()
```

When `add()` completes

```
Top

main()
```

The `add()` frame is automatically removed.

---

# Stack Frame Structure

Every Stack Frame contains

```
Stack Frame

──────────────────────

Local Variable Table

Operand Stack

Return Address

Frame Metadata

──────────────────────
```

---

# 1. Local Variable Table

Stores

- Primitive local variables
- Object references
- Method parameters

Example

```java
static void test(){

    int age = 25;

    String name = "Guru";

}
```

Stored

```
Slot 0

age = 25
```

```
Slot 1

reference → String Object
```

Important

Only the **reference** is stored.

The actual object is stored in Heap Memory.

---

## Example

```java
User user = new User();
```

Memory

```
Stack

user

↓

Heap

User Object
```

---

# Local Variables

Stored in Stack

```java
int x = 10;

double salary = 5000;

boolean active = true;
```

All primitive local variables remain inside the Local Variable Table.

---

# Object References

Stored in Stack

```java
String name = "Guru";

User user = new User();
```

Objects

```
"Guru"

User
```

are stored in Heap.

Only references exist in Stack.

---

# Method Parameters

Example

```java
void add(int a, int b)
```

The parameters

```
a

b
```

are also stored in the Local Variable Table.

---

# 2. Operand Stack

The Operand Stack is used by the JVM while executing bytecode instructions.

Example

```java
int c = a + b;
```

Conceptually

```
Load a

↓

Push

↓

Load b

↓

Push

↓

Add

↓

Store Result
```

The Operand Stack is temporary working memory used during instruction execution.

---

# 3. Return Address

Suppose

```java
main();

↓

add();

↓

main();
```

After `add()` completes, the JVM must know where to continue execution.

The Return Address stores this location.

Conceptually

```
main()

↓

add()

↓

Return to next instruction
```

---

# 4. Frame Metadata

Stores internal JVM information.

Examples

- Constant Pool reference
- Exception handling information
- Method metadata

This information is used internally by the JVM.

---

# Method Execution

Example

```java
main()

↓

A()

↓

B()

↓

C()
```

Stack

```
Top

C()

B()

A()

main()
```

When `C()` returns

```
Top

B()

A()

main()
```

Then

```
A()

↓

main()
```

Finally

```
main()
```

When `main()` finishes

```
Stack becomes empty.
```

---

# Why is Stack Memory Fast?

Reasons

- Sequential allocation
- Sequential deallocation
- LIFO structure
- No Garbage Collection
- No fragmentation
- Push/Pop operations are simple pointer updates

Therefore Stack Memory is significantly faster than Heap Memory.

---

# Stack Characteristics

| Property | Stack |
|----------|--------|
| Per Thread | ✅ Yes |
| Stores Stack Frames | ✅ Yes |
| Stores Local Variables | ✅ Yes |
| Stores Primitive Variables | ✅ Yes |
| Stores Object References | ✅ Yes |
| Stores Objects | ❌ No |
| Automatic Allocation | ✅ Yes |
| Automatic Deallocation | ✅ Yes |
| Uses Garbage Collector | ❌ No |
| Thread Safe | ✅ Yes |
| Allocation Speed | Very Fast |

---

# Common Misconception

Wrong

```
Objects are stored in Stack.
```

Correct

```
Stack

↓

Object Reference
```

```
Heap

↓

Actual Object
```

Example

```java
Employee employee =
        new Employee();
```

Memory

```
Stack

employee

↓

Heap

Employee Object
```

---

# StackOverflowError

Example

```java
public class Demo {

    static void test(){

        test();

    }

    public static void main(String[] args){

        test();

    }

}
```

Execution

```
test()

↓

test()

↓

test()

↓

test()

↓

...
```

Each call creates another Stack Frame.

Eventually

```
Stack becomes full.
```

JVM throws

```
StackOverflowError
```

Reason

```
Too many Stack Frames
```

Not

```
Heap Memory Problem
```

---

# Stack vs Heap

| Feature | Stack | Heap |
|----------|--------|------|
| Stores | Method execution data | Objects |
| Scope | Thread specific | Shared among threads |
| Allocation | Automatic | Dynamic |
| Deallocation | Automatic | Garbage Collector |
| Speed | Faster | Slower |
| Thread Safety | Naturally thread-safe | Requires synchronization for shared objects |
| Memory Size | Smaller | Larger |

---

# Best Practices

✅ Avoid unnecessary deep recursion.

✅ Prefer iteration if recursion depth can become very large.

✅ Keep local variables inside methods whenever possible.

✅ Understand that object references live in Stack while objects live in Heap.

---

# Key Takeaways

- Stack Memory manages method execution.
- Every thread owns its own Stack.
- Every method call creates a Stack Frame.
- Stack follows the LIFO principle.
- Local variables and method parameters are stored in the Local Variable Table.
- Object references are stored in the Stack; objects themselves are stored in the Heap.
- Operand Stack is used during bytecode execution.
- Return Address tells the JVM where to continue after a method returns.
- Stack allocation is very fast because it requires no Garbage Collection.
- Infinite or very deep recursion causes `StackOverflowError`.

---

# Interview Questions

## What is Stack Memory?

A per-thread runtime memory area used to manage method execution. It stores Stack Frames, local variables, method parameters, operand stacks, return addresses, and frame metadata.

---

## Why does every thread have its own Stack?

To isolate method execution and local variables, ensuring independent execution and thread safety without synchronization.

---

## What is a Stack Frame?

A Stack Frame is created for every method invocation. It contains all the information required to execute that method, including local variables, operand stack, return address, and frame metadata.

---

## Are objects stored in Stack Memory?

No.

Only object references are stored in the Stack.

The actual objects are allocated in Heap Memory.

---

## What causes `StackOverflowError`?

Excessive or infinite method calls (usually recursion) create too many Stack Frames, exhausting the thread's stack memory.

---

## Why is Stack Memory faster than Heap Memory?

Because allocation and deallocation follow the LIFO principle, requiring only pointer updates and no Garbage Collection.

# Day 7 - Module 4: Heap Memory (Deep Dive)

# Heap Memory

## Goal

Understand:

- Why Heap Memory exists
- Object allocation
- Why Heap is shared
- Young Generation
- Eden Space
- Survivor Spaces
- Old Generation
- Object lifecycle
- Object promotion
- Heap vs Stack

---

# Why Do We Need Heap Memory?

Consider the following code.

```java
User user = new User();
```

Question:

Where should the object be stored?

Can it be stored inside Stack Memory?

No.

Why?

Because Stack Memory is used only for **method execution**.

Example

```java
void test(){

    User user = new User();

}
```

When `test()` finishes

```
Stack Frame

↓

Destroyed
```

If the object were stored in Stack Memory, it would also disappear.

However, objects often need to survive beyond the method that created them.

Example

```java
User user = service.findUser();
```

The object is created inside `findUser()` but is returned to the caller.

Therefore, it must continue to exist after the method returns.

This is why Java uses **Heap Memory**.

---

# What is Heap Memory?

Heap Memory is the **runtime memory area where Java objects and arrays are dynamically allocated**.

Characteristics

- Shared by all threads
- Stores objects
- Stores arrays
- Managed by Garbage Collector
- Larger than Stack Memory

Conceptually

```
                JVM

        +----------------------+
        |        Heap          |
        +----------------------+

         ▲        ▲        ▲

     Thread1  Thread2  Thread3
```

Every thread can access objects stored in the Heap.

---

# Why is Heap Shared?

Suppose

Thread 1

```java
User user = repository.findById(1);
```

Thread 2

```java
orderService.placeOrder(user);
```

Both threads need access to the same object.

If every thread had its own Heap

- Objects could not be shared easily.
- Duplicate copies would be required.
- Memory usage would increase significantly.

Therefore Java provides **one shared Heap per JVM**.

---

# Stack vs Heap

Example

```java
User user = new User();
```

Memory Layout

```
Stack

user

↓

Heap

User Object
```

Important

Stack stores

```
Reference
```

Heap stores

```
Actual Object
```

---

# Object Allocation

Example

```java
User user = new User();
```

Conceptual steps

```
Step 1

Allocate memory in Heap.
```

↓

```
Step 2

Initialize instance variables with default values.
```

↓

```
Step 3

Execute constructor.
```

↓

```
Step 4

Return object reference.
```

↓

```
Step 5

Store reference inside Stack.
```

Visualization

```
Heap

+---------------+

User Object

+---------------+

↓

Reference

↓

Stack

user ─────────► User Object
```

---

# Heap Structure

Modern JVM divides Heap into generations.

```
Heap

├── Young Generation

│     ├── Eden

│     ├── Survivor 0 (S0)

│     └── Survivor 1 (S1)

│

└── Old Generation
```

This organization improves Garbage Collection performance.

---

# Why is Heap Divided?

Observation made by JVM designers

```
Most objects die young.
```

Examples

```java
new UserDTO();

new ArrayList<>();

new String();

new HashMap<>();
```

Many temporary objects become unreachable almost immediately.

Instead of treating every object equally, JVM groups objects based on age.

---

# Young Generation

Every newly created object starts here.

Example

```java
new User();
```

Allocation

```
Young Generation

↓

Eden Space
```

Young Generation consists of

```
Young Generation

├── Eden

├── Survivor 0

└── Survivor 1
```

---

# Eden Space

Every new object is first allocated in Eden.

Example

```java
new User();

new Employee();

new ArrayList();

new String();
```

Conceptually

```
Eden

User

Employee

ArrayList

String
```

As more objects are created

```
Eden

↓

Becomes Full
```

---

# Minor Garbage Collection

When Eden becomes full

JVM performs

```
Minor Garbage Collection
```

Suppose

```
User

Alive
```

```
Employee

Alive
```

```
ArrayList

Dead
```

```
String

Dead
```

Dead objects are removed.

Alive objects are copied to Survivor Space.

---

# Survivor Spaces

Young Generation contains

```
Survivor 0

Survivor 1
```

Only one Survivor Space is active at a time.

Example

After first Minor GC

```
Eden

↓

Empty
```

```
Survivor 0

User

Employee
```

Next Minor GC

```
Survivor 0

↓

Survivor 1
```

Objects keep moving between Survivor spaces.

---

# Object Aging

Every object has an age.

Example

```
User

Age = 1
```

↓

```
Minor GC
```

↓

```
Age = 2
```

↓

```
Minor GC
```

↓

```
Age = 3
```

The age increases every time the object survives a Minor GC.

---

# Object Promotion

After surviving several Minor Garbage Collections

the object is promoted.

```
Young Generation

↓

Old Generation
```

The promotion threshold is configurable.

A common default is around **15**, though it may vary depending on JVM settings.

---

# Old Generation

Stores long-lived objects.

Examples

- Spring Beans
- Singleton Objects
- Cached Data
- Session Objects
- Configuration Objects

Visualization

```
Young Generation

↓

Object survives many GCs

↓

Old Generation
```

Garbage Collection occurs less frequently here because scanning large, long-lived objects is more expensive.

---

# Why Do Most Objects Die Young?

Example

```java
public void process(){

    UserDTO dto = new UserDTO();

    dto.setName("Guru");

}
```

After the method returns

```
Stack Frame

↓

Destroyed
```

The reference disappears.

The object becomes unreachable.

This happens millions of times in enterprise applications.

Therefore

```
Most Objects

↓

Created

↓

Used Briefly

↓

Garbage Collected
```

This observation is the basis of the generational Heap design.

---

# Heap Characteristics

| Property | Heap |
|-----------|------|
| Shared by Threads | ✅ Yes |
| Stores Objects | ✅ Yes |
| Stores Arrays | ✅ Yes |
| Stores Instance Variables | ✅ Yes |
| Stores Primitive Local Variables | ❌ No |
| Uses Garbage Collector | ✅ Yes |
| Allocation | Dynamic |
| Size | Larger than Stack |

---

# Common Misconception

Wrong

```
Primitive variables are always stored in Stack.
```

Correct

Local primitive variables

```java
void test(){

    int age = 25;

}
```

Stored in

```
Stack
```

Instance primitive variables

```java
class User{

    int age;

}
```

Stored inside the object.

```
Heap

↓

User Object

↓

age
```

---

# Heap vs Stack

| Feature | Heap | Stack |
|----------|------|--------|
| Purpose | Object Storage | Method Execution |
| Shared | Yes | No |
| Per Thread | No | Yes |
| Stores Objects | ✅ Yes | ❌ No |
| Stores References | Indirectly inside objects | ✅ Yes |
| Stores Local Variables | ❌ No | ✅ Yes |
| Memory Management | Garbage Collector | Automatic Pop |
| Allocation Speed | Slower | Faster |
| Lifetime | Until unreachable | Until method returns |

---

# Best Practices

✅ Avoid creating unnecessary temporary objects.

✅ Reuse expensive objects where appropriate.

✅ Understand that object references live in Stack while objects live in Heap.

✅ Avoid retaining references to unused objects, as this can prevent garbage collection.

---

# Key Takeaways

- Heap Memory stores all Java objects and arrays.
- There is one Heap per JVM, shared by all threads.
- New objects are allocated in Eden Space.
- Eden belongs to the Young Generation.
- Young Generation also contains Survivor 0 and Survivor 1.
- Objects that survive Minor GCs move between Survivor spaces.
- Long-lived objects are promoted to the Old Generation.
- Most objects die young, which is why the Heap is divided into generations.
- Heap Memory is managed by the Garbage Collector.

---

# Interview Questions

## What is Heap Memory?

Heap Memory is the shared runtime memory area where Java objects and arrays are dynamically allocated. It is managed automatically by the Garbage Collector.

---

## Why is Heap shared among threads?

To allow multiple threads to access the same objects without creating duplicate copies, improving memory efficiency and enabling object sharing.

---

## Where are new objects created?

All newly created objects are initially allocated in the **Eden Space** of the Young Generation.

---

## What are Survivor Spaces?

Survivor 0 (S0) and Survivor 1 (S1) are temporary regions that hold objects which survive Minor Garbage Collections before they are promoted to the Old Generation.

---

## What is Object Promotion?

Object Promotion is the process of moving long-lived objects from the Young Generation to the Old Generation after they survive multiple Minor Garbage Collections.

---

## Why is Heap divided into generations?

Because most Java objects have short lifetimes. Separating young and long-lived objects allows the Garbage Collector to reclaim memory more efficiently.

---

## Where are instance variables stored?

Instance variables are stored inside the object itself, which resides in the Heap.

Example

```java
class User{

    int age;

}
```

The `age` field is part of the `User` object in Heap Memory.