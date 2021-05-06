package ch.epfl.sdp.blindly.permission

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.map.UserMapActivity
import ch.epfl.sdp.blindly.permissions.LocationPermission
import ch.epfl.sdp.blindly.permissions.LocationPermission.Companion.LOCATION_PERMISSION_REQUEST_CODE
import ch.epfl.sdp.blindly.permissions.LocationPermission.Companion.requestPermission
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LocationPermissionTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(UserMapActivity::class.java)

    @Test
    fun permissionDeniedDialogFragment() {
        activityRule.scenario.recreate()
        activityRule.scenario.onActivity { act ->
            act.supportFragmentManager.beginTransaction()
            val frag = LocationPermission.Companion.PermissionDeniedDialog.newInstance(false);
            frag.show(act.supportFragmentManager, "test")
        }
        onView(withText(R.string.location_permission_denied)).inRoot(isDialog()).check(
            ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    @Test
    fun permissionDeniedDialogFinishActivityFragment() {
        activityRule.scenario.onActivity { act ->
            act.supportFragmentManager.beginTransaction()
            val frag = LocationPermission.Companion.PermissionDeniedDialog.newInstance(true);
            frag.show(act.supportFragmentManager, "test")
        }
        onView(withText(R.string.location_permission_denied)).inRoot(isDialog()).check(
            ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    @Test
    fun rationaleDialogFragment() {
        activityRule.scenario.recreate()
        activityRule.scenario.onActivity { act ->
            act.supportFragmentManager.beginTransaction()
            val frag = LocationPermission.Companion.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, finishActivity = false
            );
            frag.show(act.supportFragmentManager, "test")
        }
        onView(withText(R.string.permission_rationale_location)).inRoot(isDialog()).check(
            ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    @Test
    fun rationaleDialogFragmentFinishActivty() {
        activityRule.scenario.recreate()
        activityRule.scenario.onActivity { act ->
            act.supportFragmentManager.beginTransaction()
            val frag = LocationPermission.Companion.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, finishActivity = true
            );
            frag.show(act.supportFragmentManager, "test")
        }
        onView(withText(R.string.permission_rationale_location)).inRoot(isDialog()).check(
            ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    @Test
    fun testRequestPermission() {
        activityRule.scenario.onActivity { act ->
            requestPermission(
                act, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
        // We just assert no errors because location is always granted so
        // no dialog will pop-up
    }
}