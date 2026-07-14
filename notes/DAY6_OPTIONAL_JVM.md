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

