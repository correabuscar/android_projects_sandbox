@file:Suppress("RedundantExplicitType")

package com.example.tiptime

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyDown
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                TipTimeScreen()
            }

        }
    }
}

//@Composable
//private fun Shiet(a:SemanticsPropertyReceiver):Unit {
//    var hint:String="nothing"
//    val formattedDouble:Double=0.0
//    var isError:Boolean=false
//    if (isError) {
//        hint = stringResource(id = )
//    } else {
//        hint = stringResource(
//            R.string.cost_of_service_interpreted_amount,
//            formattedDouble
//        )
//    }
//}


@Composable
fun TipTimeScreen() {
    val focusManager = LocalFocusManager.current
    //val amountInput = "0"
    //var amountInput: MutableState<String> = mutableStateOf("1")
    var amountInputAsString:String by rememberSaveable { mutableStateOf("") }
    var tipInputAsString:String by rememberSaveable { mutableStateOf("30") }
    //var preAmountInputAsStringOrNull:String?=amountInputAsString// by rememberSaveable { mutableStateOf("") }
    //var preTipInputAsString:String?=tipInputAsString
    var roundUpTip by rememberSaveable { mutableStateOf(false) }
    var roundUpTotal by rememberSaveable { mutableStateOf(true) }

    // when null the conversion from String to Double failed for some reason therefore we know to
    // display an error
    //var amountAsDoubleOrNull:Double? by rememberSaveable { mutableStateOf(amountInputAsString.toDoubleOrNull()) }
    val amountAsDoubleOrNull:Double? = amountInputAsString.toDoubleOrNull()
    //var isError:Boolean by rememberSaveable { mutableStateOf(false) } //tracking this causes double recomposition, ie. 2 consecutive ones  (aside from the on app start two ones)
    var isError:Boolean=false
    var isError2:Boolean=false
    val tipPercentAsDoubleOrNull:Double? = tipInputAsString.toDoubleOrNull()
    val tipPercent:Double=if (null != tipPercentAsDoubleOrNull) {
        //DecimalFormat
        //this will round up to the nearest 2 decimals, eg. 12.009 or 12.000001 or 12.001 is rounded to 12.01
        //FIXME: somehow 12.05 is 12.06, 30.01 is 30.02 and 3132322.10 is 3132322.11 !! I guess Double makes it so! Maybe keep BigDecimal instead of Double then?
        BigDecimal(tipPercentAsDoubleOrNull).setScale(2, RoundingMode.CEILING).toDouble()
        //(amountAsDoubleOrNull*100).roundToLong().toDouble()/100
        //kotlin.math.floor(truncate(0.1)
    }else 0.0//tipPercentAsDoubleOrNull?:0.0
    //var ignoreNext:Boolean by remember { mutableStateOf(false) }


    fun validate(currencyAmount:Double?) {
        val isValidNumber:Boolean=(currencyAmount != null) && (currencyAmount.isFinite()) &&(currencyAmount >= 0.0)
        isError= !isValidNumber
//        try {
//            val vdbl: Double = text.toDouble()
//            isError=false
//        }catch (e:NumberFormatException) {
//            isError=true
//        }
        //isError = text.count() <1 || !text.isDigitsOnly() ||
    }
    fun validate2(percentage:Double?) {
        val isValidNumber:Boolean=(percentage != null) && (percentage.isFinite()) &&(percentage >= 0.0) && (percentage <= 100.0)
        isError2= !isValidNumber
    }

    val billAmountAsDouble:Double = if (null != amountAsDoubleOrNull) {
        //DecimalFormat
        //this will round up to the nearest 2 decimals, eg. 12.009 or 12.000001 or 12.001 is rounded to 12.01
        BigDecimal(amountAsDoubleOrNull).setScale(2, RoundingMode.CEILING).toDouble()
        //(amountAsDoubleOrNull*100).roundToLong().toDouble()/100
        //kotlin.math.floor(truncate(0.1)
    }else 0.0
    val tipValueAsString:String//=calculateTip(tipValue)
    val formattedDouble = NumberFormat.getCurrencyInstance().format(billAmountAsDouble)
    //val formattedDouble =String.format("%.2f", tipValue) ; //nopeFIXME: does this need to be tracked?! what about tipValue ? is it a no because amountAsDoubleOrNull is already tracked?!
    //var hint:String by rememberSaveable { mutableStateOf("this text is never seen(although it's painted at first), if things work as intended") }
    val hint:String//XXX: hint changes only if amountInputAsString changes, so no need to track it and cause double recomposition because of two tracks!
    //var hint:String="Interpreted amount of dollars: $formattedDouble" //must be tracked! so 'remember'!


    //this call works here because a recomposition is triggered by amountInputAsString being changed, but hmm,
    // ithinkitshouldalwaysworkthoFIXME: this might not be a good idea, because recomposition can be stopped ? so all this
    //  calculation is discarded or not even reached? but isn't a stopped recomposition eventually rescheduled
    //  so it will happen to execute this anyway! right? it won't be using the prev/old composition & data
    // with the new input change. So maybe this is still ok. I mean, they're using similar
    // stuff here: https://github.com/google-developer-training/basic-android-kotlin-compose-training-tip-calculator/blob/6c20458e8393060a1ff0e3a97ca1b08e87ce5456/app/src/main/java/com/example/tiptime/MainActivity.kt#L65
    // so I'm assuming that everything in this function gets executed in order, except the @Composable function calls,
    // which may mean those stringResource() calls below which are @Composable, but hmm, if their result is assigned,
    // are they really not executed? or is the old value kept (doesn't seem so, as formattedDouble is always set, so far)
    validate(amountAsDoubleOrNull)
    validate2(tipPercentAsDoubleOrNull)

    @Suppress("LiftReturnOrAssignment")
    if (isError) {


        //XXX: because the stringResource() call must be inside a @Composable
        hint = stringResource(R.string.invalid_number_input,"")
    } else {


        hint = stringResource(
            R.string.cost_of_service_interpreted_amount,
            NumberFormat.getCurrencyInstance().currency?:"unknowns",
            formattedDouble)
    }

    val billAmount:String =stringResource(R.string.cost_of_service_aka_bill_amount)

    val tipPercentText:String =stringResource(R.string.how_was_the_service)
    val hint2:String
    val formattedDoubleForTipPercent =String.format("%.2f", tipPercent)

    @Suppress("LiftReturnOrAssignment")
    if (isError2) {
        //XXX: because the stringResource() call must be inside a @Composable
        hint2 = stringResource(R.string.invalid_number_input,stringResource(R.string.invalid_number_input_addon))
    } else {
        hint2 = stringResource(
            R.string.interpreted_tip_percent,
            formattedDoubleForTipPercent)
    }

    var total:Double
    if (isError || isError2) {
        //tipValueAsDouble=0.0
        tipValueAsString=stringResource(R.string.no_tip_without_cost)
        total=0.0
    }else{
        @Suppress("JoinDeclarationAndAssignment")
        val tipValueAsDouble:Double
        tipValueAsDouble=calculateTip(billAmountAsDouble,tipPercent, roundUpTip)
        tipValueAsString=NumberFormat.getCurrencyInstance().format(tipValueAsDouble)
        total=billAmountAsDouble + tipValueAsDouble
        if (roundUpTotal) {
            total=kotlin.math.ceil(total)
        }
    }

    //FIXME: find out how to restore the focused item after screen rotation happens, because none gets focused!

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,

    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .verticalScroll(rememberScrollState()) //because singleLine=false on the editable TextField
                //.fillMaxSize(),
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            Text(
                text = stringResource(R.string.calculate_tip),
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))



            EditNumberField(
                label={ Text(
                    text=if (isError) "*$billAmount" else billAmount,
                    //it's already red only when focused, but with this, it's red when not focused too:
                    color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
                ) },
                `val`=amountInputAsString,
                hint=hint,
                isError=isError,
                modifier= Modifier
                    .fillMaxWidth()
//                    .semantics //not needed anymore!
////                        (
////                        //doesntworkTODO: pass a function manually marked as @Composable ?
////
////                        properties = Shiet
////                    )
//             //@Composable //XXX: Type inference failed. Expected type mismatch: inferred type is @Composable() (SemanticsPropertyReceiver.() -> Unit) but SemanticsPropertyReceiver.() -> Unit was expected
//            {
//                        // Provide localized description of the error
//                        //if (isError) error("Email format is invalid.")
//                        //XXX: getSystem() here is the real cause of the crash! read its description! and can't find a getResources() one!
//                        //val res: Resources = Resources.getSystem()//getResources()
//                        //android.content.res.Resources.getResources() //see? it doesn't exist, doc! https://developer.android.com/reference/kotlin/android/content/res/Resources
////                        if (isError) {
////                            hint = //res.getString(R.string.invalid_number_input)
////                        } else {
//////                            hint = stringResource(
//////                                R.string.cost_of_service_interpreted_amount,
//////                                formattedDouble
//////                            )//XXX: doesn't let me because this lambda isn't a Composable
////                            //hint=getString(R.string.cost_of_service_interpreted_amount,formattedDouble)//XXX: "@Composable invocations can only happen from the context of a @Composable function"
////                            hint =  //res.getString(
//////                                R.string.cost_of_service_interpreted_amount,
//////                                formattedDouble
//////                            )//XXX: works,but crashes at runtime!
////                            //hint="Interpreted amount of dollars: $formattedDouble"
////                        }
//                    }
//                    .onKeyEvent {
//                        if (it.key == Key.Tab || it.key == Key.Enter) {
//                            focusManager.moveFocus(FocusDirection.Down)
//                            true
//                        } else {
//                            false
//                        }
//                    }


                    ,
                keyboardActions=KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }

                ) {
                    //this does the check only after tapping Enter on keyboard!
                    //validate(amountAsDoubleOrNull)
                                                },
                keyboardOptions = //KeyboardOptions(keyboardType = KeyboardType.Number),
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),

                        onValueChange=
                {
                    //XXX: this traps Tab/Enter before the above modifier.onKeyEvent{}  !!! FIXME: it's possible that isn't always true
                    //if (!ignoreNext) {

                    //hint=stringResource(R.string.cost_of_service_interpreted_amount,formattedDouble) //XXX: doesn't allow me to: this lambda isn't a composable! just for tests!
                    //amountInput.value = it
                    //preAmountInputAsStringOrNull= it// }
                    amountInputAsString=it
                    //amountAsDoubleOrNull=amountInputAsString.toDoubleOrNull()
                    //isError = false
                    //this does the check on every textfield change: (this is necessary!)
                    //validate(amountAsDoubleOrNull)
//                if (isError) {
//                    hint = stringResource( //"@Composable invocations can only happen from the context of a @Composable function"
//                                R.string.cost_of_service_interpreted_amount,
//                                formattedDouble
//                            )
//                }
                })


            EditNumberField(

                label={ Text(
                    text=if (isError2) "*$tipPercentText" else tipPercentText,
                    //it's already red only when focused, but with this, it's red when not focused too:
                    color = if (isError2) MaterialTheme.colors.error else MaterialTheme.colors.onSurface,
                ) },
                `val`=tipInputAsString,
                hint=hint2,
                isError=isError2,
                keyboardActions=KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ) {},
                keyboardOptions = //KeyboardOptions(keyboardType = KeyboardType.Number),
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange={
                    tipInputAsString=it
                },
                modifier=Modifier
