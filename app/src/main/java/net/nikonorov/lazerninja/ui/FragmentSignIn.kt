package net.nikonorov.lazerninja.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.nikonorov.lazerninja.FragmentSet
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 27.02.16.
 */

class FragmentSignIn : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_signin, null)

        val signin = view.findViewById(R.id.signin_in_btn)

        signin.setOnClickListener { }

        val signup = view.findViewById(R.id.signin_up_btn)

        signup.setOnClickListener { (activity as ActivitySign).changeFragment(FragmentSet.SIGN_UP) }

        val recoveryBtn = view.findViewById(R.id.signin_rec_btn)

        recoveryBtn.setOnClickListener { (activity as ActivitySign).changeFragment(FragmentSet.SIGN_RECOVERY) }

        return view
    }
}
