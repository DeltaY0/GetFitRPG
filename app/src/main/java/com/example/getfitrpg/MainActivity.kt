package com.example.getfitrpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.getfitrpg.ui.theme.GetFitRPGTheme
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import com.example.getfitrpg.pages.Splash

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetFitRPGTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Splash( Modifier.padding(innerPadding))
                }
            }
        }
    }
}

