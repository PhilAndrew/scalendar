organization := "com.github.philcali"

name := "scalendar"

version := "0.1.4"

scalaVersion := "2.11.0"

crossScalaVersions := Seq (
  "2.11.0",
  "2.10.3",
  "2.9.2", "2.9.1", "2.9.1-1","2.9.0-1", "2.9.0"
)

scalacOptions <++= scalaVersion map {
  case sv if sv startsWith "2.11" => Seq("-feature", "-language:implicitConversions")
  case _ => Nil
}

libraryDependencies <++= scalaVersion {
  case sv if sv startsWith "2.11" => Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.0.1" % "test",
    "org.scalatest" %% "scalatest" % "2.1.3" % "test")
  case sv if sv startsWith "2.10" =>
  Seq("org.scalatest" %% "scalatest" % "1.9" % "test")
  case _ =>
  Seq("org.scalatest" %% "scalatest" % "1.8" % "test")
}

publishTo <<= version { v =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

unmanagedClasspath in Compile += Attributed.blank(new java.io.File("doesnotexist"))

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/philcali/scalendar</url>
  <licenses>
    <license>
      <name>The MIT License</name>
      <url>http://www.opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:philcali/scalendar.git</url>
    <connection>scm:git:git@github.com:philcali/scalendar.git</connection>
  </scm>
  <developers>
    <developer>
      <id>philcali</id>
      <name>Philip Cali</name>
      <url>http://philcalicode.blogspot.com/</url>
    </developer>
  </developers>
)
