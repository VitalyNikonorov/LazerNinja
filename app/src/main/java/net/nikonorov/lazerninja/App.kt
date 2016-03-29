package net.nikonorov.lazerninja

import android.app.Application
import android.bluetooth.BluetoothDevice

/**
 * Created by vitaly on 27.02.16.
 */

class App : Application() {
    var device : BluetoothDevice? = null

    companion object {
        var isActive = false
        var token: String = ""
        var profile: UserProfile = UserProfile()
    }
}