//                    .onPreviewKeyEvent {
//                    @OptIn(ExperimentalComposeUiApi::class)
//                    if (it.key == Key.Tab || it.key == Key.Enter){
//                        if (it.type==KeyDown) {
//                            //focusRequester.requestFocus()
//                            focusManager.moveFocus(if (it.isShiftPressed || it.isCtrlPressed || it.isAltPressed) FocusDirection.Up else FocusDirection.Down)
//                        } //else eat the KeyUP event(s)
//                        true
//                    } else {
////                        if (preTipInputAsString==null) {
////                            //nvmFIXME: handle the case when they go in reverse order too, what's the big deal anyway! :)
////                            error("Broken assumption, onValueChange isn't executed prior to modifier.onKeyEvent{}")
////                        }
//                        //tipInputAsString=preTipInputAsString?:"Broken assumption, onValueChange isn't executed prior to modifier.onKeyEvent{}"
//                        false
//                    }
//                }
            )


            RoundUpRow(
                text=stringResource(R.string.round_up_tip),
                roundUp = roundUpTip, onRoundUpChanged = { roundUpTip = it })
            RoundUpRow(
                text=stringResource(R.string.round_up_total),
                roundUp = roundUpTotal, onRoundUpChanged = { roundUpTotal = it })

            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.tip_amount, tipValueAsString),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.total_pay,
                    if (isError || isError2)
                        stringResource(R.string.no_tip_without_cost)
                    else {
                        NumberFormat.getCurrencyInstance()
                            .format(total)
                    }
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text=stringResource(R.string.final_tip_after_roundings, if (isError || isError2)
                    stringResource(R.string.no_tip_without_cost)
                else {
                    NumberFormat.getCurrencyInstance()
                        .format(total-billAmountAsDouble)
                }),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color=MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
            )
        }
    }
}

