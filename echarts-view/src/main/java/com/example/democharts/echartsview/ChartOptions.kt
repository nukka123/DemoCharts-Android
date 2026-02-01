package com.example.democharts.echartsview

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

internal val json = Json {
    prettyPrint = true
}

/**
 * Represents the data structure for Apache ECharts' ''''option'''''' object.
 * See [the official documentation](https://echarts.apache.org/en/option.html) for more details.
 *
 * This is a sealed interface, with each implementing class corresponding to a specific chart type.
 * Each class is responsible for building its own JSON structure to be consumed by ECharts.
 */
sealed interface ChartOption {
    fun toJsonString(): String
}

// --- Enums for Chart options ---

enum class GraphLayout(val value: String) {
    NONE("none"),
    CIRCULAR("circular"),
    FORCE("force")
}

enum class Orient(val value: String) {
    HORIZONTAL("horizontal"),
    VERTICAL("vertical")
}


// --- Data Classes for ECharts options ---

/**
 * Represents the options for Bar and Line charts.
 *
 * @property xAxisData The categories for the x-axis. The number of items should match the number of data points in each series.
 * @property series The data series to be displayed on the chart. Can be a mix of [BarSeries] and [LineSeries].
 * @property dataZoom Configuration for the data zoom component.
 */
data class XYChartOption(
    val xAxisData: List<String>,
    val series: List<XYSeries>,
    val dataZoom: List<Map<String, Any>>? = null
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonObject("xAxis") {
            put("type", "category")
            putJsonArray("data") {
                xAxisData.forEach { add(it) }
            }
        }
        putJsonObject("yAxis") {
            put("type", "value")
        }
        putJsonArray("series") {
            series.forEach { seriesItem ->
                add(buildJsonObject {
                    when (seriesItem) {
                        is BarSeries -> put("type", "bar")
                        is LineSeries -> put("type", "line")
                    }
                    putJsonArray("data") {
                        seriesItem.data.forEach { d -> add(d) }
                    }
                })
            }
        }
        dataZoom?.let {
            putJsonArray("dataZoom") {
                it.forEach { zoomConfig ->
                    add(buildJsonObject {
                        for ((key, value) in zoomConfig) {
                            when (value) {
                                is String -> put(key, value)
                                is Number -> put(key, value)
                                is Boolean -> put(key, value)
                                else -> put(key, value.toString())
                            }
                        }
                    })
                }
            }
        }
    })
}

/**
 * Represents a data series for a chart, allowing for different types like bar or line.
 */
sealed interface XYSeries {
    /** The data points for this series. The size should be consistent with `xAxisData` in [XYChartOption]. */
    val data: List<Double>
}

/**
 * Represents a single bar series for a chart.
 *
 * @property data The data points for this series.
 */
data class BarSeries(override val data: List<Double>) : XYSeries

/**
 * Represents a single line series for a chart.
 *
 * @property data The data points for this series.
 */
data class LineSeries(override val data: List<Double>) : XYSeries

/**
 * Represents the value for a radius dimension, either as a percentage (Rate) or fixed pixels (Size).
 */
sealed interface RadiusValue

/**
 * Represents a radius value defined as a percentage.
 * @property value The percentage value (e.g., 50 for "50%").
 */
data class Rate(val value: Number) : RadiusValue

/**
 * Represents a radius value defined in pixels.
 * @property value The pixel value.
 */
data class Size(val value: Number) : RadiusValue


/**
 * Represents the radius of a pie chart.
 */
sealed interface Radius

/**
 * Represents the radius for a standard (non-donut) pie chart.
 * @property value The radius value.
 */
data class CircleRadius(val value: RadiusValue) : Radius

/**
 * Represents the inner and outer radius for a donut chart.
 * @property inner The inner radius value.
 * @property outer The outer radius value.
 */
data class DonutRadius(val inner: RadiusValue, val outer: RadiusValue) : Radius

/**
 * Represents the options for a Pie chart.
 *
 * @property data The data for the pie chart slices.
 * @property radius The radius of the pie chart. Use [CircleRadius] or [DonutRadius]. Defaults to `CircleRadius(Rate(50))`.
 */
data class PieChartOption(
    val data: List<PieData>,
    val radius: Radius = CircleRadius(Rate(50))
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "pie")
                when (radius) {
                    is CircleRadius -> {
                        when (val v = radius.value) {
                            is Rate -> put("radius", "${v.value}%")
                            is Size -> put("radius", v.value)
                        }
                    }

                    is DonutRadius -> {
                        putJsonArray("radius") {
                            when (val v = radius.inner) {
                                is Rate -> add("${v.value}%")
                                is Size -> add(v.value)
                            }
                            when (val v = radius.outer) {
                                is Rate -> add("${v.value}%")
                                is Size -> add(v.value)
                            }
                        }
                    }
                }
                putJsonArray("data") {
                    data.forEach {
                        add(buildJsonObject {
                            put("value", it.value)
                            put("name", it.name)
                        })
                    }
                }
            })
        }
    })
}

