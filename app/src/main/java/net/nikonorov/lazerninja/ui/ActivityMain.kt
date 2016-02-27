package net.nikonorov.lazerninja.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 27.02.16.
 */

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById(R.id.auth_btn)

        button.setOnClickListener {
            val intent = Intent(this@ActivityMain, ActivitySign::class.java)
            startActivity(intent)
        }
    }
}
