package com.example.getfitrpg.feature.auth.signup

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun SignupScreen(authManager: AuthManager, onLoginClicked: () -> Unit, onRegisterClicked: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var registrationMessage by remember { mutableStateOf<String?>(null) }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordStrong = password.length >= 8 && password.any { it.isDigit() } && password.any { it.isLetter() }
    val passwordsMatch = password == confirmPassword

    val isFormValid = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && isEmailValid && isPasswordStrong && passwordsMatch

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
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_with_text_dark),
            contentDescription = "Get Fit RPG Logo",
            modifier = Modifier.padding(vertical = 40.dp).height(120.dp)
        )

        Text(
            text = "SIGNUP",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 28.sp),
            color = RPGWhite
        )

        Spacer(modifier = Modifier.height(30.dp))

        RPGTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = "Username"
        )

        Spacer(modifier = Modifier.height(20.dp))

        RPGTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            isError = !isEmailValid && email.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (!isEmailValid && email.isNotEmpty()) {
            Text(
                text = "Please enter a valid email address.",
                color = RPGErrorRed,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        RPGTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isError = !isPasswordStrong && password.isNotEmpty(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password", tint = RPGGrey)
                }
            }
        )
        if (!isPasswordStrong && password.isNotEmpty()) {
            Text(
                text = "Password must be 8+ characters and include a number.",
                color = RPGErrorRed,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        RPGTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirm Password",
            isError = !passwordsMatch && confirmPassword.isNotEmpty(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password", tint = RPGGrey)
                }
            }
        )
        if (!passwordsMatch && confirmPassword.isNotEmpty()) {
            Text(
                text = "Passwords do not match.",
                color = RPGErrorRed,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        registrationMessage?.let {
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

        Spacer(modifier = Modifier.height(30.dp))

        RPGButton(
            text = "Register",
            onClick = {
                if (isFormValid) {
                    authManager.createUser(username, email, password, onSuccess = {
                        registrationMessage = "Registration Success! Verification email sent."
                        onRegisterClicked()
                    }, onFailure = {
                        registrationMessage = it.message
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
            text = "Signup with Google",
            onClick = { /* Handle Google signup */ },
            containerColor = RPGWhite,
            contentColor = RPGBackgroundDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onLoginClicked) {
            Text("Already have an Account? Login", color = RPGGreen, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    GetFitRPGTheme {
        SignupScreen(authManager = AuthManager(), onLoginClicked = {}, onRegisterClicked = {})
    }
}
