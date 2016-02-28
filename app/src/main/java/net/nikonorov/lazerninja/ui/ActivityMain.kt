package net.nikonorov.lazerninja.ui

import android.app.LoaderManager
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.UserProfile
import net.nikonorov.lazerninja.logic.LoaderAuth
import net.nikonorov.lazerninja.logic.LoaderUserProfile
import net.nikonorov.lazerninja.logic.api.AuthRequest

/**
 * Created by vitaly on 27.02.16.
 */

class ActivityMain : AppCompatActivity(), LoaderManager.LoaderCallbacks<UserProfile> {

    val LOADER_ID = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById(R.id.auth_btn)

        button.setOnClickListener {
            val intent = Intent(this@ActivityMain, ActivitySign::class.java)
            startActivity(intent)
        }

        val profile = findViewById(R.id.profile_btn)
        profile.setOnClickListener {
            loaderManager.initLoader(LOADER_ID, null, this@ActivityMain)
        }

    }


    override fun onCreateLoader(id: Int, p1: Bundle?): Loader<UserProfile>? {
        when(id){
            LOADER_ID -> {
                return LoaderUserProfile(this)
            }
            else -> return null
        }
    }

    override fun onLoaderReset(p0: Loader<UserProfile>?) {
        throw UnsupportedOperationException()
    }

    override fun onLoadFinished(p0: Loader<UserProfile>?, response: UserProfile?) {
        if(response != null) {
            App.profile = response
            startActivity(Intent(this@ActivityMain, ActivityUserProfile::class.java))
        }else{
            Toast.makeText(this@ActivityMain, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}
