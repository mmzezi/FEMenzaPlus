package si.uni_lj.fe.femensus

import java.util.Calendar

data class Menu(val name: String, val type: String, val details: List<String>)

enum class WeekDay(val value: Int) {
    Monday(0),
    Tuesday(1),
    Wednesday(2),
    Thursday(3),
    Friday(4);

    override fun toString(): String {
        return when (this) {
            Monday -> "Ponedeljek"
            Tuesday -> "Torek"
            Wednesday -> "Sreda"
            Thursday -> "Četrtek"
            Friday -> "Petek"
        }
    }

    companion object {
        fun getToday(): WeekDay {
            val calendar = Calendar.getInstance()
            return when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> Monday
                Calendar.TUESDAY -> Tuesday
                Calendar.WEDNESDAY -> Wednesday
                Calendar.THURSDAY -> Thursday
                Calendar.FRIDAY -> Friday
                else -> Monday
            }
        }
    }
}

data class Week(val days: List<Pair<String, List<Menu>>>) {
    fun getDayMenuItems(day: WeekDay): List<MenuItem> {
        val dayIndex = day.value
        if (dayIndex >= days.size) return emptyList()
        
        val dayData = days[dayIndex]
        if (dayData.second.isEmpty()) {
            // Return a special "Missing Menu" item
            return listOf(
                MenuItem(
                    R.drawable.icon_final,
                    "Meni ni na voljo",
                    "Praznik / Ni podatkov",
                    "Za ta dan jedilnik trenutno ni na voljo."
                )
            )
        }
        
        return dayData.second.map {
            MenuItem(
                R.drawable.icon_final,
                it.name.replaceFirstChar { char -> char.uppercase() },
                it.type,
                it.details.joinToString("\n")
            )
        }
    }
}
