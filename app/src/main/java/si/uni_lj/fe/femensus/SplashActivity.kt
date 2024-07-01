package si.uni_lj.fe.femensus

import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import java.lang.System.currentTimeMillis
import java.lang.System.runFinalizersOnExit
import java.lang.Thread.sleep
import java.util.Timer
import java.util.TimerTask

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Hide the system bars for a full-screen experience
        hideSystemBars()


        val source :ImageDecoder.Source = ImageDecoder.createSource(
            resources, R.drawable.loading_pot
        )
        val drawable :Drawable = ImageDecoder.decodeDrawable(source)

        val loading_pot = findViewById<ImageView>(R.id.loading_pot_id)
        loading_pot.setImageDrawable(drawable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (drawable as? AnimatedImageDrawable)?.start()
        }


        // Start the fetch of the menus
        Scraper.fetchAndParse()

        // Delay until the data is fetched
        val timer = Timer()
        val ctx = this

        val time = currentTimeMillis()
        var toastShown = false;

        timer.schedule(object : TimerTask() {
            override fun run() {
                // Start the fetch of the menus
                Scraper.fetchAndParse()
                if (parsedMenu != null) {
                    val intent = Intent(ctx, MainActivity::class.java)
                    startActivity(intent)

                    timer.cancel()
                    finish()
                }else{
                    if (time + 4000 < currentTimeMillis() && !toastShown){
                        runOnUiThread {
                            Toast.makeText(
                                ctx,
                                "Loading failed, check internet connection.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        toastShown = true;
                    }
                }
            }
        }, 1000, 100)
    }

    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            if (windowInsetsController != null) {
                windowInsetsController.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                windowInsetsController.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
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
