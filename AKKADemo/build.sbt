ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .settings(
    name := "AKKADemo"
  )

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.2.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.32"
