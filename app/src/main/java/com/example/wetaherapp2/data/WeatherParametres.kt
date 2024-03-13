package com.example.wetaherapp2.data

data class WeatherParametres(
    val city:String,
    val temp: Double,
    val tempFl: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure:String,
    val humidity:String,
    val weather:String,
    val weatherDesc:String,
    val clouds:String,
    val windSp:String,
    val visibility:String,
    val dayTime:String,
    var time:String
)