/**
 * Represents a single data point for a Pie chart.
 *
 * @property value The value of the pie slice.
 * @property name The name of the pie slice, used for legends and tooltips.
 */
data class PieData(val value: Double, val name: String)

/**
 * Represents the options for a Scatter chart.
 *
 * @property series The data points for the scatter chart. Each inner list should contain two values representing [x, y] coordinates.
 * @property symbolSize The size of the symbols used for the data points. Defaults to 20.
 */
data class ScatterChartOption(
    val series: List<ScatterSeries>,
    val symbolSize: Int = 20
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonObject("legend") {
            putJsonArray("data") {
                series.forEach { add(it.name) }
            }
        }
        putJsonObject("xAxis") {}
        putJsonObject("yAxis") {}
        putJsonArray("series") {
            series.forEach { seriesItem ->
                add(buildJsonObject {
                    put("name", seriesItem.name)
                    put("type", "scatter")
                    put("symbolSize", symbolSize)
                    put("data", buildJsonArray {
                        seriesItem.data.forEach { point ->
                            add(buildJsonArray {
                                point.forEach { add(it) }
                            })
                        }
                    })
                })
            }
        }
    })
}

/**
 * Represents a single scatter series for a chart.
 *
 * @property name The name of the series.
 * @property data The data points for this series.
 */
data class ScatterSeries(
    val name: String,
    val data: List<List<Double>>
)

/**
 * Represents the options for a Radar chart.
 *
 * @property legendData The names of the series to be displayed in the legend. These names must correspond to the `name` property in `seriesData`.
 * @property indicator The indicators for the radar chart axes. The number of indicators must match the number of values in each `RadarData.value` list.
 * @property seriesData The data for each series on the radar chart.
 */
data class RadarChartOption(
    val legendData: List<String>,
    val indicator: List<Indicator>,
    val seriesData: List<RadarData>
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonObject("legend") {
            putJsonArray("data") {
                legendData.forEach { add(it) }
            }
        }
        putJsonObject("radar") {
            putJsonArray("indicator") {
                indicator.forEach {
                    add(buildJsonObject {
                        put("name", it.name)
                        put("max", it.max)
                    })
                }
            }
        }
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "radar")
                putJsonArray("data") {
                    seriesData.forEach {
                        add(buildJsonObject {
                            put("name", it.name)
                            put("value", buildJsonArray {
                                it.value.forEach { v -> add(v) }
                            })
                        })
                    }
                }
            })
        }
    })
}

/**
 * Represents an indicator (axis) on a Radar chart.
 *
 * @property name The name of the indicator.
 * @property max The maximum value for this indicator's scale.
 */
data class Indicator(val name: String, val max: Double)

/**
 * Represents a single data series for a Radar chart.
 *
 * @property value The values for each indicator. The size of this list must match the number of indicators defined in `RadarChartOption`.
 * @property name The name of the data series. This must correspond to a name in `RadarChartOption.legendData`.
 */
data class RadarData(val value: List<Double>, val name: String)

/**
 * Represents the options for a Candlestick chart.
 *
 * @property xAxisData The categories for the x-axis. The size should match the size of `seriesData`.
 * @property seriesData The data for the candlestick chart. Each inner list must contain four values: [open, close, lowest, highest].
 */
data class CandlestickChartOption(
    val xAxisData: List<String>,
    val seriesData: List<List<Double>>
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonObject("xAxis") {
            put("type", "category")
            putJsonArray("data") {
                xAxisData.forEach { add(it) }
            }
        }
        putJsonObject("yAxis") {}
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "candlestick")
                put("data", buildJsonArray {
                    seriesData.forEach {
                        add(buildJsonArray {
                            it.forEach { v -> add(v) }
                        })
                    }
                })
            })
        }
    })
}

/**
 * Represents the options for a Treemap chart.
 *
 * @property data The hierarchical data to be displayed in the treemap.
 */
data class TreemapChartOption(val data: List<TreemapNode>) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "treemap")
                put("data", serializeTreemapNodes(data))
            })
        }
    })

    private fun serializeTreemapNodes(nodes: List<TreemapNode>): JsonElement = buildJsonArray {
        nodes.forEach { node ->
            add(buildJsonObject {
                put("name", node.name)
                put("value", node.value)
                node.children?.let {
                    put("children", serializeTreemapNodes(it))
                }
            })
        }
    }
}

