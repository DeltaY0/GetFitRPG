package com.example.getfitrpg.feature.workout

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily

val RepTeal = Color(0xFF2DD4BF)
val ActiveExerciseBlue = Color(0xFF3B82F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningWorkoutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        PlayerHeader()
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TrainingVideoPreview()
            }
            item {
                PowersetSection(number = 1, progress = "2/3")
            }
            item {
                ExerciseItem(reps = "0:30s", name = "HAMMER CURLS", isActive = true)
            }
            item {
                ExerciseItem(reps = "30r", name = "SITUPS")
            }
            item {
                ExerciseItem(reps = "15r", name = "PULL UPS")
            }
            item {
                PowersetSection(number = 2)
            }
            item {
                ExerciseItem(reps = "0:30s", name = "PULL UPS")
            }
        }
        PauseBottomBar(navController = navController)
    }
}


@Composable
fun TrainingVideoPreview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "03:31",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                fontFamily = MontserratFontFamily
            )

            Text(
                text = "TRAINING\nVIDEO PREVIEW",
                color = TextGrey,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = MontserratFontFamily,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun PowersetSection(number: Int, progress: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "POWERSET #$number",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = MontserratFontFamily
        )
        progress?.let {
            Text(
                text = it,
                color = TextGrey,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = MontserratFontFamily
            )
        }
    }
}

@Composable
fun ExerciseItem(reps: String, name: String, isActive: Boolean = false) {
    val backgroundColor = if (isActive) ActiveExerciseBlue else CardBackground

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = reps, color = RepTeal, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily)
            Spacer(modifier = Modifier.weight(0.2f))
            Text(text = name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun PauseBottomBar(navController: NavController) {
    Box(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_sword),
                contentDescription = "Pause",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RunningWorkoutScreenPreview() {
    GetFitRPGTheme {
        // RunningWorkoutScreen() // This will not work with a NavController
    }
}
