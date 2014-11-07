name := """jReLay"""

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
  "com.pacscloud" % "BridgeLib" % "1.0-SNAPSHOT",
  "com.google.code.morphia" % "morphia" % "0.99",
  "org.mongodb" % "mongo-java-driver" % "2.7.3",
  "com.google.code.morphia" % "morphia-logging-slf4j" % "0.99",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.6.Final", // replace by your jpa implementation
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "com.pacscloud" % "SharedMessages" % "1.0-SNAPSHOT"
)