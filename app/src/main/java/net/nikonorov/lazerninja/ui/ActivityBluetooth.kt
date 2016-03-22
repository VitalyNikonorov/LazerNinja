package net.nikonorov.lazerninja.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 21.03.16.
 */

class ActivityBluetooth: AppCompatActivity() {

    val REQUEST_ENABLE_BT = 1
    var mReceiver : BroadcastReceiver? = null
    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    var text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bluetooth)

        text = findViewById(R.id.bluetooth_info) as TextView

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }


        if (!mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        var mReceiver  = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND == action) {
                    // Get the BluetoothDevice object from the Intent
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    // Add the name and address to an array adapter to show in a ListView
                    text?.text = "name: ${device.name}, adress: ${device.address}"
                }
            }
        }

        val pairedDevices = mBluetoothAdapter.getBondedDevices()

        if (pairedDevices.size > 0) {

            for (device in pairedDevices) {
                //text.text = "name: ${device.name}, adress: ${device.address}"
            }
        }

        val visibleBtn = findViewById(R.id.visible_btn)

        visibleBtn.setOnClickListener {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            startActivity(discoverableIntent)
        }

        val discoverBtn = findViewById(R.id.discover_btn)

        discoverBtn.setOnClickListener {
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }

        if (mBluetoothAdapter.startDiscovery()){
            Toast.makeText(this@ActivityBluetooth, "OK", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@ActivityBluetooth, "Error", Toast.LENGTH_SHORT).show()
        }



    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }


}