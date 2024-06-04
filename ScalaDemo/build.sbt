ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaDemo"
  )

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.2.0"
