package com.example.getfitrpg.feature.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.navigation.components.AnimatedBottomBar
import com.example.getfitrpg.navigation.components.BottomNavItem
val ScreenBackground = Color(0xFF101828)
val CardBackground = Color(0xFF1E293B)
val TextWhite = Color.White
val TextGrey = Color.Gray
val PlayerNameYellow = Color(0xFFFBBF24)
val LevelProgressGreen = Color(0xFF4ADE80)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen() {
    var selectedNavItem by remember { mutableStateOf(BottomNavItem.Workout) }

    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = { AnimatedBottomBar(selectedItem = selectedNavItem, onItemSelected = { selectedNavItem = it }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            PlayerHeader()
            Spacer(modifier = Modifier.height(24.dp))
            PresetsHeader()
            Spacer(modifier = Modifier.height(16.dp))
            SearchPresets()
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(6) {
                    PresetItem()
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LevelProgressGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("NEW PRESET", fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily, color = ScreenBackground)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PlayerHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(PlayerNameYellow, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "R", color = ScreenBackground, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Column {
                Text(text = "PLAYER NAME", color = TextGrey, fontSize = 12.sp, fontFamily = MontserratFontFamily)
                Text(text = "CLASS NAME", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = MontserratFontFamily)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "LVL 027", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = MontserratFontFamily)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(CardBackground, RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(4.dp)
                    .background(LevelProgressGreen, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun PresetsHeader() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .border(BorderStroke(1.dp, TextGrey), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.icon_crossmark), contentDescription = null, tint = TextWhite, modifier = Modifier.size(24.dp))
            Icon(painter = painterResource(id = R.drawable.icon_ai), contentDescription = null, tint = TextWhite, modifier = Modifier.size(24.dp))
            Icon(painter = painterResource(id = R.drawable.icon_discover), contentDescription = null, tint = TextWhite, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "MY\nPRESETS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = MontserratFontFamily, textAlign = TextAlign.End)
    }
}

@Composable
fun SearchPresets() {
    var searchText by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Search Presets", color = TextGrey) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = TextGrey) },
        trailingIcon = { Icon(painter = painterResource(id = R.drawable.icon_edit), contentDescription = "Filter", tint = TextGrey) }, //Needs to insert the true icon
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = CardBackground,
            focusedContainerColor = CardBackground,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite
        )
    )
}

@Composable
fun PresetItem() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Preset #1 - Legs", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = MontserratFontFamily)
                Text("tags tags tags tags tags", color = TextGrey, fontSize = 12.sp, fontFamily = MontserratFontFamily)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.MoreHoriz, contentDescription = "More Options", tint = TextGrey)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.icon_arrow), contentDescription = "Start Preset", tint = TextWhite)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenPreview() {
    GetFitRPGTheme {
        WorkoutScreen()
    }
}