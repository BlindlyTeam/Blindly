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
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.UserHelper
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
            arrayListOf(BRUNCH, WINE, FASHION, CYCLING, RUNNING),
            arrayListOf(TEA, COFFEE, COMEDY, WALKING, BOARD_GAMES),
            arrayListOf(YOGA, KARAOKE, DOG_LOVER, GAMER, ART),
            arrayListOf(COCKTAILS, DANCING, PHOTOGRAPHY, WRITER, FOODIE),
            arrayListOf(BAKING, SWIMMING, NETFLIX, OUTDOORS, MUSIC),
            arrayListOf(MOVIES, CLIMBING, FISHING, CAT_LOVER, READING),
            arrayListOf(FOOTBALL, SPIRITUALITY, GARDENING)
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

        onView(withId(R.id.warning_p7_1))
            .check(
                matches(
                    withText(
                        containsString(
                            ERROR_MESSAGE_1
                        )
                    )
                )
            )
        onView(withId(R.id.warning_p7_1))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }

    @Test
    fun moreThanAllowedInputShowsError() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditPassions::class.java)
        activity = ActivityScenario.launch(intent)

        onView(withId(R.id.chip10)).perform(click())
        onView(withId(R.id.chip11)).perform(click())
        onView(withId(R.id.chip12)).perform(click())
        onView(withId(R.id.chip13)).perform(click())
        onView(withId(R.id.chip14)).perform(click())
        onView(withId(R.id.chip15)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.warning_p7_2))
            .check(
                matches(
                    withText(
                        containsString(
                            ERROR_MESSAGE_2
                        )
                    )
                )
            )
        onView(withId(R.id.warning_p7_2))
            .check(
                matches(
                    isDisplayed()
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
        passions.forEach {
            when (it) {
                BRUNCH -> ids.add(R.id.chip10)
                WINE -> ids.add(R.id.chip11)
                FASHION -> ids.add(R.id.chip12)
                CYCLING -> ids.add(R.id.chip13)
                RUNNING -> ids.add(R.id.chip14)
                TEA -> ids.add(R.id.chip15)
                COFFEE -> ids.add(R.id.chip16)
                COMEDY -> ids.add(R.id.chip17)
                WALKING -> ids.add(R.id.chip18)
                BOARD_GAMES -> ids.add(R.id.chip19)
                YOGA -> ids.add(R.id.chip20)
                KARAOKE -> ids.add(R.id.chip21)
                DOG_LOVER -> ids.add(R.id.chip22)
                GAMER -> ids.add(R.id.chip23)
                ART -> ids.add(R.id.chip24)
                COCKTAILS -> ids.add(R.id.chip25)
                DANCING -> ids.add(R.id.chip26)
                PHOTOGRAPHY -> ids.add(R.id.chip27)
                WRITER -> ids.add(R.id.chip28)
                FOODIE -> ids.add(R.id.chip29)
                BAKING -> ids.add(R.id.chip30)
                SWIMMING -> ids.add(R.id.chip31)
                NETFLIX -> ids.add(R.id.chip32)
                OUTDOORS -> ids.add(R.id.chip33)
                MUSIC -> ids.add(R.id.chip34)
                MOVIES -> ids.add(R.id.chip35)
                CLIMBING -> ids.add(R.id.chip36)
                FISHING -> ids.add(R.id.chip37)
                CAT_LOVER -> ids.add(R.id.chip38)
                READING -> ids.add(R.id.chip39)
                FOOTBALL -> ids.add(R.id.chip40)
                SPIRITUALITY -> ids.add(R.id.chip41)
                GARDENING -> ids.add(R.id.chip42)
            }
        }
        return ids
    }

}