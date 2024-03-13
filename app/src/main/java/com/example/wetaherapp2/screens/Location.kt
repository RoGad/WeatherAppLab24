package com.example.wetaherapp2.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wetaherapp2.R
import com.example.wetaherapp2.data.CityParametres

@Composable
fun LocationsScreen(cityState: MutableState<CityParametres>, context: Context){

    val cities:List<CityParametres> = getCities()

    val dialogState= remember{ mutableStateOf(false) }
    if(dialogState.value){
        DialogSeach(dialogState, onSubmit = {
            val ellements=it.replace(" ","").split(",")
            try{
                cityState.value= CityParametres(ellements[0],ellements[1],ellements[2])
            }catch(e:java.lang.IndexOutOfBoundsException){
                Toast.makeText(context,"Can't make a request: the recording format was not maintained.\nPlease use example format to record your city in request"
                    , Toast.LENGTH_LONG).show()
            }



        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {

            Text(
                text = "Choose your city",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Surface(
                elevation = 0.5.dp,

                color= Color(128,128,128,255),
                shape = MaterialTheme.shapes.medium){
                Row {
                    Text(
                        text = "Search",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp)
                    )
                    IconButton(onClick = { dialogState.value=true }) {
                        Icon(painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription ="SearchButton",
                            tint= Color.White)
                    }
                }

            }

            LazyColumn(modifier = Modifier.fillMaxSize()){
                itemsIndexed(cities){_,city->
                    standartCities(city,cityState)
                }
            }
        }
    }
}
@Composable
fun DialogSeach(dialogState: MutableState<Boolean>, onSubmit:(String) -> Unit){
    val dialogText= remember{ mutableStateOf("City_name,country,UTC") }
    AlertDialog(onDismissRequest = {
        dialogState.value=false
    },
        confirmButton = {
            TextButton(onClick = {
                onSubmit(dialogText.value)
                dialogState.value=false }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value=false }) {
                Text("Cancel")
            }
        },
        title={
            Column(modifier = Modifier.fillMaxWidth()){
                Text(text="Enter your city")
                TextField(value=dialogText.value,onValueChange={
                    dialogText.value=it
                })
            }
        })

}

@Composable
fun standartCities(city:CityParametres,cityState: MutableState<CityParametres>){
    var isExpanded by remember { mutableStateOf(false) }
    Surface(
        elevation = 0.5.dp,
        color = if(isExpanded) Color(244, 209, 68, 240) else MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded }){

        Text(text=city.city,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(6.dp))

        Text(text="UTC:"+city.UTC,
            fontSize = 12.sp,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp))
        if (isExpanded)
            cityState.value=city


    }

}

private fun getCities():List<CityParametres>{
    val standartCities=listOf("Omsk","Novosibirsk","Tomsk","Astana","Tallinn","Tromso","Detroit","Mumbai")
    val standartCountries=listOf("Russia","Russia","Russia","Kazakhstan","Estonia","Norway","USA","India")
    val utc=listOf("6","7","7","6","3","2","-4","5")
    val listy=ArrayList<CityParametres>()
    for(i in standartCities.indices){
        listy.add(CityParametres(standartCities[i],standartCountries[i], utc[i]))
    }
    return listy

}