ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

// Включаем SemanticDB для Scala 3 (нужно для Metals: переходы/подсветка/поиск символов)
ThisBuild / scalacOptions ++= Seq("-Ysemanticdb")

lazy val root = (project in file("."))
  .settings(
    name := "moneytalk",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.6",
      "dev.zio" %% "zio-http" % "3.0.0-RC4"
    ),
    run / fork := true
  )
