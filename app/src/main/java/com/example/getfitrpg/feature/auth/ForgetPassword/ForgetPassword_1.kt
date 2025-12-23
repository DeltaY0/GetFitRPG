package com.example.getfitrpg.feature.auth.ForgetPassword

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun ForgetPassword1Screen(
    authManager: AuthManager,
    onBackClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onNavigateToOtp: () -> Unit // 1. Added callback for navigation
) {
    // TODO: redo the forget password screens
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    // A simple regex check for UI feedback
    val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGBackgroundDark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // FIXED HEADER: Used Box for perfect centering of the logo
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
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
                modifier = Modifier.height(45.dp)
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Forgot Password?",
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
            text = "Enter email address linked with your account.",
            style = MaterialTheme.typography.bodyLarge.copy(color = RPGGrey),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    color = RPGGrey,
                    text = "Email",
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Medium
                )
            },
            textStyle = TextStyle(
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = !isEmailValid && email.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedTrailingIconColor = RPGGrey,
                unfocusedTrailingIconColor = RPGGrey,
                unfocusedPlaceholderColor = RPGGrey,
                focusedPlaceholderColor = RPGGrey,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = RPGErrorRed,
                errorContainerColor = Color.White,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
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

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                // 2. Trigger API call, then navigate on success
                authManager.sendPasswordResetEmail(email, onSuccess = {
                    message = "Code sent successfully" // Optional feedback before navigation
                    onNavigateToOtp()
                }, onFailure = { error ->
                    message = error.message
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RPGYellow),
            shape = RoundedCornerShape(12.dp),
            enabled = isEmailValid
        ) {
            Text("Send Code", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onLoginClicked) {
            Text(
                "Remember Password? Login",
                color = RPGWhite,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPassword1ScreenPreview() {
    GetFitRPGTheme {
        ForgetPassword1Screen(
            authManager = AuthManager(),
            onBackClicked = {},
            onLoginClicked = {},
            onNavigateToOtp = {}
        )
    }
}