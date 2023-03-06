val scala3Version = "3.2.2"

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := scala3Version
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-explain",
  "-language:implicitConversions",
  "-language:postfixOps"
)

ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
ThisBuild / libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.15.4" % Test
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.2.0"
lazy val picolib = RootProject(
  uri("https://github.com/hmc-cs111-spring2023/picolib.git#no-graphics")
)

lazy val root = project
  .settings(
    name := "Piconot" // you can change this to be the name of your language
  )
  .in(file("."))
  .dependsOn(picolib)

// So that we can run GUI apps multiple times from a single sbt session
// https://github.com/scalafx/scalafx/issues/361
fork := true
