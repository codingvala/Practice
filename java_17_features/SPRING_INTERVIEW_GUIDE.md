# Spring Interview Guide

## What is Spring?

Spring is a Java framework used to build enterprise applications. It provides support for dependency injection, configuration management, transaction handling, web applications, and much more.

It helps reduce boilerplate code and makes applications easier to manage and test.

---

## Why Spring is used

- Dependency Injection (DI)
- Loose coupling between components
- Easier testing
- Modular architecture
- Support for web, data, security, and transactions
- Good for enterprise applications

---

## Core Spring Concepts

### 1. Dependency Injection (DI)

Spring injects dependencies instead of creating objects manually.

```java
public class Engine {
    public void start() {
        System.out.println("Engine started");
    }
}
```

```java
public class Car {
    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }

    public void drive() {
        engine.start();
        System.out.println("Car is moving");
    }
}
```

### 2. Spring Bean

A Spring Bean is an object managed by the Spring container.

```java
import org.springframework.stereotype.Component;

@Component
public class GreetingService {
    public String greet() {
        return "Hello from Spring";
    }
}
```

### 3. Spring Container

The Spring container creates, configures, and manages beans.

---

## Spring MVC

Spring MVC is used for building web applications.

### Controller example

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello from Spring MVC";
    }
}
```

### Key annotations

- `@Controller` – marks a web controller
- `@RequestMapping` – maps HTTP requests
- `@GetMapping` – handles GET requests
- `@PostMapping` – handles POST requests
- `@ResponseBody` – returns raw response body

---

## Spring Configuration

### XML configuration example

```xml
<bean id="engine" class="com.example.Engine"/>
<bean id="car" class="com.example.Car">
    <constructor-arg ref="engine"/>
</bean>
```

### Java configuration example

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }

    @Bean
    public Car car(Engine engine) {
        return new Car(engine);
    }
}
```

---

## Spring AOP

Aspect-Oriented Programming helps separate cross-cutting concerns like logging and security.

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.*.*(..))")
    public void logBefore() {
        System.out.println("Before method execution");
    }
}
```

---

## Spring JDBC / Data Access

Spring helps with database access using JDBC templates and repositories.

```java
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

---

## Spring Interview Questions

### 1. What is Spring?

Spring is a Java framework that simplifies enterprise application development using dependency injection and modular architecture.

### 2. What is Dependency Injection?

Dependency Injection is a design pattern where dependencies are provided to a class instead of the class creating them itself.

### 3. What is a Spring Bean?

A Spring Bean is an object managed by the Spring IoC container.

### 4. What is the Spring IoC container?

The IoC container creates, configures, and injects Spring beans.

### 5. What is Spring MVC?

Spring MVC is a module used to build web applications using the MVC pattern.

### 6. What is AOP?

AOP allows separation of cross-cutting concerns like logging, security, and transactions from the business logic.

### 7. What is the difference between Spring and Spring Boot?

Spring is the core framework, while Spring Boot is a tool that simplifies setup and configuration for Spring applications.

---

## Short Interview Answer

> Spring is a Java framework used to build enterprise applications with features like dependency injection, MVC, AOP, and data access. It promotes modular and loosely coupled code, making applications easier to maintain, test, and scale.

---

## Quick Revision Summary

- Spring = Java framework
- Bean = object managed by Spring
- DI = dependency injection
- IoC = inversion of control
- MVC = web layer
- AOP = cross-cutting concerns
- Enterprise-ready architecture

---

## Final Tip

For interviews, know these concepts well:

1. IoC and DI
2. Bean lifecycle
3. Spring MVC flow
4. Annotations like `@Controller`, `@Service`, `@Repository`, `@Autowired`
5. Difference between Spring and Spring Boot

