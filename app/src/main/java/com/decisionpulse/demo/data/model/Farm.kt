package com.decisionpulse.demo.data.model

enum class FarmStatus { ON_TRACK, WATCH, CRITICAL }

enum class CattleBreed(val display: String, val ceiling: Double) {
    PURE_FRISIAN("Pure Friesian", 35.0),
    AYRSHIRE_CROSS("Ayrshire Cross", 22.0),
    JERSEY_CROSS("Jersey Cross", 18.0),
    GUERNSEY("Guernsey", 20.0),
    ZEBU_GRADE("Zebu Grade", 8.0),
    SAHIWAL_CROSS("Sahiwal Cross", 14.0),
    BORAN_CROSS("Boran Cross", 10.0)
}

data class Farm(
    val code: String,
    val saccoId: String,
    val farmerName: String,
    val subLocation: String,
    val cowCount: Int,
    val lactatingCount: Int,
    val breed: CattleBreed,
    val currentLitresPerDay: Double,
    val targetLitresPerDay: Double,
    val feedCostPerLitre: Double,
    val status: FarmStatus,
    val consistencyScore: Int,
    val weeklyHistory: List<Double>,
    val fodderSource: String,
    val labourModel: String,
    val waterSource: String,
    val milkingFrequency: Int,
    val vetCostMonthly: Double
) {
    val progressPercent: Float
        get() = (currentLitresPerDay / targetLitresPerDay).toFloat().coerceIn(0f, 1f)

    val projectedDate: String
        get() = when {
            currentLitresPerDay >= targetLitresPerDay -> "On target"
            status == FarmStatus.ON_TRACK -> "Est. Q3 2026"
            status == FarmStatus.WATCH -> "Est. Q1 2027"
            else -> "Needs intervention"
        }

    val weekTrend: Double
        get() = if (weeklyHistory.size >= 2)
            weeklyHistory.last() - weeklyHistory.first()
        else 0.0
}