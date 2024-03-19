@file:Suppress("RedundantExplicitType")

package com.example.tiptime

import android.content.Context
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent as viewKeyEvent
import android.view.KeyEvent.KEYCODE_FORWARD_DEL
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent as composeKeyEvent
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class TipUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

//    @Composable
//    inline fun wellshie():String {
//        return stringResource(id = R.string.cost_of_service_aka_bill_amount)
//    }


    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            TipTimeTheme {
                TipTimeScreen()
            }
        }

        val targetContext: Context = ApplicationProvider.getApplicationContext()
        val gr=targetContext.resources
        val cos: String =  gr.getString(R.string.cost_of_service_aka_bill_amount)

        val billAmount:Double=10.0
        val tipPercent:Double=20.0
        val expectedTipAmount= NumberFormat.getCurrencyInstance().format(billAmount*tipPercent/100) // eg. "$2.00" for 10.0 and 20%

        //initially it's red and has a prefixed star("*") on the text, to signify error.
        @Suppress("ConvertToStringTemplate")
        val badBillAmountTextInput=composeTestRule.onNodeWithText("*"+cos).assertExists()
        badBillAmountTextInput.printToLog("wtw") //interesting
        badBillAmountTextInput.performTextInput(billAmount.toString())
        badBillAmountTextInput.assertDoesNotExist()
        //now the star is gone
        val goodBillAmountTextInput=composeTestRule.onNodeWithText(cos).assertExists()
        //assert(badBillAmountTextInput === goodBillAmountTextInput)//can't do this!
        //badBillAmountTextInput.performTextInput("f") //won't work!doesn't exist anymore! it says!
        goodBillAmountTextInput.performTextInput("f")
        composeTestRule.onNodeWithText(cos).assertExists()
        goodBillAmountTextInput.assertExists()
        goodBillAmountTextInput.assertTextContains(cos)
        //badBillAmountTextInput.performTextInput("g")
        badBillAmountTextInput.assertDoesNotExist()
        goodBillAmountTextInput.assertExists()
        goodBillAmountTextInput.performTextInput("gg") //so "10fgg" so far, the double "gg" is to allow 2x Backspace(s) below(in the 2 ways) cuz 10f is valid String.toDouble() as 10.00
        goodBillAmountTextInput.assertDoesNotExist()
        badBillAmountTextInput.assertExists()
        badBillAmountTextInput.assertTextContains("*$cos")

        composeTestRule.waitForIdle() //is this even needed?

        //@OptIn(ExperimentalComposeUiApi::class)
        //doneFIXME: find out how to create KeyEvent for backspace keypress! to use with .performKeyPress() //TODO: find the Compose way of doing it!
//        val backSpacePressed:KeyEvent=KeyEvent(nativeKeyEvent = NativeKeyEvent.changeAction(android.view.KeyEvent(
//            ACTION_DOWN, KEYCODE_BACK), ACTION_DOWN))
//        //android.view.KeyEvent.changeAction(backSpacePressed,ACTION_DOWN)
//        badBillAmountTextInput.performKeyPress(backSpacePressed) //KeyDown,Key.Backspace))
        badBillAmountTextInput.performKeyPress(composeKeyEvent(NativeKeyEvent(ACTION_DOWN,NativeKeyEvent.KEYCODE_DEL))) //ie. press Backspace via Android Compose in kotlin

        //XXX from: https://issuetracker.google.com/issues/199919707#comment17  so this is the Android View way:
        InstrumentationRegistry.getInstrumentation().sendKeyDownUpSync(viewKeyEvent.KEYCODE_DEL) //ie. press Backspace via Android View in kotlin

        //so "10f" which is correct now
        //badBillAmountTextInput.performImeAction()
        badBillAmountTextInput.assertDoesNotExist()
        goodBillAmountTextInput.assertExists()
        goodBillAmountTextInput.printToLog("backspacekey?") //interesting

        val tipPercentText=gr.getString(R.string.how_was_the_service)

        //because it already has a default value set (eg. "30" (%)) //FIXME: this test fails if we decide to not set a default value for Tip %, or to set one of billAmount above!
        val goodTipText=composeTestRule.onNodeWithText(tipPercentText)
        goodTipText.assertExists()
        goodTipText.performTextClearance()
        @Suppress("ConvertToStringTemplate")
        val badTipText=composeTestRule.onNodeWithText("*"+tipPercentText)
            .assertExists()
        badTipText.performTextInput(tipPercent.toString())
        //composeTestRule.onNodeWithText(tipPercentText)
        goodTipText.assertExists()

        val tipAmountText=gr.getString(R.string.tip_amount,expectedTipAmount)
        composeTestRule.onNodeWithText(tipAmountText).assertExists()



    }

}