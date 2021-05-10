package ch.epfl.sdp.blindly.edit

import android.content.Intent
import android.widget.RadioGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.edit_info.EditGender
import ch.epfl.sdp.blindly.edit_info.EditProfile
import ch.epfl.sdp.blindly.profile_setup.ProfileOrientation
import ch.epfl.sdp.blindly.profile_setup.TEST_GENDER_WOMEN
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TEST_GENDER_MORE = "More"
private const val TEST_GENDER_MAN = "Man"
private const val TEST_GENDER_WOMAN = "Woman"
private const val TEST_GENDER_FLOWER = "AFlower"

private const val WOMAN = R.id.woman_radio_button
private const val MAN = R.id.man_radio_button
private const val MORE = R.id.more_radio_button

private const val BLANK_SPECIFICATION = "   "
private const val INCORRECT_CHARS_SPECIFICATION = "Abc;;de"
private const val NO_INPUT = ""
private const val ERROR_MESSAGE = "Please specify!"
private const val ERROR_CHARACTERS = "Please use only letters."

@HiltAndroidTest
class EditGenderTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(EditProfile::class.java)

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

    private lateinit var activity: ActivityScenario<EditGender>
    private lateinit var radioGroup: RadioGroup

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun genderTextForMoreIsMoreIfMoreIsNotSelectedAtTheStart() {
        var intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_WOMAN)
        activity = ActivityScenario.launch(intent)
        onView(withId(MORE)).check(matches(withText(TEST_GENDER_MORE)))

        intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_WOMEN)
        activity = ActivityScenario.launch(intent)
        onView(withId(MORE)).check(matches(withText(TEST_GENDER_MORE)))
    }

    @Test
    fun genderTextForMoreIsIntentIfIntentDifferentFromManOrWoman() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_FLOWER)
        activity = ActivityScenario.launch(intent)

        onView(withId(MORE)).check(matches(withText(TEST_GENDER_FLOWER)))
    }

    @Test
    fun ifMoreIsNotCheckedGenderEditorIsHidden() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_MAN)
        activity = ActivityScenario.launch(intent)

        onView(withId(WOMAN)).perform(click())

        onView(withId(R.id.edit_gender)).check(matches(not(isDisplayed())))
        onView(withId(R.id.edit_gender_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.update_gender_more)).check(matches(not(isDisplayed())))
    }

    @Test
    fun ifMoreIsCheckedAndTextIsMoreGenderEditorIsDisplayed() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_WOMAN)
        activity = ActivityScenario.launch(intent)

        onView(withId(MORE)).perform(click())
        onView(withId(R.id.update_gender_more)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_gender)).check(matches(isDisplayed()))
    }

    @Test
    fun ifMoreTextIsCustomeThanEditGenderButtonIsDisplayed() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_FLOWER)
        activity = ActivityScenario.launch(intent)
        onView(withId(R.id.edit_gender_button)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnEditGenderDisplayesGenderEditor() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_FLOWER)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender_button)).perform(click())
        onView(withId(R.id.update_gender_more)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_gender)).check(matches(isDisplayed()))
    }


    @Test
    fun whenExtraIsNullMoreIsClicked() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        activity = ActivityScenario.launch(intent)
        getRadioGroup()
        assertThat(
            radioGroup.checkedRadioButtonId, equalTo(
                MORE
            )
        )
    }

    @Test
    fun correctGenderMoreSetsMoreTextsToNewGender() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_MORE)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender_button)).perform(click())


        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(TEST_GENDER_FLOWER)
        )
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.update_gender_more)).perform(click())
        onView(withId(MORE)).check(matches(withText(TEST_GENDER_FLOWER)))
    }

    @Test
    fun incorrectCharOutputsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_MORE)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender_button)).perform(click())

        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(INCORRECT_CHARS_SPECIFICATION)
        )
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.update_gender_more)).perform(click())
        onView(withId(R.id.warning2_p4_2))
            .check(
                matches(
                    withText(
                        Matchers.containsString(
                            ERROR_CHARACTERS
                        )
                    )
                )
            )
    }

    @Test
    fun noInputOutputsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_MORE)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender_button)).perform(click())

        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(NO_INPUT)
        )
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.update_gender_more)).perform(click())
        onView(withId(R.id.warning1_p4_2))
            .check(
                matches(
                    withText(
                        Matchers.containsString(ERROR_MESSAGE)
                    )
                )
            )
    }


    @Test
    fun blankInputOutputsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, TEST_GENDER_MORE)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender_button)).perform(click())

        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(BLANK_SPECIFICATION)
        )
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.update_gender_more)).perform(click())
        onView(withId(R.id.warning1_p4_2))
            .check(
                matches(
                    withText(
                        Matchers.containsString(ERROR_MESSAGE)
                    )
                )
            )
    }

    private fun getRadioGroup() {
        activity.onActivity {
            radioGroup = it.findViewById(R.id.gender_radio_group)
        }
    }
}