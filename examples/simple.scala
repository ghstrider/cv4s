//> using scala 3.3.7
//> using dep io.github.ghstrider::cv4s::0.0-SNAPSHOT

import cv4s.*
import io.circe.parser.*
import Resume.given

val resumeJson = """{
  "basics": {
    "name": "Richard Hendriks",
    "label": "Programmer",
    "email": "richard.hendriks@mail.com",
    "location": {
      "city": "San Francisco",
      "countryCode": "US"
    },
    "profiles": [
      {"network": "GitHub", "username": "richardh"}
    ]
  },
  "work": [
    {
      "name": "Pied Piper",
      "position": "CEO/President",
      "startDate": "2013-12-01",
      "endDate": "2014-12-01",
      "highlights": ["Built an algorithm for compression"]
    }
  ],
  "skills": [
    {"name": "Web Development", "level": "Master", "keywords": ["HTML", "CSS", "Javascript"]},
    {"name": "Compression", "level": "Master", "keywords": ["Mpeg", "MP4", "GIF"]}
  ],
  "education": [
    {
      "institution": "University of Oklahoma",
      "area": "Information Technology",
      "studyType": "Bachelor"
    }
  ]
}"""

@main def simple(): Unit =
  decode[Resume](resumeJson) match
    case Left(error) =>
      println(s"Failed to parse resume: $error")

    case Right(resume) =>
      // Access basic info
      val name = resume.basics.flatMap(_.name).getOrElse("Unknown")
      val label = resume.basics.flatMap(_.label).getOrElse("N/A")
      val city = resume.basics.flatMap(_.location).flatMap(_.city).getOrElse("N/A")
      println(s"Name:  $name")
      println(s"Label: $label")
      println(s"City:  $city")

      // List all skills
      val skills = resume.skills.getOrElse(Nil)
      println(s"\nSkills (${skills.size}):")
      skills.foreach { s =>
        val keywords = s.keywords.getOrElse(Nil).mkString(", ")
        println(s"  - ${s.name.getOrElse("?")} [${s.level.getOrElse("?")}]: $keywords")
      }

      // List work experience
      val work = resume.work.getOrElse(Nil)
      println(s"\nWork Experience (${work.size}):")
      work.foreach { w =>
        val period = s"${w.startDate.getOrElse("?")} to ${w.endDate.getOrElse("present")}"
        println(s"  - ${w.position.getOrElse("?")} at ${w.name.getOrElse("?")} ($period)")
        w.highlights.getOrElse(Nil).foreach(h => println(s"    * $h"))
      }

      // List education
      val education = resume.education.getOrElse(Nil)
      println(s"\nEducation (${education.size}):")
      education.foreach { e =>
        println(s"  - ${e.studyType.getOrElse("?")} in ${e.area.getOrElse("?")} at ${e.institution.getOrElse("?")}")
      }

      // Social profiles
      val profiles = resume.basics.flatMap(_.profiles).getOrElse(Nil)
      println(s"\nProfiles (${profiles.size}):")
      profiles.foreach { p =>
        println(s"  - ${p.network.getOrElse("?")}: ${p.username.getOrElse("?")}")
      }
