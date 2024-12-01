package uk.co.bbc.application.support

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import uk.co.bbc.application.TEST_TAG_ALERT_CONFIRM_BUTTON
import uk.co.bbc.application.TEST_TAG_BREAKING_NEWS_BUTTON
import uk.co.bbc.application.TEST_TAG_CONTENT_HEADING
import uk.co.bbc.application.TEST_TAG_CONTENT_TEXT
import uk.co.bbc.application.TEST_TAG_DROPDOWN_MENU
import uk.co.bbc.application.TEST_TAG_DROPDOWN_MENU_ITEM
import uk.co.bbc.application.TEST_TAG_GO_TO_BUTTON
import uk.co.bbc.application.TEST_TAG_LAST_UPDATED
import uk.co.bbc.application.TEST_TAG_LOADING_SPINNER
import uk.co.bbc.application.TEST_TAG_REFRESH_BUTTON
import uk.co.bbc.application.TEST_TAG_TOP_BAR_TITLE
import uk.co.bbc.application.utils.ComposeAssertions
import uk.co.bbc.application.utils.ComposeActions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object HomepageHelper {

    private const val mainActivityHeaderTestTag = "mainActivityHeader"
    private const val dropDown = "dropDown"

    fun waitForMainActivityToLoad(composeTestRule: ComposeTestRule) {
        composeTestRule.waitForIdle()
        ComposeAssertions.isChildWithTextDisplayed(composeTestRule, mainActivityHeaderTestTag, "My BBC")
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_REFRESH_BUTTON)
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_LAST_UPDATED)
        ComposeAssertions.isDisplayedWithText(composeTestRule,"This is a BBC app with all your favourite content")
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_GO_TO_BUTTON)
        ComposeAssertions.isDisplayedWithText(composeTestRule, "Politics")
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_BREAKING_NEWS_BUTTON)
    }

    fun verifyUserIsOnHomepage(composeTestRule: ComposeTestRule) {
        composeTestRule.waitForIdle()
        ComposeAssertions.isDisplayedWithText(composeTestRule, "My BBC")
        ComposeAssertions.isDisplayedWithText(composeTestRule,"This is a BBC app with all your favourite content")
    }

    fun clickDropdownMenu(composeTestRule: ComposeTestRule) {
        ComposeActions.performClick(composeTestRule, dropDown)
        composeTestRule.waitForIdle()
        ComposeAssertions.isDisplayed(composeTestRule, TEST_TAG_DROPDOWN_MENU)
    }

    fun clickDropDownAndSelectTopic(composeTestRule: ComposeTestRule, topic: String) {
        clickDropdownMenu(composeTestRule)

        composeTestRule.onAllNodesWithText(topic).onFirst().performClick()
        composeTestRule.waitForIdle()
        ComposeAssertions.isDisplayedWithText(composeTestRule, topic)
    }

    fun verifyGoToLinkUpdatesToTopic(composeTestRule: ComposeTestRule, topic: String){
        val goToLinktext = getNodeText(composeTestRule, TEST_TAG_GO_TO_BUTTON)
        assertEquals(goToLinktext, "Go to " +topic)
    }

    fun verifyDropDownMenuItems(composeTestRule: ComposeTestRule){
        val expectedTopics = listOf("Politics", "UK", "Sport", "Technology", "World", "TV Guide")

        val nodes = composeTestRule.onAllNodesWithTag(TEST_TAG_DROPDOWN_MENU_ITEM).fetchSemanticsNodes()
        val actualTopics = nodes.map { node ->
            node.config.getOrNull(SemanticsProperties.Text)?.firstOrNull().toString()
        }
        assertEquals(expectedTopics, actualTopics)
    }

    fun clickRefreshButton(composeTestRule: ComposeTestRule) {
        runBlocking { delay(1000) }
        ComposeActions.performClick(composeTestRule, TEST_TAG_REFRESH_BUTTON)
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_LOADING_SPINNER)
        composeTestRule.waitForIdle()
    }

    fun verifyRefreshLastUpdated(oldLastUpdated: String, newLastUpdated: String) {
        ComposeAssertions.isDateUpdated(oldLastUpdated, newLastUpdated)
    }

    fun waitUntilLoadingSpinnerCompletes(composeTestRule: ComposeTestRule){
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag(TEST_TAG_LOADING_SPINNER).fetchSemanticsNodes().isEmpty()
        }
        ComposeAssertions.isNotDisplayed(composeTestRule, TEST_TAG_LOADING_SPINNER)
    }

    fun getNodeText(composeTestRule: ComposeTestRule, testTag: String): String {
        return composeTestRule.onNodeWithTag(testTag).fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.Text)
            ?.firstOrNull()
            .toString().trim()
    }

    fun extractDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd MMM yyyy 'at' HH:mm:ss", Locale.ENGLISH)
        return dateFormat.parse(dateString.substringAfter(": ").trim())
            ?: throw IllegalArgumentException("Invalid date format")
    }

    fun verifyUserLandsOnContentPage(composeTestRule: ComposeTestRule, topic: String) {
        ComposeAssertions.isChildWithTextDisplayed(composeTestRule, TEST_TAG_TOP_BAR_TITLE, topic)
        ComposeAssertions.isDisplayed(composeTestRule, TEST_TAG_CONTENT_HEADING)
        ComposeAssertions.isDisplayed(composeTestRule, TEST_TAG_CONTENT_TEXT)
    }

    fun verifyScrollToTheEnd(composeTestRule: ComposeTestRule, tag: String) {
        ComposeActions.scrollTo(composeTestRule, tag)
        ComposeAssertions.isDisplayed(composeTestRule, tag)
    }

    fun verifyTvLicenseAlertDialogue(composeTestRule: ComposeTestRule) {
        ComposeAssertions.isDisplayedWithText(composeTestRule, "Do you have a TV License ?")
        ComposeAssertions.isDisplayedWithText(composeTestRule, "Yes")
        ComposeAssertions.isDisplayedWithText(composeTestRule, "No")
    }

    fun verifySomethingWrongAlertDialogue(composeTestRule: ComposeTestRule) {
        ComposeAssertions.isDisplayedWithText(composeTestRule, "Something has gone wrong")
        ComposeAssertions.isDisplayed(composeTestRule, TEST_TAG_ALERT_CONFIRM_BUTTON)
    }

}