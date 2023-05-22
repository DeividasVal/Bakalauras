package com.example.bakalauras;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.is;

import android.widget.ListView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.bakalauras.ui.prisijungti.Prisijungti;

import org.hamcrest.Matchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class dalykaiTest {

    @Rule
    public ActivityScenarioRule<Prisijungti> activityRule =
            new ActivityScenarioRule<>(Prisijungti.class);

    @Test
    public void testDalykai() {
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
        Espresso.onView(ViewMatchers.withId(R.id.spinnerDalykai))
                .perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(is(Matchers.instanceOf(String.class)),
                is("Matematika"))).perform(ViewActions.click());
        Espresso.onView(withId(R.id.buttonPridėti))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.spinnerDalykai))
                .perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(is(Matchers.instanceOf(String.class)),
                is("Geografija"))).perform(ViewActions.click());
        Espresso.onView(withId(R.id.buttonPridėti))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.spinnerDalykai))
                .perform(ViewActions.click());
        Espresso.onData(Matchers.allOf(is(Matchers.instanceOf(String.class)),
                is("Informatika"))).perform(ViewActions.click());
        Espresso.onView(withId(R.id.buttonPridėti))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.pasirinktiDalykai))
                .check((view, noViewFoundException) -> {
                    int itemCount = ((ListView) view).getCount();
                    assertThat(itemCount, is(3));
                });
        }
}
