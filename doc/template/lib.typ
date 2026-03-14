#import "@preview/codelst:2.0.2": *
#import "@preview/hydra:0.6.2": hydra
#import "locale.typ": APPENDIX, REFERENCES, TABLE_OF_CONTENTS
#import "titlepage.typ": *
#import "declaration-of-authorship.typ": *


#let clean-dhbw(
  title: none,
  authors: (:),
  language: none,
  at-university: none,
  type-of-thesis: none,
  show-declaration-of-authorship: true,
  show-table-of-contents: true,
  show-abstract: true,
  abstract: none,
  appendix: none,
  declaration-of-authorship-content: none,
  titlepage-content: none,
  university: none,
  university-location: none,
  university-short: none,
  class: none,
  course: none,
  semester: none,
  city: none,
  supervisor: (:),
  date: none,
  date-format: "[day].[month].[year]",
  math-numbering: "(1)",
  logo-left: image("assets/dhbw.svg"),
  logo-right: none,
  ignored-link-label-keys-for-highlighting: (),
  body,
) = {
  // ---------- Fonts & Related Measures ---------------------------------------

  let body-font = "Source Serif 4"
  let body-size = 11pt
  let heading-font = "Source Sans 3"
  let h1-size = 40pt
  let h2-size = 16pt
  let h3-size = 11pt
  let h4-size = 11pt
  let page-grid = 16pt // vertical spacing on all pages

  // ---------- Basic Document Settings ---------------------------------------

  set document(title: title, author: authors.map(author => author.name))
  let many-authors = authors.len() > 3
  let in-frontmatter = state("in-frontmatter", true) // to control page number format in frontmatter
  let in-body = state("in-body", true) // to control heading formatting in/outside of body

  // customize captions
  set figure.caption(separator: [ -- ], position: bottom)
  show figure.caption: set text(font: heading-font, size: body-size)

  // math numbering
  set math.equation(numbering: math-numbering)

  // show links in dark blue
  show link: set text(fill: blue.darken(40%))

  // ========== TITLEPAGE ========================================

  if (titlepage-content != none) {
    titlepage-content
  } else {
    titlepage(
      authors,
      date,
      heading-font,
      language,
      logo-left,
      logo-right,
      many-authors,
      supervisor,
      title,
      type-of-thesis,
      university,
      university-location,
      at-university,
      date-format,
      university-short,
      page-grid,
      class,
      course,
      semester,
    )
  }
  counter(page).update(1)

  // ---------- Page Setup ---------------------------------------

  // adapt body text layout to basic measures
  set text(
    font: body-font,
    lang: language,
    size: body-size - 0.5pt, // 0.5pt adjustment because of large x-hight
    top-edge: 0.75 * body-size,
    bottom-edge: -0.25 * body-size,
  )
  set par(
    spacing: page-grid,
    leading: page-grid - body-size,
    justify: true,
  )

  set page(
    margin: (top: 4cm, bottom: 3cm, left: 4cm, right: 3cm),
    header: grid(
      columns: (1fr, 1fr),
      align: (left, right),
      row-gutter: 0.5em,
      smallcaps(text(font: heading-font, size: body-size, context {
        hydra(
          1,
          display: (_, it) => it.body,
          use-last: true,
          skip-starting: false,
        )
      })),
      text(font: heading-font, size: body-size, number-type: "lining", context {
        if in-frontmatter.get() {
          counter(page).display("i") // roman page numbers for the frontmatter
        } else {
          counter(page).display("1") // arabic page numbers for the rest of the document
        }
      }),
      grid.cell(colspan: 2, line(length: 100%, stroke: 0.5pt)),
    ),
    header-ascent: page-grid,
  )

  // ========== FRONTMATTER ========================================

  // ---------- Heading Format (Part I) ---------------------------------------

  show heading: set text(weight: "bold", fill: luma(80), font: heading-font)
  show heading.where(level: 1): it => {
    v(2 * page-grid) + text(size: 2 * page-grid, it)
  }

  // ---------- Abstract ---------------------------------------

  if (show-abstract and abstract != none) {
    heading(level: 1, numbering: none, outlined: false, ABSTRACT.at(language))
    text(abstract)
    pagebreak()
  }

  // ---------- ToC (Outline) ---------------------------------------

  // top-level TOC entries in bold without filling
  show outline.entry.where(level: 1): it => {
    set block(above: page-grid)
    set text(font: heading-font, weight: "semibold", size: body-size)
    link(
      it.element.location(), // make entry linkable
      it.indented(it.prefix(), it.body() + box(width: 1fr) + it.page()),
    )
  }

  // other TOC entries in regular with adapted filling
  show outline.entry.where(level: 2).or(outline.entry.where(level: 3)): it => {
    set block(above: page-grid - body-size)
    set text(font: heading-font, size: body-size)
    link(
      it.element.location(), // make entry linkable
      it.indented(
        it.prefix(),
        it.body()
          + "  "
          + box(width: 1fr, repeat([.], gap: 2pt), baseline: 30%)
          + "  "
          + it.page(),
      ),
    )
  }

  if (show-table-of-contents) {
    outline(
      title: TABLE_OF_CONTENTS.at(language),
      indent: auto,
      depth: 3,
    )
  }

  in-frontmatter.update(false) // end of frontmatter
  counter(page).update(0) // so the first chapter starts at page 1 (now in arabic numbers)

  // ========== DOCUMENT BODY ========================================

  // ---------- Table Format ---------------------------------------

  set table(stroke: (x: none, y: 0.5pt))
  show table: set text(font: heading-font, size: body-size)
  show table.cell.where(y: 0): set text(weight: "bold")

  // ---------- Heading Format (Part II: H1-H4) ---------------------------------------

  set heading(numbering: "1.1.1")

  show heading: it => {
    set par(leading: 4pt, justify: false)
    text(it, top-edge: 0.75em, bottom-edge: -0.25em)
    v(page-grid, weak: true)
  }

  show heading.where(level: 1): it => {
    set par(leading: 0pt, justify: false)
    pagebreak()
    context {
      if in-body.get() {
        v(page-grid * 10)
        place(
          // place heading number prominently at the upper right corner
          top + right,
          dx: 9pt, // slight adjustment for optimal alignment with right margin
          text(
            counter(heading).display(),
            top-edge: "bounds",
            size: page-grid * 10,
            weight: 900,
            luma(235),
          ),
        )
        text(
          // heading text on separate line
          it.body,
          size: h1-size,
          top-edge: 0.75em,
          bottom-edge: -0.25em,
        )
      } else {
        v(2 * page-grid)
        text(
          size: 2 * page-grid,
          counter(heading).display() + h(0.5em) + it.body,
        ) // appendix
      }
    }
  }

  show heading.where(level: 2): it => { v(16pt) + text(size: h2-size, it) }
  show heading.where(level: 3): it => { v(16pt) + text(size: h3-size, it) }
  show heading.where(level: 4): it => {
    v(16pt) + smallcaps(text(size: h4-size, weight: "semibold", it.body))
  }

  // ---------- Body Text ---------------------------------------

  body

  // ========== APPENDIX ========================================

  in-body.update(false)
  set heading(numbering: "A.1")
  counter(heading).update(0)

  // ---------- Appendix (other contents) ---------------------------------------

  if (appendix != none) {
    // the user has to provide heading(s)
    appendix
  }

  // ========== LEGAL BACKMATTER ========================================

  set heading(numbering: it => h(-18pt) + "", outlined: false)

  // ---------- Declaration Of Authorship ---------------------------------------

  if (show-declaration-of-authorship) {
    declaration-of-authorship(
      authors,
      title,
      declaration-of-authorship-content,
      date,
      language,
      many-authors,
      at-university,
      city,
      date-format,
    )
  }
}
