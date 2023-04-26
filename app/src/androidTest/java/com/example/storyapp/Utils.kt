package com.example.storyapp

import android.view.View
import android.widget.EditText
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Matcher


fun hasNoErrorText(): Matcher<View?> {
    return object : BoundedMatcher<View?, EditText>(EditText::class.java) {

        override fun matchesSafely(view: EditText): Boolean {
            return view.error == null
        }

        override fun describeTo(description: org.hamcrest.Description) {
            description.appendText("has no error text: ")
        }
    }
}
