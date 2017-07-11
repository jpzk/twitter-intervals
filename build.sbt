
lazy val commonSettings = Seq(
  organization := "com.madewithtea",
  version := "0.5.0",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.12.2","2.11.11"),
  description := "Intervals for Twitter Util Time",
  organizationHomepage := Some(url("https://www.madewithtea.com")))

val scalaTestVersion = "3.0.2"
val twitterVersion = "6.40.0"

lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
lazy val twitterUtilCore = "com.twitter" %% "util-core" % twitterVersion

lazy val twitterintervals = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      twitterUtilCore,
      scalaTest
    ) 
  )

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/jpzk/twitter-intervals</url>
    <licenses>
      <license>
        <name>Apache License Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:jpzk/twitter-intervals.git</url>
      <connection>scm:git:git@github.com:jpzk/twitter-intervals.git</connection>
    </scm>
    <developers>
      <developer>
        <id>jpzk</id>
        <name>Jendrik Poloczek</name>
        <url>https://www.madewithtea.com</url>
      </developer>
    </developers>
  )

