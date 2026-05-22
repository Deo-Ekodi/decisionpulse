package com.decisionpulse.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.decisionpulse.demo.ui.AppShell
import com.decisionpulse.demo.ui.theme.DecisionPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DecisionPulseTheme {
                AppShell()
            }
        }
    }
}