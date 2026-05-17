# DecisionPulse Demo App — Full Android Build Guide

> **Purpose:** A polished, animation-rich Android demo app for pitching DecisionPulse to dairy SACCOs and cooperative leadership in Nyeri County. Mock data, real design, genuinely impressive on-device.

---

## Table of Contents

1. [What We're Building](#1-what-were-building)
2. [Tech Stack Decisions](#2-tech-stack-decisions)
3. [Project Setup](#3-project-setup)
4. [Complete Directory Structure](#4-complete-directory-structure)
5. [Design System — Theme, Colors, Typography](#5-design-system)
6. [Mock Data Layer](#6-mock-data-layer)
7. [Navigation Graph](#7-navigation-graph)
8. [Reusable Components & Animations](#8-reusable-components--animations)
9. [Screen by Screen](#9-screen-by-screen)
10. [ViewModels](#10-viewmodels)
11. [MainActivity](#11-mainactivity)
12. [Build & Run Checklist](#12-build--run-checklist)
13. [Demo Script — How to Present It](#13-demo-script)

---

## 1. What We're Building

A **5-screen demo app** that shows:

| Screen | What It Shows |
|--------|--------------|
| **Splash** | Generative particle / data-stream animation. Branded intro. |
| **Dashboard** | Live SACCO overview — total litres today, farms on-track count, animated progress ring, quick-stat tiles that count up on entry. |
| **Farm Roster** | All member farms as swipeable cards — green / amber / red status. Live pulse animation on each. |
| **Farm Detail** | Per-farm trajectory chart drawing itself on entry, AI insight panel, 90-day history sparklines. |
| **AI Insights** | Feed of generative AI insight cards with typewriter animation — real-looking recommendations. |

**Context baked into the data:** Nyeri County. Dairy farmers. 12 farm codes. Target: 100 litres/day by end of 2027. SACCO coordinator persona. Every number is realistic.

---

## 2. Tech Stack Decisions

| Concern | Choice | Why |
|---------|--------|-----|
| UI | **Jetpack Compose** | Animations are first-class. Canvas API for custom charts. No XML. |
| Language | **Kotlin** | Only sensible choice with Compose. |
| Architecture | **MVVM (simplified)** | No Hilt for the demo — just `ViewModel` + `StateFlow`. Fast to build. |
| Charts | **Custom Canvas** | No external dependency. Full animation control. Looks bespoke. |
| Navigation | **Navigation Compose** | Single activity, type-safe routes. |
| Animations | **Compose Animation APIs** | `animateFloatAsState`, `InfiniteTransition`, `AnimatedVisibility`, `Canvas` + `PathMeasure`. |
| Theme | **Material 3 dark** | Looks premium on demo. Custom color scheme. |
| Min SDK | **API 26 (Android 8.0)** | Covers ~98% of field devices. |
| Target SDK | **API 34** | Current target. |

**No backend. No network calls. No Firebase. No Hilt.** Pure self-contained mock data. The app works offline, on any device, every time.

---

## 3. Project Setup

### 3.1 Android Studio

- Use **Android Studio Hedgehog (2023.1.1)** or newer.
- New Project → **Empty Activity** → Language: Kotlin → Min SDK: 26.
- Package name: `com.decisionpulse.demo`

---

### 3.2 `libs.versions.toml` (Version Catalog)

Place in `gradle/libs.versions.toml`:

```toml
[versions]
agp = "8.2.2"
kotlin = "1.9.22"
composeBom = "2024.04.01"
coreKtx = "1.13.0"
lifecycleRuntime = "2.7.0"
activityCompose = "1.9.0"
navigationCompose = "2.7.7"
coroutines = "1.8.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntime" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntime" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
compose-material-icons = { group = "androidx.compose.material", name = "material-icons-extended" }
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
kotlinx-coroutines = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

---

### 3.3 `build.gradle.kts` (Project root)

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
```

---

### 3.4 `build.gradle.kts` (App module)

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.decisionpulse.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.decisionpulse.demo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.coroutines)
    debugImplementation(libs.compose.ui.tooling)
}
```

---

## 4. Complete Directory Structure

```
app/
└── src/
    └── main/
        ├── AndroidManifest.xml
        └── java/com/decisionpulse/demo/
            │
            ├── MainActivity.kt
            │
            ├── ui/
            │   ├── theme/
            │   │   ├── Color.kt
            │   │   ├── Theme.kt
            │   │   └── Type.kt
            │   │
            │   ├── navigation/
            │   │   ├── NavGraph.kt
            │   │   └── Screen.kt
            │   │
            │   ├── components/
            │   │   ├── AnimatedRing.kt          ← progress ring, draws itself
            │   │   ├── TrajectoryChart.kt       ← line chart, path draws on entry
            │   │   ├── SparklineChart.kt        ← compact 7-day sparkline
            │   │   ├── FarmStatusCard.kt        ← card with pulsing status dot
            │   │   ├── CountingNumber.kt        ← animated number count-up
            │   │   ├── InsightCard.kt           ← typewriter AI insight
            │   │   ├── LiveMetricTile.kt        ← stat tile with shimmer
            │   │   ├── StatusBadge.kt           ← green/amber/red chip
            │   │   └── ParticleCanvas.kt        ← splash particle field
            │   │
            │   └── screens/
            │       ├── splash/
            │       │   └── SplashScreen.kt
            │       ├── dashboard/
            │       │   ├── DashboardScreen.kt
            │       │   └── DashboardViewModel.kt
            │       ├── farms/
            │       │   ├── FarmRosterScreen.kt
            │       │   └── FarmRosterViewModel.kt
            │       ├── farmdetail/
            │       │   ├── FarmDetailScreen.kt
            │       │   └── FarmDetailViewModel.kt
            │       └── insights/
            │           ├── InsightsScreen.kt
            │           └── InsightsViewModel.kt
            │
            └── data/
                ├── model/
                │   ├── Farm.kt
                │   ├── DailyReading.kt
                │   ├── SaccoSummary.kt
                │   └── AiInsight.kt
                └── mock/
                    └── MockRepository.kt
```

---

## 5. Design System

### `ui/theme/Color.kt`

```kotlin
package com.decisionpulse.demo.ui.theme

import androidx.compose.ui.graphics.Color

// Brand
val DPGreen       = Color(0xFF3ECF8E)
val DPGreenDim    = Color(0xFF1A5C40)
val DPAmber       = Color(0xFFF0C040)
val DPAmberDim    = Color(0xFF5C4A10)
val DPRed         = Color(0xFFF04060)
val DPRedDim      = Color(0xFF5C1020)
val DPBlue        = Color(0xFF4A9EFF)
val DPBlueDim     = Color(0xFF1A3A6C)
val DPPurple      = Color(0xFFA080F0)

// Backgrounds
val BgDeep        = Color(0xFF080A0F)
val BgSurface     = Color(0xFF0E1117)
val BgSurface2    = Color(0xFF13181F)
val BgSurface3    = Color(0xFF1A2030)
val Border        = Color(0xFF1E2535)
val Border2       = Color(0xFF28334A)

// Text
val TextPrimary   = Color(0xFFD8E0F0)
val TextSecondary = Color(0xFF8898B0)
val TextMuted     = Color(0xFF5A6A88)
val TextWhite     = Color(0xFFF0F4FF)
```

---

### `ui/theme/Type.kt`

```kotlin
package com.decisionpulse.demo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Using system font stack — no extra font assets needed for demo
val Typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        letterSpacing = (-1).sp,
        color = TextWhite
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp,
        color = TextWhite
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = TextWhite
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextSecondary,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = TextMuted
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.8.sp,
        color = TextMuted
    )
)
```

---

### `ui/theme/Theme.kt`

```kotlin
package com.decisionpulse.demo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary          = DPGreen,
    onPrimary        = BgDeep,
    primaryContainer = DPGreenDim,
    secondary        = DPBlue,
    onSecondary      = BgDeep,
    background       = BgDeep,
    surface          = BgSurface,
    surfaceVariant   = BgSurface2,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    error            = DPRed,
    outline          = Border2
)

@Composable
fun DecisionPulseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
```

---

## 6. Mock Data Layer

### `data/model/Farm.kt`

```kotlin
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
```

---

### `data/model/DailyReading.kt`

```kotlin
package com.decisionpulse.demo.data.model

data class DailyReading(
    val day: Int,       // 1–90
    val litres: Double,
    val feedKg: Double,
    val notes: String = ""
)
```

---

### `data/model/SaccoSummary.kt`

```kotlin
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
```

---

### `data/model/AiInsight.kt`

```kotlin
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
```

---

### `data/mock/MockRepository.kt`

```kotlin
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
```

---

## 7. Navigation Graph

### `ui/navigation/Screen.kt`

```kotlin
package com.decisionpulse.demo.ui.navigation

sealed class Screen(val route: String) {
    object Splash      : Screen("splash")
    object Dashboard   : Screen("dashboard")
    object FarmRoster  : Screen("farms")
    object FarmDetail  : Screen("farm/{farmCode}") {
        fun createRoute(farmCode: String) = "farm/$farmCode"
    }
    object Insights    : Screen("insights")
}
```

---

### `ui/navigation/NavGraph.kt`

```kotlin
package com.decisionpulse.demo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.decisionpulse.demo.ui.screens.dashboard.DashboardScreen
import com.decisionpulse.demo.ui.screens.farmdetail.FarmDetailScreen
import com.decisionpulse.demo.ui.screens.farms.FarmRosterScreen
import com.decisionpulse.demo.ui.screens.insights.InsightsScreen
import com.decisionpulse.demo.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onViewFarms = { navController.navigate(Screen.FarmRoster.route) },
                onViewInsights = { navController.navigate(Screen.Insights.route) }
            )
        }

        composable(Screen.FarmRoster.route) {
            FarmRosterScreen(
                onFarmSelected = { farmCode ->
                    navController.navigate(Screen.FarmDetail.createRoute(farmCode))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.FarmDetail.route,
            arguments = listOf(navArgument("farmCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val farmCode = backStackEntry.arguments?.getString("farmCode") ?: ""
            FarmDetailScreen(farmCode = farmCode, onBack = { navController.popBackStack() })
        }

        composable(Screen.Insights.route) {
            InsightsScreen(onBack = { navController.popBackStack() })
        }
    }
}
```

---

## 8. Reusable Components & Animations

> These are the visual centrepieces. Build these first — every screen uses them.

---

### `components/CountingNumber.kt`

Animates from 0 to target value on composition. Used for all live stat tiles.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlin.math.roundToInt

@Composable
fun CountingNumber(
    target: Double,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    suffix: String = "",
    decimals: Int = 0,
    durationMs: Int = 1200
) {
    var start by remember { mutableStateOf(false) }
    val animatedValue by animateFloatAsState(
        targetValue = if (start) target.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "CountingNumber"
    )

    LaunchedEffect(Unit) { start = true }

    val displayed = if (decimals == 0) {
        animatedValue.roundToInt().toString()
    } else {
        "%.${decimals}f".format(animatedValue)
    }

    Text(text = "$displayed$suffix", modifier = modifier, style = style, color = color)
}
```

---

### `components/AnimatedRing.kt`

A circular progress ring that sweeps from 0 to the target angle on entry. Used on the Dashboard as the main visual.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.decisionpulse.demo.ui.theme.*

@Composable
fun AnimatedRing(
    progress: Float,           // 0f to 1f
    modifier: Modifier = Modifier,
    trackColor: Color = Border2,
    progressColor: Color = DPGreen,
    strokeWidth: Float = 24f,
    durationMs: Int = 1400
) {
    var started by remember { mutableStateOf(false) }
    val sweep by animateFloatAsState(
        targetValue = if (started) progress * 300f else 0f,   // 300° max arc
        animationSpec = tween(
            durationMillis = durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "RingSweep"
    )

    LaunchedEffect(Unit) { started = true }

    Canvas(modifier = modifier) {
        val inset = strokeWidth / 2
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = Offset(inset, inset)
        val startAngle = 120f

        // Track (background)
        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = 300f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress
        if (sweep > 0f) {
            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}
```

---

### `components/TrajectoryChart.kt`

The signature animation: a line chart whose path draws itself from left to right on screen entry. 90 days of data. Target line shown as dashed.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.theme.*

@Composable
fun TrajectoryChart(
    readings: List<Double>,     // ordered chronologically
    target: Double = 100.0,
    modifier: Modifier = Modifier,
    lineColor: Color = DPGreen,
    strokeWidth: Dp = 2.5.dp,
    showGradient: Boolean = true,
    durationMs: Int = 1800
) {
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(
            durationMillis = durationMs,
            easing = FastOutSlowInEasing
        ),
        label = "ChartDraw"
    )

    LaunchedEffect(Unit) { started = true }

    if (readings.isEmpty()) return

    Canvas(modifier = modifier) {
        val maxVal = maxOf(readings.max(), target) * 1.08
        val minVal = readings.min() * 0.88
        val range = maxVal - minVal

        val w = size.width
        val h = size.height

        fun xOf(index: Int) = w * index / (readings.size - 1)
        fun yOf(v: Double) = h - ((v - minVal) / range * h).toFloat()

        // Target dashed line
        val targetY = yOf(target)
        val dashWidth = 12f; val dashGap = 8f
        var x = 0f
        while (x < w) {
            drawLine(
                color = DPAmber.copy(alpha = 0.45f),
                start = Offset(x, targetY),
                end = Offset(minOf(x + dashWidth, w), targetY),
                strokeWidth = 1.5.dp.toPx()
            )
            x += dashWidth + dashGap
        }

        // Build full path
        val fullPath = Path().apply {
            readings.forEachIndexed { i, v ->
                val px = xOf(i); val py = yOf(v)
                if (i == 0) moveTo(px, py) else lineTo(px, py)
            }
        }

        // Clip to animated progress (left → right reveal)
        val pm = PathMeasure()
        pm.setPath(fullPath, false)
        val totalLength = pm.length
        val clippedPath = Path()
        pm.getSegment(0f, totalLength * progress, clippedPath, true)

        // Gradient fill under line
        if (showGradient) {
            val fillPath = Path().apply {
                addPath(clippedPath)
                // close down to bottom
                val lastIdx = (readings.size * progress).toInt().coerceAtMost(readings.size - 1)
                lineTo(xOf(lastIdx), h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0f))
                )
            )
        }

        // Chart line
        drawPath(
            path = clippedPath,
            color = lineColor,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
```

---

### `components/SparklineChart.kt`

Compact 7-day trend line for farm roster cards.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPRed

@Composable
fun SparklineChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    durationMs: Int = 900
) {
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMs, easing = FastOutSlowInEasing),
        label = "Sparkline"
    )
    LaunchedEffect(Unit) { started = true }
    if (data.size < 2) return

    val trending = data.last() >= data.first()
    val color = if (trending) DPGreen else DPRed

    Canvas(modifier = modifier) {
        val max = data.max(); val min = data.min()
        val range = (max - min).takeIf { it > 0 } ?: 1.0
        val path = Path().apply {
            data.forEachIndexed { i, v ->
                val x = size.width * i / (data.size - 1)
                val y = size.height - ((v - min) / range * size.height).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        val pm = PathMeasure()
        pm.setPath(path, false)
        val seg = Path()
        pm.getSegment(0f, pm.length * progress, seg, true)
        drawPath(seg, color, style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
```

---

### `components/FarmStatusCard.kt`

Card with pulsing status indicator and sparkline. Tappable.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.data.model.Farm
import com.decisionpulse.demo.data.model.FarmStatus
import com.decisionpulse.demo.ui.theme.*

@Composable
fun FarmStatusCard(
    farm: Farm,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusColor, statusLabel) = when (farm.status) {
        FarmStatus.ON_TRACK -> DPGreen to "On Track"
        FarmStatus.WATCH    -> DPAmber to "Watch"
        FarmStatus.CRITICAL -> DPRed   to "Critical"
    }

    // Pulsing dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface2),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border2)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status pulse dot
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.22f))
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(farm.code, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    StatusBadge(label = statusLabel, color = statusColor)
                }
                Spacer(Modifier.height(2.dp))
                Text(farm.subLocation, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "%.1fL/day".format(farm.currentLitresPerDay),
                            style = MaterialTheme.typography.headlineMedium,
                            color = statusColor
                        )
                        Text(
                            "Target: %.0fL — %.0f%%".format(
                                farm.targetLitresPerDay,
                                farm.progressPercent * 100
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    SparklineChart(
                        data = farm.weeklyHistory,
                        modifier = Modifier.size(width = 72.dp, height = 36.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Linear progress bar
                LinearProgressIndicator(
                    progress = { farm.progressPercent },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = statusColor,
                    trackColor = Border2
                )
            }
        }
    }
}
```

---

### `components/StatusBadge.kt`

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(label: String, color: Color) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 7.dp, vertical = 3.dp)
    )
}
```

---

### `components/InsightCard.kt`

AI insight card with typewriter body animation.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.data.model.AiInsight
import com.decisionpulse.demo.data.model.InsightPriority
import com.decisionpulse.demo.data.model.InsightType
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun InsightCard(insight: AiInsight, modifier: Modifier = Modifier) {
    val priorityColor = when (insight.priority) {
        InsightPriority.HIGH   -> DPRed
        InsightPriority.MEDIUM -> DPAmber
        InsightPriority.LOW    -> DPGreen
    }
    val icon: ImageVector = when (insight.type) {
        InsightType.ALERT       -> Icons.Filled.Warning
        InsightType.COST        -> Icons.Filled.MonetizationOn
        InsightType.PERFORMANCE -> Icons.Filled.TrendingUp
        InsightType.OPPORTUNITY -> Icons.Filled.Lightbulb
    }

    // Typewriter animation
    var displayedBody by remember { mutableStateOf("") }
    LaunchedEffect(insight.id) {
        displayedBody = ""
        insight.body.forEachIndexed { i, _ ->
            delay(18L)
            displayedBody = insight.body.substring(0, i + 1)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface2),
        border = androidx.compose.foundation.BorderStroke(1.dp, priorityColor.copy(alpha = 0.28f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(priorityColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = priorityColor, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(insight.title, style = MaterialTheme.typography.titleMedium)
                    insight.farmCode?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = DPBlue)
                    } ?: Text("SACCO-wide", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
                StatusBadge(
                    label = insight.priority.name,
                    color = priorityColor
                )
            }

            Spacer(Modifier.height(12.dp))
            Text(displayedBody, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(10.dp))

            Divider(color = Border, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ArrowForward, contentDescription = null,
                    tint = DPGreen, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(insight.action, style = MaterialTheme.typography.bodySmall, color = DPGreen)
                Spacer(Modifier.weight(1f))
                Text("${insight.hoursAgo}h ago", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
```

---

### `components/LiveMetricTile.kt`

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.theme.*

@Composable
fun LiveMetricTile(
    label: String,
    value: Double,
    suffix: String = "",
    decimals: Int = 0,
    accent: Color = DPGreen,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgSurface2)
            .padding(14.dp)
    ) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(6.dp))
        CountingNumber(
            target = value,
            suffix = suffix,
            decimals = decimals,
            style = MaterialTheme.typography.headlineLarge,
            color = accent
        )
    }
}
```

---

### `components/ParticleCanvas.kt`

Animated particle field for the splash screen.

```kotlin
package com.decisionpulse.demo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.decisionpulse.demo.ui.theme.DPGreen
import com.decisionpulse.demo.ui.theme.DPBlue
import kotlin.math.*
import kotlin.random.Random

private data class Particle(
    val startX: Float, val startY: Float,
    val vx: Float, val vy: Float,
    val radius: Float, val color: Color,
    val phase: Float  // offset for sin wave motion
)

@Composable
fun ParticleCanvas(modifier: Modifier = Modifier, particleCount: Int = 55) {
    val particles = remember {
        List(particleCount) {
            val colors = listOf(DPGreen, DPBlue, DPGreen.copy(alpha = .6f), DPBlue.copy(alpha = .5f))
            Particle(
                startX = Random.nextFloat(),
                startY = Random.nextFloat(),
                vx = (Random.nextFloat() - 0.5f) * 0.12f,
                vy = (Random.nextFloat() - 0.5f) * 0.12f,
                radius = Random.nextFloat() * 3.5f + 1f,
                color = colors.random(),
                phase = Random.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16_000, easing = LinearEasing)
        ),
        label = "time"
    )

    Canvas(modifier = modifier) {
        particles.forEach { p ->
            val t = time * 0.002f
            val x = ((p.startX + p.vx * t + sin(t * 0.7f + p.phase) * 0.04f) % 1f + 1f) % 1f
            val y = ((p.startY + p.vy * t + cos(t * 0.5f + p.phase) * 0.04f) % 1f + 1f) % 1f
            drawCircle(
                color = p.color,
                radius = p.radius,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}
```

---

## 9. Screen by Screen

### `screens/splash/SplashScreen.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.decisionpulse.demo.ui.components.ParticleCanvas
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "SplashAlpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated particles filling background
        ParticleCanvas(modifier = Modifier.fillMaxSize().alpha(0.6f))

        Column(
            modifier = Modifier.fillMaxSize().alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "DecisionPulse",
                style = MaterialTheme.typography.displayLarge,
                color = TextWhite
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "AI-Powered Agricultural Intelligence",
                style = MaterialTheme.typography.titleMedium,
                color = DPGreen
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Nyeri County · Dairy SACCO Demo",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}
```

---

### `screens/dashboard/DashboardScreen.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.ui.components.*
import com.decisionpulse.demo.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onViewFarms: () -> Unit,
    onViewInsights: () -> Unit,
    vm: DashboardViewModel = viewModel()
) {
    val summary by vm.summary.collectAsState()
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        show = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        AnimatedVisibility(show, enter = fadeIn() + slideInVertically { -30 }) {
            Column {
                Text("Good morning,", style = MaterialTheme.typography.bodyLarge)
                Text(summary.name, style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Nyeri County Dairy SACCO · Live Dashboard",
                    style = MaterialTheme.typography.bodySmall,
                    color = DPGreen
                )
            }
        }

        // Main ring + today litres
        AnimatedVisibility(show, enter = fadeIn(tween(600))) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                AnimatedRing(
                    progress = summary.monthlyProgressPercent,
                    modifier = Modifier.size(210.dp),
                    progressColor = DPGreen,
                    strokeWidth = 26f
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CountingNumber(
                        target = summary.totalLitresToday,
                        decimals = 1,
                        suffix = "L",
                        style = MaterialTheme.typography.displayLarge,
                        color = DPGreen
                    )
                    Text("litres today", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "%.0f%% of monthly target".format(summary.monthlyProgressPercent * 100),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Stat tiles row
        AnimatedVisibility(show, enter = fadeIn(tween(800))) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveMetricTile(
                    label = "On Track", value = summary.farmsOnTrack.toDouble(),
                    accent = DPGreen, modifier = Modifier.weight(1f)
                )
                LiveMetricTile(
                    label = "Watching", value = summary.farmsWatch.toDouble(),
                    accent = DPAmber, modifier = Modifier.weight(1f)
                )
                LiveMetricTile(
                    label = "Critical", value = summary.farmsCritical.toDouble(),
                    accent = DPRed, modifier = Modifier.weight(1f)
                )
            }
        }

        // Cost per litre
        AnimatedVisibility(show, enter = fadeIn(tween(900))) {
            LiveMetricTile(
                label = "Avg Feed Cost / Litre",
                value = summary.avgFeedCostPerLitre,
                suffix = " KES",
                decimals = 2,
                accent = DPBlue,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // CTA Buttons
        AnimatedVisibility(show, enter = fadeIn(tween(1000))) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onViewFarms,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DPGreenDim)
                ) {
                    Icon(Icons.Filled.Agriculture, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("View All Farms", color = DPGreen)
                }
                OutlinedButton(
                    onClick = onViewInsights,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DPBlue.copy(alpha = 0.4f))
                ) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = DPBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("AI Insights", color = DPBlue)
                }
            }
        }
    }
}
```

---

### `screens/farms/FarmRosterScreen.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.farms

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.ui.components.FarmStatusCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmRosterScreen(
    onFarmSelected: (String) -> Unit,
    onBack: () -> Unit,
    vm: FarmRosterViewModel = viewModel()
) {
    val farms by vm.farms.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Farm Roster") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            itemsIndexed(farms) { index, farm ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 80L)
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(tween(350))
                ) {
                    FarmStatusCard(farm = farm, onClick = { onFarmSelected(farm.code) })
                }
            }
        }
    }
}
```

---

### `screens/farmdetail/FarmDetailScreen.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.farmdetail

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.data.model.FarmStatus
import com.decisionpulse.demo.ui.components.*
import com.decisionpulse.demo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmDetailScreen(
    farmCode: String,
    onBack: () -> Unit
) {
    val vm: FarmDetailViewModel = viewModel()
    LaunchedEffect(farmCode) { vm.load(farmCode) }

    val farm by vm.farm.collectAsState()
    val history by vm.history.collectAsState()

    val farm_ = farm ?: return

    val statusColor = when (farm_.status) {
        FarmStatus.ON_TRACK -> DPGreen
        FarmStatus.WATCH    -> DPAmber
        FarmStatus.CRITICAL -> DPRed
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(farm_.code) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Sub-location + status
            Row {
                Text(farm_.subLocation, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                StatusBadge(label = farm_.status.name, color = statusColor)
            }

            // Big number
            CountingNumber(
                target = farm_.currentLitresPerDay,
                suffix = "L/day",
                decimals = 1,
                style = MaterialTheme.typography.displayLarge,
                color = statusColor
            )
            Text(
                farm_.projectedDate,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )

            // 90-day trajectory chart
            Text("90-Day Trajectory", style = MaterialTheme.typography.titleMedium)
            Card(
                colors = CardDefaults.cardColors(containerColor = BgSurface2),
                modifier = Modifier.fillMaxWidth()
            ) {
                TrajectoryChart(
                    readings = history.map { it.litres },
                    target = farm_.targetLitresPerDay,
                    lineColor = statusColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(12.dp)
                )
            }

            // Progress ring + key stats side by side
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(modifier = Modifier.size(120.dp)) {
                    AnimatedRing(
                        progress = farm_.progressPercent,
                        modifier = Modifier.fillMaxSize(),
                        progressColor = statusColor,
                        strokeWidth = 16f
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text("%.0f%%".format(farm_.progressPercent * 100),
                            style = MaterialTheme.typography.headlineMedium, color = statusColor)
                        Text("to goal", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LiveMetricTile("Feed Cost/L", farm_.feedCostPerLitre, " KES", decimals = 2, accent = DPBlue, modifier = Modifier.fillMaxWidth())
                    LiveMetricTile("Consistency", farm_.consistencyScore.toDouble(), "/100", accent = DPPurple, modifier = Modifier.fillMaxWidth())
                }
            }

            // 7-day sparkline
            Text("Last 7 Days", style = MaterialTheme.typography.titleMedium)
            Card(colors = CardDefaults.cardColors(containerColor = BgSurface2)) {
                SparklineChart(
                    data = farm_.weeklyHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(12.dp)
                )
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}
```

---

### `screens/insights/InsightsScreen.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.insights

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.decisionpulse.demo.ui.components.InsightCard
import com.decisionpulse.demo.ui.theme.DPGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onBack: () -> Unit,
    vm: InsightsViewModel = viewModel()
) {
    val insights by vm.insights.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Insights") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Text(
                    "Generated based on 60-day farm dataset · Nyeri Dairy SACCO",
                    style = MaterialTheme.typography.bodySmall,
                    color = DPGreen
                )
                Spacer(Modifier.height(4.dp))
            }
            itemsIndexed(insights) { index, insight ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 150L)
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(tween(400))
                ) {
                    InsightCard(insight = insight)
                }
            }
        }
    }
}
```

---

## 10. ViewModels

### `screens/dashboard/DashboardViewModel.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.SaccoSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {
    private val _summary = MutableStateFlow(MockRepository.saccoSummary)
    val summary: StateFlow<SaccoSummary> = _summary
}
```

---

### `screens/farms/FarmRosterViewModel.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.farms

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.Farm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FarmRosterViewModel : ViewModel() {
    private val _farms = MutableStateFlow(MockRepository.farms)
    val farms: StateFlow<List<Farm>> = _farms
}
```

---

### `screens/farmdetail/FarmDetailViewModel.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.farmdetail

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.DailyReading
import com.decisionpulse.demo.data.model.Farm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FarmDetailViewModel : ViewModel() {
    private val _farm = MutableStateFlow<Farm?>(null)
    val farm: StateFlow<Farm?> = _farm

    private val _history = MutableStateFlow<List<DailyReading>>(emptyList())
    val history: StateFlow<List<DailyReading>> = _history

    fun load(code: String) {
        val f = MockRepository.getFarmByCode(code)
        _farm.value = f
        _history.value = f?.let { MockRepository.getHistory(it) } ?: emptyList()
    }
}
```

---

### `screens/insights/InsightsViewModel.kt`

```kotlin
package com.decisionpulse.demo.ui.screens.insights

