name := "casper"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % "2.0.0-RC2",
  "com.github.tototoshi" %% "scala-csv" % "1.3.4",
  "org.apache.pdfbox" % "pdfbox" % "2.0.6",
  "org.bouncycastle" % "bcprov-jdk15" % "1.44",
  "org.bouncycastle" % "bcmail-jdk15" % "1.44"
)