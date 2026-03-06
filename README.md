# cv4s

A Scala 3 library for parsing and working with [JSON Resume](https://jsonresume.org/schema) files using [Circe](https://circe.github.io/circe/).

Cross-published for **JVM**, **Scala.js**, and **Scala Native**.

## Installation

```scala
// build.sbt
libraryDependencies += "io.github.ghstrider" %%% "cv4s" % "<version>"
```

Or with scala-cli:

```scala
//> using dep io.github.ghstrider::cv4s::<version>
```

## Quick Start

```scala
import cv4s.*
import io.circe.parser.*
import io.circe.syntax.*
import Resume.given

// Parse a resume.json
val json = scala.io.Source.fromFile("resume.json").mkString
val resume = decode[Resume](json) // Either[Error, Resume]

// Access fields
val name   = resume.toOption.flatMap(_.basics).flatMap(_.name)
val skills = resume.toOption.flatMap(_.skills).getOrElse(Nil)

// Encode back to JSON
resume.foreach(r => println(r.asJson.spaces2))
```

All fields are `Option`, so partial/incomplete resumes parse without error — even `decode[Resume]("{}")` works.

## Available Types

| Type | Key Fields |
|------|-----------|
| `Resume` | `basics`, `work`, `education`, `skills`, `projects`, `awards`, `certificates`, `publications`, `languages`, `interests`, `references`, `volunteer`, `meta` |
| `Basics` | `name`, `label`, `email`, `phone`, `url`, `summary`, `image`, `location`, `profiles` |
| `Location` | `address`, `city`, `countryCode`, `region`, `postalCode` |
| `Profile` | `network`, `username`, `url` |
| `Work` | `name`, `position`, `location`, `description`, `url`, `startDate`, `endDate`, `summary`, `highlights` |
| `Volunteer` | `organization`, `position`, `url`, `startDate`, `endDate`, `summary`, `highlights` |
| `Education` | `institution`, `url`, `area`, `studyType`, `startDate`, `endDate`, `score`, `courses` |
| `Award` | `title`, `date`, `awarder`, `summary` |
| `Certificate` | `name`, `date`, `issuer`, `url` |
| `Publication` | `name`, `publisher`, `releaseDate`, `url`, `summary` |
| `Skill` | `name`, `level`, `keywords` |
| `Language` | `language`, `fluency` |
| `Interest` | `name`, `keywords` |
| `Reference` | `name`, `reference` |
| `Project` | `name`, `description`, `highlights`, `keywords`, `startDate`, `endDate`, `url`, `roles`, `entity`, `type` |
| `Meta` | `canonical`, `version`, `lastModified` |

## Examples

Runnable examples are in the [`examples/`](examples/) directory. Run them with [scala-cli](https://scala-cli.virtuslab.org/):

### Simple — Parse and query a resume

```bash
scala-cli examples/simple.scala
```

```scala
import cv4s.*
import io.circe.parser.*
import Resume.given

val resume = decode[Resume](json).toOption.get

// Access nested fields
val name  = resume.basics.flatMap(_.name).getOrElse("Unknown")
val city  = resume.basics.flatMap(_.location).flatMap(_.city).getOrElse("N/A")
val skills = resume.skills.getOrElse(Nil)

// Query for decision-making
val hasExperience = resume.work.exists(_.size >= 3)
val knowsScala = skills.exists(s =>
  s.name.contains("Scala") || s.keywords.exists(_.contains("Scala"))
)
```

### With Lenses — Use Monocle for deep access and modification

```bash
scala-cli examples/with-lenses.scala
```

```scala
import cv4s.*
import monocle.macros.GenLens
import monocle.std.option.some

// Define optics
val _basics = GenLens[Resume](_.basics) andThen some
val _name   = GenLens[Basics](_.name) andThen some
val _city   = GenLens[Location](_.city) andThen some
val _location = GenLens[Basics](_.location) andThen some

val _resumeName = _basics andThen _name
val _resumeCity = _basics andThen _location andThen _city

// Read deeply nested values
val name = _resumeName.getOption(resume)  // Some("Richard Hendriks")
val city = _resumeCity.getOption(resume)  // Some("San Francisco")

// Modify without nested .copy() chains
val relocated = _resumeCity.replace("New York")(resume)
```

See [examples/simple.scala](examples/simple.scala) and [examples/with-lenses.scala](examples/with-lenses.scala) for the full runnable code.

## License

Licensed under the [Apache License 2.0](LICENSE).
