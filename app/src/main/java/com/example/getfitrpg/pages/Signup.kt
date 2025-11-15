package com.example.getfitrpg.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.util.PatternsCompat
import androidx.navigation.NavController

@Composable
fun Signup(navController: NavController, modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isEmailValid by remember(email) {
        mutableStateOf(PatternsCompat.EMAIL_ADDRESS.matcher(email).matches())
    }

    val passwordStrength = remember(password) {
        checkPasswordStrength(password)
    }
    val isPasswordStrong = passwordStrength.all { it.value }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            isError = username.isEmpty() && password.isNotEmpty() // Show error if empty and user moves on
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = !isEmailValid && email.isNotEmpty(),
            supportingText = {
                if (!isEmailValid && email.isNotEmpty()) {
                    Text("Enter a valid email address")
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = !isPasswordStrong && password.isNotEmpty(),
            supportingText = {
                if (!isPasswordStrong && password.isNotEmpty()) {
                    PasswordStrengthIndicator(passwordStrength)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("login") },
            enabled = username.isNotEmpty() && isEmailValid && isPasswordStrong && email.isNotEmpty()
        ) {
            Text("Sign Up")
        }
    }
}

private fun checkPasswordStrength(password: String): Map<String, Boolean> {
    return mapOf(
        "8+ characters" to (password.length >= 8),
        "one uppercase" to password.any { it.isUpperCase() },
        "one lowercase" to password.any { it.isLowerCase() },
        "one digit" to password.any { it.isDigit() },
        "one special character" to password.any { !it.isLetterOrDigit() }
    )
}

@Composable
private fun PasswordStrengthIndicator(strength: Map<String, Boolean>) {
    val unfulfilledRequirements = strength.filter { !it.value }.keys
    if (unfulfilledRequirements.isNotEmpty()) {
        Column {
            Text("Please make strong password. Requirements:")
            unfulfilledRequirements.forEach { requirement ->
                Text("- $requirement")
            }
        }
    }
}
