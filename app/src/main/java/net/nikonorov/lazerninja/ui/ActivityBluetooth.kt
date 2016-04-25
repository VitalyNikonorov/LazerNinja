package net.nikonorov.lazerninja.ui

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.BluetoothClient
import net.nikonorov.lazerninja.logic.BluetoothServer
import net.nikonorov.lazerninja.ui.adapters.RVAdapter
import java.util.*

/**
 * Created by vitaly on 21.03.16.
 */

class ActivityBluetooth : AppCompatActivity() {

    val REQUEST_ENABLE_BT = 1
    var mBluetoothAdapter : BluetoothAdapter? = null
    internal val REQUEST_CODE_LOCATION = 1
    val devices = ArrayList<BluetoothDevice>()
    var adapter : RVAdapter? = null
    var client : BluetoothClient? = null

    var infoTV : TextView? = null

    var dialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        dialog = ProgressDialog(this)
        dialog?.setTitle("Поиск устройств")
        dialog?.setMessage("Идет поиск устройств")

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }


        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

//        val pairedDevices = mBluetoothAdapter?.getBondedDevices()
//
//        if (pairedDevices!!.size > 0) {
//
//            for (device in pairedDevices) {
//                //text.text = "name: ${device.name}, adress: ${device.address}"
//            }
//        }

        val visibleBtn = findViewById(R.id.visible_btn)

        visibleBtn?.setOnClickListener {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            startActivity(discoverableIntent)
        }

        val discoverBtn = findViewById(R.id.discover_btn)

        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        discoverBtn?.setOnClickListener {

            dialog?.show()

            val hasLocationPermission = ContextCompat.checkSelfPermission(this@ActivityBluetooth,
                    Manifest.permission.ACCESS_FINE_LOCATION)

            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this@ActivityBluetooth,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel("You need to allow access to LOCATION",
                            DialogInterface.OnClickListener { dialog, which ->
                                ActivityCompat.requestPermissions(this@ActivityBluetooth,
                                        arrayOf(Manifest.permission.CAMERA),
                                        REQUEST_CODE_LOCATION)
                            })
                }
                    ActivityCompat.requestPermissions(this@ActivityBluetooth,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_CODE_LOCATION)

            }


             // Don't forget to unregister during onDestroy
            if (mBluetoothAdapter!!.startDiscovery()){
                Toast.makeText(this@ActivityBluetooth, "OK", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@ActivityBluetooth, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        registerReceiver(mReceiver, filter)

        val hostBtn = findViewById(R.id.host_btn)
        hostBtn?.setOnClickListener {
            val host = BluetoothServer(mBluetoothAdapter as BluetoothAdapter, this@ActivityBluetooth)
            host.acceptThread()
            host.start()
        }

        val joinBtn = findViewById(R.id.join_btn)

        val rv = findViewById(R.id.devices_list) as RecyclerView
        rv.layoutManager = LinearLayoutManager(this@ActivityBluetooth)
        adapter = RVAdapter(this@ActivityBluetooth, devices)
        rv.adapter = adapter

        joinBtn?.setOnClickListener {
            client = BluetoothClient(this@ActivityBluetooth)

            (application as App).client = client

            if ((application as App).device != null) {
                client?.connectThread((application as App).device)
                client?.start()
            }
        }

        infoTV = findViewById(R.id.info_tv) as TextView


    }


    var mReceiver  = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            Toast.makeText(this@ActivityBluetooth, action, Toast.LENGTH_SHORT).show()

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                devices.add(device)
                adapter?.notifyDataSetChanged()

                //text?.text = "name: ${device.name}, adress: ${device.address}"
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                dialog?.dismiss()
                //text?.text = "name: ${device.name}, adress: ${device.address}"
            }
        }
    }


    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@ActivityBluetooth)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    override fun onResume() {
        super.onResume()
        devices.clear()
        if (adapter != null){
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        if(mBluetoothAdapter != null) {
            (mBluetoothAdapter as BluetoothAdapter).cancelDiscovery()
        }
        super.onPause()
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
        Log.d("TAAAG", "test")
    }

}