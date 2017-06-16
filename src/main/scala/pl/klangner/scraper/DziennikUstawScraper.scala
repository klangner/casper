package pl.klangner.scraper

import java.io.File

import com.github.tototoshi.csv.CSVWriter
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._


/**Pobieranie dzienników ustaw */
object DziennikUstawScraper {

  val SITE_URL = "http://isap.sejm.gov.pl/"
  val browser = JsoupBrowser()

  case class DziennikUstaw(id: String, url: String, pdf: Option[String])

  def scrap(): Unit = {
    val xs = scrapAllYear()
      .flatMap(scrapYear)
      .flatMap(scrapSection)
      .map(scrapDetails)
    save(xs)
  }

  def scrapAllYear(): Seq[String] = {
    val doc = browser.get(SITE_URL + "VolumeServlet?type=wdu")
    (doc >> elementList(".cel_black11"))
      .map(node => node >> attr("href"))
      .filter(_.contains("rok"))
  }

  /** Return links for all sections in given year */
  def scrapYear(link: String): Seq[String] = {
    val doc = browser.get(SITE_URL + link)
    (doc >> elementList(".cel_black11"))
      .map(node => node >> attr("href"))
      .filter(_.contains("rok"))
  }

  /** Return list of links for a given section */
  def scrapSection(link: String): Seq[String] = {
    val doc = browser.get(SITE_URL + link)
    (doc >> elementList("a"))
      .map(node => (node >?> attr("href")).getOrElse(""))
      .filter(_.contains("DetailsServlet?id="))
  }

  def scrapDetails(link: String): DziennikUstaw = {
    val id = link.substring(link.indexOf("id=")+3)
    val docUrl = SITE_URL + link
    val doc = browser.get(docUrl)
    val pdfLink = (doc >> elementList("a"))
      .map(node => (node >?> attr("href")).getOrElse(""))
      .find(_.contains("Download"))
      .map(l => SITE_URL + l.replace("&amp;", "&").substring(1))

    DziennikUstaw(id, docUrl, pdfLink)
  }

  def save(xs: Seq[DziennikUstaw]): Unit = {
    val writer = CSVWriter.open(new File("data/ustawy.csv"))
    writer.writeRow(List("id", "pdf"))
    xs.foreach(x => writer.writeRow(List(x.id, x.pdf.getOrElse(""))))
    writer.close()
  }
}
