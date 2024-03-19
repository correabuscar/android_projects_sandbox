package com.example.myapplicationlvg2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplicationlvg2.ui.theme.MyApplicationLVG2Theme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationLVG2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}


val itemsList = (0..50).toList()
val itemsIndexedList = listOf("A", "B", "C")

val itemModifier = Modifier
    .border(1.dp, Color.Blue)
    .height(40.dp)
    .padding(start = 4.dp, end = 4.dp)
    .wrapContentSize()

//src: https://stackoverflow.com/questions/70500071/measuring-string-width-to-properly-size-text-composable/70508246#70508246
@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints()).width.toDp()

        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}

fun Int.pow(toWhatPower:Int):Int {
    return this.toFloat().pow(toWhatPower).toInt()
}

fun doh(n:Int, max_n:Int=(2.pow(n)-1)) {

}
@Composable
fun Greeting(name: String, modifier: Modifier=Modifier) {
    Column {
        //item {
            Text(text = "Hello $name!")
        //}
        //item {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(48.dp)//Fixed(3)
            , modifier =
                    modifier.height(100.dp)
            ) {
                items(span = { GridItemSpan(2) }
                    ,items=itemsList,) {
                    Text("Item is $it", itemModifier,
                    maxLines = 1, textAlign = TextAlign.Center,)
                }
                item {
                    Text("Single item", itemModifier)
                }
                itemsIndexed(itemsIndexedList) { index, item ->
                    Text("Item at index $index is $item", itemModifier)
                }
            }
        //}

        //item {
        val maxX=2500
        val paddingForNumbers=2.dp //note that this is for Left or Right only, so you've to 2x it!
        val sections = (1 until maxX+1).toList().chunked(150)
        Text(text="sections=${sections.size}")
        //val buttonz= rememberSaveable { //java.lang.IllegalArgumentException: androidx.compose.runtime.snapshots.SnapshotStateList@b32fd8d cannot be saved using the current SaveableStateRegistry. The default implementation only supports types which can be stored inside the Bundle. Please consider implementing a custom Saver for this class and pass it to rememberSaveable().
        //this remembers selections on device screen rotation change. (ie. portrait to landscape which causes recomposition)
        val buttonz= rememberSaveable(
            //src: https://stackoverflow.com/questions/68885154/using-remembersaveable-with-mutablestatelistof/68887484#68887484
            saver = listSaver(
                save = { stateList ->
                    if (stateList.isNotEmpty()) {
                        val first = stateList.first()
                        if (!canBeSaved(first)) {
                            throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                        }
                    }
                    stateList.toList()
                },
                restore = { it.toMutableStateList() }
            )
        ) {
            val lst=mutableStateListOf<Boolean>()
            for (i in 1..sections.size) {
                lst.add(false)
            }
            lst
        }
        //this, with 'remember', forgets selections on device rotation change.
//        val buttonz=remember { //nvmFIXME: this needs to be rememberSaveable but I have to provide my own custom Saver, see: https://stackoverflow.com/questions/68885154/using-remembersaveable-with-mutablestatelistof
//            val lst=mutableStateListOf<Boolean>()
//            //Array<Boolean>(sections.size){false}
//        //)
////            SnapshotStateList<Boolean>(
////                // MutableList(sections.size) { false }
////                sections.size)
////            {false}
//            for (i in 1..sections.size) {
//                lst.add(false)
//            }
//            lst
//        }

        assert(buttonz.size == sections.size)
        //assert(false) //works?

//        var buttonz by rememberSaveable {
//            mutableStateListOf<Boolean>()
//                //SnapshotStateList<Boolean>(
//               // MutableList(sections.size) { false }
//               // sections.size)
//            //{false}
//
//            //)
//        }
        MeasureUnconstrainedViewWidth(
            viewToMeasure = {
                Text("$maxX")
            }
        ) { measuredWidth ->
            // use measuredWidth to create your view

            LazyVerticalGrid(
                columns = GridCells.Adaptive(measuredWidth+paddingForNumbers*2),//40.dp),
                //horizontalArrangement = Arrangement.spacedBy(1.dp),
                //verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                sections.forEachIndexed { index, items ->

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Button(
                            onClick = {
                                      buttonz[index]=!buttonz[index]
                                //buttonz[index]=!buttonz[index]
                            },

                        ) {
                            Text(
                                "$index. Do you see your number below?",
                                modifier
                                    .border(1.dp, Color.Gray)
                                    .height(80.dp)
                                    .wrapContentSize()
                            )
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier=modifier.height(2.dp))
                    }
                    items(
                        items,
                        // not required as it is the default
                        //span = { GridItemSpan(1)} //currentLineSpan = if (it < 100) 1 else 2) }
                        span=null,
                    ) {
                        Text(softWrap = false,

                            text="$it",
                            modifier= modifier
                                .border(1.dp, Color.Blue)
                                //.height(80.dp)
                                .padding(start = paddingForNumbers, end = paddingForNumbers)
                            //.wrapContentSize(unbounded = false)
                            //.fillMaxSize(1f)//.fillMaxWidth()
                            ,
                            textAlign = TextAlign.Center,
                            maxLines = 1,

                            )
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier=modifier.height(2.dp))
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Center,
                        ) {
                            Button(

                                onClick = {
                                    buttonz[index]=!buttonz[index]
                                },
                                modifier = modifier.wrapContentSize()
                                    //.weight(4f)
                                    //.weight(1f)

                                ,
                                //shape =

                            ) {
                                Text(
                                    "$index. Was your number above?",
                                    modifier= modifier
                                        .border(1.dp, Color.Red)
                                        .height(80.dp)
                                        .wrapContentSize()
                                )
                            }
                            Spacer(modifier=modifier.width(20.dp))

//                            MeasureUnconstrainedViewWidth(
//                                viewToMeasure = {
//                                    Text(text="Yes",
//                                        modifier=modifier
//                                        .border(1.dp, Color.Green)
//                                        .padding(2.dp)
//                                        //.align(CenterHorizontally)
//                                    )
//                                }
//                            ) { measuredWidth ->
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(1),
                                    //modifier=modifier.wrapContentHeight()
                                    modifier= modifier
                                        .height(80.dp) //TODO: shouldn't this be 2 x the size of Yes(or No) Text? ie. should I compute that?
                                        .padding(end = 20.dp)

                                    ,
                                    verticalArrangement = Center,
                                    horizontalArrangement = Center,


                                ) {
                                    item {
                                        Surface(
                                            color= if (buttonz[index]) MaterialTheme.colors.secondary else MaterialTheme.colors.background,
                                        ) {


                                            Text(
                                                text = "Yes",
                                                modifier = modifier
                                                    .border(1.dp, MaterialTheme.colors.secondary)
                                                    //.width(measuredWidth)
                                                    .padding(2.dp),
                                                textAlign = TextAlign.Center,

                                                )
                                        }
                                    }
                                    item {
                                        Spacer(modifier.height(20.dp))
                                    }
                                    item {
                                        Surface(
                                            color= if (!buttonz[index]) MaterialTheme.colors.error else MaterialTheme.colors.background,
                                        ) {
                                            Text(
                                                text = "No",

                                                modifier = modifier

                                                    .border(1.dp, MaterialTheme.colors.error)
                                                    //.width(measuredWidth)
                                                    .padding(2.dp)

                                                //.width(measuredWidth)
                                                //.width(measuredWidth) //fail
                                                ,
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }
                                }
//                                Column(
//                                    modifier=modifier
//                                        //.weight(1f)
//                                        .padding(end=4.dp)
//                                    // ,
//                                    //verticalArrangement = Center
//                                ) {
//                                    // use measuredWidth to create your view
//                                    Text(
//                                        text = "Yes",
//                                        modifier = modifier
//                                            .border(1.dp, Color.Green)
//                                            .width(measuredWidth)
//                                            .padding(2.dp)
//                                            .align(CenterHorizontally)
//
//                                    )
//                                    Text(
//                                        text = "No",
//
//                                        modifier = modifier
//
//                                            .border(1.dp, Color.Red)
//                                            .width(measuredWidth)
//                                            .padding(2.dp)
//
//                                            //FIXME: I dare you to center the text whilst also setting the width size! seems not possible? damn, it's textAlign= isn't it?!
//                                            //.width(measuredWidth)
//                                            .align(CenterHorizontally)
//                                            //.width(measuredWidth) //fail
//
//                                    )
//                                }
//                            }
                        }
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier=modifier.height(10.dp))
                    }
                }
            }
        }
        //}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationLVG2Theme {
        Greeting("Android")
    }
}