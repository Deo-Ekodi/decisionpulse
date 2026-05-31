package com.decisionpulse.demo.data.mock

import com.decisionpulse.demo.data.model.*
import kotlin.math.sin

object MockRepository {

    const val coordinatorName = "Deo Ekodi"

    // ── SACCOS ──────────────────────────────────────────────────────────────

    val saccos: List<Sacco> = listOf(
        Sacco(
            id = "NYR",
            name = "Nyeri Dairy SACCO",
            region = "Nyeri County",
            subCounty = "Othaya",
            totalFarms = 12,
            targetLitresPerDayPerFarm = 100.0,
            targetYear = 2027,
            collectionCentres = 3,
            avgFeedCostTarget = 12.0,
            primaryBreed = "Pure Friesian / Ayrshire Cross",
            monthlyHistory = listOf(58.2, 61.4, 65.8, 67.2, 69.0, 70.1, 71.4, 72.8, 73.0, 73.4, 74.1, 73.9),
            coordinatorName = "Deo Ekodi",
            extensionOfficerCount = 2
        ),
        Sacco(
            id = "MKR",
            name = "Mukurweini Farmers SACCO",
            region = "Nyeri County",
            subCounty = "Mukurweini",
            totalFarms = 18,
            targetLitresPerDayPerFarm = 80.0,
            targetYear = 2026,
            collectionCentres = 2,
            avgFeedCostTarget = 14.0,
            primaryBreed = "Ayrshire Cross / Jersey Cross",
            monthlyHistory = listOf(42.0, 44.1, 46.3, 48.0, 49.8, 51.2, 52.6, 53.1, 54.0, 55.2, 56.8, 57.0),
            coordinatorName = "Grace Wanjiku",
            extensionOfficerCount = 3
        ),
        Sacco(
            id = "KNI",
            name = "Kieni Highlands Co-op",
            region = "Nyeri County",
            subCounty = "Kieni East",
            totalFarms = 9,
            targetLitresPerDayPerFarm = 120.0,
            targetYear = 2027,
            collectionCentres = 2,
            avgFeedCostTarget = 10.5,
            primaryBreed = "Pure Friesian",
            monthlyHistory = listOf(78.0, 80.2, 83.1, 85.4, 87.0, 88.5, 90.1, 91.4, 92.0, 93.5, 94.8, 95.2),
            coordinatorName = "Peter Kamau",
            extensionOfficerCount = 2
        ),
        Sacco(
            id = "TTU",
            name = "Tetu Smallholder Dairy",
            region = "Nyeri County",
            subCounty = "Tetu",
            totalFarms = 24,
            targetLitresPerDayPerFarm = 60.0,
            targetYear = 2026,
            collectionCentres = 4,
            avgFeedCostTarget = 16.0,
            primaryBreed = "Sahiwal Cross / Zebu Grade",
            monthlyHistory = listOf(28.0, 29.4, 31.0, 32.5, 33.8, 35.1, 36.0, 37.2, 38.4, 39.0, 40.1, 41.2),
            coordinatorName = "Mary Njeri",
            extensionOfficerCount = 4
        ),
        Sacco(
            id = "MTH",
            name = "Mathira Elite Dairy",
            region = "Nyeri County",
            subCounty = "Mathira East",
            totalFarms = 11,
            targetLitresPerDayPerFarm = 90.0,
            targetYear = 2026,
            collectionCentres = 2,
            avgFeedCostTarget = 11.5,
            primaryBreed = "Pure Friesian / Guernsey",
            monthlyHistory = listOf(52.0, 55.3, 58.0, 60.4, 62.8, 64.1, 65.5, 67.0, 68.3, 69.4, 70.8, 71.5),
            coordinatorName = "James Mwangi",
            extensionOfficerCount = 2
        )
    )

    // ── FARMS (20 farms across 5 SACCOs) ───────────────────────────────────

