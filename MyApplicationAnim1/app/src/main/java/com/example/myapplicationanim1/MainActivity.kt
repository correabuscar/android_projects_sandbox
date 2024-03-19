package com.example.myapplicationanim1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationanim1.ui.theme.MyApplicationAnim1Theme
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationAnim1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    AnimatedVisibilityInLazyColumn()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationAnim1Theme {
        //Greeting("Android")
        AnimatedVisibilityInLazyColumn()
    }
}

val turquoiseColors = listOf(
    Color(0xff07688C),
    Color(0xff1986AF),
    Color(0xff50B6CD),
    Color(0xffBCF8FF),
    Color(0xff8AEAE9),
    Color(0xff46CECA)
)

// MyModel class handles the data change of the items that are displayed in LazyColumn.
class MyModel {
    private val _items: MutableList<ColoredItem> = mutableStateListOf()
    private var lastItemId = 0
    val items: List<ColoredItem> = _items

    // Each item has a MutableTransitionState field to track as well as to mutate the item's
    // visibility. When the MutableTransitionState's targetState changes, corresponding
    // transition will be fired. MutableTransitionState allows animation lifecycle to be
    // observed through it's [currentState] and [isIdle]. See below for details.
    inner class ColoredItem(
        val visible: MutableTransitionState<Boolean>,
        val itemId: Int
    ) {
        val color: Color
            get() = turquoiseColors.let {
                it[itemId % it.size]
            }
    }

    fun addNewItem() {
        lastItemId++
        _items.add(
            ColoredItem(
                // Here the initial state of the MutableTransitionState is set to false, and
                // target state is set to true. This will result in an enter transition for
                // the newly added item.
                MutableTransitionState(false).apply { targetState = true },
                lastItemId
            )
        )
    }

    fun removeItem(item: ColoredItem) {
        // By setting the targetState to false, this will effectively trigger an exit
        // animation in AnimatedVisibility.
        item.visible.targetState = false
    }

    fun pruneItems() {
        // Inspect the animation status through MutableTransitionState. If isIdle == true,
        // all animations have finished for the transition.
        _items.removeAll(
            items.filter {
                // This checks that the animations have finished && the animations are exit
                // transitions. In other words, the item has finished animating out.
                it.visible.isIdle && !it.visible.targetState
            }
        )
    }

    fun removeAll() {
        _items.forEach {
            it.visible.targetState = false
        }
    }
}

@Composable
fun AnimatedVisibilityInLazyColumn() {
    Column {
        val model = remember/*Saveable(
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
        )*/ { MyModel() }
        Row(Modifier.fillMaxWidth()) {
            Button(
                { model.addNewItem() },
                modifier = Modifier.padding(15.dp).weight(1f)
            ) {
                Text("Add")
            }
        }

        // This sets up a flow to check whether any item has finished animating out. If yes,
        // notify the model to prune the list.
        LaunchedEffect(model) {
            snapshotFlow {
                model.items.firstOrNull { it.visible.isIdle && !it.visible.targetState }
            }.collect {
                if (it != null) {
                    model.pruneItems()
                }
            }
        }
        LazyColumn {
            items(model.items, key = { it.itemId }) { item ->
                AnimatedVisibility(
                    item.visible,
//                    enter = expandVertically(),
//                    exit = shrinkVertically()
                            enter = slideIn(tween(200, easing = LinearOutSlowInEasing)) { fullSize ->
                        // Specifies the starting offset of the slide-in to be 1/4 of the width to the right,
                        // 100 (pixels) below the content position, which results in a simultaneous slide up
                        // and slide left.
                        IntOffset(fullSize.width / 4, 100)
                    },
                    exit = slideOut(tween(200, easing = FastOutSlowInEasing)) {
                        // The offset can be entirely independent of the size of the content. This specifies
                        // a target offset 180 pixels to the left of the content, and 50 pixels below. This will
                        // produce a slide-left combined with a slide-down.
                        IntOffset(-180, 50)
                    },

                    ) {
                    Box(
                        Modifier.fillMaxWidth().requiredHeight(90.dp)
                            .background(item.color)
                    ) {
                        Button(
                            { model.removeItem(item) },
                            modifier = Modifier.align(Alignment.CenterEnd)
                                .padding(15.dp)
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }

        Button(
            { model.removeAll() },
            modifier = Modifier.align(Alignment.End).padding(15.dp)
        ) {
            Text("Clear All")
        }
    }
}