@VisibleForTesting
internal fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean,
): Double {
    var tip:Double = (tipPercent / 100) * amount
    if (roundUp) {
        val alterRoundUp:Double=BigDecimal(tip).setScale(0, RoundingMode.CEILING).toDouble()
        tip = kotlin.math.ceil(tip)
        if (tip != alterRoundUp) {
            error("$tip!=$alterRoundUp")
        }
    }
    return tip
}


@Composable
fun EditNumberField(
    label:@Composable ()->Unit,
    `val`: String, //XXX: so keywords can be used after all
    hint: String, //hint is outside because I need formattedDouble var
    isError: Boolean,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier=Modifier,
) {

    //TODO: make Del key delete char to the right
    //TODO: make shift+arrows start/extend selection

    //var moo=`val`
    //var moo by rememberSaveable { mutableStateOf(`val`) }
    //val amountAsDoubleOrNull=amountInputAsString.toDoubleOrNull()
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                onValueChange("")
            },
        ) {
            Icon(
                imageVector=Icons.Default.Clear,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    val iconFunc=if (`val`.isNotBlank()) trailingIconView else null

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        leadingIcon = iconFunc,
        trailingIcon =iconFunc,
        label = label,
        isError = isError,

        //value = amountInput.value,
        value = `val`,
        //amountInputAsString,
        onValueChange = onValueChange,
//        {
//            //moo=it
//            onValueChange(it)
//        },
//        {
//            //amountInput.value = it
//            amountInputAsString= it
//            amountAsDoubleOrNull=amountInputAsString.toDoubleOrNull()
//            //isError = false
//            validate(amountAsDoubleOrNull)
//
//        },
            // Other parameters
            modifier = modifier
                .onPreviewKeyEvent {
                    @OptIn(ExperimentalComposeUiApi::class)  //FIXME: wut?!
                    when (it.key) {
                        Key.Tab,Key.Enter -> {
                            if (it.type == KeyDown) {
                                //FIXME: no way to use modifiers to tab back in emulator!
                                focusManager.moveFocus(
                                    if (it.isShiftPressed || it.isCtrlPressed || it.isAltPressed || it.isMetaPressed)
                                        FocusDirection.Up else FocusDirection.Down
                                )
                            }
                            true
                        }
//                        Key.Delete/*,Key.NumPad*/ -> {
//                            //TODO: delete char to the right, as without this it currently does nothing!
//                            true
//                        }
                        else -> false
                    }
//                    if (it.key == Key.Tab || it.key == Key.Enter) {
//                        //focusRequester.requestFocus()
//                        //XXX: shift+tab is taken over by Android Studio or emulator to select the buttons above the emulator when pressed!
//                        //XXX: holding ctrl yields a 2 point thing so I can't use that either ffs! alt+tab (L or R, Alt)doesn't work even tho I've not set it for anything like switching windows in X!
//                        if (it.type == KeyDown) {
//                            //FIXME: no way to use modifiers to tab back in emulator!
//                            focusManager.moveFocus(
//                                if (it.isShiftPressed || it.isCtrlPressed || it.isAltPressed || it.isMetaPressed)
//                                    FocusDirection.Up else FocusDirection.Down
//                            )
//                        } //else eat the KeyUP event(s)
//                        //ignoreNext=true
//                        //XXX: note that preAmountInputAsStringOrNull already ate the tab&enter and we aren't setting it (here) to amountInputAsString, thus dropping it as intended!
//                        true //XXX: won't reach .onKeyEvent after this!
//                    } else {
////                            if (preAmountInputAsStringOrNull==null) {
////                                //this means that onValueChange below didn't happen first, as expected from my tests that it should!
////                                //so the value we would assign here to amountInputAsString is one char/action behind the actual one!
////                                error("Broken assumption, onValueChange isn't executed prior to modifier.onKeyEvent{}")
////                            }
//                        //amountInputAsString=preAmountInputAsStringOrNull?:"!!!Broken assumption, onValueChange isn't executed prior to modifier.onKeyEvent{}"
//                        //XXX: fixed by replacing .onKeyEvent with .onPreviewKeyEvent
//                        //ignoreNext=false
//                        false
//                    }
                },

            singleLine = true,//singleLine = true, //doneFIXME: but pressing Enter on physical keyboard(inside emulator) still goes to second line even tho this line is below the visible view!

            keyboardActions = keyboardActions,

            keyboardOptions = keyboardOptions,

            )
//    val hint: String = if (isError) {
//        stringResource(R.string.invalid_number_input)
//    } else {
//        stringResource(
//            R.string.cost_of_service_interpreted_amount,
//            formattedDouble
//        )
//    }

            Text(
                text = hint,
                color = (if (isError)
                    MaterialTheme.colors.error
                else MaterialTheme.colors.onSurface
                        ).copy(alpha = ContentAlpha.medium),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
}
@Composable
fun RoundUpRow(
    text:String,
    roundUp: Boolean,
                   onRoundUpChanged: (Boolean) -> Unit,
                   modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
            //.wrapContentWidth(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.SpaceBetween//Evenly
    ) {
        Text(text = text)
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start)
            ,colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray
            ),
        )

    }

}


@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    showSystemUi = true,
    name = "Light",
)
@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true,
    name="Dark",
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    showSystemUi = true,
    name = "Light French",
    locale = "fr-rFR",
)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}