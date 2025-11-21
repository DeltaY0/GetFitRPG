package com.example.getfitrpg.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.getfitrpg.core.designsystem.*

@Composable
fun SystemStatBar(
    modifier: Modifier = Modifier,
    label: String,      // e.g. "XP" or "HP"
    current: Int,       // e.g. 50
    max: Int,           // e.g. 100
    color: Color = PrimaryGreen
) {
    val progress = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)

    Column(modifier = modifier.fillMaxWidth()) {
        // Label Row: "XP        50/100"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$current / $max",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextWhite
                )
            )
        }

        // The Bar
        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceDark) // The empty background
                .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress) // Fills based on %
                    .background(color)
            )
        }
    }
}