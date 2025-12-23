package com.example.getfitrpg.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.RPGBackgroundDark
import com.example.getfitrpg.core.designsystem.RPGGreen
import kotlinx.coroutines.launch

@Composable
fun RPGButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = RPGGreen,
    contentColor: Color = RPGBackgroundDark,
    textStyle: TextStyle? = null
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null // Removes ripple effect
            ) {
                scope.launch {
                    scale.animateTo(
                        targetValue = 0.90f,
                        animationSpec = tween(durationMillis = 50)
                    )
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 100)
                    )
                    onClick()
                }
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = contentColor,
                style = textStyle ?: MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )
        }
    }
}