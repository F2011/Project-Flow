#let rule(body) = text(style: "italic", body)

= Domain Driven Design
- Analyse der Ubiquitous Language
  - Analysieren Sie die Fachlichkeit Ihrer Problemdomäne, indem Sie die relevanten Begriffe und deren *fachliche Bedeutung, Aufgaben und Regeln dokumentieren*
- Verwendung taktischer Muster des DDD:
  - Alle genannten Muster des DDD sind im Source Code zu verwenden (Value Objects, Entities, Aggregates, Repositories, Domain Services)
- Analyse und Begründung der verwendeten Muster
  - Begründen Sie jedes einzelne, oben genannte Muster anhand von je einem konkreten Beispiel aus Ihrem Source Code
  - „XX ist als Value Object implementiert, weil...“

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

// === finanzieller Spielraum
// - finanzieller Rahmen in einem bestimmten Zeitraum, der NICHT überschritten werden darf

=== Budget
- finanzieller Rahmen auf Projektebene, der NICHT überschritten werden darf
- #rule[Darf nicht negativ sein]

=== Qualifikation
- Anforderung, die ein Mitarbeiter erfüllen muss, um für ein Projekt reserviert zu werden

== DDD Muster
=== Value Objects
`Money` ist als Value Object implementiert, weil //TODO
`TimeRange` ist als Value Object implementiert, weil //TODO

=== Entities

=== Aggregates

=== Repositories

=== Domain Services
