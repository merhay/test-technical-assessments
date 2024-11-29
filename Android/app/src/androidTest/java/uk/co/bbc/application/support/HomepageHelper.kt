package uk.co.bbc.application.support

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import uk.co.bbc.application.TEST_TAG_BREAKING_NEWS_BUTTON
import uk.co.bbc.application.TEST_TAG_DROPDOWN_MENU
import uk.co.bbc.application.TEST_TAG_DROPDOWN_MENU_ITEM
import uk.co.bbc.application.TEST_TAG_GO_TO_BUTTON
import uk.co.bbc.application.TEST_TAG_LAST_UPDATED
import uk.co.bbc.application.TEST_TAG_LOADING_SPINNER
import uk.co.bbc.application.TEST_TAG_REFRESH_BUTTON
import uk.co.bbc.application.utils.ComposeAssertions
import uk.co.bbc.application.utils.ComposeActions

object HomepageHelper {

    private const val mainActivityHeaderTestTag = "mainActivityHeader"
    private const val dropDown = "dropDownMenu"

    fun waitForMainActivityToLoad(composeTestRule: ComposeTestRule) {
        composeTestRule.waitForIdle()
        ComposeAssertions.isChildWithTextDisplayed(composeTestRule, mainActivityHeaderTestTag, "My BBC")

        ComposeAssertions.isDisplayedWithText(composeTestRule, "My BBC")
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_REFRESH_BUTTON)
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_LAST_UPDATED)
        ComposeAssertions.isDisplayedWithText(composeTestRule,"This is a BBC app with all your favourite content")
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_GO_TO_BUTTON)
        ComposeAssertions.isDisplayedWithText(composeTestRule, "Politics")
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_BREAKING_NEWS_BUTTON)
    }

    fun clickDropDownMenu(composeTestRule: ComposeTestRule) {
        ComposeActions.performClick(composeTestRule, dropDown)
        composeTestRule.waitForIdle()
        ComposeAssertions.isDisplayed(composeTestRule, TEST_TAG_DROPDOWN_MENU)
    }

    fun verifyDropDownMenuItems(composeTestRule: ComposeTestRule){
        val expectedTopics = listOf("Politics", "UK", "Sport", "Technology", "World", "TV Guide")

        val nodes = composeTestRule.onAllNodesWithTag(TEST_TAG_DROPDOWN_MENU_ITEM).fetchSemanticsNodes()
        val actualTopics = nodes.map { node ->
            node.config.getOrNull(SemanticsProperties.Text)?.firstOrNull().toString()
        }
        assert(actualTopics == expectedTopics)
    }

    fun clickRefreshButton(composeTestRule: ComposeTestRule) {
        val oldLastUpdated = getLastUpdatedText(composeTestRule)

        runBlocking { delay(1000) }

        ComposeActions.performClick(composeTestRule, TEST_TAG_REFRESH_BUTTON)
        ComposeAssertions.isDisplayed(composeTestRule,TEST_TAG_LOADING_SPINNER)
        composeTestRule.waitForIdle()
        composeTestRule.waitUntil(3000) {
                composeTestRule.onAllNodesWithTag(TEST_TAG_LOADING_SPINNER).fetchSemanticsNodes().isEmpty()
        }
        ComposeAssertions.isNotDisplayed(composeTestRule, TEST_TAG_LOADING_SPINNER)

        val newLastUpdated = getLastUpdatedText(composeTestRule)

        ComposeAssertions.isDateUpdated(oldLastUpdated, newLastUpdated)
    }


    fun getLastUpdatedText(composeTestRule: ComposeTestRule): String {
        return composeTestRule.onNodeWithTag(TEST_TAG_LAST_UPDATED).fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.Text)
            ?.firstOrNull()
            .toString()
    }



}