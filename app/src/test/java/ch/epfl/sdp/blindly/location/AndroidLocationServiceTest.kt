package ch.epfl.sdp.blindly.location

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test

class AndroidLocationServiceTest {

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun buildWithContextAndProvider() {
        //val locSer = AndroidLocationService.buildFromContextAndProvider(context, null, null)
        //assertTrue(locSer is LocationService)
    }

    @Test
    fun buildWithContextAndCriteria() {
        //val locSer = AndroidLocationService.buildFromContextAndCriteria(context, null, null)
        //assertTrue(locSer is LocationService)
    }
}