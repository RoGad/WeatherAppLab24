package com.example.wetaherapp2

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route:String,
    val title:String,
    val icon:ImageVector
) {
    data object Locations: BottomBarScreen(
        route="locations",
        title="Locations",
        icon= Icons.Default.Star
    )
    data object Forecast: BottomBarScreen(
        route="forecast",
        title="Forecast",
        icon=Icons.Default.Search
    )
    data object Settings: BottomBarScreen(
        route="settings",
        title="Settings",
        icon=Icons.Default.Settings
    )
}