import androidx.lifecycle.ViewModel
import com.decisionpulse.demo.data.mock.MockRepository
import com.decisionpulse.demo.data.model.AiInsight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InsightsViewModel : ViewModel() {
    private val _insights = MutableStateFlow(MockRepository.insights)
    val insights: StateFlow<List<AiInsight>> = _insights
}
```

---

## 11. `MainActivity.kt`

```kotlin
package com.decisionpulse.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.decisionpulse.demo.ui.navigation.NavGraph
import com.decisionpulse.demo.ui.theme.BgDeep
import com.decisionpulse.demo.ui.theme.DecisionPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DecisionPulseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgDeep
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
```

---

### `AndroidManifest.xml` (minimum needed)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="false"
        android:label="DecisionPulse"
        android:theme="@style/Theme.AppCompat.NoActionBar"
        android:exported="true">

        <activity
            android:name=".MainActivity"
            android:windowSoftInputMode="adjustResize"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>
</manifest>
```

---

## 12. Build & Run Checklist

```
□  Android Studio → File → New → New Project → Empty Activity
□  Set package: com.decisionpulse.demo   Min SDK: 26
□  Replace gradle/libs.versions.toml with the version catalog above
□  Replace build.gradle.kts (app) with the one above
□  Sync project (File → Sync Project with Gradle Files)
□  Create the full directory tree under java/com/decisionpulse/demo/
□  Paste each file in order: Theme → Models → MockRepository → Components → ViewModels → Screens → NavGraph → MainActivity
□  Build → Make Project (Ctrl+F9 / Cmd+F9). Fix any import mismatches.
□  Run on a physical device via USB — animations always look better on device than emulator
□  Set screen brightness to max before demo
□  Turn on Do Not Disturb before any in-person demo
```

