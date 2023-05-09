package com.example.bakalauras;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class KorepetitoriausProfilisPasirinktiMokymosiTipaTest {

    @Rule
    public ActivityScenarioRule<Prisijungti> activityRule =
            new ActivityScenarioRule<>(Prisijungti.class);

    @Test
    public void testTipas() {
        Espresso.onView(withId(R.id.prisijungtiVardas))
                .perform(ViewActions.typeText("gediminas"));
        Espresso.onView(withId(R.id.slaptazodisPrisijungti))
                .perform(ViewActions.typeText("gediminas"))
                .perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.korRadio))
                .perform(click());
        Espresso.onView(withId(R.id.prisijungtiButton))
                .perform(click());
        Espresso.onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.nav_sukurti_profili))
                .perform(ViewActions.click());
        ViewInteraction checkBox = onView(withId(R.id.checkBoxGyvai));
        checkBox.perform(click());
        checkBox.check(matches(isChecked()));
        checkBox.perform(click());
        ViewInteraction checkBox2 = onView(withId(R.id.checkBoxOnline));
        checkBox2.perform(click());
        checkBox2.check(matches(isChecked()));
        checkBox2.perform(click());
        checkBox.perform(click());
        checkBox2.perform(click());
        checkBox.check(matches(isChecked()));
        checkBox2.check(matches(isChecked()));
    }
}
