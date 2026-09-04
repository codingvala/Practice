# Java 8 Examples with Code

Java 8 was a major release because it introduced functional programming features while keeping Java object-oriented. The most important additions were lambda expressions, functional interfaces, the Stream API, `Optional`, and the modern Date/Time API.

This guide explains each feature, shows code, and includes interview points.

---

## 1. Lambda Expressions

Lambda expressions are short implementations of functional interfaces. They let you pass behavior as a method argument instead of creating an anonymous class.

### Syntax

```text
(parameters) -> expression
(parameters) -> { statements }
```

The parameter types can usually be inferred by the compiler.

```java
import java.util.Arrays;
import java.util.List;

public class LambdaExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        names.forEach(name -> System.out.println("Hello, " + name));
    }
}
```

### Lambda with multiple statements

```java
import java.util.function.Consumer;

public class LambdaBlockExample {
    public static void main(String[] args) {
        Consumer<String> printer = name -> {
            String message = "Hello, " + name;
            System.out.println(message);
        };

        printer.accept("Java 8");
    }
}
```

### Interview point

Lambdas do not create a new type. They provide an implementation for a functional interface and are commonly used with collections and streams.

---

## 2. Functional Interfaces

A functional interface has exactly one abstract method. It can have multiple `default` and `static` methods. The `@FunctionalInterface` annotation is optional, but it makes the compiler verify the rule.

```java
@FunctionalInterface
interface Greeting {
    void sayHello(String name);
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        Greeting greeting = name -> System.out.println("Hello, " + name);
        greeting.sayHello("Java 8");
    }
}
```

### Built-in functional interfaces

Java 8 provides commonly used interfaces in `java.util.function`:

| Interface | Method | Purpose |
|---|---|---|
| `Predicate<T>` | `boolean test(T)` | checks a condition |
| `Function<T, R>` | `R apply(T)` | converts a value |
| `Consumer<T>` | `void accept(T)` | consumes a value |
| `Supplier<T>` | `T get()` | supplies a value |

```java
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltInFunctionalInterfaceExample {
    public static void main(String[] args) {
        Predicate<Integer> isEven = number -> number % 2 == 0;
        Function<String, Integer> length = String::length;
        Consumer<String> print = System.out::println;
        Supplier<String> defaultValue = () -> "Default value";

        System.out.println(isEven.test(4));
        System.out.println(length.apply("Java"));
        print.accept(defaultValue.get());
    }
}
```

### Interview point

Functional interfaces are the foundation of lambda expressions, streams, callbacks, and many modern Java APIs.

---

## 3. Streams API

The Stream API processes a sequence of data in a declarative style. A stream does not store data and does not change the original collection unless the operation explicitly changes mutable state.

### Stream operation types

- Intermediate operations return another stream, for example `filter`, `map`, and `sorted`.
- Terminal operations produce a result or side effect, for example `collect`, `forEach`, `count`, and `reduce`.
- Streams are lazy: intermediate operations run only when a terminal operation is called.

```java
import java.util.Arrays;
import java.util.List;

public class StreamExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        int sum = numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("Sum of even numbers: " + sum);
    }
}
```

### Filter, map, sort, and collect

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamPipelineExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Bob", "Alice", "Charlie", "Ann");

        List<String> result = names.stream()
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());

        System.out.println(result);
    }
}
```

### `reduce` example

```java
import java.util.Arrays;

public class ReduceExample {
    public static void main(String[] args) {
        int total = Arrays.asList(1, 2, 3, 4)
                .stream()
                .reduce(0, Integer::sum);

        System.out.println(total);
    }
}
```

### Interview point

Use streams for readable data transformations. Avoid modifying shared variables inside `forEach`, especially with parallel streams.

---

## 4. Default Methods in Interfaces

Interfaces can now provide `default` method implementations and `static` utility methods. This allowed Java to evolve existing interfaces without forcing every implementation class to add a new method.

```java
interface Vehicle {
    void start();

    default void stop() {
        System.out.println("Vehicle stopped");
    }
}

class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car started");
    }
}

public class DefaultMethodExample {
    public static void main(String[] args) {
        Vehicle car = new Car();
        car.start();
    }
}
```

### Static interface method

```java
interface MathOperations {
    static int square(int value) {
        return value * value;
    }
}

public class StaticInterfaceMethodExample {
    public static void main(String[] args) {
        System.out.println(MathOperations.square(5));
    }
}
```

### Interview point

Default methods are inherited by implementing classes. If two interfaces provide the same default method, the implementing class must resolve the conflict by overriding it.

---

## 5. Date and Time API

Java 8 introduced the immutable and thread-safe `java.time` API. It replaced many problems associated with `Date` and `Calendar`.

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeExample {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();

        System.out.println("Today: " + date);
        System.out.println("Current time: " + dateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Formatted: " + date.format(formatter));
    }
}
```

### Common date/time types

- `LocalDate` represents a date without time or timezone.
- `LocalTime` represents a time without a date or timezone.
- `LocalDateTime` represents date and time without a timezone.
- `ZonedDateTime` represents date and time in a timezone.
- `Instant` represents a point on the UTC timeline.
- `Period` measures date-based amounts such as years and days.
- `Duration` measures time-based amounts such as seconds and hours.

```java
import java.time.LocalDate;
import java.time.Period;

public class DateCalculationExample {
    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 1);

        System.out.println(Period.between(start, end).getYears());
        System.out.println(start.plusDays(10));
    }
}
```

### Interview point

Most `java.time` classes are immutable and thread-safe. Methods such as `plusDays` return a new object instead of changing the original value.

