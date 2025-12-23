package com.example.getfitrpg.feature.auth.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.RPGBackgroundDark
import com.example.getfitrpg.core.designsystem.RPGErrorRed
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGGrey
import com.example.getfitrpg.core.designsystem.RPGPurple
import com.example.getfitrpg.core.designsystem.RPGWhite
import com.example.getfitrpg.core.designsystem.components.RPGButton
import com.example.getfitrpg.core.designsystem.components.RPGTextField
import com.example.getfitrpg.feature.auth.AuthManager

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    authManager: AuthManager
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var loginMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        RPGPurple,
                        RPGBackgroundDark
                    )
                )
            )
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_with_text_dark),
            contentDescription = "Get Fit RPG Logo",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "LOGIN",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 28.sp),
            color = RPGWhite
        )

        Spacer(modifier = Modifier.height(30.dp))

        RPGTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            placeholder = "Email",
            isError = !isEmailValid && email.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        RPGTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Icon(Icons.Default.Fingerprint, contentDescription = "Fingerprint icon", tint = RPGGrey)
            }
        )

        loginMessage?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = it,
                color = if (it.contains("Success")) RPGGreen else RPGErrorRed,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }
        
        TextButton(onClick = onNavigateToForgotPassword, modifier = Modifier.fillMaxWidth()) {
            Text("Forgot Password?", color = RPGWhite, textAlign = TextAlign.End, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(10.dp))

        RPGButton(
            text = "Login",
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    loginMessage = "Email and Password are required."
                } else {
                    authManager.signIn(email, password, onSuccess = {
                        loginMessage = "Login Success!"
                        onLoginSuccess()
                    }, onFailure = {
                        loginMessage = it.message
                    })
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "OR",
            color = RPGWhite,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        RPGButton(
            text = "Login with Google",
            onClick = { /* Handle Google login */ },
            containerColor = RPGWhite,
            contentColor = RPGBackgroundDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToSignup) {
            Text("Don't Have an Account? Register", color = RPGWhite, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    GetFitRPGTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToSignup = {}, onNavigateToForgotPassword = {}, authManager = AuthManager())
    }
}
