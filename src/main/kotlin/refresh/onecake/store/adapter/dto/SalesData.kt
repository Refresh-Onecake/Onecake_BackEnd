package refresh.onecake.store.adapter.dto

data class SalesData(
    val numOfOrdersThisMonth: Long,
    val numOfSalesThisMonth: Long,
    val numOfSalesLastMonth: Long
)

data class GraphData(
    val month: Int,
    val monthMinusOne: Int,
    val monthMinusTwo: Int,
    val monthMinusThree: Int,
    val monthMinusFour: Int
)
