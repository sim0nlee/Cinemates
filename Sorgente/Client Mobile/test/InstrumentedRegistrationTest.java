package com.example.cinemates;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cinemates.activities.LogInActivity;
import com.example.cinemates.activities.MainActivity;
import com.example.cinemates.activities.RegistrationActivity;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class InstrumentedRegistrationTest {

    private static final int id_nameInput = R.id.nome_registrazione_input;
    private static final int id_surnameInput = R.id.cognome_registrazione_input;
    private static final int id_emailInput = R.id.email_registrazione_input;
    private static final int id_passwordInput = R.id.password_registrazione_input;

    private static final int id_registerButton = R.id.goto_registration_button;
    private static final int id_mainActivity = R.id.mainactivity_constraintlayout;

    private static final String VALID_NAME = "test";
    private static final String VALID_SURNAME = "user";
    private static final String VALID_EMAIL = "test@test.it";
    private static final String VALID_PASSWORD = "password";

    private static final String[] INVALID_NAMES = new String[]{"", "prova1", "p"};
    private static final String[] INVALID_SURNAMES = new String[]{"", "prova1", "p"};
    private static final String[] INVALID_EMAILS = new String[]{"abc..def@mail.com",".abc@mail.com","abc#def@mail.com",
                                                                "abc.def@mail.c","abc.def@mail#archive.com","abc.def@mail",
                                                                "abc.def@mail..com"};
    private static final String[] INVALID_PASSWORDS = new String[]{"", " password", "passw", "password "};

    private static final String EXISTING_EMAIL = "prova@prova.it";


    @Rule
    public ActivityScenarioRule<LogInActivity> loginActivityRule = new ActivityScenarioRule<>(LogInActivity.class);
    @Rule
    public ActivityScenarioRule<RegistrationActivity> registrationActivityRule = new ActivityScenarioRule<>(RegistrationActivity.class);
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void gotoRegistrationActivity() {
        onView(withId(R.id.goto_registration_button))
                .perform(click());
    }


    @Test
    //2A
    public void A_testRegistrationWithInvalidName() {

        onView(withId(id_surnameInput))
                .perform(typeText(VALID_SURNAME), closeSoftKeyboard());
        onView(withId(id_emailInput))
                .perform(typeText(VALID_EMAIL), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(VALID_PASSWORD), closeSoftKeyboard());

        for (String s : INVALID_NAMES) {
            onView(withId(id_nameInput))
                .perform(clearText(), typeText(s), closeSoftKeyboard());
            onView(withId(id_registerButton))
                .perform(click());
            onView(withId(id_emailInput)).check(matches(isDisplayed()));
        }

    }

    @Test
    //2B
    public void B_testRegistrationWithInvalidSurname() {

        onView(withId(id_nameInput))
                .perform(typeText(VALID_NAME), closeSoftKeyboard());
        onView(withId(id_emailInput))
                .perform(typeText(VALID_EMAIL), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(VALID_PASSWORD), closeSoftKeyboard());

        for (String s : INVALID_SURNAMES) {
            onView(withId(id_surnameInput))
                    .perform(clearText(), typeText(s), closeSoftKeyboard());
            onView(withId(id_registerButton))
                    .perform(click());
            onView(withId(id_emailInput)).check(matches(isDisplayed()));
        }

    }

    @Test
    //2C
    public void C_testRegistrationWithInvalidEmailFormat() {

        onView(withId(id_nameInput))
                .perform(typeText(VALID_NAME), closeSoftKeyboard());
        onView(withId(id_surnameInput))
                .perform(typeText(VALID_SURNAME), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(VALID_PASSWORD), closeSoftKeyboard());

        for (String s : INVALID_EMAILS) {
            onView(withId(id_emailInput))
                    .perform(clearText(), typeText(s), closeSoftKeyboard());
            onView(withId(id_registerButton))
                    .perform(click());
            waitForTask();
            onView(withId(id_emailInput)).check(matches(isDisplayed()));
        }

    }

    @Test
    //2D
    public void D_testRegistrationWithInvalidPassword() {

        onView(withId(id_nameInput))
                .perform(typeText(VALID_NAME), closeSoftKeyboard());
        onView(withId(id_surnameInput))
                .perform(typeText(VALID_SURNAME), closeSoftKeyboard());
        onView(withId(id_emailInput))
                .perform(typeText(VALID_EMAIL), closeSoftKeyboard());

        for (String s : INVALID_PASSWORDS) {
            onView(withId(id_passwordInput))
                    .perform(clearText(), typeText(s), closeSoftKeyboard());
            onView(withId(id_registerButton))
                    .perform(click());
            onView(withId(id_emailInput)).check(matches(isDisplayed()));
        }

    }

    @Test
    //2E
    public void E_testRegistrationWithExistingEmail() {

        onView(withId(id_nameInput))
                .perform(typeText(VALID_NAME), closeSoftKeyboard());
        onView(withId(id_surnameInput))
                .perform(typeText(VALID_SURNAME), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(VALID_PASSWORD), closeSoftKeyboard());


        onView(withId(id_emailInput))
                .perform(clearText(), typeText(EXISTING_EMAIL), closeSoftKeyboard());
        onView(withId(id_registerButton))
                .perform(click());
        waitForTask();
        onView(withId(id_emailInput)).check(matches(isDisplayed()));

    }

    @Test
    //2F
    public void F_testRegistrationWithValidData() {

        onView(withId(id_nameInput))
                .perform(typeText(VALID_NAME), closeSoftKeyboard());
        onView(withId(id_surnameInput))
                .perform(typeText(VALID_SURNAME), closeSoftKeyboard());
        onView(withId(id_emailInput))
                .perform(typeText(VALID_EMAIL), closeSoftKeyboard());
        onView(withId(id_passwordInput))
                .perform(typeText(VALID_PASSWORD), closeSoftKeyboard());

        onView(withId(id_registerButton))
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
