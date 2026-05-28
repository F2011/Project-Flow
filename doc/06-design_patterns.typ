#import "@preview/note-me:0.6.0": *

= Design Patterns
== Strategy Pattern
Repository-Interfaces als austauschbare Persistenzstrategie

=== Einsatz im Code

Die Repository-Interfaces `CompanyRepository`, `ProjectRepository`, `ResourceRepository` und `ReservationRepository` (definiert in `application/src/main/java/dhbw/swe/ports/`) bilden die Strategie-Schnittstelle. Die Use Cases sind der Kontext: `BookResourceUseCase` hält eine Referenz auf `ResourceRepository` und ruft ausschließlich `findById()` und `save()` auf, ohne zu wissen, wie diese implementiert sind. Die konkreten Strategien sind die JPA-Adapter in `plugins`, z. B. `ResourceRepositoryAdapter`, der `ResourceRepository` implementiert und PostgreSQL als Persistenztechnologie verwendet.

```java
// Strategie-Interface (application)
public interface ResourceRepository {
    void save(Resource resource);
    Optional<Resource> findById(UUID id);
}

// Kontext (application) – kennt nur das Interface
public class BookResourceUseCase {
    private final ResourceRepository resourceRepository;
    ...
}

// Konkrete Strategie (plugins)
public class ResourceRepositoryAdapter implements ResourceRepository { ... }
```

Die Verdrahtung (Klient im Sinne des Musters) erfolgt in `UseCaseConfig` via Dependency Injection: dort wird entschieden, welche konkrete Implementierung übergeben wird.

=== Warum dieses Muster an dieser Stelle

Die Use Cases enthalten fachliche Domänenlogik; die Frage _wie_ Daten gespeichert werden, ist eine technische Entscheidung, die unabhängig davon getroffen werden soll. Das Strategie-Muster kapselt den Persistenzalgorithmus hinter einem Interface und macht ihn austauschbar – ohne den Use Case zu ändern.

=== Wie das Muster den Code verbessert

Ohne das Muster würden die Use Cases direkt von einer konkreten Persistenzklasse (z. B. `ResourceRepositoryAdapter`) abhängen. Jede Änderung der Datenbanktechnologie würde Änderungen in der Domänen- und Anwendungsschicht erfordern. Mit dem Strategie-Muster ist dieser Bereich gegen Änderungen geschlossen (Open/Closed Principle). Testbarkeit und Erweiter-/Austauschbarkeit steigen erheblich: Beispielsweise in Unit-Tests werden einfach In-Memory-Implementierungen des Interfaces übergeben.

=== Vorteile

- Persistenztechnologie kann ausgetauscht werden (z. B. PostgreSQL → MongoDB) ohne Änderung der Use Cases
- Use Cases sind isoliert testbar durch einfache Test-Mocks (In-Memory-Implementierungen)
- Einheitliche Anschlussstelle für neue Persistenz-Implementierungen (z. B. Caching-Adapter)

=== Nachteile

- Mehrere Interfaces und Klassen statt direkter Abhängigkeit erhöhen die Anzahl der Dateien
- Gefahr spekulativer Komplexität, wenn tatsächlich nie eine zweite Implementierung benötigt wird (aber hier haben wir ja schon mit den Unit-Tests eine 2. Implementierung)
