// https://typelevel.org/sbt-typelevel/faq.html#what-is-a-base-version-anyway
ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "io.github.ghstrider"
ThisBuild / organizationName := "cv4s contributors"
ThisBuild / startYear := Some(2026)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  tlGitHubDev("ankushs92", "Ankush Singh")
)

// publish website from this branch
ThisBuild / tlSitePublishBranch := Some("main")
// Central Portal doesn't support snapshots, only publish on version tags
ThisBuild / githubWorkflowPublishTargetBranches := Seq(
  RefPredicate.StartsWith(Ref.Tag("v"))
)

ThisBuild / crossScalaVersions := Seq("3.3.7")
ThisBuild / scalaVersion := "3.3.7"

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    name := "cv4s",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % "0.14.13",
      "io.circe" %%% "circe-generic" % "0.14.13",
      "io.circe" %%% "circe-parser" % "0.14.13",
      "org.scalameta" %%% "munit" % "1.2.4" % Test
    )
  )

lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .settings(
    tlSiteApiUrl := None,
    tlSiteHelium ~= {
      import laika.helium.config._
      _.site.topNavigationBar(
        homeLink =
          IconLink.internal(laika.ast.Path.Root / "index.md", HeliumIcon.home)
      )
    }
  )
