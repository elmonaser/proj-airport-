package model

case class Runway(
                   id: Int,
                   airportRef: Int,
                   airportIdent: String,
                   lengthFt: Option[Int],
                   widthFt: Option[Int],
                   surface: Option[String],
                   lighted: Boolean,
                   closed: Boolean,
                   leIdent: Option[String],
                   leLatitudeDeg: Option[Double],
                   leLongitudeDeg: Option[Double],
                   leElevationFt: Option[Int],
                   leHeadingDegT: Option[Double],
                   leDisplacedThresholdFt: Option[Int],
                   heIdent: Option[String],
                   heLatitudeDeg: Option[Double],
                   heLongitudeDeg: Option[Double],
                   heElevationFt: Option[Int],
                   heHeadingDegT: Option[Double],
                   heDisplacedThresholdFt: Option[Int]
                 )

object Runway {
  def fromCsv(line: String): Option[Runway] = {
    val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1).map(_.trim.replaceAll("^\"|\"$", ""))
    if (parts.length >= 20) {
      Some(Runway(
        parts(0).toInt,
        parts(1).toInt,
        parts(2),
        if (parts(3).nonEmpty) Some(parts(3).toInt) else None,
        if (parts(4).nonEmpty) Some(parts(4).toInt) else None,
        if (parts(5).nonEmpty) Some(parts(5)) else None,
        parts(6).toBoolean,
        parts(7).toBoolean,
        if (parts(8).nonEmpty) Some(parts(8)) else None,
        if (parts(9).nonEmpty) Some(parts(9).toDouble) else None,
        if (parts(10).nonEmpty) Some(parts(10).toDouble) else None,
        if (parts(11).nonEmpty) Some(parts(11).toInt) else None,
        if (parts(12).nonEmpty) Some(parts(12).toDouble) else None,
        if (parts(13).nonEmpty) Some(parts(13).toInt) else None,
        if (parts(14).nonEmpty) Some(parts(14)) else None,
        if (parts(15).nonEmpty) Some(parts(15).toDouble) else None,
        if (parts(16).nonEmpty) Some(parts(16).toDouble) else None,
        if (parts(17).nonEmpty) Some(parts(17).toInt) else None,
        if (parts(18).nonEmpty) Some(parts(18).toDouble) else None,
        if (parts(19).nonEmpty) Some(parts(19).toInt) else None
      ))
    } else None
  }
}
