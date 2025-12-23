package com.example.getfitrpg.feature.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.navigation.components.AnimatedBottomBar
import com.example.getfitrpg.navigation.components.BottomNavItem

val ScreenBackground = Color(0xFF101828)
val CardBackground = Color(0xFF1E293B)
val TextWhite = Color.White
val TextGrey = Color.Gray
val PlayerNameYellow = Color(0xFFFBBF24)
val LevelProgressGreen = Color(0xFF4ADE80)
val StrRed = Color(0xFFF87171)
val AgiBlue = Color(0xFF60A5FA)
val VitGreen = Color(0xFF4ADE80)
val IntYellow = Color(0xFFFACC15)
val DumbbellYellow = Color(0xFFD69E2E)
val FireOrange = Color(0xFFFB923C)
val PomodoroGreen = Color(0xFF34D399)
val AiRed = Color(0xFFF87171)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedNavItem by remember { mutableStateOf(BottomNavItem.Home) }

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
            verticalArrangement = Arrangement.SpaceAround
        ) {
            PlayerHeader()
            StartWorkoutCard(onStartWorkoutClicked = {})
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CharacterStatsCard(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WorkoutsInARowCard(modifier = Modifier.fillMaxWidth())
                    PomodoroTimerCard(modifier = Modifier.fillMaxWidth(), onClick = {})
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RecentWorkoutCard(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
                WorkoutAICard(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())
            }
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
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(CardBackground, RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(4.dp)
                .background(LevelProgressGreen, RoundedCornerShape(2.dp)))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartWorkoutCard(onStartWorkoutClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        onClick = onStartWorkoutClicked
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_workout),
                contentDescription = "Start Workout",
                tint = DumbbellYellow,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "START A NEW\nWORKOUT",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = MontserratFontFamily,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun CharacterStatsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("CHARACTER STATS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
            StatItem("STR", 12, StrRed)
            StatItem("AGI", 7, AgiBlue)
            StatItem("VIT", 5, VitGreen)
            StatItem("INT", 10, IntYellow)
        }
    }
}

@Composable
fun StatItem(name: String, value: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = name, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = MontserratFontFamily)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "%02d".format(value), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = MontserratFontFamily)
    }
}

@Composable
fun WorkoutsInARowCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Workouts in a row",
                    tint = FireOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("17", color = IntYellow, fontWeight = FontWeight.Bold, fontSize = 32.sp)
            }
            Text("WORKOUTS IN A ROW", color = TextGrey, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTimerCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_dumbbell_timer),
                contentDescription = "Pomodoro Timer",
                tint = PomodoroGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "POMODORO\nTIMER >>",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun RecentWorkoutCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("RECENT WORKOUT", color = TextGrey, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("01.12.2025", color = IntYellow, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("PRESET #5", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("LEGS", color = VitGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun WorkoutAICard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("WORKOUT AI", color = TextGrey, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.icon_ai),
                contentDescription = "Workout AI",
                tint = AiRed,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GetFitRPGTheme {
        HomeScreen()
    }
}