package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 27.02.16.
 */

class ActivitySplash : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        isActive = true

        setContentView(R.layout.activity_splash)

        if (App.isActive) {
            val intent = Intent(this, ActivityMain::class.java)
            startActivity(intent)
            finish()
        } else {
            App.isActive = true
            Thread(Runnable {
                try {
                    Thread.sleep(oneSecond.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if (isActive) {
                    val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
                    startActivity(intent)
                    this@ActivitySplash.finish()
                }
            }).start()
        }

    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    companion object {

        private val oneSecond = 1000
        private var isActive = false
    }
}