/**
 * Represents a node in a Treemap chart.
 *
 * @property name The name of the node.
 * @property value The value of the node, which determines its area.
 * @property children Optional list of child nodes to create a hierarchy.
 */
data class TreemapNode(val name: String, val value: Double, val children: List<TreemapNode>? = null)

/**
 * Represents the options for a Sunburst chart.
 *
 * @property data The hierarchical data to be displayed in the sunburst chart.
 */
data class SunburstChartOption(val data: List<SunburstNode>) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "sunburst")
                put("data", serializeSunburstNodes(data))
            })
        }
    })

    private fun serializeSunburstNodes(nodes: List<SunburstNode>): JsonElement = buildJsonArray {
        nodes.forEach { node ->
            add(buildJsonObject {
                put("name", node.name)
                put("value", node.value)
                node.children?.let {
                    put("children", serializeSunburstNodes(it))
                }
            })
        }
    }
}

/**
 * Represents a node in a Sunburst chart.
 *
 * @property name The name of the node.
 * @property value The value of the node.
 * @property children Optional list of child nodes to create a hierarchy.
 */
data class SunburstNode(
    val name: String,
    val value: Double,
    val children: List<SunburstNode>? = null
)

/**
 * Represents the options for a Funnel chart.
 *
 * @property data The data for the funnel chart.
 */
data class FunnelChartOption(val data: List<FunnelData>) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "funnel")
                putJsonArray("data") {
                    data.forEach {
                        add(buildJsonObject {
                            put("value", it.value)
                            put("name", it.name)
                        })
                    }
                }
            })
        }
    })
}

/**
 * Represents a single data item in a Funnel chart.
 *
 * @property name The name of the data item (e.g., a stage in a funnel).
 * @property value The value associated with the data item.
 */
data class FunnelData(val name: String, val value: Double)

/**
 * Represents the options for a Gauge chart.
 *
 * @property data The data to be displayed on the gauge.
 */
data class GaugeChartOption(val data: List<GaugeData>) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "gauge")
                putJsonArray("data") {
                    data.forEach {
                        add(buildJsonObject {
                            put("value", it.value)
                        })
                    }
                }
            })
        }
    })
}

/**
 * Represents a single data point for a Gauge chart.
 *
 * @property value The value to be displayed.
 */
data class GaugeData(val value: Double)

/**
 * Represents the options for a Graph chart.
 *
 * @property nodes The list of nodes in the graph. Node names should be unique.
 * @property links The list of links connecting the nodes. The `source` and `target` properties must refer to the `name` of a node in the `nodes` list.
 * @property layout The layout algorithm for the graph.
 */
data class GraphChartOption(
    val nodes: List<GraphNode>,
    val links: List<GraphLink>,
    val layout: GraphLayout = GraphLayout.NONE
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "graph")
                put("layout", layout.value)
                putJsonArray("nodes") {
                    nodes.forEach {
                        add(buildJsonObject {
                            put("name", it.name)
                            put("x", it.x)
                            put("y", it.y)
                        })
                    }
                }
                putJsonArray("links") {
                    links.forEach {
                        add(buildJsonObject {
                            put("source", it.source)
                            put("target", it.target)
                        })
                    }
                }
            })
        }
    })
}

/**
 * Represents a node in a Graph chart.
 *
 * @property name A unique identifier for the node. This is used as a key in `GraphLink`.
 * @property x The x-coordinate of the node.
 * @property y The y-coordinate of the node.
 */
data class GraphNode(val name: String, val x: Int, val y: Int)

/**
 * Represents a link between two nodes in a Graph chart.
 *
 * @property source The name of the source node. This must match the `name` of a `GraphNode`.
 * @property target The name of the target node. This must match the `name` of a `GraphNode`.
 */
data class GraphLink(val source: String, val target: String)

/**
 * Represents the options for a Heatmap chart.
 *
 * @property xAxisData The categories for the x-axis. The size should match the number of columns in `seriesData`.
 * @property yAxisData The categories for the y-axis. The size should match the number of rows in `seriesData`.
 * @property seriesData The data for the heatmap, represented as a 2D list (matrix) of values. `seriesData[i][j]` corresponds to the value at `yAxisData[i]` and `xAxisData[j]`.
 * @property visualMap Configuration for the visual map component, which maps data values to visual properties like color.
 */
data class HeatmapChartOption(
    val xAxisData: List<String>,
    val yAxisData: List<String>,
    val seriesData: List<List<Double>>,
    val visualMap: VisualMap
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonObject("xAxis") {
            put("type", "category")
            putJsonArray("data") { xAxisData.forEach { add(it) } }
        }
        putJsonObject("yAxis") {
            put("type", "category")
            putJsonArray("data") { yAxisData.forEach { add(it) } }
        }
        putJsonObject("visualMap") {
            put("min", visualMap.min)
            put("max", visualMap.max)
            put("calculable", visualMap.calculable)
            put("orient", visualMap.orient.value)
            put("left", visualMap.left)
        }
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "heatmap")
                put("data", buildJsonArray {
                    seriesData.forEach { row ->
                        add(buildJsonArray {
                            row.forEach { item -> add(item) }
                        })
                    }
                })
            })
        }
    })
}

