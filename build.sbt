name := """genQuest"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test"
)
