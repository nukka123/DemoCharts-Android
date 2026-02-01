package com.example.democharts.echartsview

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Base64
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import androidx.compose.ui.graphics.Color as ComposeColor

/**
 * A Composable function that displays an ECharts chart.
 *
 * This composable wraps a WebView that renders the chart using the ECharts JavaScript library.
 * The chart's appearance is always responsive to the device's dark mode setting, as `darkMode: 'auto'` is consistently applied.
 *
 * For more information on ECharts options, see the
 * [Apache ECharts documentation](https://echarts.apache.org/en/option.html).
 *
 * @param option The chart configuration object. See [ChartOption].
 * @param modifier The modifier to be applied to the chart.
 * @param theme The ECharts theme to apply. If `null`, the default (light) theme is used.
 * @param controller An [EChartsController] to interact with the chart. Use [rememberEChartsController] to create one.
 * @param onDataClick A callback that is invoked when a data item is clicked on the chart. The name of the clicked item is passed as an argument.
 */
@Composable
fun EChartsView(
    option: ChartOption,
    modifier: Modifier = Modifier,
    theme: EChartsTheme? = null,
    controller: EChartsController = rememberEChartsController(),
    onDataClick: (String) -> Unit = {}
) {
    Surface(modifier = modifier) {
        if (LocalInspectionMode.current) {
            EChartsPreviewPlaceholder(modifier = Modifier.fillMaxSize())
        } else {
            EChartsWebViewInternal(
                option = option,
                theme = theme,
                modifier = Modifier.fillMaxSize(),
                controller = controller,
                onDataClick = onDataClick
            )
        }
    }
}

/**
 * A placeholder composable for [EChartsView] in preview mode.
 *
 * This is displayed in Android Studio's preview because WebView is not supported in that context.
 * It shows a simple gray box with the text "ECharts View".
 */
@Composable
private fun EChartsPreviewPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(ComposeColor.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "ECharts View")
    }
}

/**
 * A controller for interacting with an [EChartsView].
 *
 * An instance of this controller can be created using [rememberEChartsController].
 * It allows for imperative control over the chart, such as zooming or capturing an image.
 *
 * For more information on ECharts actions and options, see the
 * [Apache ECharts documentation](https://echarts.apache.org/en/api.html).
 *
 * @see rememberEChartsController
 */
class EChartsController internal constructor() {
    private var webView: WebView? = null

    internal fun attach(webView: WebView) {
        this.webView = webView
    }

    /**
     * Zooms the chart to a specified range.
     *
     * This function dispatches a 'dataZoom' action to the ECharts instance.
     *
     * @param start The starting percentage of the zoom, from 0 to 100.
     * @param end The ending percentage of the zoom, from 0 to 100.
     * @see [ECharts dataZoom action](https://echarts.apache.org/en/api.html#action.dataZoom)
     */
    fun zoom(start: Float, end: Float) {
        // EChartsの `dispatchAction` を使って dataZoom アクションを発行する
        val script = "myChart.dispatchAction({ type: 'dataZoom', start: $start, end: $end })"
        webView?.evaluateJavascript(script, null)
    }

    /**
     * Captures the current chart as an image and returns its data as a byte array.
     *
     * This function internally calls the ECharts `getDataURL` JavaScript function.
     * The `getDataURL` function returns a string in the Data URI scheme format
     * (e.g., "data:image/png;base64,iVBORw0KGgo...").
     * This method processes the Data URI, extracts the Base64-encoded data, decodes it,
     * and returns the raw image data as a [ByteArray].
     *
     * @param type The desired image format, such as "png" (default) or "jpeg".
     * @return A [ByteArray] containing the image data, or null if the operation fails.
     * @see [ECharts getDataURL function](https://echarts.apache.org/en/api.html#echartsInstance.getDataURL)
     */
    suspend fun captureChartImage(type: String = "png"): ByteArray? {
        val script = "myChart.getDataURL({type: '$type'})"
        return suspendCancellableCoroutine { continuation ->
            webView?.evaluateJavascript(script) { result ->
                val dataUrl = result?.removeSurrounding("\"")
                val base64String = dataUrl?.substringAfter(',')
                val bytes = base64String?.let { Base64.decode(it, Base64.DEFAULT) }
                continuation.resume(bytes)
            }
        }
    }
}

/**
 * Creates and remembers an [EChartsController].
 *
 * The controller will be remembered across recompositions.
 *
 * @return A new or remembered [EChartsController] instance.
 */
@Composable
fun rememberEChartsController(): EChartsController {
    return remember { EChartsController() }
}

/**
 * An internal composable that hosts the WebView for rendering ECharts.
 *
 * This function sets up the WebView, loads the ECharts HTML file, and communicates with the
 * JavaScript code to render and update the chart.
 *
 * @param option The chart configuration.
 * @param theme The ECharts theme to use.
 * @param modifier The modifier for this composable.
 * @param controller The [EChartsController] to attach to the WebView.
 * @param onDataClick Callback for data click events.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun EChartsWebViewInternal(
    option: ChartOption,
    theme: EChartsTheme?,
    modifier: Modifier,
    controller: EChartsController,
    onDataClick: (String) -> Unit
) {
    val themeQuery = theme?.let { "?theme=${it.key}" } ?: ""
    val jsonOption = option.toJsonString()

    AndroidView(
        modifier = modifier.testTag("EChartsWebView"),
        factory = {
            WebView(it).apply {
                // WebViewを親レイアウトいっぱいに広げる
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // WebViewの背景を透明にし、Compose側の背景色が見えるようにする
                setBackgroundColor(Color.TRANSPARENT)
                // EChartsライブラリの動作に必須なJavaScriptを有効化する
                settings.javaScriptEnabled = true
                // DOMストレージを有効化し、EChartsがキャッシュなどを行えるようにする
                settings.domStorageEnabled = true
                // スクロールバーを非表示にする
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false

                // デバッグビルドの場合にWebViewのリモートデバッグを有効にする
                if (BuildConfig.DEBUG) {
                    WebView.setWebContentsDebuggingEnabled(true)
                }
                // コントローラーにWebViewインスタンスを渡し、外部からの操作を可能にする
                controller.attach(this)

                // JavaScriptからAndroidのメソッドを呼び出すためのインターフェースを定義する
                val dataReceiver = object {
                    @JavascriptInterface
                    fun onDataClick(name: String) {
                        onDataClick(name)
                    }
                }
                // WebViewにJavaScriptインターフェースを登録する
                addJavascriptInterface(dataReceiver, "Android")

                // WebViewの読み込みイベントをハンドルする
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // ページの読み込みが完了したら、チャートのオプションを設定する
                        view?.evaluateJavascript("setChartOption($jsonOption)", null)
                    }
                }
                // EChartsを描画するためのHTMLファイルを読み込む
                loadUrl("file:///android_asset/echarts.html$themeQuery")
            }
        },
        update = {
            val themeKey = theme?.key ?: "null"
            it.evaluateJavascript("updateTheme('$themeKey')", null)
            it.evaluateJavascript("setChartOption($jsonOption)", null)
            it.evaluateJavascript("resizeChart()", null)
        }
    )
}
