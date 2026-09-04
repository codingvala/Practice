# Java 17 Examples with Code

This document includes practical Java 17 examples covering the most important language and API features introduced or finalized in Java 17.

> Java 17 is a Long-Term Support (LTS) release.

## Java 17 Feature Status

| Feature | Java 17 status | Main benefit |
|---|---|---|
| Switch expressions | Final | concise value-producing `switch` |
| Text blocks | Final | readable multiline strings |
| Records | Final | compact immutable data classes |
| Pattern matching for `instanceof` | Final | type check and cast together |
| Sealed classes | Final | controlled inheritance |
| Foreign Function and Memory API | Preview | access native memory and code |

Java 17 is also important because it is an LTS release commonly used in enterprise applications.

---

## 1. Switch Expression

Java 17 supports modern `switch` expressions with `->` and no accidental fall-through behavior. Unlike the traditional `switch` statement, a switch expression returns a value.

```java
public class SwitchExample {
    public static void main(String[] args) {
        String day = "FRIDAY";

        String result = switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Working day";
            case "SATURDAY", "SUNDAY" -> "Weekend";
            default -> "Unknown day";
        };

        System.out.println(result);
    }
}
```

### Traditional switch versus switch expression

```java
int dayNumber = 2;

String day = switch (dayNumber) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    default -> "Unknown";
};
```

Use `yield` only when a switch arm contains a block and needs to return a value from that block.

### Interview point

The arrow form prevents fall-through. Multiple labels can share one result, and `default` is normally required when the input is not an enum whose cases are fully covered.

### Example with yield

```java
public class SwitchYieldExample {
    public static void main(String[] args) {
        int month = 4;

        String season = switch (month) {
            case 12, 1, 2 -> {
                yield "Winter";
            }
            case 3, 4, 5 -> {
                yield "Spring";
            }
            case 6, 7, 8 -> {
                yield "Summer";
            }
            case 9, 10, 11 -> {
                yield "Autumn";
            }
            default -> {
                yield "Invalid month";
            }
        };

        System.out.println(season);
    }
}
```

---

## 2. Text Blocks

Text blocks are multiline string literals. Java removes incidental indentation while preserving meaningful formatting, which makes JSON, SQL, HTML, and configuration text easier to read.

```java
public class TextBlockExample {
    public static void main(String[] args) {
        String json = """
            {
              "name": "John",
              "age": 30,
              "city": "New York"
            }
            """;

        System.out.println(json);
    }
}
```

### Useful text block methods

```java
public class TextBlockMethodExample {
    public static void main(String[] args) {
        String query = """
                SELECT id, name
                FROM users
                WHERE active = true
                """;

        System.out.println(query.stripIndent());
        System.out.println(query.translateEscapes());
    }
}
```

### Interview point

Text blocks are still ordinary `String` values at runtime. They are a syntax improvement, not a new runtime type.

### Text block with indentation control

```java
public class TextBlockIndentExample {
    public static void main(String[] args) {
        String html = """
            <html>
                <body>
                    <h1>Hello, Java 17!</h1>
                </body>
            </html>
            """;

        System.out.println(html);
    }
}
```

---

## 3. Records

Records are concise classes for transparent, shallowly immutable data. The compiler generates a final class, private final components, accessors, a canonical constructor, `equals`, `hashCode`, and `toString`.

```java
public record Person(String name, int age) {
}

public class RecordExample {
    public static void main(String[] args) {
        Person person = new Person("Alice", 28);

        System.out.println(person.name());
        System.out.println(person.age());
        System.out.println(person);
        System.out.println(person.equals(new Person("Alice", 28)));
    }
}
```

### Record limitations

- A record cannot extend another class.
- A record can implement interfaces.
- Record components are final references, but referenced objects can still be mutable.
- Records are ideal for DTOs, API responses, and value objects.

### Record with custom methods

```java
public record Employee(String name, String department) {
    public Employee {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    public String fullInfo() {
        return name + " works in " + department;
    }
}

public class EmployeeExample {
    public static void main(String[] args) {
        Employee emp = new Employee("Bob", "Engineering");
        System.out.println(emp.fullInfo());
    }
}
```

---

## 4. Pattern Matching for instanceof

This simplifies type checks and casting by declaring the pattern variable directly in the `instanceof` expression. The variable is available only where the compiler knows the condition is true.

```java
public class PatternMatchingExample {
    public static void main(String[] args) {
        Object value = "Java 17";

        if (value instanceof String s) {
            System.out.println("String length: " + s.length());
        }

        Object number = 42;
        if (number instanceof Integer i) {
            System.out.println("Integer value: " + i);
        }
    }
}
```

