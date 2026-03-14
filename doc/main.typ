#import "template/lib.typ": *

#show: clean-dhbw.with(
  title: "Thema: Project Flow",
  authors: (
    (
      name: "Leon Fertig",
      student-id: "1142628",
      course: "TINF23B1",
      course-of-studies: "Informatik",
    ),
  ),
  type-of-thesis: "Programmentwurf",
  at-university: false, // if true the company name on the title page and the confidentiality statement are hidden
  date: datetime.today(),
  // glossary: glossary-entries, // displays the glossary terms defined in "glossary.typ"
  language: "de", // en, de
  supervisor: (university: "Mirko Dostmann"),
  university: "Duale Hochschule Baden-Württemberg",
  university-location: "Karlsruhe",
  university-short: "DHBW",
  class: "Software Engineering II",
  course: "TINF23B1",
  semester: "5./6. Semester",
  // for more options check the package documentation (https://typst.app/universe/package/clean-dhbw)
)

#include "01-ddd.typ"
#include "02-clean_architecture.typ"
#include "03-programming_principles.typ"
#include "04-unit_tests.typ"
#include "05-refactoring.typ"
#include "06-design_patterns.typ"
