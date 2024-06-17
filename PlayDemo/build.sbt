name := """PlayDemo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

//scalaVersion := "2.13.14"
scalaVersion := "2.11.12"

//libraryDependencies += guice
//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
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
  "ch.qos.logback" % "logback-core" % "1.2.3"
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
  "org.tukaani" % "xz" % "1.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
