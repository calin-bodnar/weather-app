package calin.bodnar.weatherapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import calin.bodnar.weatherapp.weather.WeatherActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WeatherScreenTest {

    @Rule
    public ActivityTestRule<WeatherActivity> weatherActivityTestRule =
            new ActivityTestRule<>(WeatherActivity.class);

    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(
                weatherActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(
                weatherActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void swipeRefresh_reloadsListView() {
        onView(withId(R.id.swipe_refresh)).perform(swipeDown());
        onView(withId(R.id.swipe_refresh)).check(matches(isDisplayed()));
    }

    @Test
    public void clickCity_opensWeatherDetailsUi() {
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        onView(withId(R.id.name)).check(matches(isDisplayed()));
    }

    @Test
    public void clickBackButtonOnWeatherDetails_showsWeatherListUi() {
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
        pressBack();
        onView(withId(R.id.swipe_refresh)).check(matches(isDisplayed()));
    }
}
