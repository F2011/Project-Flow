= Clean Architecture

== Schichtarchitektur

Die Anwendung folgt den Prinzipien der Clean Architecture (hexagonale Architektur). Sie ist in drei Maven-Module unterteilt, die den Schichten des Architekturmodells entsprechen:

#table(
  columns: (auto, auto, auto),
  [*Schicht*], [*Maven-Modul*], [*Verantwortung*],
  [Domain Code], [`domain`], [Fachliche Regeln, Entitäten, Value Objects],
  [Application Code], [`application`], [Use Cases, Port-Interfaces],
  [Plugins], [`plugins`], [Persistenz, REST-Controller, Spring-Konfiguration],
)

=== Kern und Peripherie

Die Domain bildet den Kern der Anwendung und enthält die essential complexity – die fachliche Logik, die sich selten ändert. Sie ist in reinem Java implementiert, ohne Abhängigkeiten zu Frameworks oder Datenbanktechnologien.

Die Plugins-Schicht bildet die Peripherie: Sie enthält die accidental complexity (Persistenz, HTTP, Spring Boot) und ist damit leichter austauschbar.

=== Dependency Rule

Abhängigkeiten zeigen ausschließlich von außen nach innen: `plugins` → `application` → `domain`. Der Kern-Code hängt nie von Plugins ab.

Die inneren Schichten definieren Interfaces (Repository-Ports in `application`); die äußeren Schichten implementieren diese (Adapter in `plugins`). Dies entspricht dem Prinzip der Dependency Inversion.

Technologie-Entscheidungen (Datenbankwahl, Framework) werden damit nach außen verlagert – entsprechend dem Prinzip: „Gute Entscheidungen werden spät getroffen."
