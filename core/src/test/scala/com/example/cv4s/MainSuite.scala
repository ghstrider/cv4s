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

import io.circe.parser.*
import io.circe.syntax.*
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
            List(Profile(network = Some("GitHub"), username = Some("janesmith")))
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
