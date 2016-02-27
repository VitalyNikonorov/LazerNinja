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
import net.nikonorov.lazerninja.FragmentSet
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.api.AuthRequest
import net.nikonorov.lazerninja.logic.api.AuthToken
import net.nikonorov.lazerninja.logic.api.LoaderAuth

/**
 * Created by vitaly on 27.02.16.
 */

class FragmentSignIn : Fragment(), LoaderManager.LoaderCallbacks<AuthToken> {

    val LOADER_ID = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_signin, null)

        val signin = view.findViewById(R.id.signin_in_btn)

        val loginET = view.findViewById(R.id.sign_login_et) as EditText
        val passET  = view.findViewById(R.id.sign_pass_et) as EditText

        signin.setOnClickListener {

            val login = loginET.text.toString()
            val pass = passET.text.toString()

            val bundle = Bundle()

            bundle.putString("username", login)
            bundle.putString("password", pass)


            loaderManager.initLoader(LOADER_ID, bundle, this@FragmentSignIn)

        }

        val signup = view.findViewById(R.id.signin_up_btn)

        signup.setOnClickListener { (activity as ActivitySign).changeFragment(FragmentSet.SIGN_UP) }

        val recoveryBtn = view.findViewById(R.id.signin_rec_btn)

        recoveryBtn.setOnClickListener { (activity as ActivitySign).changeFragment(FragmentSet.SIGN_RECOVERY) }

        return view
    }

    override fun onCreateLoader(id: Int, bundle: Bundle): Loader<AuthToken>? {
        when(id){
            LOADER_ID -> {
                val authBody = AuthRequest(bundle.getString("username"), bundle.getString("password"))
            return LoaderAuth(activity, authBody)
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
