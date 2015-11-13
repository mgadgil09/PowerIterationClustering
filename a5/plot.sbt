name := "PIC"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.1"

libraryDependencies  ++= Seq(
              // other dependencies here
  "org.scalanlp" %% "breeze" % "0.11.2",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes. 
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "0.11.2",
  // the visualization library is distributed separately as well. 
  // It depends on LGPL code.
    "org.scalanlp" %% "breeze-viz" % "0.11.2",
 ("org.apache.spark"  % "spark-core_2.10"              % "1.5.1" % "provided").exclude("org.mortbay.jetty", "servlet-api").
    exclude("commons-beanutils", "commons-beanutils-core").
    exclude("commons-collections", "commons-collections").
    exclude("commons-logging", "commons-logging").
    exclude("com.esotericsoftware.minlog", "minlog"),
 "org.apache.hadoop" % "hadoop-client" % "2.0.0-cdh4.4.0" % "provided",
  "org.apache.spark"  % "spark-mllib_2.10"             % "1.5.1" % "provided"


)

libraryDependencies += "org.scalala" % "scalala_2.9.1" % "1.0.0.RC2"


mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
    case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
    case PathList("org", "apache", xs @ _*) => MergeStrategy.last
    case PathList("com", "google", xs @ _*) => MergeStrategy.last
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
    case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
    case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
    case "about.html" => MergeStrategy.rename
    case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
    case "META-INF/mailcap" => MergeStrategy.last
    case "META-INF/mimetypes.default" => MergeStrategy.last
    case "plugin.properties" => MergeStrategy.last
    case "log4j.properties" => MergeStrategy.last
    case x => old(x)
  }
}

resolvers ++= Seq(
        // other resolvers here
  // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
 "Scala Tools Snapshots" at "https://oss.sonatype.org/content/groups/scala-tools/",
            "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo"
)

// Scala 2.9.2 is still supported for 0.2.1, but is dropped afterwards.
scalaVersion := "2.10.4" // or 2.10.3 or later
