package com.example.getfitrpg.feature.auth.signup

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.RPGBackgroundBlue
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGWhite
import com.example.getfitrpg.core.designsystem.components.RPGButton
import com.example.getfitrpg.feature.auth.AuthManager
import kotlin.math.roundToInt

@Composable
fun WeightScreen(
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    authManager: AuthManager
) {
    WeightContent(onNextClicked, onBackClicked, authManager)
}

@Composable
fun WeightContent(onNextClicked: () -> Unit, onBackClicked: () -> Unit, authManager: AuthManager) {
    var weight by remember { mutableFloatStateOf(75f) }
    var unit by remember { mutableStateOf("KG") }

    val minWeight = if (unit == "KG") 40f else 88f
    val maxWeight = if (unit == "KG") 150f else 330f

    val onWeightChange = { newWeight: Float ->
        weight = newWeight.coerceIn(minWeight, maxWeight)
    }
    
    MeasurementScreenTemplate(
        title = "WHAT IS YOUR WEIGHT?",
        value = weight,
        unit = unit,
        units = listOf("KG", "LB"),
        onValueChange = onWeightChange,
        onUnitChange = { newUnit ->
             if (unit != newUnit) {
                if (newUnit == "LB") {
                    weight = (weight * 2.20462f).coerceIn(88f, 330f)
                } else {
                    weight = (weight / 2.20462f).coerceIn(40f, 150f)
                }
                unit = newUnit
            }
        },
        onNextClicked = {
            // Save weight to Firebase
            // Convert to KG if needed for standard storage, or store with unit. 
            // Assuming standard storage in KG for now or just storing the raw value.
            // Let's store the value as is for now, or convert to a standard if required by backend.
            // For simplicity, I'll store the value in KG.
            val weightInKg = if (unit == "LB") weight / 2.20462f else weight
            authManager.updateUserWeight(weightInKg)
            onNextClicked()
        },
        onBackClicked = onBackClicked,
        themeColor = RPGGreen
    )
}

@Composable
fun MeasurementScreenTemplate(
    title: String,
    value: Float,
    unit: String,
    units: List<String>,
    onValueChange: (Float) -> Unit,
    onUnitChange: (String) -> Unit,
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    themeColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundBlue)
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "GET FIT RPG",
            style = MaterialTheme.typography.headlineLarge,
            color = RPGWhite,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "USER INFO",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 22.sp),
            color = themeColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Big Number Box with Embedded Unit Selector
        Box(
            modifier = Modifier
                .background(themeColor, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 40.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${value.roundToInt()}",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = MontserratFontFamily
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                // Embedded Unit Selector
                UnitSelector(
                    selectedUnit = unit,
                    units = units,
                    onUnitChange = onUnitChange
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Ruler(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .pointerInput(unit) { 
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onValueChange(value - dragAmount.x / 20f)
                    }
                },
            currentValue = value,
            unit = unit,
            themeColor = themeColor
        )


        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClicked,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RPGWhite),
                border = BorderStroke(3.dp, Color.White),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(50.dp)
            ) {
                 Icon(
                     imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     contentDescription = "Back",
                     tint = RPGWhite
                 )
            }
            RPGButton(
                text = "NEXT",
                onClick = onNextClicked,
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.headlineMedium.copy(fontSize = 12.sp)
            )
        }
    }
}

@Composable
fun UnitSelector(
    selectedUnit: String,
    units: List<String>,
    onUnitChange: (String) -> Unit
) {
    val selectedIndex = units.indexOf(selectedUnit)

    Box(
        modifier = Modifier
            .width(140.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.1f)) 
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            val maxWidth = maxWidth
            val tabWidth = maxWidth / units.size

            val indicatorOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "indicator"
            )

            // Sliding Indicator
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(tabWidth)
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black) 
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                units.forEach { item ->
                    val isSelected = item == selectedUnit
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onUnitChange(item) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            fontFamily = MontserratFontFamily,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Ruler(
    modifier: Modifier = Modifier,
    currentValue: Float,
    unit: String,
    themeColor: Color
) {
    val tickColor = Color.Gray
    val numberColor = Color.White
    
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = numberColor.toArgb()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val unitTextPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = themeColor.toArgb()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val tickHeight = 30.dp
    val smallTickHeight = 15.dp
    val numberYOffset = 40.dp
    val unitYOffset = 80.dp 
    val pixelsPerUnit = 20.dp


    Canvas(modifier = modifier) {
        val width = size.width
        val center = width / 2f
        val pixelsPerUnitPx = pixelsPerUnit.toPx()

        val textHeight = 14.sp.toPx()
        textPaint.textSize = textHeight
        unitTextPaint.textSize = 24.sp.toPx() 

        // Draw center line
        drawLine(
            color = themeColor,
            start = Offset(center, 0f),
            end = Offset(center, tickHeight.toPx()),
            strokeWidth = 2.dp.toPx()
        )

        val startVal = (currentValue - (center / pixelsPerUnitPx)).toInt()
        val endVal = (currentValue + (center / pixelsPerUnitPx)).toInt()

        for (w in startVal..endVal) {
            val x = center + (w - currentValue) * pixelsPerUnitPx
            if (x in 0f..width) {
                if (w % 5 == 0) {
                     drawLine(
                        color = tickColor,
                        start = Offset(x, 0f),
                        end = Offset(x, tickHeight.toPx()),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            "$w",
                            x,
                            tickHeight.toPx() + numberYOffset.toPx(),
                            textPaint
                        )
                    }
                } else {
                    drawLine(
                        color = tickColor,
                        start = Offset(x, 0f),
                        end = Offset(x, smallTickHeight.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
        }

        drawIntoCanvas {
             it.nativeCanvas.drawText(
                unit,
                center,
                tickHeight.toPx() + unitYOffset.toPx(),
                unitTextPaint
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF101828)
@Composable
fun WeightScreenPreview() {
    GetFitRPGTheme {
        WeightScreen(onNextClicked = {}, onBackClicked = {}, authManager = AuthManager())
    }
}
