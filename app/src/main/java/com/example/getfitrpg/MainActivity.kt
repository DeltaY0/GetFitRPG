package com.example.getfitrpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.getfitrpg.core.designsystem.GetFitRPGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GetFitRPGTheme {
                MainScreen()
            }
        }
    }
}