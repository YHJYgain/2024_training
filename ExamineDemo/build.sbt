name := """ExamineDemo"""

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

  "org.webjars" % "npm" % "2.11.2",
  "org.fusesource.leveldbjni" % "leveldbjni-linux32" % "1.8",
  "org.fusesource.leveldbjni" % "leveldbjni-linux64" % "1.8",
  "org.fusesource.leveldbjni" % "leveldbjni-osx" % "1.8",
  "org.fusesource.leveldbjni" % "leveldbjni-win64" % "1.8",
  "org.fusesource.leveldbjni" % "leveldbjni-win32" % "1.8",
  "org.seleniumhq.selenium" % "selenium-java" % "3.141.59",
  "org.webjars" % "webjars-locator" % "0.25",
  "org.tukaani" % "xz" % "1.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "org.slf4j" % "slf4j-api" % "1.7.21",

  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",

  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
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
  "ch.qos.logback" % "logback-classic" % "1.5.6",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",

  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.5.13",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.13",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13"
)

resolvers ++= Seq(
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
  "Typesafe Ivy Releases" at "https://repo.typesafe.com/typesafe/ivy-releases/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Maven Central" at "https://repo1.maven.org/maven2/",
  "FuseSource releases" at "https://repo.fusesource.com/nexus/content/repositories/releases",
  "Akka library repository".at("https://repo.akka.io/maven")
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
