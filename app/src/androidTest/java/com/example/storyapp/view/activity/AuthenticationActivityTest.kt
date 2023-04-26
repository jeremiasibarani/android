package com.example.storyapp.view.activity

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapp.R
import com.example.storyapp.hasNoErrorText
import com.example.storyapp.util.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationActivityTest{

    private val wrongEmailFormat = "sibarani@123gmail.com"
    private val wrongPasswordFormat = "wpass"
    private val correctEmailFormat = "shingen@gmail.com"
    private val correctPasswordFormat = "newpassword"
    private val emailErrorMessage = "Email format is not valid"
    private val passwordErrorMessage = "Password must at least 8 characters"

    @get:Rule
    val activity = ActivityScenarioRule(AuthenticationActivity::class.java)

    @Before
    fun setup(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


    @Test
    fun showErrorWhenEmailEditTextValueFormatIsIncorrect(){
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
        onView(withId(R.id.et_email)).perform(typeText(wrongEmailFormat), closeSoftKeyboard())
            .check(matches(hasErrorText(emailErrorMessage)))
    }

    @Test
    fun showEmptyErrorWhenEmailEditTextValueFormatIsCorrect(){
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
        onView(withId(R.id.et_email)).perform(typeText(correctEmailFormat), closeSoftKeyboard())
            .check(matches(hasNoErrorText()))
    }

    @Test
    fun showErrorWhenPasswordEditTextValueIsIncorrect(){
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).perform(typeText(wrongPasswordFormat), closeSoftKeyboard())
            .check(matches(hasErrorText(passwordErrorMessage)))
    }

    @Test
    fun showEmptyErrorWhenPasswordEditTextValueIsCorrect(){
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).perform(typeText(correctPasswordFormat), closeSoftKeyboard())
            .check(matches(hasNoErrorText()))
    }

    @Test
    fun correctPasswordAndEmailShouldIntentToStoryActivity(){
        Intents.init()
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
            .perform(typeText(correctEmailFormat), closeSoftKeyboard())
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))
            .perform(typeText(correctPasswordFormat), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
            .perform(click())
        intended(hasComponent(StoryActivity::class.java.name))
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        assertTrue(activity.scenario.state == Lifecycle.State.DESTROYED)
    }

}