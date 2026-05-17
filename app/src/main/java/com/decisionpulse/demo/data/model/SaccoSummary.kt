package com.decisionpulse.demo.data.model

data class SaccoSummary(
    val name: String,
    val totalFarms: Int,
    val farmsOnTrack: Int,
    val farmsWatch: Int,
    val farmsCritical: Int,
    val totalLitresToday: Double,
    val monthlyTargetLitres: Double,
    val monthlyActualLitres: Double,
    val avgFeedCostPerLitre: Double,
    val topPerformerCode: String,
    val monthlyHistory: List<Double> // 12 months
) {
    val onTrackPercent: Float
        get() = farmsOnTrack.toFloat() / totalFarms.toFloat()
    val monthlyProgressPercent: Float
        get() = (monthlyActualLitres / monthlyTargetLitres).toFloat().coerceIn(0f, 1f)
}