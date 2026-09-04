# Java 21 Examples with Code

Java 21 is an LTS release with important improvements for data modeling, pattern matching, collection APIs, and concurrency.

## Java 21 Feature Status

| Feature | Java 21 status | Main benefit |
|---|---|---|
| Virtual threads | Final | lightweight high-concurrency tasks |
| Sequenced collections | Final | consistent first/last operations |
| Record patterns | Final | nested record destructuring |
| Pattern matching for `switch` | Final | type-aware switch logic |
| String templates | Preview | structured string interpolation |
| Scoped values | Preview | bounded context propagation |
| Structured concurrency | Preview | simpler task lifetime management |

The distinction between final and preview features is important in interviews. Preview features are not enabled by default and may change in a later release.

---

## 1. String Templates (Preview / JEP 430)

String templates combine a template processor with embedded expressions. In Java 21, `STR` is the built-in processor.

```java
public class StringTemplateExample {
    public static void main(String[] args) {
        String name = "Alice";
        int age = 30;

        String message = STR."Hello, \{name}! You are \{age} years old.";
        System.out.println(message);
    }
}
```

> String templates are a Java 21 preview feature. They require preview flags and were not finalized in Java 21.

### Interview point

Templates are more structured than string concatenation because a processor can validate, transform, or safely handle the embedded values. Do not describe them as finalized Java 21 syntax.

---

## 2. Record Patterns

Record patterns simplify destructuring of records and can be nested inside other patterns. They were finalized in Java 21.

```java
public record Point(int x, int y) {}

public class RecordPatternExample {
    public static void main(String[] args) {
        Object obj = new Point(10, 20);

        if (obj instanceof Point(int x, int y)) {
            System.out.println("x = " + x + ", y = " + y);
        }
    }
}
```

### Nested record pattern

```java
public record Address(String city) {}
public record Customer(String name, Address address) {}

public class NestedRecordPatternExample {
    public static void main(String[] args) {
        Object value = new Customer("Alice", new Address("London"));

        if (value instanceof Customer(String name, Address(String city))) {
            System.out.println(name + " lives in " + city);
        }
    }
}
```

---

## 3. Pattern Matching for `switch`

Pattern matching for `switch` was finalized in Java 21. It allows a switch to select behavior based on an object's type and value.

```java
public class PatternMatchingSwitchExample {
    public static void main(String[] args) {
        Object value = "Java 21";

        String result = switch (value) {
            case Integer i -> "Integer: " + i;
            case String s -> "String: " + s;
            case Double d -> "Double: " + d;
            default -> "Other type";
        };

        System.out.println(result);
    }
}
```

### Null handling

```java
static String describe(Object value) {
    return switch (value) {
        case null -> "null value";
        case String text -> "text: " + text;
        default -> "other value";
    };
}
```

Unlike many older switch statements, a pattern switch can explicitly handle `null` with `case null`.

---

## 4. Sequenced Collections

Java 21 adds `SequencedCollection`, `SequencedSet`, and `SequencedMap`. They provide consistent operations such as `getFirst`, `getLast`, `addFirst`, `addLast`, and `reversed` for ordered collections.

```java
import java.util.List;

public class SequencedCollectionExample {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie");

        System.out.println(names.getFirst());
        System.out.println(names.getLast());
        System.out.println(names.reversed());
    }
}
```

### Interview point

Before Java 21, developers often used collection-specific code to access the first and last elements. Sequenced collections provide a common API for ordered collection types.

---

## 5. Virtual Threads

Virtual threads are lightweight threads managed by the Java runtime. They are suitable for many concurrent tasks, especially tasks that spend time waiting for I/O.

```java
public class VirtualThreadExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.ofVirtual().unstarted(() -> {
            System.out.println("Running in a virtual thread");
        });

        thread.start();
        thread.join();
    }
}
```

### Virtual thread executor

```java
import java.util.concurrent.Executors;

public class VirtualThreadExecutorExample {
    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> System.out.println("Task 1"));
            executor.submit(() -> System.out.println("Task 2"));
        }
    }
}
```

Do not use virtual threads to make CPU-bound work faster. CPU-bound work is still limited by available processor cores.

---

## 6. Scoped Values (Preview)

Scoped values provide immutable, bounded context that can be read by methods called within a dynamic scope. In Java 21 they are a preview feature.

