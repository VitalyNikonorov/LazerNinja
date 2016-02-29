package net.nikonorov.lazerninja.ui

import android.app.LoaderManager
import android.content.Loader
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.UserProfile
import net.nikonorov.lazerninja.logic.LoaderChangePass
import net.nikonorov.lazerninja.logic.LoaderLogout
import net.nikonorov.lazerninja.logic.LoaderRecovery
import net.nikonorov.lazerninja.logic.api.ChangePassRequest
import net.nikonorov.lazerninja.logic.api.RecoveryRequest
import net.nikonorov.lazerninja.logic.api.SuccessResponse

/**
 * Created by vitaly on 28.02.16.
 */


class ActivityUserProfile: AppCompatActivity(), LoaderManager.LoaderCallbacks<SuccessResponse>{

    val LOADER_LOGOUT = 5
    val LOADER_CHANGE_PASS = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)

        val logoutBtn = findViewById(R.id.logout_btn)

        logoutBtn.setOnClickListener {
            loaderManager.initLoader(LOADER_LOGOUT, null, this@ActivityUserProfile)
        }

        val pass1 = findViewById(R.id.new_pass_1) as EditText
        val pass2 = findViewById(R.id.new_pass_2) as EditText

        val changePassBtn = findViewById(R.id.change_pass_btn)

        changePassBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("pass1", pass1.text.toString())
            bundle.putString("pass2", pass2.text.toString())
            loaderManager.initLoader(LOADER_CHANGE_PASS, bundle, this@ActivityUserProfile)
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
            LOADER_LOGOUT -> {
                return LoaderLogout(this@ActivityUserProfile)
            }
            LOADER_CHANGE_PASS ->{
                if(bundle != null) {
                    val changePassRequest = ChangePassRequest(bundle.getString("pass1"), bundle.getString("pass2"))
                    return LoaderChangePass(this@ActivityUserProfile, changePassRequest)
                }else {
                    return null
                }
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