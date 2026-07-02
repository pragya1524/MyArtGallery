package com.example.myartgallery

import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myartgallery.ui.theme.MyArtGalleryTheme
import com.google.firebase.auth.FirebaseAuth
import com.example.myartgallery.ui.auth.AuthScreen
import com.example.myartgallery.ui.upload.UploadArtScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myartgallery.ui.feed.ArtFeedScreen
import com.example.myartgallery.ui.profile.ProfileScreen
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myartgallery.ui.navigation.BottomNavItem

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
                    val auth = FirebaseAuth.getInstance()
                    var currentUser by remember { mutableStateOf(auth.currentUser) }

                    if (currentUser == null) {
                        AuthScreen(onAuthSuccess = {
                            currentUser = auth.currentUser
                        })
                    } else {
                    MyArtGalleryApp()
                    }
                }
            }
        }
    }
}

@Composable
fun MyArtGalleryApp( modifier: Modifier = Modifier){
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(BottomNavItem.Home, BottomNavItem.Upload, BottomNavItem.Profile)
                items.forEach { screen ->
                    val isCurrentUserProfile = screen == BottomNavItem.Profile && auth.currentUser?.uid != null
                    val route = if (isCurrentUserProfile) "profile/${auth.currentUser?.uid}" else screen.route
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true ||
                                (isCurrentUserProfile && currentDestination?.route == "profile/{userId}"),
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = BottomNavItem.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(BottomNavItem.Home.route) {
                ArtFeedScreen(navController = navController)
            }
            composable(BottomNavItem.Upload.route) {
                UploadArtScreen(onUploadSuccess = { navController.popBackStack() })
            }
            composable(BottomNavItem.Profile.route) {
                val userId = it.arguments?.getString("userId") ?: auth.currentUser?.uid
                if (userId != null) {
                    ProfileScreen(userId = userId)
                }
            }
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
