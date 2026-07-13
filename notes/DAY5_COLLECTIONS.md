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

# Chapter 2 — Iterable & Collection Interface

Before learning **List**, **Set**, or **Queue**, we must first understand the two interfaces that form the foundation of the Java Collections Framework.

```
                Iterable
                     │
               Collection
        ┌────────────┼────────────┐
        │            │            │
      List          Set         Queue
```

Every collection (except `Map`) starts here.

---

# Why Was Iterable Introduced?

Consider an array.

```java
String[] fruits = {
        "Apple",
        "Orange",
        "Mango"
};
```

We can iterate using an index.

```java
for (int i = 0; i < fruits.length; i++) {

    System.out.println(fruits[i]);

}
```

or using the enhanced for loop.

```java
for (String fruit : fruits) {

    System.out.println(fruit);

}
```

Arrays have

- Continuous memory
- Fixed size
- Index-based access

Iteration is straightforward.

---

Now consider a LinkedList.

```
Apple → Orange → Mango
```

There are **no indexes**.

How can Java use the same `for-each` loop for both an array and a linked list?

Java needed a **common traversal mechanism**.

That mechanism is **Iterable**.

---

# What is Iterable?

`Iterable` is the root interface for traversable collections.

Its purpose is simple.

> **"I know how to provide my elements one at a time."**

Notice that it does **not** define how elements are stored.

Each collection decides its own traversal strategy.

---

# Iterable Interface

The interface is very small.

```java
public interface Iterable<T> {

    Iterator<T> iterator();

}
```

It contains a single essential method.

```java
iterator()
```

Whenever Java wants to traverse a collection, it calls this method.

---

# Why Does Iterable Only Have One Method?

Each collection already knows

- where its data is
- how its elements are organized

Java simply asks

> "Give me an Iterator."

The returned iterator handles traversal.

---

# What is an Iterator?

An **Iterator** is an object that traverses a collection one element at a time.

Think of it as a cursor moving through the collection.

Example

```
Apple
Orange
Mango
```

Initial position

```
↓

Apple
Orange
Mango
```

After calling `next()`

```
Apple
      ↓
Orange
Mango
```

After another `next()`

```
Apple
Orange
        ↓
Mango
```

---

# Iterator Methods

## hasNext()

Checks whether another element exists.

```java
iterator.hasNext();
```

Returns

- true
- false

---

## next()

Returns the next element.

```java
iterator.next();
```

---

## remove()

Safely removes the current element from the collection.

```java
iterator.remove();
```

---

# Example

```java
List<String> fruits = List.of(
        "Apple",
        "Orange",
        "Mango"
);

Iterator<String> iterator =
        fruits.iterator();

while (iterator.hasNext()) {

    System.out.println(iterator.next());

}
```

Output

```
Apple
Orange
Mango
```

---

# How Does the Enhanced For Loop Work?

Many developers think this is a special Java feature.

```java
for (String fruit : fruits) {

    System.out.println(fruit);

}
```

Internally, the compiler roughly converts it into

```java
Iterator<String> iterator =
        fruits.iterator();

while (iterator.hasNext()) {

    String fruit = iterator.next();

    System.out.println(fruit);

}
```

Therefore

> **The enhanced for loop is syntactic sugar built on top of an Iterator.**

---

# Why Not Use Indexes?

Indexes work well for arrays.

```
0 1 2 3
```

But they don't work for every data structure.

Linked List

```
A → B → C → D
```

Tree

```
        10
      /    \
     5      20
```

Neither has indexes.

Java needed one traversal mechanism that works for every data structure.

That mechanism is the **Iterator**.

---

# Why Doesn't a Collection Return Itself?

Instead of exposing its internal traversal state, a collection creates a new iterator.

```java
Iterator<String> first = list.iterator();

Iterator<String> second = list.iterator();
```

Each iterator is independent.

This allows multiple traversals at the same time.

---

# Collection Interface

The next interface in the hierarchy is

```
Iterable
      │
Collection
```

`Collection` extends `Iterable`.

```java
public interface Collection<E>
        extends Iterable<E> {

}
```

Therefore, every Collection automatically supports iteration.

---

# What Does Collection Add?

The Collection interface defines the common operations shared by almost every collection.

| Method | Purpose |
|---------|----------|
| add() | Insert an element |
| remove() | Remove an element |
| contains() | Check whether an element exists |
| size() | Number of elements |
| isEmpty() | Check if empty |
| clear() | Remove all elements |
| toArray() | Convert to an array |

---

# Example

```java
Collection<String> names =
        new ArrayList<>();

names.add("Guru");
names.add("Rahul");

System.out.println(names.size());

System.out.println(
        names.contains("Guru")
);

names.remove("Rahul");
```

Notice that we program against the **Collection interface**, not the implementation.

---

# Why Is Collection an Interface?

Without a common interface, every collection might expose different APIs.

Example

```
ArrayList

insert()

LinkedList

append()

HashSet

store()
```

Developers would need to learn every API separately.

Instead, Java standardizes common operations.

```
add()

remove()

contains()

size()

clear()
```

This consistency makes collections easy to learn and use.

---

# Why Doesn't Map Extend Collection?

A Collection stores individual elements.

Example

```
Apple

Orange

Mango
```

A Map stores key-value pairs.

Example

```
101 → Guru

102 → Rahul
```

Since the storage model is different, Java designed `Map` as a separate hierarchy.

However, Map provides iterable views.

```java
map.keySet();

map.values();

map.entrySet();
```

These returned collections can be traversed using an Iterator or the enhanced for loop.

---

# Architecture So Far

```
Iterable
    │
    └── iterator()
            │
            ▼
        Iterator

Collection
    │
    ├── add()
    ├── remove()
    ├── contains()
    ├── size()
    ├── clear()
    └── toArray()

        ▲
        │
──────────────────────────
│          │            │
List       Set        Queue
```

---

# Key Takeaways

- `Iterable` is the root interface for traversable collections.
- `Iterable` contains one important method: `iterator()`.
- `Iterator` traverses elements one by one.
- The enhanced `for-each` loop internally uses an Iterator.
- `Collection` extends `Iterable`.
- `Collection` defines common methods such as `add()`, `remove()`, `contains()`, `size()`, and `clear()`.
- `Map` is not part of the Collection hierarchy because it stores key-value pairs.
- `Map` exposes iterable views using `keySet()`, `values()`, and `entrySet()`.

---

# Interview Questions

## Basic

1. What is Iterable?
2. Why was Iterable introduced?
3. What method does Iterable define?
4. What is an Iterator?
5. Explain `hasNext()` and `next()`.
6. How does the enhanced for loop work internally?
7. What is the purpose of the Collection interface?
8. Name five methods defined by Collection.
9. Why doesn't Map implement Collection?
10. How do you iterate over a Map?

## Intermediate

11. Why is an Iterator preferred over index-based traversal?
12. Why does `iterator()` return a new Iterator every time?
13. Can multiple Iterators traverse the same collection simultaneously?
14. Why is Collection an interface rather than a class?
15. Explain the relationship between Iterable, Iterator, and Collection.

# Java Collections Framework - Interface Hierarchy & Methods

```
                                    Iterable<E>
                                         │
        ┌────────────────────────────────┼────────────────────────────────┐
        │                                │                                │
iterator()                        forEach()                      spliterator()

                                         │
                                         ▼
                                 Collection<E>
                                         │
     ┌───────────────────────────────────┼─────────────────────────────────────┐
     │                                   │                                     │
General Collection Operations      Bulk Operations                    Utility Operations
     │                                   │                                     │
add(E)                            addAll(Collection)                  size()
remove(Object)                    removeAll(Collection)               isEmpty()
clear()                           retainAll(Collection)               contains(Object)
removeIf(Predicate)               containsAll(Collection)             toArray()
                                                                  toArray(T[])
                                                                  stream()
                                                                  parallelStream()

                                         │
        ┌────────────────────────────────┼────────────────────────────────┐
        │                                │                                │
        ▼                                ▼                                ▼
     List<E>                          Set<E>                         Queue<E>
        │                                │                                │
        │                                │                                │
Index-Based Operations            Duplicate-Free                  Queue Operations
        │                          Collection                         (FIFO)
        │                                │                                │
get(index)                     (No new methods)                   offer(E)
set(index,E)                                                poll()
add(index,E)                                                peek()
addAll(index,c)                                             element()
remove(index)                                               remove()
indexOf(Object)
lastIndexOf(Object)
subList()
listIterator()
```

---

# Implementations

```
Collection
│
├── List
│     ├── ArrayList
│     ├── LinkedList
│     ├── Vector
│     └── Stack
│
├── Set
│     ├── HashSet
│     ├── LinkedHashSet
│     ├── SortedSet
│     │      └── TreeSet
│     └── NavigableSet
│            └── TreeSet
│
└── Queue
      ├── PriorityQueue
      ├── Deque
      │      ├── ArrayDeque
      │      └── LinkedList
      └── (other concurrent queue implementations)
```

---

# Map Hierarchy (Separate from Collection)

```
Map<K,V>

Basic Operations
│
├── put(K,V)
├── putAll(Map)
├── get(Object)
├── getOrDefault()
├── remove(Object)
├── replace()
├── replaceAll()
├── containsKey()
├── containsValue()
├── keySet()
├── values()
├── entrySet()
├── size()
├── isEmpty()
├── clear()
├── compute()
├── computeIfAbsent()
├── computeIfPresent()
├── merge()
├── forEach()
└── putIfAbsent()

                │
      ┌─────────┼──────────────┐
      │         │              │
      ▼         ▼              ▼
  HashMap  LinkedHashMap   SortedMap
                                 │
                                 ▼
                           NavigableMap
                                 │
                                 ▼
                              TreeMap
```

---

# What Each Interface Adds

## Iterable

Purpose

- Enables enhanced for-loop.

Methods

```
iterator()

forEach()

spliterator()
```

---

## Collection

Purpose

General collection operations.

Methods

```
add()

addAll()

remove()

removeAll()

retainAll()

removeIf()

clear()

contains()

containsAll()

size()

isEmpty()

toArray()

stream()

parallelStream()
```

---

## List

Purpose

Ordered collection with index-based access.

Additional Methods

```
get()

set()

add(index)

addAll(index)

remove(index)

indexOf()

lastIndexOf()

subList()

listIterator()
```

---

## Set

Purpose

Stores **unique elements**.

Additional Methods

```
None
```

The uniqueness guarantee comes from the **contract**, not from new methods.

---

## SortedSet

Purpose

Maintains elements in sorted order.

Methods

```
first()

last()

headSet()

tailSet()

subSet()

comparator()
```

---

## NavigableSet

Purpose

Navigation around sorted elements.

Methods

```
lower()

floor()

ceiling()

higher()

pollFirst()

pollLast()

descendingSet()

descendingIterator()
```

---

## Queue

Purpose

FIFO processing.

Methods

```
offer()

poll()

peek()

element()

remove()
```

---

## Deque

Purpose

Double-ended queue.

Additional Methods

Front

```
offerFirst()

pollFirst()

peekFirst()

addFirst()

removeFirst()

getFirst()
```

Back

```
offerLast()

pollLast()

peekLast()

addLast()

removeLast()

getLast()
```

Stack Operations

```
push()

pop()
```

---

# Map

Purpose

Stores **key-value pairs**.

Methods

```
put()

putAll()

get()

remove()

replace()

containsKey()

containsValue()

keySet()

values()

entrySet()

compute()

computeIfAbsent()

computeIfPresent()

merge()

putIfAbsent()

forEach()
```

---

# Complete Interview Flow

```
Iterable
      │
      ▼
Collection
      │
      ├── List
      │      ├── ArrayList
      │      ├── LinkedList
      │      ├── Vector
      │      └── Stack
      │
      ├── Set
      │      ├── HashSet
      │      ├── LinkedHashSet
      │      └── TreeSet
      │
      └── Queue
             ├── PriorityQueue
             ├── ArrayDeque
             └── LinkedList


Map (Independent Hierarchy)
      │
      ├── HashMap
      ├── LinkedHashMap
      └── TreeMap
```

# Chapter 3 — List Interface

The **List** interface is the first major collection interface in the Java Collections Framework.

Hierarchy

```
Iterable
     │
Collection
     │
    List
     │
────────────────────────────────
│         │          │        │
ArrayList LinkedList Vector   Stack
```

One important point to remember is:

> **List is an interface, not a class.**

It does not store data itself.

Instead, it defines the **contract** that every List implementation must follow.

---

# Why Was List Introduced?

Before the Collections Framework, data was commonly stored using arrays.

```java
Student[] students = new Student[100];
```

Arrays introduced several problems.

- Fixed size
- Manual insertion
- Manual deletion
- Manual shifting
- Difficult resizing

Java introduced the **List** interface to solve these problems by providing a dynamic, ordered collection with a rich API.

---

# What is a List?

A **List** is an ordered collection that

- Preserves insertion order
- Allows duplicate elements
- Supports positional (index-based) access
- Grows dynamically

This definition summarizes the behavior that every List implementation must provide.

---

# Characteristics of a List

## 1. Ordered Collection

A List preserves the order in which elements are inserted.

```java
List<String> fruits = new ArrayList<>();

fruits.add("Apple");
fruits.add("Orange");
fruits.add("Mango");
```

Result

```
0 → Apple
1 → Orange
2 → Mango
```

Printing the list produces

```
Apple
Orange
Mango
```

The insertion order is maintained.

---

## 2. Allows Duplicate Elements

A List can contain duplicate values.

```java
List<String> colors = new ArrayList<>();

colors.add("Red");
colors.add("Blue");
colors.add("Red");
colors.add("Green");
colors.add("Blue");
```

Result

```
Red
Blue
Red
Green
Blue
```

Duplicates are stored without any restriction.

### Real-world Example

Shopping cart

```
Milk
Milk
Bread
```

Buying multiple quantities of the same item is perfectly valid.

---

## 3. Supports Index-Based Access

Every element has an index.

```
0 → Apple
1 → Orange
2 → Mango
```

Accessing an element

```java
System.out.println(fruits.get(1));
```

Output

```
Orange
```

This is called **positional access**.

---

## 4. Supports Insertion at Any Position

```java
fruits.add(1, "Banana");
```

Result

```
Apple
Banana
Orange
Mango
```

The List automatically shifts existing elements to make space.

---

## 5. Supports Removal by Position

```java
fruits.remove(2);
```

Before

```
Apple
Banana
Orange
Mango
```

After

```
Apple
Banana
Mango
```

The List automatically shifts the remaining elements.

---

## 6. Dynamic Size

Arrays have a fixed size.

```java
new int[100];
```

A List grows automatically as elements are added.

```java
List<String> list = new ArrayList<>();
```

No manual resizing is required.

---

## 7. Allows null Values

A List can store `null`.

```java
List<String> names = new ArrayList<>();

names.add(null);
names.add("Guru");
```

Result

```
null
Guru
```

Although allowed, excessive use of `null` is generally discouraged because it can lead to `NullPointerException`.

---

# Common Methods of List

Since List extends Collection, it inherits methods like

- add()
- remove()
- contains()
- size()
- clear()

It also introduces methods related to positional access.

| Method | Description |
|----------|-------------|
| `add(E e)` | Adds an element at the end |
| `add(int index, E e)` | Inserts an element at a specific position |
| `get(int index)` | Retrieves the element at the given index |
| `set(int index, E e)` | Replaces an element |
| `remove(int index)` | Removes an element by index |
| `remove(Object o)` | Removes an element by value |
| `indexOf(Object o)` | Returns the first occurrence |
| `lastIndexOf(Object o)` | Returns the last occurrence |
| `subList(int from, int to)` | Returns a portion of the list |

---

# Example

```java
List<String> fruits = new ArrayList<>();

fruits.add("Apple");
fruits.add("Orange");
fruits.add("Mango");

System.out.println(fruits.get(1));

fruits.set(1, "Banana");

System.out.println(fruits);

fruits.remove("Apple");

System.out.println(fruits);
```

Output

```
Orange

[Apple, Banana, Mango]

[Banana, Mango]
```

---

# Why Is List an Interface?

Suppose Java only provided `ArrayList`.

Changing to another implementation would require modifying application code.

Instead, Java encourages programming to the interface.

Preferred

```java
List<String> names = new ArrayList<>();
```