---

## 6. `Optional` Class

`Optional<T>` represents a value that may or may not exist. It is mainly useful as a method return type when absence is a valid result.

```java
import java.util.Optional;

public class OptionalExample {
    public static void main(String[] args) {
        Optional<String> value = Optional.of("Java 8");

        System.out.println(value.isPresent());
        System.out.println(value.get());
        System.out.println(value.orElse("Default Value"));
    }
}
```

### Mapping an Optional

```java
import java.util.Optional;

public class OptionalMappingExample {
    public static void main(String[] args) {
        Optional<String> name = Optional.ofNullable("alice");

        String upperCaseName = name
                .map(String::toUpperCase)
                .orElse("UNKNOWN");

        System.out.println(upperCaseName);
    }
}
```

### Important Optional rules

- Use `Optional.of(value)` only when the value is known to be non-null.
- Use `Optional.ofNullable(value)` when the value may be null.
- Prefer `orElse`, `orElseGet`, or `orElseThrow` over calling `get()` without checking.
- Avoid using `Optional` as a field or method parameter in most application code.

---

## 7. Method References

Method references are a shorter form of a lambda when an existing method already performs the required operation.

Common forms are:

- `ClassName::staticMethod`
- `object::instanceMethod`
- `ClassName::instanceMethod`
- `ClassName::new` for a constructor reference

```java
import java.util.Arrays;
import java.util.List;

public class MethodReferenceExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        names.forEach(System.out::println);
    }
}
```

### Constructor reference

```java
import java.util.function.Supplier;

public class ConstructorReferenceExample {
    public static void main(String[] args) {
        Supplier<StringBuilder> builderFactory = StringBuilder::new;
        System.out.println(builderFactory.get().append("Java 8"));
    }
}
```

---

## 8. Base64 API

Java 8 added a standard Base64 encoder and decoder in `java.util`.

```java
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Example {
    public static void main(String[] args) {
        String original = "Java 8";
        String encoded = Base64.getEncoder().encodeToString(
                original.getBytes(StandardCharsets.UTF_8));
        String decoded = new String(
                Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);

        System.out.println(encoded);
        System.out.println(decoded);
    }
}
```

> Base64 is encoding, not encryption. Do not use it to protect passwords or sensitive data.

---

## 9. Nashorn JavaScript Engine

Java 8 included the Nashorn JavaScript engine. It was deprecated in Java 11 and removed in Java 15, so it is useful as a historical Java 8 topic but should not be used in new applications.

```java
// This is a Java 8 feature example, not a recommended modern usage.
// Nashorn was part of Java 8 and was removed in later JDKs.
```

---

## 10. `forEach` on Collections

A cleaner iteration style using `forEach`.

```java
import java.util.Arrays;
import java.util.List;

public class ForEachExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("A", "B", "C");
        names.forEach(name -> System.out.println("Name: " + name));
    }
}
```

---

## 11. `Collectors` Example

Used with streams to collect results into a collection.

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectorsExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        List<String> upperCaseNames = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(upperCaseNames);
    }
}
```

---

## 12. CompletableFuture

`CompletableFuture` supports asynchronous and non-blocking-style workflows.

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "data")
                .thenApply(String::toUpperCase)
                .thenApply(value -> "Result: " + value);

        System.out.println(future.join());
    }
}
```

`thenApply` transforms a result, `thenAccept` consumes a result, and `thenCompose` chains another asynchronous operation.

---

## 13. Parallel Streams

Parallel streams split work across multiple threads. They can help with large, CPU-heavy, independent operations, but they are not automatically faster.

```java
import java.util.Arrays;

public class ParallelStreamExample {
    public static void main(String[] args) {
        int sum = Arrays.asList(1, 2, 3, 4, 5)
                .parallelStream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(sum);
    }
}
```

Avoid parallel streams for small collections, blocking I/O, or operations that depend on shared mutable state.

---

## 14. Complete Example

```java
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Java8Demo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        names.stream()
             .filter(name -> name.startsWith("A"))
             .map(String::toUpperCase)
             .forEach(System.out::println);

        Optional<String> optionalName = Optional.of("Java 8");
        System.out.println(optionalName.orElse("No value"));
    }
}
```

### Expected output

```text
ALICE
Java 8
```

---

## 15. How to Compile

```bash
javac Java8Demo.java
java Java8Demo
```

Check the installed Java version with:

```bash
java -version
javac -version
```

To force Java 8 language and API compatibility from a newer JDK, use:

```bash
javac --release 8 Java8Demo.java
```

---

## Summary

Java 8 introduced major improvements:

- Lambda expressions
- Streams API
- Optional
- Functional interfaces
- Default methods
- New Date/Time API
- Improved collection iteration
- Built-in functional interfaces
- Base64 utilities
- CompletableFuture
- Parallel streams

These features changed the way Java code is written and became the base for modern Java programming.

---

## Interview Revision Questions

### What is the difference between a Collection and a Stream?

A collection stores data. A stream processes data and usually does not store it.

### What is the difference between `map` and `filter`?

`map` transforms every element. `filter` keeps only elements that satisfy a condition.

### What is the difference between `orElse` and `orElseGet`?

`orElse` evaluates its fallback immediately. `orElseGet` evaluates the fallback lazily through a `Supplier`.

### Why was the new Date/Time API introduced?

The old date APIs were difficult to use, mutable, and not consistently thread-safe. `java.time` provides clearer immutable types.

### What is a default method?

A default method is an interface method with an implementation. It allows interfaces to add behavior without breaking existing implementations.
