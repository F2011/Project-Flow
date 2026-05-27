= Programming Principles

== SOLID-Prinzipien

=== Single Responsibility Principle (SRP)

Das Single Responsibility Principle besagt, dass eine Klasse nur genau eine Aufgabe haben soll.

Dies wird zum Beispiel in der Klasse `ReservationService` berücksichtigt: Sie ist ausschließlich für die fachliche Logik rund um Reservierungen zuständig (Verfügbarkeitsprüfung, Qualifikationsprüfung, Budgetprüfung). Persistenz, HTTP-Handling und Use-Case-Orchestrierung liegen in getrennten Klassen. Die Klasse hat damit genau eine Aufgabe: die Buchungsregeln.

=== Open/Closed Principle (OCP)

Das Open/Closed Principle besagt, dass Software-Entitäten offen für Erweiterung, aber geschlossen bezüglich Veränderung sein sollen. Bestehender Code sollte nicht geändert werden müssen, um neues Verhalten hinzuzufügen.

Dies wird in der Repository-Architektur berücksichtigt: Die Use Cases (z. B. `BookResourceUseCase`) arbeiten ausschließlich gegen die Port-Interfaces `ProjectRepository`, `ResourceRepository` und `ReservationRepository`. Eine neue Persistenztechnologie (z. B. Wechsel der Datenbank) erfordert nur eine neue Adapter-Implementierung in `plugins` – der bestehende Use-Case bleibt unverändert. Die offene Schnittstelle ist das Interface.

=== Liskov Substitution Principle (LSP)

Das Liskov Substitution Principle besagt, dass Objekte in einem Programm durch Instanzen ihrer Subtypen ersetzbar sein sollen, ohne die Korrektheit des Programms zu ändern. Ein Subtyp darf die Funktionalität lediglich erweitern, nicht einschränken.

Dies wird in der Vererbungshierarchie `Resource` → `Employee` / `Room` berücksichtigt. Der `ReservationService` arbeitet mit dem Basistyp `Resource` und ruft `resource.isAvailableDuring(timeRange)` sowie `resource.getCostsPerHour()` auf. Beide Subtypen `Employee` und `Room` implementieren `getCostsPerHour()` vollständig und schränken das Verhalten von `Resource` nicht ein – sie können überall dort eingesetzt werden, wo ein `Resource`-Objekt erwartet wird, ohne dass die Korrektheit der Buchungslogik leidet. Außerdem erfüllen sie auch logisch die gleichen Aufgaben in den geerbten Methoden.

=== Interface Segregation Principle (ISP)

Das Interface Segregation Principle besagt, dass mehrere spezifische Interfaces besser sind als ein großes Interface. Ein Client sollte nicht von Details eines Services abhängig sein, die er nicht braucht. Dies unterstützt high cohesion und loose coupling.

Dies wird durch die vier getrennten Repository-Ports berücksichtigt: `CompanyRepository`, `ResourceRepository`, `ProjectRepository` und `ReservationRepository`. Jeder Use-Case hängt nur von den Ports ab, die er tatsächlich benötigt. `BookResourceUseCase` beispielsweise kennt kein `CompanyRepository`, weil es für die Buchungslogik irrelevant ist. Hätte es ein einziges `RepositoryFacade`-Interface gegeben, wäre jeder Use Case von Methoden abhängig, die er nie aufruft.

=== Dependency Inversion Principle (DIP)

Das Dependency Inversion Principle besagt, dass Module höherer Ebenen nicht von Modulen niedrigerer Ebenen abhängen sollen – beide sollen von Abstraktionen abhängen. Die inneren Schichten definieren Interfaces; die äußeren Schichten implementieren diese.

Dies wird konsequent in der gesamten Architektur berücksichtigt. Die Use Cases in `application` definieren ihre Abhängigkeiten ausschließlich über Interfaces (z. B. `ProjectRepository`). Die konkreten Implementierungen (`ProjectRepositoryAdapter`) liegen in `plugins` und sind der `application`-Schicht unbekannt. Die Abhängigkeit zeigt damit von außen nach innen: `plugins` hängt von `application`, nicht umgekehrt. Die Verknüpfung erfolgt in `UseCaseConfig` via Dependency Injection.
