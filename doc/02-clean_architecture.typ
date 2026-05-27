#import "@preview/note-me:0.6.0": *

= Clean Architecture

== Schichtarchitektur: Planung und Begründung

Die Anwendung folgt den Prinzipien der *hexagonalen Architektur*.

=== Schichten der Anwendung

Die Anwendung ist in drei Schichten unterteilt, die als separate Maven-Module umgesetzt sind:

==== 1. Domain
/ Maven-Modul: `domain`

Die Domain enthält die gesamte fachliche Logik der Anwendung (*essential complexity*). Sie kann ohne alle anderen Schichten getestet werden (s. Tests im Modul `domain`)

==== 2. Application (Anwendungsschicht)
/ Maven-Modul: `application` (hängt ab von: `domain`)

Die Interfaces definieren, _was_ die Anwendung von der Außenwelt benötigt, ohne festzulegen, _wie_. Die Anwendungsschicht kennt die Domäne, aber keine äußeren Schichten (Spring Boot, Persistenztechnologien, ...). Technologie-Entscheidungen (z. B. Art der Datenbank) werden nach außen verlagert – entsprechend dem Prinzip: „Gute Entscheidungen werden spät getroffen."

=== Dependency Rule

- Abhängigkeiten zeigen ausschließlich *von außen nach innen*: `application` → `domain`
- Der Kern (`domain`) kennt weder Use Cases noch Controller noch Repositories
- Die Ports (`CompanyRepository` etc.) sind Interfaces in der `application`-Schicht - konkrete Implementierungen sind irrelevant
- Durchgesetzt durch Maven-Modulabhängigkeiten: `domain` hat keine Abhängigkeiten, `application` hängt nur von `domain` ab

=== Zusammenfassung

#table(
  columns: (auto, auto, auto),
  [*Schicht*], [*Maven-Modul*], [*Verantwortung*],
  [Domain], [`domain`], [Fachliche Regeln, Entitäten, Value Objects, Domain Services],
  [Application], [`application`], [Anwendungsfälle, Port-Interfaces],
)

