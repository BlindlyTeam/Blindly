package ch.epfl.sdp.blindly.match

import android.content.Intent
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.settings.SettingsLocation
import com.yuyakaido.android.cardstackview.CardStackView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.ArrayList

@HiltAndroidTest
class MatchActivityTest {

    private val profiles = createProfiles()


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun firstcardIsWellDisplayed() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MatchActivity::class.java)
        intent.putParcelableArrayListExtra("EXTRA_MATCH_PROFILES", profiles as ArrayList<out Parcelable>)

        ActivityScenario.launch<MatchActivity>(intent)
        onView(withId(R.id.item_name)).check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(profiles[0].name))))
    }


    private fun createProfiles(): List<Profile> {
        val profiles = ArrayList<Profile>()
        profiles.add(Profile(name = "Michelle", age = 25))
        profiles.add(Profile(name = "Jean", age = 32))
        profiles.add(Profile(name = "Jacques", age = 28))
        profiles.add(Profile(name = "Bernadette", age = 35))
        profiles.add(Profile(name = "Jeannine", age = 46))
        profiles.add(Profile(name = "Kilian", age = 18))
        profiles.add(Profile(name = "Melissa", age = 20))
        profiles.add(Profile(name = "Tibor", age = 36))
        profiles.add(Profile(name = "Cagin", age = 27))
        profiles.add(Profile(name = "Capucine", age = 21))
        return profiles
    }
}