Later, changing the implementation becomes simple.

```java
List<String> names = new LinkedList<>();
```

The rest of the code often remains unchanged because both implementations follow the List contract.

This follows the principle

> **Program to interfaces, not implementations.**

---

# List vs Array

| Feature | Array | List |
|----------|-------|------|
| Size | Fixed | Dynamic |
| Ordered | Yes | Yes |
| Duplicates | Allowed | Allowed |
| Insert in middle | Manual shifting | Built-in |
| Remove | Manual shifting | Built-in |
| Generics | No | Yes |
| Rich API | No | Yes |

---

# List vs Set

| Feature | List | Set |
|----------|------|-----|
| Preserves Order | Yes | Depends on implementation |
| Allows Duplicates | Yes | No |
| Index-Based Access | Yes | No |
| Positional Operations | Yes | No |

---

# Real-World Use Cases

### Playlist

```
Song 1
Song 2
Song 3
```

Order matters.

Duplicates are allowed.

---

### Chat Messages

```
Hello
Hi
How are you?
```

Messages should appear in the order they were sent.

---

### Shopping Cart

```
Milk
Milk
Bread
```

Duplicate products are allowed.

---

# When Should You Use a List?

Use a List when

- Order matters.
- Duplicate elements are allowed.
- Index-based access is required.
- Elements need to be inserted or removed by position.
- Data represents a sequence.

---

# What the List Interface Does NOT Define

The List interface specifies **behavior**, not **performance**.

For example, both `ArrayList` and `LinkedList` provide

```java
get(int index)
```

However, the execution time differs because their internal implementations are different.

The interface guarantees the operation exists, but **not** how efficiently it is implemented.

---

# Key Takeaways

- List is an interface.
- List extends Collection.
- List preserves insertion order.
- List allows duplicate elements.
- List supports index-based access.
- List grows dynamically.
- List supports positional insertion and deletion.
- List allows null values.
- Program to the List interface rather than concrete implementations.
- Performance depends on the implementation, not the interface.

---

# Interview Questions

## Basic

1. What is the List interface?
2. Why was List introduced?
3. What are the characteristics of a List?
4. Does List allow duplicates?
5. Does List preserve insertion order?
6. Can List store null values?
7. What is positional access?
8. Name four implementations of List.

## Intermediate

9. Why is List an interface rather than a class?
10. What additional methods does List provide over Collection?
11. Why should you declare variables as List instead of ArrayList?
12. Does the List interface guarantee performance?
13. When would you choose List over Set?
14. What is the difference between `remove(int index)` and `remove(Object o)`?
15. Explain the List hierarchy in the Collections Framework.

# Chapter 4 — ArrayList

ArrayList is the most commonly used implementation of the **List** interface.

Hierarchy

```
Iterable
      │
Collection
      │
    List
      │
 ArrayList
```

`ArrayList` is a **class**, whereas `List` is an **interface**.

Preferred declaration

```java
List<String> list = new ArrayList<>();
```

instead of

```java
ArrayList<String> list = new ArrayList<>();
```

This follows the principle

> **Program to interfaces, not implementations.**

---

# What is ArrayList?

**Definition**

> ArrayList is a **resizable array implementation** of the List interface.

The important words are

- Array
- Resizable

Internally, ArrayList stores elements inside an array and automatically resizes it when required.

---

# Why Was ArrayList Introduced?

Arrays have several limitations.

```java
int[] numbers = new int[5];
```

Problems

- Fixed size
- Manual resizing
- Manual copying
- Manual insertion
- Manual deletion

ArrayList solves these problems by providing

- Dynamic growth
- Rich API
- Fast random access
- Automatic memory management

---

# Internal Structure

A simplified version of ArrayList looks like

```java
class ArrayList<E> {

    Object[] elementData;

}
```

Internally, every ArrayList maintains an array called `elementData`.

All operations are performed on this internal array.

---

# Internal Representation

Suppose

```java
List<String> fruits = new ArrayList<>();
```

Internally

```
ArrayList
      │
      ▼
 Object[]
```

Initially

```
Index

0
1
2
3
4
5
6
7
8
9
```

Capacity

```
10
```

Size

```
0
```

---

# Capacity vs Size

One of the most frequently asked interview questions.

## Size

The number of elements actually stored.

Example

```java
list.add("A");
list.add("B");
```

Size

```
2
```

---

## Capacity

The total number of elements the internal array can currently hold before resizing.

Example

```
Capacity = 10
```

Memory

```
0 → A

1 → B

2 → empty

3 → empty

...

9 → empty
```

Capacity

```
10
```

Size

```
2
```

### Difference

| Size | Capacity |
|------|----------|
| Actual elements stored | Internal array length |
| Changes after every insertion/removal | Changes only during resize |

---

# Why Extra Capacity Exists

If Java resized the array after every insertion

```
1 element

↓

new array

↓

2 elements

↓

new array

↓

3 elements
```

there would be excessive memory allocation and copying.

Instead, Java allocates additional space so that most insertions do not require resizing.

---

# How add() Works

Suppose

```java
list.add("Apple");
```

Java checks

```
Is free space available?
```

If yes

```
Store the element.

Done.
```

No resizing is required.

---

# What Happens When Capacity Is Full?

Suppose

```
Capacity = 10

Size = 10
```

Another insertion

```java
list.add("New");
```

cannot fit into the current array.

Java performs a resize.

---

# Resizing Algorithm

Current JDK implementation grows approximately by **1.5×**.

Formula

```java
newCapacity = oldCapacity + (oldCapacity >> 1);
```

Example

```
Old Capacity = 10

↓

New Capacity = 15
```

Steps

1. Create a larger array.
2. Copy all existing elements.
3. Insert the new element.
4. Old array becomes eligible for garbage collection.

---

# Visualization

Before

```
Capacity = 10

A
B
C
D
E
F
G
H
I
J
```

Resize

↓

```
Capacity = 15

A
B
C
D
E
F
G
H
I
J
_
_
_
_
_
```

Insert

```
K
```

---

# Why Doesn't Java Double the Capacity?

Doubling would reduce the number of resize operations but waste more memory.

Very small growth would reduce memory waste but require frequent copying.

A growth factor of approximately **1.5×** provides a good balance between memory usage and performance.

---

# Time Complexity of add()

### Normal Case

Space is available.

```java
list.add("A");
```

Time Complexity

```
O(1)
```

---

### Resize Case

When resizing occurs

- Allocate a new array
- Copy existing elements
- Insert the new element

Time Complexity

```
O(n)
```

---

### Overall Complexity

Interview Answer

> **add(E e) is amortized O(1).**

Although resizing is expensive, it happens infrequently, so the average cost of each insertion remains constant.

---

# Random Access

Suppose

```
0 → Apple

1 → Orange

2 → Mango
```

Access

```java
list.get(2);
```

The JVM directly calculates the memory location using the index.

No traversal is required.

Time Complexity

```
O(1)
```

This is one of the biggest advantages of ArrayList.

---

# Why Is Middle Insertion Slow?

Suppose

```
A
B
C
D
```

Insert

```
X
```

Result

```
A
B
X
C
D
```

Elements

```
C
D
```

must be shifted.

Time Complexity

```
O(n)
```

---

# Why Is Middle Deletion Slow?

Suppose

```
A
B
C
D
```

Delete

```
B
```

Result

```
A
C
D
```

Remaining elements shift left.

Time Complexity

```
O(n)
```

---

# Time Complexity Summary

| Operation | Complexity |
|-----------|------------|
| get(index) | O(1) |
| set(index) | O(1) |
| add(E) | Amortized O(1) |
| add(index) | O(n) |
| remove(index) | O(n) |
| contains() | O(n) |
| indexOf() | O(n) |

---

# When Should You Use ArrayList?

Use ArrayList when

- Frequent reading is required.
- Random access is important.
- Most insertions happen at the end.
- The collection grows dynamically.
- Middle insertions and deletions are relatively rare.

Typical use cases

- Product lists
- Search results
- User lists
- API response collections
- Configuration data

---

# Common Interview Mistakes

❌ ArrayList is implemented using a linked list.

✔ ArrayList is backed by a dynamically resized array.

---

❌ add() is always O(1).

✔ add(E) is **amortized O(1)** because resizing occasionally takes O(n).

---

❌ Capacity and size are the same.

✔ Capacity is the internal array length.

✔ Size is the number of stored elements.

---

❌ ArrayList is best for every operation.

✔ It is excellent for random access but inefficient for frequent middle insertions and deletions.

---

# Key Takeaways

- ArrayList implements List.
- Internally uses a dynamically resized array.
- Size and capacity are different.
- Current JDK grows capacity by approximately 1.5×.
- get() and set() are O(1).
- add(E) is amortized O(1).
- Middle insertion and deletion are O(n).
- Choose ArrayList when reads greatly outnumber structural modifications.

---

# Interview Questions

## Basic

1. What is ArrayList?
2. How is ArrayList implemented internally?
3. What is the difference between size and capacity?
4. Why was ArrayList introduced?
5. Why is get() O(1)?

## Intermediate

6. Explain the resizing algorithm of ArrayList.
7. Why is add(E) called amortized O(1)?
8. Why is insertion in the middle O(n)?
9. Why is deletion in the middle O(n)?
10. When would you choose ArrayList over LinkedList?

# Chapter 5 — LinkedList

LinkedList is another implementation of the **List** interface.

Unlike ArrayList, which stores elements in a dynamically resized array, LinkedList stores elements as **nodes connected together**.

Hierarchy

```
Iterable
      │
Collection
      │
    List
      │
 LinkedList
```

---

# What is LinkedList?

**Definition**

> LinkedList is a **doubly linked list implementation** of the List and Deque interfaces.

Unlike ArrayList, LinkedList does **not** use an array internally.

Instead, it stores data inside **nodes**.

---

# Why Was LinkedList Introduced?

Suppose we have

```
A
B
C
D
```

stored in an array.

Insert

```
X
```

after **B**.

Result

```
A
B
X
C
D
```

Elements

```
C
D
```

must be shifted.

Time Complexity

```
O(n)
```

Java introduced LinkedList to avoid shifting elements during insertion and deletion.

---

# Internal Structure

A LinkedList is composed of **nodes**.

Each node stores

- The actual data
- Reference to the previous node
- Reference to the next node

Simplified implementation

```java
class Node<E> {

    E item;

    Node<E> prev;

    Node<E> next;

}
```

Unlike an array, the nodes are **not stored continuously in memory**.

---

# Visual Representation

Suppose

```java
List<String> list = new LinkedList<>();

list.add("Apple");
list.add("Orange");
list.add("Mango");
```

Memory representation

```
┌────────────┐
│ Apple      │
│ prev = null│
│ next ──────┼────────►
└────────────┘

                 ┌────────────┐
                 │ Orange     │
◄────────────────┤ prev       │
                 │ next ──────┼────────►
                 └────────────┘

                              ┌────────────┐
                              │ Mango      │
◄─────────────────────────────┤ prev       │
                              │ next = null│
                              └────────────┘
```

Every node knows

- Previous node
- Next node

---

# Singly vs Doubly Linked List

## Singly Linked List

```
A → B → C → D
```

Each node stores only the next reference.

---

## Doubly Linked List

```
null ← A ⇄ B ⇄ C ⇄ D → null
```

Each node stores

- Previous reference
- Next reference

Java's LinkedList uses a **doubly linked list**.

---

# Why Does Java Use a Doubly Linked List?

Suppose

```
A ⇄ B ⇄ C ⇄ D
```

Delete

```
C
```

Java reconnects

```
A ⇄ B ⇄ D
```

using

- `B.next`
- `D.prev`

This makes insertion and deletion easier once the node is located.

---

# Head and Tail References

LinkedList stores references to both ends of the list.

Simplified implementation

```java
class LinkedList<E> {

    Node<E> first;

    Node<E> last;

    int size;

}
```

Visualization

```
first
  │
  ▼
A ⇄ B ⇄ C ⇄ D
              ▲
              │
             last
```

This makes operations at both ends efficient.

---

# How add(E) Works

Suppose

```
A ⇄ B ⇄ C
```

Adding D

```
A ⇄ B ⇄ C ⇄ D
```

Java

- Creates a new node
- Updates `C.next`
- Updates `D.prev`
- Updates `last`

No resizing.

No array copying.

Time Complexity

```
O(1)
```

---

# Insertion in the Middle

Suppose

```
A ⇄ B ⇄ C ⇄ D
```

Insert

```
X
```

after **B**

```
A ⇄ B ⇄ X ⇄ C ⇄ D
```

Java updates four references.

- `B.next`
- `X.prev`
- `X.next`
- `C.prev`

Updating references is

```
O(1)
```

However, Java must first **find the insertion position**.

Traversal

```
A → B → C ...
```

takes

```
O(n)
```

Overall complexity

```
O(n)
```

---

# get(index)

Suppose

```
A ⇄ B ⇄ C ⇄ D
```

Need

```java
list.get(2);
```

Java cannot jump directly to index 2.

It traverses node by node.

```
A → B → C
```

Time Complexity

```
O(n)
```

---

# Java Optimization

Java optimizes traversal.

If the requested index is in the first half

```
A → B → C
```

Traversal starts from the head.

If it is in the second half

```
D ← C ← B
```

Traversal starts from the tail.

This improves average performance but the worst-case complexity remains

```
O(n)
```

---

# Deletion

Suppose

```
A ⇄ B ⇄ C ⇄ D
```

Delete

```
C
```

Java updates

- `B.next`
- `D.prev`

Node C becomes unreachable and is eventually garbage collected.

Finding the node

```
O(n)
```

Updating the links

```
O(1)
```

Overall

```
O(n)
```

---

# Time Complexity

| Operation | Complexity |
|------------|------------|
| get(index) | O(n) |
| set(index) | O(n) |
| add(E) | O(1) |
| add(index) | O(n) |
| remove(index) | O(n) |
| contains() | O(n) |

---

# ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|------------|-----------|------------|
| get(index) | O(1) | O(n) |
| add(E) | Amortized O(1) | O(1) |
| add(index) | O(n) | O(n) |
| remove(index) | O(n) | O(n) |
| Memory Usage | Lower | Higher |

---

# Why Is ArrayList Usually Faster?

Although LinkedList avoids shifting elements, it has disadvantages.

Every node is a separate object.

Each node stores

- Data
- Previous reference
- Next reference

Nodes are scattered throughout memory.

Modern CPUs perform better when data is stored continuously in memory.

ArrayList stores elements inside one contiguous array.

This provides better cache locality and often results in faster real-world performance.

---

# Memory Comparison

### ArrayList

```
[A][B][C][D]
```

One continuous array.

---

### LinkedList

```
[A] ⇄ [B] ⇄ [C] ⇄ [D]
```

Each node is a separate object containing

- Data
- Previous reference
- Next reference

This consumes more memory.

---

# When Should You Use LinkedList?

Choose LinkedList when

- Frequent insertion/removal at the beginning is required.
- Random access is not important.
- The collection behaves like a queue or deque.
- You already have a reference to the node being modified.

For most general-purpose applications, ArrayList is usually the better default choice.

---

# Common Interview Mistakes

❌ LinkedList insertion is always O(1).

✔ Updating references is O(1), but finding the insertion point is O(n).

---

❌ LinkedList is always faster than ArrayList.

✔ In practice, ArrayList is often faster because of better CPU cache locality.

---

❌ LinkedList uses less memory.

✔ False.

Each node stores additional references, increasing memory usage.

---

# Key Takeaways

- LinkedList implements the List interface.
- Internally uses a doubly linked list.
- Each node stores data, previous, and next references.
- Maintains references to both the first and last nodes.
- get(index) is O(n).
- add(E) at the end is O(1).
- add(index) and remove(index) are O(n).
- Uses more memory than ArrayList.
- ArrayList is usually the preferred default for most applications.

---

# Interview Questions

## Basic

1. What is LinkedList?
2. How is LinkedList implemented internally?
3. What is a node?
4. Why does Java use a doubly linked list?
5. What are the first and last references?

## Intermediate

6. Why is get(index) O(n)?
7. Why is add(E) O(1)?
8. Why is insertion by index O(n)?
9. Why is LinkedList often slower than ArrayList in practice?
10. When would you choose LinkedList over ArrayList?

# Chapter 6 — Vector & Stack (Legacy Collections)

