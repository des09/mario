name := "mario"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies += "org.pircbotx" % "pircbotx" % "2.0.1"

mainClass in Compile := Some("it.slack.mario.Mario")
