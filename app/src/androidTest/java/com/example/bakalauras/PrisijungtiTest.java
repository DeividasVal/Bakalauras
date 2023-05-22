package com.example.bakalauras;

import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules. ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit. Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.IsNot.not;

import com.example.bakalauras.ui.prisijungti.Prisijungti;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PrisijungtiTest {

    @Rule
    public ActivityScenarioRule<Prisijungti> activityRule =
            new ActivityScenarioRule<>(Prisijungti.class);

    @Test
    public void testLoginMokinys() {
        Espresso.onView(withId(R.id.prisijungtiVardas))
                .perform(ViewActions.typeText("lukas"));
        Espresso.onView(withId(R.id.slaptazodisPrisijungti))
                .perform(ViewActions.typeText("lukas"))
                .perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.mokinysRadio))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.prisijungtiButton))
                .perform(ViewActions.click());
        activityRule.getScenario().onActivity(activity -> {
            new Thread(() -> {
                String toastMessage = "Prisijungta sėkmingai!";
                onView(withText(toastMessage))
                        .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                        .check(matches(isDisplayed()));
            }).start();
        });
    }

    @Test
    public void testLoginKorepetitorius() {
        Espresso.onView(withId(R.id.prisijungtiVardas))
                .perform(ViewActions.typeText("gediminas"));
        Espresso.onView(withId(R.id.slaptazodisPrisijungti))
                .perform(ViewActions.typeText("gediminas"))
                .perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.korRadio))
                .perform(ViewActions.click());
        Espresso.onView(withId(R.id.prisijungtiButton))
                .perform(ViewActions.click());
        activityRule.getScenario().onActivity(activity -> {
            new Thread(() -> {
                String toastMessage = "Prisijungta sėkmingai!";
                onView(withText(toastMessage))
                        .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                        .check(matches(isDisplayed()));
            }).start();
        });
    }
}
