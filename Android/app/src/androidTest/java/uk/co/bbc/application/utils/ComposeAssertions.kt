package uk.co.bbc.application.utils


import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    fun isNotDisplayed(composeTestRule: ComposeTestRule, testTag: String) {
        composeTestRule.onNodeWithTag(testTag).assertDoesNotExist()
    }

    fun isDateUpdated(oldDateString: String, newDateString: String) {
        val oldDate = extractDate(oldDateString)
        val newDate = extractDate(newDateString)
        assert(oldDate < newDate) { "New date ($newDate) should be later than the old date ($oldDate)." }
    }

    fun extractDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd MMM yyyy 'at' HH:mm:ss", Locale.ENGLISH)
        return dateFormat.parse(dateString.substringAfter(": ").trim())
            ?: throw IllegalArgumentException("Invalid date format")
    }
}