    val farms: List<Farm> = listOf(

        // ─ NYR (Nyeri Dairy SACCO) — 12 farms ─
        Farm(
            code = "NYR-001", saccoId = "NYR", farmerName = "Joseph Kariuki",
            subLocation = "Othaya Central", cowCount = 3, lactatingCount = 3,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 87.4, targetLitresPerDay = 100.0,
            feedCostPerLitre = 12.1, status = FarmStatus.ON_TRACK, consistencyScore = 91,
            weeklyHistory = listOf(81.0, 83.0, 85.0, 84.0, 86.0, 87.0, 87.4),
            fodderSource = "Own land (Brachiaria)", labourModel = "Owner-operated",
            waterSource = "Piped water", milkingFrequency = 2, vetCostMonthly = 1200.0
        ),
        Farm(
            code = "NYR-004", saccoId = "NYR", farmerName = "Hannah Wanjiku",
            subLocation = "Othaya Central", cowCount = 2, lactatingCount = 2,
            breed = CattleBreed.AYRSHIRE_CROSS, currentLitresPerDay = 74.2, targetLitresPerDay = 100.0,
            feedCostPerLitre = 14.8, status = FarmStatus.ON_TRACK, consistencyScore = 78,
            weeklyHistory = listOf(68.0, 70.0, 71.0, 73.0, 73.0, 74.0, 74.2),
            fodderSource = "Purchased Napier", labourModel = "Hired herdsman",
            waterSource = "Borehole", milkingFrequency = 2, vetCostMonthly = 800.0
        ),
        Farm(
            code = "NYR-007", saccoId = "NYR", farmerName = "Simon Gathura",
            subLocation = "Nyeri North", cowCount = 4, lactatingCount = 4,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 92.1, targetLitresPerDay = 100.0,
            feedCostPerLitre = 11.2, status = FarmStatus.ON_TRACK, consistencyScore = 96,
            weeklyHistory = listOf(88.0, 89.0, 90.0, 91.0, 91.5, 92.0, 92.1),
            fodderSource = "Own paddock (Napier + Rhodes)", labourModel = "Owner + 1 casual",
            waterSource = "River gravity", milkingFrequency = 3, vetCostMonthly = 900.0
        ),
        Farm(
            code = "NYR-012", saccoId = "NYR", farmerName = "Esther Muthoni",
            subLocation = "Tetu Sub-county", cowCount = 2, lactatingCount = 1,
            breed = CattleBreed.JERSEY_CROSS, currentLitresPerDay = 55.0, targetLitresPerDay = 100.0,
            feedCostPerLitre = 18.4, status = FarmStatus.WATCH, consistencyScore = 52,
            weeklyHistory = listOf(58.0, 57.0, 55.5, 56.0, 54.0, 55.0, 55.0),
            fodderSource = "Purchased Napier (50km)", labourModel = "Owner-operated",
            waterSource = "Surface collection", milkingFrequency = 1, vetCostMonthly = 2100.0
        ),
        Farm(
            code = "NYR-019", saccoId = "NYR", farmerName = "David Mwangi",
            subLocation = "Mathira East", cowCount = 3, lactatingCount = 2,
            breed = CattleBreed.AYRSHIRE_CROSS, currentLitresPerDay = 48.3, targetLitresPerDay = 100.0,
            feedCostPerLitre = 22.7, status = FarmStatus.CRITICAL, consistencyScore = 34,
            weeklyHistory = listOf(52.0, 50.0, 49.0, 48.0, 48.5, 47.0, 48.3),
            fodderSource = "Purchased Napier (60km)", labourModel = "Hired herdsman",
            waterSource = "Communal borehole", milkingFrequency = 1, vetCostMonthly = 3800.0
        ),
        Farm(
            code = "NYR-023", saccoId = "NYR", farmerName = "Alice Kamau",
            subLocation = "Mukurweini", cowCount = 2, lactatingCount = 2,
            breed = CattleBreed.AYRSHIRE_CROSS, currentLitresPerDay = 78.9, targetLitresPerDay = 100.0,
            feedCostPerLitre = 13.3, status = FarmStatus.ON_TRACK, consistencyScore = 84,
            weeklyHistory = listOf(74.0, 75.0, 76.0, 77.0, 78.0, 78.5, 78.9),
            fodderSource = "Own land + concentrates", labourModel = "Owner-operated",
            waterSource = "Piped water", milkingFrequency = 2, vetCostMonthly = 600.0
        ),
        Farm(
            code = "NYR-038", saccoId = "NYR", farmerName = "Francis Maina",
            subLocation = "Kieni East", cowCount = 5, lactatingCount = 5,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 95.8, targetLitresPerDay = 100.0,
            feedCostPerLitre = 10.8, status = FarmStatus.ON_TRACK, consistencyScore = 98,
            weeklyHistory = listOf(92.0, 93.0, 94.0, 95.0, 95.5, 95.8, 95.8),
            fodderSource = "Own paddock (Brachiaria rotation)", labourModel = "2 permanent herdsmen",
            waterSource = "Borehole + gravity tank", milkingFrequency = 3, vetCostMonthly = 400.0
        ),
        Farm(
            code = "NYR-044", saccoId = "NYR", farmerName = "Rose Njeri",
            subLocation = "Nyeri Central", cowCount = 2, lactatingCount = 1,
            breed = CattleBreed.ZEBU_GRADE, currentLitresPerDay = 42.0, targetLitresPerDay = 100.0,
            feedCostPerLitre = 25.2, status = FarmStatus.CRITICAL, consistencyScore = 28,
            weeklyHistory = listOf(50.0, 47.0, 45.0, 43.0, 43.5, 42.0, 42.0),
            fodderSource = "Market purchase (daily)", labourModel = "Owner-operated",
            waterSource = "Water kiosk", milkingFrequency = 1, vetCostMonthly = 5200.0
        ),
        Farm(
            code = "NYR-051", saccoId = "NYR", farmerName = "Charles Githinji",
            subLocation = "Tetu Sub-county", cowCount = 3, lactatingCount = 3,
            breed = CattleBreed.AYRSHIRE_CROSS, currentLitresPerDay = 82.3, targetLitresPerDay = 100.0,
            feedCostPerLitre = 12.7, status = FarmStatus.ON_TRACK, consistencyScore = 88,
            weeklyHistory = listOf(78.0, 79.0, 80.0, 81.0, 81.5, 82.0, 82.3),
            fodderSource = "Own Napier + silage buffer", labourModel = "Owner + family",
            waterSource = "Piped water", milkingFrequency = 2, vetCostMonthly = 700.0
        ),
        Farm(
            code = "NYR-058", saccoId = "NYR", farmerName = "Beatrice Wanjiru",
            subLocation = "Mathira West", cowCount = 2, lactatingCount = 2,
            breed = CattleBreed.GUERNSEY, currentLitresPerDay = 67.4, targetLitresPerDay = 100.0,
            feedCostPerLitre = 15.5, status = FarmStatus.WATCH, consistencyScore = 69,
            weeklyHistory = listOf(66.0, 66.5, 67.0, 67.0, 67.2, 67.0, 67.4),
            fodderSource = "Mix: own 40% + purchased 60%", labourModel = "Hired herdsman",
            waterSource = "River + filter", milkingFrequency = 2, vetCostMonthly = 1100.0
        ),
        Farm(
            code = "NYR-062", saccoId = "NYR", farmerName = "Samuel Ndungu",
            subLocation = "Kieni West", cowCount = 4, lactatingCount = 4,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 89.7, targetLitresPerDay = 100.0,
            feedCostPerLitre = 11.8, status = FarmStatus.ON_TRACK, consistencyScore = 93,
            weeklyHistory = listOf(85.0, 86.0, 87.0, 88.0, 89.0, 89.5, 89.7),
            fodderSource = "Own paddock + concentrates", labourModel = "Owner + 1 part-time",
            waterSource = "Borehole", milkingFrequency = 2, vetCostMonthly = 550.0
        ),
        Farm(
            code = "NYR-071", saccoId = "NYR", farmerName = "Lydia Mwangi",
            subLocation = "Othaya East", cowCount = 3, lactatingCount = 2,
            breed = CattleBreed.JERSEY_CROSS, currentLitresPerDay = 61.5, targetLitresPerDay = 100.0,
            feedCostPerLitre = 16.9, status = FarmStatus.WATCH, consistencyScore = 63,
            weeklyHistory = listOf(60.0, 60.5, 61.0, 61.0, 60.8, 61.2, 61.5),
            fodderSource = "Purchased Napier (20km)", labourModel = "Owner-operated",
            waterSource = "Communal tap", milkingFrequency = 2, vetCostMonthly = 1400.0
        ),

        // ─ MKR (Mukurweini Farmers SACCO) — 3 farms for demo ─
        Farm(
            code = "MKR-003", saccoId = "MKR", farmerName = "John Muriuki",
            subLocation = "Mukurweini Central", cowCount = 2, lactatingCount = 2,
            breed = CattleBreed.AYRSHIRE_CROSS, currentLitresPerDay = 63.1, targetLitresPerDay = 80.0,
            feedCostPerLitre = 15.2, status = FarmStatus.ON_TRACK, consistencyScore = 82,
            weeklyHistory = listOf(59.0, 60.0, 61.5, 62.0, 62.8, 63.0, 63.1),
            fodderSource = "Own land (mixed grasses)", labourModel = "Owner-operated",
            waterSource = "Gravity scheme", milkingFrequency = 2, vetCostMonthly = 750.0
        ),
        Farm(
            code = "MKR-011", saccoId = "MKR", farmerName = "Teresa Njoki",
            subLocation = "Mukurweini North", cowCount = 1, lactatingCount = 1,
            breed = CattleBreed.JERSEY_CROSS, currentLitresPerDay = 38.4, targetLitresPerDay = 80.0,
            feedCostPerLitre = 20.3, status = FarmStatus.WATCH, consistencyScore = 47,
            weeklyHistory = listOf(42.0, 41.0, 40.5, 39.0, 38.8, 38.5, 38.4),
            fodderSource = "Market (no storage)", labourModel = "Owner-operated",
            waterSource = "Communal borehole", milkingFrequency = 1, vetCostMonthly = 2900.0
        ),
        Farm(
            code = "MKR-017", saccoId = "MKR", farmerName = "Peter Njoroge",
            subLocation = "Mukurweini East", cowCount = 3, lactatingCount = 2,
            breed = CattleBreed.SAHIWAL_CROSS, currentLitresPerDay = 51.8, targetLitresPerDay = 80.0,
            feedCostPerLitre = 17.8, status = FarmStatus.WATCH, consistencyScore = 61,
            weeklyHistory = listOf(50.0, 50.5, 51.0, 51.5, 51.8, 51.5, 51.8),
            fodderSource = "Mix: own 50% + purchased", labourModel = "Hired (1 part-time)",
            waterSource = "Rainwater harvesting", milkingFrequency = 2, vetCostMonthly = 1200.0
        ),

        // ─ KNI (Kieni Highlands Co-op) — 2 farms for demo ─
        Farm(
            code = "KNI-001", saccoId = "KNI", farmerName = "Michael Kamau",
            subLocation = "Kieni East Highlands", cowCount = 6, lactatingCount = 6,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 112.4, targetLitresPerDay = 120.0,
            feedCostPerLitre = 10.1, status = FarmStatus.ON_TRACK, consistencyScore = 97,
            weeklyHistory = listOf(108.0, 109.5, 110.0, 111.0, 111.8, 112.0, 112.4),
            fodderSource = "Own 5-acre paddock (Brachiaria)", labourModel = "2 full-time herdsmen",
            waterSource = "Private borehole", milkingFrequency = 3, vetCostMonthly = 320.0
        ),
        Farm(
            code = "KNI-007", saccoId = "KNI", farmerName = "Agnes Waweru",
            subLocation = "Kieni West Highlands", cowCount = 4, lactatingCount = 3,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 88.2, targetLitresPerDay = 120.0,
            feedCostPerLitre = 11.4, status = FarmStatus.WATCH, consistencyScore = 74,
            weeklyHistory = listOf(91.0, 90.0, 89.5, 89.0, 88.5, 88.0, 88.2),
            fodderSource = "Own + silage (6-month buffer)", labourModel = "Owner + hired",
            waterSource = "River gravity", milkingFrequency = 2, vetCostMonthly = 480.0
        ),

        // ─ TTU (Tetu Smallholder) — 2 farms for demo ─
        Farm(
            code = "TTU-004", saccoId = "TTU", farmerName = "Margaret Wambui",
            subLocation = "Tetu West", cowCount = 2, lactatingCount = 1,
            breed = CattleBreed.SAHIWAL_CROSS, currentLitresPerDay = 34.8, targetLitresPerDay = 60.0,
            feedCostPerLitre = 18.6, status = FarmStatus.ON_TRACK, consistencyScore = 71,
            weeklyHistory = listOf(31.0, 32.0, 33.0, 33.5, 34.0, 34.5, 34.8),
            fodderSource = "Own 0.5 acre Napier", labourModel = "Owner-operated",
            waterSource = "Communal tap", milkingFrequency = 1, vetCostMonthly = 900.0
        ),
        Farm(
            code = "TTU-009", saccoId = "TTU", farmerName = "Zacchaeus Gitau",
            subLocation = "Tetu Central", cowCount = 1, lactatingCount = 1,
            breed = CattleBreed.BORAN_CROSS, currentLitresPerDay = 19.2, targetLitresPerDay = 60.0,
            feedCostPerLitre = 28.4, status = FarmStatus.CRITICAL, consistencyScore = 22,
            weeklyHistory = listOf(24.0, 22.5, 21.0, 20.0, 19.5, 19.0, 19.2),
            fodderSource = "Market (daily, no storage)", labourModel = "Owner-operated",
            waterSource = "Water kiosk (expensive)", milkingFrequency = 1, vetCostMonthly = 4100.0
        ),

        // ─ MTH (Mathira Elite Dairy) — 2 farms for demo ─
        Farm(
            code = "MTH-002", saccoId = "MTH", farmerName = "Elijah Nderitu",
            subLocation = "Mathira Central", cowCount = 3, lactatingCount = 3,
            breed = CattleBreed.PURE_FRISIAN, currentLitresPerDay = 84.6, targetLitresPerDay = 90.0,
            feedCostPerLitre = 11.6, status = FarmStatus.ON_TRACK, consistencyScore = 89,
            weeklyHistory = listOf(80.0, 81.5, 82.0, 83.0, 83.8, 84.0, 84.6),
            fodderSource = "Own Brachiaria + concentrates", labourModel = "Owner + 1 part-time",
            waterSource = "Borehole", milkingFrequency = 2, vetCostMonthly = 500.0
        ),
        Farm(
            code = "MTH-008", saccoId = "MTH", farmerName = "Priscilla Maina",
            subLocation = "Mathira East", cowCount = 2, lactatingCount = 2,
            breed = CattleBreed.GUERNSEY, currentLitresPerDay = 57.3, targetLitresPerDay = 90.0,
            feedCostPerLitre = 16.8, status = FarmStatus.WATCH, consistencyScore = 58,
            weeklyHistory = listOf(62.0, 61.0, 60.0, 58.5, 58.0, 57.5, 57.3),
            fodderSource = "Purchased Napier (30km)", labourModel = "Hired full-time",
            waterSource = "Piped water", milkingFrequency = 2, vetCostMonthly = 1600.0
        )
    )

