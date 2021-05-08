package ch.epfl.sdp.blindly.edit

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.edit_info.*
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val ERROR_MESSAGE_1 = "Please select at least one!"
private const val ERROR_MESSAGE_2 = "You can not select more than 3!"

@HiltAndroidTest
class EditSexualOrientationsTest {

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

    private lateinit var activity: ActivityScenario<EditSexualOrientations>
    private lateinit var chipGroup: ChipGroup

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun chipFromIntentAreClicked() {
        val TEST_SEXUAL_ORIENTATIONS = arrayListOf(
            arrayListOf(STRAIGHT, LESBIAN, GAY),
            arrayListOf(BISEXUAL, ASEXUAL, DEMISEXUAL),
            arrayListOf(PANSEXUAL, QUEER, QUESTIONING)
        )

        TEST_SEXUAL_ORIENTATIONS.forEach {
            assertIsCorrect(it)
        }
    }

    private fun assertIsCorrect(sexualOrientations: ArrayList<String>) {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditSexualOrientations::class.java)
        intent.putStringArrayListExtra(SEXUAL_ORIENTATIONS, sexualOrientations)
        activity = ActivityScenario.launch(intent)
        getChipGroup()
        assertThat(chipGroup.checkedChipIds, equalTo(getCheckedChipIds(sexualOrientations)))
    }

    @Test
    fun whenExtraIsNullNoChipIsChecked() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditSexualOrientations::class.java)
        activity = ActivityScenario.launch(intent)

        getChipGroup()
        assertThat(chipGroup.checkedChipIds, equalTo(arrayListOf()))
    }


    private fun getChipGroup() {
        activity.onActivity {
            chipGroup = it.findViewById(R.id.sexual_orientations_chip_group)
        }

    }

    private fun getCheckedChipIds(sexualOrientations: ArrayList<String>): ArrayList<Int> {
        val ids = arrayListOf<Int>()
        sexualOrientations.forEach {
            when (it) {
                STRAIGHT -> ids.add(R.id.chip1)
                LESBIAN -> ids.add(R.id.chip2)
                GAY -> ids.add(R.id.chip3)
                BISEXUAL -> ids.add(R.id.chip4)
                ASEXUAL -> ids.add(R.id.chip5)
                DEMISEXUAL -> ids.add(R.id.chip6)
                PANSEXUAL -> ids.add(R.id.chip7)
                QUEER -> ids.add(R.id.chip8)
                QUESTIONING -> ids.add(R.id.chip9)
            }
        }
        return ids
    }

    @Test
    fun noInputOutputsError() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditSexualOrientations::class.java)
        activity = ActivityScenario.launch(intent)

        Espresso.pressBack()

        onView(withId(R.id.warning_p5_1))
            .check(
                matches(
                    withText(
                        containsString(
                            ERROR_MESSAGE_1
                        )
                    )
                )
            )
    }

    @Test
    fun moreThanAllowedInputOutputsError() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditSexualOrientations::class.java)
        activity = ActivityScenario.launch(intent)
        onView(withId(R.id.chip1)).perform(click())
        onView(withId(R.id.chip2)).perform(click())
        onView(withId(R.id.chip3)).perform(click())
        onView(withId(R.id.chip4)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.warning_p5_2))
            .check(
                matches(
                    withText(
                        containsString(
                            ERROR_MESSAGE_2
                        )
                    )
                )
            )
    }
}