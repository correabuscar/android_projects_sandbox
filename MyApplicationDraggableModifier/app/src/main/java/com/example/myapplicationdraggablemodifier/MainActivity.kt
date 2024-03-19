package com.example.myapplicationdraggablemodifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationdraggablemodifier.ui.theme.MyApplicationDraggableModifierTheme
//import android.os.Bundle
//import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationDraggableModifierTheme {
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

@Composable
fun Greeting(name: String) {
    //Text(text = "Hello $name!")
    // Creating a Simple Scaffold
    // Layout for the application
    Scaffold(

        // Creating a Top Bar
        topBar = { TopAppBar(title = { Text("GFG | Draggable Modifier", color = Color.White) }, backgroundColor = Color(0xff0f9d58)) },

        // Creating Content
        content = {
            it
            // Creating a Column Layout
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

                //FIXME: recompute x,y offset based on screen orientation, so that it's in the same relative place, even if switched from Portrait to Landscape!

                // Horizontally Draggable Modifier
                var offsetX by rememberSaveable { mutableStateOf(0f) }
                var offsetY by rememberSaveable { mutableStateOf(0f) }
                Text(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                offsetX += delta
                            }
                        )
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                offsetY += delta
                            }
                        ),
                    text = "I move Horizontally or Vertically!", fontSize = 20.sp
                )

                // Adding a Space of 100dp height
                Spacer(modifier = Modifier.height(100.dp))

//                // Vertically Draggable Modifier
//                var offsetY by remember { mutableStateOf(0f) }
//                Text(
//                    modifier = Modifier
//                        .offset { IntOffset(0, offsetY.roundToInt()) }
//                        .draggable(
//                            orientation = Orientation.Vertical,
//                            state = rememberDraggableState { delta ->
//                                offsetY += delta
//                            }
//                        ),
//                    text = "I move Vertically!", fontSize = 20.sp
//                )
//                var offX by remember { mutableStateOf(0f) }
//                var offY by remember { mutableStateOf(0f) }
//                Spacer(modifier = Modifier.height(10.dp))
//
//                Text(
//                    modifier = Modifier
//                        .offset { IntOffset(offX.roundToInt(), offY.roundToInt()) }
//                        .draggable(
//                            //orientation = Orientation.Vertical,
//                            state = rememberDraggableState { delta ->
//                                offsetY += delta
//                            }
//                        ),
//                    text = "I move anywhere!", fontSize = 20.sp
//                )
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationDraggableModifierTheme {
        Greeting("Android")
    }
}