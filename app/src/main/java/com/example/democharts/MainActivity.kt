package com.example.democharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.democharts.ui.ChartsScreen
import com.example.democharts.ui.theme.DemoChartsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoChartsTheme {
                ChartsScreen()
            }
        }
    }
}
