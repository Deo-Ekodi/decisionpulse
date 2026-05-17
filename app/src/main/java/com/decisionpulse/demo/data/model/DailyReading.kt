package com.decisionpulse.demo.data.model

data class DailyReading(
    val day: Int,       // 1–90
    val litres: Double,
    val feedKg: Double,
    val notes: String = ""
)
