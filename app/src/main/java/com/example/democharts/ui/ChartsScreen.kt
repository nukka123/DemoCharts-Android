package com.example.democharts.ui

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.democharts.ui.layouts.LandscapeLayout
import com.example.democharts.ui.layouts.PortraitLayout
import com.example.democharts.ui.theme.DemoChartsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Demo Charts") },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            LandscapeLayout(innerPadding)
        } else {
            PortraitLayout(innerPadding)
        }
    }
}

@Preview(showBackground = true, name = "Portrait")
@Composable
fun ChartsScreenPreviewPortrait() {
    DemoChartsTheme {
        ChartsScreen()
    }
}

@Preview(showBackground = true, name = "Landscape", device = "spec:width=891dp,height=411dp")
@Composable
fun ChartsScreenPreviewLandscape() {
    DemoChartsTheme {
        ChartsScreen()
    }
}
