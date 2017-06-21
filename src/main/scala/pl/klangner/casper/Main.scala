package pl.klangner.casper

/**
  * Created by Krzysztof Langner on 2017-06-14.
  */
object Main {

  def main(args: Array[String]): Unit = {
//    Isap.scrapDziennikUstaw("data/dziennik-ustaw")
//    Isap.scrapMonitorPolski("data/monitor-polski")
    ResourceDownloader.downloadAllResources("data")
  }
}
