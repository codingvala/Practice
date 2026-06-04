# Spring Annotation Demo

Simple Spring Boot demo showing common annotations and a tiny Thymeleaf UI.

Run:

```bash
cd spring-annotation-demo
mvn spring-boot:run
```

Open http://localhost:8080 to view the demo. H2 Console: http://localhost:8080/h2-console

## Application Flow

```mermaid
flowchart TD
    UI["Browser<br/>index.html"]
    Controller["Controller<br/>DemoController"]
    Service["Service<br/>UserService"]
    Repository["Repository<br/>UserRepository"]
    DB["H2 Database"]
    Component["Component<br/>GreetingComponent"]
    Config["Configuration<br/>AppConfig"]
    
    UI -->|GET /| Controller
    Controller -->|inject| Component
    Component -->|greeting| Controller
    Controller -->|inject| Service
    Service -->|inject| Repository
    Repository -->|query| DB
    Service -->|persist| DB
    Config -->|init| Service
    Controller -->|render| UI
```

## Files of interest:
- `src/main/java/com/example/springannotationdemo/SpringAnnotationDemoApplication.java`
- `src/main/java/com/example/springannotationdemo/web/DemoController.java`
- `src/main/java/com/example/springannotationdemo/service/UserService.java`
- `src/main/java/com/example/springannotationdemo/repository/UserRepository.java`
- `src/main/resources/templates/index.html`
