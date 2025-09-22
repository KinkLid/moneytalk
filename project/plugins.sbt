// Подключаем плагин Scalafmt для форматирования кода
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2") // Версия плагина Scalafmt

// Подключаем плагин Scalafix для автоматических исправлений кода
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.13.0") // Версия плагина Scalafix

// Подключаем плагин scoverage для отчёта покрытия тестами
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.12") // Версия плагина scoverage

// Плагин для сборки Docker образов (опционально, позже настроим)
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.0") // Плагин для упаковки