**If you get a Compose BOM version conflict:**
Change `composeBom = "2024.04.01"` in `libs.versions.toml` to `"2024.02.00"` (more stable).

**If animations look janky on emulator:**
That's normal. Run on device. The Canvas path animations (TrajectoryChart, SparklineChart) need real GPU acceleration to be smooth.

---

## 13. Demo Script

> How to present this in the field. Every screen has a purpose.

### 1 — Splash (do nothing)
Hand them the phone *after* you open the app. Let the particle animation and branding land before you say a word. 3 seconds. Let them see "AI-Powered Agricultural Intelligence — Nyeri County." They'll already be curious.

### 2 — Dashboard
**Say:** *"This is what a SACCO coordinator sees every morning. Today this SACCO has collected [litres] litres across 12 farms. Seven are on track for 100 litres by 2027. Two need attention."*

Point at the ring animating: *"That ring shows monthly progress. It draws itself as the data loads — live."*

Point at the stat tiles counting up: **don't explain the animation. just watch them watch it.**

### 3 — Farm Roster
Tap "View All Farms". Let the cards animate in one by one (staggered).

**Say:** *"Each card shows one farmer's current production, their 7-day trend line, and their status. Green means on track. Red means the coordinator needs to act."*

Point at a red one: *"NYR-019. Declining. The system already flagged it."*

Let them scroll. Don't rush.

### 4 — Farm Detail
Tap NYR-038 (the top performer, 95.8L/day).

**Watch:** the trajectory chart draws its path from left to right automatically. Most people in a demo stop talking here. Let the animation work.

**Say:** *"90 days of data. The dotted line is the 100-litre target. You can see the growth curve. Feed cost per litre, consistency score, weekly trend — everything the coordinator needs to advise this farmer. No guesswork."*

Then tap NYR-019 (the critical farm). Show the contrast.

### 5 — AI Insights
Go back, tap AI Insights. Let the cards slide in and the typewriter text animate.

**Say:** *"The system reads the patterns across all farms and surfaces recommendations. NYR-019 is declining and the AI flagged it's consistent with what happened in Mathira East last quarter when a Napier fodder intervention worked. That's not something a coordinator would know from one farm's numbers alone."*

**Pause. Don't sell. Let them ask questions.**

---

### The Golden Rule

**Hand them the phone and stop talking.**

The best pitch moment is silence while they navigate themselves. They'll swipe between farms, tap on numbers, scroll the insights. That's the product selling itself. Your job is to answer what they discover, not describe what they're looking at.

---