```java
import java.lang.ScopedValue;

public class ScopedValueExample {
    static final ScopedValue<String> USER = ScopedValue.newInstance();

    static String getUserName() {
        return USER.isBound() ? USER.get() : "guest";
    }

    public static void main(String[] args) {
        ScopedValue.where(USER, "Alice").run(() -> {
            System.out.println("User: " + getUserName());
        });
    }
}
```

Scoped values are designed as an alternative to passing request context through every method or relying on mutable thread-local state.

---

## 7. `List.getFirst()` and `getLast()`

New convenience methods for ordered collections.

```java
import java.util.List;

public class OrderedListExample {
    public static void main(String[] args) {
        List<String> items = List.of("A", "B", "C");

        System.out.println(items.getFirst());
        System.out.println(items.getLast());
    }
}
```

---

## 8. `Set.of` and `Map.of` Improvements

Java 21 continues to support compact immutable collection creation.

```java
import java.util.Map;
import java.util.Set;

public class CollectionExample {
    public static void main(String[] args) {
        Set<String> languages = Set.of("Java", "Python", "Go");
        Map<String, Integer> scores = Map.of("Java", 100, "Python", 90);

        System.out.println(languages);
        System.out.println(scores);
    }
}
```

---

## 9. `Thread.ofPlatform()` and `Thread.ofVirtual()`

Java 21 gives developers direct control over thread types.

```java
public class ThreadTypeExample {
    public static void main(String[] args) {
        Thread platformThread = Thread.ofPlatform().start(() -> {
            System.out.println("Platform thread");
        });

        Thread virtualThread = Thread.ofVirtual().start(() -> {
            System.out.println("Virtual thread");
        });

        try {
            platformThread.join();
            virtualThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## 10. Switch with Patterns and Guards

Java 21 supports guarded pattern cases using `when`.

```java
public class SwitchGuardExample {
    public static void main(String[] args) {
        Object value = 25;

        String result = switch (value) {
            case Integer i when i > 0 -> "Positive integer";
            case Integer i -> "Non-positive integer";
            case String s -> "String: " + s;
            default -> "Unknown";
        };

        System.out.println(result);
    }
}
```

The guard is evaluated only after the value matches the pattern. This lets the switch distinguish values such as positive and non-positive integers.

---

## 11. Structured Concurrency (Preview)

Structured concurrency treats related concurrent tasks as one unit of work. In Java 21 it is a preview API, so the exact API may evolve.

```java
// Preview API example for Java 21.
// Compile with --enable-preview when using the matching JDK.
```

The main idea is that child tasks have a clear lifetime and are joined or cancelled together, which makes error handling easier than managing unrelated threads.

---

## 12. Full Demo Example

```java
public class Java21Demo {
    public static void main(String[] args) {
        String name = "Alice";
        int age = 30;

        String message = STR."Hello, \{name}! Age: \{age}";
        System.out.println(message);

        Object obj = new Point(5, 10);

        if (obj instanceof Point(int x, int y)) {
            System.out.println("Point: (" + x + ", " + y + ")");
        }

        Thread virtualThread = Thread.ofVirtual().start(() -> {
            System.out.println("Executing in virtual thread");
        });

        try {
            virtualThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    record Point(int x, int y) {}
}
```

---

## 13. Compile with Java 21

```bash
javac --enable-preview --release 21 Java21Demo.java
java --enable-preview Java21Demo
```

> Some features like string templates and some preview features may require preview flags.

---

## Summary

Java 21 introduces modern, high-performance features such as:

- virtual threads
- scoped values
- record patterns
- pattern matching for switch
- sequenced collections
- string templates

These features help build scalable and expressive Java applications.

---

## Interview Revision Questions

### What are virtual threads?

Virtual threads are lightweight Java threads designed to support very large numbers of concurrent tasks without requiring one expensive platform thread per task.

### Are virtual threads always faster?

No. They improve scalability for many waiting or blocking tasks. They do not increase the CPU capacity available for CPU-bound calculations.

### Which Java 21 features are final?

Virtual threads, sequenced collections, record patterns, and pattern matching for `switch` are final in Java 21.

### Which Java 21 features are preview features?

String templates, scoped values, and structured concurrency are preview features in Java 21.

### What problem do record patterns solve?

They allow a record's components to be extracted directly during pattern matching, including nested records.