Before the Java Collections Framework (Java 1.2), Java already provided a few collection classes.

The most important were

- Vector
- Stack

These classes still exist today but are considered **legacy classes** because newer and better alternatives are available.

---

# Before the Collections Framework

Today we typically write

```java
List<String> list = new ArrayList<>();
```

However, before Java 1.2, `ArrayList` did not exist.

The primary dynamic array implementation was **Vector**.

When the Collections Framework was introduced, Java kept `Vector` for backward compatibility and introduced `ArrayList` as its modern replacement.

---

# What is Vector?

**Definition**

> Vector is a **dynamically resizable array implementation** of the List interface whose methods are synchronized.

Like ArrayList, Vector

- Stores elements in an array
- Grows dynamically
- Preserves insertion order
- Allows duplicate elements
- Supports index-based access

The major difference is **thread safety**.

---

# Internal Structure

A simplified implementation looks like

```java
class Vector<E> {

    Object[] elementData;

}
```

Like ArrayList, Vector stores its elements inside an array.

---

# Why Was Vector Introduced?

One of Java's original goals was to support multithreaded programming.

Suppose two threads try to add elements simultaneously.

```
Thread A

↓

vector.add("Apple")
```

```
Thread B

↓

vector.add("Orange")
```

Without coordination, both threads might modify the internal array at the same time, leading to inconsistent data.

Vector prevents this by synchronizing its methods.

---

# What Does synchronized Mean?

The `synchronized` keyword allows **only one thread at a time** to execute a synchronized method on the same object.

Without synchronization

```
Thread A  ─────────►

Thread B  ─────────►
```

Both threads may modify the object simultaneously.

With synchronization

```
Thread A  ─────────►

            waits

Thread B             ─────────►
```

The second thread waits until the first thread finishes.

---

# Example

A simplified version of the `add()` method

```java
public synchronized boolean add(E element) {

    // Add element

}
```

The synchronized keyword ensures that only one thread can execute this method at a time for a given Vector instance.

---

# Is Synchronization Free?

No.

Synchronization introduces additional work.

Every synchronized method

- Acquires a lock
- Executes the method
- Releases the lock

This makes Vector slower than ArrayList in single-threaded applications.

---

# ArrayList vs Vector

| Feature | ArrayList | Vector |
|----------|-----------|---------|
| Internal Storage | Array | Array |
| Dynamic Resize | Yes | Yes |
| Preserves Order | Yes | Yes |
| Allows Duplicates | Yes | Yes |
| Thread Safe | No | Yes |
| Synchronization | No | Yes |
| Performance | Faster | Slightly Slower |

---

# Should We Use Vector Today?

Generally, **no**.

Modern Java provides better thread-safe alternatives.

Examples

- `Collections.synchronizedList()`
- `CopyOnWriteArrayList`
- Other collections from `java.util.concurrent`

Therefore, Vector is considered a **legacy class**.

---

# What is a Legacy Class?

A legacy class is an older class that remains in Java primarily for backward compatibility.

Examples

- Vector
- Stack
- Hashtable

These classes are still supported but are usually not recommended for new applications.

---

# What is Stack?

**Definition**

> Stack is a legacy class that extends Vector and implements the **Last In, First Out (LIFO)** principle.

Hierarchy

```
Object
   │
Vector
   │
Stack
```

Unlike ArrayList or LinkedList, Stack is specifically designed to process elements in **LIFO order**.

---

# What is LIFO?

LIFO stands for

> **Last In, First Out**

Suppose we push

```
A

B

C
```

The stack becomes

```
Top
 │
 ▼
 C
 B
 A
```

Removing an element removes the most recently inserted element.

```
Top
 │
 ▼
 B
 A
```

---

# Stack Operations

## push()

Adds an element to the top.

```java
Stack<String> stack = new Stack<>();

stack.push("A");
stack.push("B");
stack.push("C");
```

Result

```
Top
 │
 ▼
 C
 B
 A
```

---

## pop()

Removes and returns the top element.

```java
String value = stack.pop();
```

Returns

```
C
```

Remaining stack

```
Top
 │
 ▼
 B
 A
```

---

## peek()

Returns the top element without removing it.

```java
stack.peek();
```

Returns

```
B
```

The stack remains unchanged.

---

## empty()

Checks whether the stack contains any elements.

```java
stack.empty();
```

Returns

- true
- false

---

# Why Is Stack Considered Legacy?

Stack extends Vector.

Therefore, it inherits many methods that are unrelated to stack behavior.

Example

```java
stack.add("X");

stack.insertElementAt("Y", 0);

stack.remove(2);
```

These methods violate the idea of a pure stack.

A stack should ideally expose only

- push()
- pop()
- peek()

This is one reason Stack is considered an outdated design.

---

# Modern Replacement — Deque

Modern Java recommends using **Deque** for stack operations.

Example

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("A");
stack.push("B");

System.out.println(stack.pop());
```

Advantages

- Faster
- Better design
- Cleaner API
- No unnecessary synchronization
- Specifically designed for stack and queue operations

For new applications, prefer **Deque** over Stack.

---

# When Might You Still See Stack?

Stack is commonly found in

- Legacy Java projects
- Older enterprise applications
- Older textbooks
- Interview questions

Understanding Stack remains important because many existing codebases still use it.

---

# Common Interview Mistakes

❌ Vector is the same as ArrayList.

✔ Both are dynamic arrays, but Vector synchronizes its methods.

---

❌ Thread-safe means faster.

✔ Synchronization improves safety, not performance.

---

❌ Stack is the recommended implementation.

✔ Modern Java recommends using Deque (usually ArrayDeque).

---

# Key Takeaways

- Vector is a legacy implementation of the List interface.
- Vector stores elements in a dynamically resized array.
- Vector synchronizes its methods, making it thread-safe.
- Synchronization introduces additional overhead.
- Stack extends Vector.
- Stack follows the LIFO principle.
- Main Stack operations are push(), pop(), peek(), and empty().
- Stack is considered legacy because it inherits many unrelated methods from Vector.
- Modern Java recommends Deque instead of Stack.

---

# Interview Questions

## Basic

1. What is Vector?
2. How is Vector implemented internally?
3. What does synchronized mean?
4. Why is Vector slower than ArrayList?
5. What is a legacy class?
6. What is Stack?
7. What does LIFO mean?
8. What are the primary Stack operations?

## Intermediate

9. Why does Stack extend Vector?
10. Why is Stack considered poor design today?
11. Why does Java recommend Deque instead of Stack?
12. When would you use Vector instead of ArrayList?
13. Compare Stack and Deque.

# Chapter 7 — Set Interface

The **Set** interface is the second major collection type in the Java Collections Framework.

Unlike `List`, which focuses on **ordered collections**, `Set` focuses on **uniqueness**.

Hierarchy

```
Iterable
      │
Collection
      │
     Set
```

`Set` is an **interface**, not a class.

Common implementations

- HashSet
- LinkedHashSet
- TreeSet

---

# Why Was Set Introduced?

Suppose we are storing registered students.

Using a List

```text
Guru
Rahul
Anitha
Guru
Rahul
```

Duplicate registrations are possible.

In many real-world applications, duplicates are not allowed.

Examples

- Student IDs
- Email addresses
- Product IDs
- Usernames

Java introduced the **Set** interface to automatically prevent duplicate elements.

---

# What is a Set?

**Definition**

> A Set is a collection that **does not allow duplicate elements**.

Unlike List

- Duplicate elements are not allowed.
- Index-based access is not supported.
- Ordering depends on the implementation.

---

# Characteristics of Set

## 1. No Duplicate Elements

Example

```java
Set<String> names = new HashSet<>();

names.add("Guru");
names.add("Rahul");
names.add("Guru");
```

Result

```
Guru
Rahul
```

The duplicate `"Guru"` is ignored.

No exception is thrown.

---

## 2. add() Returns a boolean

Unlike many collection methods, `add()` returns a boolean.

```java
boolean result = names.add("Guru");
```

Return values

```
true
```

The element was added.

```
false
```

The element already existed.

Example

```java
System.out.println(names.add("Guru"));   // true

System.out.println(names.add("Guru"));   // false
```

---

## 3. No Index-Based Access

Unlike List

```java
list.get(2);
```

A Set does not provide

```java
set.get(2);
```

or

```java
set.remove(2);
```

because a Set has no concept of element positions.

---

## 4. Order Depends on the Implementation

The Set interface itself **does not guarantee any ordering**.

Different implementations behave differently.

### HashSet

```
B
A
D
C
```

No guaranteed order.

---

### LinkedHashSet

```
A
B
C
D
```

Maintains insertion order.

---

### TreeSet

```
A
B
C
D
```

Maintains sorted order.

---

## 5. Allows One null (HashSet & LinkedHashSet)

Example

```java
Set<String> names = new HashSet<>();

names.add(null);
names.add(null);
```

Result

```
null
```

Only one null value is stored because duplicates are not allowed.

> **Note:** TreeSet handles null differently because it sorts elements. This will be covered in the TreeSet chapter.

---

# Methods Available in Set

Set extends Collection and **does not introduce any new methods**.

Common inherited methods

```java
add(E e)

remove(Object o)

contains(Object o)

size()

isEmpty()

clear()

iterator()

forEach()

stream()

parallelStream()

toArray()
```

---

# Methods Not Available

Because Set has no index-based access, the following methods do not exist.

```java
get()

set()

add(index)

remove(index)

indexOf()

lastIndexOf()

subList()

listIterator()
```

These belong to the List interface.

---

# Example

```java
Set<String> languages = new HashSet<>();

languages.add("Java");
languages.add("Python");
languages.add("Java");

System.out.println(languages);
```

Possible output

```
[Python, Java]
```

Observations

- Duplicate `"Java"` is ignored.
- Order is not guaranteed.

---

# Why Doesn't Set Have get(index)?

Suppose

```java
set.get(0);
```

Which element should be returned?

```
Java

Python

C++

Go
```

The Set interface does not define a first, second, or third element.

Therefore, index-based methods are not meaningful.

---

# Real-World Examples

## Registered Students

```
Guru
Rahul
Anitha
```

Each student should appear only once.

---

## Email Addresses

```
guru@gmail.com

rahul@gmail.com
```

Duplicate email IDs are not allowed.

---

## Product IDs

```
P101

P102

P103
```

Each product ID must be unique.

---

## Tags

```
Java

Spring

Backend
```

Repeated tags are unnecessary.

---

# List vs Set

| Feature | List | Set |
|----------|------|-----|
| Duplicate Elements | Allowed | Not Allowed |
| Insertion Order | Preserved | Depends on implementation |
| Index-Based Access | Yes | No |
| get(index) | Yes | No |
| add(index) | Yes | No |
| remove(index) | Yes | No |
| Primary Purpose | Ordered collection | Unique elements |

---

# Why Doesn't Set Add New Methods?

The Collection interface already provides all necessary operations.

```
add()

remove()

contains()

size()
```

The Set interface changes the **behavior (contract)** rather than introducing new operations.

Its contract is simply

> **No duplicate elements are allowed.**

---

# Common Interview Mistakes

❌ Set never preserves order.

✔ The Set interface does not guarantee order.

- HashSet → No guaranteed order.
- LinkedHashSet → Insertion order.
- TreeSet → Sorted order.

---

❌ add() throws an exception for duplicates.

✔ add() returns false.

---

❌ Set supports get(index).

✔ Set has no positional access.

---

❌ Set introduces new methods.

✔ Set adds no new methods beyond those inherited from Collection.

---

# Key Takeaways

- Set is an interface that extends Collection.
- Duplicate elements are not allowed.
- add() returns true for successful insertion and false for duplicates.
- Set does not support index-based operations.
- Ordering depends on the implementation.
- Set introduces no additional methods; it changes the uniqueness contract.
- Common implementations are HashSet, LinkedHashSet, and TreeSet.

---

# Interview Questions

## Basic

1. What is the Set interface?
2. Why was Set introduced?
3. Does Set allow duplicate elements?
4. Does Set preserve insertion order?
5. Does Set support indexing?
6. What does add() return when inserting a duplicate?

## Intermediate

7. Why doesn't Set provide get(index)?
8. Why doesn't Set introduce new methods?
9. Compare List and Set.
10. Name the major implementations of Set and explain their ordering behavior.

# Chapter 8 — HashSet

HashSet is the most commonly used implementation of the **Set** interface.

It stores **unique elements** and provides very fast insertion, deletion, and searching.

Hierarchy

```
Iterable
      │
Collection
      │
     Set
      │
  HashSet
```

---

# What is HashSet?

**Definition**

> HashSet is an implementation of the Set interface that stores **unique elements** using **hashing**.

Characteristics

- Does not allow duplicate elements.
- Does not guarantee iteration order.
- Allows one null element.
- Average time complexity for add(), remove(), and contains() is O(1).

---

# Why Was HashSet Introduced?

The Set interface defines the rule

> No duplicate elements.

HashSet provides an efficient implementation of this rule using **hashing**, allowing duplicate detection without comparing every element in the collection.

---

# Internal Implementation

HashSet does **not** store elements directly.

Internally, it uses a **HashMap**.

Conceptually

```java
class HashSet<E> {

    private HashMap<E, Object> map;

    private static final Object PRESENT = new Object();

}
```

Each element in the HashSet becomes a **key** in the internal HashMap.

The value is a constant dummy object (`PRESENT`) because only the keys are important.

Example

```java
Set<String> set = new HashSet<>();

set.add("Java");
set.add("Spring");
```

Conceptually becomes

```
HashMap

Key         Value

Java   ->   PRESENT

Spring ->   PRESENT
```

---

# How add() Works

When

```java
set.add("Java");
```

is executed,

HashSet internally performs

```java
map.put("Java", PRESENT);
```

If the key does not exist

- The element is inserted.
- add() returns true.

If the key already exists

- No duplicate is inserted.
- add() returns false.

---

# How Does HashSet Detect Duplicates?

HashSet uses two methods

```java
hashCode()

equals()
```

The simplified process is

1. Calculate the object's hashCode().
2. Locate the appropriate storage location.
3. If another element is already there, compare them using equals().
4. If equals() returns true, the element is considered a duplicate.
5. Otherwise, it is stored as a different element.

> The detailed storage mechanism (buckets, collisions, resizing, etc.) is part of HashMap internals and will be covered in the Map chapter.

---

# equals() and hashCode()

Both methods are required.

Example

```java
class Student {

    private int id;

    private String name;

}
```

If only equals() is overridden and hashCode() is not, logically equal objects may still be stored as duplicates.

Rule

> If two objects are equal according to equals(), they **must** produce the same hashCode().

---

# Time Complexity

| Operation | Average Complexity |
|-----------|--------------------|
| add() | O(1) |
| contains() | O(1) |
| remove() | O(1) |

Worst case

```
O(n)
```

---

# Does HashSet Preserve Order?

No.

Example

```java
Set<String> set = new HashSet<>();

set.add("A");
set.add("B");
set.add("C");
```

Possible output

```
[C, A, B]
```

The order is not guaranteed.

---

# Does HashSet Allow null?

Yes.

Example

```java
Set<String> set = new HashSet<>();

set.add(null);

set.add(null);
```

Result

```
null
```

Only one null value is stored because duplicates are not allowed.

---

# When Should You Use HashSet?

Use HashSet when

- Elements must be unique.
- Ordering is not important.
- Fast searching is required.

Examples

- Usernames
- Email addresses
- Product IDs
- Tags
- Unique records

---

# HashSet vs List

| Feature | HashSet | List |
|----------|----------|------|
| Duplicates | Not Allowed | Allowed |
| Order | Not Guaranteed | Preserved |
| Index Access | No | Yes |
| contains() | O(1) Average | O(n) |

---

# Common Interview Mistakes

❌ HashSet stores data directly.

✔ HashSet is internally backed by a HashMap.

---

❌ HashSet uses only hashCode().

✔ HashSet uses both hashCode() and equals().

---

❌ Overriding equals() is enough.

✔ equals() and hashCode() should always be overridden together.

---

❌ HashSet preserves insertion order.

✔ Use LinkedHashSet if insertion order is required.

---

# Key Takeaways

- HashSet implements the Set interface.
- Internally backed by a HashMap.
- Stores elements as keys in the internal HashMap.
- Duplicate detection relies on hashCode() and equals().
- Does not preserve insertion order.
- Allows one null element.
- Average complexity for add(), remove(), and contains() is O(1).

---

# Interview Questions

## Basic

1. What is HashSet?
2. Why was HashSet introduced?
3. Does HashSet allow duplicate elements?
4. Does HashSet preserve insertion order?
5. Does HashSet allow null?

## Intermediate

6. How is HashSet implemented internally?
7. Why does HashSet use a HashMap?
8. How does HashSet detect duplicate elements?
9. Why are equals() and hashCode() both required?
10. When would you choose HashSet over a List?

# Chapter 9 — LinkedHashSet

`LinkedHashSet` is an implementation of the **Set** interface that stores **unique elements** while preserving **insertion order**.

It combines the advantages of **HashSet** (fast lookups) with the ability to maintain the order in which elements were added.

Hierarchy

```
Iterable
      │
