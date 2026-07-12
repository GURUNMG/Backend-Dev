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