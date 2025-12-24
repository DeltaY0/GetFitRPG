package com.example.getfitrpg.feature.timer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.ModernizFontFamily
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.OnderFontFamily
import com.example.getfitrpg.core.designsystem.RPGBackgroundBlue
import com.example.getfitrpg.core.designsystem.RPGContainerBlue
import com.example.getfitrpg.core.designsystem.RPGCoolPurple
import com.example.getfitrpg.core.designsystem.RPGDarkBlue
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGRed
import com.example.getfitrpg.core.designsystem.RPGSurfaceDark
import com.example.getfitrpg.core.designsystem.RPGWhite
import com.example.getfitrpg.core.designsystem.RPGBlue
import com.example.getfitrpg.core.designsystem.RPGCoolBlue
import com.example.getfitrpg.core.designsystem.RPGCoolGreen
import com.example.getfitrpg.core.designsystem.RPGCoolRed
import com.example.getfitrpg.core.designsystem.RPGGrey
import com.example.getfitrpg.core.designsystem.RPGYellow
import com.example.getfitrpg.core.designsystem.components.RPGButton
import com.example.getfitrpg.core.designsystem.components.RPGTextField
import com.example.getfitrpg.core.designsystem.components.PlayerStatsCard

private enum class TimerPhase { Work, ShortBreak, LongBreak }
private enum class TimerMode { Edit, Running }

@Composable
fun TimerScreen(
    playerName: String = "PLAYER NAME",
    className: String = "CLASS NAME",
    level: Int = 27,
    levelProgress: Float = 0.68f
) {
    var mode by rememberSaveable { mutableStateOf(TimerMode.Edit) }
    
    // Timer State
    var workDurationSeconds by rememberSaveable { mutableIntStateOf(12 * 60) } // 12:00
    var shortBreakSeconds by rememberSaveable { mutableIntStateOf(1 * 60) } // 01:00
    var longBreakSeconds by rememberSaveable { mutableIntStateOf(5 * 60) } // 05:00
    var totalReps by rememberSaveable { mutableIntStateOf(3) }
    var totalSets by rememberSaveable { mutableIntStateOf(2) }

    Surface(color = RPGBackgroundBlue) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PlayerStatsCard(
                playerName = playerName,
                className = className,
                level = level,
                levelProgress = levelProgress
            )

            if (mode == TimerMode.Edit) {
                EditTimerContent(
                    workSeconds = workDurationSeconds,
                    shortBreakSeconds = shortBreakSeconds,
                    longBreakSeconds = longBreakSeconds,
                    reps = totalReps,
                    sets = totalSets,
                    onWorkChange = { workDurationSeconds = it },
                    onShortBreakChange = { shortBreakSeconds = it },
                    onLongBreakChange = { longBreakSeconds = it },
                    onRepsChange = { totalReps = it },
                    onSetsChange = { totalSets = it },
                    onStart = { mode = TimerMode.Running }
                )
            } else {
                RunningTimerContent(
                    workDurationSeconds = workDurationSeconds,
                    shortBreakSeconds = shortBreakSeconds,
                    longBreakSeconds = longBreakSeconds,
                    totalReps = totalReps,
                    totalSets = totalSets,
                    onStop = { mode = TimerMode.Edit }
                )
            }
        }
    }
}

