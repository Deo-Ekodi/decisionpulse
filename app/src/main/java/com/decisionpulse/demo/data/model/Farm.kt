package com.decisionpulse.demo.data.model

enum class FarmStatus { ON_TRACK, WATCH, CRITICAL }

data class Farm(
    val code: String,          // e.g. "NYR-047"
    val subLocation: String,   // e.g. "Othaya Central"
    val cowCount: Int,
    val currentLitresPerDay: Double,
    val targetLitresPerDay: Double = 100.0,
    val feedCostPerLitre: Double, // KES
    val status: FarmStatus,
    val consistencyScore: Int, // 0-100
    val weeklyHistory: List<Double> // last 7 days
) {
    val progressPercent: Float
        get() = (currentLitresPerDay / targetLitresPerDay).toFloat().coerceIn(0f, 1f)

    val projectedDate: String
        get() = when {
            currentLitresPerDay >= targetLitresPerDay -> "On target ✓"
            status == FarmStatus.ON_TRACK -> "Est. Q3 2026"
            status == FarmStatus.WATCH -> "Est. Q1 2027"
            else -> "Needs intervention"
        }
}
