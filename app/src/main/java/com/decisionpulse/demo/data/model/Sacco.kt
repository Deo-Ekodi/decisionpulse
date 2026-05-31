package com.decisionpulse.demo.data.model

data class Sacco(
    val id: String,
    val name: String,
    val region: String,
    val subCounty: String,
    val totalFarms: Int,
    val targetLitresPerDayPerFarm: Double,
    val targetYear: Int,
    val collectionCentres: Int,
    val avgFeedCostTarget: Double,    // KES/litre target
    val primaryBreed: String,
    val monthlyHistory: List<Double>, // avg litres/day per farm, last 12 months
    val coordinatorName: String,
    val extensionOfficerCount: Int
)