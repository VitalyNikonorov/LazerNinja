package net.nikonorov.lazerninja.logic

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.ui.ActivityBluetooth
import java.io.IOException
import java.util.*

/**
 * Created by vitaly on 22.03.16.
 */

class BluetoothServer(val mBluetoothAdapter : BluetoothAdapter, val activity : ActivityBluetooth) : Thread() {

    val NAME = "HOST"
    val MY_UUID = UUID.fromString(activity.getString(R.string.UUID))


    var mmServerSocket : BluetoothServerSocket? = null

    fun acceptThread() {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        var tmp : BluetoothServerSocket? = null
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID)
        } catch (e : IOException) { }
        mmServerSocket = tmp
    }

    override fun run() {
        var socket : BluetoothSocket? = null
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket?.accept()
            } catch (e : IOException) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket(socket);
                mmServerSocket?.close()
                break
            }
        }
    }

    fun manageConnectedSocket(socket: BluetoothSocket){
        val inputStream = socket.inputStream
        var readBytes : Int = 0
        val buffer = ByteArray(1024)
        var temp: String? = null

        while (true) {
            try {
                readBytes = inputStream.read(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (readBytes != -1) {

                val readBuffer = ByteArray(readBytes)

                System.arraycopy(buffer, 0, readBuffer, 0, readBytes)

                temp = String(readBuffer)

                if(temp.equals("1")) {
                    (activity.application as App).xPosition += 1f
                }

                Log.i("SERVERLOG", temp)
            }
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    fun cancel() {
        try {
            mmServerSocket?.close()
        } catch (e : IOException) { }
    }
}