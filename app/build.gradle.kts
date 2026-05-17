import java.util.Properties

// ── Auto-versioning ────────────────────────────────────────────────────────────
val versionPropsFile = rootProject.file("version.properties")

fun readVersionProps(): Properties = Properties().apply {
    if (versionPropsFile.exists()) load(versionPropsFile.inputStream())
}

val versionProps   = readVersionProps()
val appVersionCode = versionProps.getProperty("VERSION_CODE", "2").toInt()
val appVersionName = buildString {
    append(versionProps.getProperty("VERSION_MAJOR", "1"))
    append(".")
    append(versionProps.getProperty("VERSION_MINOR", "0"))
    append(".")
    append(versionProps.getProperty("VERSION_PATCH", "0"))
}

tasks.register("bumpVersionCode") {
    outputs.upToDateWhen { false }
    doLast {
        val props    = readVersionProps()
        val newCode  = props.getProperty("VERSION_CODE",  "2").toInt() + 1
        val newPatch = props.getProperty("VERSION_PATCH", "0").toInt() + 1
        props.setProperty("VERSION_CODE",  newCode.toString())
        props.setProperty("VERSION_PATCH", newPatch.toString())
        versionPropsFile.outputStream().use { props.store(it, "Auto-bumped") }
        println("📦 versionCode=$newCode  versionName=${props.getProperty("VERSION_MAJOR")}.${props.getProperty("VERSION_MINOR")}.$newPatch")
    }
}

afterEvaluate {
    tasks.matching { it.name == "assembleRelease" || it.name == "bundleRelease" }
        .configureEach { dependsOn("bumpVersionCode") }
}
// ──────────────────────────────────────────────────────────────────────────────

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.decisionpulse.demo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.decisionpulse.demo"
        minSdk = 26
        targetSdk = 35
        versionCode   = appVersionCode
        versionName   = appVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable        = true
            versionNameSuffix   = "-debug"
        }
    }

    // ── APK output naming ─────────────────────────────────────────────────────
    applicationVariants.all {
        val variant = this
        outputs.all {
            val out = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            out.outputFileName = when (variant.buildType.name) {
                "release" -> "DPulse-v${variant.versionName}.apk"
                else      -> "DPulse-v${variant.versionName}-${variant.buildType.name}.apk"
            }
        }
    }
    // ──────────────────────────────────────────────────────────────────────────

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures { 
        compose = true 
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
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