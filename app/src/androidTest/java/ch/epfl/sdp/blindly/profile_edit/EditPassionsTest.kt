package ch.epfl.sdp.blindly.profile_edit

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.profile.edit.EditPassions
import ch.epfl.sdp.blindly.main_screen.profile.edit.EditProfile
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.enums.Passions.*
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val ERROR_MESSAGE_1 = "Please select at least one!"
private const val ERROR_MESSAGE_2 = "You can not select more than 5!"

@HiltAndroidTest
class EditPassionsTest {

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

    private lateinit var activity: ActivityScenario<EditPassions>
    private lateinit var chipGroup: ChipGroup

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun chipFromIntentAreChecked() {
        val TEST_PASSIONS = arrayListOf(
            arrayListOf(
                BRUNCH.asString,
                WINE.asString,
                FASHION.asString,
                CYCLING.asString,
                RUNNING.asString
            ),
            arrayListOf(
                TEA.asString,
                COFFEE.asString,
                COMEDY.asString,
                WALKING.asString,
                BOARD_GAMES.asString
            ),
            arrayListOf(
                YOGA.asString,
                KARAOKE.asString,
                DOG_LOVER.asString,
                GAMER.asString,
                ART.asString
            ),
            arrayListOf(
                COCKTAILS.asString,
                DANCING.asString,
                PHOTOGRAPHY.asString,
                WRITER.asString,
                FOODIE.asString
            ),
            arrayListOf(
                BAKING.asString,
                SWIMMING.asString,
                NETFLIX.asString,
                OUTDOORS.asString,
                MUSIC.asString
            ),
            arrayListOf(
                MOVIES.asString,
                CLIMBING.asString,
                FISHING.asString,
                CAT_LOVER.asString,
                READING.asString
            ),
            arrayListOf(
                FOOTBALL.asString,
                SPIRITUALITY.asString,
                GARDENING.asString
            )
        )

        TEST_PASSIONS.forEach {
            assertIsCorrect(it)
        }
    }

    private fun assertIsCorrect(passions: ArrayList<String>) {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditPassions::class.java)
        intent.putStringArrayListExtra(PASSIONS, passions)
        activity = ActivityScenario.launch(intent)

        getChipGroup()
        assertThat(chipGroup.checkedChipIds, equalTo(getCheckedChipIds(passions)))
    }

    @Test
    fun whenExtraIsNullNoChipIsChecked() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditPassions::class.java)
        activity = ActivityScenario.launch(intent)

        getChipGroup()
        assertThat(chipGroup.checkedChipIds, equalTo(arrayListOf()))
    }

    @Test
    fun noInputShowsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditPassions::class.java)
        activity = ActivityScenario.launch(intent)

        Espresso.pressBack()

        onView(withId(R.id.at_least_one_warning))
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
    fun moreThanAllowedInputShowsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditPassions::class.java)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.brunch_chip)).perform(click())
        onView(withId(R.id.wine_chip)).perform(click())
        onView(withId(R.id.fashion_chip)).perform(click())
        onView(withId(R.id.cycling_chip)).perform(click())
        onView(withId(R.id.running_chip)).perform(click())
        onView(withId(R.id.tea_chip)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.no_more_than_5_warning))
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

    private fun getChipGroup() {
        activity.onActivity {
            chipGroup = it.findViewById(R.id.passions_chip_group)
        }
    }


    private fun getCheckedChipIds(passions: ArrayList<String>): ArrayList<Int> {
        val ids = arrayListOf<Int>()
        passions.forEach { p ->
            values().forEach { v ->
                if (v.asString == p)
                    ids.add(v.id)
            }
        }
        return ids
    }

}