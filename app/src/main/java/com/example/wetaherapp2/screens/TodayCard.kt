package com.example.wetaherapp2.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wetaherapp2.data.WeatherParametres

@Composable
fun TodayCard(forecastState: MutableState<List<WeatherParametres>>){
    val today:WeatherParametres = try{
        getWeatherToday(forecastState.value)

    }catch(e:java.lang.IndexOutOfBoundsException){
        WeatherParametres("",0.0,0.0,0.0,0.0,"","","",
            "","","","","","2024-01-01")
    }
    val toCelsiusMin:String = (today.tempMin-273.15).toInt().toString()
    val toCelsiusMax:String = (today.tempMax-273.15).toInt().toString()



    Row {

        Text(
            text = "Today:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )
        Text(
            text = today.time,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )
        Text(
            text = "${toCelsiusMax}/${toCelsiusMin} ℃",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )

    }
}

private fun getWeatherToday(day: List<WeatherParametres>):WeatherParametres{
    var minTemp:Double
    var maxTemp:Double
    var splittedListArmagedon:List<String>
    val sortedDay:WeatherParametres

    val splittedList:List<String> = day[0].time.split(" ")
    minTemp=day[0].temp
    maxTemp=day[0].temp
    for(i in 1 until day.size){
        splittedListArmagedon=day[i].time.split(" ")
        if(splittedList[0]!=splittedListArmagedon[0]) {
            break
        }
        if(minTemp>day[i].temp)
            minTemp=day[i].temp
        if(maxTemp<day[i].temp)
            maxTemp=day[i].temp
    }
    sortedDay=WeatherParametres("",0.0,0.0,minTemp,maxTemp,"",
        "","","","","","","",splittedList[0])

    return sortedDay
}