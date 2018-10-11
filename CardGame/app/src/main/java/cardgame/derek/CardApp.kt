package cardgame.derek

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree



/**
 * User: derek
 * Date: 11/10/18 1:20 PM
 */
class CardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}