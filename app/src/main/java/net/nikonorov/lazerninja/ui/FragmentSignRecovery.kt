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
import net.nikonorov.lazerninja.logic.LoaderRecovery
import net.nikonorov.lazerninja.logic.api.RecoveryRequest
import net.nikonorov.lazerninja.logic.api.SuccessResponse

/**
 * Created by vitaly on 27.02.16.
 */

class FragmentSignRecovery : Fragment(), LoaderManager.LoaderCallbacks<SuccessResponse> {

    val LOADER_ID = 3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_recovery, null)

        val recoveryBtn = view.findViewById(R.id.signrec_btn)

        val recoverLogin = view.findViewById(R.id.sign_login_recover_et) as EditText

        recoveryBtn.setOnClickListener {

            val login = recoverLogin.text.toString()

            val bundle = Bundle()

            bundle.putString("email", login)

            loaderManager.initLoader(LOADER_ID, bundle, this@FragmentSignRecovery)

        }



        return view
    }

    override fun onCreateLoader(id: Int, bundle: Bundle): Loader<SuccessResponse>? {
        when(id){
            LOADER_ID -> {
                val recoveryRequest = RecoveryRequest(bundle.getString("email"))
                return LoaderRecovery(activity, recoveryRequest)
            }
            else -> return null
        }
    }

    override fun onLoaderReset(p0: Loader<SuccessResponse>?) {
        //throw UnsupportedOperationException()
    }

    override fun onLoadFinished(p0: Loader<SuccessResponse>?, response: SuccessResponse?) {
        Toast.makeText(activity, response?.success, Toast.LENGTH_SHORT).show()
    }

}