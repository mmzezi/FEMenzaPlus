package si.uni_lj.fe.femensus

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

var parsedMenu: Week? = null
var menuTitle: String? = null

class Scraper {
    companion object {
        private const val URL = "https://fe.uni-lj.si/o-fakulteti/restavracija/"
        // Testing server
        //private const val URL = "http://212.85.161.209:18000/Restavracija%20-%20Fakulteta%20za%20elektrotehniko25.5.html"

        @OptIn(DelicateCoroutinesApi::class)
        fun fetchAndParse() {
            println("Starting the fetch")
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val doc = Jsoup.connect(URL).get()
                    parsedMenu = Week(extractAndParseMenu(doc))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            println("Exiting from the fetch function")
        }

        private fun extractAndParseMenu(doc: Document): List<Pair<String, List<Menu>>> {
            val menuList = mutableListOf<Pair<String, List<Menu>>>()
            menuTitle = doc.select(".section-title h2").first()?.text()
                ?: "Menu Title Not Found"  // Extract the title

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
                        .map { detail -> detail.trim().capitalize() } else listOf()
                    Menu(menuName, menuType, menuDetails)
                }


                menuList.add(day to menuItems)
            }
            return menuList
        }
    }
}

object NetworkUtils {

    fun testServerConnectivity(serverUrl: String, callback: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = Jsoup.connect(serverUrl).execute()
                withContext(Dispatchers.Main) {
                    callback(response.statusCode() == 200)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(false)
                }
            }
        }
    }
}
