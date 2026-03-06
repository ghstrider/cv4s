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

import io.circe.*
import io.circe.generic.semiauto.*

case class Meta(
    canonical: Option[String] = None,
    version: Option[String] = None,
    lastModified: Option[String] = None
)

/** JSON Resume schema - https://jsonresume.org/schema */
case class Resume(
    `$schema`: Option[String] = None,
    basics: Option[Basics] = None,
    work: Option[List[Work]] = None,
    volunteer: Option[List[Volunteer]] = None,
    education: Option[List[Education]] = None,
    awards: Option[List[Award]] = None,
    certificates: Option[List[Certificate]] = None,
    publications: Option[List[Publication]] = None,
    skills: Option[List[Skill]] = None,
    languages: Option[List[Language]] = None,
    interests: Option[List[Interest]] = None,
    references: Option[List[Reference]] = None,
    projects: Option[List[Project]] = None,
    meta: Option[Meta] = None
)

case class Basics(
    name: Option[String] = None,
    label: Option[String] = None,
    image: Option[String] = None,
    email: Option[String] = None,
    phone: Option[String] = None,
    url: Option[String] = None,
    summary: Option[String] = None,
    location: Option[Location] = None,
    profiles: Option[List[Profile]] = None
)

case class Location(
    address: Option[String] = None,
    postalCode: Option[String] = None,
    city: Option[String] = None,
    countryCode: Option[String] = None,
    region: Option[String] = None
)

case class Profile(
    network: Option[String] = None,
    username: Option[String] = None,
    url: Option[String] = None
)

case class Work(
    name: Option[String] = None,
    location: Option[String] = None,
    description: Option[String] = None,
    position: Option[String] = None,
    url: Option[String] = None,
    startDate: Option[String] = None,
    endDate: Option[String] = None,
    summary: Option[String] = None,
    highlights: Option[List[String]] = None
)

case class Volunteer(
    organization: Option[String] = None,
    position: Option[String] = None,
    url: Option[String] = None,
    startDate: Option[String] = None,
    endDate: Option[String] = None,
    summary: Option[String] = None,
    highlights: Option[List[String]] = None
)

case class Education(
    institution: Option[String] = None,
    url: Option[String] = None,
    area: Option[String] = None,
    studyType: Option[String] = None,
    startDate: Option[String] = None,
    endDate: Option[String] = None,
    score: Option[String] = None,
    courses: Option[List[String]] = None
)

case class Award(
    title: Option[String] = None,
    date: Option[String] = None,
    awarder: Option[String] = None,
    summary: Option[String] = None
)

case class Certificate(
    name: Option[String] = None,
    date: Option[String] = None,
    issuer: Option[String] = None,
    url: Option[String] = None
)

case class Publication(
    name: Option[String] = None,
    publisher: Option[String] = None,
    releaseDate: Option[String] = None,
    url: Option[String] = None,
    summary: Option[String] = None
)

case class Skill(
    name: Option[String] = None,
    level: Option[String] = None,
    keywords: Option[List[String]] = None
)

case class Language(
    language: Option[String] = None,
    fluency: Option[String] = None
)

case class Interest(
    name: Option[String] = None,
    keywords: Option[List[String]] = None
)

case class Reference(
    name: Option[String] = None,
    reference: Option[String] = None
)

case class Project(
    name: Option[String] = None,
    description: Option[String] = None,
    highlights: Option[List[String]] = None,
    keywords: Option[List[String]] = None,
    startDate: Option[String] = None,
    endDate: Option[String] = None,
    url: Option[String] = None,
    roles: Option[List[String]] = None,
    entity: Option[String] = None,
    `type`: Option[String] = None
)

object Resume:
  given Codec[Meta] = deriveCodec
  given Codec[Location] = deriveCodec
  given Codec[Profile] = deriveCodec
  given Codec[Basics] = deriveCodec
  given Codec[Work] = deriveCodec
  given Codec[Volunteer] = deriveCodec
  given Codec[Education] = deriveCodec
  given Codec[Award] = deriveCodec
  given Codec[Certificate] = deriveCodec
  given Codec[Publication] = deriveCodec
  given Codec[Skill] = deriveCodec
  given Codec[Language] = deriveCodec
  given Codec[Interest] = deriveCodec
  given Codec[Reference] = deriveCodec
  given Codec[Project] = deriveCodec
  given Codec[Resume] = deriveCodec
