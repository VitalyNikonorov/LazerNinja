package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import net.nikonorov.lazerninja.FragmentSet
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 27.02.16.
 */

class ActivitySign : Activity() {

    private val fragments = arrayOfNulls<Fragment>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign)

        fragments[FragmentSet.SIGN_IN] = FragmentSignIn()
        fragments[FragmentSet.SIGN_UP] = FragmentSignUp()
        fragments[FragmentSet.SIGN_RECOVERY] = FragmentSignRecovery()

        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragment_sign_place, fragments[FragmentSet.SIGN_IN])
        transaction.commit()

    }

    fun changeFragment(newFragment: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_sign_place, fragments[newFragment])
        transaction.commit()
    }
}
