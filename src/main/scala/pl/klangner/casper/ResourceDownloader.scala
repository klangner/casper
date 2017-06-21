package pl.klangner.casper

import sys.process._
import java.net.URL
import java.io.File

import com.github.tototoshi.csv.CSVReader

import scala.util.Try


/**
  * Download resources (files) from the internet.
  */
object ResourceDownloader {

  def downloadAllResources(folder: String): Unit = {
    val files = recursiveListFiles(new File(folder)).filter(_.getName == "resources.csv")
    files.foreach(downloadResources)
  }

  def recursiveListFiles(f: File): Seq[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  def downloadResources(file: File): Unit = {
    val folder = file.getParent + "/"
    val reader = CSVReader.open(file)
    println("download resources from: " + file.getAbsolutePath)
    // Skip headers
    reader.all().tail.foreach{ xs: Seq[String] =>
      Try(downloadFile(xs(1), folder + xs.head + ".pdf"))
    }
  }

  /** Download file only if it doesn't exist yet */
  def downloadFile(url: String, filename: String): Unit = {
    val file = new File(filename)
    if(!file.exists() || file.length() == 0) {
      println("download: " + url)
      new URL(url) #> new File(filename) !!
    }
  }
}
