/*
 * Copyright 2026 cv4s contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cv4s

import io.circe.parser._
import io.circe.syntax._

import Resume.given

class ResumeSuite extends munit.FunSuite:

  test("decode minimal resume") {
    val json = """{"basics":{"name":"John Doe","label":"Programmer"}}"""
    val result = decode[Resume](json)
    assert(result.isRight)
    assertEquals(result.toOption.get.basics.flatMap(_.name), Some("John Doe"))
  }

  test("roundtrip encode/decode") {
    val resume = Resume(
      basics = Some(
        Basics(
          name = Some("Jane Smith"),
          email = Some("jane@example.com"),
          profiles = Some(
            List(
              Profile(network = Some("GitHub"), username = Some("janesmith"))
            )
          )
        )
      ),
      skills = Some(
        List(Skill(name = Some("Scala"), keywords = Some(List("FP", "JVM"))))
      )
    )
    val json = resume.asJson
    val decoded = json.as[Resume]
    assertEquals(decoded, Right(resume))
  }

  test("decode empty resume") {
    val result = decode[Resume]("{}")
    assert(result.isRight)
  }

  val sampleResumeJson: String =
    """{
      |  "$schema": "https://raw.githubusercontent.com/jsonresume/resume-schema/v1.0.0/schema.json",
      |  "basics": {
      |    "name": "Richard Hendriks",
      |    "label": "Programmer",
      |    "image": "",
      |    "email": "richard.hendriks@mail.com",
      |    "phone": "(912) 555-4321",
      |    "url": "http://richardhendricks.example.com",
      |    "summary": "Richard hails from Tulsa.",
      |    "location": {
      |      "address": "2712 Broadway St",
      |      "postalCode": "CA 94115",
      |      "city": "San Francisco",
      |      "countryCode": "US",
      |      "region": "California"
      |    },
      |    "profiles": [
      |      {"network": "Twitter", "username": "neutralthoughts", "url": "https://www.twitter.com"},
      |      {"network": "SoundCloud", "username": "dandymusicnl", "url": "https://soundcloud.example.com/dandymusicnl"}
      |    ]
      |  },
      |  "work": [{
      |    "name": "Pied Piper",
      |    "location": "Palo Alto, CA",
      |    "description": "Awesome compression company",
      |    "position": "CEO/President",
      |    "url": "http://piedpiper.example.com",
      |    "startDate": "2013-12-01",
      |    "endDate": "2014-12-01",
      |    "summary": "Pied Piper is a multi-platform technology company.",
      |    "highlights": ["Built an algorithm for compression", "Successfully won Techcrunch Disrupt"]
      |  }],
      |  "volunteer": [{
      |    "organization": "CoderDojo",
      |    "position": "Teacher",
      |    "url": "http://coderdojo.example.com/",
      |    "startDate": "2012-01-01",
      |    "endDate": "2013-01-01",
      |    "summary": "Global movement of free coding clubs for young people.",
      |    "highlights": ["Awarded 'Teacher of the Month'"]
      |  }],
      |  "education": [{
      |    "institution": "University of Oklahoma",
      |    "url": "https://www.ou.edu/",
      |    "area": "Information Technology",
      |    "studyType": "Bachelor",
      |    "startDate": "2011-06-01",
      |    "endDate": "2014-01-01",
      |    "score": "4.0",
      |    "courses": ["DB1101 - Basic SQL", "CS2011 - Java Introduction"]
      |  }],
      |  "awards": [{"title": "Digital Compression Pioneer Award", "date": "2014-11-01", "awarder": "Techcrunch", "summary": "There is no spoon."}],
      |  "publications": [{"name": "Video compression for 3d media", "publisher": "Hooli", "releaseDate": "2014-10-01", "url": "http://en.wikipedia.org/wiki/Silicon_Valley_(TV_series)", "summary": "Innovative middle-out compression algorithm."}],
      |  "skills": [
      |    {"name": "Web Development", "level": "Master", "keywords": ["HTML", "CSS", "Javascript"]},
      |    {"name": "Compression", "level": "Master", "keywords": ["Mpeg", "MP4", "GIF"]}
      |  ],
      |  "languages": [{"language": "English", "fluency": "Native speaker"}],
      |  "interests": [{"name": "Wildlife", "keywords": ["Ferrets", "Unicorns"]}],
      |  "references": [{"name": "Erlich Bachman", "reference": "It is my pleasure to recommend Richard."}],
      |  "projects": [{
      |    "name": "Miss Direction",
      |    "description": "A mapping engine that misguides you",
      |    "highlights": ["Won award at AIHacks 2016"],
      |    "keywords": ["GoogleMaps", "Chrome Extension", "Javascript"],
      |    "startDate": "2016-08-24",
      |    "endDate": "2016-08-24",
      |    "url": "http://missdirection.example.com",
      |    "roles": ["Team lead", "Designer"],
      |    "entity": "Smoogle",
      |    "type": "application"
      |  }],
      |  "meta": {"canonical": "https://raw.githubusercontent.com/jsonresume/resume-schema/v1.0.0/sample.resume.json", "version": "v1.0.0", "lastModified": "2017-12-24T15:53:00"}
      |}""".stripMargin

  test("decode sample resume.json from JSON Resume project") {
    val result = decode[Resume](sampleResumeJson)
    assert(result.isRight, s"Failed to decode: ${result.left.toOption.get}")

    val resume = result.toOption.get

    // basics
    assertEquals(resume.basics.flatMap(_.name), Some("Richard Hendriks"))
    assertEquals(resume.basics.flatMap(_.label), Some("Programmer"))
    assertEquals(
      resume.basics.flatMap(_.location).flatMap(_.city),
      Some("San Francisco")
    )
    assertEquals(resume.basics.flatMap(_.profiles).map(_.size), Some(2))

    // work
    assertEquals(resume.work.map(_.size), Some(1))
    assertEquals(
      resume.work.flatMap(_.headOption).flatMap(_.name),
      Some("Pied Piper")
    )
    assertEquals(
      resume.work.flatMap(_.headOption).flatMap(_.location),
      Some("Palo Alto, CA")
    )

    // education
    assertEquals(
      resume.education.flatMap(_.headOption).flatMap(_.institution),
      Some("University of Oklahoma")
    )

    // skills
    assertEquals(resume.skills.map(_.size), Some(2))

    // projects
    assertEquals(
      resume.projects.flatMap(_.headOption).flatMap(_.`type`),
      Some("application")
    )

    // meta
    assertEquals(resume.meta.flatMap(_.version), Some("v1.0.0"))

    // $schema
    assert(resume.`$schema`.isDefined)
  }
