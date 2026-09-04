# Java Memory Management Interview Guide

This guide explains the JVM memory areas, stack and heap behavior, garbage collection, the Java Memory Model, memory leaks, and practical interview questions.

## 1. JVM Memory at a Glance

A Java process uses several memory areas. Some are managed by the JVM, while others are native operating-system memory.

```text
Java process
|
+-- Heap
|   +-- Young generation: Eden + Survivor spaces
|   +-- Old generation
|
+-- Thread-local memory
|   +-- Java stack for each thread
|   +-- Program counter for each thread
|   +-- Native method stack for each thread
|
+-- Class metadata
|   +-- Metaspace
|
+-- Native memory
    +-- Direct buffers
    +-- JNI allocations
    +-- JVM code cache and internal structures
```

### Interview point

The heap is shared by all Java threads. Each thread has its own stack, program counter, and native method stack. Metaspace stores class metadata and is generally allocated from native memory rather than the Java heap.

## 2. Stack Memory

Each thread receives its own Java Virtual Machine stack when the thread starts. A stack contains frames for active method calls.

A stack frame commonly contains:

- Local variables, including primitive values and object references.
- An operand stack used while executing bytecode.
- A reference to the runtime constant pool for the current class.
- Information needed to return from the method.

```java
public class StackExample {
    public static void main(String[] args) {
        int number = 10;                 // primitive value in main's frame
        Person person = new Person();    // reference in the frame
        calculate(number, person);
    }

    static void calculate(int number, Person person) {
        int result = number * 2;         // local value in calculate's frame
        person.setAge(result);           // object itself remains on the heap
    }
}

class Person {
    private int age;

    void setAge(int age) {
        this.age = age;
    }
}
```

Important details:

- Every thread has a separate stack, so local variables are not automatically shared between threads.
- A stack frame exists only while its method is active.
- A reference variable can be stored on the stack while the referenced object is stored on the heap.
- Recursive calls create additional frames and can exhaust the stack.

### StackOverflowError

`StackOverflowError` usually occurs when the call stack grows beyond its configured limit, often because of unbounded recursion.

```java
public class RecursionExample {
    static void callForever() {
        callForever();
    }

    public static void main(String[] args) {
        callForever();
    }
}
```

The stack size can be influenced with a JVM option such as `-Xss512k`, but increasing it does not fix incorrect recursion.

## 3. Heap Memory

The heap is the runtime area from which objects and arrays are allocated. It is shared by all threads and is managed by the garbage collector.

```java
Person first = new Person();
Person second = first;
first = null;
second = null;
```

After both references are cleared, the `Person` object is eligible for garbage collection, assuming no other reference reaches it.

### Heap characteristics

- Objects and arrays are normally allocated on the heap.
- The heap is shared, so concurrent access must be designed safely.
- Heap size is commonly controlled with `-Xms` for the initial size and `-Xmx` for the maximum size.
- An object being eligible for collection does not mean it is collected immediately.
- `System.gc()` is only a request and must not be used as a correctness mechanism.

### Heap exhaustion

`OutOfMemoryError: Java heap space` means the JVM could not allocate an object in the Java heap. Common causes include:

- An actual live-data set larger than the configured heap.
- A collection that grows without bound.
- Objects retained accidentally by static fields, caches, listeners, or thread locals.
- Excessive allocation combined with unsuitable garbage-collector settings.

## 4. Object Allocation and References

Consider this code:

```java
Order order = new Order();
```

Conceptually:

1. Memory for the `Order` object is allocated in the heap.
2. Its fields receive default values.
3. The constructor runs.
4. The reference to the object is assigned to `order`.
5. The local variable `order` is stored in the current stack frame.

The exact physical layout and optimizations are JVM implementation details. The JIT compiler may use escape analysis and scalar replacement, so an object that does not escape a method might not require a traditional heap allocation.

### Pass-by-value

Java is always pass-by-value. For an object, the value copied is the reference value, not the object itself.

```java
static void change(Person person) {
    person.setAge(30);       // changes the object seen by the caller
    person = new Person();   // changes only the local copied reference
}
```

## 5. Generational Heap Layout

Most garbage collectors use the observation that most objects die young.

```text
Young generation
+--------+----------------+----------------+
| Eden   | Survivor 0     | Survivor 1     |
+--------+----------------+----------------+

Old generation
+------------------------------------------+
| Objects that survived enough collections |
+------------------------------------------+
```

Typical allocation flow:

1. New objects are allocated in Eden.
2. A young collection identifies objects still reachable.
3. Surviving objects move between survivor spaces or are promoted.
4. Objects that survive long enough are promoted to the old generation.
5. Old-generation collection reclaims unreachable long-lived objects.

The exact algorithm depends on the selected collector. Modern collectors may use different regions rather than this simple contiguous layout.

