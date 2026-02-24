package si.uni_lj.fe.femensus

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

var parsedMenu: Week? = null
var menuTitle: String? = null

class Scraper {
    companion object {
        private const val URL = "https://fe.uni-lj.si/o-fakulteti/restavracija/"

        suspend fun fetchAndParse(): Result<Week> = withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(URL).get()
                val week = Week(extractAndParseMenu(doc))
                parsedMenu = week
                Result.success(week)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.failure(e)
            }
        }

        private fun extractAndParseMenu(doc: Document): List<Pair<String, List<Menu>>> {
            val menuDays = mutableMapOf<Int, Pair<String, List<Menu>>>()
            menuTitle = doc.select(".section-title h2").first()?.text()
                ?: "Naslov menija ni bil najden"

            val accordions = doc.select(".accordion-single")

            for (accordion in accordions) {
                val dayText = accordion.select(".title strong").text()
                
                val dayIndex = when {
                    dayText.contains("Ponedeljek", true) -> 0
                    dayText.contains("Torek", true) -> 1
                    dayText.contains("Sreda", true) -> 2
                    dayText.contains("Četrtek", true) -> 3
                    dayText.contains("Petek", true) -> 4
                    else -> -1
                }
                
                if (dayIndex != -1) {
                    val menuItems = accordion.select(".content--wrapper ul li").map {
                        val menuText = it.text()
                        val menuParts = menuText.split(":")
                        val menuNameType = menuParts[0].split("(", ")")
                        val menuName = menuNameType[0].trim()
                        val menuType = if (menuNameType.size > 1) menuNameType[1].trim() else ""
                        
                        val menuDetails = if (menuParts.size > 1) {
                            val rawDetails = menuParts[1].split(",")
                            val merged = mutableListOf<String>()
                            var current = ""
                            for (detail in rawDetails) {
                                val trimmed = detail.trim()
                                if (current.isEmpty()) {
                                    current = trimmed
                                } else {
                                    if ((current.contains("solata s ", true) || current.contains("solata z ", true)) && 
                                        !current.contains(" ali ", true)) {
                                        current += ", $trimmed"
                                    } else {
                                        merged.add(current.replaceFirstChar { it.uppercase() })
                                        current = trimmed
                                    }
                                }
                            }
                            if (current.isNotEmpty()) {
                                merged.add(current.replaceFirstChar { it.uppercase() })
                            }
                            merged
                        } else listOf()

                        Menu(menuName, menuType, menuDetails)
                    }
                    menuDays[dayIndex] = dayText to menuItems
                }
            }
            
            val finalMenu = mutableListOf<Pair<String, List<Menu>>>()
            val defaultDayNames = listOf("Ponedeljek", "Torek", "Sreda", "Četrtek", "Petek")
            for (i in 0..4) {
                finalMenu.add(menuDays[i] ?: (defaultDayNames[i] to emptyList()))
            }
            return finalMenu
        }
    }
}
