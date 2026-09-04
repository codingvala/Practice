# Kubernetes Interview Guide

## What is Kubernetes?

Kubernetes, often called K8s, is an open-source container orchestration platform used to manage and automate the deployment, scaling, and operation of containerized applications.

It helps you run many containers across one or more machines and ensures applications stay available, scalable, and resilient.

### Why Kubernetes is used

- Deploys and manages containers at scale
- Automates rolling updates and rollbacks
- Load balancing across containers
- Self-healing when containers fail
- Scaling up or down based on demand
- Works well for microservices and cloud deployments

---

## Kubernetes vs Docker

| Topic | Docker | Kubernetes |
|---|---|---|
| Purpose | Run containers | Manage containers at scale |
| Scope | Single host / local container runtime | Cluster of machines |
| Scaling | Manual | Automatic scaling |
| Load balancing | Basic | Built-in service discovery and load balancing |
| High availability | Limited | Strong orchestration support |

### Simple definition

- Docker packages and runs a single application in a container.
- Kubernetes manages many Docker containers across a cluster.

---

## Kubernetes Architecture

A Kubernetes cluster has these main components:

### 1. Master / Control Plane

Controls the cluster and manages scheduling and state.

Main components:
- API Server
- Controller Manager
- Scheduler
- etcd

### 2. Worker Nodes

These run the actual application containers.

Main components:
- Kubelet
- Container runtime
- Kube-proxy

---

## Important Kubernetes Objects

### Pod

A Pod is the smallest deployable unit in Kubernetes.

It can contain one or more containers that share the same network and storage.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
spec:
  containers:
    - name: app
      image: nginx
      ports:
        - containerPort: 80
```

### Deployment

A Deployment manages ReplicaSets and ensures the desired number of Pods are running.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
        - name: nginx
          image: nginx:latest
          ports:
            - containerPort: 80
```

### Service

A Service exposes Pods to the network.

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
spec:
  selector:
    app: nginx
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  type: LoadBalancer
```

### Namespace

Namespaces divide cluster resources logically.

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: dev
```

### ConfigMap

ConfigMaps store configuration data separately from application code.

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  APP_ENV: production
```

### Secret

Secrets store sensitive data like passwords or tokens.

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
type: Opaque
stringData:
  password: mysecretpassword
```

---

## Common Kubernetes Commands

### Create a deployment

```bash
kubectl apply -f deployment.yaml
```

### Get pods

```bash
kubectl get pods
```

### Get services

```bash
kubectl get svc
```

### Describe a pod

```bash
kubectl describe pod my-app
```

### Delete a resource

```bash
kubectl delete -f deployment.yaml
```

### View logs

```bash
kubectl logs <pod-name>
```

### Scale a deployment

```bash
kubectl scale deployment nginx-deployment --replicas=5
```

### Get all resources in a namespace

```bash
kubectl get all -n dev
```

---

## Example: Running Nginx in Kubernetes

### deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
        - name: nginx
          image: nginx:latest
          ports:
            - containerPort: 80
```

### service.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
spec:
  selector:
    app: nginx
  ports:
    - protocol: TCP
      port: 80
        targetPort: 80
  type: LoadBalancer
```

### Apply files

```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

### Check running pods

```bash
kubectl get pods
kubectl get svc
```

---

## Kubernetes Features in Interview Context

### 1. Self-healing

If a Pod crashes, Kubernetes restarts it automatically.

### 2. Auto-scaling

Kubernetes can scale workloads based on CPU, memory, or custom metrics.

### 3. Load balancing

A Service distributes traffic across multiple Pods.

### 4. Rolling updates

New versions are deployed gradually without downtime.

### 5. Rollback

If a deployment fails, Kubernetes can revert to the previous version.

---

## Interview Questions and Answers

### 1. What is Kubernetes?

Kubernetes is a container orchestration tool used to deploy, scale, and manage containerized applications across clusters.

### 2. What is a Pod?

A Pod is the smallest unit in Kubernetes and usually contains one or more containers that share resources.

### 3. What is a Deployment?

A Deployment manages the lifecycle of Pods, ensures the desired number of replicas are running, and helps with rolling updates.

### 4. What is a Service?

A Service exposes Pods internally or externally and provides stable networking.

### 5. Why is Kubernetes needed?

It helps manage containers in production, including scaling, failover, rolling deployments, and monitoring.

### 6. What is the difference between Docker and Kubernetes?

Docker runs containers; Kubernetes orchestrates and manages many containers across a cluster.

### 7. What is a Namespace?

A Namespace isolates resources within the same Kubernetes cluster.

### 8. What is the role of the Scheduler?

The Scheduler decides which worker node should run a new Pod.

### 9. What is etcd?

etcd is the key-value store used by Kubernetes to store cluster state.

### 10. What is a ReplicaSet?

A ReplicaSet ensures the specified number of identical Pods are running.

---

## Short Interview Answer

> Kubernetes is a container orchestration platform used to run and manage containerized applications in clusters. It automates deployment, scaling, service discovery, health checks, and rolling updates. Kubernetes helps ensure high availability and efficient resource usage in production environments.

---

## Quick Revision Summary

- Kubernetes = orchestration of containers
- Pod = smallest deployable unit
- Deployment = manages app replicas
- Service = exposes Pods
- Namespace = logical separation
- ConfigMap = configuration
- Secret = sensitive configuration
- Self-healing = restarts failed containers
- Rolling update = smooth upgrade strategy

---

## Final Tip for Interviews

For interviews, focus on:

1. architecture of Kubernetes
2. difference between Pod, Deployment, and Service
3. how scaling and self-healing work
4. commands like `kubectl get pods`, `kubectl logs`, and `kubectl apply`
5. why Kubernetes is useful in production and microservices

This will help you answer both beginner and intermediate Kubernetes interview questions confidently.
