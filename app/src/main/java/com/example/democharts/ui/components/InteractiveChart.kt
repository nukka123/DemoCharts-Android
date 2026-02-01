package com.example.democharts.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.democharts.echartsview.BarSeries
import com.example.democharts.echartsview.EChartsTheme
import com.example.democharts.echartsview.EChartsView
import com.example.democharts.echartsview.XYChartOption
import com.example.democharts.echartsview.rememberEChartsController
import com.example.democharts.ui.theme.DemoChartsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveChart(modifier: Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val controller = rememberEChartsController()

    val dataCount = 50

    var selectedItemLabel by remember { mutableStateOf("No item selected") }

    val (xAxisData, seriesData) = remember(dataCount) {
        val xData = (1..dataCount).map { "Category $it" }
        val yData = (1..dataCount).map { (10..100).random().toDouble() }
        xData to yData
    }

    var sliderRange by remember { mutableStateOf(0f..100f) }

    // State for theme selection
    var selectedTheme by remember { mutableStateOf<EChartsTheme?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val themes = listOf(null) + EChartsTheme.entries

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png"),
        onResult = { uri: Uri? ->
            uri?.let {
                coroutineScope.launch {
                    val imageBytes = controller.captureChartImage()
                    if (imageBytes != null) {
                        saveByteArrayToUri(context, it, imageBytes)
                    }
                }
            }
        }
    )

    Column {
        // Dropdown for theme selection
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedTheme?.key ?: "Default",
                onValueChange = {},
                label = { Text("Chart Theme") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                themes.forEach { theme ->
                    DropdownMenuItem(
                        text = { Text(theme?.key ?: "Default") },
                        onClick = {
                            selectedTheme = theme
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = selectedItemLabel,
            onValueChange = {},
            enabled = false,
            label = { Text("Selected Item") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        EChartsView(
            option = createLongDataOption(xAxisData, seriesData, 0f, 100f),
            controller = controller,
            modifier = modifier,
            theme = selectedTheme, // Pass the selected theme
            onDataClick = {
                selectedItemLabel = it
            }
        )

        RangeSlider(
            value = sliderRange,
            onValueChange = {
                sliderRange = it
                controller.zoom(start = it.start, end = it.endInclusive)
            },
            valueRange = 0f..100f
        )

        Button(
            onClick = {
                launcher.launch("chart.png")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Chart as Image")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InteractiveChartPreview() {
    DemoChartsTheme {
        InteractiveChart(modifier = Modifier.height(400.dp))
    }
}

private fun createLongDataOption(
    xAxisData: List<String>,
    seriesData: List<Double>,
    startPercentage: Float,
    endPercentage: Float
): XYChartOption {
    return XYChartOption(
        xAxisData = xAxisData,
        series = listOf(BarSeries(data = seriesData)),
        dataZoom = listOf(
            mapOf("type" to "inside", "start" to startPercentage, "end" to endPercentage)
        )
    )
}

private fun saveByteArrayToUri(context: Context, uri: Uri, data: ByteArray) {
    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(data)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
