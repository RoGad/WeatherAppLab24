package com.example.wetaherapp2.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wetaherapp2.data.CityParametres
import com.example.wetaherapp2.data.WeatherParametres
import org.json.JSONObject

const val API_KEY="83fe5e57923168014763ce74191e8111"

@Composable
fun ForecastScreen(cityState: MutableState<CityParametres>, context: Context, packageName:String){
    val forecastState= remember {
        mutableStateOf(listOf<WeatherParametres>())
    }
    val currentWeatherState= remember{
        mutableStateOf(WeatherParametres("Omsk", 0.0,0.0,0.0,0.0,"...","...","",
            "...","...","...","...","","..."))
    }
    for (i in 0 until forecastState.value.size){
        forecastState.value[i].temp

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            RightNowCard(currentWeatherState = currentWeatherState,context,packageName, onClickSync = {
                updateWeather(cityState.value.city,forecastState,currentWeatherState,context)
            })

            TodayCard(forecastState)
            TabLayout(forecastState,context,packageName,cityState.value.UTC)


        }
    }
    updateWeather(cityState.value.city,forecastState,currentWeatherState,context)
}

private fun updateWeather(city: String, forecastState: MutableState<List<WeatherParametres>>
                          , currentWeatherState: MutableState<WeatherParametres>, context: Context
){

    val url= "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$API_KEY"

    val queue = Volley.newRequestQueue(context)
    val stringRequest= StringRequest(
        Request.Method.GET,
        url,
        {
                responce ->
            //val obj=JSONObject(responce)
            //val jbo=JSONArray(responce)
            val data_list= getWeather(responce)
            forecastState.value=data_list
            currentWeatherState.value=data_list[0]
            //Log.d("MyLog","Responce:$data_list")
            //state.value=jbo.getJSONObject(0).getString("temp")
            //state.value=obj.getJSONObject("main").getString("temp")
        },
        {
                error->
            Toast.makeText(
                context,
                "Sorry, we can't find that city.\nPlease make sure that you wrote everything alright!",
                Toast.LENGTH_LONG
            ).show()
            Log.d("MyLog","Error: $error")
        }
    )
    queue.add(stringRequest)
}

private fun getWeather(response: String): List<WeatherParametres>{
    if (response.isEmpty()) return listOf()

    val list=ArrayList<WeatherParametres>()
    val mainObject= JSONObject(response)
    val city=mainObject.getJSONObject("city").getString("name")
    val hours=mainObject.getJSONArray("list")
    for (i in 0 until hours.length()){
        val item=hours[i] as JSONObject
        list.add(
            WeatherParametres(
                city,
                item.getJSONObject("main").getString("temp").toDouble(),
                item.getJSONObject("main").getString("feels_like").toDouble(),
                //item.getJSONObject("main").getString("temp_min"),
                0.0,
                0.0,
                //item.getJSONObject("main").getString("temp_max"),
                item.getJSONObject("main").getString("pressure"),
                item.getJSONObject("main").getString("humidity"),
                item.getJSONArray("weather").getJSONObject(0).getString("main"),
                item.getJSONArray("weather").getJSONObject(0).getString("description"),
                item.getJSONObject("clouds").getString("all"),
                item.getJSONObject("wind").getString("speed"),
                item.getString("visibility"),
                item.getJSONObject("sys").getString("pod"),
                item.getString("dt_txt")
            )
        )
    }
    return list
}