package com.example.getfitrpg.feature.auth.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.core.designsystem.TextWhite

val ArchiveBlue = Color(0xFF818CF8)
val FoodScanGreen = Color(0xFFA3E635)
val FocusModePink = Color(0xFFF472B6)
val CardBackground = Color(0xFF1E293B)
val ScreenBackground = Color(0xFF101828)

@Composable
fun OnboardingScreen(onStartClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "GET FIT RPG",
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "FEATURES",
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "ARCHIVE",
                titleColor = ArchiveBlue,
                description = "2,000+ Predefined Workouts, each with it's own tutorial video built in.\n\nFully Offline. No internet required."
            )
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "FOOD SCAN",
                titleColor = FoodScanGreen,
                description = "Scan food, get estimated calories per gram.\n\nStay healthy. Meet your goals. Level up."
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FeatureCard(
            modifier = Modifier.fillMaxWidth(),
            title = "THE SYSTEM",
            titleColor = TextWhite,
            description = "Your life, gamified. Earn XP for every rep, level up your stats (STR, AGI, INT), and unlock your potential."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "FOCUS MODE",
                titleColor = FocusModePink,
                description = "Pomodoro timers for HIIT, stretching, or focused grinding."
            )
            FeatureCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                title = "AI SENSEI",
                titleColor = PrimaryGreen,
                description = "Custom workout plans generated instantly based on your goals.\n\nCustom diet plans curated for your dietary restrictions.\n"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color.White),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
             Button(
                onClick = onStartClicked,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "START",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    fontFamily = MontserratFontFamily,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color,
    description: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Normal,
                color = TextWhite.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    GetFitRPGTheme {
        OnboardingScreen(onStartClicked = {})
    }
}
