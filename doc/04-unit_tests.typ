= Unit Tests

Die Unit Tests erfüllen die ATRIP-Regeln: Sie sind:
/ Automatic,: da Maven Ausführung und Auswertung übernimmt;
/ Thorough,: da sowohl Happy-Path als auch Fehlerfälle (z. B. fehlendes Budget, fehlende Qualifikation) abgedeckt sind;
/ Repeatable,: da jeder Test ein frisches `UseCaseTestSetup`-Objekt mit In-Memory-Repositories erzeugt und keinen Zustand braucht;
/ Independent,: da kein Test von einem anderen abhängt oder dessen Zustand verändert; und
/ Professional,: da Testnamen das erwartete Verhalten beschreiben (z. B. `execute_createsReservationOnProjectAndResource`).

Mocks sind enthalten, da die PostgreSQL-Datenbank von einer In-Memory-Implementierungen ersetzt wird.

Unit-Tests für die Use-Cases wurden nicht geschrieben, um sie vollständig abzudecken, sondern um spätere Änderungen sofort sichtbar zu machen (falls der Happy-Path verletzt wird). Als relevante Unit-Tests sind nur die Domain-Unit-Tests zu sehen.