    // ── INSIGHTS ────────────────────────────────────────────────────────────

    val insights: List<AiInsight> = listOf(
        AiInsight(
            id = 1, saccoId = "NYR", farmCode = "NYR-019",
            title = "Declining Yield — Intervention Needed",
            body = "NYR-019 has dropped 7.8% over 14 days. Pattern matches farms in Mathira East that responded to Napier fodder supplementation in Q3 last year. Feed cost at KES 22.70/L is 87% above the SACCO average.",
            action = "Schedule extension visit. Review feed sourcing chain — nearest Napier surplus is NYR-023, 12km away.",
            priority = InsightPriority.HIGH, type = InsightType.ALERT, hoursAgo = 2
        ),
        AiInsight(
            id = 2, saccoId = "NYR", farmCode = "NYR-044",
            title = "Feed Cost 108% Above SACCO Average",
            body = "Cost per litre at KES 25.20 vs. SACCO average of KES 15.41. Main driver is daily market feed purchase with zero storage buffer and a Zebu-grade breed with a production ceiling of 8L per cow.",
            action = "Breed upgrade assessment. Silage storage feasibility study. Compare with NYR-038 input model.",
            priority = InsightPriority.HIGH, type = InsightType.COST, hoursAgo = 4
        ),
        AiInsight(
            id = 3, saccoId = "NYR", farmCode = null,
            title = "Four Farms Within 15L of 100L Target",
            body = "At current growth rates, NYR-001, NYR-007, NYR-051, and NYR-062 will reach 100L/day before end of Q2 2026. Consistent upward trajectory across all four. Recognition at next AGM recommended.",
            action = "Prepare performance certificates. Use peer group as motivation framework for Watch farms.",
            priority = InsightPriority.MEDIUM, type = InsightType.OPPORTUNITY, hoursAgo = 8
        ),
        AiInsight(
            id = 4, saccoId = "NYR", farmCode = "NYR-038",
            title = "Top Performer Benchmark Available",
            body = "NYR-038 (95.8L/day, KES 10.80/L) runs the lowest cost-per-litre in the SACCO. Brachiaria rotation on own paddock, 3-times-daily milking, 6-month feed buffer, and 2 permanent herdsmen. All replicable at smaller scale.",
            action = "Document NYR-038 practices. Schedule site visit for 3 Watch-status farms.",
            priority = InsightPriority.MEDIUM, type = InsightType.OPPORTUNITY, hoursAgo = 12
        ),
        AiInsight(
            id = 5, saccoId = "MKR", farmCode = "MKR-011",
            title = "Sustained Decline — Mukurweini North",
            body = "MKR-011 has declined from 42.0 to 38.4 litres over 7 days. Single lactating cow with no storage and market-only feed. Breed ceiling for Jersey cross at this input level is approximately 40L — farm is near ceiling under current conditions.",
            action = "Discuss second heifer acquisition. Assess fodder land availability. Priority field visit this week.",
            priority = InsightPriority.HIGH, type = InsightType.ALERT, hoursAgo = 6
        ),
        AiInsight(
            id = 6, saccoId = "KNI", farmCode = "KNI-001",
            title = "Kieni Best Practice — Replication Opportunity",
            body = "KNI-001 at 112.4L/day with KES 10.10/L is the most efficient farm across all five SACCOs. 5-acre Brachiaria paddock self-sufficiency model eliminates market fodder cost entirely. 3x daily milking adds estimated 18% yield vs. twice daily.",
            action = "Document full input model. Cross-SACCO best practice circular for extension officers.",
            priority = InsightPriority.MEDIUM, type = InsightType.OPPORTUNITY, hoursAgo = 14
        ),
        AiInsight(
            id = 7, saccoId = "TTU", farmCode = "TTU-009",
            title = "Critical: Cost 3.5x SACCO Target",
            body = "TTU-009 at KES 28.40/L vs. the Tetu SACCO target of KES 16.00. Boran cross breed ceiling is 10L/cow — current yield of 19.2L from one cow exceeds ceiling expectation, suggesting data may be overreported. Field verification needed.",
            action = "Field verification visit. Check milking records. Assess breed documentation. Consider breed reclassification.",
            priority = InsightPriority.HIGH, type = InsightType.ALERT, hoursAgo = 3
        ),
        AiInsight(
            id = 8, saccoId = null, farmCode = null,
            title = "Cross-SACCO: Fodder Sourcing Network Opportunity",
            body = "Across all five Nyeri SACCOs, 7 farms are purchasing Napier from distances over 30km at significant transport cost premium. NYR-038 and KNI-001 both have documented surplus in dry season. A formal fodder exchange program could reduce average cost-per-litre by an estimated KES 2.40 across affected farms.",
            action = "Coordinate inter-SACCO fodder exchange pilot. Present at next County Agriculture Forum.",
            priority = InsightPriority.MEDIUM, type = InsightType.OPPORTUNITY, hoursAgo = 18
        ),
        AiInsight(
            id = 9, saccoId = "MTH", farmCode = "MTH-008",
            title = "Mathira East — 7-Day Decline Pattern",
            body = "MTH-008 has fallen from 62.0 to 57.3 litres in 7 days. Pattern coincides with a confirmed dry-season onset in Mathira East. However, the rate of decline (7.6%) exceeds the SACCO seasonal average (4.2%), flagging this as anomalous.",
            action = "Verify fodder supply continuity. Check for subclinical illness — last vet visit was 6 weeks ago.",
            priority = InsightPriority.MEDIUM, type = InsightType.ALERT, hoursAgo = 5
        ),
        AiInsight(
            id = 10, saccoId = null, farmCode = null,
            title = "Record-Keeping Drives 18% Yield Advantage",
            body = "Across all five SACCOs, farms logging feed costs weekly outperform non-loggers by an average of 18.3% on yield and 22% on feed cost efficiency. The 9 farms with consistency scores above 80 share one common practice: daily record keeping.",
            action = "Push weekly WhatsApp log reminder to all 74 farms. Simple format, 3 fields only: litres, feed kg, vet spend.",
            priority = InsightPriority.LOW, type = InsightType.PERFORMANCE, hoursAgo = 24
        )
    )

