package net.nikonorov.lazerninja.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 27.02.16.
 */

class FragmentSignRecovery : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_recovery, null)

        val revoveryBtn = view.findViewById(R.id.signrec_btn)

        return view
    }

}