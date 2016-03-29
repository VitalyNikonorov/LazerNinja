package net.nikonorov.lazerninja.logic

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import net.nikonorov.lazerninja.R
import java.io.BufferedOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by vitaly on 22.03.16.
 */

class BluetoothClient (val context : Context) : Thread() {
    var mmSocket : BluetoothSocket? = null
    var mmDevice : BluetoothDevice? = null
    val MY_UUID = UUID.fromString(context.getString(R.string.UUID))

    var bout : BufferedOutputStream? = null

    fun connectThread(device : BluetoothDevice?) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final

        var tmp : BluetoothSocket? = null
        mmDevice = device

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device?.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (e : IOException) { }
        mmSocket = tmp;
    }

    override fun run() {
        // Cancel discovery because it will slow down the connection
        //mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket?.connect();
        } catch (connectException : IOException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket?.close();
            } catch (closeException : IOException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(mmSocket);
    }

    fun manageConnectedSocket(socket : BluetoothSocket?){
        val os = socket?.getOutputStream()
        bout = BufferedOutputStream(os)

        bout?.write("data".toByteArray())
        bout?.flush()
    }

    fun send(data : String){
        bout?.write(data.toByteArray())
        bout?.flush()
    }

    /** Will cancel an in-progress connection, and close the socket */
    fun cancel() {
        try {
            mmSocket?.close();
        } catch (e : IOException) { }
    }
}