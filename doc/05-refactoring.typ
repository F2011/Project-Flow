#import "@preview/zebraw:0.6.3": zebraw

= Refactoring

== Code Smells
Die Code Smells sind jeweils auf Commit #link("https://github.com/F2011/Project-Flow/tree/f17b13ddc76c0d98d322b0f47050cae21664aa2c")[f17b13d] gefunden worden und die beiden Code Smells _Duplicated Code_ und _Long Method_ wurden anschließend durch eine alternative Implementierung entfernt.

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

Behoben durch Extract Method: Die duplizierte Logik wurde in die private Methode `calculateCost(Resource, TimeRange)` extrahiert, die an beiden Stellen aufgerufen wird.

=== Long Method

`ReservationService.reserveResource` vereint drei eigenständige fachliche Prüfungen in einer einzigen Methode (s. `ReservationService.java` Zeile 13–42, Commit: #link("https://github.com/F2011/Project-Flow/blob/f17b13ddc76c0d98d322b0f47050cae21664aa2c/src/projectflow/domain/src/main/java/dhbw/swe/services/ReservationService.java#L13")[f17b13d]):

- Verfügbarkeitsprüfung (Zeile 14–17): Ist die Ressource im Zeitraum frei?
- Qualifikationsprüfung (Zeile 19–26): Hat der Mitarbeiter die erforderlichen Qualifikationen?
- Budgetprüfung (Zeile 28–36): Übersteigen die Gesamtkosten das Projektbudget?

Jeder Block ist semantisch abgeschlossen. Die Komplexität erschwert Verständlichkeit und Testbarkeit, da alle drei Pfade gemeinsam getestet werden müssen.

Behoben durch Extract Method: Jede Prüfung wurde in eine eigene private Methode extrahiert (`checkAvailability`, `checkQualifications`, `checkBudget`). `reserveResource` besteht damit nur noch aus drei lesbaren Methodenaufrufen.

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

Behoben durch Ersetzen von `CompanyRepositoryAdapter` durch `CompanyRepository`.

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

