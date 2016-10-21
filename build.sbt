name := """prototyp_api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.firebase" % "firebase-server-sdk" % "3.0.1"
)
