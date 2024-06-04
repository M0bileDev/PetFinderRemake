package com.example.petfinderremake.features.filter.presentation.screen.select

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.common.presentation.utils.commonString
import com.example.petfinderremake.features.filter.R
import com.example.petfinderremake.features.filter.di.TestSelectModuleBreeds
import com.example.petfinderremake.features.filter.di.TestSelectModuleTypes
import com.example.petfinderremake.features.filter.ext.launchFragmentInHiltContainer
import com.example.petfinderremake.features.filter.presentation.adapter.SelectAdapter
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import com.example.petfinderremake.features.filter.presentation.model.navigation.toStringResource
import com.example.petfinderremake.features.filter.presentation.navigation.SelectNavigation
import com.example.petfinderremake.features.filter.utils.TestUtils.NOT_EXISTING_ITEM
import com.example.petfinderremake.features.filter.utils.TestUtils.testNames
import com.example.petfinderremake.features.filter.utils.atPositionOnView
import com.example.petfinderremake.features.filter.utils.withItemCount
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(TestSelectModuleTypes::class)
class SelectFragmentMultiSelectTest {

    private val appContext: Context = ApplicationProvider.getApplicationContext()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var commonNavigation: CommonNavigation

    @Inject
    lateinit var selectNavigation: SelectNavigation

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun searchItem_typeFullNameOfItem_findOneItem() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last()))
        onView(withId(R.id.select_recycler_view)).check(matches(withItemCount(1)))
    }

    @Test
    fun searchItem_typeFullNameOfItem_findExactItem() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last()))
        onView(withId(R.id.select_recycler_view)).check(matches(hasDescendant(withText(testNames.last()))))
    }

    @Test
    fun searchItem_typeFullNameOfItem_switchFullNameOfItem_findOneItem() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last()))
        onView(withId(R.id.select_editText)).perform(replaceText(testNames[0]))
        onView(withId(R.id.select_recycler_view)).check(matches(withItemCount(1)))
    }

    @Test
    fun searchItem_typeFullNameOfItem_switchFullNameOfItem_findExactItem() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last()))
        onView(withId(R.id.select_editText)).perform(replaceText(testNames[0]))
        onView(withId(R.id.select_recycler_view)).check(matches(hasDescendant(withText(testNames[0]))))
    }

    @Test
    fun searchItem_typeADistinctiveNameForTheItem_findOneItem() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last().takeLast(1)))
        onView(withId(R.id.select_recycler_view)).check(matches(withItemCount(1)))
    }

    @Test
    fun searchItem_typeADistinctiveNameForTheItem_findExactItem() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last().takeLast(1)))
        onView(withId(R.id.select_recycler_view)).check(matches(hasDescendant(withText(testNames.last()))))
    }

    @Test
    fun searchItem_typeFullNameOfItem_clearText_allItemsDisplay() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames.last()))
        onView(withId(R.id.select_editText)).perform(clearText())
        onView(withId(R.id.select_recycler_view)).check(matches(withItemCount(testNames.size)))
    }

    @Test
    fun searchItem_typeNotExistingNameOfItem_noItemsDisplay() {
        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(NOT_EXISTING_ITEM))
        onView(withId(R.id.select_recycler_view)).check(matches(withItemCount(0)))
    }

    @Test
    fun testTopAppBar_hasTitleBasedOnSelectType_selectedTypeIsBreed() {

        val type = appContext.getString(SelectType.BREED.toStringResource())
        val expectedTitle = appContext.getString(commonString.select_x, type)

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_topAppBar)).check(matches(hasDescendant(withText(expectedTitle))))
    }

    @Test
    fun clickFirstItem_firstItemCheckboxIsChecked() {

        val firstItemPosition = 0

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

    }

    @Test
    fun clickFirstItemTwice_firstItemCheckboxIsUnchecked() {

        val firstItemPosition = 0

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isNotChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

    }

    /*

    Test selection of recycler view items. For BREED select type, selection is working in multi select
    mode.

    a) Click first item
    b) First item checkbox is checked
    c) Click second item
    d) First item checkbox is still checked
    e) Second item checkbox is checked

     */
    @Test
    fun testSelectionState_SelectionIsWorkingInMultiSelectionMode() {

        val firstItemPosition = 0
        val secondItemPosition = 1

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    secondItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        secondItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )
    }

    @Test
    fun searchFirstItem_clickFirstItem_firstItemIsSelected() {

        val searchedItemPosition = 0

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames[searchedItemPosition]))

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    searchedItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    searchedItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        searchedItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )
    }

    @Test
    fun searchItems_clickSearchedItems_searchedItemsAreSelected() {

        val firstItemPosition = 0
        val lastItemPosition = testNames.size - 1

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames[firstItemPosition]))

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_editText)).perform(replaceText(testNames[lastItemPosition]))

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_editText)).perform(clearText())

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    lastItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        lastItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(TestSelectModuleBreeds::class)
class SelectFragmentSingleSelectTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var commonNavigation: CommonNavigation

    @Inject
    lateinit var selectNavigation: SelectNavigation

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickFirstItem_firstItemCheckboxIsChecked() {

        val firstItemPosition = 0

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

    }

    @Test
    fun clickFirstItemTwice_firstItemCheckboxIsUnchecked() {

        val firstItemPosition = 0

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isNotChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

    }

    /*

    Test selection of recycler view items. For TYPE select type, selection is working in single select
    mode.

    a) Click first item
    b) First item checkbox is checked
    c) Click second item
    d) First item checkbox is unchecked
    e) Second item checkbox is checked

     */
    @Test
    fun testSelectionState_SelectionIsWorkingInSingleSelectionMode() {

        val firstItemPosition = 0
        val secondItemPosition = 1

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    secondItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isNotChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        secondItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )
    }

    @Test
    fun searchItems_clickSearchedItems_lastClickedItemIsSelected() {

        val firstItemPosition = 0
        val lastItemPosition = testNames.size - 1

        launchFragmentInHiltContainer<SelectFragment>(
            themeResId = R.style.Theme_PetFinderRemake
        )

        onView(withId(R.id.select_editText)).perform(typeText(testNames[firstItemPosition]))

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_editText)).perform(replaceText(testNames[lastItemPosition]))

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition,
                    click()
                )
            )

        onView(withId(R.id.select_editText)).perform(clearText())

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    firstItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        firstItemPosition,
                        isNotChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )

        onView(withId(R.id.select_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<SelectAdapter.SelectItemViewHolder>(
                    lastItemPosition
                )
            ).check(
                matches(
                    atPositionOnView(
                        lastItemPosition,
                        isChecked(),
                        R.id.select_item_checkbox
                    )
                )
            )
    }

}