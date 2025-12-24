package com.example.getfitrpg.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.ModernizFontFamily
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.OnderFontFamily
import com.example.getfitrpg.core.designsystem.RPGCoolBlue
import com.example.getfitrpg.core.designsystem.RPGDarkBlue
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGWhite
import com.example.getfitrpg.core.designsystem.RPGYellow

@Composable
fun PlayerStatsCard(
    playerName: String,
    className: String,
    level: Int,
    levelProgress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = RPGDarkBlue),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Transparent, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "R",
                        color = RPGYellow,
                        fontWeight = FontWeight.Black,
                        fontFamily = ModernizFontFamily,
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = playerName,
                        color = Color(0xFF94A3B8),
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = className,
                        color = RPGWhite,
                        fontFamily = OnderFontFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 8.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "LVL %03d".format(level),
                    color = RPGCoolBlue,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { levelProgress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = RPGGreen,
                trackColor = Color(0xFF0B1220)
            )
        }
    }
}

@Preview
@Composable
private fun PlayerStatsCardPreview() {
    PlayerStatsCard(
        playerName = "PLAYER NAME",
        className = "CLASS NAME",
        level = 27,
        levelProgress = 0.68f
    )
}