## 6. Garbage Collection

Garbage collection reclaims memory occupied by objects that are no longer reachable from GC roots.

### Common GC roots

- Local variables in active stack frames.
- Live threads.
- Static fields of loaded classes.
- JNI references.
- System class loaders and other JVM-managed references.

An object is collectible when no GC root can reach it through object references.

### Basic collection phases

A collector may perform some combination of:

- Marking reachable objects.
- Sweeping unreachable objects.
- Compacting or moving surviving objects.
- Updating references after objects move.

### Stop-the-world pauses

A stop-the-world pause temporarily stops application threads so the JVM can perform work safely. Some collectors reduce pause duration and perform more work concurrently, but no collector guarantees zero pause in every situation.

### Common collectors

| Collector | Typical characteristic |
|---|---|
| Serial GC | Simple, single-threaded collection; useful for small heaps or single-core environments |
| Parallel GC | Uses multiple threads and aims for throughput |
| G1 GC | Region-based collector with predictable pause-time goals; common for server workloads |
| ZGC | Designed for very low pauses and large heaps |
| Shenandoah | Concurrent evacuation with low-pause goals |

The right collector depends on latency, throughput, heap size, allocation rate, and deployment constraints.

## 7. Strong, Soft, Weak, and Phantom References

Java provides reference types with different relationships to garbage collection.

- **Strong reference:** normal reference; the object remains reachable while a strong path exists.
- **Soft reference:** may be cleared when the JVM needs memory; historically used for memory-sensitive caches, but explicit bounded caches are usually more predictable.
- **Weak reference:** does not prevent collection; useful for structures such as `WeakHashMap`.
- **Phantom reference:** used with a `ReferenceQueue` to receive notification after an object becomes phantom reachable; it cannot be used to access the object.

```java
Map<Key, Value> cache = new WeakHashMap<>();
```

A weak-key entry can disappear when its key has no strong references elsewhere.

## 8. Metaspace

Metaspace stores class metadata, such as information about loaded classes and methods. Since Java 8, it replaced PermGen.

Important points:

- Metaspace is generally allocated in native memory.
- It grows as classes are loaded, subject to available native memory and optional limits.
- Class unloading can reclaim metadata when the class loader and its classes are no longer reachable.
- Repeatedly creating class loaders or dynamically generating classes can cause metadata exhaustion.

`OutOfMemoryError: Metaspace` often indicates excessive class loading, a class-loader leak, or an undersized `MaxMetaspaceSize` setting.

## 9. Native Memory and Direct Buffers

Not all memory used by a Java process belongs to the Java heap.

Examples include:

- Thread stacks.
- Metaspace.
- JIT-compiled code cache.
- JVM internal structures.
- JNI allocations.
- Direct byte buffers.
- Native libraries.

```java
ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
```

A direct buffer can reduce copying during some I/O operations, but it consumes native memory and must be monitored separately from heap usage.

Possible failures include:

- `OutOfMemoryError: Direct buffer memory`.
- `OutOfMemoryError: unable to create native thread`.
- Operating-system process termination caused by total native memory pressure.

## 10. Java Memory Model

The Java Memory Model (JMM) defines how threads interact through memory. It describes visibility, ordering, and atomicity rules.

### Visibility

A write by one thread is not guaranteed to become visible to another thread without a valid happens-before relationship.

```java
class Worker {
    private volatile boolean running = true;

    void stop() {
        running = false;
    }

    void work() {
        while (running) {
            // The volatile read observes the volatile write.
        }
    }
}
```

`volatile` provides visibility and ordering for the variable, but it does not make compound operations atomic.

### Atomicity

This is not atomic:

```java
count++;
```

It consists conceptually of read, add, and write. Use synchronization or an atomic type when multiple threads update the value.

```java
AtomicInteger count = new AtomicInteger();
count.incrementAndGet();
```

### Happens-before relationships

Common happens-before rules include:

- Unlocking a monitor happens-before a later lock of the same monitor.
- A write to a volatile field happens-before a later read of that field.
- Starting a thread happens-before actions in the started thread.
- Actions in a thread happen-before another thread successfully returns from `join()`.
- Actions before placing an object in a thread-safe collection happen-before a properly synchronized retrieval.

### Safe publication

An object must be safely published for another thread to see a correctly initialized state. Common mechanisms include:

- Constructing and accessing it under the same lock.
- Storing it in a volatile field.
- Using a static initializer.
- Using a thread-safe collection.
- Using immutable objects with final fields correctly initialized in the constructor.

## 11. Common Memory Leaks in Java

Java prevents manual deallocation, but applications can still leak memory by retaining objects longer than necessary.

Frequent causes:

