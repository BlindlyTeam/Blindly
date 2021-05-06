package ch.epfl.sdp.blindly.map

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.map.UserMapActivity.Companion.POINTS
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class UserMapTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun openAndDisplayUserMap() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), UserMapActivity::class.java)

        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    fun openAndDisplayUserWithPoints() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), UserMapActivity::class.java)
        intent.putExtra(POINTS, LatLng(0.0, 0.0))

        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    fun openAndDisplayUserWithoutPoints() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), UserMapActivity::class.java)

        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}