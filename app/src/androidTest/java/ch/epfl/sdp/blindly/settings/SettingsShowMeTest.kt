package ch.epfl.sdp.blindly.settings

import android.content.Intent
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.text.Typography.dagger

@HiltAndroidTest
class SettingsShowMeTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Settings::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Inject
    lateinit var db: FirebaseFirestore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun showMeFromIntentIsDisplayedProperly() {
        val TEST_SHOW_ME = fakeUser.showMe
        val TEST_SHOW_ME_ID = R.id.everyone_radio_button // fakeUser showMe is Everyone
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)
        intent.putExtra(EXTRA_SHOW_ME, TEST_SHOW_ME)


        val act = ActivityScenario.launch<SettingsShowMe>(intent)
        var showMeGroup: RadioGroup? = null
        act.onActivity {
            showMeGroup = it.findViewById(R.id.show_me_radio_group)
        }

        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_ID))
    }

    @Test
    fun onButtonClickShowMeChanges() {
        val TEST_SHOW_ME = fakeUser.showMe
        val TEST_SHOW_ME_ID = R.id.women_radio_button // WOMEN
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)
        intent.putExtra(EXTRA_SHOW_ME, TEST_SHOW_ME)

        val act = ActivityScenario.launch<SettingsShowMe>(intent)
        var showMeGroup: RadioGroup? = null
        act.onActivity {
            showMeGroup = it.findViewById(R.id.show_me_radio_group)
        }
        onView(withId(R.id.women_radio_button)).perform(click())

        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_ID))
    }
}