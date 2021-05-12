package ch.epfl.sdp.blindly.profile_edit

import android.content.Intent
import android.widget.RadioGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.profile.edit.EditGender
import ch.epfl.sdp.blindly.main_screen.profile.edit.EditProfile
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.enums.Gender.*
import ch.epfl.sdp.blindly.user.storage.UserCache
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

private const val NO_BUTTON_SELECTED = -1

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
    fun clickingOnRadioButtonWorks() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, MORE.asString)
        activity = ActivityScenario.launch(intent)

        getRadioGroup()
        assertThat(radioGroup.checkedRadioButtonId, equalTo(MORE.id))
        onView(withId(WOMAN.id)).perform(click())
        assertThat(radioGroup.checkedRadioButtonId, equalTo(WOMAN.id))
        onView(withId(MAN.id)).perform(click())
        assertThat(radioGroup.checkedRadioButtonId, equalTo(MAN.id))
    }

    @Test
    fun editGenderIsVisibleOnlyIfMoreIsClicked() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, MORE.asString)
        activity = ActivityScenario.launch(intent)
        //More is clicked as it was passed as an intent
        getRadioGroup()
        val editGender = onView(withId(R.id.edit_gender))
        editGender.check(matches(isDisplayed()))

        //Clicking on another gender hised the editText
        onView(withId(WOMAN.id)).perform(click())
        editGender.check(matches(not(isDisplayed())))

        //Clicking on more shows the editText
        onView(withId(MORE.id)).perform(click())
        editGender.check(matches(isDisplayed()))
    }

    @Test
    fun editGenderIsNotVisibleOnCreateIfMoreIsNotClicked() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, MAN.asString)
        activity = ActivityScenario.launch(intent)

        getRadioGroup()
        val editGender = onView(withId(R.id.edit_gender))
        editGender.check(matches(not(isDisplayed())))
    }

    @Test
    fun whenExtraIsNullNoButtonIsClicked() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        activity = ActivityScenario.launch(intent)
        getRadioGroup()
        assertThat(
            radioGroup.checkedRadioButtonId, equalTo(
                NO_BUTTON_SELECTED
            )
        )
    }

    @Test
    fun incorrectCharOutputsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, MORE.asString)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(INCORRECT_CHARS_SPECIFICATION)
        )
        Espresso.closeSoftKeyboard()

        Espresso.pressBack()
        onView(withId(R.id.use_only_letters_warning))
            .check(
                matches(
                    withText(
                        Matchers.containsString(
                            ERROR_CHARACTERS
                        )
                    )
                )
            )
        onView(withId(R.id.use_only_letters_warning))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }

    @Test
    fun noInputOutputsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, MORE.asString)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(NO_INPUT)
        )
        Espresso.closeSoftKeyboard()

        Espresso.pressBack()
        onView(withId(R.id.please_specify_warning))
            .check(
                matches(
                    withText(
                        Matchers.containsString(ERROR_MESSAGE)
                    )
                )
            )
        onView(withId(R.id.please_specify_warning))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }


    @Test
    fun blankInputOutputsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditGender::class.java)
        intent.putExtra(GENDER, MORE.asString)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.edit_gender)).perform(
            ViewActions.clearText(),
            ViewActions.typeText(BLANK_SPECIFICATION)
        )
        Espresso.closeSoftKeyboard()

        Espresso.pressBack()
        onView(withId(R.id.please_specify_warning))
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