/**
  * Web Scraper for:
  * Internetowy System Aktów Prawny
  *
  * Zawiera:
  *   * Dzienniki Ustaw
  *   * Monitory Polskie
  */
package pl.klangner.casper

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document


object Isap {

  val SITE_URL = "http://isap.sejm.gov.pl/"
  val browser = JsoupBrowser()

  case class DziennikUstaw(id: String, url: String, pdf: Option[String])

  def scrapDziennikUstaw(): Unit = {
    val xs = scrapAllYear("wdu")
      .flatMap(scrapYear)
      .flatMap(scrapSection)
      .map(scrapDetails)
    save("data/dziennik-ustaw/", xs)
  }

  def scrapMonitorPolski(): Unit = {
    val xs = scrapAllYear("wmp")
      .flatMap(scrapYear)
      .flatMap(scrapSection)
      .map(scrapDetails)
    save("data/monitor-polski/", xs)
  }

  def loadDocument(link: String): Document = {
    browser.get(link)
  }

  def scrapAllYear(docType: String): Seq[String] = {
    val doc = loadDocument(SITE_URL + "VolumeServlet?type=" + docType)
    (doc >> elementList(".cel_black11"))
      .map(node => node >> attr("href"))
      .filter(_.contains("rok"))
  }

  /** Return links for all sections in given year */
  def scrapYear(link: String): Seq[String] = {
    val doc = loadDocument(SITE_URL + link)
    (doc >> elementList(".cel_black11"))
      .map(node => node >> attr("href"))
      .filter(_.contains("rok"))
  }

  /** Return list of links for a given section */
  def scrapSection(link: String): Seq[String] = {
    println(link)
    val doc = loadDocument(SITE_URL + link)
    (doc >> elementList("a"))
      .map(node => (node >?> attr("href")).getOrElse(""))
      .filter(_.contains("DetailsServlet?id="))
  }

  def scrapDetails(link: String): DziennikUstaw = {
    val id = link.substring(link.indexOf("id=")+3)
    val docUrl = SITE_URL + link
    val doc = loadDocument(docUrl)
    val pdfLink = (doc >> elementList("a"))
      .map(node => (node >?> attr("href")).getOrElse(""))
      .find(_.contains("Download"))
      .map(l => SITE_URL + l.replace("&amp;", "&").substring(1))

    DziennikUstaw(id, docUrl, pdfLink)
  }

  def save(folder: String, xs: Seq[DziennikUstaw]): Unit = {
    val writer = CSVWriter.open(new File(folder + "resources.csv"))
    writer.writeRow(List("id", "pdf"))
    xs.foreach(x => writer.writeRow(List(x.id, x.pdf.getOrElse(""))))
    writer.close()
  }

}
