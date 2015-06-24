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
  "com.adrianhurt" %% "play-bootstrap3" % "0.4",
  "org.scalaz" %% "scalaz-core" % "7.1.0"
)

scalikejdbcSettings

// setting a maintainer which is used for all packaging types
maintainer := "Thomas Meijers"

packageName in Docker := "frontend"

// exposing the play ports
dockerExposedPorts in Docker := Seq(9000, 9443)

fork in run := false
