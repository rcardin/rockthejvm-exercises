name := "rockthejvm-exercises"

version := "0.1"

scalaVersion := "2.13.4"

val AkkaVersion = "2.6.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
  "org.typelevel" %% "cats-core" % "2.2.0"
)
