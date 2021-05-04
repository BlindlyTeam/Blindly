package ch.epfl.sdp.blindly.main_screen

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.protobuf.NullValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.isNull

@HiltAndroidTest
class MainScreenTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun afterEach() {
        Intents.release()
    }

    @Test
    fun backButtonHandlesExit(){
        Espresso.pressBack()
        onView(withText("Yes")).perform(click())
        Thread.sleep(500)
        assertThat(activityRule.scenario.state, `is`(Lifecycle.State.DESTROYED))
    }

    @Test
    fun backButtonHandlesStayInApp(){
        Espresso.pressBack()
        onView(withText("No")).perform(click())
        Thread.sleep(500)
        assertThat(activityRule.scenario.state, `is`(Lifecycle.State.RESUMED))
    }


}