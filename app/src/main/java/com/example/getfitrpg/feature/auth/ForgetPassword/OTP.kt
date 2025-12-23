package com.example.getfitrpg.feature.auth.ForgetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
// Ensure these imports match your actual package structure
import com.example.getfitrpg.core.designsystem.RPGYellow
import com.example.getfitrpg.core.designsystem.RPGBackgroundDark
import com.example.getfitrpg.core.designsystem.RPGErrorRed
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGGrey
import com.example.getfitrpg.core.designsystem.RPGWhite
import com.example.getfitrpg.feature.auth.AuthManager

@Composable
fun OtpScreen(
    authManager: AuthManager,
    onBackClicked: () -> Unit,
    onVerifySuccess: (String) -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    val otpLength = 4
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundDark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RPGWhite,
                    modifier = Modifier
                        .background(RPGGrey.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.logo_with_text_dark),
                contentDescription = "Get Fit RPG Logo",
                modifier = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.size(48.dp)) // Spacer to balance the Row
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "OTP Verification",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp,
                color = RPGWhite,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter verification code we just sent to your email address.",
            style = MaterialTheme.typography.bodyLarge.copy(color = RPGGrey),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(40.dp))

        OtpTextField(
            value = otpValue,
            onValueChange = { otpValue = it },
            otpLength = otpLength
        )

        message?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = it,
                color = if (it.contains("Success", ignoreCase = true)) RPGGreen else RPGErrorRed,
                fontFamily = MontserratFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                authManager.verifyPasswordResetCode(otpValue, onSuccess = {
                    onVerifySuccess(otpValue)
                }, onFailure = { error ->
                    message = error.message
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RPGYellow),
            shape = RoundedCornerShape(12.dp),
            enabled = otpValue.length == otpLength
        ) {
            Text("Verify", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = { /* Handle resend logic here */ }) {
            Text(
                "Didn't receive code? Resend",
                color = RPGWhite,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    otpLength: Int = 4,
) {
    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length <= otpLength && it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        // Changed to Number so digits are visible, strict Password hides them with dots
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        // Hide the actual BasicTextField text so it doesn't overlap our custom boxes
        textStyle = TextStyle(color = Color.Transparent),
        singleLine = true,
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(otpLength) { index ->
                    val char = value.getOrNull(index)
                    val isFocused = value.length == index

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                if (char != null) Color.White else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = when {
                                    isFocused -> RPGGreen
                                    char != null -> Color.Transparent
                                    else -> RPGGrey
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (char != null) {
                            Text(
                                text = char.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    GetFitRPGTheme {
        // Warning: Passing a real AuthManager here might crash if it needs Context/Dependencies.
        // For UI preview purposes, passing a dummy or mocking is safer if AuthManager is complex.
        OtpScreen(authManager = AuthManager(), onBackClicked = {}, onVerifySuccess = {})
    }
}