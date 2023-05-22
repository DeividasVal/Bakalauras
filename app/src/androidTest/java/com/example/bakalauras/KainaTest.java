package com.example.bakalauras;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import com.example.bakalauras.ui.prisijungti.Prisijungti;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class KainaTest {

    @Rule
    public ActivityScenarioRule<Prisijungti> activityRule =
            new ActivityScenarioRule<>(Prisijungti.class);

    @Test
    public void testNurodytiKaina() {
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
        String textToType = "1";
        onView(ViewMatchers.withId(R.id.kainaPerValProfilis))
                .perform(ViewActions.typeText(textToType));
        onView(ViewMatchers.withId(R.id.kainaPerValProfilis))
                .check(matches(isEnabled()))
                .check(matches(hasFocus()));
    }
}
