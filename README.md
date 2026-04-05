# Java Multithreaded Network Scanner

A fast, concurrent TCP port scanner built with Java thread pools and raw sockets.

## Features
- Multithreaded scanning via ExecutorService fixed thread pool
- Distinguishes OPEN / CLOSED / FILTERED port states
- Banner grabbing for service detection
- Live progress bar during scan
- JSON result export
- CLI interface for any IP or IP range

## Build
```bash
mvn clean package
```

## Usage
```bash
# Scan localhost (defaults: ports 1-1024, 200 threads)
java -jar target/scanner.jar

# Scan single IP
java -jar target/scanner.jar 192.168.1.1

# Scan IP range with custom ports and threads
java -jar target/scanner.jar <startIp> <endIp> <startPort> <endPort> <threads> <timeoutMs>
```

## Architecture

| Component | Class | Responsibility |
|---|---|---|
| IP generator | `IPRangeGenerator` | Enumerates target IPs and ports |
| Task model | `ScanTask` | Immutable (host, port) pair |
| Worker | `PortScanner` | TCP connect + banner grab per thread |
| Thread pool | `ScannerEngine` | ExecutorService dispatch and Future collection |
| Storage | `ResultCollector` | Thread-safe ConcurrentHashMap |
| Result model | `ScanResult` | State enum + banner |

## Tech Stack
- Java 17
- Maven
- java.net.Socket (raw TCP)
- java.util.concurrent (ExecutorService, Future, AtomicInteger)
- Gson (JSON export)
