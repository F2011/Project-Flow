#let rule(body) = text(style: "italic", body)

= Domain Driven Design

== Ubiquitous Language
Die Fachlichkeit der Problemdomäne ist bereits in der Themenanmeldung angerissen und wird hier in Form von Definitionen der zentralen Begriffe spezifiziert:

=== Unternehmen
- Wurzelelement der Domäne
- Besitzer von Projekten und Ressourcen

=== Projekt
- Arbeitseinheit mit Budget und allokierten Ressourcen
- #rule[Ein Projekt darf nur angelegt werden, wenn es das Budget des Unternehmens oder des Eltern-Projektes nicht übersteigt]

=== Teilprojekt
- ein Projekt, das einen Teil eines größeren Projektes abbildet
- einzige Besonderheit eines Teilprojektes ist, dass es ein Eltern-Projekt hat
- #rule[Ein Teilprojekt darf nicht vor seinem Eltern-Projekt starten und nicht nach diesem enden.]

=== Ressource
- abstraktes Konzept: Mitarbeiter oder Raum

=== Mitarbeiter
- menschliche Ressource mit Qualifikationen

=== Raum
- physikalische Ressource

=== Allokation
- Zuordnung einer Ressource zu einem Projekt

=== Reservierung
- zeitlich begrenzte Allokation einer Ressource
- #rule[Funktioniert nur, wenn die Ressource im Zeitraum der Reservierung verfügbar ist]


=== Budget
- finanzieller Rahmen auf Projektebene, der NICHT überschritten werden darf
- #rule[Darf nicht negativ sein]

=== Qualifikation
- Anforderung, die ein Mitarbeiter erfüllen muss, um für ein Projekt reserviert zu werden

== DDD Muster

=== Value Objects

`Money` ist als Value Object implementiert, weil:
- es keine eigene Identität besitzt – zwei `Money`-Objekte mit gleichem Betrag und Währung sind fachlich identisch
- es unveränderlich ist (`final`-Klasse, alle Felder `final`)
- arithmetische Operationen neue Instanzen zurückgeben

`TimeRange` ist als Value Object implementiert, weil:
- ein Zeitraum keine eigene Identität besitzt – er ist vollständig durch Start und Ende definiert
- es unveränderlich ist (`final`-Klasse, alle Felder `final`)
- der Konstruktor validiert, dass `end` nach `start` liegt
- es fachliche Verhaltensmethoden enthält: `overlaps(TimeRange)`, `includes(TimeRange)`, `getDuration()`

`Reservation` ist als Value Object implementiert, weil:
- eine Reservierung fachlich durch ihre Inhalte (Ressource, Zeitraum, Projekt) vollständig bestimmt ist
- sie keine eigene Lebensgeschichte besitzt

=== Entities

`Company` ist als Entity implementiert, weil:
- ein Unternehmen eine eindeutige Identität (UUID) besitzt, die es über Zustandsänderungen hinweg identifizierbar macht
- zwei Unternehmen mit gleichem Namen fachlich unterschiedliche Objekte sind, solange ihre IDs verschieden sind
- es veränderlich ist: Projekte und Ressourcen werden hinzugefügt und entfernt

`Project` ist als Entity implementiert, weil:
- Projekte eine eigene Identität (UUID) und eine Lebensgeschichte haben
- Gleichheit auf der UUID basiert, nicht auf Name oder Budget

`Employee` und `Room` sind als Entities implementiert, weil:
- beide eine eindeutige Identität (UUID, geerbt von `Resource`) besitzen
- sie über die Zeit verändert werden (Reservierungen werden hinzugefügt/entfernt, Qualifikationen ändern sich)

=== Aggregates

`Company` ist Aggregate Root, weil:
- es seine Projekte und Ressourcen besitzt und für die Aufrechterhaltung der fachlichen Invarianten verantwortlich ist
- `createProject(name, budget, duration)` prüft, ob das Unternehmensbudget überschritten wird, bevor das Projekt erstellt wird – ein Umgehen dieser Prüfung von außen ist nicht möglich
- alle Objekte innerhalb des Aggregats (`Project`, `Employee`, `Room`) ausschließlich über `Company` erzeugt werden

=== Repositories

`CompanyRepository`, `ProjectRepository`, `ResourceRepository` und `ReservationRepository` sind als Repositories implementiert, weil:
- sie den Datenzugriff abstrahieren und den Use Cases ermöglichen, Aggregate Roots zu laden und zu speichern
- sie als Interfaces in der `application`-Schicht definiert sind, ohne von einer konkreten Persistenztechnologie abhängig zu sein
- die Implementierungen in der `plugins`-Schicht liegen und damit die Grenze zwischen Domänenmodell und Infrastruktur bilden

=== Domain Services

`ReservationService` ist als Domain Service implementiert, weil:
- die fachliche Logik einer Reservierungsprüfung mehrere Entitäten übergreift und nicht sinnvoll einer einzelnen Entität zugeordnet werden kann
- der Service drei übergreifende Bedingungen prüft: Verfügbarkeit (`resource.isAvailableDuring(timeRange)`), Qualifikation des Mitarbeiters für das Projekt, und Budgetdeckung des Projekts
- diese Logik fachlich zur Domäne gehört, aber kein natürlicher Teil einer einzelnen Entität ist