    // ── COMPUTED SUMMARIES ───────────────────────────────────────────────────

    fun getSaccoById(id: String): Sacco? = saccos.find { it.id == id }

    fun getFarmsBySacco(saccoId: String): List<Farm> = farms.filter { it.saccoId == saccoId }

    fun getFarmByCode(code: String): Farm? = farms.find { it.code == code }

    fun getInsightForFarm(code: String): AiInsight? =
        insights.firstOrNull { it.farmCode == code }

    fun getInsightsBySacco(saccoId: String): List<AiInsight> =
        insights.filter { it.saccoId == saccoId || it.saccoId == null }

    fun getSaccoSummary(saccoId: String): SaccoSummary? {
        val saccoFarms = getFarmsBySacco(saccoId)
        if (saccoFarms.isEmpty()) return null
        val sacco = getSaccoById(saccoId) ?: return null
        return SaccoSummary(
            name = sacco.name,
            totalFarms = saccoFarms.size,
            farmsOnTrack = saccoFarms.count { it.status == FarmStatus.ON_TRACK },
            farmsWatch = saccoFarms.count { it.status == FarmStatus.WATCH },
            farmsCritical = saccoFarms.count { it.status == FarmStatus.CRITICAL },
            totalLitresToday = saccoFarms.sumOf { it.currentLitresPerDay },
            monthlyTargetLitres = saccoFarms.size * sacco.targetLitresPerDayPerFarm * 30,
            monthlyActualLitres = saccoFarms.sumOf { it.currentLitresPerDay } * 30,
            avgFeedCostPerLitre = saccoFarms.map { it.feedCostPerLitre }.average(),
            topPerformerCode = saccoFarms.maxByOrNull { it.currentLitresPerDay }?.code ?: "",
            monthlyHistory = sacco.monthlyHistory
        )
    }

