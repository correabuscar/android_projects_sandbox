package com.example.happybirthday

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.happybirthday.ui.theme.HappyBirthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    //BirthdayGreetingWithText( "blah blah!", "- from whoever")
                    BirthdayGreetingWithImage("","")
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}

@Composable
fun BirthdayGreetingWithText(message: String
                             , from: String) {
    Column(
        //horizontalAlignment=Alignment.Start,
        //verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = message, fontSize = 36.sp
            ,modifier=Modifier.fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)//Start)
                .padding( top = 16.dp)
        )
        Text(
            text = from,
            fontSize = 24.sp
            ,color= Color.Magenta
            ,textAlign = TextAlign.Right
                    ,modifier = Modifier
                    .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)//End)
                .padding(start = 16.dp, end = 16.dp)



        )
        Text(
            text = from,
        )
    }
}

@Composable
fun BirthdayGreetingWithImage(message: String, from: String) {
    val image = painterResource(R.drawable.androidparty)
    //Step 3 create a box to overlap image and texts
    Box {

        Image(
            painter = image, contentDescription = null
            ,modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
            ,contentScale = ContentScale.FillHeight//Fit//Crop


        )
        BirthdayGreetingWithText( message, from)
    }

}


@Preview(showBackground = true,
    showSystemUi = true,
    name="my preview"
)
@Composable
fun DefaultPreview() {
    HappyBirthdayTheme {
        //Greeting("Android preview")
        BirthdayGreetingWithText( stringResource(R.string.happy_birthday_text),
            stringResource(R.string.signature_text)
            //"wtw2saasssdsadasdasdasdasdss"
        )

    }
}

@Preview(showBackground = true
        ,showSystemUi = true,
    name="your preview")
@Composable
fun BirthdayCardPreview() {
   HappyBirthdayTheme {

       //Greeting("Android")
       Column {

           Text("Some text")
           Text("Some more text")
           Text("Last text")
           BirthdayGreetingWithImage("wtw","wtw2saasssdsadasdasdasdasdss")
       }

   }
}
