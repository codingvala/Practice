# Spring Boot Interview Guide

## What is Spring Boot?

Spring Boot is a framework built on top of Spring that makes it easier to create production-ready applications with minimal configuration.

It reduces boilerplate code and helps developers start quickly with an opinionated setup.

---

## Why Spring Boot is popular

- Easy project setup
- Embedded Tomcat/Jetty server
- Auto-configuration
- Production-ready defaults
- Less XML configuration
- Fast development for REST APIs and microservices

---

## Key Spring Boot Features

### 1. Auto-Configuration

Spring Boot automatically configures common application components based on dependencies.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 2. Embedded Server

Spring Boot includes an embedded web server, so you do not need to deploy WAR files manually.

### 3. Starter Dependencies

Starters help add common dependencies in one line.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 4. Spring Boot Actuator

Actuator provides health and metrics endpoints.

```properties
management.endpoints.web.exposure.include=health,info,metrics
```

---

## REST Controller Example

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot";
    }
}
```

---

## Application Properties

```properties
server.port=8081
spring.application.name=my-app
```

---

## Dependency Injection in Spring Boot

```java
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public String greet() {
        return "Welcome to Spring Boot";
    }
}
```

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greet")
    public String greet() {
        return greetingService.greet();
    }
}
```

---

## Spring Boot Project Structure

```text
my-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── mvnw
```

---

## Common Spring Boot Annotations

- `@SpringBootApplication` – main application class
- `@RestController` – REST endpoint controller
- `@GetMapping` – maps GET requests
- `@PostMapping` – maps POST requests
- `@RequestBody` – binds request body
- `@Service` – service layer
- `@Repository` – data layer
- `@Configuration` – configuration class

---

## Spring Boot Interview Questions

### 1. What is Spring Boot?

Spring Boot is a framework that simplifies Spring application development by reducing configuration and providing auto-configured defaults.

### 2. What is auto-configuration?

Auto-configuration automatically configures Spring beans and runtime components based on the dependencies present in the project.

### 3. What is an embedded server?

An embedded server is a web server packaged inside the application, such as Tomcat or Jetty, so the app can run directly.

### 4. What is `@SpringBootApplication`?

This annotation combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.

### 5. Why is Spring Boot preferred for microservices?

Because it is fast to setup, lightweight, supports REST APIs, integrates with cloud services, and is easy to deploy.

### 6. What is `application.properties`?

It is the file used to configure application settings like port, database, and logging.

### 7. Difference between Spring and Spring Boot?

Spring is the main framework; Spring Boot is a faster, opinionated way to build and run Spring applications with minimal configuration.

---

## Short Interview Answer

> Spring Boot is a framework built on top of Spring that helps developers create production-ready Java applications quickly. It provides auto-configuration, embedded servers, starter dependencies, and simplified setup so developers can focus on business logic instead of configuration.

---

## Quick Revision Summary

- Spring Boot = simplified Spring development
- Auto-configuration = automatic bean setup
- Embedded server = app runs without external server setup
- Starter dependencies = common package bundles
- REST APIs = easy to build with controllers
- Great for microservices and cloud apps

---

## Final Tip

For interviews, focus on:

1. `@SpringBootApplication`
2. Spring Boot starter dependencies
3. embedded server concept
4. REST controller and HTTP mapping
5. difference between Spring and Spring Boot
6. how auto-configuration works

These are the most commonly asked Spring Boot interview topics.