    // Cross-SACCO executive summary
    fun getNetworkSummary(): SaccoSummary {
        val allFarms = farms
        return SaccoSummary(
            name = "Nyeri County — All SACCOs",
            totalFarms = allFarms.size,
            farmsOnTrack = allFarms.count { it.status == FarmStatus.ON_TRACK },
            farmsWatch = allFarms.count { it.status == FarmStatus.WATCH },
            farmsCritical = allFarms.count { it.status == FarmStatus.CRITICAL },
            totalLitresToday = allFarms.sumOf { it.currentLitresPerDay },
            monthlyTargetLitres = saccos.sumOf { s -> s.totalFarms * s.targetLitresPerDayPerFarm * 30 },
            monthlyActualLitres = allFarms.sumOf { it.currentLitresPerDay } * 30,
            avgFeedCostPerLitre = allFarms.map { it.feedCostPerLitre }.average(),
            topPerformerCode = "KNI-001",
            monthlyHistory = listOf(52.0, 55.1, 58.4, 60.8, 63.2, 65.0, 66.8, 68.4, 69.6, 71.0, 72.3, 73.1)
        )
    }

    fun getHistory(farm: Farm): List<DailyReading> {
        val baseStart = farm.currentLitresPerDay * 0.72
        return (1..90).map { day ->
            val trend = (farm.currentLitresPerDay - baseStart) * (day / 90.0)
            val noise = sin(day * 0.7) * 2.5
            val value = (baseStart + trend + noise).coerceAtLeast(5.0)
            DailyReading(day = day, litres = value, feedKg = value * 0.38)
        }
    }

    // Default SACCO for demo when none is selected
    val defaultSaccoId = "NYR"

    // The primary farmer for the farmer view demo
    val demoFarmerCode = "NYR-038"
}