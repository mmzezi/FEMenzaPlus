package si.uni_lj.fe.femensus

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var today: WeekDay = WeekDay.Monday

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Find the toolbar in the layout
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        // Set the toolbar as the app bar for the activity
        setSupportActionBar(toolbar)

        // Get current day, Monday if weekend
        today = WeekDay.getToday()

        // Set the title programmatically after setting the day of the week
        supportActionBar?.title = getTitleForToolbar()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_monday -> {
                    today = WeekDay.Monday
                }
                R.id.navigation_tuesday -> {
                    today = WeekDay.Tuesday
                }
                R.id.navigation_wednesday -> {
                    today = WeekDay.Wednesday
                }
                R.id.navigation_thursday -> {
                    today = WeekDay.Thursday
                }
                R.id.navigation_friday -> {
                    today = WeekDay.Friday
                }
                else -> {
                    today = WeekDay.Monday
                }
            }
            val selectedFragment = DayFragment(today);
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
            supportActionBar?.title = getTitleForToolbar()
            true
        }

        // Set default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = when (today) {
                WeekDay.Monday -> R.id.navigation_monday
                WeekDay.Tuesday -> R.id.navigation_tuesday
                WeekDay.Wednesday -> R.id.navigation_wednesday
                WeekDay.Thursday -> R.id.navigation_thursday
                WeekDay.Friday -> R.id.navigation_friday
            }
        }
    }

    private fun getTitleForToolbar(): String {
        return menuTitle!!.replace(".", "")
            .replace(" od ", ": ")
            .replace(" do ", " - ")
            .replace(Regex("([a-z])ja$"), "$1j")
            .let {
                val words = it.split(" ")
                if (words.isNotEmpty()) {
                    val lastWord = words.last().replaceFirstChar { char -> char.uppercaseChar() }
                    words.dropLast(1).joinToString(" ") + " " + lastWord
                } else {
                    it
                }
            } ?: "Not loaded"
    }


}

