package com.example.wetaherapp2.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wetaherapp2.R
import com.example.wetaherapp2.data.WeatherParametres

@SuppressLint("DiscouragedApi")
@Composable
fun RightNowCard(currentWeatherState: MutableState<WeatherParametres>, context: Context, packageName:String, onClickSync: ()-> Unit){
    var toCelsius:Int
    var toCelsiusFL:Int
    val weatherNow:String
    try{
        toCelsius=(currentWeatherState.value.temp-273.15).toInt()
        toCelsiusFL=(currentWeatherState.value.tempFl-273.15).toInt()
    }catch (e:java.lang.NumberFormatException){
        toCelsius=0
        toCelsiusFL=0
    }
    val famousWeather= listOf("clear","clouds","dust","extreme","fog","rain","snow","thunderstorm","wind","")
    weatherNow = if(currentWeatherState.value.weather.lowercase() in famousWeather){
        if (currentWeatherState.value.weather.lowercase()=="wind" || currentWeatherState.value.weather.lowercase()=="dust"){
            currentWeatherState.value.weather.lowercase()
        }else{
            currentWeatherState.value.weather.lowercase().ifEmpty { "clear" }+"_"+currentWeatherState.value.dayTime.ifEmpty { "d" }
        }
    }else{//IMO, unknown weather needs an attention of the user; in situations when state is just empty i prefer to show user the most default icon(clear_d)
        "extreme_"+currentWeatherState.value.dayTime.ifEmpty { "d" }
    }


    val weatherNowId:Int = context.resources.getIdentifier(weatherNow,"drawable",packageName)



    Column {
        Row{
            Text(
                text = "Right now",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(6.dp)
            )

            IconButton(onClick = {
                onClickSync.invoke()
                Toast.makeText(
                    context,
                    "Weather data were successfully updated",
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Icon(painter = painterResource(id = R.drawable.baseline_cloud_sync_24),
                    contentDescription = "Sync icon",
                    tint= Color.White,
                    modifier = Modifier.size(28.dp))
                //.padding(vertical = 2.dp))
            }
        }

        Row {
            Image(
                painter = painterResource(id = weatherNowId),
                contentDescription = "logo picture"
            )
            Column{
                Text(
                    text = "$toCelsius ℃",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentWeatherState.value.weatherDesc,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column {
                Text(
                    text = "Feels like: $toCelsiusFL ℃",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Pressure: ${currentWeatherState.value.pressure} hPa",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Humidity: ${currentWeatherState.value.humidity} %",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Wind: ${currentWeatherState.value.windSp} m/s",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Visibility: ${currentWeatherState.value.visibility} m",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

            }

        }

    }
}