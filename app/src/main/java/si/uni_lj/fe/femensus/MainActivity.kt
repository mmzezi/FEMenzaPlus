package si.uni_lj.fe.femensus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var today: WeekDay = WeekDay.Monday

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if data is loaded. If process was killed, parsedMenu will be null.
        if (parsedMenu == null) {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        today = WeekDay.getToday()
        supportActionBar?.title = getTitleForToolbar()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val newDay = when (item.itemId) {
                R.id.navigation_monday -> WeekDay.Monday
                R.id.navigation_tuesday -> WeekDay.Tuesday
                R.id.navigation_wednesday -> WeekDay.Wednesday
                R.id.navigation_thursday -> WeekDay.Thursday
                R.id.navigation_friday -> WeekDay.Friday
                else -> WeekDay.Monday
            }
            
            if (newDay != today || supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
                today = newDay
                replaceFragment(today)
                supportActionBar?.title = getTitleForToolbar()
            }
            true
        }

        if (savedInstanceState == null) {
            val selectedItemId = when (today) {
                WeekDay.Monday -> R.id.navigation_monday
                WeekDay.Tuesday -> R.id.navigation_tuesday
                WeekDay.Wednesday -> R.id.navigation_wednesday
                WeekDay.Thursday -> R.id.navigation_thursday
                WeekDay.Friday -> R.id.navigation_friday
            }
            bottomNavigationView.selectedItemId = selectedItemId
        }
    }

    private fun replaceFragment(day: WeekDay) {
        val fragment = DayFragment.newInstance(day)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun getTitleForToolbar(): String {
        val title = menuTitle ?: return "Ni naloženo"
        
        var result = title.trim().removeSuffix(".")
            .replace("Tedenski ", "", ignoreCase = true)
            .replace(" od ", ": ", ignoreCase = true)
            .replace(" do ", " – ", ignoreCase = true)

        val monthMap = mapOf(
            "januarja" to "januar", "februarja" to "februar", "marca" to "marec",
            "aprila" to "april", "maja" to "maj", "junija" to "junij",
            "julija" to "julij", "avgusta" to "avgust", "septembra" to "september",
            "oktobra" to "oktober", "novembra" to "november", "decembra" to "december"
        )

        monthMap.forEach { (gen, nom) ->
            result = result.replace(gen, nom, ignoreCase = true)
        }

        return result.replace(Regex("\\s+"), " ")
            .replaceFirstChar { it.uppercase() }
    }
}
