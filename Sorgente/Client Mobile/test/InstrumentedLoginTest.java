package com.example.cinemates;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cinemates.activities.LogInActivity;
import com.example.cinemates.activities.MainActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class InstrumentedLoginTest {

    private static final int id_emailInput = R.id.email_login_input;
    private static final int id_passwordInput = R.id.password_login_input;
    private static final int id_loginButton = R.id.login_button;
    private static final int id_mainActivity = R.id.mainactivity_constraintlayout;

    private static final String RANDOM_PASSWORD = "test";
    private static final String INVALID_EMAIL = "test";

    private static final String CORRECT_EMAIL = "prova@prova.it";
    private static final String CORRECT_PASSWORD = "123123";


    @Rule
    public ActivityScenarioRule<LogInActivity> loginActivityRule = new ActivityScenarioRule<>(LogInActivity.class);
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    //1A
    public void A_testLogInWithNoEmail() {

        onView(withId(id_passwordInput))
                .perform(typeText(RANDOM_PASSWORD), closeSoftKeyboard());

        onView(withId(id_loginButton))
                .perform(click());

        onView(withId(id_emailInput)).check(matches(isDisplayed()));

    }

    @Test
    //1B
    public void B_testLogInWithInvalidEmailFormat() {

        onView(withId(id_emailInput))
                .perform(typeText(INVALID_EMAIL), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(RANDOM_PASSWORD), closeSoftKeyboard());

        onView(withId(id_loginButton))
            .perform(click());

        onView(withId(id_emailInput)).check(matches(isDisplayed()));

    }

    @Test
    //1C
    public void C_testLogInWithWrongPassword() {

        onView(withId(id_emailInput))
                .perform(typeText(CORRECT_EMAIL), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(RANDOM_PASSWORD), closeSoftKeyboard());

        onView(withId(id_loginButton))
                .perform(click());

        waitForTask();

        onView(withId(id_emailInput)).check(matches(isDisplayed()));

    }

    @Test
    //1D
    public void D_testLogInWithCorrectCredentials() {

        onView(withId(id_emailInput)).check(matches(isDisplayed()))
                .perform(typeText(CORRECT_EMAIL), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(CORRECT_PASSWORD), closeSoftKeyboard());

        onView(withId(id_loginButton))
                .perform(click());

        waitForTask();

        onView(withId(id_mainActivity)).check(matches(isDisplayed()));

    }


    private void waitForTask () {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}