/**
 * Configures the visual mapping from data to visual elements.
 *
 * @property min The minimum value of the data range for the visual mapping.
 * @property max The maximum value of the data range for the visual mapping.
 * @property calculable If `true`, a handle is provided for the user to interactively adjust the range.
 * @property orient The orientation of the visual map component.
 * @property left The horizontal position of the component. Expects fixed values like "center", "left", "right", or a specific pixel/percentage value.
 */
data class VisualMap(
    val min: Double,
    val max: Double,
    val calculable: Boolean = true,
    val orient: Orient = Orient.HORIZONTAL,
    val left: String = "center"
)


/**
 * Represents the options for a Boxplot chart.
 *
 * @property xAxisData The categories for the x-axis. The size should match the size of `seriesData`.
 * @property seriesData The data for the boxplot. Each inner list must contain 5 values in the order: [minimum, Q1, median, Q3, maximum].
 */
data class BoxplotChartOption(
    val xAxisData: List<String>,
    val seriesData: List<List<Double>>
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonObject("xAxis") {
            put("type", "category")
            putJsonArray("data") { xAxisData.forEach { add(it) } }
        }
        putJsonObject("yAxis") {
            put("type", "value")
        }
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "boxplot")
                put("data", buildJsonArray {
                    seriesData.forEach {
                        add(buildJsonArray {
                            it.forEach { v -> add(v) }
                        })
                    }
                })
            })
        }
    })
}

/**
 * Represents the options for a Parallel Coordinates chart.
 *
 * @property parallelAxis The definition of each axis in the parallel coordinate system.
 * @property seriesData The data for the chart. Each inner list represents a data item, and its values correspond to the axes defined in `parallelAxis` by index. The size of each inner list must match the size of `parallelAxis`.
 */
data class ParallelChartOption(
    val parallelAxis: List<ParallelAxis>,
    val seriesData: List<List<Double>>
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("parallelAxis") {
            parallelAxis.forEach {
                add(buildJsonObject {
                    put("dim", it.dim)
                    put("name", it.name)
                })
            }
        }
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "parallel")
                put("data", buildJsonArray {
                    seriesData.forEach {
                        add(buildJsonArray {
                            it.forEach { v -> add(v) }
                        })
                    }
                })
            })
        }
    })
}

/**
 * Defines an axis in a Parallel Coordinates chart.
 *
 * @property dim The dimension index of the axis, starting from 0. This should be unique within the list of axes.
 * @property name The name of the axis, displayed on the chart.
 */
data class ParallelAxis(val dim: Int, val name: String)

/**
 * Represents the options for a Sankey chart.
 *
 * @property nodes The list of nodes in the Sankey diagram. Node names must be unique.
 * @property links The list of links that connect the nodes and represent flow.
 */
data class SankeyChartOption(
    val nodes: List<SankeyNode>,
    val links: List<SankeyLink>
) : ChartOption {
    override fun toJsonString(): String = json.encodeToString(buildJsonObject {
        putJsonArray("series") {
            add(buildJsonObject {
                put("type", "sankey")
                put("emphasis", buildJsonObject {
                    put("focus", "adjacency")
                })
                put("lineStyle", buildJsonObject {
                    put("color", "source")
                    put("curveness", 0.5)
                })
                put("label", buildJsonObject {
                    put("position", "right")
                })
                put("orient", "horizontal")
                put("nodeGap", 18)
                put("nodeWidth", 20)
                putJsonArray("data") {
                    nodes.forEach {
                        add(buildJsonObject {
                            put("name", it.name)
                        })
                    }
                }
                putJsonArray("links") {
                    links.forEach {
                        add(buildJsonObject {
                            put("source", it.source)
                            put("target", it.target)
                            put("value", it.value)
                        })
                    }
                }
            })
        }
    })
}

/**
 * Represents a link between two nodes in a Sankey chart.
 *
 * @property source The name of the source node. Must correspond to the `name` of a `SankeyNode`.
 * @property target The name of the target node. Must correspond to the `name` of a `SankeyNode`.
 * @property value The value of the flow from source to target.
 */
data class SankeyLink(val source: String, val target: String, val value: Double)

/**
 * Represents a node in a Sankey chart.
 *
 * @property name A unique identifier for the node. Used as a key in `SankeyLink`.
 */
data class SankeyNode(val name: String)
