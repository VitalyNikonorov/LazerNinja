package net.nikonorov.lazerninja

import android.app.Application
import android.bluetooth.BluetoothDevice
import net.nikonorov.lazerninja.logic.BluetoothClient

/**
 * Created by vitaly on 27.02.16.
 */

class App : Application() {
    var device : BluetoothDevice? = null
    var xPosition : Float = 0f

    var client : BluetoothClient? = null

    companion object {
        var isActive = false
        var token: String = ""
        var profile: UserProfile = UserProfile()
    }
}
