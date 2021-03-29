package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R

import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_GENRE_MORE = "More"
const val TEST_GENRE_MEN = "Man"
const val TEST_GENRE_WOMEN = "Woman"

@RunWith(AndroidJUnit4::class)
class TestProfileGender {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileGender::class.java)

    private val NO_INPUT_ERROR = "Please select one!"

    @Test
    fun noInputOutputsError() {
        Intents.init()
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        intended(hasComponent(ProfileGenderMore::class.java.name), times(0))
        Espresso.onView(withId(R.id.warning_p4))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                NO_INPUT_ERROR
                                        )
                                )
                        )
                );
        Intents.release()
    }

    @Test
    fun womanFiresProfileOrientation() {
        Intents.init()
        val buttonWoman = Espresso.onView(withId(R.id.sex1_user))
        buttonWoman.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(Matchers.allOf(hasComponent(ProfileOrientation::class.java.name),
                IntentMatchers.hasExtras(BundleMatchers.hasEntry(EXTRA_GENRE, TEST_GENRE_WOMEN))))
        Intents.release()
    }

    @Test
    fun manFiresProfileOrientation() {
        Intents.init()
        val buttonWoman = Espresso.onView(withId(R.id.sex2_user))
        buttonWoman.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(Matchers.allOf(hasComponent(ProfileOrientation::class.java.name),
                IntentMatchers.hasExtras(BundleMatchers.hasEntry(EXTRA_GENRE, TEST_GENRE_MEN))))
        Intents.release()
    }

    @Test
    fun moreFiresProfileGenderMore() {
        Intents.init()
        val buttonMore = Espresso.onView(withId(R.id.sex3_more))
        buttonMore.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(Matchers.allOf(hasComponent(ProfileGenderMore::class.java.name),
                IntentMatchers.hasExtras(BundleMatchers.hasEntry(EXTRA_GENRE, TEST_GENRE_MORE))))
        Intents.release()
    }


}