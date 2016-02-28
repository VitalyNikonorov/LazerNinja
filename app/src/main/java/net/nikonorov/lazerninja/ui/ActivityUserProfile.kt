package net.nikonorov.lazerninja.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.UserProfile

/**
 * Created by vitaly on 28.02.16.
 */


class ActivityUserProfile: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)

    }

    override fun onResume() {
        super.onResume()

        val username = findViewById(R.id.profile_username) as TextView
        val email = findViewById(R.id.profile_email) as TextView
        val firstName = findViewById(R.id.profile_first_name) as TextView
        val lastName = findViewById(R.id.profile_last_name) as TextView


        username.text = App.profile.username
        email.text = App.profile.email
        firstName.text = App.profile.first_name
        lastName.text = App.profile.last_name
    }



}