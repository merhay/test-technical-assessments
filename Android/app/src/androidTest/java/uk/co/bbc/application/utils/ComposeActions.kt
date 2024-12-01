package uk.co.bbc.application.utils

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput

object ComposeActions {

    fun performClick(composeTestRule: ComposeTestRule, testTag: String) {
        composeTestRule.onNodeWithTag(testTag).performClick()
    }

    fun performClickWithText(composeTestRule: ComposeTestRule, testTag: String) {
        composeTestRule.onNodeWithText(testTag).performClick()
    }

    fun enterText(composeTestRule: ComposeTestRule, testTag: String, text: String) {
        composeTestRule.onNodeWithTag(testTag).performTextInput(text)
    }

    fun scrollTo(composeTestRule: ComposeTestRule, testTag: String) {
        composeTestRule.onNodeWithTag(testTag).performScrollTo()
    }

}