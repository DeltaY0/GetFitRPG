package com.example.getfitrpg.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.core.designsystem.RPGErrorRed
import com.example.getfitrpg.core.designsystem.RPGGreen
import com.example.getfitrpg.core.designsystem.RPGGrey
import com.example.getfitrpg.core.designsystem.RPGSurfaceDark
import com.example.getfitrpg.core.designsystem.RPGWhite

@Composable
fun RPGTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = RPGGrey, style = MaterialTheme.typography.labelMedium.copy(fontSize = 16.sp)) },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = RPGWhite),
        modifier = modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = RPGSurfaceDark,
            unfocusedContainerColor = RPGSurfaceDark,
            disabledContainerColor = RPGSurfaceDark,
            focusedTextColor = RPGWhite,
            unfocusedTextColor = RPGWhite,
            cursorColor = RPGGreen,
            focusedBorderColor = RPGGreen,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = RPGErrorRed,
            errorContainerColor = RPGSurfaceDark,
            errorTextColor = RPGWhite
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}