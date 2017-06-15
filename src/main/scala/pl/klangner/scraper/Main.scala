package pl.klangner.scraper

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

/**
  * Created by Krzysztof Langner on 2017-06-14.
  */
object Main {

  def main(args: Array[String]): Unit = {
    DziennikUstawScraper.scrap()
  }
}
