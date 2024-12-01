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
import uk.co.bbc.application.utils.ComposeActions

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
    fun loadsHomepageSuccessfully() {
        mainActivityScenario.use {
            HomepageHelper.waitForMainActivityToLoad(composeTestRule)
            HomepageHelper.clickDropdownMenu(composeTestRule)
            HomepageHelper.verifyDropDownMenuItems(composeTestRule)
        }
    }

    @Test
    fun refreshesLastUpdated() {
        mainActivityScenario.use {
            val oldLastUpdated = getNodeText(composeTestRule, TEST_TAG_LAST_UPDATED)
            HomepageHelper.clickRefreshButton(composeTestRule)
            HomepageHelper.waitUntilLoadingSpinnerCompletes(composeTestRule)
            val newLastUpdated = getNodeText(composeTestRule, TEST_TAG_LAST_UPDATED)
            HomepageHelper.verifyRefreshLastUpdated(oldLastUpdated, newLastUpdated)
        }
    }

    @Test
    fun updatesGoToLinkOnTopicPicker() {
        mainActivityScenario.use {
            val topic = "Technology"
            HomepageHelper.clickDropDownAndSelectTopic(composeTestRule, topic)
            HomepageHelper.verifyGoToLinkUpdatesToTopic(composeTestRule, topic)
        }
    }

    @Test
    fun navigatesToContentPageOnGoToLinkClick() {
        mainActivityScenario.use {
            val topic = "Technology"
            HomepageHelper.clickDropDownAndSelectTopic(composeTestRule, topic)
            ComposeActions.performClick(composeTestRule, TEST_TAG_GO_TO_BUTTON)
            HomepageHelper.verifyUserLandsOnContentPage(composeTestRule, topic)
            HomepageHelper.verifyScrollToTheEnd(composeTestRule, TEST_TAG_CONTENT_END)
            ComposeActions.performClick(composeTestRule, TEST_TAG_BACK_BUTTON)
            HomepageHelper.verifyUserIsOnHomepage(composeTestRule)
        }
    }

    @Test
    fun showsAlertForNoTvLicence() {
        mainActivityScenario.use {
            val topic = "TV Guide"
            HomepageHelper.clickDropDownAndSelectTopic(composeTestRule, topic)
            ComposeActions.performClick(composeTestRule, TEST_TAG_GO_TO_BUTTON)
            HomepageHelper.verifyTvLicenseAlertDialogue(composeTestRule)
            ComposeActions.performClickWithText(composeTestRule, "No")
            HomepageHelper.verifyUserIsOnHomepage(composeTestRule)
        }
    }

    @Test
    fun navigatesToContentPageForUserWithTvLicense() {
        mainActivityScenario.use {
            val topic = "TV Guide"
            HomepageHelper.clickDropDownAndSelectTopic(composeTestRule, topic)
            ComposeActions.performClick(composeTestRule, TEST_TAG_GO_TO_BUTTON)
            HomepageHelper.verifyTvLicenseAlertDialogue(composeTestRule)
            ComposeActions.performClickWithText(composeTestRule, "Yes")
            HomepageHelper.verifyUserLandsOnContentPage(composeTestRule, topic)
            ComposeActions.performClick(composeTestRule, TEST_TAG_BACK_BUTTON)
            HomepageHelper.verifyUserIsOnHomepage(composeTestRule)
        }
    }

    @Test
    fun breakingNewsShowsErrorAlert() {
        mainActivityScenario.use {
            ComposeActions.performClick(composeTestRule, TEST_TAG_BREAKING_NEWS_BUTTON)
            HomepageHelper.verifySomethingWrongAlertDialogue(composeTestRule)
            ComposeActions.performClick(composeTestRule, TEST_TAG_ALERT_CONFIRM_BUTTON)
            HomepageHelper.verifyUserIsOnHomepage(composeTestRule)
        }
    }

}