@Composable
private fun EditTimerContent(
    workSeconds: Int,
    shortBreakSeconds: Int,
    longBreakSeconds: Int,
    reps: Int,
    sets: Int,
    onWorkChange: (Int) -> Unit,
    onShortBreakChange: (Int) -> Unit,
    onLongBreakChange: (Int) -> Unit,
    onRepsChange: (Int) -> Unit,
    onSetsChange: (Int) -> Unit,
    onStart: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMinutes by remember { mutableStateOf("") }
    var dialogSeconds by remember { mutableStateOf("") }
    var onDialogConfirm by remember { mutableStateOf<(Int) -> Unit>({}) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = dialogTitle, color = RPGWhite, fontFamily = MontserratFontFamily, fontWeight = FontWeight.Bold) },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Minutes", color = RPGGrey, fontSize = 12.sp, fontFamily = MontserratFontFamily, modifier = Modifier.padding(bottom = 4.dp))
                        RPGTextField(
                            value = dialogMinutes,
                            onValueChange = { dialogMinutes = it },
                            placeholder = "00",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Seconds", color = RPGGrey, fontSize = 12.sp, fontFamily = MontserratFontFamily, modifier = Modifier.padding(bottom = 4.dp))
                        RPGTextField(
                            value = dialogSeconds,
                            onValueChange = { dialogSeconds = it },
                            placeholder = "00",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val m = dialogMinutes.toIntOrNull() ?: 0
                    val s = dialogSeconds.toIntOrNull() ?: 0
                    onDialogConfirm(m * 60 + s)
                    showDialog = false
                }) {
                    Text("OK", color = RPGGreen, fontFamily = MontserratFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = RPGRed, fontFamily = MontserratFontFamily, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = RPGSurfaceDark,
            textContentColor = RPGWhite
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        
        TimerSettingCard(
            title = "WORK",
            value = formatTime(workSeconds),
            color = RPGGreen,
            onClick = {
                dialogTitle = "Edit Work Duration"
                dialogMinutes = (workSeconds / 60).toString()
                dialogSeconds = (workSeconds % 60).toString()
                onDialogConfirm = onWorkChange
                showDialog = true
            }
        )

        TimerSettingCard(
            title = "SHORT BREAK",
            value = formatTime(shortBreakSeconds),
            color = RPGYellow,
            onClick = {
                dialogTitle = "Edit Short Break"
                dialogMinutes = (shortBreakSeconds / 60).toString()
                dialogSeconds = (shortBreakSeconds % 60).toString()
                onDialogConfirm = onShortBreakChange
                showDialog = true
            }
        )

        TimerSettingCard(
            title = "LONG BREAK",
            value = formatTime(longBreakSeconds),
            color = Color(0xFFEF4444), // Red
            onClick = {
                dialogTitle = "Edit Long Break"
                dialogMinutes = (longBreakSeconds / 60).toString()
                dialogSeconds = (longBreakSeconds % 60).toString()
                onDialogConfirm = onLongBreakChange
                showDialog = true
            }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CounterCard(
                modifier = Modifier.weight(1f),
                title = "REPS",
                value = reps,
                color = Color(0xFF54D7FF), // Cyan
                onIncrement = { onRepsChange(reps + 1) },
                onDecrement = { if (reps > 1) onRepsChange(reps - 1) }
            )
            CounterCard(
                modifier = Modifier.weight(1f),
                title = "SETS",
                value = sets,
                color = Color(0xFFC084FC), // Purple
                onIncrement = { onSetsChange(sets + 1) },
                onDecrement = { if (sets > 1) onSetsChange(sets - 1) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RPGButton(
            text = "START",
            onClick = onStart,
            containerColor = RPGWhite,
            contentColor = Color.Black
        )
    }
}

@Composable
private fun TimerSettingCard(
    title: String,
    value: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = RPGContainerBlue),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = value,
                    color = color,
                    fontFamily = ModernizFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp
                )
                Text(
                    text = title,
                    color = RPGGrey,
                    fontFamily = OnderFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Edit",
                tint = RPGGrey
            )
        }
    }
}

@Composable
private fun CounterCard(
    modifier: Modifier = Modifier,
    title: String,
    value: Int,
    color: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = RPGContainerBlue),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = RPGGrey,
                fontFamily = ModernizFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = value.toString(),
                    color = color,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 48.sp
                )
                Column {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Up",
                        tint = RPGWhite,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(onClick = onIncrement)
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Down",
                        tint = RPGWhite,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(onClick = onDecrement)
                    )
                }
            }
        }
    }
}

@Composable
private fun RunningTimerContent(
    workDurationSeconds: Int,
    shortBreakSeconds: Int,
    longBreakSeconds: Int,
    totalReps: Int,
    totalSets: Int,
    onStop: () -> Unit
) {
    var phase by rememberSaveable { mutableStateOf(TimerPhase.Work) }
    var phaseRemaining by rememberSaveable { mutableIntStateOf(workDurationSeconds) }
    var totalElapsedSeconds by rememberSaveable { mutableIntStateOf(0) }
    var repsDone by rememberSaveable { mutableIntStateOf(1) }
    var setsDone by rememberSaveable { mutableIntStateOf(1) }
    var isRunning by rememberSaveable { mutableStateOf(true) }

    // Calculate total expected duration
    val oneSetDuration = (totalReps * workDurationSeconds) + ((totalReps - 1) * shortBreakSeconds)
    val totalExpectedSeconds = (totalSets * oneSetDuration) + ((totalSets - 1) * longBreakSeconds)

    LaunchedEffect(isRunning, phase) {
        if (!isRunning) return@LaunchedEffect
        while (isActive && isRunning) {
            delay(1000)
            if (phaseRemaining > 0) {
                phaseRemaining -= 1
                totalElapsedSeconds += 1
            } else {
                // Logic for phase switching
                if (phase == TimerPhase.Work) {
                    if (repsDone < totalReps) {
                        phase = TimerPhase.ShortBreak
                        phaseRemaining = shortBreakSeconds
                    } else if (setsDone < totalSets) {
                        phase = TimerPhase.LongBreak
                        phaseRemaining = longBreakSeconds
                    } else {
                        isRunning = false // Workout Complete
                    }
                } else if (phase == TimerPhase.ShortBreak) {
                    repsDone += 1
                    phase = TimerPhase.Work
                    phaseRemaining = workDurationSeconds
                } else if (phase == TimerPhase.LongBreak) {
                    setsDone += 1
                    repsDone = 1
                    phase = TimerPhase.Work
                    phaseRemaining = workDurationSeconds
                }
            }
        }
    }

    WorkTimerCard(
        phase = phase,
        remainingTime = phaseRemaining,
        totalTime = when (phase) {
            TimerPhase.Work -> workDurationSeconds
            TimerPhase.ShortBreak -> shortBreakSeconds
            TimerPhase.LongBreak -> longBreakSeconds
        },
        restDuration = shortBreakSeconds, // Simplified display
        isRunning = isRunning
    )

    TotalElapsedCard(
        totalElapsedSeconds = totalElapsedSeconds,
        totalExpectedSeconds = totalExpectedSeconds
    )

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        TimerStatCard(
            title = "REP",
            current = repsDone,
            total = totalReps,
            accent = RPGCoolBlue,
            modifier = Modifier.weight(1f)
        )
        TimerStatCard(
            title = "SET",
            current = setsDone,
            total = totalSets,
            accent = RPGCoolRed,
            modifier = Modifier.weight(1f)
        )
    }

    PauseCard(
        isRunning = isRunning,
        onToggle = { isRunning = !isRunning }
    )
    
    RPGButton(
        text = "STOP",
        onClick = onStop,
        containerColor = RPGSurfaceDark,
        contentColor = RPGRed
    )
}