- Static collections that continuously grow.
- Unbounded caches.
- Listeners or callbacks that are never deregistered.
- `ThreadLocal` values left on long-lived thread-pool threads.
- Class-loader retention in application servers or plugin systems.
- Queues that are produced to faster than they are consumed.
- Maps keyed by objects whose lifecycle has ended.

```java
class RequestContext {
    private static final ThreadLocal<byte[]> CONTEXT = new ThreadLocal<>();

    static void handle() {
        try {
            CONTEXT.set(new byte[1024 * 1024]);
            // Request work
        } finally {
            CONTEXT.remove();
        }
    }
}
```

The `finally` block is important when pooled threads outlive individual requests.

## 12. OutOfMemoryError versus StackOverflowError

| Error | Meaning | Typical cause |
|---|---|---|
| `OutOfMemoryError: Java heap space` | Heap allocation failed | Too many live objects or a leak |
| `OutOfMemoryError: Metaspace` | Class metadata allocation failed | Class-loader or dynamic-class leak |
| `OutOfMemoryError: Direct buffer memory` | Direct-buffer allocation failed | Excessive direct-buffer use |
| `OutOfMemoryError: unable to create native thread` | Native thread could not be created | Too many threads or insufficient native memory |
| `StackOverflowError` | Thread stack limit was exceeded | Deep or infinite recursion |

## 13. Useful JVM Diagnostics

Useful tools and options include:

```bash
jps -lv
jcmd <pid> GC.heap_info
jcmd <pid> GC.class_histogram
jcmd <pid> Thread.print
jstack <pid>
jmap -histo <pid>
```

For production diagnostics, prefer `jcmd` and controlled JVM logging. Heap dumps can be enabled with options such as:

```bash
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/java-heap-dump.hprof
```

GC logging on modern Java versions can be enabled with:

```bash
-Xlog:gc*,safepoint:file=gc.log:time,uptime,level,tags
```

A practical investigation usually compares:

1. Used heap after a full collection.
2. Allocation rate and GC frequency.
3. Old-generation occupancy over time.
4. Thread count and thread-stack memory.
5. Metaspace and direct-buffer usage.
6. A heap dump or class histogram when retention is suspected.

## 14. Frequently Asked Interview Questions

### Is Java pass-by-reference?

No. Java is always pass-by-value. For objects, the copied value is a reference to the object.

### Where are primitive variables stored?

A primitive local variable is normally part of a stack frame, while a primitive field belongs to its containing object on the heap. The JIT compiler may optimize the physical representation.

### Where are objects stored?

Objects and arrays are normally allocated on the heap. JVM optimizations such as escape analysis can change the physical allocation without changing Java semantics.

### Are static variables stored on the stack?

No. A static field belongs to its class and is associated with class metadata and JVM-managed storage. It is not a per-thread stack local.

### What is the difference between `final`, `finally`, and `finalize`?

- `final` prevents reassignment, overriding, or inheritance depending on where it is used.
- `finally` is a block commonly used for cleanup.
- `finalize` was an unreliable cleanup mechanism and has been deprecated for removal. Use `try-with-resources` and `AutoCloseable` instead.

### Does calling `System.gc()` force garbage collection?

No. It only requests that the JVM consider collection. The JVM may ignore the request.

### Can an object be garbage-collected while a method is still running?

An object can be collected once it is no longer reachable, even if its lexical variable is still in scope. The JIT may determine that a reference is no longer needed. Use `Reference.reachabilityFence` only when a resource needs to remain reachable until a specific operation completes.

### What does `volatile` solve?

It provides visibility and ordering for reads and writes of a field. It does not make operations such as incrementing a counter atomic.

### Why can a Java application use more memory than `-Xmx`?

`-Xmx` limits the Java heap, not total process memory. Metaspace, thread stacks, direct buffers, the code cache, native libraries, and JVM internals also consume memory.

### How would you investigate a memory leak?

Confirm that post-GC heap usage grows over time, capture class histograms or heap dumps at different points, identify retained objects and their GC-root paths, then inspect the owner such as a cache, static field, listener, or thread local.

## 15. Short Interview Summary

- **Stack:** per-thread method frames, local variables, and call state.
- **Heap:** shared area for objects and arrays, managed by GC.
- **Metaspace:** class metadata, generally in native memory.
- **Native memory:** direct buffers, thread stacks, code cache, JNI, and JVM internals.
- **GC:** reclaims unreachable objects, not merely objects that are old.
- **Memory leak:** reachable objects retained beyond their useful lifetime.
- **JMM:** defines visibility, ordering, and atomicity between threads.
- **`volatile`:** visibility and ordering, not compound-operation atomicity.
- **`-Xmx`:** maximum heap, not maximum process memory.
- **Diagnostics:** use GC logs, `jcmd`, thread dumps, histograms, and heap dumps.
