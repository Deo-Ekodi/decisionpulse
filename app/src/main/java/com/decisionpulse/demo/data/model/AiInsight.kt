package com.decisionpulse.demo.data.model

enum class InsightPriority { HIGH, MEDIUM, LOW }
enum class InsightType { PERFORMANCE, COST, ALERT, OPPORTUNITY }

data class AiInsight(
    val id: Int,
    val farmCode: String?,       // null = SACCO-wide insight
    val title: String,
    val body: String,
    val action: String,
    val priority: InsightPriority,
    val type: InsightType,
    val hoursAgo: Int
)
