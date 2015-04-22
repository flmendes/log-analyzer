name := "log-analyzer"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.apache.spark"   %% "spark-core"              % "1.3.0" % "provided",
  "io.thekraken"       %  "grok"                    % "0.1.1",
  "org.json4s"         %% "json4s-native"           % "3.2.10"
  )

exportJars := true