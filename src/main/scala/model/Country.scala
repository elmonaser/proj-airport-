package model

case class Country(
                    id: Int,
                    code: String,
                    name: String,
                    continent: String,
                    wikipediaLink: String,
                    keywords: Option[String]
                  )

object Country {
  def fromCsv(line: String): Option[Country] = {
    val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1).map(_.trim.replaceAll("^\"|\"$", ""))
    if (parts.length >= 5) {
      Some(Country(
        parts(0).toInt,
        parts(1),
        parts(2),
        parts(3),
        parts(4),
        if (parts.length > 5 && parts(5).nonEmpty) Some(parts(5)) else None
      ))
    } else None
  }
}