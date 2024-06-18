name := "ExamineDemo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

lazy val akkaVersion = sys.props.getOrElse("akka.version", "2.5.13")

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.softwaremill.macwire" %% "macros" % "2.2.4" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.2.4",
  "com.softwaremill.macwire" %% "proxy" % "2.2.4",

  "com.typesafe.play" %% "play" % "2.5.18",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,

  "org.seleniumhq.selenium" % "selenium-java" % "3.141.59",
  "org.webjars" % "webjars-locator" % "0.25",
  "org.tukaani" % "xz" % "1.0",

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "org.slf4j" % "slf4j-api" % "1.7.30",

  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.2.18" % Test
)

dependencyOverrides ++= Set(
  "com.google.code.findbugs" % "jsr305" % "3.0.2",
  "com.google.guava" % "guava" % "25.0-jre",
  "io.netty" % "netty-transport" % "4.0.51.Final",
  "io.netty" % "netty-buffer" % "4.0.51.Final",
  "io.netty" % "netty-common" % "4.0.51.Final",
  "io.netty" % "netty-handler" % "4.0.51.Final",
  "io.netty" % "netty-codec" % "4.0.51.Final",
  "org.webjars" % "webjars-locator" % "0.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)
