package com.example.wetaherapp2

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wetaherapp2.data.CityParametres

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(context: Context, packageName: String) {
    val navController = rememberNavController()

    val defaultCity = "Novosibirsk"
    val defaultCountry = "Russia"
    val cityState = remember { mutableStateOf(CityParametres(defaultCity, defaultCountry, "7")) }

    Scaffold(
        topBar = { TopBar(cityState = cityState.value) },
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController, context = context, packageName = packageName, cityState = cityState)
    }
}

@Composable
fun TopBar(cityState: CityParametres) {
    Text(
        text = "${cityState.country}, ${cityState.city}",
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 14.dp)
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Locations,
        BottomBarScreen.Forecast,
        BottomBarScreen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach { screen ->
            AddBottomNavigationItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddBottomNavigationItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = { Text(text = screen.title) },
        icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}