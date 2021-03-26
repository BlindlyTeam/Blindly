package ch.epfl.sdp.blindly

import android.app.Application
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp

// Custom app to let hilt hook into
@HiltAndroidApp
class BlindlyApplication : Application() {

}
