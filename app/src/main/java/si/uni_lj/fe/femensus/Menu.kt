package si.uni_lj.fe.femensus

import java.util.Calendar

data class Menu(var name: String, var type: String, var details: List<String>)

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
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dayOfWeekEnum = when (dayOfWeek) {
                Calendar.MONDAY -> Monday
                Calendar.TUESDAY -> Tuesday
                Calendar.WEDNESDAY -> Wednesday
                Calendar.THURSDAY -> Thursday
                Calendar.FRIDAY -> Friday
                else -> Monday
            }
            return dayOfWeekEnum
        }
    }
}

data class Week(var days: List<Pair<String, List<Menu>>>) {
    private fun getDay(day: WeekDay): Pair<String, List<Menu>>{
        return days[day.value];
    }

    fun getDayMenuItems(day: WeekDay): List<MenuItem> {
        val menuList: List<MenuItem> = parsedMenu!!.getDay(day).second.map {
            MenuItem(R.drawable.icon_final_1_, it.name.capitalize(), it.type, it.details.joinToString("\n"))
        }
        return menuList
    }
}