Collection
      │
     Set
      │
  HashSet
      │
LinkedHashSet
```

---

# Why Was LinkedHashSet Introduced?

`HashSet` efficiently stores unique elements but **does not guarantee iteration order**.

Example

```java
Set<String> technologies = new HashSet<>();

technologies.add("Java");
technologies.add("Spring");
technologies.add("Docker");
technologies.add("MongoDB");

System.out.println(technologies);
```

Possible Output

```
[Docker, Java, MongoDB, Spring]
```

The order is unpredictable.

In many applications, the order in which elements are inserted is important.

Examples

- Recently viewed products
- Navigation history
- Ordered tags
- Recently opened files

To solve this problem, Java introduced **LinkedHashSet**.

---

# What is LinkedHashSet?

**Definition**

> LinkedHashSet is an implementation of the Set interface that stores **unique elements** while maintaining **insertion order**.

Characteristics

- Does not allow duplicate elements.
- Preserves insertion order.
- Allows one null element.
- Provides average O(1) time complexity for add(), remove(), and contains().

---

# Characteristics

## 1. No Duplicate Elements

Example

```java
Set<String> set = new LinkedHashSet<>();

set.add("Java");
set.add("Spring");
set.add("Java");
```

Result

```
Java
Spring
```

The duplicate `"Java"` is ignored.

---

## 2. Preserves Insertion Order

Example

```java
Set<String> set = new LinkedHashSet<>();

set.add("Java");
set.add("Spring");
set.add("Docker");
set.add("MongoDB");

System.out.println(set);
```

Output

```
Java
Spring
Docker
MongoDB
```

Elements are returned in the same order they were inserted.

---

## 3. Allows One null

Example

```java
Set<String> set = new LinkedHashSet<>();

set.add(null);

set.add(null);
```

Result

```
null
```

Only one null value is stored.

---

## 4. Fast Operations

Average time complexity

| Operation | Complexity |
|-----------|------------|
| add() | O(1) |
| contains() | O(1) |
| remove() | O(1) |

---

# How Does LinkedHashSet Preserve Order?

Conceptually, LinkedHashSet combines

- Hashing for fast lookup.
- A linked structure to remember insertion order.

Conceptual view

```
Hash Storage

Java
Spring
Docker
MongoDB
```

Insertion order

```
Java
 │
 ▼
Spring
 │
 ▼
Docker
 │
 ▼
MongoDB
```

When iterating, Java follows the linked order instead of the hash storage order.

> **Note:** The detailed implementation uses `LinkedHashMap` internally. We'll study its internals in the Map chapter.

---

# What Happens When a Duplicate is Added?

Example

```java
Set<String> set = new LinkedHashSet<>();

set.add("Java");
set.add("Spring");
set.add("Java");
```

Output

```
Java
Spring
```

The duplicate is ignored.

---

# Does Re-inserting an Element Change Its Position?

Example

```java
LinkedHashSet<String> set = new LinkedHashSet<>();

set.add("A");
set.add("B");
set.add("C");

set.add("A");
```

Output

```
A
B
C
```

The existing element remains in its original position.

Re-inserting an element does **not** move it to the end.

---

# Internal Architecture (High Level)

```
LinkedHashSet
        │
        ▼
Uses LinkedHashMap Internally
        │
        ▼
Hashing + Linked Structure
```

At this stage, it is sufficient to know that LinkedHashSet relies on a `LinkedHashMap`.

The complete internal implementation will be covered in the Map chapter.

---

# HashSet vs LinkedHashSet

| Feature | HashSet | LinkedHashSet |
|----------|----------|---------------|
| Duplicate Elements | Not Allowed | Not Allowed |
| Order | Not Guaranteed | Preserves Insertion Order |
| Allows null | Yes | Yes |
| Average add() | O(1) | O(1) |
| Average contains() | O(1) | O(1) |
| Average remove() | O(1) | O(1) |
| Memory Usage | Lower | Slightly Higher |
| Internal Implementation | HashMap | LinkedHashMap |

---

# When Should You Use LinkedHashSet?

Use LinkedHashSet when

- Elements must be unique.
- Insertion order must be preserved.
- Fast lookup is required.

Examples

- Recently viewed products
- Search history
- Ordered tags
- Browser history
- Recently opened files

---

# Common Interview Mistakes

❌ LinkedHashSet sorts elements.

✔ It preserves **insertion order**, not sorted order.

---

❌ LinkedHashSet allows duplicate elements.

✔ It follows the Set contract and stores only unique elements.

---

❌ Re-inserting an element moves it to the end.

✔ Existing elements remain in their original insertion position.

---

❌ LinkedHashSet is slower because it is linked.

✔ Basic operations still have average O(1) complexity. It only uses slightly more memory than HashSet.

---

# Key Takeaways

- LinkedHashSet extends HashSet.
- Stores unique elements.
- Preserves insertion order.
- Allows one null element.
- Uses hashing for fast operations.
- Internally backed by LinkedHashMap.
- Average complexity for add(), remove(), and contains() is O(1).
- Uses slightly more memory than HashSet due to maintaining insertion order.

---

# Interview Questions

## Basic

1. What is LinkedHashSet?
2. Why was LinkedHashSet introduced?
3. Does LinkedHashSet allow duplicate elements?
4. Does LinkedHashSet preserve insertion order?
5. Does LinkedHashSet allow null?

## Intermediate

6. How does LinkedHashSet preserve insertion order?
7. What happens if you add the same element twice?
8. Does re-inserting an element change its position?
9. How is LinkedHashSet implemented internally?
10. Compare HashSet and LinkedHashSet.

# Chapter 10 — TreeSet

`TreeSet` is an implementation of the **Set** interface that stores **unique elements in sorted order**.

Unlike `HashSet` and `LinkedHashSet`, which are based on **hashing**, `TreeSet` is based on **sorting**.

Hierarchy

```
Iterable
      │
Collection
      │
     Set
      │
 SortedSet
      │
NavigableSet
      │
   TreeSet
```

Unlike `HashSet`, `TreeSet` also implements **SortedSet** and **NavigableSet**, which provide additional sorting and navigation capabilities.

---

# Why Was TreeSet Introduced?

Suppose we are storing student marks.

Input

```
85
72
96
68
91
```

Using a HashSet

```
96
68
85
91
72
```

Order is unpredictable.

Using a LinkedHashSet

```
85
72
96
68
91
```

Insertion order is preserved.

But many applications require the data to always remain sorted.

Expected output

```
68
72
85
91
96
```

To solve this problem, Java introduced **TreeSet**.

---

# What is TreeSet?

**Definition**

> TreeSet is an implementation of the Set interface that stores **unique elements in sorted order**.

Characteristics

- Does not allow duplicate elements.
- Automatically sorts elements.
- Does not support indexing.
- Does not allow arbitrary null values.
- Average time complexity is O(log n).

---

# Automatic Sorting

Example

```java
Set<Integer> marks = new TreeSet<>();

marks.add(85);
marks.add(72);
marks.add(96);
marks.add(68);
marks.add(91);

System.out.println(marks);
```

Output

```
[68, 72, 85, 91, 96]
```

TreeSet automatically sorts the elements.

---

# How Does TreeSet Sort?

TreeSet compares elements while inserting them.

Example

```
85

72
```

Since

```
72 < 85
```

72 is placed before 85.

TreeSet continuously compares elements and maintains sorted order.

---

# Natural Ordering

Many Java classes already know how to compare themselves.

Examples

- Integer
- Long
- Double
- String
- LocalDate

These classes implement the `Comparable` interface.

Therefore, they work directly with TreeSet.

Example

```java
TreeSet<Integer> numbers = new TreeSet<>();
```

No additional configuration is required.

---

# Comparable

**Definition**

> Comparable defines the **natural ordering** of objects.

Interface

```java
public interface Comparable<T> {

    int compareTo(T other);

}
```

The `compareTo()` method returns

| Return Value | Meaning |
|--------------|---------|
| Negative | Current object is smaller |
| Zero | Objects are equal |
| Positive | Current object is larger |

---

# TreeSet with Comparable

```java
class Student implements Comparable<Student> {

    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
```

Usage

```java
TreeSet<Student> students = new TreeSet<>();

students.add(new Student(103, "Guru"));
students.add(new Student(101, "Rahul"));
students.add(new Student(105, "Anitha"));
students.add(new Student(102, "Vijay"));

System.out.println(students);
```

Output

```
101 - Rahul
102 - Vijay
103 - Guru
105 - Anitha
```

Students are sorted by `id`.

---

# Comparator

Sometimes one natural ordering is not enough.

Example

- Sort by ID
- Sort by Name
- Sort by Age

Instead of modifying the class, use a `Comparator`.

Example

```java
Comparator<Student> byName =
        Comparator.comparing(Student::getName);

TreeSet<Student> students =
        new TreeSet<>(byName);
```

Now the TreeSet sorts students by name.

---

# Comparable vs Comparator

| Comparable | Comparator |
|------------|------------|
| Inside the class | Outside the class |
| Defines natural ordering | Defines custom ordering |
| Uses compareTo() | Uses compare() |
| One ordering | Multiple orderings |

---

# What Happens with Custom Objects?

Suppose

```java
class Student {

