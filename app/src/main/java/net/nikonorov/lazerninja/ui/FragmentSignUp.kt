package net.nikonorov.lazerninja.ui

import android.app.Fragment
import android.app.LoaderManager
import android.content.Loader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.LoaderReg
import net.nikonorov.lazerninja.logic.api.AuthToken
import net.nikonorov.lazerninja.logic.api.RegRequest

/**
 * Created by vitaly on 27.02.16.
 */

class FragmentSignUp : Fragment(), LoaderManager.LoaderCallbacks<AuthToken> {

    val LOADER_ID = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_signup, null)

        val signupBtn = view.findViewById(R.id.sign_up_btn)

        val loginET = view.findViewById(R.id.signup_login_et) as EditText
        val emailET = view.findViewById(R.id.signup_email_et) as EditText
        val firstNameET = view.findViewById(R.id.signup_first_name_et) as EditText
        val lastNameET = view.findViewById(R.id.signup_last_name_et) as EditText
        val passET = view.findViewById(R.id.signup_pass_et) as EditText
        val confirmPassET = view.findViewById(R.id.signup_confirm_pass_et) as EditText

        signupBtn.setOnClickListener {

            val login = loginET.text.toString()
            val email = emailET.text.toString()
            val firstName = firstNameET.text.toString()
            val lastName = lastNameET.text.toString()
            val pass = passET.text.toString()
            val confirmPass = confirmPassET.text.toString()

            val regBody = RegRequest(username = login, email = email, first_name = firstName, last_name = lastName, password1 = pass, password2 = confirmPass)


            val bundle = Bundle()
            bundle.putString("username", login)
            bundle.putString("email", email)
            bundle.putString("first_name", firstName)
            bundle.putString("last_name", lastName)
            bundle.putString("password1", pass)
            bundle.putString("password2", confirmPass)

            loaderManager.initLoader(LOADER_ID, bundle, this@FragmentSignUp)

        }

        return view
    }


    override fun onCreateLoader(id: Int, args: Bundle): Loader<AuthToken>? {

        when(id){

            LOADER_ID -> {
                val regBody = RegRequest(
                        username = args.getString("username"),
                        email = args.getString("email"),
                        first_name = args.getString("first_name"),
                        last_name = args.getString("last_name"),
                        password1 = args.getString("password1"),
                        password2 = args.getString("password2"))

                return LoaderReg(activity, regBody)
            }
            else -> return null
        }

    }

    override fun onLoaderReset(p0: Loader<AuthToken>?) {
        //throw UnsupportedOperationException()
    }

    override fun onLoadFinished(p0: Loader<AuthToken>?, token: AuthToken?) {
        Toast.makeText(activity, token?.key, Toast.LENGTH_SHORT).show()
    }


}
