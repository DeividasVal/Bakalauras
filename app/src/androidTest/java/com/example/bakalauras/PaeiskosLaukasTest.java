package com.example.bakalauras;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.bakalauras.ui.prisijungti.Prisijungti;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PaeiskosLaukasTest {

    @Rule
    public ActivityScenarioRule<Prisijungti> activityRule =
            new ActivityScenarioRule<>(Prisijungti.class);

    @Test
    public void testPaiekosLaukas() {
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
                .perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.nav_ziureti))
                .perform(ViewActions.click());
        String textToType = "test";
        onView(ViewMatchers.withId(R.id.ieskoti))
                .perform(ViewActions.typeText(textToType));
        onView(ViewMatchers.withId(R.id.ieskoti))
                .check(matches(isEnabled()))
                .check(matches(hasFocus()));
    }
}
