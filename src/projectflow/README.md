# ProjectFlow

## Prerequisites

- Java 17+
- Docker

## Setup

```bash
# clone the project
cd src/projectflow/
./mvnw install -DskipTests
```

## Run

```bash
make run
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Stop

```bash
make stop
```

## Testing

```bash
./mvnw test
```