### Flow scoping

```java
public class PatternFlowScopeExample {
    public static void print(Object value) {
        if (value instanceof String text && !text.isBlank()) {
            System.out.println(text.trim());
        }
    }
}
```

### Interview point

Pattern matching removes explicit casts such as `(String) value` and makes code safer because the compiler controls the variable's valid scope.

### Pattern matching inside a method

```java
public class PatternMatchingMethodExample {
    public static void printValue(Object obj) {
        if (obj instanceof String str && str.length() > 3) {
            System.out.println("Long string: " + str);
        } else if (obj instanceof Integer num) {
            System.out.println("Integer: " + num);
        } else {
            System.out.println("Other type");
        }
    }

    public static void main(String[] args) {
        printValue("Hello Java");
        printValue(10);
        printValue(10.5);
    }
}
```

---

## 5. Sealed Classes

Sealed classes restrict which classes can extend or implement them. A permitted subclass must be `final`, `sealed`, or `non-sealed`.

```java
public sealed class Shape permits Circle, Rectangle {
}

public final class Circle extends Shape {
    public double radius;
}

public final class Rectangle extends Shape {
    public double width;
    public double height;
}

public class SealedClassExample {
    public static void main(String[] args) {
        Shape shape = new Circle();

        if (shape instanceof Circle c) {
            System.out.println("Circle with radius " + c.radius);
        } else if (shape instanceof Rectangle r) {
            System.out.println("Rectangle width=" + r.width + ", height=" + r.height);
        }
    }
}
```

### Sealed hierarchy example

```java
public sealed interface Payment permits CardPayment, CashPayment {
}

public final class CardPayment implements Payment {
}

public non-sealed class CashPayment implements Payment {
}
```

`non-sealed` intentionally opens a branch of the hierarchy for further extension.

---

## 6. `List.of`, `Set.of`, `Map.of`

Java 9+ provides compact collection factories, which are available in Java 17. The returned collections reject modification and do not allow null elements or keys.

```java
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionFactoryExample {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Set<Integer> numbers = Set.of(1, 2, 3, 4);
        Map<String, Integer> ages = Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 28
        );

        System.out.println(names);
        System.out.println(numbers);
        System.out.println(ages);
    }
}
```

```java
import java.util.List;

public class ImmutableCollectionExample {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob");

        try {
            names.add("Charlie");
        } catch (UnsupportedOperationException exception) {
            System.out.println("Collection is immutable");
        }
    }
}
```

---

## 7. `Stream.toList()`

Java 16 introduced `Stream.toList()`, which is available in Java 17. It returns an unmodifiable list. Use `collect(Collectors.toList())` when your code specifically requires a mutable list.

```java
import java.util.List;
import java.util.stream.Stream;

public class StreamToListExample {
    public static void main(String[] args) {
        List<String> names = Stream.of("A", "B", "C")
                .map(String::toLowerCase)
                .toList();

        System.out.println(names);
    }
}
```

---

## 8. Helpful Java 17 API Improvements

Java 17 also includes useful APIs introduced in earlier releases and commonly used with Java 17 applications.

```java
public class StringApiExample {
    public static void main(String[] args) {
        System.out.println("Java".repeat(2));
        System.out.println("  Java 17  ".strip());
        System.out.println("Java 17".formatted());
    }
}
```

These APIs are not all new in Java 17, but they are worth knowing when working on a Java 17 codebase.

---

## 9. Interview Questions

### Why are records useful?

They reduce boilerplate for immutable data carriers while automatically providing value-based equality and accessors.

### What is the difference between a record and a normal class?

A record has a restricted data-oriented structure and generated members. A normal class gives complete control over inheritance, fields, and behavior.

### What is the purpose of a sealed class?

It makes the permitted inheritance hierarchy explicit, which improves domain modeling, validation, and compiler analysis.

### Is `Stream.toList()` mutable?

No. The list returned by `Stream.toList()` is unmodifiable.

### What is the difference between a switch statement and expression?

A statement performs actions, while an expression produces a value and can be assigned or returned.

---

## 10. Compile and Run

```bash
javac --release 17 SwitchExample.java
java SwitchExample
```

Check the installed version with:

```bash
java -version
javac -version
```

---
## Summary

Java 17 provides a modern developer experience with:

- Switch expressions
- Text blocks
- Records
- Pattern matching for `instanceof`
- Sealed classes
- Better collection factories
- Simpler stream usage
- Stronger string and file APIs
- Better API ergonomics for everyday coding

This is a good foundation for learning modern Java programming and preparing for Java 21+ features.