    int id;
    String name;

}
```

Then

```java
TreeSet<Student> students = new TreeSet<>();

students.add(new Student());
```

throws

```
ClassCastException
```

because Java does not know how to compare two Student objects.

To fix this

- Implement Comparable
- OR provide a Comparator

---

# Does TreeSet Allow null?

No.

Example

```java
TreeSet<String> set = new TreeSet<>();

set.add(null);
```

Result

```
NullPointerException
```

TreeSet cannot determine how to compare null with other elements.

---

# Important Methods

TreeSet inherits methods from Set, SortedSet, and NavigableSet.

Common methods

```java
first()

last()

higher()

lower()

ceiling()

floor()

pollFirst()

pollLast()
```

These methods allow navigation through the sorted collection.

---

# Time Complexity

| Operation | Complexity |
|-----------|------------|
| add() | O(log n) |
| contains() | O(log n) |
| remove() | O(log n) |

Compared to HashSet

| Collection | add() |
|------------|--------|
| HashSet | O(1) Average |
| LinkedHashSet | O(1) Average |
| TreeSet | O(log n) |

---

# Internal Architecture

High-level architecture

```
TreeSet
      │
      ▼
TreeMap
      │
      ▼
Red-Black Tree
```

TreeSet is internally backed by a **TreeMap**.

The Red-Black Tree implementation will be studied in detail during the Map chapter.

---

# HashSet vs LinkedHashSet vs TreeSet

| Feature | HashSet | LinkedHashSet | TreeSet |
|----------|----------|---------------|----------|
| Duplicate Elements | Not Allowed | Not Allowed | Not Allowed |
| Order | Not Guaranteed | Insertion Order | Sorted Order |
| Allows null | Yes | Yes | No |
| Index-Based Access | No | No | No |
| add() Complexity | O(1) Average | O(1) Average | O(log n) |
| Internal Structure | HashMap | LinkedHashMap | TreeMap |

---

# When Should You Use TreeSet?

Use TreeSet when

- Elements must be unique.
- Elements should always remain sorted.
- Navigation operations are required.

Examples

- Student rankings
- Product prices
- Leaderboards
- Sorted dates
- Alphabetically sorted names

---

# Common Interview Mistakes

❌ TreeSet preserves insertion order.

✔ TreeSet preserves **sorted order**.

---

❌ TreeSet uses hashing.

✔ TreeSet uses a tree-based structure.

---

❌ TreeSet allows null.

✔ TreeSet throws `NullPointerException` when using natural ordering.

---

❌ compareTo() returns boolean.

✔ compareTo() returns a negative, zero, or positive integer.

---

# Key Takeaways

- TreeSet implements the Set interface through SortedSet and NavigableSet.
- Stores unique elements in sorted order.
- Uses natural ordering (`Comparable`) or custom ordering (`Comparator`).
- Internally backed by TreeMap.
- Does not allow arbitrary null values.
- Average complexity for add(), remove(), and contains() is O(log n).
- Suitable when sorted unique data is required.

---

# Interview Questions

## Basic

1. What is TreeSet?
2. Why was TreeSet introduced?
3. Does TreeSet allow duplicate elements?
4. Does TreeSet preserve insertion order?
5. Does TreeSet allow null?

## Intermediate

6. How does TreeSet maintain sorted order?
7. What is natural ordering?
8. Explain Comparable and Comparator.
9. Why does TreeSet throw ClassCastException for custom objects?
10. What is the internal data structure used by TreeSet?
11. Compare HashSet, LinkedHashSet, and TreeSet.

# Chapter 11 — Queue Interface

The **Queue** interface represents a collection designed to process elements in a specific order.

Most queue implementations follow the **FIFO (First In, First Out)** principle, where the first element inserted is the first element removed.

Hierarchy

```
Iterable
      │
Collection
      │
    Queue
      │
 ┌────┴────────────┐
 │                 │
Deque       PriorityQueue
 │
ArrayDeque
LinkedList
```

Queue is an **interface**, and different implementations provide different behaviors.

---

# Why Was Queue Introduced?

Many real-world problems require elements to be processed in the order they arrive.

Examples

- Customers waiting in a billing queue
- Printer jobs
- Task scheduling
- CPU scheduling
- Message processing
- Breadth-First Search (BFS)

Using a queue ensures that the **first element added is processed first**.

---

# What is FIFO?

FIFO stands for

> **First In, First Out**

Example

```
Insert

A
B
C
```

Queue

```
Front

↓

A

B

C

↑

Rear
```

Removing an element

```
poll()
```

Removes

```
A
```

Remaining

```
Front

↓

B

C
```

The first inserted element is the first one removed.

---

# Why Not Use a List?

Suppose we use an `ArrayList`.

```java
List<String> list = new ArrayList<>();

list.add("A");
list.add("B");
list.add("C");

list.remove(0);
```

After removing the first element

```
Before

A
B
C

After

B
C
```

All remaining elements must shift left.

Time complexity

```
O(n)
```

Queue implementations are optimized for insertion and removal according to queue semantics.

---

# Characteristics of Queue

- Usually follows FIFO.
- Allows duplicate elements.
- Does not support index-based access.
- Elements are inserted at the rear.
- Elements are removed from the front.
- Provides specialized methods for insertion, removal, and retrieval.

---

# Queue Methods

Queue provides six primary methods.

| Operation | Throws Exception | Returns Special Value |
|-----------|------------------|-----------------------|
| Insert | add() | offer() |
| Remove | remove() | poll() |
| Retrieve Head | element() | peek() |

---

# add()

Adds an element to the queue.

```java
Queue<String> queue = new LinkedList<>();

queue.add("Java");
queue.add("Spring");
```

Returns

```java
boolean
```

If insertion fails (for example, in a bounded queue), it throws an exception.

---

# offer()

Also inserts an element.

```java
queue.offer("Docker");
```

Difference

If insertion fails

```
Returns false
```

instead of throwing an exception.

---

# remove()

Removes and returns the head element.

Example

```
Queue

A
B
C
```

```java
queue.remove();
```

Returns

```
A
```

Queue becomes

```
B
C
```

If the queue is empty

```
NoSuchElementException
```

is thrown.

---

# poll()

Also removes the head element.

```java
queue.poll();
```

Difference

If the queue is empty

```
Returns null
```

instead of throwing an exception.

---

# element()

Returns the head element without removing it.

Example

```
Queue

A
B
C
```

```java
queue.element();
```

Returns

```
A
```

Queue remains unchanged.

If the queue is empty

```
NoSuchElementException
```

is thrown.

---

# peek()

Also returns the head element without removing it.

```java
queue.peek();
```

Returns

```
A
```

Queue remains unchanged.

If the queue is empty

```
Returns null
```

---

# Queue Operations Summary

Initial Queue

```
Front

↓

A

B

C

↑

Rear
```

| Method | Result | Queue After |
|---------|--------|-------------|
| offer("D") | Adds D | A B C D |
| add("D") | Adds D | A B C D |
| peek() | Returns A | A B C |
| element() | Returns A | A B C |
| poll() | Removes A | B C |
| remove() | Removes A | B C |

---

# Exception vs Special Value

| Method | Empty Queue |
|----------|-------------|
| add() | Exception (if insertion fails) |
| offer() | false (if insertion fails) |
| remove() | NoSuchElementException |
| poll() | null |
| element() | NoSuchElementException |
| peek() | null |

**Best Practice**

- Prefer `offer()` over `add()`.
- Prefer `poll()` over `remove()`.
- Prefer `peek()` over `element()`.

These methods avoid unnecessary exceptions.

---

# Example

```java
Queue<String> queue = new LinkedList<>();

queue.offer("Java");
queue.offer("Spring");
queue.offer("Docker");

System.out.println(queue);

System.out.println(queue.peek());

System.out.println(queue.poll());

System.out.println(queue);
```

Output

```
[Java, Spring, Docker]

Java

Java

[Spring, Docker]
```

Notice

- `peek()` reads the head.
- `poll()` removes the head.

---

# Queue vs List

| Feature | Queue | List |
|----------|-------|------|
| Ordering | FIFO | Insertion Order |
| Duplicates | Allowed | Allowed |
| Index-Based Access | No | Yes |
| Primary Purpose | Processing | General Storage |
| Front Removal | Optimized | Expensive in ArrayList |

---

# Common Queue Implementations

| Implementation | Description |
|----------------|-------------|
| LinkedList | General-purpose queue implementation |
| ArrayDeque | Faster implementation for most queue operations |
| PriorityQueue | Orders elements based on priority instead of FIFO |

---

# Real-World Applications

Queue is commonly used in

- Printer queues
- Task scheduling
- CPU scheduling
- Customer service systems
- Breadth-First Search (BFS)
- Job processing
- Request processing
- Message queues

---

# Common Interview Mistakes

❌ Queue always means FIFO.

✔ Most queues are FIFO, but `PriorityQueue` processes elements by priority.

---

❌ `peek()` removes an element.

✔ It only retrieves the head.

---

❌ `poll()` throws an exception if the queue is empty.

✔ It returns `null`.

---

❌ `remove()` returns `null` if the queue is empty.

✔ It throws `NoSuchElementException`.

---

# Key Takeaways

- Queue represents FIFO processing.
- Elements are inserted at the rear and removed from the front.
- `offer()` and `poll()` are generally preferred over `add()` and `remove()`.
- `peek()` retrieves the head without removing it.
- Queue does not support index-based access.
- Common implementations include `LinkedList`, `ArrayDeque`, and `PriorityQueue`.

---

# Interview Questions

## Basic

1. What is a Queue?
2. Why was the Queue interface introduced?
3. Explain FIFO.
4. What is the difference between `add()` and `offer()`?
5. What is the difference between `remove()` and `poll()`?
6. What is the difference between `element()` and `peek()`?

## Intermediate

7. Why is `ArrayList` not suitable for queue operations?
8. Why are `offer()`, `poll()`, and `peek()` preferred in production code?
9. Compare Queue and List.
10. Name the common implementations of Queue.
11. Does Queue support index-based access?
12. Name some real-world applications of Queue.

# Chapter 12 — PriorityQueue

`PriorityQueue` is an implementation of the **Queue** interface that processes elements based on **priority** instead of insertion order.

Unlike a normal queue, which follows **FIFO (First In, First Out)**, a `PriorityQueue` always removes the **highest-priority** element first.

By default, Java treats the **smallest element as the highest priority**.

Hierarchy

```
Iterable
      │
Collection
      │
    Queue
      │
PriorityQueue
```

---

# Why Was PriorityQueue Introduced?

Some real-world applications cannot process elements in the order they arrive.

Example

Hospital Emergency Room

```
Arrival Order

Patient A - Minor Injury
Patient B - Heart Attack
Patient C - Fever
Patient D - Accident
```

If FIFO is followed

```
A
B
C
D
```

Patient A is treated first.

This is not practical.

Instead, patients are treated based on **priority**.

```
Heart Attack
Accident
Fever
Minor Injury
```

To support this behavior, Java introduced **PriorityQueue**.

---

# What is PriorityQueue?

**Definition**

> A `PriorityQueue` is a Queue implementation that orders elements according to their priority instead of insertion order.

---

# Characteristics

- Allows duplicate elements.
- Does **not** preserve insertion order.
- Does **not** follow FIFO.
- Automatically orders elements based on priority.
- By default behaves as a **Min Heap**.
- Does not allow `null`.
- Backed internally by a **Binary Heap**.
- Average insertion and removal complexity is **O(log n)**.

---

# Example

```java
Queue<Integer> pq = new PriorityQueue<>();

pq.offer(50);
pq.offer(20);
pq.offer(70);
pq.offer(10);
pq.offer(40);

System.out.println(pq);
```

Possible Output

```
[10, 20, 70, 50, 40]
```

The output is **not guaranteed to be fully sorted**.

The only guarantee is

```
The head element has the highest priority.
```

---

# Removing Elements

```java
while (!pq.isEmpty()) {
    System.out.println(pq.poll());
}
```

Output

```
10
20
40
50
70
```

Although the internal structure is not fully sorted, removing elements one by one produces sorted output.

---

# Natural Ordering

PriorityQueue uses the **natural ordering** of elements.

Example

```java
Queue<String> pq = new PriorityQueue<>();

pq.offer("Java");
pq.offer("Spring");
pq.offer("Docker");

while (!pq.isEmpty()) {
    System.out.println(pq.poll());
}
```

Output

```
Docker
Java
Spring
```

Since `String` implements `Comparable`, elements are sorted alphabetically.

---

# Internal Data Structure

PriorityQueue is implemented using a **Binary Heap**.

Conceptual representation

```
          10
        /    \
      20      70
     /  \
   50   40
```

This is **not** a Binary Search Tree.

It follows the **Heap Property**.

For a Min Heap

```
Parent <= Children
```

Only the root is guaranteed to contain the highest-priority element.

---

# Min Heap (Default)

Example

```java
Queue<Integer> pq = new PriorityQueue<>();

pq.offer(8);
pq.offer(3);
pq.offer(10);
pq.offer(1);

System.out.println(pq.peek());
```

Output

```
1
```

The smallest element is always at the root.

---

# Max Heap

To retrieve the largest element first, provide a custom comparator.

```java
Queue<Integer> pq =
        new PriorityQueue<>(Comparator.reverseOrder());

pq.offer(20);
pq.offer(50);
pq.offer(10);
pq.offer(40);

while (!pq.isEmpty()) {
    System.out.println(pq.poll());
}
```

Output

```
50
40
20
10
```

The queue now behaves as a **Max Heap**.

---

# Custom Objects

Suppose

```java
class Task {

