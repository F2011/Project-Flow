#import "@preview/zebraw:0.6.3": zebraw

= Refactoring
- Identifizieren, nennen und begründen Sie mindestens 4 Code Smells, die
  - aktuell noch im Code existieren oder
  - die Sie im Laufe der Entwicklung identifiziert und beseitigt haben (dann mit Verweis auf entsprechenden commit)
  - Orientieren Sie sich an den in der Vorlesung genannten Smells, gerne auch andere Smells, zum Beispiel https://refactoring.guru/refactoring/smells\ #text(fill: red)[Achtung: vermeiden Sie “unused import”, “unused variable”, “parameter could be final” und ähnliche, “schwache” Smells]
- Nennen und begründen Sie zwei konkrete Refactorings, die Sie entweder
  - im Laufe des Projektes ausgeführt haben oder
  - die Sie zur Verbesserung des Projektes am aktuellen Code durchführen würden.
  - Orientieren Sie sich an den in der Vorlesung genannten Refactorings. Gerne dürfen Sie auch andere Refactorings aus seriösen Quellen verwenden, zum Beispiel https://refactoring.guru/refactoring/techniques

== Code Smells

=== Duplicated Code

In `ReservationService.reserveResource` wird die Berechnung der Kosten einer Reservierung (Stunden × Stundensatz) zweimal implementiert (s. `ReservationService.java` in Commit: #link("https://github.com/F2011/Project-Flow/blob/f17b13ddc76c0d98d322b0f47050cae21664aa2c/src/projectflow/domain/src/main/java/dhbw/swe/services/ReservationService.java#L27")[f17b13d])
#let duplication-code = ```java
long hours = (long) Math.ceil(timeRange.getDuration().toMinutes() / 60.0);
Money reservationCost = resource.getCostsPerHour().multiply((int) hours);
Money alreadyAllocated = project.getReservations().stream().map(r -> {
int h = (int) Math.ceil(r.getTimeRange().getDuration().toMinutes() / 60.0);
```
#zebraw(numbering-offset: 27, line-range: range(1, 3), duplication-code)
#zebraw(numbering-offset: 27, line-range: range(4, 6), duplication-code)

Beide Stellen berechnen identisch `ceil(duration.toMinutes() / 60.0)` und rufen anschließend `getCostsPerHour().multiply(h)` auf.

=== Long Method

`ReservationService.reserveResource` vereint drei eigenständige fachliche Prüfungen in einer einzigen Methode (s. `ReservationService.java` Zeile 13–42, Commit: #link("https://github.com/F2011/Project-Flow/blob/f17b13ddc76c0d98d322b0f47050cae21664aa2c/src/projectflow/domain/src/main/java/dhbw/swe/services/ReservationService.java#L13")[f17b13d]):

- Verfügbarkeitsprüfung (Zeile 14–17): Ist die Ressource im Zeitraum frei?
- Qualifikationsprüfung (Zeile 19–26): Hat der Mitarbeiter die erforderlichen Qualifikationen?
- Budgetprüfung (Zeile 28–36): Übersteigen die Gesamtkosten das Projektbudget?

Jeder Block ist semantisch abgeschlossen. Die Komplexität erschwert Verständlichkeit und Testbarkeit, da alle drei Pfade gemeinsam getestet werden müssen.

=== Inappropriate Intimacy

`CompanyController` hängt direkt von der konkreten Infrastrukturklasse `CompanyRepositoryAdapter` ab, anstatt das Port-Interface `CompanyRepository` zu verwenden (s. `CompanyController.java` Zeile 25–30, Commit: #link("https://github.com/F2011/Project-Flow/blob/f17b13ddc76c0d98d322b0f47050cae21664aa2c/src/projectflow/plugins/src/main/java/dhbw/swe/plugins/web/CompanyController.java#L24")[f17b13d]):

#let intimacy-code = ```java
private final CompanyRepositoryAdapter companyRepository;
...

public CompanyController(CompanyRepositoryAdapter companyRepository,
        ...) {
    this.companyRepository = companyRepository;
```
#zebraw(numbering-offset: 24, intimacy-code)

Ein Controller (Peripherie) kennt damit die interne Implementierung der Persistenzschicht. Das verletzt das Dependency Inversion Principle: Die höhere Schicht sollte nur vom Interface abhängen. Außerdem bricht es die Clean-Architecture-Regel, dass `web` und `persistence` voneinander unabhängige Adapter sind.

=== Switch Statements

In `DomainMapper` wird die Fallunterscheidung nach Ressourcentyp (`Employee` vs. `Room`) zweimal mit identischer `instanceof`-Kette implementiert – in `toDomainResource` (Zeile 30–45) und in `toJpaResource` (Zeile 102–123), Commit: #link("https://github.com/F2011/Project-Flow/blob/f17b13ddc76c0d98d322b0f47050cae21664aa2c/src/projectflow/plugins/src/main/java/dhbw/swe/plugins/persistence/DomainMapper.java#L30")[f17b13d]:

#let switch-code-1 = ```java
if (e instanceof EmployeeJpaEntity emp) { ... return new Employee(...); }
if (e instanceof RoomJpaEntity room)    { ... return new Room(...); }
throw new IllegalStateException(...);
```
#let switch-code-2 = ```java
if (resource instanceof Employee emp) { ... return employeeEntity; }
if (resource instanceof Room room)    { ... return roomEntity; }
throw new IllegalStateException(...);
```
#zebraw(numbering-offset: 29, switch-code-1)
#zebraw(numbering-offset: 101, switch-code-2)

Jede neue Ressourcenart (z. B. wenn man Fahrzeuge einführen wollte) erfordert Änderungen an beiden Methoden.\
Mögliche Lösung: Polymorphie ersetzt die Konditional-Struktur – die Konvertierungslogik wird in typspezifische Mapper-Klassen ausgelagert, sodass neue Typen ohne Änderung des bestehenden Codes ergänzt werden können.

