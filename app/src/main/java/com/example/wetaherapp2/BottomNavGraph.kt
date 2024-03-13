package com.example.wetaherapp2

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wetaherapp2.data.CityParametres
import com.example.wetaherapp2.screens.ForecastScreen
import com.example.wetaherapp2.screens.LocationsScreen
import com.example.wetaherapp2.screens.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController, context: Context, packageName:String, cityState: MutableState<CityParametres>){
    NavHost(navController=navController,
        startDestination=BottomBarScreen.Locations.route
    ){
        composable(route=BottomBarScreen.Locations.route){
            LocationsScreen(cityState,context)
        }
        composable(route=BottomBarScreen.Forecast.route){
            ForecastScreen(cityState,context,packageName)
        }
        composable(route=BottomBarScreen.Settings.route){
            SettingsScreen()
        }
    }
}