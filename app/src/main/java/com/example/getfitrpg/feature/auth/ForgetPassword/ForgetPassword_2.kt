package com.example.getfitrpg.feature.auth.ForgetPassword

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.AccentYellow
import com.example.getfitrpg.core.designsystem.BackgroundDark
import com.example.getfitrpg.core.designsystem.ErrorRed
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.core.designsystem.TextGrey
import com.example.getfitrpg.core.designsystem.TextWhite
import com.example.getfitrpg.feature.auth.AuthManager

@Composable
fun ForgetPassword2Screen(authManager: AuthManager, code: String, onBackClicked: () -> Unit, onResetPasswordClicked: () -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    // Password must be at least 8 characters, contain a letter and a number.
    val isNewPasswordStrong = newPassword.length >= 8 && newPassword.any { it.isDigit() } && newPassword.any { it.isLetter() }
    val passwordsMatch = newPassword == confirmPassword

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            IconButton(onClick = onBackClicked, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite,
                    modifier = Modifier
                        .background(TextGrey.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.logo_with_text_dark),
                contentDescription = "Get Fit RPG Logo",
                modifier = Modifier
                    .height(40.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Create New Password",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your new password must be unique from those previously used.",
            style = MaterialTheme.typography.bodyLarge.copy(color = TextGrey, fontFamily = MontserratFontFamily),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholder = { Text("New Password", fontFamily = MontserratFontFamily, color = TextGrey) },
            textStyle = TextStyle(fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.Black),
            modifier = Modifier.fillMaxWidth(),
            isError = !isNewPasswordStrong && newPassword.isNotEmpty(),
            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (newPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (newPasswordVisible) "Hide password" else "Show password", tint = TextGrey)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = ErrorRed,
                errorContainerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        if (!isNewPasswordStrong && newPassword.isNotEmpty()) {
            Text(
                text = "Password must be 8+ characters and contain a number.",
                color = ErrorRed,
                fontFamily = MontserratFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirm Password", fontFamily = MontserratFontFamily, color = TextGrey) },
            textStyle = TextStyle(fontFamily = MontserratFontFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.Black),
            modifier = Modifier.fillMaxWidth(),
            isError = !passwordsMatch && confirmPassword.isNotEmpty(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password", tint = TextGrey)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = ErrorRed,
                errorContainerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        if (!passwordsMatch && confirmPassword.isNotEmpty()) {
            Text(
                text = "Passwords do not match.",
                color = ErrorRed,
                fontFamily = MontserratFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        message?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = it,
                color = if (it.contains("Success")) PrimaryGreen else ErrorRed,
                fontFamily = MontserratFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                authManager.confirmPasswordReset(code, newPassword, onSuccess = {
                    message = "Password reset successfully."
                    onResetPasswordClicked()
                }, onFailure = {
                    message = it.message
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentYellow),
            shape = RoundedCornerShape(12.dp),
            enabled = passwordsMatch && isNewPasswordStrong && newPassword.isNotEmpty()
        ) {
            Text("Reset Password", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPassword2ScreenPreview() {
    GetFitRPGTheme {
        ForgetPassword2Screen(authManager = AuthManager(), code = "", onBackClicked = {}, onResetPasswordClicked = {})
    }
}
