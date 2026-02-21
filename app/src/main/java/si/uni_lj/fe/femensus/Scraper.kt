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
            val menuList = mutableListOf<Pair<String, List<Menu>>>()
            menuTitle = doc.select(".section-title h2").first()?.text()
                ?: "Naslov menija ni bil najden"

            val accordions = doc.select(".accordion-single")

            for (accordion in accordions) {
                val day = accordion.select(".title strong").text()
                val menuItems = accordion.select(".content--wrapper ul li").map {
                    val menuText = it.text()
                    val menuParts = menuText.split(":")
                    val menuNameType = menuParts[0].split("(", ")")
                    val menuName = menuNameType[0].trim()
                    val menuType = if (menuNameType.size > 1) menuNameType[1].trim() else ""
                    val menuDetails = if (menuParts.size > 1) menuParts[1].split(",")
                        .map { detail -> detail.trim().replaceFirstChar { char -> char.uppercase() } } else listOf()
                    Menu(menuName, menuType, menuDetails)
                }
                menuList.add(day to menuItems)
            }
            return menuList
        }
    }
}
