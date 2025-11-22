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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.BackgroundDark
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.core.designsystem.TextGrey
import com.example.getfitrpg.core.designsystem.TextWhite

@Composable
fun OtpScreen(onBackClicked: () -> Unit, onVerifySuccess: () -> Unit) {
    var otpValue by remember { mutableStateOf("") }
    val otpLength = 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
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
                    tint = TextWhite,
                    modifier = Modifier.background(TextGrey.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(4.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.logo_with_text_dark),
                contentDescription = "Get Fit RPG Logo",
                modifier = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "OTP Verification",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            ),
             modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter verification code we just sent to your email address.",
            style = MaterialTheme.typography.bodyLarge.copy(color = TextGrey),
            textAlign = TextAlign.Start,
             modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(40.dp))

        OtpTextField(
            value = otpValue,
            onValueChange = { otpValue = it },
            otpLength = otpLength
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onVerifySuccess, // Fixed: Connect to the navigation action
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(12.dp),
            enabled = otpValue.length == otpLength
        ) {
            Text("Verify", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = { /* Handle resend */ }) {
            Text(
                "Didn't receive code? Resend",
                color = TextWhite,
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(otpLength) { index ->
                    val char = value.getOrNull(index)
                    val isFocused = value.length == index

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                if (char != null) Color.White else Color.Transparent, // Fixed: Transparent background when empty
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = when {
                                    isFocused -> PrimaryGreen
                                    char != null -> Color.Transparent // Fixed: No border when filled
                                    else -> TextGrey
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
        // Fixed: Add the new parameter to the preview
        OtpScreen(onBackClicked = {}, onVerifySuccess = {})
    }
}
