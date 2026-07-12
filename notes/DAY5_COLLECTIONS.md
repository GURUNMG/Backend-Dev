# Day 5 — Java Collections Framework

> **Topics**
>
> - Collections Framework Architecture
> - Why Collections?
> - Iterable
> - Collection Interface
> - List
> - Set
> - Queue (Architecture Overview)
> - Map
> - Internal Implementations
> - Time Complexity
> - Choosing the Right Collection
> - Best Practices
> - Interview Questions

---

# Chapter 1 — Why Collections?

Before learning any collection class, we must first understand **why Java introduced the Collections Framework**.

One of the most common interview questions is:

> **Why not simply use arrays?**

---

# Before Collections (Java 1.1 and Earlier)

Suppose we need to store employees.

```java
Employee[] employees = new Employee[100];
```

At first glance this looks simple, but several problems quickly appear.

---

## Problem 1 — Fixed Size

```java
Employee[] employees = new Employee[100];
```

If another employee joins after the array is full, we cannot store them without creating a new array.

If only a few employees exist, most of the allocated memory remains unused.

**Problems**

- Fixed capacity
- Wasted memory
- Difficult resizing

---

## Problem 2 — Manual Searching

Finding an employee requires traversing the array manually.

```java
Employee found = null;

for (Employee employee : employees) {

    if (employee.getId() == id) {
        found = employee;
        break;
    }

}
```

Every search requires explicit looping.

---

## Problem 3 — Manual Insertion

Suppose we have

```
A B C D
```

Insert **X** after **B**

```
A B X C D
```

To accomplish this, every element after **B** must be shifted manually.

---

## Problem 4 — Manual Deletion

Suppose we delete **B**

```
A B C D

↓

A C D
```

Every element after the deleted position must be shifted left.

---

## Problem 5 — Manual Sorting

If we wanted sorted data, we had to implement algorithms ourselves.

Examples

- Bubble Sort
- Selection Sort
- Insertion Sort

Every project repeated the same code.

---

## Problem 6 — No Standard API

Different developers created different utility methods.

Project A

```text
findEmployee()
```

Project B

```text
searchEmployee()
```

Project C

```text
getEmployee()
```

No standard way existed to work with data.

---

# Why Java Introduced Collections

The Java Collections Framework was introduced in **Java 1.2**.

Its goals were

- Dynamic storage
- Reusable data structures
- Efficient searching
- Efficient sorting
- Standard APIs
- Optimized algorithms
- Less boilerplate code

Think of Collections as

> **A library of optimized data structures provided by Java.**

Instead of implementing everything ourselves, Java already provides

- ArrayList
- LinkedList
- HashMap
- TreeMap
- HashSet
- TreeSet
- PriorityQueue
- ArrayDeque

---

# Real-World Analogy

Imagine a library.

Books are not stored randomly.

Depending on the requirement, different storage mechanisms are used.

- Shelves
- Catalogs
- Indexes
- Sections

Collections work the same way.

Different data structures solve different problems.

---

# Collections Framework Architecture

```
                Iterable
                    │
              Collection
        ┌──────────┼──────────┐
        │          │          │
      List        Set       Queue
        │          │
 Implementations Implementations


Map
(Separate Hierarchy)
```

Notice an important point.

> **Map does NOT extend Collection.**

This is one of the most frequently asked interview questions.

---

# Why Is Map Separate?

Collections store individual elements.

Example

```
Apple

Orange

Mango
```

A Map stores **key-value pairs**.

Example

```
101 → Guru

102 → Rahul

103 → John
```

Since the storage model is completely different, Java designed **Map** as a separate hierarchy.

---

# Complete Architecture

```
Iterable

↓

Collection

↓

List
    ↓
    ArrayList
    LinkedList
    Vector
    Stack

Set
    ↓
    HashSet
    LinkedHashSet
    TreeSet

Queue
    ↓
    PriorityQueue
    ArrayDeque
    LinkedList

Map
    ↓
    HashMap
    LinkedHashMap
    Hashtable
    TreeMap
    ConcurrentHashMap
```

---

# Why So Many Implementations?

Each implementation solves a different problem.

Need duplicates?

```
List
```

Need uniqueness?

```
Set
```

Need fast lookup?

```
HashMap
```

Need sorted data?

```
TreeMap
```

Need insertion order?

```
LinkedHashMap
```

Each implementation is optimized for a specific requirement.

---

# Collections Are Interface-Based

Instead of writing

```java
ArrayList<String> names = new ArrayList<>();
```

Prefer

```java
List<String> names = new ArrayList<>();
```

This follows the principle

> **Program to interfaces, not implementations.**

Benefits

- Loose coupling
- Easy replacement of implementations
- Better maintainability
- Better extensibility

---

# Components of the Collections Framework

The framework consists of three major parts.

## 1. Interfaces

They define **what operations are available**.

Examples

- List
- Set
- Queue
- Map

Interfaces describe behavior.

---

## 2. Implementations

These classes provide the actual implementation.

Examples

- ArrayList
- LinkedList
- HashMap
- HashSet
- TreeMap

Implementations decide how the data is stored internally.

---

## 3. Algorithms

Java also provides reusable algorithms through the `Collections` utility class.

Examples

```java
Collections.sort(list);

Collections.reverse(list);

Collections.shuffle(list);

Collections.binarySearch(list, key);
```

These eliminate the need to write common algorithms repeatedly.

---

# Key Takeaways

- Arrays have fixed size.
- Collections grow dynamically.
- Collections provide optimized data structures.
- Collection stores values.
- Map stores key-value pairs.
- Map is not part of the Collection hierarchy.
- The framework consists of Interfaces, Implementations, and Algorithms.
- Different implementations are optimized for different use cases.
- Prefer programming to interfaces.

---

# Interview Questions

### Basic

1. Why was the Collections Framework introduced?
2. What are the disadvantages of arrays?
3. Why is Map not a Collection?
4. Why are there multiple collection implementations?
5. What are the three components of the Collections Framework?

### Intermediate

6. Why should we code against interfaces?
7. What does the Collections utility class provide?
8. Why are optimized algorithms included in the framework?
9. What kinds of problems does the framework solve?
10. Explain the architecture of the Collections Framework.
