package com.example.studygroupfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studygroupfinder.ui.navigation.AppNavigation
import com.example.studygroupfinder.ui.theme.STUDYGROUPFINDERTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            STUDYGROUPFINDERTheme {
                AppNavigation()
            }
        }
    }
}