    int priority;
    String name;

}
```

Then

```java
PriorityQueue<Task> tasks =
        new PriorityQueue<>();
```

throws

```
ClassCastException
```

because Java does not know how to compare two `Task` objects.

To fix this

- Implement `Comparable`
- OR provide a `Comparator`

Example

```java
Comparator<Task> byPriority =
        Comparator.comparingInt(Task::getPriority);

PriorityQueue<Task> tasks =
        new PriorityQueue<>(byPriority);
```

---

# Time Complexity

| Operation | Complexity |
|-----------|------------|
| offer() | O(log n) |
| poll() | O(log n) |
| peek() | O(1) |
| contains() | O(n) |

---

# PriorityQueue vs Queue (LinkedList)

| Feature | LinkedList Queue | PriorityQueue |
|----------|------------------|---------------|
| Processing Order | FIFO | Priority |
| Insertion Order | Preserved | Not Preserved |
| Head Element | First Inserted | Highest Priority |
| Internal Structure | Linked List | Binary Heap |

---

# PriorityQueue vs TreeSet

| Feature | PriorityQueue | TreeSet |
|----------|---------------|----------|
| Duplicates | Allowed | Not Allowed |
| Fully Sorted | No | Yes |
| Internal Structure | Binary Heap | Red-Black Tree |
| add() Complexity | O(log n) | O(log n) |
| peek()/first() | Highest Priority | Smallest Element |

---

# Real-World Applications

PriorityQueue is used in

- CPU scheduling
- Task scheduling
- Dijkstra's shortest path algorithm
- Huffman Coding
- A* Search
- Event simulation
- Load balancing
- Top-K largest/smallest problems

---

# Common Interview Mistakes

❌ PriorityQueue is always sorted.

✔ Only the head element is guaranteed to have the highest priority.

---

❌ PriorityQueue preserves insertion order.

✔ It does not.

---

❌ PriorityQueue follows FIFO.

✔ It processes elements based on priority.

---

❌ PriorityQueue is implemented using a Binary Search Tree.

✔ It is implemented using a Binary Heap.

---

# Key Takeaways

- PriorityQueue is a Queue implementation that processes elements by priority.
- It does not follow FIFO.
- By default, it behaves as a Min Heap.
- A custom Comparator can be used to create a Max Heap.
- Allows duplicate elements.
- Does not allow `null`.
- Internally backed by a Binary Heap.
- `peek()` is O(1).
- `offer()` and `poll()` are O(log n).

---

# Interview Questions

## Basic

1. What is a PriorityQueue?
2. Why was PriorityQueue introduced?
3. Does PriorityQueue follow FIFO?
4. Does PriorityQueue preserve insertion order?
5. Does PriorityQueue allow duplicate elements?
6. Does PriorityQueue allow `null`?

## Intermediate

7. What internal data structure does PriorityQueue use?
8. Why is PriorityQueue not fully sorted?
9. Explain Min Heap and Max Heap.
10. How do you create a Max Heap?
11. Why does PriorityQueue throw `ClassCastException` for custom objects?
12. Compare PriorityQueue and TreeSet.

# Chapter 13 — Deque Interface

A **Deque (Double Ended Queue)** is an extension of the **Queue** interface that allows insertion, removal, and retrieval of elements from **both the front and the rear**.

Unlike a normal Queue, which supports insertion at one end and removal at the other, a Deque supports operations at **both ends**.

Hierarchy

```
Iterable
      │
Collection
      │
    Queue
      │
    Deque
      │
 ┌────┴─────────┐
 │              │
ArrayDeque   LinkedList
```

---

# Why Was Deque Introduced?

A normal Queue supports

- Insert at the rear
- Remove from the front

Example

```
Front

↓

A

B

C

↑

Rear
```

Queue Operations

```
offer()

↓

Rear
```

```
poll()

↓

Front
```

Suppose an application also needs

- Insert at the front
- Remove from the rear

A Queue cannot perform these operations.

To solve this problem, Java introduced **Deque (Double Ended Queue)**.

---

# What is Deque?

**Definition**

> A Deque is a linear collection that allows insertion, removal, and retrieval of elements at both the front and rear.

Deque stands for

```
Double Ended Queue
```

Deque extends the Queue interface and provides additional operations for both ends.

---

# Characteristics

- Allows insertion at both ends.
- Allows removal at both ends.
- Allows retrieval from both ends.
- Usually allows duplicate elements.
- Does not support index-based access.
- Can behave as both a Queue and a Stack.

---

# Visual Representation

```
Front

↓

A

B

C

↑

Rear
```

Possible operations

```
addFirst()

↓

Front
```

```
addLast()

↓

Rear
```

```
removeFirst()

↓

Front
```

```
removeLast()

↓

Rear
```

Elements can be inserted and removed from either end.

---

# Deque Methods

| Operation | Front | Rear |
|-----------|-------|------|
| Insert | addFirst() | addLast() |
| Insert (Safe) | offerFirst() | offerLast() |
| Remove | removeFirst() | removeLast() |
| Remove (Safe) | pollFirst() | pollLast() |
| Retrieve | getFirst() | getLast() |
| Retrieve (Safe) | peekFirst() | peekLast() |

Like Queue, Deque provides

- Exception-based methods
- Safe methods that return special values

---

# Adding Elements

```java
Deque<String> deque = new ArrayDeque<>();

deque.addFirst("B");
deque.addFirst("A");

deque.addLast("C");
deque.addLast("D");

System.out.println(deque);
```

Output

```
[A, B, C, D]
```

Final structure

```
Front

↓

A

B

C

D

↑

Rear
```

---

# Removing Elements

Current Deque

```
A

B

C

D
```

```java
deque.removeFirst();
```

Removes

```
A
```

Remaining

```
B

C

D
```

```java
deque.removeLast();
```

Removes

```
D
```

Remaining

```
B

C
```

---

# Peek Operations

Current Deque

```
A

B

C
```

```java
deque.peekFirst();
```

Returns

```
A
```

Deque remains unchanged.

```java
deque.peekLast();
```

Returns

```
C
```

Deque remains unchanged.

---

# Exception vs Safe Methods

| Method | Empty Deque |
|----------|-------------|
| removeFirst() | NoSuchElementException |
| pollFirst() | null |
| getFirst() | NoSuchElementException |
| peekFirst() | null |
| removeLast() | NoSuchElementException |
| pollLast() | null |
| getLast() | NoSuchElementException |
| peekLast() | null |

**Best Practice**

Prefer

- offerFirst()
- offerLast()
- pollFirst()
- pollLast()
- peekFirst()
- peekLast()

These methods avoid unnecessary exceptions.

---

# Deque as a Queue

Queue behavior

- Insert at the rear
- Remove from the front

```java
Deque<Integer> deque = new ArrayDeque<>();

deque.offerLast(10);
deque.offerLast(20);
deque.offerLast(30);

System.out.println(deque.pollFirst());
```

Output

```
10
```

Deque behaves exactly like a FIFO Queue.

---

# Deque as a Stack

Stack behavior

- Insert at the top
- Remove from the top

Deque provides stack operations.

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.pop());
```

Output

```
30
```

Deque behaves like a LIFO Stack.

---

# Stack Methods Provided by Deque

| Stack Method | Equivalent Deque Method |
|---------------|-------------------------|
| push(e) | addFirst(e) |
| pop() | removeFirst() |
| peek() | peekFirst() |

This is why `ArrayDeque` is generally preferred over the legacy `Stack` class.

---

# Time Complexity

For common implementations (`ArrayDeque` and `LinkedList`)

| Operation | Complexity |
|-----------|------------|
| addFirst() | O(1) |
| addLast() | O(1) |
| removeFirst() | O(1) |
| removeLast() | O(1) |
| peekFirst() | O(1) |
| peekLast() | O(1) |

---

# Queue vs Deque

| Feature | Queue | Deque |
|----------|-------|--------|
| Insert Front | No | Yes |
| Insert Rear | Yes | Yes |
| Remove Front | Yes | Yes |
| Remove Rear | No | Yes |
| FIFO | Yes | Yes |
| LIFO | No | Yes |

Deque is essentially a **superset of Queue**.

---

# Deque vs Stack

| Feature | Stack | Deque |
|----------|--------|--------|
| LIFO | Yes | Yes |
| FIFO | No | Yes |
| Double Ended | No | Yes |
| Recommended | No (Legacy) | Yes |

---

# Common Implementations

| Implementation | Internal Structure |
|----------------|--------------------|
| ArrayDeque | Resizable Circular Array |
| LinkedList | Doubly Linked List |

---

# Real-World Applications

Deque is commonly used in

- Browser Back/Forward history
- Undo/Redo operations
- Sliding Window algorithms
- Palindrome checking
- Queue implementation
- Stack implementation
- Task scheduling

---

# Common Interview Mistakes

❌ Deque only behaves like a Queue.

✔ It can behave as both a Queue and a Stack.

---

❌ Deque is an implementation class.

✔ It is an interface.

---

❌ Only Stack provides push() and pop().

✔ Deque also provides push(), pop(), and peek().

---

❌ Queue and Deque are unrelated.

✔ Deque extends the Queue interface.

---

# Key Takeaways

- Deque stands for **Double Ended Queue**.
- It extends the Queue interface.
- Supports insertion, removal, and retrieval from both ends.
- Can behave as both a FIFO Queue and a LIFO Stack.
- `ArrayDeque` and `LinkedList` are common implementations.
- `ArrayDeque` is generally preferred over the legacy `Stack` class.

---

# Interview Questions

## Basic

1. What is a Deque?
2. What does Deque stand for?
3. Why was the Deque interface introduced?
4. Does Deque extend Queue?
5. Can Deque be used as a Stack?

## Intermediate

6. Explain the difference between addFirst() and offerFirst().
7. Explain the difference between removeLast() and pollLast().
8. Why is Deque preferred over the Stack class?
9. Compare Queue and Deque.
10. Name the common implementations of Deque.
11. What are the time complexities of common Deque operations?
12. Name some real-world applications of Deque.

# ArrayDeque Resizing Mechanism

`ArrayDeque` is backed by a **resizable circular array**.

Like `ArrayList`, when the internal array becomes full, Java allocates a **larger array**, copies the existing elements into it, and continues using the new array.

However, because `ArrayDeque` uses a **circular array**, the copying process is different from `ArrayList`.

---

# Does ArrayDeque Grow Like ArrayList?

Conceptually **Yes**.

Both classes

- Allocate a larger array.
- Copy the existing elements.
- Discard the old array.
- Continue using the new array.

However, the **growth strategy is different**.

### ArrayList

Typically grows by approximately

```
Current Capacity × 1.5
```

Example

```
10

↓

15

↓

22

↓

33
```

---

### ArrayDeque

`ArrayDeque` also grows automatically, but **its exact growth algorithm is an implementation detail of the JDK** and should not be relied upon.

For interviews, remember:

> When the internal array is full, `ArrayDeque` allocates a larger array and copies the elements.

---

# ArrayList Resizing

Initial Capacity

```
10
```

```
[A][B][C][D][E][F][G][H][I][J]
```

Adding one more element

```java
list.add("K");
```

Internal Steps

### Step 1

Allocate a larger array.

```
10

↓

15
```

### Step 2

Copy all elements.

```
Old Array

A B C D E F G H I J

↓

New Array

A B C D E F G H I J
```

### Step 3

Discard the old array.

---

# ArrayDeque Resizing

Suppose the circular array has capacity **8**.

```
Index

0 1 2 3 4 5 6 7
```

After several insertions and removals

```
[E][F][ ][ ][A][B][C][D]
```

Notice

The **physical layout**

```
E F _ _ A B C D
```

The **logical order**

```
A → B → C → D → E → F
```

These are different because the array wraps around.

---

# Why Can't Java Simply Copy the Array?

If Java copied the physical memory directly

```
[E][F][ ][ ][A][B][C][D]
```

the logical order would still be split.

Instead, Java copies the elements **in logical queue order**.

Result

```
[A][B][C][D][E][F][ ][ ][ ][ ]
```

After resizing

```
head = 0

tail = size
```

The wrapped structure disappears.

---

# Internal Resizing Steps

When the deque becomes full

### Step 1

Allocate a larger array.

### Step 2

Traverse the deque from **head** to **tail**.

### Step 3

Copy the elements into the new array in logical order.

```
Old

[E][F][ ][ ][A][B][C][D]

↓

New

[A][B][C][D][E][F]
```

### Step 4

Reset

```
head = 0

tail = size
```

### Step 5

Discard the old array.

---

# Time Complexity

Normal insertion

```
O(1)
```

Insertion during resizing

```
O(n)
```

Therefore

```
Insertion = O(1) amortized
```

---

# ArrayList vs ArrayDeque Resizing

| Feature | ArrayList | ArrayDeque |
|----------|-----------|------------|
| Internal Structure | Dynamic Array | Circular Dynamic Array |
| Resizes Automatically | Yes | Yes |
| Copies Elements | Yes | Yes |
| Copy Order | Physical Order | Logical Queue Order |
| Resets Head/Tail | No | Yes |
| Average Insert Complexity | O(1) Amortized | O(1) Amortized |

---

# Visual Summary

### Before Resize

```
Capacity = 8

Index

0 1 2 3 4 5 6 7

[E][F][ ][ ][A][B][C][D]
```

Logical order

```
A → B → C → D → E → F
```

---

### After Resize

```
Capacity = Larger

[A][B][C][D][E][F][ ][ ][ ][ ]
```

```
head = 0

tail = size
```

---

# Key Takeaways

- `ArrayDeque` uses a **resizable circular array**.
- When full, it allocates a larger array.
- Unlike `ArrayList`, it copies elements in **logical queue order**, not physical memory order.
- After copying, `head` is reset to `0` and `tail` points after the last element.
- Resizing takes **O(n)** time.
- Most insertions remain **O(1) amortized**.

---

# Interview Questions

1. Does `ArrayDeque` resize automatically?
2. Is the resizing mechanism similar to `ArrayList`?
3. Why can't `ArrayDeque` simply copy the underlying array?
4. What is the difference between physical order and logical order in a circular array?
5. What happens to the `head` and `tail` after resizing?
6. Why is insertion still considered **O(1) amortized**?

# Day 5 — Chapter 16: Hashing Fundamentals

## Why Was Hashing Introduced?

Suppose employee records are stored in an array.

```
John
Alice
Bob
David
Emma
```

Searching for "Bob" requires checking each element.

```
John ❌
Alice ❌
Bob ✅
```

Time Complexity

```
O(n)
```

As the number of elements grows, searching becomes slow.

Hashing was introduced to provide **fast lookup**.

---

# What is Hashing?

**Definition**

> Hashing is the technique of converting a key into a fixed-size integer (hash value) so that data can be stored and retrieved efficiently.

Flow

```
Key

↓

Hash Function

↓

Hash Value

↓

Bucket

↓

Object
```

---

# Real-World Analogy

Apartment Mailboxes

```
Apartment Number

↓

Mailbox Number

↓

Letter
```

Instead of searching every mailbox, you directly go to the correct mailbox.

Hashing works in the same way.

---

# Components of Hashing

```
Key

↓

Hash Function

↓

Hash Value

↓

Bucket
```

---

# 1. Key

The object used to identify data.

Example

```
101
102
103
```

---

# 2. Hash Function

Converts the key into an integer.

```
101

↓

5829134
```

The same key always produces the same hash value (as long as the object doesn't change in a way that affects hashing).

---

# 3. Hash Value

The integer returned by the hash function.

Example

```
15423879
```

This value is **not** the storage location.

It is used to determine the bucket.

---

# 4. Bucket

A logical storage location inside the hash table.

Example

```
Bucket 0

Bucket 1

Bucket 2

...

Bucket 15
```

---

# Why Can't Java Use the Hash Value Directly?

Suppose

```
hashCode()

↓

1456789345
```

Creating billions of buckets would waste memory.

Instead, Java converts the hash value into a valid bucket index.

Conceptually

```
Bucket Index

=

Hash Value % Number of Buckets
```

Example

```
1456789345 % 16

=

1
```

The object is stored in **Bucket 1**.

> **Note:** Modern `HashMap` uses a bitwise operation (`(n - 1) & hash`) instead of `%`, because its capacity is always a power of two. We'll study this in the `HashMap` chapter.

---

# Complete Flow

```
Key

↓

hashCode()

↓

Hash Value

↓

Bucket Index

↓

Bucket

↓

Store Object
```

Searching follows the same path.

---

# Why Is Hashing Fast?

Without hashing

```
A

↓

B

↓

C

↓

D
```

Search Complexity

```
O(n)
```

With hashing

```
Key

↓

Bucket

↓

Object
```

Average Search Complexity

```
O(1)
```

---

# Collision

Sometimes different keys map to the same bucket.

Example

```
Key A

↓

Bucket 4
```

```
Key B

↓

Bucket 4
```

This is called a **collision**.

Example

```
Bucket 4

↓

John

Alice
```

Collisions are normal.

---

# Does hashCode() Need to Be Unique?

No.

Different objects can produce

- The same hash value.
- The same bucket.

This is expected.

---

# Where Does hashCode() Fit?

Every Java object inherits

```java
hashCode()
```

Its purpose is to generate the hash value used during hashing.

---

# Where Does equals() Fit?

Hashing first finds the correct bucket.

If multiple objects exist inside the bucket, Java uses

```java
equals()
```

to identify the correct key.

Flow

```
Key

↓

hashCode()

↓

Bucket

↓

equals()

↓

Correct Entry
```

---

# hashCode() vs equals()

| hashCode() | equals() |
|-------------|----------|
| Finds the bucket | Finds the correct object inside the bucket |
| Returns an integer | Returns true or false |
| Fast lookup | Exact comparison |

Think of them as working together.

- `hashCode()` answers **"Which bucket?"**
- `equals()` answers **"Which object?"**

---

# Common Interview Mistakes

❌ Hashing guarantees no collisions.

✔ Collisions are expected.

---

❌ Hash value is the bucket number.

✔ The hash value is converted into a bucket index.

---

❌ hashCode() uniquely identifies every object.

✔ Different objects may have the same hash code.

---

❌ equals() is called before hashCode().

✔ Java first computes the hash, then uses equals() only when required.

---

# Key Takeaways

- Hashing provides fast lookup.
- A hash function converts a key into a hash value.
- Hash values are mapped to buckets.
- Buckets store the data.
- Collisions are normal.
- hashCode() generates the hash value.
- equals() identifies the correct object inside a bucket.
- Average lookup complexity is **O(1)**.

---

# Interview Questions

1. What is hashing?
2. Why was hashing introduced?
3. What is a hash function?
4. What is a bucket?
5. Why is hashing faster than linear search?
6. What is a collision?
7. Does hashCode() guarantee uniqueness?
8. Why are hashCode() and equals() both needed?
9. Explain the complete hashing flow.
10. What is the average time complexity of hashing?
```

# Day 5 — Chapter 17: `hashCode()` and `equals()`

## Why Do We Need `hashCode()` and `equals()`?

Suppose we have two `Student` objects.

```java
Student s1 = new Student(101, "Guru");
Student s2 = new Student(101, "Guru");
```

Memory

```
        Heap

+-------------------+
| id = 101          |
| name = Guru       |
+-------------------+
        ^
        |
       s1


+-------------------+
| id = 101          |
| name = Guru       |
+-------------------+
        ^
        |
       s2
```

Although both objects contain the same data, they are **different objects** stored at different memory locations.

---

# The `==` Operator

The `==` operator compares **references**, not object contents.

```java
System.out.println(s1 == s2);
```

Output

```
false
```

Explanation

```
s1 -------> Object A

s2 -------> Object B
```

Different references mean the result is `false`.

---

# Default `equals()`

```java
System.out.println(s1.equals(s2));
```

Output

```
false
```

Why?

Because `Student` inherits `equals()` from the `Object` class.

Conceptually, the default implementation is

```java
public boolean equals(Object obj) {
    return this == obj;
}
```

Therefore

- `==` compares references.
- Default `equals()` also compares references.

---

# Why Override `equals()`?

Suppose the business rule is

> Two students are equal if their **ID** is the same.

The default implementation cannot know this rule.

We must override `equals()`.

---

# Overriding `equals()`

```java
import java.util.Objects;

class Student {

    private int id;
    private String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Student other = (Student) obj;

        return this.id == other.id;
    }
}
```

Now

```java
Student s1 = new Student(101, "Guru");
Student s2 = new Student(101, "Alice");

System.out.println(s1.equals(s2));
```

Output

```
true
```

Because

```
101 == 101
```

---

# Understanding `this`

When we execute

```java
s1.equals(s2);
```

Internally

```
this

↓

s1
```

```
obj

↓

s2
```

The statement

```java
return this.id == other.id;
```

becomes

```java
101 == 101
```

Result

```
true
```

---

# What is `hashCode()`?

Every Java object inherits

```java
hashCode()
```

from the `Object` class.

Its purpose is to generate an integer called the **hash code**.

Example

```java
System.out.println(s1.hashCode());
```

Output

```
1867083167
```

The exact number is not important.

It is used internally by hash-based collections.

---

# Default `hashCode()`

The default implementation generates a hash based on the object's identity.

Therefore

```java
Student s1 = new Student(101, "Guru");
Student s2 = new Student(101, "Guru");
```

Usually

```java
s1.hashCode() != s2.hashCode();
```

because they are different objects.

---

# Why Override `hashCode()`?

Suppose

```java
s1.equals(s2)
```

returns

```
true
```

If

```java
s1.hashCode()

!=

s2.hashCode()
```

then `HashMap` may place them into different buckets.

As a result, Java may never compare them using `equals()`.

Therefore Java requires

> **Equal objects must produce the same hash code.**

---

# Overriding `hashCode()`

```java
@Override
public int hashCode() {
    return Integer.hashCode(id);
}
```

or

```java
@Override
public int hashCode() {
    return Objects.hash(id);
}
```

Now

```java
Student s1 = new Student(101, "Guru");
Student s2 = new Student(101, "Alice");
```

Both produce the same hash code because they have the same `id`.

---

# The `equals()` and `hashCode()` Contract

## Rule 1

If

```java
obj1.equals(obj2)
```

returns

```
true
```

then

```java
obj1.hashCode() == obj2.hashCode()
```

must also be true.

---

## Rule 2

If two objects have different hash codes, they cannot be equal.

---

## Rule 3

Two different objects **may** have the same hash code.

This is called a **collision**.

Example

```
Student A

↓

hashCode = 100
```

```
Student B

↓

hashCode = 100
```

The objects are different but happen to share the same hash code.

---

# How HashMap Uses `hashCode()` and `equals()`

Suppose we execute

```java
map.get(student);
```

HashMap performs the following steps.

### Step 1

Compute

```java
student.hashCode();
```

↓

```
1432
```

---

### Step 2

Convert the hash code into a bucket index.

↓

```
Bucket 8
```

---

### Step 3

Search only inside Bucket 8.

---

### Step 4

Compare keys using

```java
equals()
```

Internally

```java
storedKey.equals(requestedKey);
```

---

### Step 5

If `equals()` returns `true`, the value is returned.

---

# Complete Flow

```
Key

↓

hashCode()

↓

Hash Value

↓

Bucket Index

↓

Bucket

↓

equals()

↓

Correct Entry

↓

Return Value
```

---

# Why Both Methods Are Needed

Think of them as performing different jobs.

| Method | Responsibility |
|----------|---------------|
| `hashCode()` | Determines the bucket where the object should be searched. |
| `equals()` | Determines whether two objects represent the same logical object. |

Remember

> **`hashCode()` finds the bucket.**

> **`equals()` finds the correct object inside the bucket.**

---

# Common Mistake

Overriding only `equals()`.

```java
@Override
public boolean equals(Object obj) {
    ...
}
```

Without overriding `hashCode()`.

This breaks the behavior of

- HashMap
- HashSet
- LinkedHashMap
- LinkedHashSet

Always override both methods together.

---

# Best Practice

Generate both methods using your IDE.

IntelliJ IDEA

```
Right Click

↓

Generate

↓

equals() and hashCode()
```

This ensures the contract is maintained.

---

# Summary

| `==` | `equals()` |
|------|------------|
| Compares references | Compares logical equality (if overridden) |

---

| `hashCode()` | `equals()` |
|---------------|-----------|
| Determines the bucket | Determines the exact object inside the bucket |

---

# Key Takeaways

- `==` compares references.
- Default `equals()` compares references.
- Override `equals()` to define logical equality.
- Override `hashCode()` whenever `equals()` is overridden.
- Equal objects must have equal hash codes.
- `hashCode()` and `equals()` work together inside hash-based collections.
- `hashCode()` narrows the search to a bucket.
- `equals()` identifies the correct object inside that bucket.

---

# Interview Questions

## Basic

1. What is the difference between `==` and `equals()`?
2. What does the default `equals()` method do?
3. Why do we override `equals()`?
4. What is `hashCode()`?
5. Why do we override `hashCode()`?

## Intermediate

6. Explain the `equals()` and `hashCode()` contract.
7. Can two objects have the same hash code?
8. Can two equal objects have different hash codes?
9. Why does `HashMap` use both `hashCode()` and `equals()`?
10. What happens if only `equals()` is overridden?
11. Explain the complete lookup flow inside a `HashMap`.

# Day 5 — Chapter 18: HashMap (Part 1 — Architecture & Internal Working)

## Why Was HashMap Introduced?

Suppose we want to store employee records.

```
101 → Guru
102 → Alice
103 → Bob
```

The requirements are:

- Fast insertion
- Fast searching
- Fast deletion

Using a `List`

```java
List<Employee> employees = new ArrayList<>();
```

Searching requires checking each employee one by one.

```
101?

↓

Employee 1

↓

Employee 2

↓

Employee 3
```

Time Complexity

```
O(n)
```

As the number of records grows, searching becomes slow.

Java introduced **HashMap** to provide **constant-time average lookup**.

---

# What is HashMap?

**Definition**

> `HashMap` is a hash table–based implementation of the `Map` interface that stores data as key-value pairs and provides fast insertion, searching, and deletion.

Hierarchy

```
Object
   │
AbstractMap
   │
HashMap
```

Implements

```
Map
```

---

# Characteristics

- Stores key-value pairs.
- Keys must be unique.
- Values may be duplicated.
- Allows one `null` key.
- Allows multiple `null` values.
- Not synchronized.
- Does not maintain insertion order.
- Average lookup complexity is **O(1)**.

---

# Why is HashMap Fast?

Instead of searching every element,

HashMap performs

```
Key

↓

hashCode()

↓

Bucket

↓

Entry
```

Only one bucket is searched.

This makes lookup much faster than linear searching.

---

# What is a Hash Table?

A **HashMap** is internally backed by a **Hash Table**.

A hash table is simply an **array**.

Example

```
Index

0
1
2
3
4
5
6
7
```

Each index in the array is called a **bucket**.

Initially

```
Bucket 0 → null

Bucket 1 → null

Bucket 2 → null

Bucket 3 → null

Bucket 4 → null

Bucket 5 → null

Bucket 6 → null

Bucket 7 → null
```

No entries have been inserted yet.

---

# What is a Bucket?

A **bucket** is one slot in the internal array.

Suppose the array has a capacity of **8**.

```
Index

0
1
2
3
4
5
6
7
```

These correspond to

```
Bucket 0

Bucket 1

Bucket 2

...

Bucket 7
```

When a key is inserted, Java determines which bucket should store it.

---

# Internal Architecture

Conceptually

```
                 HashMap
                     │
                     ▼
             Hash Table (Array)
                     │
      ┌────┬────┬────┬────┬────┬────┬────┬────┐
      │ B0 │ B1 │ B2 │ B3 │ B4 │ B5 │ B6 │ B7 │
      └────┴────┴────┴────┴────┴────┴────┴────┘
```

Initially every bucket is empty.

---

# What Does a Bucket Store?

A bucket does **not** store only the value.

It stores an **Entry (Node)**.

Each entry contains

```
Hash

↓

Key

↓

Value

↓

Next
```

Conceptually

```
+----------------------+
| hash                 |
| key                  |
| value                |
| next                 |
+----------------------+
```

- **hash** → Cached hash value
- **key** → Map key
- **value** → Associated value
- **next** → Points to the next entry in case of collisions

---

# High-Level Insertion Flow

Suppose

```java
map.put(101, "Guru");
```

The overall process is

```
Key

↓

hashCode()

↓

Hash Value

↓

Bucket Index

↓

Bucket

↓

Store Entry
```

HashMap directly calculates the bucket instead of searching all entries.

---

# Example

```java
map.put(101, "Guru");
```

Suppose

```
hashCode(101)

↓

3056
```

Conceptually

```
Bucket Index

=

3056 % 8

=

0
```

The entry is stored in

```
Bucket 0

↓

(101, Guru)
```

> **Note:** Modern `HashMap` does **not** use the `%` operator. It uses a faster bitwise calculation (`(n - 1) & hash`) because the capacity is always a power of two. We'll study the exact implementation in the next chapter.

---

# Why Buckets Improve Performance

Without buckets

```
John

↓

Alice

↓

Bob

↓

David

↓

Emma
```

Search Complexity

```
O(n)
```

With buckets

```
Key

↓

Bucket

↓

Entry
```

Average Search Complexity

```
O(1)
```

This is the biggest advantage of HashMap.

---

# Example Internal Structure

After inserting several entries

```
Bucket 0 → (101, Guru)

Bucket 1 → null

Bucket 2 → (105, Alice)

Bucket 3 → null

Bucket 4 → (120, Bob)

Bucket 5 → null

Bucket 6 → null

Bucket 7 → (200, David)
```

Searching for key **120**

```
120

↓

hashCode()

↓

Bucket 4

↓

Found
```

Only one bucket is searched.

---

# Common Interview Mistakes

❌ HashMap stores data in a linked list.

✔ HashMap is backed by an **array (hash table)**.

Linked lists are used **only when multiple entries map to the same bucket**.

---

❌ Buckets store only values.

✔ Buckets store complete **Entry (Node)** objects.

---

❌ HashMap searches every bucket.

✔ HashMap directly computes the bucket using the key's hash code.

---

# Key Takeaways

- HashMap is an implementation of the `Map` interface.
- It is backed by a **hash table (array)**.
- Each array index is called a **bucket**.
- Each bucket stores **Entry (Node)** objects.
- An entry contains:
    - hash
    - key
    - value
    - next
- HashMap uses `hashCode()` to determine the bucket.
- Average insertion, lookup, and deletion are **O(1)**.

---

# Interview Questions

## Basic

1. What is a HashMap?
2. Why was HashMap introduced?
3. What data structure does HashMap use internally?
4. What is a bucket?
5. What is stored inside a bucket?

## Intermediate

6. Explain the internal architecture of HashMap.
7. Why is HashMap generally faster than ArrayList for searching?
8. Why doesn't HashMap search every bucket?
9. Explain the complete high-level insertion flow.
10. Why is the average time complexity of HashMap **O(1)**?

# Day 5 — Chapter 19: HashMap `put()` Internal Working (Part 2)

## What Happens When We Call `put()`?

Suppose we write

```java
Map<Integer, String> map = new HashMap<>();

map.put(101, "Guru");
```

Although it looks like a single method call, HashMap performs several internal steps.

```
put(key, value)

↓

Compute hash

↓

Calculate bucket index

↓

Locate bucket

↓

Bucket Empty?
     │
 ┌───┴────┐
 │        │
Yes      No
 │         │
Store    Compare Keys
           │
      Same Key?
       │      │
      Yes    No
       │       │
   Update   Collision
```

---

# Step 1 — Compute the Hash

HashMap first computes the hash code of the key.

```java
Integer key = 101;

System.out.println(key.hashCode());
```

Output

```
101
```

For the `Integer` class, the hash code is simply the integer value itself.

For custom objects, the result depends on the overridden `hashCode()` method.

---

# Step 2 — Hash Spreading

HashMap does **not** directly use the value returned by `hashCode()`.

Instead, Java performs an additional operation called **hash spreading**.

Conceptually

```java
int hash = hashCode ^ (hashCode >>> 16);
```

Example

```
hashCode

↓

101

↓

Hash Spreading

↓

Improved Hash
```

### Why?

If many hash codes differ only in their higher-order bits, they may all end up in the same bucket.

Hash spreading mixes the higher bits into the lower bits.

Benefits

- Better bucket distribution.
- Fewer collisions.
- Improved performance.

> **Note:** You do not need to memorize the bitwise expression. Just remember that Java performs hash spreading before calculating the bucket index.

---

# Step 3 — Calculate the Bucket Index

Suppose the current table capacity is

```
16
```

Conceptually

```
Bucket Index

=

Hash % Capacity
```

Example

```
101 % 16

=

5
```

So the entry would go into

```
Bucket 5
```

---

# Actual HashMap Calculation

The JDK uses

```java
(n - 1) & hash
```

instead of `%`.

Where

- **n** = current table capacity.

Example

```
Capacity = 16

↓

15 & hash

↓

Bucket Index
```

### Why use `&` instead of `%`?

Because bitwise operations are much faster than division.

This optimization works because HashMap capacities are always powers of two.

```
16
32
64
128
256
...
```

---

# Step 4 — Locate the Bucket

Suppose the calculated bucket index is

```
Bucket 5
```

HashMap checks

```
Bucket 5

↓

null ?
```

---

# Case 1 — Bucket Is Empty

If

```
Bucket 5

↓

null
```

HashMap creates a new node.

```
Bucket 5

↓

(101, Guru)
```

Insertion is complete.

Time Complexity

```
O(1)
```

---

# What Is Stored Inside a Bucket?

A bucket stores a **Node (Entry)**.

Conceptually

```java
class Node<K, V> {

