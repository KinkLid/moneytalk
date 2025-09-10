ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val zioVersion     = "2.1.6"
lazy val zioHttpVersion = "3.0.0-RC7"

lazy val root = (project in file("."))
  .settings(
    name := "moneyTalk",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-http" % zioHttpVersion
    )
  )
