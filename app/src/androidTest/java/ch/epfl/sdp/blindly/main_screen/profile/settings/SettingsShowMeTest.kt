package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.content.Intent
import android.widget.RadioGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.fake_module.FakeUserRepositoryModule.Companion.fakeUser
import ch.epfl.sdp.blindly.main_screen.profile.settings.*
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.enums.ShowMe.*
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

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
        val TEST_SHOW_ME_ID = getShowMeId()

        val act = launchSettingsShowMe()
        val showMeGroup = getShowMeRadioButton(act)
        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_ID))
    }

    @Test
    fun onButtonClickShowMeChanges() {
        val TEST_SHOW_ME_WOMEN = WOMEN.id
        val TEST_SHOW_ME_MEN = MEN.id
        val TEST_SHOW_ME_EVERYONE = EVERYONE.id

        val act = launchSettingsShowMe()
        val showMeGroup = getShowMeRadioButton(act)

        onView(withId(WOMEN.id)).perform(click())
        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_WOMEN))
        onView(withId(MEN.id)).perform(click())
        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_MEN))
        onView(withId(EVERYONE.id)).perform(click())
        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_EVERYONE))
    }

    @Test
    fun onBackPressedUpdateShowMe() {
        val TEST_SHOW_ME_UPDATED = WOMEN.id //Women

        launchSettingsShowMe()
        onView(withId(WOMEN.id)).perform(click())

        Espresso.pressBackUnconditionally()

        val act = launchSettingsShowMe()
        val showMeGroup = getShowMeRadioButton(act)
        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME_UPDATED))
    }

    @Test
    fun onBackPressedDoesNotUpdateIfShowMeIsTheSame() {
        val TEST_SHOW_ME = getShowMeId()

        launchSettingsShowMe()

        //Press back and relaunch
        Espresso.pressBackUnconditionally()
        val act = launchSettingsShowMe()
        val showMeGroup = getShowMeRadioButton(act)

        assertThat(showMeGroup?.checkedRadioButtonId, equalTo(TEST_SHOW_ME))
    }

    private fun launchSettingsShowMe(): ActivityScenario<SettingsShowMe> =
        runBlocking {
            val intent =
                Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)

            intent.putExtra(
                EXTRA_SHOW_ME,
                userRepository.getUser(userHelper.getUserId()!!)!!.showMe
            )
            return@runBlocking ActivityScenario.launch(intent)
        }

    private fun getShowMeRadioButton(act: ActivityScenario<SettingsShowMe>): RadioGroup? {
        var showMeGroup: RadioGroup? = null
        act.onActivity {
            showMeGroup = it.findViewById(R.id.show_me_radio_group)
        }
        return showMeGroup
    }

    private fun getShowMeId(): Int {
        return when (fakeUser.showMe) {
            WOMEN.asString -> R.id.women_radio_button
            MEN.asString -> R.id.men_radio_button
            else -> R.id.everyone_radio_button
        }
    }
}