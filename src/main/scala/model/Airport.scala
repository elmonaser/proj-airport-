package model


case class Airport(
                    id: Int,
                    ident: String,
                    `type`: String,
                    name: String,
                    latitudeDeg: Double,
                    longitudeDeg: Double,
                    elevationFt: Option[Int],
                    continent: String,
                    isoCountry: String,
                    isoRegion: String,
                    municipality: String,
                    scheduledService: String,
                    gpsCode: String,
                    iataCode: Option[String],
                    localCode: Option[String],
                    homeLink: Option[String],
                    wikipediaLink: Option[String],
                    keywords: Option[String]
                  )

object Airport {
  def fromCsv(line: String): Option[Airport] = {
    val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1).map(_.trim.replaceAll("^\"|\"$", ""))
    if (parts.length >= 18) {
      Some(Airport(
        parts(0).toInt,
        parts(1),
        parts(2),
        parts(3),
        parts(4).toDouble,
        parts(5).toDouble,
        if (parts(6).nonEmpty) Some(parts(6).toInt) else None,
        parts(7),
        parts(8),
        parts(9),
        parts(10),
        parts(11),
        parts(12),
        if (parts(13).nonEmpty) Some(parts(13)) else None,
        if (parts(14).nonEmpty) Some(parts(14)) else None,
        if (parts(15).nonEmpty) Some(parts(15)) else None,
        if (parts(16).nonEmpty) Some(parts(16)) else None,
        if (parts(17).nonEmpty) Some(parts(17)) else None
      ))
    } else None
  }
}