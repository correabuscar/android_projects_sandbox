package com.example.myapplicationlvg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationlvg.ui.theme.MyApplicationLVGTheme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.grid.*
//import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val itemsList = (0..5).toList()
val itemsIndexedList = listOf("A", "B", "C")

val itemModifier = Modifier
    .border(1.dp, Color.Blue)
    .height(80.dp)
    .wrapContentSize()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationLVGTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    LazyColumn() {
        Text(text = "Hello $name!")

        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(itemsList) {
                Text("Item is $it", itemModifier)
            }
            item {
                Text("Single item", itemModifier)
            }
            itemsIndexed(itemsIndexedList) { index, item ->
                Text("Item at index $index is $item", itemModifier)
            }
        }
        val sections = (0 until 25).toList().chunked(5)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            sections.forEachIndexed { index, items ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        "This is section $index",
                        Modifier
                            .border(1.dp, Color.Gray)
                            .height(80.dp)
                            .wrapContentSize()
                    )
                }
                items(
                    items,
                    // not required as it is the default
                    span = { GridItemSpan(1) }
                ) {
                    Text(
                        "Item $it",
                        Modifier
                            .border(1.dp, Color.Blue)
                            .height(80.dp)
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationLVGTheme {
        Greeting("Android")
    }
}