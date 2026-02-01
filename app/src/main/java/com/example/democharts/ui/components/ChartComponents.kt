package com.example.democharts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.democharts.echartsview.BarSeries
import com.example.democharts.echartsview.BoxplotChartOption
import com.example.democharts.echartsview.CandlestickChartOption
import com.example.democharts.echartsview.EChartsView
import com.example.democharts.echartsview.FunnelChartOption
import com.example.democharts.echartsview.FunnelData
import com.example.democharts.echartsview.GaugeChartOption
import com.example.democharts.echartsview.GaugeData
import com.example.democharts.echartsview.GraphChartOption
import com.example.democharts.echartsview.GraphLink
import com.example.democharts.echartsview.GraphNode
import com.example.democharts.echartsview.HeatmapChartOption
import com.example.democharts.echartsview.Indicator
import com.example.democharts.echartsview.LineSeries
import com.example.democharts.echartsview.Orient
import com.example.democharts.echartsview.ParallelAxis
import com.example.democharts.echartsview.ParallelChartOption
import com.example.democharts.echartsview.PieChartOption
import com.example.democharts.echartsview.PieData
import com.example.democharts.echartsview.RadarChartOption
import com.example.democharts.echartsview.RadarData
import com.example.democharts.echartsview.SankeyChartOption
import com.example.democharts.echartsview.SankeyLink
import com.example.democharts.echartsview.SankeyNode
import com.example.democharts.echartsview.ScatterChartOption
import com.example.democharts.echartsview.ScatterSeries
import com.example.democharts.echartsview.SunburstChartOption
import com.example.democharts.echartsview.SunburstNode
import com.example.democharts.echartsview.TreemapChartOption
import com.example.democharts.echartsview.TreemapNode
import com.example.democharts.echartsview.VisualMap
import com.example.democharts.echartsview.XYChartOption
import com.example.democharts.ui.theme.DemoChartsTheme

internal data class Chart(
    val title: String,
    val content: @Composable () -> Unit
)

internal val charts = listOf(
    Chart("Interactive Chart") {
        InteractiveChart(modifier = Modifier.height(400.dp))
    },
    Chart("Bar Chart") {
        BarChart(modifier = Modifier.height(300.dp))
    },
    Chart("Line Chart") {
        LineChart(modifier = Modifier.height(300.dp))
    },
    Chart("Bar and Line Chart") {
        XYeChart(modifier = Modifier.height(300.dp))
    },
    Chart("Pie Chart") {
        PieChart(modifier = Modifier.height(300.dp))
    },
    Chart("Scatter Chart") {
        ScatterChart(modifier = Modifier.height(300.dp))
    },
    Chart("Radar Chart") {
        RadarChart(modifier = Modifier.height(300.dp))
    },
    Chart("Candlestick Chart") {
        CandlestickChart(modifier = Modifier.height(300.dp))
    },
    Chart("Treemap Chart") {
        TreemapChart(modifier = Modifier.height(300.dp))
    },
    Chart("Sunburst Chart") {
        SunburstChart(modifier = Modifier.height(300.dp))
    },
    Chart("Funnel Chart") {
        FunnelChart(modifier = Modifier.height(300.dp))
    },
    Chart("Gauge Chart") {
        GaugeChart(modifier = Modifier.height(300.dp))
    },
    Chart("Graph Chart") {
        GraphChart(modifier = Modifier.height(300.dp))
    },
    Chart("Heatmap Chart") {
        HeatmapChart(modifier = Modifier.height(300.dp))
    },
    Chart("Boxplot Chart") {
        BoxplotChart(modifier = Modifier.height(300.dp))
    },
    Chart("Parallel Chart") {
        ParallelChart(modifier = Modifier.height(300.dp))
    },
    Chart("Sankey Chart") {
        SankeyChart(modifier = Modifier.height(300.dp))
    }
)

