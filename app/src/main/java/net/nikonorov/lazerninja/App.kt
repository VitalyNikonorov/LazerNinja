package net.nikonorov.lazerninja

import android.app.Application

/**
 * Created by vitaly on 27.02.16.
 */

class App : Application() {
    companion object {
        var isActive = false
    }

    var profile: UserProfile? = null
}
