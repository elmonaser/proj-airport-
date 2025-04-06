package repository

import model.{Country, Airport, Runway}
import parser.csvp
import scala.collection.immutable.Map

class dataR(countriesFile: String, airportsFile: String, runwaysFile: String) {
  val countries: List[Country] = csvp.parseFile(countriesFile, Country.fromCsv)
  val airports: List[Airport] = csvp.parseFile(airportsFile, Airport.fromCsv)
  val runways: List[Runway] = csvp.parseFile(runwaysFile, Runway.fromCsv)

  /*def findCountryByCodeOrName(input: String): Option[Country] = {
    countries.find(c => c.code.equalsIgnoreCase(input) || c.name.equalsIgnoreCase(input))}

  def findAirportsByCountry(countryCode: String): List[Airport] = {
    airports.filter(_.isoCountry.equalsIgnoreCase(countryCode))}

  def findRunwaysByAirport(airportId: Int): List[Runway] = {
    runways.filter(_.airportRef == airportId)}

  }*///
  // M2.2: Requête par pays (nom ou code)
  def findCountryAirportsAndRunways(input: String): Option[(Country, List[(Airport, List[Runway])])] = {
    findCountryByCodeOrName(input).map { country =>
      val countryAirports = airports.filter(_.isoCountry.equalsIgnoreCase(country.code))
      val airportsWithRunways = countryAirports.map { airport =>
        (airport, runways.filter(_.airportRef == airport.id))
      }
      (country, airportsWithRunways)
    }
  }

  // M2.3.1: Rapports - Top 10 pays avec + et - d'aéroports
  def getAirportCountReport: (List[(Country, Int)], List[(Country, Int)]) = {
    val counts = countries.map { country =>
      (country, airports.count(_.isoCountry.equalsIgnoreCase(country.code)))
    }.sortBy(_._2)

    val top10 = counts.takeRight(10).reverse
    val bottom10 = counts.take(10)
    (top10, bottom10)
  }

  // M2.3.2: Rapports - Types de runways par pays
  def getRunwaySurfacesReport: Map[String, Set[String]] = {
    airports.flatMap { airport =>
        runways
          .filter(_.airportRef == airport.id)
          .flatMap(_.surface)
          .map(surface => (airport.isoCountry, surface))
      }.groupMap(_._1)(_._2)
      .map { case (code, surfaces) =>
        (code, surfaces.toSet)
      }
  }

  // M2.3.3: Rapports - Top 10 latitudes communes
  def getCommonRunwayIdentReport: List[(String, Int)] = {
    runways
      .flatMap(_.leIdent)
      .groupBy(identity)
      .map { case (ident, occurrences) => (ident, occurrences.size) }
      .toList
      .sortBy(-_._2)
      .take(10)
  }

  // C.1: Recherche fuzzy (optionnelle)
  def findCountriesFuzzy(input: String): List[Country] = {
    val normalizedInput = input.toLowerCase.trim
    countries.filter { country =>
      country.name.toLowerCase.contains(normalizedInput) ||
        country.code.toLowerCase.contains(normalizedInput)
    }
  }

  // Méthodes utilitaires privées
  private def findCountryByCodeOrName(input: String): Option[Country] = {
    countries.find { country =>
      country.code.equalsIgnoreCase(input) ||
        country.name.equalsIgnoreCase(input)
    }
  }

  // C.2: Version alternative avec base de données (optionnelle)
  def getDbConnection: java.sql.Connection = {
    import java.sql.DriverManager
    DriverManager.getConnection("jdbc:h2:mem:airports;DB_CLOSE_DELAY=-1", "sa", "")
  }

  def getAirportsWithRunways(countryCode: String): List[(Airport, List[Runway])] = {
    airports
      .filter(_.isoCountry.equalsIgnoreCase(countryCode))
      .map(airport => (airport, runways.filter(_.airportRef == airport.id)))
  }

  // Ajoutez cette méthode dans la classe dataR
  def getRunwaySurfacesByCountry: Map[String, List[String]] = {
    (for {
      airport <- airports
      runway <- runways
      if runway.airportRef == airport.id
      if runway.surface != null
      if runway.surface.nonEmpty
    } yield (airport.isoCountry, runway.surface))
      .foldLeft(Map.empty[String, Set[String]]) { case (acc, (code, surface)) =>
        acc.updated(code, acc.getOrElse(code, Set.empty))
      }
      .map { case (k, v) => (k, v.toList) }
  }

 

}

