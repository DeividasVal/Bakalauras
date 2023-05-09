package com.example.bakalauras;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AtsijungtiTest {

    @Rule
    public ActivityScenarioRule<Prisijungti> activityRule =
            new ActivityScenarioRule<>(Prisijungti.class);

    @Test
    public void testAtsijungtiMokinys() {
        Espresso.onView(ViewMatchers.withId(R.id.prisijungtiVardas))
                .perform(ViewActions.typeText("lukas"));
        Espresso.onView(ViewMatchers.withId(R.id.slaptazodisPrisijungti))
                .perform(ViewActions.typeText("lukas"))
                .perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.mokinysRadio))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.prisijungtiButton))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
                .perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.nav_atsijungti))
                .perform(ViewActions.click());

        String toastMessage = "Sėkmingai atsijungtėte!";
        activityRule.getScenario().onActivity(activity -> {
            new Thread(() -> {
                onView(withText(toastMessage))
                        .inRoot(RootMatchers.withDecorView(not(activity.getWindow().getDecorView())))
                        .check(matches(isDisplayed()));
            }).start();
        });
    }

    @Test
    public void testAtsijungtiKorepetitorius() {
        Espresso.onView(ViewMatchers.withId(R.id.prisijungtiVardas))
                .perform(ViewActions.typeText("gediminas"));
        Espresso.onView(ViewMatchers.withId(R.id.slaptazodisPrisijungti))
                .perform(ViewActions.typeText("gediminas"))
                .perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.korRadio))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.prisijungtiButton))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
                .perform(ViewActions.swipeUp());
        Espresso.onView(ViewMatchers.withId(R.id.nav_atsijungti))
                .perform(ViewActions.click());

        String toastMessage = "Sėkmingai atsijungtėte!";
        activityRule.getScenario().onActivity(activity -> {
            new Thread(() -> {
                onView(withText(toastMessage))
                        .inRoot(RootMatchers.withDecorView(not(activity.getWindow().getDecorView())))
                        .check(matches(isDisplayed()));
            }).start();
        });
    }
}
