package kz.novatron.myauthenticator.addkey;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kz.novatron.myauthenticator.R;
import kz.novatron.myauthenticator.keys.KeysActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by SMustafa on 25.07.2017.
 */
@RunWith(AndroidJUnit4.class)
public class KeysScreenTest {

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Rule
    public ActivityTestRule<KeysActivity> mKeysActivityTestRule =
            new ActivityTestRule<>(KeysActivity.class);

    @Test
    public void clickAddKeyButton() {
        onView(withId(R.id.fab_add_keys)).perform(click());
        onView(withText(R.string.add_key_type)).check(matches(isDisplayed()));
    }

    @Test
    public void addKeyToKeysListManually(){
        String newKeyEmail = "aab@example.com";
        String newKeySecret = "baaa-ssss-qqqq-wwww";

        onView(withId(R.id.fab_add_keys)).perform(click());

        onView(withText(R.string.type_manually)).perform(click());

        onView(withId(R.id.etEmail)).perform(typeText(newKeyEmail), closeSoftKeyboard());
        onView(withId(R.id.etSecretKey)).perform(typeText(newKeySecret), closeSoftKeyboard());

        onView(withId(R.id.btnAdd)).perform(click());

        onView(withItemText(newKeyEmail)).check(matches(isDisplayed()));
    }

    @Test
    public void clickDeleteBtn(){
        String deleteKeyEmail = "aab@example.com";
        onView(allOf(withId(R.id.btnDelete), hasSibling(withText(deleteKeyEmail)))).perform(click());
        onView(withItemText(deleteKeyEmail)).check(doesNotExist());
    }

    @Test
    public void clickScanQRAndStartCamera() throws Exception {
        Intents.init();

        onView(withId(R.id.fab_add_keys)).perform(click());

        onView(withText(R.string.scan_qr)).perform(click());

        intended(hasComponent(QRScannerActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void checkEmptyFields(){
        Intents.init();
        String newKeyEmail = "";
        String newKeySecret = "";

        onView(withId(R.id.fab_add_keys)).perform(click());

        onView(withText(R.string.type_manually)).perform(click());

        onView(withId(R.id.etEmail)).perform(typeText(newKeyEmail), closeSoftKeyboard());
        onView(withId(R.id.etSecretKey)).perform(typeText(newKeySecret), closeSoftKeyboard());

        onView(withId(R.id.btnAdd)).perform(click());

        intended(hasComponent(KeysActivity.class.getName()), times(0));
        Intents.release();
    }
}
