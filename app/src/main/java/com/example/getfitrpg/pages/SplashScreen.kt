package com.example.getfitrpg.pages

import com.example.getfitrpg.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.getfitrpg.R
@Composable
fun Splash(modifier: Modifier){

    Column(
        modifier = Modifier.fillMaxSize().background(Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Splash Image",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
    }

}


