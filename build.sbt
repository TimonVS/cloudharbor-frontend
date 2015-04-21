name := """FrontEnd"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
 "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.scalikejdbc" %% "scalikejdbc"                       % "2.2.5",
  "org.scalikejdbc" %% "scalikejdbc-config"                % "2.2.5",
  "org.scalikejdbc" %% "scalikejdbc-play-plugin"           % "2.3.6",
  "org.scalikejdbc" %% "scalikejdbc-test"   % "2.2.5"   % "test",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars.bower" % "bootstrap-sass" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.15",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4"
)

scalikejdbcSettings
