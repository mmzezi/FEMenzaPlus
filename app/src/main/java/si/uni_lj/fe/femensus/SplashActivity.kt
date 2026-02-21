package si.uni_lj.fe.femensus

import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideSystemBars()

        val source: ImageDecoder.Source = ImageDecoder.createSource(
            resources, R.drawable.loading_pot
        )
        val drawable: Drawable = ImageDecoder.decodeDrawable(source)

        val loadingPot = findViewById<ImageView>(R.id.loading_pot_id)
        loadingPot.setImageDrawable(drawable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (drawable as? AnimatedImageDrawable)?.start()
        }

        CoroutineScope(Dispatchers.Main).launch {
            val result = Scraper.fetchAndParse()
            result.onSuccess {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure {
                Toast.makeText(
                    this@SplashActivity,
                    "Nalaganje ni uspelo, preverite internetno povezavo.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            windowInsetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }
}
