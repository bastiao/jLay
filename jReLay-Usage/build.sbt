name := """jReLay-Usage"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

resolvers += Resolver.mavenLocal

resolvers += "Maven repository" at "http://morphia.googlecode.com/svn/mavenrepo/"

resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "jrelay" % "jrelay_2.11" % "1.0-SNAPSHOT"
)