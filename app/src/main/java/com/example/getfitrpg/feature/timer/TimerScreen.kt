package com.example.getfitrpg.feature.timer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.RPGBackgroundBlue
import com.example.getfitrpg.core.designsystem.RPGCoolPurple
import com.example.getfitrpg.core.designsystem.RPGDarkBlue
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGWhite

private enum class TimerPhase { Work, Rest }

@Composable
fun TimerScreen(
	playerName: String = "PLAYER NAME",
	className: String = "CLASS NAME",
	level: Int = 27,
	levelProgress: Float = 0.68f,
	workDurationSeconds: Int = 211, // 03:31
	restDurationSeconds: Int = 720, // 12:00
	totalReps: Int = 3,
	totalSets: Int = 2
) {
	var phase by rememberSaveable { mutableStateOf(TimerPhase.Work) }
	var phaseRemaining by rememberSaveable { mutableStateOf(workDurationSeconds) }
	var totalElapsedSeconds by rememberSaveable { mutableStateOf(15 * 60 + 31) }
	var repsDone by rememberSaveable { mutableStateOf(2) }
	var setsDone by rememberSaveable { mutableStateOf(2) }
	var isRunning by rememberSaveable { mutableStateOf(true) }

	LaunchedEffect(isRunning, phase, workDurationSeconds, restDurationSeconds) {
		if (!isRunning) return@LaunchedEffect
		val workDuration = workDurationSeconds
		val restDuration = restDurationSeconds
		while (isActive && isRunning) {
			delay(1000)
			if (phaseRemaining > 0) {
				phaseRemaining -= 1
				totalElapsedSeconds += 1
			} else {
				if (phase == TimerPhase.Work) {
					if (repsDone < totalReps) {
						repsDone += 1
					} else if (setsDone < totalSets) {
						setsDone += 1
						repsDone = 1
					}
				}

				phase = if (phase == TimerPhase.Work) TimerPhase.Rest else TimerPhase.Work
				phaseRemaining = if (phase == TimerPhase.Work) workDuration else restDuration
			}
		}
	}

	Surface(color = RPGBackgroundBlue) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.padding(20.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			TimerHeader(
				playerName = playerName,
				className = className,
				level = level,
				levelProgress = levelProgress
			)

			WorkTimerCard(
				phase = phase,
				remainingTime = phaseRemaining,
				restDuration = restDurationSeconds
			)

			TotalElapsedCard(totalElapsedSeconds = totalElapsedSeconds)

			Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
				TimerStatCard(
					title = "REP",
					current = repsDone,
					total = totalReps,
					accent = Color(0xFF54D7FF)
				)
				TimerStatCard(
					title = "SET",
					current = setsDone,
					total = totalSets,
					accent = RPGCoolPurple
				)
			}

			PauseCard(
				isRunning = isRunning,
				onToggle = { isRunning = !isRunning }
			)
		}
	}
}

@Composable
private fun TimerHeader(
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
						.background(RPGGreen, RoundedCornerShape(4.dp)),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = "R",
						color = Color(0xFF0B1220),
						fontWeight = FontWeight.Black,
						fontSize = 18.sp,
						fontFamily = MontserratFontFamily
					)
				}

				Spacer(modifier = Modifier.width(10.dp))

				Column(modifier = Modifier.weight(1f)) {
					Text(
						text = playerName,
						color = Color(0xFF94A3B8),
						fontFamily = MontserratFontFamily,
						fontWeight = FontWeight.SemiBold,
						fontSize = 12.sp
					)
					Text(
						text = className,
						color = RPGWhite,
						fontFamily = MontserratFontFamily,
						fontWeight = FontWeight.Black,
						fontSize = 18.sp
					)
				}

				Text(
					text = "LVL %03d".format(level),
					color = Color(0xFFCDE8FF),
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

@Composable
private fun WorkTimerCard(
	phase: TimerPhase,
	remainingTime: Int,
	restDuration: Int
) {
	val workColor = Color(0xFF21D9B5)
	val restColor = Color(0xFF222B3B)
	val phaseColor = if (phase == TimerPhase.Work) workColor else Color(0xFF7F6BFF)

	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1626)),
		shape = RoundedCornerShape(22.dp)
	) {
		Box(modifier = Modifier.fillMaxWidth()) {
			Row(modifier = Modifier.fillMaxWidth()) {
				Box(
					modifier = Modifier
						.weight(0.62f)
						.height(180.dp)
						.background(phaseColor, RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp)),
					contentAlignment = Alignment.Center
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						Text(
							text = phase.name.uppercase(),
							color = Color(0xFF4A3E74),
							fontFamily = MontserratFontFamily,
							fontWeight = FontWeight.ExtraBold,
							fontSize = 26.sp
						)
						Text(
							text = formatTime(remainingTime),
							color = if (phase == TimerPhase.Work) Color(0xFF03131C) else RPGWhite,
							fontFamily = MontserratFontFamily,
							fontWeight = FontWeight.Black,
							fontSize = 64.sp
						)
					}
				}

				Box(
					modifier = Modifier
						.weight(0.38f)
						.height(180.dp)
						.background(restColor, RoundedCornerShape(topEnd = 22.dp, bottomEnd = 22.dp))
				)
			}

			Text(
				text = if (phase == TimerPhase.Rest) formatTime(remainingTime) else formatTime(restDuration),
				color = Color(0xFFB8BDC9),
				fontFamily = MontserratFontFamily,
				fontWeight = FontWeight.Bold,
				fontSize = 20.sp,
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(20.dp)
			)
		}
	}
}

@Composable
private fun TotalElapsedCard(totalElapsedSeconds: Int) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = Color.Transparent),
		shape = RoundedCornerShape(20.dp)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					brush = Brush.horizontalGradient(
						listOf(Color(0xFF2F73F2), Color(0xFF1C2461))
					)
				)
				.padding(vertical = 18.dp),
			contentAlignment = Alignment.Center
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(
					text = formatTime(totalElapsedSeconds),
					color = RPGWhite,
					fontFamily = MontserratFontFamily,
					fontWeight = FontWeight.Black,
					fontSize = 36.sp
				)
				Text(
					text = "TOTAL ELAPSED",
					color = Color(0xFFFBD34E),
					fontFamily = MontserratFontFamily,
					fontWeight = FontWeight.ExtraBold,
					fontSize = 18.sp
				)
			}
		}
	}
}

@Composable
private fun TimerStatCard(title: String, current: Int, total: Int, accent: Color) {
	Card(
		modifier = Modifier.weight(1f),
		colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1626)),
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
				color = Color(0xFFA4AAB7),
				fontFamily = MontserratFontFamily,
				fontWeight = FontWeight.ExtraBold,
				fontSize = 18.sp
			)
			Spacer(modifier = Modifier.height(6.dp))
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = current.toString(),
					color = accent,
					fontFamily = MontserratFontFamily,
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
		colors = CardDefaults.cardColors(containerColor = Color(0xFF101C2C)),
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