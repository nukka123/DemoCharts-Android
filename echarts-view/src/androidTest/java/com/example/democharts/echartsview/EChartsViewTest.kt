package com.example.democharts.echartsview

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EChartsViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun echartsView_displaysWebView() {
        // Basic bar chart option for testing
        val chartOption = XYChartOption(
            xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            series = listOf(
                BarSeries(
                    data = listOf(150.0, 230.0, 224.0, 218.0, 135.0, 147.0, 260.0)
                )
            )
        )

        composeTestRule.setContent {
            EChartsView(
                option = chartOption,
                modifier = Modifier,
            )
        }

        // Verify that the WebView is displayed by checking its testTag.
        composeTestRule.onNodeWithTag("EChartsWebView").assertExists()
    }
}
