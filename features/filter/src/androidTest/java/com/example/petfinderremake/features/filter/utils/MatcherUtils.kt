package com.example.petfinderremake.features.filter.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withItemCount(expectedCount: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("RecyclerView should have $expectedCount items")
        }

        override fun matchesSafely(view: View): Boolean {
            return (view as RecyclerView).adapter?.itemCount == expectedCount
        }
    }
}

fun atPositionOnView(position: Int, itemMatcher: Matcher<View>, targetViewId: Int): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: // has no item on such position
                return false
            val targetView = viewHolder.itemView.findViewById<View>(targetViewId)
            return itemMatcher.matches(targetView)
        }
    }
}
