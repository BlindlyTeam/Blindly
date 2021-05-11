package ch.epfl.sdp.blindly.profile_edit

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.enums.Passions
import ch.epfl.sdp.blindly.user.enums.SexualOrientations
import ch.epfl.sdp.blindly.user.enums.SexualOrientations.*
import ch.epfl.sdp.blindly.user.storage.UserCache
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
            arrayListOf(STRAIGHT.asString, LESBIAN.asString, GAY.asString),
            arrayListOf(BISEXUAL.asString, ASEXUAL.asString, DEMISEXUAL.asString),
            arrayListOf(PANSEXUAL.asString, QUEER.asString, QUESTIONING.asString)
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
        sexualOrientations.forEach { p ->
            SexualOrientations.values().forEach { v ->
                if(v.asString == p)
                    ids.add(v.id)
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

        onView(withId(R.id.at_least_1_warning))
            .check(
                matches(
                    withText(
                        containsString(
                            ERROR_MESSAGE_1
                        )
                    )
                )
            )
        onView(withId(R.id.at_least_1_warning))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }

    @Test
    fun moreThanAllowedInputOutputsError() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditSexualOrientations::class.java)
        activity = ActivityScenario.launch(intent)
        onView(withId(R.id.straight_chip)).perform(click())
        onView(withId(R.id.lesbian_chip)).perform(click())
        onView(withId(R.id.gay_chip)).perform(click())
        onView(withId(R.id.bisexual_chip)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.no_more_than_3_warning))
            .check(
                matches(
                    withText(
                        containsString(
                            ERROR_MESSAGE_2
                        )
                    )
                )
            )
        onView(withId(R.id.no_more_than_3_warning))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }
}