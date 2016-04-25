package net.nikonorov.lazerninja.ui

import android.Manifest
import android.app.Activity
import android.app.LoaderManager
import android.content.DialogInterface
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
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

    val REQUEST_CODE_EXTERNAL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById(R.id.auth_btn)

        button?.setOnClickListener {
            val intent = Intent(this@ActivityMain, ActivitySign::class.java)
            startActivity(intent)
        }

        val profile = findViewById(R.id.profile_btn)
        profile?.setOnClickListener {
            loaderManager.initLoader(LOADER_ID, null, this@ActivityMain)
        }

        val gyroBtn = findViewById(R.id.gyro)

        gyroBtn?.setOnClickListener {
            startActivity(Intent(this@ActivityMain, ActivitySaber::class.java))
        }

        val gameBtn = findViewById(R.id.game_btn)

        gameBtn?.setOnClickListener {
            startActivity(Intent(this@ActivityMain, Game::class.java))
        }

        val bluetoothBtn = findViewById(R.id.bluetooth_btn)

        bluetoothBtn?.setOnClickListener {
            startActivity(Intent(this@ActivityMain, ActivityBluetooth::class.java))
        }

        takePermission()

    }

    fun takePermission(){
        val hasReadContactsPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to WRITE_EXTERNAL_STORAGE",
                        DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    REQUEST_CODE_EXTERNAL)
                        })
                return
            }
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_EXTERNAL)
            return
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show()
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
        //throw UnsupportedOperationException()
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
