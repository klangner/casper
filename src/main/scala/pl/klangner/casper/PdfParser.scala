package pl.klangner.casper

import java.io.{BufferedWriter, File, FileWriter}
import javax.imageio.ImageIO

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.rendering.PDFRenderer


/**
  * Created by Krzysztof Langner on 2017-06-21.
  */
object PdfParser {

  def parseAllFiles(folder: String): Unit = {
    val files = recursiveListFiles(new File(folder)).filter(_.getName.endsWith(".pdf"))
    files.foreach(parseFile)
  }

  def recursiveListFiles(f: File): Seq[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  def parseFile(file: File): Unit = {
    val pdf = PDDocument.load(file)
    val text = new PDFTextStripper().getText(pdf).trim()

    if(text.length > 0){
      val textFile = new File(file.getAbsolutePath.replace(".pdf", ".txt"))
      val writer = new BufferedWriter(new FileWriter(textFile))
      writer.write(text)
      writer.close()
    } else {
      val renderer = new PDFRenderer(pdf)
      0.until(pdf.getNumberOfPages).foreach{ i =>
        val image = renderer.renderImage(i, 5)
        val imageFile = new File(file.getAbsolutePath.replace(".pdf", "-%d.png".format(i+1)))
        ImageIO.write(image, "PNG", imageFile)
      }
    }

    pdf.close()
  }
}
