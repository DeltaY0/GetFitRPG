package com.example.getfitrpg.feature.auth.ForgetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getfitrpg.R
import com.example.getfitrpg.core.designsystem.BackgroundDark
import com.example.getfitrpg.core.designsystem.GetFitRPGTheme
import com.example.getfitrpg.core.designsystem.MontserratFontFamily
import com.example.getfitrpg.core.designsystem.PrimaryGreen
import com.example.getfitrpg.core.designsystem.TextGrey
import com.example.getfitrpg.core.designsystem.TextWhite

@Composable
fun ForgetPassword3Screen(onBackToLoginClicked: () -> Unit) {
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
            IconButton(onClick = onBackToLoginClicked, modifier = Modifier.align(Alignment.CenterStart)) {
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

        Spacer(modifier = Modifier.weight(0.5f))

        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Success Checkmark",
            tint = PrimaryGreen,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Password Changed!",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your password has been changed successfully.",
            style = MaterialTheme.typography.bodyLarge.copy(color = TextGrey, fontFamily = MontserratFontFamily),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onBackToLoginClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(width = 2.dp, color = PrimaryGreen, shape = RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Back to Login", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = MontserratFontFamily)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun ForgetPassword3ScreenPreview() {
    GetFitRPGTheme {
        ForgetPassword3Screen(onBackToLoginClicked = {})
    }
}
