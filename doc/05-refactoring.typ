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

In `ReservationService.reserveResource` wird die Berechnung der Kosten einer Reservierung (Stunden × Stundensatz) zweimal implementiert (s. `ReservationService.java` in Commit: #link("https://github.com/F2011/Project-Flow/blob/f17b13ddc76c0d98d322b0f47050cae21664aa2c/src/projectflow/domain/src/main/java/dhbw/swe/services/ReservationService.java")[f17b13d])
#let duplication-code =```java
long hours = (long) Math.ceil(timeRange.getDuration().toMinutes() / 60.0);
Money reservationCost = resource.getCostsPerHour().multiply((int) hours);
Money alreadyAllocated = project.getReservations().stream().map(r -> {
int h = (int) Math.ceil(r.getTimeRange().getDuration().toMinutes() / 60.0);
```
#zebraw(numbering-offset: 27, line-range: range(1, 3), duplication-code)
#zebraw(numbering-offset: 27, line-range: range(4, 6), duplication-code)

Beide Stellen berechnen identisch `ceil(duration.toMinutes() / 60.0)` und rufen anschließend `getCostsPerHour().multiply(h)` auf.

=== Long Method

`ReservationService.reserveResource` vereint drei eigenständige fachliche Prüfungen in einer einzigen Methode:

- Verfügbarkeitsprüfung: Ist die Ressource im Zeitraum frei?
- Qualifikationsprüfung: Hat der Mitarbeiter die erforderlichen Qualifikationen?
- Budgetprüfung: Übersteigen die Gesamtkosten das Projektbudget?

Jeder Block ist semantisch abgeschlossen. Die Komplexität erschwert Verständlichkeit und Testbarkeit, da alle drei Pfade gemeinsam getestet werden müssen.

=== Duplicated Code: Feld `costsPerHour` in Subklassen

`Employee` und `Room` deklarieren beide unabhängig voneinander das Feld `costsPerHour` und implementieren jeweils die Methode `getCostsPerHour()`:

#let costs-employee-code = ```java
// Employee.java
private Money costsPerHour;
public Money getCostsPerHour() { return costsPerHour; }
```
#let costs-room-code = ```java
// Room.java
private Money costsPerHour;
public Money getCostsPerHour() { return costsPerHour; }
```
#zebraw(costs-employee-code)
#zebraw(costs-room-code)

Da `getCostsPerHour()` bereits als abstrakte Methode in `Resource` deklariert ist und beide Subklassen sie identisch implementieren, ist das Feld in beiden Klassen redundant. Änderungen an der Kostenlogik müssen an zwei Stellen gepflegt werden. Abhilfe: Feld und Implementierung in die Basisklasse `Resource` hochziehen (Pull Up Field / Pull Up Method).

=== Duplicated Code: Budget-Prüfung in `Company` und `Project`

Die gleiche Budget-Akkumulierungslogik – alle bestehenden Budgets summieren, das neue hinzuaddieren, mit dem Gesamtbudget vergleichen – erscheint sowohl in `Company.createProject` als auch in `Project.createSubProject`:

#let budget-company-code = ```java
// Company.java, Zeile 27–30
Money allocated = projects.stream()
        .map(Project::getBudget)
        .reduce(projectMoney, Money::add);
if (allocated.compareTo(budget) > 0) { ... }
```
#let budget-project-code = ```java
// Project.java, Zeile 36–37
if (this.subProjects.stream().map((Project p) -> p.getBudget())
        .reduce(budget, Money::add).compareTo(this.budget) > 0) { ... }
```
#zebraw(budget-company-code)
#zebraw(budget-project-code)

Beide Stellen lösen dasselbe Problem: Überschreitet das neue Budget zusammen mit den bereits vergebenen Budgets das Limit? Wird die Logik geändert (z. B. reserviertes statt vergebenes Budget berücksichtigen), muss sie an beiden Stellen angepasst werden. Abhilfe: Extract Method `exceedsBudget(List<Money> allocated, Money newBudget, Money limit) : boolean`, die von beiden Klassen aufgerufen wird.