    int hash;

    K key;

    V value;

    Node<K, V> next;
}
```

Example

```
hash = 101

key = 101

value = Guru

next = null
```

---

# Internal Representation

Before insertion

```
Bucket 0 → null

Bucket 1 → null

Bucket 2 → null

Bucket 3 → null

Bucket 4 → null

Bucket 5 → null

Bucket 6 → null

Bucket 7 → null
```

After

```java
map.put(101, "Guru");
```

```
Bucket 5

↓

+----------------------+
| hash = 101           |
| key = 101            |
| value = Guru         |
| next = null          |
+----------------------+
```

---

# Case 2 — Bucket Already Contains an Entry

Suppose

```
Bucket 5

↓

101 → Guru
```

Now execute

```java
map.put(101, "Updated Guru");
```

HashMap compares

```
Existing Key

↓

101
```

with

```
New Key

↓

101
```

using

```java
equals()
```

Since both keys are equal,

HashMap updates the value.

```
Guru

↓

Updated Guru
```

No new node is created.

---

# Case 3 — Different Key, Same Bucket

Suppose

```
Bucket 5

↓

101 → Guru
```

Now insert

```
205 → Alice
```

If both keys map to the same bucket,

HashMap cannot replace the existing entry because the keys are different.

This situation is called a **collision**.

Collision handling will be covered in the next chapter.

---

# Complete `put()` Flow

```
put()

↓

hashCode()

↓

Hash Spreading

↓

Bucket Index

↓

Locate Bucket

↓

Bucket Empty?

        │
    ┌───┴────┐
    │        │
   Yes      No
    │        │
Create     Compare Keys
 Node         │
              │
        Keys Equal?
         │      │
        Yes    No
         │       │
     Update   Collision
```

---

# Why Is `put()` Fast?

HashMap does **not** search the entire table.

It directly computes the bucket.

```
Key

↓

Hash

↓

Bucket

↓

Store
```

Average Time Complexity

```
O(1)
```

Only collisions may increase the work.

---

# Common Interview Mistakes

❌ `put()` always creates a new node.

✔ If the key already exists, HashMap updates the value.

---

❌ HashMap uses `%` internally.

✔ Modern HashMap uses `(n - 1) & hash`.

---

❌ `hashCode()` is directly used.

✔ Java first performs hash spreading.

---

❌ Every bucket stores only one entry.

✔ A bucket may store multiple entries when collisions occur.

---

# Key Takeaways

- `put()` begins by calling `hashCode()`.
- Java performs hash spreading to improve bucket distribution.
- The bucket index is calculated using `(n - 1) & hash`.
- If the bucket is empty, a new node is created.
- If the same key already exists, the value is updated.
- If a different key maps to the same bucket, a collision occurs.
- Average insertion complexity is **O(1)**.

---

# Interview Questions

## Basic

1. What happens when `HashMap.put()` is called?
2. Why is `hashCode()` computed?
3. What is hash spreading?
4. Why does HashMap use `(n - 1) & hash`?
5. What happens when the bucket is empty?

## Intermediate

6. What is stored inside a bucket?
7. What happens if the same key is inserted twice?
8. What causes a collision?
9. Why is `put()` usually `O(1)`?
10. Explain the complete `put()` algorithm.

# Day 5 — Chapter 20: HashMap Collision Handling & Treeification (Java 8)

## What is a Collision?

A **collision** occurs when **two different keys produce the same bucket index**.

> **Collision = Different Keys + Same Bucket**

Example

```
Key 101

↓

Bucket 5
```

```
Key 205

↓

Bucket 5
```

Both keys map to the same bucket.

This is called a **collision**.

---

# Why Do Collisions Occur?

A HashMap contains a **fixed number of buckets**.

Example

```
Capacity = 16

Buckets

0
1
2
...
15
```

If we store hundreds or thousands of keys, multiple keys are guaranteed to share buckets.

This follows the **Pigeonhole Principle**.

> If there are more objects than containers, at least one container must contain more than one object.

Therefore,

> **Collisions are unavoidable.**

---

# How HashMap Handles Collisions

HashMap does **not** reject the second key.

Instead, it stores **multiple entries inside the same bucket**.

Before Java 8 (and initially in Java 8 as well), this is done using a **Linked List**.

Example

```
Bucket 5

↓

101 → Guru

↓

205 → Alice

↓

309 → Bob
```

Each entry points to the next entry.

This technique is called **Separate Chaining**.

---

# The Internal Node Structure

Each bucket stores **Node** objects.

Conceptually

```java
class Node<K, V> {

    int hash;

    K key;

    V value;

    Node<K, V> next;
}
```

Each node stores

- **hash** → Cached hash value
- **key** → Map key
- **value** → Associated value
- **next** → Reference to the next node in the same bucket

Example

```
+----------------------------+
| hash  = 101                |
| key   = 101                |
| value = Guru               |
| next  = null               |
+----------------------------+
```

---

# Collision Example

Insert

```java
map.put(101, "Guru");
```

```
Bucket 5

↓

+----------------------------+
| hash = 101                 |
| key = 101                  |
| value = Guru               |
| next = null                |
+----------------------------+
```

Insert another key that maps to the same bucket

```java
map.put(205, "Alice");
```

```
Bucket 5

↓

+----------------------------+
| hash = 101                 |
| key = 101                  |
| value = Guru               |
| next ---------------------+|
+---------------------------||
                            ||
                            \/
                    +----------------------------+
                    | hash = 205                 |
                    | key = 205                  |
                    | value = Alice             |
                    | next = null               |
                    +----------------------------+
```

Another collision

```java
map.put(309, "Bob");
```

```
Bucket 5

↓

101 → Guru

↓

205 → Alice

↓

309 → Bob

↓

null
```

---

# Why Does Node Have a `next` Reference?

The `next` field exists so that multiple nodes can be stored inside the same bucket.

Without collisions

```
Node

↓

next = null
```

With collisions

```
Node

↓

Node

↓

Node

↓

null
```

---

# Searching During a Collision

Suppose the bucket contains

```
101 → Guru

↓

205 → Alice

↓

309 → Bob
```

Search

```java
map.get(205);
```

HashMap performs

```
205

↓

hashCode()

↓

Bucket 5

↓

Compare with 101

↓

Not Equal

↓

Move to next node

↓

Compare with 205

↓

Equal

↓

Return "Alice"
```

Notice

HashMap searches **only inside the calculated bucket**, not the entire table.

---

# Time Complexity

## Without Collision

```
Key

↓

Bucket

↓

Found
```

Complexity

```
O(1)
```

---

## With Collision

```
Bucket

↓

Node

↓

Node

↓

Node
```

Complexity

```
O(n)
```

Where **n** is the number of nodes inside that bucket.

---

# Why Too Many Collisions Are Bad

Suppose every key maps to the same bucket.

```
Bucket 0

↓

A

↓

B

↓

C

↓

D

↓

E
```

Searching now becomes

```
O(n)
```

The HashMap behaves like a Linked List.

---

# Java 8 Improvement — Treeification

Java 8 introduced a major optimization.

If a bucket becomes too large, HashMap converts the Linked List into a **Red-Black Tree**.

Searching changes from

```
O(n)
```

to

```
O(log n)
```

---

# When Does Treeification Happen?

Treeification occurs **during `put()`** after a new node is inserted.

The decision process is

```
Insert New Node

↓

Count Nodes in Bucket

↓

Nodes >= 8 ?

        │
   No   │   Yes
        ▼
Capacity >= 64 ?

        │
   No   │   Yes
        ▼
Resize Table

