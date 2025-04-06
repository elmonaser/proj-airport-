import ui.cL
import repository.dataR
object Main {
  def main(args: Array[String]): Unit = {

    println("\n" + "=" * 50)
    println(
      """
        |   ___    _   _   _   _   ___   ___   ___   ___
        |  |_ _|  | | | | | \ | | | _ \ | _ \ | _ \ |_ _|
        |   | |   | | | | |  \| | |   / |   / |   /  | |
        |  |___|  |_| |_| |_|\__| |_|_\ |_|_\ |_|_\ |___|
        |
        |  █████╗ ██╗██████╗ ██████╗  ██████╗ ██████╗ ████████╗
        | ██╔══██╗██║██╔══██╗██╔══██╗██╔═══██╗██╔══██╗╚══██╔══╝
        | ███████║██║██████╔╝██████╔╝██║   ██║██████╔╝   ██║
        | ██╔══██║██║██╔═══╝ ██╔══██╗██║   ██║██╔══██╗   ██║
        | ██║  ██║██║██║     ██║  ██║╚██████╔╝██║  ██║   ██║
        | ╚═╝  ╚═╝╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝
        |
        |  | Data Analysis System | by Ahmed El Monaser]
        |""".stripMargin)
    println("Starting Airport Data Analysis...")
    val countriesFile = "data/countries.csv"
    val airportsFile = "data/airports.csv"
    val runwaysFile = "data/runways.csv"

    val repository = new dataR(countriesFile, airportsFile, runwaysFile)
    val cli = new cL(repository)

    cli.start()
   }
  }