# Docker Interview Guide

## What is Docker?

Docker is a platform used to build, ship, and run applications in isolated environments called containers.

A container packages the application and all its dependencies (libraries, configuration files, runtime, etc.) into one unit. This makes the application run the same way on a developer machine, test server, or production server.

### Why Docker is useful

- Consistency across environments
- Faster setup than virtual machines
- Lightweight and efficient
- Easy deployment and scaling
- Good for microservices and CI/CD pipelines

### Docker vs Virtual Machine

| Feature | Docker Container | Virtual Machine |
|---|---|---|
| Startup time | Fast | Slower |
| Size | Lightweight | Heavy |
| Resource usage | Lower | Higher |
| Isolation | Process-level | Full OS-level |

### Important Docker concepts

- Image: a blueprint for a container
- Container: a running instance of an image
- Dockerfile: instructions to build an image
- Docker Hub: public registry for images
- Volume: persistent data storage
- Network: communication between containers

---

## Dockerfile Example

A Dockerfile is used to define how an image is built.

```dockerfile
# Use a base image
FROM openjdk:17-jdk

# Set working directory
WORKDIR /app

# Copy app files
COPY . .

# Compile Java code
RUN javac HelloWorld.java

# Run the app
CMD ["java", "HelloWorld"]
```

### Example Java program

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello from Docker!");
    }
}
```

---

## Common Docker Commands

### Build an image

```bash
docker build -t my-java-app .
```

### Run a container

```bash
docker run my-java-app
```

### List running containers

```bash
docker ps
```

### List all containers

```bash
docker ps -a
```

### Stop a container

```bash
docker stop <container_id>
```

### Remove a container

```bash
docker rm <container_id>
```

### List images

```bash
docker images
```

### Remove an image

```bash
docker rmi <image_id>
```

### Pull image from Docker Hub

```bash
docker pull nginx
```

---

## Docker Example with a Java App

### Project structure

```text
my-app/
├── HelloWorld.java
├── Dockerfile
```

### HelloWorld.java

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Java app running in Docker container");
    }
}
```

### Dockerfile

```dockerfile
FROM openjdk:17-jdk
WORKDIR /app
COPY HelloWorld.java .
RUN javac HelloWorld.java
CMD ["java", "HelloWorld"]
```

### Build and run

```bash
docker build -t java-docker-demo .
docker run java-docker-demo
```

### Output

```text
Java app running in Docker container
```

---

## Docker Compose Example

Docker Compose is used to run multi-container applications.

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
  db:
    image: postgres:15
    environment:
      POSTGRES_PASSWORD: secret
```

### Run Compose

```bash
docker-compose up
```

or with newer Docker:

```bash
docker compose up
```

---

## Interview Questions and Answers

### 1. What is Docker?

Docker is a tool that packages an application and its dependencies into a container so it can run consistently in any environment.

### 2. What is a Docker image?

A Docker image is a read-only template used to create containers. It contains the application code, runtime, dependencies, and configuration.

### 3. What is a container?

A container is a running instance of a Docker image. It is isolated but shares the host OS kernel.

### 4. Why use Docker instead of a VM?

Docker is lighter and faster because containers share the host OS, while VMs include a full guest operating system.

### 5. What is a Dockerfile?

A Dockerfile is a script with instructions to build a Docker image.

### 6. What is Docker Hub?

Docker Hub is a public registry where Docker images can be stored and pulled from.

### 7. What is the difference between `docker run` and `docker build`?

- `docker build`: creates an image from a Dockerfile
- `docker run`: starts a container from an image

### 8. What is a volume in Docker?

A volume is used to persist data outside the container lifecycle, so data stays available even if the container is removed.

### 9. What is the purpose of `CMD` in Dockerfile?

`CMD` specifies the default command to run when a container starts.

### 10. Why is Docker important for interview preparation?

Because many companies use Docker for deployment, microservices, CI/CD, and cloud environments. It is a common DevOps and Java backend interview topic.

---

## Short Interview Answer

> Docker is a containerization platform that packages applications and their dependencies into a portable environment called a container. It helps ensure consistency across development, testing, and production. A Dockerfile defines how an image is built, and the image is used to run containers. Docker improves deployment speed, scalability, and environment consistency, especially in modern cloud and microservices applications.

---

## Quick Revision Summary

- Docker = containerization platform
- Image = packaged app + dependencies
- Container = running instance of an image
- Dockerfile = build instructions
- Docker Hub = image registry
- Volume = persistent storage
- Compose = multi-container orchestration

---

## Final Tip

For interviews, focus on these points:

1. concept of container vs VM
2. Dockerfile structure
3. basic commands
4. how Docker helps in deployment
5. practical example with Java or Spring Boot app

If you are preparing for backend or DevOps interviews, this is a very important topic to master.
