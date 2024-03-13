package com.example.wetaherapp2.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wetaherapp2.data.WeatherParametres
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(
    forecastState: MutableState<List<WeatherParametres>>,
    context: Context,
    packageName: String,
    utc: String
) {
    val tabList = listOf("In next 24 hours", "In next 5 days")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.clip(RoundedCornerShape(6.dp))) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = { pos ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, pos)
                )
            },
            backgroundColor = Color(244, 209, 68, 255)
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    }
                )
            }
        }
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { index ->
            val list = when (index) {
                0 -> getWeatherByHours(forecastState.value, utc)
                else -> getWeatherByDays(forecastState.value)
            }
            MainList(list, context, packageName)
        }
    }
}

private fun getWeatherByHours(hours: List<WeatherParametres>, utc: String): List<WeatherParametres> {
    if (hours.isEmpty()) return listOf()
    val sortedHours = ArrayList<WeatherParametres>()
    var rightTime: List<String>
    var rightTimeUTC: Int

    val firstItem = hours[0]
    val firstTime = firstItem.time.split(" ")[1]
    rightTime = firstTime.split(":")
    rightTimeUTC = rightTime[0].toInt() + utc.toInt()
    if (rightTimeUTC > 23) rightTimeUTC %= 24
    else if (rightTimeUTC < 0) rightTimeUTC += 24

    sortedHours.add(
        WeatherParametres(
            "",
            firstItem.temp,
            firstItem.tempFl,
            0.0,
            0.0,
            "",
            "",
            firstItem.weather,
            "",
            "",
            "",
            "",
            firstItem.dayTime,
            "$rightTimeUTC:${rightTime[1]}"
        )
    )

    for (i in 1 until hours.size) {
        val currentItem = hours[i]
        val currentTime = currentItem.time.split(" ")[1]
        if (firstTime == currentTime) break
        rightTime = currentTime.split(":")
        rightTimeUTC = rightTime[0].toInt() + utc.toInt()
        if (rightTimeUTC > 23) rightTimeUTC %= 24
        else if (rightTimeUTC < 0) rightTimeUTC += 24

        sortedHours.add(
            WeatherParametres(
                "",
                currentItem.temp,
                currentItem.tempFl,
                0.0,
                0.0,
                "",
                "",
                currentItem.weather,
                "",
                "",
                "",
                "",
                currentItem.dayTime,
                "$rightTimeUTC:${rightTime[1]}"
            )
        )
    }
    return sortedHours
}

private fun getWeatherByDays(days: List<WeatherParametres>): List<WeatherParametres> {
    if (days.isEmpty()) return listOf()

    val sortedDays = ArrayList<WeatherParametres>()
    var minTemp = days[0].temp
    var maxTemp = days[0].temp
    var checker = true

    val firstItem = days[0]
    var currentDay = firstItem.time.split(" ")[0]

    for (i in 1 until days.size) {
        val currentItem = days[i]
        val currentDaySplit = currentItem.time.split(" ")[0]
        if (currentDay != currentDaySplit) {
            if (checker) checker = false
            else {
                sortedDays.add(
                    WeatherParametres(
                        "",
                        0.0,
                        0.0,
                        minTemp,
                        maxTemp,
                        "",
                        "",
                        days[i - 1].weather,
                        "",
                        "",
                        "",
                        "",
                        "d",
                        currentDay
                    )
                )
            }
            currentDay = currentDaySplit
            minTemp = currentItem.temp
            maxTemp = currentItem.temp
        } else {
            if (minTemp > currentItem.temp) minTemp = currentItem.temp
            if (maxTemp < currentItem.temp) maxTemp = currentItem.temp
        }
    }

    return sortedDays
}

@Composable
fun MainList(list: List<WeatherParametres>, context: Context, packageName: String) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { _, item ->
            ItemList(item, context, packageName)
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun ItemList(item: WeatherParametres, context: Context, packageName: String) {
    var toCelsius: String
    var toCelsiusFl: String
    var toCelsiusMin: String
    var toCelsiusMax: String

    try {
        toCelsius = (item.temp - 273.15).toInt().toString()
        toCelsiusFl = (item.tempFl - 273.15).toInt().toString()
    } catch (e: java.lang.NumberFormatException) {
        toCelsius = "0"
        toCelsiusFl = "0"
    }

    try {
        if (item.tempMin != item.tempMax) {
            toCelsiusMin = (item.tempMin - 273.15).toInt().toString()
            toCelsiusMax = (item.tempMax - 273.15).toInt().toString()
        } else {
            toCelsiusMin = ""
            toCelsiusMax = ""
        }
    } catch (e: java.lang.NumberFormatException) {
        toCelsiusMin = ""
        toCelsiusMax = ""
    }

    val weatherNow: String = (item.weather.ifEmpty { "clear" } + "_" + item.dayTime.ifEmpty { "d" }).lowercase()
    val weatherNowId: Int = context.resources.getIdentifier(weatherNow, "drawable", packageName)

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 0.5.dp,
        color = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text = item.time,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(6.dp)
            )
            Image(
                painter = painterResource(weatherNowId),
                contentDescription = "weather_logo",
                modifier = Modifier.size(52.dp, 48.dp)
            )
            Text(
                text = item.weather,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(6.dp)
            )
            Text(
                text = "${toCelsiusMax.ifEmpty { toCelsius }}/${toCelsiusMin.ifEmpty { toCelsiusFl }} ℃",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}
