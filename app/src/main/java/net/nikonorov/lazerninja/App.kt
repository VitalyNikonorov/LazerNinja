package net.nikonorov.lazerninja

import android.app.Application
import android.bluetooth.BluetoothDevice
import com.badlogic.gdx.math.Quaternion
import net.nikonorov.lazerninja.logic.BluetoothClient

/**
 * Created by vitaly on 27.02.16.
 */

class App : Application() {
    var device : BluetoothDevice? = null
//    var xPosition : Float = 0f

    var quaternion = Quaternion(0.0f, 0.0f, 0.0f, 0.0f)

    var client : BluetoothClient? = null

    companion object {
        var isActive = false
        var token: String = ""
        var profile: UserProfile = UserProfile()
    }
}
