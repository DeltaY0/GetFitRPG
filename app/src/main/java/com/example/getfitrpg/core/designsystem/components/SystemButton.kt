package com.example.getfitrpg.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.getfitrpg.core.designsystem.*

@Composable
fun SystemButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    type: SystemButtonType = SystemButtonType.Primary
) {
    val shape = RoundedCornerShape(8.dp)
    val height = 52.dp

    if (type == SystemButtonType.Primary) {
        Button(
            onClick = onClick,
            modifier = modifier.fillMaxWidth().height(height),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                contentColor = BackgroundDark
            )
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.fillMaxWidth().height(height),
            shape = shape,
            border = BorderStroke(1.dp, PrimaryGreen), // Green Border
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextWhite // White text
            )
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

enum class SystemButtonType {
    Primary, Secondary
}