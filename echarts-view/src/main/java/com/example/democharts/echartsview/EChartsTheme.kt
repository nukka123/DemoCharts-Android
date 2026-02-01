package com.example.democharts.echartsview

/**
 * Defines the themes that can be applied to an ECharts view.
 *
 * When no theme is specified, the default theme is applied.
 *
 * The chart's appearance is always responsive to the device's dark mode setting,
 * as `darkMode: 'auto'` is consistently enabled. This behavior applies across all themes,
 * including the default.
 *
 * With the exception of `Dark` (a built-in theme), each enum case corresponds to a
 * JavaScript theme file in the `src/main/assets/theme` directory. These files are loaded
 * in `echarts.html` to make the themes available.
 *
 * @see <a href="https://echarts.apache.org/handbook/en/concepts/style/">ECharts Style Customization</a>
 * @see <a href="https://echarts.apache.org/en/download-theme.html">ECharts Download Theme</a>
 * @see <a href="https://echarts.apache.org/en/option.html#darkMode">ECharts darkMode</a>
 */
enum class EChartsTheme(val key: String) {
    /** Built-in theme. */
    Dark("dark"),

    /** Custom theme, corresponds to `assets/theme/roma.js`. */
    Roma("roma"),

    /** Custom theme, corresponds to `assets/theme/shine.js`. */
    Shine("shine"),

    /** Custom theme, corresponds to `assets/theme/vintage.js`. */
    Vintage("vintage"),

    /** Custom theme, corresponds to `assets/theme/macarons.js`. */
    Macarons("macarons"),

    /** Custom theme, corresponds to `assets/theme/infographic.js`. */
    Infographic("infographic")
}
