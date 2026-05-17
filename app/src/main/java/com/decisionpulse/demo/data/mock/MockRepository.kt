package com.decisionpulse.demo.data.mock

import com.decisionpulse.demo.data.model.*
import kotlin.math.sin

object MockRepository {

    val farms: List<Farm> = listOf(
        Farm("NYR-001", "Othaya Central",    3, 87.4, feedCostPerLitre = 12.1, status = FarmStatus.ON_TRACK,  consistencyScore = 91, weeklyHistory = listOf(81.0,83.0,85.0,84.0,86.0,87.0,87.4)),
        Farm("NYR-004", "Othaya Central",    2, 74.2, feedCostPerLitre = 14.8, status = FarmStatus.ON_TRACK,  consistencyScore = 78, weeklyHistory = listOf(68.0,70.0,71.0,73.0,73.0,74.0,74.2)),
        Farm("NYR-007", "Nyeri North",       4, 92.1, feedCostPerLitre = 11.2, status = FarmStatus.ON_TRACK,  consistencyScore = 96, weeklyHistory = listOf(88.0,89.0,90.0,91.0,91.5,92.0,92.1)),
        Farm("NYR-012", "Tetu Sub-county",   2, 55.0, feedCostPerLitre = 18.4, status = FarmStatus.WATCH,     consistencyScore = 52, weeklyHistory = listOf(58.0,57.0,55.5,56.0,54.0,55.0,55.0)),
        Farm("NYR-019", "Mathira East",      3, 48.3, feedCostPerLitre = 22.7, status = FarmStatus.CRITICAL,  consistencyScore = 34, weeklyHistory = listOf(52.0,50.0,49.0,48.0,48.5,47.0,48.3)),
        Farm("NYR-023", "Mukurweini",        2, 78.9, feedCostPerLitre = 13.3, status = FarmStatus.ON_TRACK,  consistencyScore = 84, weeklyHistory = listOf(74.0,75.0,76.0,77.0,78.0,78.5,78.9)),
        Farm("NYR-031", "Othaya East",       3, 61.5, feedCostPerLitre = 16.9, status = FarmStatus.WATCH,     consistencyScore = 63, weeklyHistory = listOf(60.0,60.5,61.0,61.0,60.8,61.2,61.5)),
        Farm("NYR-038", "Kieni East",        5, 95.8, feedCostPerLitre = 10.8, status = FarmStatus.ON_TRACK,  consistencyScore = 98, weeklyHistory = listOf(92.0,93.0,94.0,95.0,95.5,95.8,95.8)),
        Farm("NYR-044", "Nyeri Central",     2, 42.0, feedCostPerLitre = 25.2, status = FarmStatus.CRITICAL,  consistencyScore = 28, weeklyHistory = listOf(50.0,47.0,45.0,43.0,43.5,42.0,42.0)),
        Farm("NYR-051", "Tetu Sub-county",   3, 82.3, feedCostPerLitre = 12.7, status = FarmStatus.ON_TRACK,  consistencyScore = 88, weeklyHistory = listOf(78.0,79.0,80.0,81.0,81.5,82.0,82.3)),
        Farm("NYR-058", "Mathira West",      2, 67.4, feedCostPerLitre = 15.5, status = FarmStatus.WATCH,     consistencyScore = 69, weeklyHistory = listOf(66.0,66.5,67.0,67.0,67.2,67.0,67.4)),
        Farm("NYR-062", "Kieni West",        4, 89.7, feedCostPerLitre = 11.8, status = FarmStatus.ON_TRACK,  consistencyScore = 93, weeklyHistory = listOf(85.0,86.0,87.0,88.0,89.0,89.5,89.7)),
    )

    val saccoSummary = SaccoSummary(
        name = "Nyeri Dairy SACCO",
        totalFarms = farms.size,
        farmsOnTrack = farms.count { it.status == FarmStatus.ON_TRACK },
        farmsWatch = farms.count { it.status == FarmStatus.WATCH },
        farmsCritical = farms.count { it.status == FarmStatus.CRITICAL },
        totalLitresToday = farms.sumOf { it.currentLitresPerDay },
        monthlyTargetLitres = farms.size * 100.0 * 30,
        monthlyActualLitres = farms.size * 73.4 * 30,
        avgFeedCostPerLitre = farms.map { it.feedCostPerLitre }.average(),
        topPerformerCode = "NYR-038",
        monthlyHistory = listOf(58.2, 61.4, 65.8, 67.2, 69.0, 70.1, 71.4, 72.8, 73.0, 73.4, 74.1, 73.9)
    )

    // Generate 90 days of realistic trajectory data for a farm
    fun getHistory(farm: Farm): List<DailyReading> {
        val baseStart = farm.currentLitresPerDay * 0.72
        return (1..90).map { day ->
            val trend = (farm.currentLitresPerDay - baseStart) * (day / 90.0)
            val noise = sin(day * 0.7) * 2.5
            val value = (baseStart + trend + noise).coerceAtLeast(20.0)
            DailyReading(day = day, litres = value, feedKg = value * 0.38)
        }
    }

    val insights: List<AiInsight> = listOf(
        AiInsight(1, "NYR-019", "Declining Yield — Intervention Needed",
            "NYR-019 has dropped 7.8% over 14 days. Pattern matches farms in Mathira East that responded to Napier fodder supplementation in Q3 last year.",
            "Schedule extension visit. Review feed sourcing.", InsightPriority.HIGH, InsightType.ALERT, 2),
        AiInsight(2, "NYR-044", "Feed Cost 108% Above SACCO Average",
            "Cost per litre at KES 25.20 vs. SACCO average of KES 15.41. Main driver appears to be concentrate overuse without proportional yield improvement.",
            "Audit feed mix. Compare with NYR-038 input schedule.", InsightPriority.HIGH, InsightType.COST, 4),
        AiInsight(3, null, "7 Farms Within 20L of 100L Target",
            "At current growth rates, NYR-001, NYR-023, NYR-051, and NYR-062 will hit 100L/day before end of Q2 2026. Recognition at next AGM recommended.",
            "Prepare performance certificates. Use as peer motivation.", InsightPriority.MEDIUM, InsightType.OPPORTUNITY, 8),
        AiInsight(4, "NYR-038", "Top Performer Benchmark Available",
            "NYR-038 (95.8L/day, KES 10.80/L) is running the lowest cost-per-litre in the SACCO. Their Brachiaria grass rotation and 4-month feed buffer stock model is replicable.",
            "Document NYR-038 practices. Share at next SACCO meeting.", InsightPriority.MEDIUM, InsightType.OPPORTUNITY, 12),
        AiInsight(5, null, "Feed Cost Correlation Found",
            "Farms logging feed costs weekly outperform non-loggers by an average of 18.3% on yield. The discipline of tracking appears to change behaviour.",
            "Encourage all farms to log weekly. Simple WhatsApp report is enough.", InsightPriority.MEDIUM, InsightType.PERFORMANCE, 20),
        AiInsight(6, "NYR-012", "Consistency Score Recovering",
            "NYR-012 dropped to 38 in January but has recovered to 52 after the coordinator visit in February. Still below target but trend is positive.",
            "Continue monthly coordinator check-ins. No emergency intervention needed.", InsightPriority.LOW, InsightType.PERFORMANCE, 36),
    )

    fun getFarmByCode(code: String): Farm? = farms.find { it.code == code }
}