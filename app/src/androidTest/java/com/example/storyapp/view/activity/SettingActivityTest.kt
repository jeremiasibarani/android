package com.example.storyapp.view.activity

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.storyapp.R

@RunWith(AndroidJUnit4::class)
class SettingActivityTest{
    @get:Rule
    val activity = ActivityScenarioRule(SettingActivity::class.java)

    @Test
    fun logoutButtonClickedShouldIntentBackToLoginActivity(){
        Intents.init()
        onView(withId(R.id.cv_avatar)).check(matches(isDisplayed()))
        onView(withId(R.id.img_avatar)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_name)).check(matches(isDisplayed()))
        onView(withId(R.id.cv_language_setting)).check(matches(isDisplayed()))
        onView(withId(R.id.ll_language_setting)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_language_setting)).check(matches(isDisplayed()))
        onView(withId(R.id.ll_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.cv_logout)).check(matches(isDisplayed()))
            .perform(click())
        intended(hasComponent(AuthenticationActivity::class.java.name))
        assertTrue(activity.scenario.state == Lifecycle.State.DESTROYED)
    }
}