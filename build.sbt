name := "ProductSearch"

organization := "com.jandritsch"

version := "0.0.1"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test" withSources() withJavadoc(),
  "org.json4s" %% "json4s-native" % "3.2.11"
)

initialCommands := "import com.jandritsch.productsearch._"

mainClass := Some("com.jandritsch.productsearch.Program")

