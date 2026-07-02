package com.example.myartgallery.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, "artFeed")
    object Upload : BottomNavItem("Upload", Icons.Filled.Add, "uploadArt")
    object Profile : BottomNavItem("Profile", Icons.Filled.AccountCircle, "profile/{userId}")
}


