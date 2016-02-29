package net.nikonorov.lazerninja.ui

import android.app.LoaderManager
import android.content.Loader
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.UserProfile
import net.nikonorov.lazerninja.logic.LoaderLogout
import net.nikonorov.lazerninja.logic.LoaderRecovery
import net.nikonorov.lazerninja.logic.api.RecoveryRequest
import net.nikonorov.lazerninja.logic.api.SuccessResponse

/**
 * Created by vitaly on 28.02.16.
 */


class ActivityUserProfile: AppCompatActivity(), LoaderManager.LoaderCallbacks<SuccessResponse>{

    val LOADER_ID = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)

        val logoutBtn = findViewById(R.id.logout_btn)

        logoutBtn.setOnClickListener {
            loaderManager.initLoader(LOADER_ID, null, this@ActivityUserProfile)
        }

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

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<SuccessResponse>? {
        when(id){
            LOADER_ID -> {
                return LoaderLogout(this@ActivityUserProfile)
            }
            else -> return null
        }
    }

    override fun onLoaderReset(p0: Loader<SuccessResponse>?) {
        //throw UnsupportedOperationException()
    }

    override fun onLoadFinished(p0: Loader<SuccessResponse>?, response: SuccessResponse?) {
        Toast.makeText(this@ActivityUserProfile, response?.success, Toast.LENGTH_SHORT).show()
    }

}