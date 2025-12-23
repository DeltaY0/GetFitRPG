package com.example.getfitrpg.feature.auth.signup

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.AccentYellow
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.core.designsystem.TextWhite
import kotlin.math.roundToInt

@Composable
fun WeightScreen(
    onNextClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    var weight by remember { mutableStateOf(75f) }
    var unit by remember { mutableStateOf("KG") }

    val onWeightChange = { newWeight: Float ->
        weight = newWeight.coerceIn(40f, 150f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101828))
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "GET FIT RPG",
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "USER INFO",
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "WHAT IS YOUR WEIGHT?",
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        UnitSelector(selectedUnit = unit, onUnitChange = { unit = it })

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .background(PrimaryGreen, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 80.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${weight.roundToInt()}",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = MontserratFontFamily
            )
        }

        WeightRuler(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onWeightChange(weight - dragAmount.x / 20f)
                    }
                },
            currentWeight = weight,
            unit = unit
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
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                border = BorderStroke(1.dp, Color.White),
                modifier = Modifier.size(50.dp)
            ) {
                 Text("<", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Button(
                onClick = onNextClicked,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("NEXT >>>", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily)
            }
        }
    }
}

@Composable
fun UnitSelector(
    selectedUnit: String,
    onUnitChange: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row {
            TextButton(
                onClick = { onUnitChange("KG") },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (selectedUnit == "KG") AccentYellow else Color.Transparent,
                    contentColor = if (selectedUnit == "KG") Color.Black else Color.Black
                ),
                 modifier = Modifier.width(80.dp)
            ) {
                Text("KG", fontWeight = FontWeight.Bold)
            }
            TextButton(
                onClick = { onUnitChange("LB") },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (selectedUnit == "LB") AccentYellow else Color.Transparent,
                    contentColor = if (selectedUnit == "LB") Color.Black else Color.White
                ),
                 modifier = Modifier.width(80.dp)
            ) {
                Text("LB", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WeightRuler(
    modifier: Modifier = Modifier,
    currentWeight: Float,
    unit: String,
    minWeight: Int = 40,
    maxWeight: Int = 150
) {
    val tickColor = Color.Gray
    val numberColor = Color.White
    val centerLineColor = PrimaryGreen

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = numberColor.toArgb()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val unitTextPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = PrimaryGreen.toArgb()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val density = LocalDensity.current
    val tickHeight = 30.dp
    val smallTickHeight = 15.dp
    val numberYOffset = 40.dp
    val unitYOffset = 50.dp
    val pixelsPerUnit = 20.dp


    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val center = width / 2f
        val pixelsPerUnitPx = pixelsPerUnit.toPx()

        val textHeight = 14.sp.toPx()
        textPaint.textSize = textHeight
        unitTextPaint.textSize = 18.sp.toPx()

        // Draw center line
        drawLine(
            color = centerLineColor,
            start = Offset(center, 0f),
            end = Offset(center, tickHeight.toPx()),
            strokeWidth = 2.dp.toPx()
        )

        val startWeight = (currentWeight - (center / pixelsPerUnitPx)).toInt()
        val endWeight = (currentWeight + (center / pixelsPerUnitPx)).toInt()

        for (w in startWeight..endWeight) {
            val x = center + (w - currentWeight) * pixelsPerUnitPx
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
        WeightScreen(onNextClicked = {}, onBackClicked = {})
    }
}
