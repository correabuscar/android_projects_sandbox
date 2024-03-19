package com.example.learntogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learntogether.ui.theme.LearnTogetherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnTogetherTheme {
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
    val image= painterResource(id = R.drawable.bg_compose_background)
    Column(
        //modifier=Modifier.blur(7.dp)
    ) {

        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                //.fillMaxHeight()
                //.fillMaxWidth()
            //    .padding(1.dp),
            //modifier = Modifier.padding(1.sp)
                .blur(2.dp)
        )
        Text(
            text= stringResource(R.string.compose_tutorial_text),
            fontSize = 24.sp,//TextUnit.Unspecified,
            modifier = Modifier
                .padding(start=16.dp, end=16.dp, top=16.dp, bottom=16.dp),

        )
        Text(
            text= stringResource(R.string.compose_intro_text),
            //fontSize = TextUnit.Unspecified, // aka default when not used!
            textAlign = TextAlign.Justify,
            modifier = Modifier
                //.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)


            )
        Text(
            text= stringResource(R.string.bruh_text),
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(16.dp),
        )
    }
}

@Preview(showBackground = true,
    showSystemUi = true,
)
@Composable
fun DefaultPreview() {
    LearnTogetherTheme {
        Greeting("Android")
    }
}