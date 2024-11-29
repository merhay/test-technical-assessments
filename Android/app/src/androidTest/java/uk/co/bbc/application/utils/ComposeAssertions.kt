package uk.co.bbc.application.utils


import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText

object ComposeAssertions {

    fun isDisplayed(composeTestRule: ComposeTestRule, testTag: String) {
        composeTestRule.onNodeWithTag(testTag).assertIsDisplayed()
    }

    fun isDisplayedWithText(composeTestRule: ComposeTestRule, text: String) {
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun isChildWithTextDisplayed(composeTestRule: ComposeTestRule, parentTestTag: String, childText: String) {
        composeTestRule.onNodeWithTag(parentTestTag)
            .onChildren()
            .filter(hasText(childText))
            .assertAny(hasText(childText))
    }
}