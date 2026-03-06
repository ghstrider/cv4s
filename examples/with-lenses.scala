//> using scala 3.3.7
//> using dep io.github.ghstrider::cv4s::0.0-SNAPSHOT
//> using dep dev.optics::monocle-core::3.3.0
//> using dep dev.optics::monocle-macro::3.3.0

import cv4s.*
import io.circe.parser.*
import io.circe.syntax.*
import Resume.given
import monocle.macros.GenLens
import monocle.std.option.some
import monocle.std.list.each
import monocle.Traversal

// Reusable optics for cv4s types
object ResumeOptics:
  val _basics   = GenLens[Resume](_.basics) andThen some
  val _work     = GenLens[Resume](_.work) andThen some
  val _skills   = GenLens[Resume](_.skills) andThen some
  val _education = GenLens[Resume](_.education) andThen some

  val _name     = GenLens[Basics](_.name) andThen some
  val _label    = GenLens[Basics](_.label) andThen some
  val _location = GenLens[Basics](_.location) andThen some
  val _city     = GenLens[Location](_.city) andThen some

  // Composed optics for deep access
  val _resumeName = _basics andThen _name
  val _resumeCity = _basics andThen _location andThen _city

  // Traversals across lists
  val _allSkillNames: Traversal[Resume, String] =
    _skills andThen each[List[Skill], Skill] andThen GenLens[Skill](_.name) andThen some

  val _allWorkHighlights: Traversal[Resume, String] =
    _work andThen each[List[Work], Work] andThen GenLens[Work](_.highlights) andThen some andThen each[List[String], String]

val resumeJson = """{
  "basics": {
    "name": "Richard Hendriks",
    "label": "Programmer",
    "email": "richard.hendriks@mail.com",
    "location": {
      "city": "San Francisco",
      "countryCode": "US"
    }
  },
  "work": [
    {
      "name": "Pied Piper",
      "position": "CEO/President",
      "startDate": "2013-12-01",
      "endDate": "2014-12-01",
      "highlights": ["Built an algorithm for compression", "Won Techcrunch Disrupt"]
    }
  ],
  "skills": [
    {"name": "Web Development", "level": "Master", "keywords": ["HTML", "CSS", "Javascript"]},
    {"name": "Compression", "level": "Master", "keywords": ["Mpeg", "MP4", "GIF"]}
  ]
}"""

@main def withLenses(): Unit =
  import ResumeOptics.*

  val resume = decode[Resume](resumeJson) match
    case Left(error)   => sys.error(s"Failed to parse: $error")
    case Right(resume) => resume

  // --- READ with lenses ---
  println("=== Reading with Lenses ===")
  println(s"Name: ${_resumeName.getOption(resume).getOrElse("N/A")}")
  println(s"City: ${_resumeCity.getOption(resume).getOrElse("N/A")}")
  println(s"All skill names: ${_allSkillNames.getAll(resume)}")
  println(s"All highlights:  ${_allWorkHighlights.getAll(resume)}")

  // --- MODIFY with lenses (no nested .copy!) ---
  println("\n=== Modifying with Lenses ===")

  // Update name
  val renamed = _resumeName.replace("Richard H.")(resume)
  println(s"Renamed: ${_resumeName.getOption(renamed).getOrElse("N/A")}")

  // Relocate to a new city
  val relocated = _resumeCity.replace("New York")(resume)
  println(s"Relocated to: ${_resumeCity.getOption(relocated).getOrElse("N/A")}")

  // Uppercase all highlights
  val shouting = _allWorkHighlights.modify(_.toUpperCase)(resume)
  println(s"Shouting highlights: ${_allWorkHighlights.getAll(shouting)}")

  // --- Compare: lenses vs manual copy ---
  println("\n=== Lenses vs Manual Copy ===")

  // With lenses - one liner
  val v1 = _resumeCity.replace("Berlin")(resume)

  // Without lenses - nested copy
  val v2 = resume.copy(
    basics = resume.basics.map(b =>
      b.copy(location = b.location.map(l =>
        l.copy(city = Some("Berlin"))
      ))
    )
  )

  println(s"Lens result:   ${_resumeCity.getOption(v1)}")
  println(s"Manual result: ${_resumeCity.getOption(v2)}")
  println(s"Both equal:    ${v1 == v2}")

  // --- Roundtrip back to JSON ---
  println("\n=== Roundtrip to JSON ===")
  val modified = (_resumeName.replace("Jane Doe") andThen _resumeCity.replace("Tokyo"))(resume)
  println(modified.asJson.spaces2)
