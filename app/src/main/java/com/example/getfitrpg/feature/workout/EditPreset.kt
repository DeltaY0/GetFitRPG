package com.example.getfitrpg.feature.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.navigation.components.AnimatedBottomBar
import com.example.getfitrpg.navigation.components.BottomNavItem
import com.example.getfitrpg.core.designsystem.*

val RepBlue = Color(0xFF67E8F9)
val SetYellow = Color(0xFFFACC15)
val AddWorkoutBlue = Color(0xFF0EA5E9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPresetScreen() {
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
            PresetTitleBar()
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    WorkoutSection()
                }
            }

            BottomActionButtons()
        }
    }
}


@Composable
fun PresetTitleBar() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.Black)
            }
            Text(
                text = "PRESET #6",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = MontserratFontFamily
            )
            IconButton(onClick = { /* Handle edit */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Name", tint = Color.Black)
            }
        }
    }
}

@Composable
fun WorkoutSection() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "WARMUP",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = MontserratFontFamily
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "x3",
                color = SetYellow,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = MontserratFontFamily
            )
        }
        ExerciseItem(reps = "30r", name = "SITUPS", sets = "x1")
        ExerciseItem(reps = "15r", name = "PULL UPS", sets = "x1")
        ExerciseItem(reps = "0:30s", name = "PULL UPS", sets = "x1")

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AddWorkoutBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ADD WORKOUT", fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = MontserratFontFamily, color = TextWhite)
        }
    }
}

@Composable
fun ExerciseItem(reps: String, name: String, sets: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = reps, color = RepBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily, modifier = Modifier.weight(1f))
            Text(text = sets, color = SetYellow, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily)
        }
    }
}


@Composable
fun BottomActionButtons() {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("ADD SECTION", fontWeight = FontWeight.Bold, color = Color.Black, fontFamily = MontserratFontFamily)
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text("REORDER", fontWeight = FontWeight.Bold, color = Color.Black, fontFamily = MontserratFontFamily)
            }
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LevelProgressGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SAVE PRESET", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ScreenBackground, fontFamily = MontserratFontFamily)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101828)
@Composable
fun EditPresetScreenPreview() {
    GetFitRPGTheme {
        EditPresetScreen()
    }
}