@Composable
private fun WorkTimerCard(
    phase: TimerPhase,
    remainingTime: Int,
    totalTime: Int,
    restDuration: Int,
    isRunning: Boolean
) {
    val workColor = RPGCoolGreen
    val phaseColor = when (phase) {
        TimerPhase.Work -> workColor
        TimerPhase.ShortBreak -> RPGYellow
        TimerPhase.LongBreak -> Color(0xFFEF4444)
    }

    // Calculate progress (0.0 to 1.0)
    val targetProgress = if (totalTime > 0) 1f - (remainingTime.toFloat() / totalTime.toFloat()) else 0f
    
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = RPGContainerBlue),
        shape = RoundedCornerShape(22.dp)
    ) {
        // Use a Box to layer the progress bar behind the content
        Box(modifier = Modifier.fillMaxWidth()) {
            // Progress Bar Background (Fills the whole card)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(180.dp) // Match the height of the content
                    .background(
                        Brush.horizontalGradient(
                            0.0f to phaseColor.copy(alpha = 0.8f),
                            animatedProgress to phaseColor.copy(alpha = 0.8f),
                            animatedProgress + 0.001f to RPGSurfaceDark,
                            1.0f to RPGSurfaceDark
                        )
                    )
            )

            // Content
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(0.62f)
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = phase.name.uppercase().replace("_", " "),
                            color = RPGWhite, // Always white for contrast
                            fontFamily = ModernizFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = formatTime(remainingTime),
                            color = RPGWhite, // Always white for contrast
                            fontFamily = ModernizFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 48.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.38f)
                        .height(180.dp)
                )
            }

            Text(
                text = formatTime(restDuration),
                color = Color(0xFFB8BDC9),
                fontFamily = ModernizFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
private fun TotalElapsedCard(totalElapsedSeconds: Int, totalExpectedSeconds: Int) {
    val targetProgress = if (totalExpectedSeconds > 0) totalElapsedSeconds.toFloat() / totalExpectedSeconds.toFloat() else 0f
    
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        0.0f to RPGBlue,
                        animatedProgress to RPGBlue,
                        animatedProgress + 0.001f to RPGDarkBlue,
                        1.0f to RPGDarkBlue
                    )
                )
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(totalElapsedSeconds),
                    color = RPGWhite,
                    fontFamily = ModernizFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp
                )
                Text(
                    text = "TOTAL ELAPSED",
                    color = RPGYellow,
                    fontFamily = OnderFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun TimerStatCard(title: String, current: Int, total: Int, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = RPGContainerBlue),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = RPGCoolBlue,
                fontFamily = OnderFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = current.toString(),
                    color = accent,
                    fontFamily = ModernizFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp
                )
                Text(
                    text = "/$total",
                    color = Color(0xFFD9DDE5),
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(start = 6.dp)
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PauseCard(isRunning: Boolean, onToggle: () -> Unit) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = RPGSurfaceDark),
		shape = RoundedCornerShape(18.dp),
		onClick = onToggle
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 14.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				imageVector = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
				contentDescription = if (isRunning) "Pause" else "Play",
				tint = RPGWhite,
				modifier = Modifier.size(32.dp)
			)
			Text(
				text = if (isRunning) "PAUSE" else "RESUME",
				color = RPGWhite,
				fontFamily = MontserratFontFamily,
				fontWeight = FontWeight.ExtraBold,
				fontSize = 18.sp,
				modifier = Modifier.padding(start = 12.dp)
			)
		}
	}
}

private fun formatTime(totalSeconds: Int): String {
	val safeSeconds = if (totalSeconds < 0) 0 else totalSeconds
	val minutes = safeSeconds / 60
	val seconds = safeSeconds % 60
	return "%02d:%02d".format(minutes, seconds)
}

@Preview(showBackground = true)
@Composable
private fun TimerScreenPreview() {
	GetFitRPGTheme {
		TimerScreen()
	}
}