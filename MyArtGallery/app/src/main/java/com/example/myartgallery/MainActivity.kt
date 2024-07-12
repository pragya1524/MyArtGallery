package com.example.myartgallery

import android.os.Bundle
import android.util.EventLogTags.Description
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myartgallery.ui.theme.MyArtGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyArtGalleryTheme {
                //call function here
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyArtGalleryApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyArtGalleryApp( modifier: Modifier = Modifier){
    var nextStep by remember {mutableStateOf(0)}
    var previousStep by remember {mutableStateOf(0)}
    when (nextStep) {
        0 -> {
            MyArtGalleryStartAndEnd(
                imageResourceId = R.drawable.artgalleryopening,
                contentDescriptionResourceId = R.string.PictureDescription,
                TitleResourceId = R.string.app_description,
                DescriptionResourceId = R.string.Creator,
                Act = R.string.Start,
                onStartButtonClick = {
                    nextStep += 1
                }
            )
        }

        1 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_1,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_1,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.iPhone,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        2 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_2,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_2,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.iPhone,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        3 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_3,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_3,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.iPhone,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        4 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_4,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_4,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.iPhone,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        5 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_5,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_5,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.iPhone,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        6 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_6,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_6,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.iPhone,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        7 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_7,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_7,
                creatorResourceId = R.string.Creator,
                shotonResourceId = R.string.Android,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }

        8 -> {
            MyArtGalleryLayout(
                imageResourceId = R.drawable.picture_8,
                contentDescriptionResourceId = R.string.PictureDescription,
                pictureTitleResourceId = R.string.Picture_8,
                creatorResourceId = R.string.Co_Creator,
                shotonResourceId = R.string.Samsung,
                onPreviousClick = {
                    nextStep -= 1
                },
                onNextClick = {
                    nextStep += 1
                }
            )
        }
        9 -> {
            MyArtGalleryStartAndEnd(
                imageResourceId = R.drawable.artgalleryopening,
                contentDescriptionResourceId = R.string.PictureDescription,
                TitleResourceId = R.string.End,
                DescriptionResourceId = R.string.End_Description,
                Act = R.string.Visit_Again,
                onStartButtonClick = {
                    nextStep = 0
                }
            )
        }


    }
}


//@Preview(showBackground = true)
@Composable
fun MyArtGalleryStartAndEnd(
    imageResourceId: Int,
    contentDescriptionResourceId: Int,
    TitleResourceId: Int,
    DescriptionResourceId: Int,
    Act: Int,
    onStartButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //val imageResource = R.drawable.artgalleryopening
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){  Box(modifier = Modifier
        .padding(20.dp),
        contentAlignment = Alignment.TopCenter )
        {
            Image(
                    painter = painterResource(imageResourceId),
                    contentDescription = stringResource(contentDescriptionResourceId),
                    modifier = Modifier
                        .size(585.dp)
                        //.width(500.dp)
                        //.height(500.dp)
                        .shadow(10.dp)
                        .padding(top = 28.dp, bottom = 23.dp)
                )
        }
        Text(
            text = stringResource(TitleResourceId),
            fontSize = 36.sp,
            modifier = Modifier.padding(top = 20.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(DescriptionResourceId),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp),
        )
        Button(
            onClick = onStartButtonClick,
            modifier = Modifier
                .padding(top = 20.dp)
                .height(80.dp)
                .width(200.dp),
            shape = RoundedCornerShape(ZeroCornerSize),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),

        ){
            Text(
                text = stringResource(Act),
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun MyArtGalleryLayout(
    imageResourceId: Int,
    contentDescriptionResourceId: Int,
    pictureTitleResourceId: Int,
    creatorResourceId: Int,
    shotonResourceId: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
){
    //val imageResource = R.drawable.picture_1
    //val stringResource = R.string.PictureDescription
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {   Box(modifier = Modifier
        .padding(20.dp),
        contentAlignment = Alignment.TopCenter ) {
        Image(
            painter = painterResource(imageResourceId),
            contentDescription = stringResource(contentDescriptionResourceId),
            modifier = Modifier
                .size(500.dp)
                .shadow(10.dp)
                //.width(500.dp)
                //.height(500.dp)
                .padding(top = 28.dp, bottom = 23.dp, start = 23.dp, end = 23.dp)
            )
        }
        Text(
            text = stringResource(pictureTitleResourceId),
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 20.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(creatorResourceId),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(shotonResourceId),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Button(
                onClick = onPreviousClick,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(50.dp)
                    .width(140.dp),
                shape = RoundedCornerShape(ZeroCornerSize),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            )
            {
                Text(
                    text = stringResource(R.string.Previous),
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .padding(top = 20.dp, start = 70.dp)
                    .height(50.dp)
                    .width(140.dp),
                shape = RoundedCornerShape(ZeroCornerSize),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            )
            {
                Text(
                    text = stringResource(R.string.Next),
                    fontSize = 15.sp,
                    color = Color.White,
                )
            }
        }
    }
}
