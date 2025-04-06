package ui

import repository.dataR
import model.{Country, Airport, Runway}
import scala.io.StdIn
class cL(repository: dataR) {
 



    def start(): Unit = {
      displayWelcomeMessage()
      mainMenu()
    }

    private def displayWelcomeMessage(): Unit = {
      println("\n" + "=" * 50)
      println(
        """|  ___    _   _   _   _   ___   ___   ___   ___
           | |_ _|  | | | | | \ | | | _ \ | _ \ | _ \ |_ _|
           |  | |   | | | | |  \| | |   / |   / |   /  | |
           | |___|  |_| |_| |_|\__| |_|_\ |_|_\ |_|_\ |___|""".stripMargin)
      println("=" * 50 + "\n")
    }

    private def mainMenu(): Unit = {
      println("\nMenu Principal:")
      println("1. Rechercher un pays")
      println("2. Afficher les rapports")
      println("3. Quitter")
      print("\nVotre choix : ")

      StdIn.readLine().trim match {
        case "1" => queryMenu()
        case "2" => reportsMenu()
        case "3" => println("\nMerci d'avoir utilisé Airport Data Analysis. Au revoir!")
        case _ =>
          println("\nOption invalide. Veuillez choisir 1, 2 ou 3.")
          mainMenu()
      }
    }

    private def queryMenu(): Unit = {
      print("\nEntrez un nom de pays ou un code (partiel accepté) : ")
      val input = StdIn.readLine().trim

      if (input.isEmpty) {
        println("\nVeuillez entrer une valeur.")
        queryMenu()
      } else {
        displayCountryResults(input)
        returnToMainMenu()
      }
    }

    private def displayCountryResults(input: String): Unit = {
      println("\n" + "=" * 50)
      println(s"Résultats pour : '$input'")
      println("=" * 50)

      val results = repository.findCountriesFuzzy(input)

      if (results.isEmpty) {
        println("\nAucun pays trouvé. Essayez avec un autre terme.")
      } else {
        results.foreach { country =>
          println(s"\n► Pays : ${country.name} (${country.code})")

          repository.getAirportsWithRunways(country.code) match {
            case Nil => println("  Aucun aéroport trouvé.")
            case airportsWithRunways =>
              airportsWithRunways.foreach { case (airport, runways) =>
                println(s"\n  Aéroport : ${airport.name} (${airport.`type`})")

                // Alternative à getOrElse pour municipality
                val ville = if (airport.municipality == null || airport.municipality.isEmpty) "Inconnue" else airport.municipality
                println(s"  Ville : $ville")

                if (runways.nonEmpty) {
                  println("  Pistes :")
                  runways.foreach { runway =>
                    // Alternative pour leIdent
                    val ident = if (runway.leIdent == null || runway.leIdent.isEmpty) "Inconnu" else runway.leIdent
                    println(s"    - Identifiant: $ident")

                    // Alternative pour surface
                    val surface = if (runway.surface == null || runway.surface.isEmpty) "Inconnue" else runway.surface
                    println(s"      Surface: $surface")

                    // Alternative pour lengthFt
                    val longueur = if (runway.lengthFt == null) "Inconnue" else runway.lengthFt.toString + " ft"
                    println(s"      Longueur: $longueur")
                  }
                } else {
                  println("  Aucune piste enregistrée.")
                }
              }
          }
        }
      }
    }

    private def reportsMenu(): Unit = {
      println("\n" + "=" * 50)
      println("Menu des Rapports")
      println("=" * 50)
      println("\n1. Top 10 des pays par nombre d'aéroports")
      println("2. Types de pistes par pays")
      println("3. Top 10 des identifiants de pistes les plus communs")
      println("4. Retour au menu principal")
      print("\nVotre choix : ")

      StdIn.readLine().trim match {
        case "1" => displayAirportCountReport()
        case "2" => displayRunwaySurfacesReport()
        case "3" => displayCommonRunwayIdents()
        case "4" => // Retour automatique au menu principal
        case _ => println("\nOption invalide.")
      }
    }

    private def displayAirportCountReport(): Unit = {
      val (top10, bottom10) = repository.getAirportCountReport

      println("\n" + "=" * 50)
      println("Top 10 des pays avec le plus d'aéroports")
      println("=" * 50)
      top10.foreach { case (country, count) =>
        println(f"${country.name}%-30s ${count}%3d aéroports")
      }

      println("\n" + "=" * 50)
      println("Top 10 des pays avec le moins d'aéroports")
      println("=" * 50)
      bottom10.foreach { case (country, count) =>
        println(f"${country.name}%-30s ${count}%3d aéroports")
      }

      returnToMainMenu()
    }

    private def displayRunwaySurfacesReport(): Unit = {
      val surfacesByCountry = repository.getRunwaySurfacesReport

      println("\n" + "=" * 50)
      println("Types de pistes par pays")
      println("=" * 50)

      surfacesByCountry.toList.sortBy(_._1).foreach { case (countryCode, surfaces) =>
        println(s"\n$countryCode :")
        if (surfaces.nonEmpty) {
          surfaces.foreach(surface => println(s"  - $surface"))
        } else {
          println("  Aucune donnée de piste disponible")
        }
      }

      returnToMainMenu()
    }

    private def displayCommonRunwayIdents(): Unit = {
      val commonIdents = repository.getCommonRunwayIdentReport

      println("\n" + "=" * 50)
      println("Top 10 des identifiants de pistes les plus communs")
      println("=" * 50)

      commonIdents.foreach { case (ident, count) =>
        println(f"$ident%-10s : $count%3d occurrences")
      }

      returnToMainMenu()
    }

    private def returnToMainMenu(): Unit = {
      print("\nAppuyez sur Entrée pour continuer...")
      StdIn.readLine()
      mainMenu()
    }

  // Ajoutez cette méthode privée
  private def showRunwayTypesReport(): Unit = {
    println("\n=== TYPES DE PISTES PAR PAYS ===")

    repository.getRunwaySurfacesByCountry.toList
      .sortBy(_._1) // Trie par code pays
      .foreach { case (code, surfaces) =>
        println(s"\n$code:")
        surfaces.foreach(s => println(s"  - $s"))
      }

    print("\nAppuyez sur Entrée pour continuer...")
    StdIn.readLine()
  }

  // Modifiez la méthode handleReports() pour inclure l'option :
  private def handleReports(): Unit = {
    var inReports = true
    while (inReports) {
      println("\n=== RAPPORTS ===")
      println("1. Types de pistes par pays")
      println("2. Autre rapport...")
      println("3. Retour")
      print("Choix : ")

      StdIn.readLine() match {
        case "1" => showRunwayTypesReport() // <-- Nouvelle option
        case "2" => // ... autres rapports
        case "3" => inReports = false
        case _ => println("Option invalide")
      }
    }
  }
  
  }