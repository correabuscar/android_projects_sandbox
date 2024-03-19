package com.example.compose.rally


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()

        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = { },
                    currentScreen = RallyScreen.Accounts,

                    )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
        //Thread.sleep(5000)

        //composeTestRule.onRoot().printToLog("currentLabelExists")

//        composeTestRule
//            .onNodeWithText(RallyScreen.Accounts.name.uppercase())
//            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertExists()

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase()) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()

    }

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            RallyTheme {
                OverviewBody()
            }
        }

        composeTestRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()


        //Thread.sleep(5000) //no animation, prolly because of this? or it just doesn't happen in instrumentation tests?
        //This following block makes some animation happen, tho it's not real time, but less than real time, or so I think.
        //XXX: must be false at start of test: composeTestRule.mainClock.autoAdvance = false
        //the UI test is supposed to fail tho.
        composeTestRule.waitUntil(5000) {
            composeTestRule.mainClock.advanceTimeByFrame()
            false
        }
    }

}
