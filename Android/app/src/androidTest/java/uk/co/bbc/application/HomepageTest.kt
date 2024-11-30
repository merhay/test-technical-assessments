package uk.co.bbc.application

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import uk.co.bbc.application.support.HomepageHelper
import uk.co.bbc.application.support.HomepageHelper.getNodeText

@RunWith(AndroidJUnit4::class)
class HomepageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mainActivityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testHomePageLoadsSuccessfully() {
        mainActivityScenario.use {
            // Add your test code here
            HomepageHelper.waitForMainActivityToLoad(composeTestRule)
            HomepageHelper.clickDropDownMenu(composeTestRule)
            HomepageHelper.verifyDropDownMenuItems(composeTestRule)
        }
    }

    @Test
    fun testRefreshLastUpdatedSuccessfully() {
        mainActivityScenario.use {
            val oldLastUpdated = getNodeText(composeTestRule, TEST_TAG_LAST_UPDATED)
            HomepageHelper.clickRefreshButton(composeTestRule)
            HomepageHelper.waitUntilLoadingSpinnerCompletes(composeTestRule)
            val newLastUpdated = getNodeText(composeTestRule, TEST_TAG_LAST_UPDATED)
            HomepageHelper.verifyRefreshLastUpdated(oldLastUpdated, newLastUpdated)
        }
    }

    @Test
    fun testGoToLinkChangesAccordingToTopicPicker() {
        mainActivityScenario.use {
            val topic = "Technology"
            HomepageHelper.clickDropDownAndSelectTopic(composeTestRule, topic)
            HomepageHelper.verifyGoToLinkUpdatesToTopic(composeTestRule, topic)
        }
    }
}