package com.example.myapplicationswipetodismiss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationswipetodismiss.ui.theme.MyApplicationSwipeToDismissTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationSwipeToDismissTheme {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Greeting(name: String) {
    //Text(text = "Hello $name!")
    // This is an example of a list of dismissible items, similar to what you would see in an
// email app. Swiping left reveals a 'delete' icon and swiping right reveals a 'done' icon.
// The background will start as grey, but once the dismiss threshold is reached, the colour
// will animate to red if you're swiping left or green if you're swiping right. When you let
// go, the item will animate out of the way if you're swiping left (like deleting an email) or
// back to its default position if you're swiping right (like marking an email as read/unread).
    val items=listOf("a","b",1,2,3,4)
    LazyColumn {
        items(items) { item ->
//            SwipeableYesNo()
//            {
//
//            }
            var yesno by rememberSaveable { mutableStateOf<DismissValue>(DismissValue.Default) }
            val stickySelection:Boolean=true //if sticky then once you chose Y/N remember it, don't reset to Default/unchosen when swiping in the same direction the second time!
            val dismissState = rememberDismissState(
                //initialValue = DismissValue.Default,
                confirmStateChange = {
//                    when(it) {
//                        DismissValue.DismissedToEnd -> yesno = true
//                        DismissValue.DismissedToStart -> yesno = false
//                    }
                    if ((DismissValue.Default != it) || (!stickySelection))
                        yesno=it //once you chose yes or no, remember it, don't go to default if you swipe twice in the same direction!(which happens without the 'if')
                    false//never dismiss!
                }
            )

            val currentAllowableSwipes:Set<DismissDirection> =
                when(yesno) {
                    DismissValue.Default -> setOf(DismissDirection.StartToEnd,DismissDirection.EndToStart)
                    DismissValue.DismissedToStart -> setOf(DismissDirection.StartToEnd)
                    DismissValue.DismissedToEnd  -> setOf(DismissDirection.EndToStart)
                }
//                if (DismissValue.DismissedToStart == yesno || DismissValue.Default == yesno) DismissDirection.StartToEnd,
//                if (DismissValue.DismissedToEnd == yesno || DismissValue.Default == yesno) DismissDirection.EndToStart


            SwipeToDismiss(
                dismissThresholds={FractionalThreshold(0.2f)},//{FixedThreshold(150.dp)},
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = currentAllowableSwipes,

                background = {
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> Color.Red
                        }
                    )
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val yntext:String
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> yntext="Yes"//if (yesno) Icons.Default.Email else Icons.Default.Done
                        DismissDirection.EndToStart -> yntext="No" //Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )

//                    when (dismissState.targetValue) {
//                        DismissValue.Default -> { //Color.LightGray

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 4.dp),
                                contentAlignment = alignment
                            ) {
//                                Icon(
//                                    icon,
//                                    contentDescription = "Localized description",
//                                    modifier = Modifier.scale(scale)
//                                )
                                Text(text=yntext,
                                    fontSize=48.sp,
                                    //textAlign= TextAlign.Start,
                                    modifier = Modifier
                                        .scale(scale))
                            }
//                        }
//                        DismissValue.DismissedToEnd -> {}//Color.Green
//                        DismissValue.DismissedToStart -> {}//Color.Red
//                    }
                },
                dismissContent = {
                    val color2 = //by animateColorAsState(
                        //if (dismissState.targetValue )
                        if (dismissState.targetValue == DismissValue.Default) {
                            when(yesno) {
//                            if (DismissValue.DismissedToEnd == yesno) Color.Green
//                            else Color.Red
                                DismissValue.Default -> MaterialTheme.colors.secondary
                                DismissValue.DismissedToEnd -> Color.Green
                                DismissValue.DismissedToStart -> Color.Red
                            }
                        } else MaterialTheme.colors.secondary
                    //)
                    Card(
                        elevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 4.dp else 0.dp
                        ).value,
                        //modifier = Modifier.fillMaxSize().background(Color.DarkGray)
                        backgroundColor = color2,
                    ) {
                        ListItem(
                            text = {
                                Text(item.toString(), fontWeight = if (DismissValue.DismissedToEnd==yesno) FontWeight.Bold else null)
                            },
                            secondaryText = { Text("Swipe me left or right!${yesno}") }
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationSwipeToDismissTheme {
        Greeting("Android")
    }
}