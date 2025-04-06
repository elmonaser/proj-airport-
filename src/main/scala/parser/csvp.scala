package parser

import scala.io.Source
import scala.util.{Try, Success, Failure}

object csvp {
  def parseFile[T](filename: String, converter: String => Option[T]): List[T] = {
    Try {
      val source = Source.fromFile(filename)
      val lines = source.getLines().toList.tail // Skip header
      source.close()
      lines.flatMap(converter)
    } match {
      case Success(data) => data
      case Failure(ex) =>
        println(s"Error parsing $filename: ${ex.getMessage}")
        List.empty[T]
    }
  }
}