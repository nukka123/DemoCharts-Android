package com.example.democharts.echartsview

import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class ChartOptionTest {

    @Test
    fun xyChartOption_toJsonString_line() {
        val option = XYChartOption(
            xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            series = listOf(
                LineSeries(
                    data = listOf(150.0, 230.0, 224.0, 218.0, 135.0, 147.0, 260.0)
                )
            )
        )

        val expectedJson =
            """{"xAxis":{"type":"category","data":["Mon","Tue","Wed","Thu","Fri","Sat","Sun"]},"yAxis":{"type":"value"},"series":[{"type":"line","data":[150.0,230.0,224.0,218.0,135.0,147.0,260.0]}]}"""
        assertEquals(
            Json.parseToJsonElement(expectedJson),
            Json.parseToJsonElement(option.toJsonString())
        )
    }

    @Test
    fun xyChartOption_toJsonString_bar() {
        val option = XYChartOption(
            xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            series = listOf(
                BarSeries(
                    data = listOf(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                )
            )
        )

        val expectedJson =
            """{"xAxis":{"type":"category","data":["Mon","Tue","Wed","Thu","Fri","Sat","Sun"]},"yAxis":{"type":"value"},"series":[{"type":"bar","data":[120.0,200.0,150.0,80.0,70.0,110.0,130.0]}]}"""
        assertEquals(
            Json.parseToJsonElement(expectedJson),
            Json.parseToJsonElement(option.toJsonString())
        )
    }

    @Test
    fun xyChartOption_toJsonString_mixed() {
        val option = XYChartOption(
            xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
            series = listOf(
                BarSeries(
                    data = listOf(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                ),
                LineSeries(
                    data = listOf(150.0, 230.0, 224.0, 218.0, 135.0, 147.0, 260.0)
                )
            )
        )

        val expectedJson =
            """{"xAxis":{"type":"category","data":["Mon","Tue","Wed","Thu","Fri","Sat","Sun"]},"yAxis":{"type":"value"},"series":[{"type":"bar","data":[120.0,200.0,150.0,80.0,70.0,110.0,130.0]},{"type":"line","data":[150.0,230.0,224.0,218.0,135.0,147.0,260.0]}]}"""
        assertEquals(
            Json.parseToJsonElement(expectedJson),
            Json.parseToJsonElement(option.toJsonString())
        )
    }

    @Test
    fun pieChartOption_toJsonString() {
        val option = PieChartOption(
            data = listOf(
                PieData(value = 1048.0, name = "Search Engine"),
                PieData(value = 735.0, name = "Direct"),
                PieData(value = 580.0, name = "Email"),
                PieData(value = 484.0, name = "Union Ads"),
                PieData(value = 300.0, name = "Video Ads")
            ),
            radius = CircleRadius(Rate(50))
        )

        val expectedJson =
            """{"series":[{"type":"pie","radius":"50%","data":[{"value":1048.0,"name":"Search Engine"},{"value":735.0,"name":"Direct"},{"value":580.0,"name":"Email"},{"value":484.0,"name":"Union Ads"},{"value":300.0,"name":"Video Ads"}]}]}"""
        assertEquals(
            Json.parseToJsonElement(expectedJson),
            Json.parseToJsonElement(option.toJsonString())
        )
    }

    @Test
    fun scatterChartOption_toJsonString() {
        val option = ScatterChartOption(
            series = listOf(
                ScatterSeries(
                    name = "Series 1",
                    data = listOf(
                        listOf(10.0, 8.04),
                        listOf(8.07, 6.95),
                        listOf(13.0, 7.58),
                        listOf(9.05, 8.81),
                        listOf(11.0, 8.33)
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

        val expectedJson =
            """{"legend":{"data":["Series 1","Series 2"]},"xAxis":{},"yAxis":{},"series":[{"name":"Series 1","type":"scatter","symbolSize":20,"data":[[10.0,8.04],[8.07,6.95],[13.0,7.58],[9.05,8.81],[11.0,8.33]]},{"name":"Series 2","type":"scatter","symbolSize":20,"data":[[14.0,7.66],[13.4,6.81],[10.0,6.33],[14.0,8.96],[12.5,6.82]]}]}"""
        assertEquals(
            Json.parseToJsonElement(expectedJson),
            Json.parseToJsonElement(option.toJsonString())
        )
    }

    @Test
    fun radarChartOption_toJsonString() {
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

        val expectedJson =
            """{"legend":{"data":["Allocated Budget","Actual Spending"]},"radar":{"indicator":[{"name":"Sales","max":6500.0},{"name":"Administration","max":16000.0},{"name":"Information Technology","max":30000.0},{"name":"Customer Support","max":38000.0},{"name":"Development","max":52000.0},{"name":"Marketing","max":25000.0}]},"series":[{"type":"radar","data":[{"name":"Allocated Budget","value":[4200.0,3000.0,20000.0,35000.0,50000.0,18000.0]},{"name":"Actual Spending","value":[5000.0,14000.0,28000.0,26000.0,42000.0,21000.0]}]}]}"""
        assertEquals(
            Json.parseToJsonElement(expectedJson),
            Json.parseToJsonElement(option.toJsonString())
        )
    }
}