@Composable
internal fun ChartCard(title: String, content: @Composable () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChartCardPreview() {
    DemoChartsTheme {
        ChartCard(title = "Sample Chart") {
            BarChart(modifier = Modifier.height(300.dp))
        }
    }
}

@Composable
fun BarChart(modifier: Modifier) {
    val option = XYChartOption(
        xAxisData = listOf("Jan", "Feb", "Mar", "Apr", "May"),
        series = listOf(BarSeries(data = listOf(20.0, 30.0, 40.0, 50.0, 60.0)))
    )
    EChartsView(option = option, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun BarChartPreview() {
    BarChart(modifier = Modifier.height(300.dp))
}

@Composable
fun LineChart(modifier: Modifier) {
    val option = XYChartOption(
        xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        series = listOf(LineSeries(data = listOf(150.0, 230.0, 224.0, 218.0, 135.0, 147.0, 260.0)))
    )
    EChartsView(option = option, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun LineChartPreview() {
    LineChart(modifier = Modifier.height(300.dp))
}

@Composable
fun XYeChart(
    modifier: Modifier
) {
    val option = XYChartOption(
        xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        series = listOf(
            LineSeries(data = listOf(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0)),
            BarSeries(data = listOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0))
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun PieChart(modifier: Modifier) {
    val option = PieChartOption(
        data = listOf(
            PieData(value = 1048.0, name = "Search Engine"),
            PieData(value = 735.0, name = "Direct"),
            PieData(value = 580.0, name = "Email"),
            PieData(value = 484.0, name = "Union Ads"),
            PieData(value = 300.0, name = "Video Ads")
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    PieChart(modifier = Modifier.height(300.dp))
}

@Composable
fun ScatterChart(modifier: Modifier) {
    val option = ScatterChartOption(
        series = listOf(
            ScatterSeries(
                name = "Series 1",
                data = listOf(
                    listOf(10.0, 8.04),
                    listOf(8.07, 6.95),
                    listOf(13.0, 7.58),
                    listOf(9.05, 8.81),
                    listOf(11.0, 8.33),
                )
            ),
            ScatterSeries(
                name = "Series 2",
                data = listOf(
                    listOf(14.0, 7.66),
                    listOf(13.4, 6.81),
                    listOf(10.0, 6.33),
                    listOf(14.0, 8.96),
                    listOf(12.5, 6.82)
                )
            )
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun RadarChart(
    modifier: Modifier
) {
    val option = RadarChartOption(
        legendData = listOf("Allocated Budget", "Actual Spending"),
        indicator = listOf(
            Indicator(name = "Sales", max = 6500.0),
            Indicator(name = "Administration", max = 16000.0),
            Indicator(name = "Information Technology", max = 30000.0),
            Indicator(name = "Customer Support", max = 38000.0),
            Indicator(name = "Development", max = 52000.0),
            Indicator(name = "Marketing", max = 25000.0)
        ),
        seriesData = listOf(
            RadarData(
                value = listOf(4200.0, 3000.0, 20000.0, 35000.0, 50000.0, 18000.0),
                name = "Allocated Budget"
            ),
            RadarData(
                value = listOf(5000.0, 14000.0, 28000.0, 26000.0, 42000.0, 21000.0),
                name = "Actual Spending"
            )
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun CandlestickChart(modifier: Modifier) {
    val option = CandlestickChartOption(
        xAxisData = listOf("2017-10-24", "2017-10-25", "2017-10-26", "2017-10-27"),
        seriesData = listOf(
            listOf(20.0, 34.0, 10.0, 38.0),
            listOf(40.0, 35.0, 30.0, 50.0),
            listOf(31.0, 38.0, 33.0, 44.0),
            listOf(38.0, 15.0, 5.0, 42.0)
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun TreemapChart(modifier: Modifier) {
    val option = TreemapChartOption(
        data = listOf(
            TreemapNode(
                name = "nodeA", value = 10.0, children = listOf(
                    TreemapNode(name = "nodeAa", value = 4.0),
                    TreemapNode(name = "nodeAb", value = 6.0)
                )
            ),
            TreemapNode(
                name = "nodeB", value = 20.0, children = listOf(
                    TreemapNode(name = "nodeBa", value = 10.0),
                    TreemapNode(name = "nodeBb", value = 10.0)
                )
            )
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun SunburstChart(modifier: Modifier) {
    val option = SunburstChartOption(
        data = listOf(
            SunburstNode(
                name = "Grandpa", value = 25.0, children = listOf(
                    SunburstNode(name = "Uncle", value = 15.0),
                    SunburstNode(name = "Father", value = 10.0)
                )
            )
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun FunnelChart(modifier: Modifier) {
    val option = FunnelChartOption(
        data = listOf(
            FunnelData(name = "Shown", value = 100.0),
            FunnelData(name = "Clicked", value = 80.0),
            FunnelData(name = "Visited", value = 60.0),
            FunnelData(name = "Consulted", value = 40.0),
            FunnelData(name = "Ordered", value = 20.0)
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun GaugeChart(modifier: Modifier) {
    val option = GaugeChartOption(
        data = listOf(GaugeData(value = 50.0))
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun GraphChart(modifier: Modifier) {
    val option = GraphChartOption(
        nodes = listOf(
            GraphNode(name = "1", x = 0, y = 0),
            GraphNode(name = "2", x = 100, y = 100),
            GraphNode(name = "3", x = 0, y = 100)
        ),
        links = listOf(
            GraphLink(source = "1", target = "2"),
            GraphLink(source = "2", target = "3"),
            GraphLink(source = "3", target = "1")
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun HeatmapChart(
    modifier: Modifier
) {
    val option = HeatmapChartOption(
        xAxisData = listOf("12a", "1a", "2a"),
        yAxisData = listOf("Saturday", "Friday", "Thursday"),
        visualMap = VisualMap(
            min = 0.0,
            max = 10.0,
            calculable = true,
            orient = Orient.HORIZONTAL,
            left = "center"
        ),
        seriesData = listOf(
            listOf(0.0, 0.0, 5.0),
            listOf(0.0, 1.0, 1.0),
            listOf(0.0, 2.0, 0.0)
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun BoxplotChart(modifier: Modifier) {
    val option = BoxplotChartOption(
        xAxisData = listOf("Sample 1", "Sample 2"),
        seriesData = listOf(
            listOf(655.0, 850.0, 940.0, 980.0, 1175.0, 1450.0),
            listOf(700.0, 900.0, 1000.0, 1100.0, 1200.0, 1500.0)
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun ParallelChart(modifier: Modifier) {
    val option = ParallelChartOption(
        parallelAxis = listOf(
            ParallelAxis(dim = 0, name = "Price"),
            ParallelAxis(dim = 1, name = "Net Weight"),
            ParallelAxis(dim = 2, name = "Amount")
        ),
        seriesData = listOf(
            listOf(12.99, 100.0, 80.0),
            listOf(9.99, 80.0, 70.0),
            listOf(20.0, 120.0, 90.0)
        )
    )
    EChartsView(option = option, modifier = modifier)
}

@Composable
fun SankeyChart(modifier: Modifier) {
    val option = SankeyChartOption(
        links = listOf(
            SankeyLink(source = "a", target = "b", value = 5.0),
            SankeyLink(source = "b", target = "c", value = 3.0),
            SankeyLink(source = "a", target = "c", value = 2.0)
        ),
        nodes = listOf(
            SankeyNode(name = "a"),
            SankeyNode(name = "b"),
            SankeyNode(name = "c")
        )
    )
    EChartsView(option = option, modifier = modifier)
}
