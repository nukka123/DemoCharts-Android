package com.example.democharts.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.democharts.ui.components.ChartCard
import com.example.democharts.ui.components.charts
import com.example.democharts.ui.theme.DemoChartsTheme

@Composable
internal fun PortraitLayout(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        charts.forEach { chart ->
            ChartCard(title = chart.title) {
                chart.content()
            }
        }
    }
}

@Preview(showBackground = true, name = "Portrait")
@Composable
internal fun PortraitLayoutPreview() {
    DemoChartsTheme {
        PortraitLayout(paddingValues = PaddingValues())
    }
}