        OR

Convert Linked List
      into
Red-Black Tree
```

---

# Conditions for Treeification

Java converts a Linked List into a Red-Black Tree only when **both** conditions are satisfied.

### Condition 1

The bucket contains **at least 8 nodes**.

This constant is

```java
TREEIFY_THRESHOLD = 8
```

---

### Condition 2

The HashMap capacity is **64 or greater**.

This constant is

```java
MIN_TREEIFY_CAPACITY = 64
```

If the capacity is less than 64,

HashMap performs **resize** instead of treeification.

---

# Why Doesn't Java Treeify Immediately?

Suppose

```
Capacity = 16
```

One bucket contains

```
101

↓

205

↓

309

↓

401

↓

550

↓

612

↓

720

↓

888
```

Although there are 8 nodes,

Java first increases the table size.

```
16

↓

32

↓

64
```

After resizing, entries are redistributed into different buckets.

Example

Before Resize

```
Bucket 5

↓

101

↓

205

↓

309

↓

401

↓

550

↓

612

↓

720

↓

888
```

After Resize

```
Bucket 5

↓

101

↓

550
```

```
Bucket 21

↓

205

↓

720
```

```
Bucket 37

↓

309
```

```
Bucket 53

↓

401

↓

612

↓

888
```

Now no bucket contains 8 nodes.

Treeification is unnecessary.

---

# What Happens During Treeification?

Initially, the bucket points to the head of a Linked List.

```
Bucket 5

↓

Node

↓

Node

↓

Node
```

After treeification,

the bucket points to the **root of a Red-Black Tree**.

```
Bucket 5

↓

              (401)
             /     \
         (205)    (612)
         /   \    /   \
     (101) (309)(550)(720)
                          \
                          (888)
```

---

# Does the Data Change?

No.

Each entry still stores

```
hash

key

value
```

The difference is **how the entries are connected**.

### Before Treeification

```
Node

↓

next

↓

Node

↓

next

↓

Node
```

---

### After Treeification

Each node becomes a **TreeNode**.

Conceptually

```java
class TreeNode<K, V> extends Node<K, V> {

    TreeNode<K, V> parent;

    TreeNode<K, V> left;

    TreeNode<K, V> right;

    TreeNode<K, V> prev;

    boolean red;
}
```

Each `TreeNode` still contains

- hash
- key
- value

But it also contains

- parent
- left
- right
- previous
- node color

These additional references maintain the Red-Black Tree.

---

# Important Constants

| Constant | Value | Purpose |
|----------|------:|---------|
| `TREEIFY_THRESHOLD` | 8 | Convert a Linked List into a Red-Black Tree. |
| `UNTREEIFY_THRESHOLD` | 6 | Convert a Red-Black Tree back into a Linked List after removals. |
| `MIN_TREEIFY_CAPACITY` | 64 | Minimum HashMap capacity required before treeification is allowed. |

---

# Complete Collision Flow

```
put()

↓

Compute Hash

↓

Calculate Bucket

↓

Bucket Empty?

        │
   ┌────┴────┐
   │         │
  Yes       No
   │         │
Insert   Compare Keys
            │
     Keys Equal?

        │
   ┌────┴────┐
   │         │
  Yes       No
   │         │
Update   Add New Node
              │
      Bucket Size >= 8 ?

              │
        Capacity >= 64 ?

        │            │
      No            Yes
       │             │
   Resize      Treeify Bucket
```

---

# Common Interview Mistakes

❌ Collisions indicate a broken `hashCode()`.

✔ Collisions are normal and unavoidable.

---

❌ HashMap immediately converts collisions into a Red-Black Tree.

✔ It first uses a Linked List.

---

❌ Eight nodes always cause treeification.

✔ Treeification requires:

- Bucket size ≥ 8
- Capacity ≥ 64

Otherwise, HashMap resizes.

---

❌ Treeification changes the stored data.

✔ Only the structure changes.

The same entries (`hash`, `key`, `value`) are reorganized into a Red-Black Tree.

---

# Key Takeaways

- A collision occurs when different keys map to the same bucket.
- HashMap resolves collisions using **Separate Chaining**.
- Each bucket stores `Node` objects.
- Each `Node` contains:
    - hash
    - key
    - value
    - next
- Initially, collisions form a Linked List.
- Java 8 converts long Linked Lists into Red-Black Trees.
- Treeification occurs only when:
    - Bucket size ≥ 8
    - HashMap capacity ≥ 64
- If capacity is less than 64, HashMap resizes instead.
- Treeification improves lookup from **O(n)** to **O(log n)** for heavily populated buckets.

---

# Interview Questions

## Basic

1. What is a collision?
2. Why are collisions unavoidable?
3. How does HashMap handle collisions?
4. What is Separate Chaining?
5. What fields does a `HashMap.Node` contain?

## Intermediate

6. Why does `Node` contain a `next` reference?
7. Explain how `HashMap.get()` works after a collision.
8. When does Java convert a Linked List into a Red-Black Tree?
9. Why doesn't Java treeify immediately?
10. What are `TREEIFY_THRESHOLD`, `UNTREEIFY_THRESHOLD`, and `MIN_TREEIFY_CAPACITY`?
11. What changes internally when a bucket is treeified?
12. How does treeification improve performance?

# Day 5 — Chapter 21: HashMap `get()` Internal Working

## Why Study `get()`?

When we write

```java
String name = map.get(205);
```

HashMap performs several internal operations before returning the value.

Internally it must answer:

- Which bucket should I search?
- What if multiple entries exist in the bucket?
- How do I identify the correct key?
- What happens if the bucket has become a Red-Black Tree?

---

# High-Level Flow

```
get(key)

↓

Compute hash

↓

Hash Spreading

↓

Calculate Bucket Index

↓

Locate Bucket

↓

Bucket Empty?

      │
 ┌────┴────┐
 │         │
Yes       No
 │          │
Return     Compare Keys
null          │
              │
        Match Found?
         │       │
        Yes     No
         │        │
 Return Value  Next Node / Tree Search
```

---

# Example Setup

```java
Map<Integer, String> map = new HashMap<>();

map.put(101, "Guru");
map.put(205, "Alice");
map.put(309, "Bob");
```

Assume all three keys map to the same bucket.

```
Bucket 5

↓

101 → Guru

↓

205 → Alice

↓

309 → Bob
```

---

# Step 1 — Compute the Hash

```java
map.get(205);
```

HashMap computes

```java
205.hashCode();
```

For an `Integer`

```
hash = 205
```

---

# Step 2 — Hash Spreading

Java performs hash spreading.

Conceptually

```java
hash ^ (hash >>> 16)
```

Purpose

- Better distribution
- Fewer collisions
- Improved performance

---

# Step 3 — Calculate the Bucket Index

Suppose the table capacity is

```
16
```

HashMap calculates

```java
(n - 1) & hash
```

Suppose the result is

```
Bucket 5
```

HashMap directly jumps to Bucket 5.

It never searches every bucket.

---

# Step 4 — Check Whether the Bucket Is Empty

Suppose

```
Bucket 5

↓

null
```

Then

```java
map.get(205);
```

returns

```java
null
```

Time Complexity

```
O(1)
```

---

# Step 5 — Compare Keys

Suppose Bucket 5 contains

```
101 → Guru

↓

205 → Alice

↓

309 → Bob
```

HashMap compares

```
101.equals(205)
```

Result

```
false
```

Move to next node.

Now compare

```
205.equals(205)
```

Result

```
true
```

Return

```
Alice
```

---

# Why Doesn't HashMap Compare Only Hash Codes?

Suppose

```
Student A

hashCode()

↓

10
```

```
Student B

hashCode()

↓

10
```

Different objects can have the same hash code.

Therefore HashMap must also call

```java
equals()
```

Only if

```java
equals() == true
```

does HashMap return the value.

---

# Searching Inside a Linked List

```
Bucket 5

↓

101

↓

205

↓

309

↓

401
```

Searching for

```
401
```

HashMap compares

```
101

↓

205

↓

309

↓

401
```

Found.

Time Complexity

```
O(n)
```

where **n** is the number of nodes inside that bucket.

---

# Searching Inside a Red-Black Tree

If the bucket has been treeified

```
          309
        /     \
      205     550
     /   \    /  \
   101  250 401 700
```

Searching for

```
401
```

Traversal

```
309

↓

550

↓

401
```

Time Complexity

```
O(log n)
```

---

# Simplified Internal Algorithm

```java
public V get(Object key) {

    int hash = spreadHash(key.hashCode());

    int bucketIndex = calculateBucket(hash);

    Node node = table[bucketIndex];

    while (node != null) {

        if (node.hash == hash &&
            node.key.equals(key)) {

            return node.value;
        }

        node = node.next;
    }

    return null;
}
```

> This is a simplified version for learning. The actual JDK implementation contains additional optimizations and special handling for treeified buckets.

---

# Complete `get()` Flow

```
get(key)

↓

hashCode()

↓

Hash Spreading

↓

Bucket Index

↓

Locate Bucket

↓

Bucket Empty?

        │
    ┌───┴────┐
    │        │
   Yes      No
    │        │
Return     Compare Hash
 null          │
               ▼
        Compare equals()
               │
       ┌───────┴────────┐
       │                │
    Match           No Match
       │                │
Return Value     Next Node / Tree Search
```

---

# Time Complexity

| Scenario | Complexity |
|----------|------------|
| Normal lookup | **O(1)** |
| Linked List bucket | **O(n)** |
| Red-Black Tree bucket | **O(log n)** |

---

# Common Interview Mistakes

❌ `get()` searches every bucket.

✔ It searches only the calculated bucket.

---

❌ Matching hash codes are enough.

✔ HashMap also uses `equals()`.

---

❌ Collisions return incorrect values.

✔ `equals()` ensures the correct key is found.

---

❌ Treeification changes the bucket selection process.

✔ Treeification only changes how entries are searched **inside** the bucket.

---

# Key Takeaways

- `get()` computes the hash and bucket index.
- HashMap directly jumps to the correct bucket.
- If the bucket is empty, it returns `null`.
- It compares both `hash` and `equals()` to locate the correct key.
- Linked List buckets have **O(n)** lookup.
- Red-Black Tree buckets have **O(log n)** lookup.
- Average lookup complexity remains **O(1)**.

---

# Interview Questions

## Basic

1. What happens internally when `HashMap.get()` is called?
2. Why doesn't HashMap search every bucket?
3. Why is `equals()` required even after matching hash codes?
4. What happens if the bucket is empty?
5. What does `get()` return when the key is absent?

## Intermediate

6. Explain the complete `get()` algorithm.
7. How does `HashMap.get()` work during collisions?
8. How does searching differ in a Linked List and a Red-Black Tree?
9. Why does HashMap compare both `hash` and `equals()`?
10. Compare the time complexities of `get()` in different scenarios.

# Day 5 — Chapter 22.1: HashMap Resizing vs Treeification

One of the most confusing topics in `HashMap` is understanding the difference between **Resizing** and **Treeification**.

Although both improve performance, they solve **different problems**.

---

# Two Different Mechanisms

| Resizing | Treeification |
|----------|---------------|
| Affects the entire `HashMap` | Affects only one bucket |
| Increases the number of buckets | Converts one bucket from a Linked List to a Red-Black Tree |
| Triggered by the total number of entries | Triggered by too many nodes in a single bucket |

---

# 1. Resizing

## Purpose

Resizing increases the size of the hash table to reduce collisions across the entire `HashMap`.

---

## When Does Resizing Happen?

Resizing depends on **three values**.

### Capacity

The total number of buckets.

Example

```
Capacity = 16
```

---

### Load Factor

The percentage of the table that can be filled before resizing.

Default

```
Load Factor = 0.75
```

---

### Threshold

The maximum number of entries allowed before resizing.

Formula

```
Threshold = Capacity × Load Factor
```

Example

```
Capacity = 16

Load Factor = 0.75

Threshold = 12
```

---

## Resize Example

Suppose

```
Capacity = 16

Threshold = 12
```

Insert entries

```
Entry 1

↓

Entry 2

↓

...

↓

Entry 12
```

No resize.

Insert

```
Entry 13
```

Now

```
Entries = 13

Threshold = 12

13 > 12
```

HashMap performs a **resize**.

---

## What Happens During Resizing?

Old table

```
Capacity = 16
```

```
+----+----+----+----+----+----+----+----+
| B0 | B1 | B2 | B3 | ...              |
+----+----+----+----+----+----+----+----+
```

↓

Create a new table

```
Capacity = 32
```

↓

Move every existing entry into the new table.

Each entry gets a new bucket index based on the new capacity.

This process redistributes the entries and usually reduces collisions.

---

## Time Complexity

```
O(n)
```

where **n** is the total number of entries.

---

# 2. Treeification

## Purpose

Treeification speeds up searching inside **one heavily populated bucket**.

Instead of searching a Linked List (`O(n)`), HashMap uses a Red-Black Tree (`O(log n)`).

---

## When Does Treeification Happen?

Both conditions must be satisfied.

### Condition 1

Bucket contains **8 or more nodes**.

```
TREEIFY_THRESHOLD = 8
```

---

### Condition 2

HashMap capacity is at least

```
64
```

```
MIN_TREEIFY_CAPACITY = 64
```

Only then does HashMap convert the bucket into a Red-Black Tree.

---

# Treeification Example

Suppose

```
Capacity = 64
```

Bucket 5 contains

```
101

↓

205

↓

309

↓

401

↓

550

↓

612

↓

720

↓

888
```

There are **8 nodes**.

HashMap checks

```
Bucket Size >= 8 ?

↓

Yes

↓

Capacity >= 64 ?

↓

Yes

↓

Convert Linked List

↓

Red-Black Tree
```

---

# What If Capacity Is Less Than 64?

Suppose

```
Capacity = 16
```

Bucket 5 contains

```
101

↓

205

↓

309

↓

401

↓

550

↓

612

↓

720

↓

888
```

HashMap checks

```
Bucket Size >= 8 ?

↓

Yes

↓

Capacity >= 64 ?

↓

No
```

Instead of treeifying,

HashMap performs a **resize**.

Why?

Increasing the number of buckets may redistribute the entries into different buckets, eliminating the long linked list.

---

# Complete Decision Flow

```
Insert New Entry

↓

Find Bucket

↓

Collision?

↓

Add New Node

↓

Bucket Size >= 8 ?

        │
   ┌────┴────┐
   │         │
  No        Yes
              │
              ▼
     Capacity >= 64 ?

        │
   ┌────┴────┐
   │         │
  No        Yes
   │          │
Resize    Treeify Bucket

↓

After Insertion

↓

Total Entries > Threshold ?

        │
   ┌────┴────┐
   │         │
  No        Yes
              │
      Resize Entire HashMap
```

> **Note:** In the actual JDK implementation, the resize check is part of the `put()` operation. The important concept is that resizing depends on the **total entries**, while treeification depends on the **number of nodes in a single bucket**.

---

# Resizing vs Treeification

| Feature | Resizing | Treeification |
|----------|----------|---------------|
| Scope | Entire HashMap | One Bucket |
| Trigger | Total entries exceed threshold | Bucket has ≥ 8 nodes and capacity ≥ 64 |
| What changes? | Creates a larger hash table | Converts Linked List into Red-Black Tree |
| Capacity Changes? | Yes | No |
| Bucket Count Changes? | Yes | No |
| Improves | Overall collision rate | Lookup speed inside one bucket |

---

# Memory Trick

Think of a city.

### Resizing

```
Entire City

↓

Too many people

↓

Build More Roads

↓

City Becomes Larger
```

Equivalent to

```
More Buckets
```

---

### Treeification

```
One Apartment Building

↓

Too many residents

↓

Reorganize Building

↓

Faster Navigation
```

Equivalent to

```
Linked List

↓

Red-Black Tree
```

---

# Key Takeaways

- **Capacity** is the number of buckets.
- **Entries** are the total key-value pairs stored.
- **Threshold = Capacity × Load Factor**.
- Resizing occurs when **total entries exceed the threshold**.
- Resizing doubles the capacity and redistributes all entries.
- Treeification affects only one bucket.
- Treeification requires:
    - Bucket size ≥ 8
    - Capacity ≥ 64
- If capacity is less than 64, HashMap resizes instead of treeifying.

---

# Interview Questions

## Basic

1. What is resizing in `HashMap`?
2. What triggers resizing?
3. What is treeification?
4. What triggers treeification?
5. Why doesn't Java treeify immediately?

## Intermediate

6. Explain the difference between resizing and treeification.
7. Why is the minimum capacity for treeification 64?
8. What happens internally during resizing?
9. How does treeification improve lookup performance?
10. Can resizing and treeification both occur during a `put()` operation? Explain.

