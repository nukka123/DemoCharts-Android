package com.example.democharts.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.democharts.ui.components.ChartCard
import com.example.democharts.ui.components.charts
import com.example.democharts.ui.theme.DemoChartsTheme

@Composable
internal fun LandscapeLayout(paddingValues: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(charts) { chart ->
            ChartCard(title = chart.title) {
                chart.content()
            }
        }
    }
}

@Preview(showBackground = true, name = "Landscape", device = "spec:width=891dp,height=411dp")
@Composable
internal fun LandscapeLayoutPreview() {
    DemoChartsTheme {
        LandscapeLayout(paddingValues = PaddingValues())
    }
}
