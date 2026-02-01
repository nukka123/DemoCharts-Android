# DemoCharts

This is a demo application that demonstrates how to display [Apache ECharts](https://echarts.apache.org/en/index.html) in an Android WebView.
This demo is based on Apache ECharts v6.0 and incorporates files obtained from [this link](https://github.com/apache/echarts/tree/6.0.0/dist).

## Overview

This demo app introduces solutions for the following challenges when embedding interactive charts in an Android application.

*   **Basic ECharts Display Settings**: Shows how to display ECharts within an Android app using a WebView.
*   **Integration with Compose UI**: Demonstrates how to integrate WebView with Jetpack Compose's modern UI toolkit to build a seamless user interface.
*   **Android Dark Theme Support**: Shows how to automatically switch the chart's theme to follow the device's theme setting (light/dark).

## Development Environment

*   Android Studio Otter 3 Feature Drop | 2025.2.3
*   Kotlin 2.3.0
*   Compose BOM 2024.06.00
*   Android API 36
*   Min Android API 33
*   Gradle 9.1.0

## Key Implementations

### Basic ECharts Display Settings

The `EChartsView` composable within the `echarts-view` module is responsible for displaying ECharts. It internally uses a WebView to load `echarts.html` from the `assets` directory.

Chart options are passed as a `ChartOption` object and set in ECharts via the `setChartOption` JavaScript function.

```kotlin
// EChartsView.kt
@Composable
fun EChartsView(
    option: ChartOption,
    modifier: Modifier = Modifier,
    // ...
) {
    // ...
    AndroidView(
        factory = {
            WebView(it).apply {
                // ...
                loadUrl("file:///android_asset/echarts.html$themeQuery")
            }
        },
        update = {
            // ...
            it.evaluateJavascript("setChartOption($jsonOption)", null)
        }
    )
}
```

```html
<!-- echarts.html -->
<script type="text/javascript">
    // ...
    function setChartOption(option) {
        option.darkMode = 'auto';
        myChart.setOption(option);
    }
</script>
```

### Integration with Compose UI

`EChartsView` is defined as a regular composable function, so it can be placed in layouts just like any other composable. Since it supports the `modifier` parameter, you can flexibly customize its size, padding, alignment, and more using Compose's powerful modifier system.

```kotlin
// ChartComponents.kt
@Composable
fun BarChart(modifier: Modifier) {
    val option = XYChartOption(
        xAxisData = listOf("Jan", "Feb", "Mar", "Apr", "May"),
        series = listOf(BarSeries(data = listOf(20.0, 30.0, 40.0, 50.0, 60.0)))
    )
    EChartsView(option = option, modifier = modifier)
}
```

### Android Dark Theme Support

By using the `darkMode: 'auto'` option in ECharts, the chart automatically adapts to the device's theme settings. This option is set in the `setChartOption` function within `echarts.html`.

```html
<!-- echarts.html -->
<script type="text/javascript">
    // ...
    function setChartOption(option) {
        option.darkMode = 'auto'; // Automatically follows the system theme
        myChart.setOption(option);
    }
</script>
```

Additionally, you can apply a specific theme (e.g., `infographic`, `macarons`) by passing an `EChartsTheme` to the `theme` parameter of the `EChartsView`.

## Build and Run

1.  Clone this repository.
2.  Open the project in Android Studio.
3.  Run the app on an emulator or a physical device.
