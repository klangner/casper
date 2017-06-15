package pl.klangner.scraper

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._


/**Pobieranie dzienników ustaw */
object DziennikUstawScraper {

  val SITE_URL = "http://isap.sejm.gov.pl/"
  val browser = JsoupBrowser()


  def scrap(): Unit = {
    val doc = browser.get(SITE_URL + "VolumeServlet?type=wdu")
    val yearNodes = doc >> elementList(".cel_black11")
    val links = yearNodes.map(node => node >> attr("href"))
                         .filter(_.contains("rok"))
                         .flatMap(scrapYear)
                         .flatMap(scrapSection)
    links.take(10).foreach(println)
  }

  /** Return links for all sections in given year */
  def scrapYear(link: String): Seq[String] = {
    val doc = browser.get(SITE_URL + link)
    val sectionLinks = doc >> elementList(".cel_black11")
    sectionLinks.map(node => node >> attr("href")).filter(_.contains("rok"))
  }

  /** Return list of links for a given section */
  def scrapSection(link: String): Seq[String] = {
    val doc = browser.get(SITE_URL + link)
    val sectionLinks = doc >> elementList("a")
    sectionLinks.map(node => (node >?> attr("href")).getOrElse(""))
      .filter(_.contains("DetailsServlet?id="))
  }
}
