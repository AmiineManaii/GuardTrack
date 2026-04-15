# 🛡️ GuardianTrack — Fichier d'Implémentation Complet (Jetpack Compose)

> **Mini-Projet Android Natif Avancé (Kotlin) — BONUS +5 pts**
> ISET Rades — Master Professionnel DAM — Mehdi M'tir
> UI 100% Jetpack Compose — Zéro XML de layout
> Deadline : **Samedi 18 Avril 2026**

---

## 📋 TABLE DES MATIÈRES

1. [Vision du Projet & Concept Design](#1-vision-du-projet--concept-design)
2. [Structure du Projet — Arborescence Complète](#2-structure-du-projet--arborescence-complète)
3. [Configuration Gradle & Dépendances Compose](#3-configuration-gradle--dépendances-compose)
4. [Design System Compose — Theme, Colors, Typography](#4-design-system-compose--theme-colors-typography)
5. [Architecture MVVM — Vue d'ensemble](#5-architecture-mvvm--vue-densemble)
6. [Couche Model — Entités, DTO, DomainModel](#6-couche-model--entités-dto-domainmodel)
7. [Room Database](#7-room-database)
8. [DataStore Preferences](#8-datastore-preferences)
9. [Retrofit & API](#9-retrofit--api)
10. [Injection de Dépendances — Hilt Modules](#10-injection-de-dépendances--hilt-modules)
11. [Repositories](#11-repositories)
12. [ViewModels](#12-viewmodels)
13. [Navigation Compose — AppNavigation](#13-navigation-compose--appnavigation)
14. [MainActivity — Point d'entrée Compose](#14-mainactivity--point-dentrée-compose)
15. [Screen 1 — DashboardScreen](#15-screen-1--dashboardscreen)
16. [Screen 2 — HistoryScreen](#16-screen-2--historyscreen)
17. [Screen 3 — SettingsScreen](#17-screen-3--settingsscreen)
18. [Composants Réutilisables — UI Kit](#18-composants-réutilisables--ui-kit)
19. [Animations Material 3 — Catalogue complet](#19-animations-material-3--catalogue-complet)
20. [SurveillanceService — Foreground Service](#20-surveillanceservice--foreground-service)
21. [BroadcastReceivers](#21-broadcastreceivers)
22. [ContentProvider](#22-contentprovider)
23. [WorkManager — Synchronisation Différée](#23-workmanager--synchronisation-différée)
24. [Permissions Dynamiques — Compose Style](#24-permissions-dynamiques--compose-style)
25. [Sécurité — EncryptedSharedPreferences](#25-sécurité--encryptedsharedpreferences)
26. [Export CSV/TXT — Scoped Storage](#26-export-csvtxt--scoped-storage)
27. [SMS Manager — Mode Simulation](#27-sms-manager--mode-simulation)
28. [AndroidManifest.xml Complet](#28-androidmanifestxml-complet)
29. [Rapport Technique — Réponses aux 6 Questions](#29-rapport-technique--réponses-aux-6-questions)
30. [README.md du Projet](#30-readmemd-du-projet)
31. [Checklist Finale](#31-checklist-finale)

---

## 1. Vision du Projet & Concept Design

### 🎨 Direction Artistique : "Tactical Dark Glass — Compose Edition"

GuardianTrack est un **instrument de surveillance de précision** — cockpit d'avion de chasse miniaturisé dans votre poche. Avec Compose, on pousse le design encore plus loin : chaque pixel est animé, chaque transition est orchestrée, chaque état UI est fluide.

#### Palette de Couleurs (Compose ColorScheme)
```
BgDeep          : Color(0xFF090E1A)   — noir nuit profonde
BgSurface       : Color(0xFF111827)   — ardoise marine
BgCard          : Color(0xFF1C2537)   — verre givré sombre
BgCardElevated  : Color(0xFF243044)   — card surélevée
CyanElectric    : Color(0xFF00D4FF)   — signal vivant (primary)
CyanDim         : Color(0x3300D4FF)   — halo cyan
RedAlert        : Color(0xFFFF3B5C)   — alerte critique (error)
AmberWarning    : Color(0xFFFFB547)   — attention (tertiary)
GreenSecure     : Color(0xFF00E5A0)   — sécurisé
TextPrimary     : Color(0xFFE8F4FF)   — blanc laiteux
TextSecondary   : Color(0xFF6B8AA8)   — bleu gris
BorderSubtle    : Color(0x1A00D4FF)   — halo subtil
```

#### Typographie Compose
```kotlin
// Rajdhani Bold   → Display, titres principaux (impact militaire)
// DM Mono Regular → Données numériques (magnitude, coords, timestamp)
// Inter Tight     → Labels, corps de texte
```

#### Signature Visuelle Compose — Ce qui rend l'app inoubliable
- **Radar animé** sur le Dashboard : cercles concentriques `Canvas` qui pulsent avec `rememberInfiniteTransition`
- **Chiffres animés** : magnitude accéléromètre avec `animateFloatAsState` + spring physics
- **Cards avec scan line** : bande lumineuse qui descend via `DrawScope` + animation infinie
- **Entrées en fondu décalé** : chaque carte arrive avec `AnimatedVisibility` + `slideInVertically` avec stagger
- **Bouton d'alerte** : ripple personnalisé rouge + pulsation `scale` continue
- **Transition entre screens** : `fadeIn + scaleIn` Material 3
- **SnackBar** stylisé avec token couleur Compose
- **SwipeToDismiss** natif Compose pour supprimer les incidents

---

## 2. Structure du Projet — Arborescence Complète

```
GuardianTrack/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/guardian/track/
│           │
│           ├── GuardianTrackApplication.kt
│           │
│           ├── data/
│           │   ├── local/
│           │   │   ├── db/
│           │   │   │   ├── GuardianDatabase.kt
│           │   │   │   ├── dao/
│           │   │   │   │   ├── IncidentDao.kt
│           │   │   │   │   └── EmergencyContactDao.kt
│           │   │   │   └── entity/
│           │   │   │       ├── IncidentEntity.kt
│           │   │   │       └── EmergencyContactEntity.kt
│           │   │   ├── datastore/
│           │   │   │   └── UserPreferencesDataStore.kt
│           │   │   └── security/
│           │   │       └── EncryptedPreferencesManager.kt
│           │   ├── remote/
│           │   │   ├── api/
│           │   │   │   ├── GuardianApi.kt
│           │   │   │   └── dto/IncidentDto.kt
│           │   │   └── interceptor/ApiKeyInterceptor.kt
│           │   └── repository/
│           │       ├── IncidentRepositoryImpl.kt
│           │       └── ContactRepositoryImpl.kt
│           │
│           ├── domain/
│           │   ├── model/
│           │   │   ├── Incident.kt
│           │   │   ├── EmergencyContact.kt
│           │   │   └── IncidentType.kt
│           │   └── util/
│           │       ├── NetworkResult.kt
│           │       └── Mapper.kt
│           │
│           ├── service/
│           │   └── SurveillanceService.kt
│           │
│           ├── receiver/
│           │   ├── BatteryReceiver.kt
│           │   └── BootReceiver.kt
│           │
│           ├── provider/
│           │   └── EmergencyContactProvider.kt
│           │
│           ├── worker/
│           │   ├── SyncWorker.kt
│           │   └── BootStartWorker.kt
│           │
│           ├── ui/
│           │   ├── MainActivity.kt                    ← setContent { GuardianApp() }
│           │   ├── navigation/
│           │   │   ├── AppNavigation.kt               ← NavHost Compose
│           │   │   └── Screen.kt                      ← Sealed routes
│           │   ├── theme/
│           │   │   ├── GuardianTheme.kt               ← MaterialTheme
│           │   │   ├── Color.kt
│           │   │   ├── Typography.kt
│           │   │   └── Shape.kt
│           │   ├── components/
│           │   │   ├── GlassCard.kt                   ← Card glassmorphique
│           │   │   ├── RadarPulse.kt                  ← Canvas animé
│           │   │   ├── SevenSegmentDisplay.kt         ← Magnitude style LCD
│           │   │   ├── ScanLineCard.kt                ← Card avec scan line
│           │   │   ├── AlertButton.kt                 ← Bouton pulsation rouge
│           │   │   ├── StatusBadge.kt                 ← Badge sync/actif
│           │   │   ├── IncidentItem.kt                ← Item liste swipeable
│           │   │   ├── PermissionRationaleDialog.kt   ← Dialog permission
│           │   │   └── BottomNavBar.kt                ← Navigation bar stylisée
│           │   ├── dashboard/
│           │   │   ├── DashboardScreen.kt
│           │   │   └── DashboardViewModel.kt
│           │   ├── history/
│           │   │   ├── HistoryScreen.kt
│           │   │   └── HistoryViewModel.kt
│           │   └── settings/
│           │       ├── SettingsScreen.kt
│           │       └── SettingsViewModel.kt
│           │
│           ├── util/
│           │   ├── PermissionManager.kt
│           │   ├── SmsHelper.kt
│           │   ├── ExportHelper.kt
│           │   └── NotificationHelper.kt
│           │
│           └── di/
│               ├── DatabaseModule.kt
│               ├── NetworkModule.kt
│               ├── RepositoryModule.kt
│               └── LocationModule.kt
│
├── local.properties          ← NON VERSIONNÉ
├── .gitignore
└── README.md
```

> ✅ **Zéro fichier XML dans `res/layout/`** — Tout est Compose.
> Les seuls XML conservés : `AndroidManifest.xml`, `res/values/strings.xml`, `res/values/colors.xml` (pour les couleurs système), et les ressources non-layout (drawables vectoriels pour les icônes de notification).

---

## 3. Configuration Gradle & Dépendances Compose

### `build.gradle.kts` (Project level)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false   // ← Compose compiler plugin
}
```

### `build.gradle.kts` (App level)
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)    // ← Obligatoire Kotlin 2.0+
    id("kotlin-parcelize")
}

android {
    namespace   = "com.guardian.track"
    compileSdk  = 35

    defaultConfig {
        applicationId = "com.guardian.track"
        minSdk        = 26
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"

        val localProperties = java.util.Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) load(file.inputStream())
        }
        buildConfigField("String", "API_BASE_URL",
            "\"${localProperties.getProperty("API_BASE_URL", "https://mockapi.io/")}\"")
        buildConfigField("String", "API_KEY",
            "\"${localProperties.getProperty("API_KEY", "")}\"")
    }

    buildFeatures {
        compose     = true     // ← Active Compose
        buildConfig = true
        // viewBinding = false  ← On ne l'active PAS (bonus Compose)
    }

    // Compose compiler — version gérée par le plugin alias
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    // ── Compose BOM (gère toutes les versions Compose) ────────
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // ── Compose Core ─────────────────────────────────────────
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)  // Icônes MD3 étendues
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)

    // ── Navigation Compose ────────────────────────────────────
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // ── Activity Compose ──────────────────────────────────────
    implementation(libs.androidx.activity.compose)

    // ── Lifecycle Compose ─────────────────────────────────────
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)        // collectAsStateWithLifecycle

    // ── Core Android ─────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)                        // Pour les receivers/services

    // ── Hilt ─────────────────────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // ── Room ─────────────────────────────────────────────────
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ── DataStore ────────────────────────────────────────────
    implementation(libs.androidx.datastore.preferences)

    // ── WorkManager ──────────────────────────────────────────
    implementation(libs.androidx.work.runtime.ktx)

    // ── Retrofit + OkHttp ────────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // ── Location ─────────────────────────────────────────────
    implementation(libs.play.services.location)

    // ── Security ─────────────────────────────────────────────
    implementation(libs.androidx.security.crypto)

    // ── Coroutines ───────────────────────────────────────────
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // ── Fonts Google (Rajdhani, DM Mono) ─────────────────────
    implementation(libs.androidx.compose.ui.text.google.fonts)

    // ── Debug outils Compose ──────────────────────────────────
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ── Tests ────────────────────────────────────────────────
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
```

### `local.properties` (NON VERSIONNÉ — dans .gitignore)
```properties
sdk.dir=/Users/VOTRE_NOM/Library/Android/sdk
API_BASE_URL=https://YOUR_PROJECT.mockapi.io/
API_KEY=your_secret_api_key_here
```

---

## 4. Design System Compose — Theme, Colors, Typography

### `ui/theme/Color.kt`
```kotlin
package com.guardian.track.ui.theme

import androidx.compose.ui.graphics.Color

// ── GuardianTrack Design Tokens ──────────────────────────────────

// Backgrounds
val BgDeep        = Color(0xFF090E1A)
val BgSurface     = Color(0xFF111827)
val BgCard        = Color(0xFF1C2537)
val BgCardElev    = Color(0xFF243044)

// Accents principaux
val CyanElectric  = Color(0xFF00D4FF)
val CyanDim       = Color(0x3300D4FF)
val CyanGlow      = Color(0x1A00D4FF)
val RedAlert      = Color(0xFFFF3B5C)
val RedDim        = Color(0x33FF3B5C)
val AmberWarn     = Color(0xFFFFB547)
val AmberDim      = Color(0x33FFB547)
val GreenSecure   = Color(0xFF00E5A0)
val GreenDim      = Color(0x3300E5A0)

// Texte
val TextPrimary   = Color(0xFFE8F4FF)
val TextSecondary = Color(0xFF6B8AA8)
val TextDisabled  = Color(0xFF3A4A5C)

// Bordures
val BorderSubtle  = Color(0x1A00D4FF)
val BorderActive  = Color(0x4D00D4FF)
val Divider       = Color(0x1AFFFFFF)

// Transparent
val Transparent   = Color(0x00000000)
```

### `ui/theme/Typography.kt`
```kotlin
package com.guardian.track.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.guardian.track.R

// ── Google Fonts via Compose ──────────────────────────────────────
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

val RajdhaniFamily = FontFamily(
    Font(GoogleFont("Rajdhani"), provider, FontWeight.Normal),
    Font(GoogleFont("Rajdhani"), provider, FontWeight.SemiBold),
    Font(GoogleFont("Rajdhani"), provider, FontWeight.Bold),
)

val DmMonoFamily = FontFamily(
    Font(GoogleFont("DM Mono"), provider, FontWeight.Normal),
    Font(GoogleFont("DM Mono"), provider, FontWeight.Medium),
)

val InterTightFamily = FontFamily(
    Font(GoogleFont("Inter Tight"), provider, FontWeight.Normal),
    Font(GoogleFont("Inter Tight"), provider, FontWeight.Medium),
    Font(GoogleFont("Inter Tight"), provider, FontWeight.SemiBold),
)

// ── Typography Material 3 ─────────────────────────────────────────
val GuardianTypography = Typography(
    // Titres principaux — Rajdhani Bold (impact militaire)
    displayLarge = TextStyle(
        fontFamily = RajdhaniFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 48.sp,
        letterSpacing = 0.15.sp
    ),
    displayMedium = TextStyle(
        fontFamily = RajdhaniFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 36.sp,
        letterSpacing = 0.1.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = RajdhaniFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp,
        letterSpacing = 0.05.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = RajdhaniFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 22.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = RajdhaniFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 18.sp,
    ),

    // Corps / Labels — Inter Tight
    bodyLarge = TextStyle(
        fontFamily = InterTightFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = InterTightFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = InterTightFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 10.sp,
        letterSpacing = 0.1.sp
    ),

    // Labels — Inter Tight Medium
    labelLarge = TextStyle(
        fontFamily = InterTightFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 12.sp,
        letterSpacing = 0.15.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterTightFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 10.sp,
        letterSpacing = 0.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterTightFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 9.sp,
        letterSpacing = 0.25.sp
    ),

    // Monospace — DM Mono pour données numériques
    titleLarge = TextStyle(
        fontFamily = DmMonoFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 56.sp,       // Magnitude "7-segments"
    ),
    titleMedium = TextStyle(
        fontFamily = DmMonoFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 32.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = DmMonoFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
    ),
)
```

### `ui/theme/Shape.kt`
```kotlin
package com.guardian.track.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val GuardianShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small      = RoundedCornerShape(8.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(50.dp)   // Boutons pill
)
```

### `ui/theme/GuardianTheme.kt`
```kotlin
package com.guardian.track.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Schéma sombre — couleurs primaires GuardianTrack
private val DarkColorScheme = darkColorScheme(
    primary          = CyanElectric,
    onPrimary        = BgDeep,
    primaryContainer = CyanDim,
    secondary        = AmberWarn,
    onSecondary      = BgDeep,
    tertiary         = GreenSecure,
    error            = RedAlert,
    onError          = TextPrimary,
    errorContainer   = RedDim,
    background       = BgDeep,
    onBackground     = TextPrimary,
    surface          = BgSurface,
    onSurface        = TextPrimary,
    surfaceVariant   = BgCard,
    onSurfaceVariant = TextSecondary,
    outline          = BorderSubtle,
    outlineVariant   = Divider,
    scrim            = Color(0x80090E1A),
)

// Schéma clair — version légère pour le mode jour
private val LightColorScheme = lightColorScheme(
    primary          = Color(0xFF006B82),
    onPrimary        = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB8EAFF),
    secondary        = Color(0xFFB87900),
    background       = Color(0xFFF5F8FF),
    onBackground     = Color(0xFF0D1520),
    surface          = Color(0xFFE8F0FA),
    onSurface        = Color(0xFF0D1520),
    error            = Color(0xFFB3001B),
)

@Composable
fun GuardianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Status bar transparent avec icônes claires/sombres selon le thème
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BgDeep.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = GuardianTypography,
        shapes      = GuardianShapes,
        content     = content
    )
}

// Import Color nécessaire pour LightColorScheme
import androidx.compose.ui.graphics.Color
```

---

## 5. Architecture MVVM — Vue d'ensemble

```
┌──────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER (Compose)                 │
│  ┌──────────────────────┐  collectAsStateWithLifecycle()          │
│  │  Screen Composable   │ ◄──── StateFlow<UiState>               │
│  │  (View Only)         │                                         │
│  │  DashboardScreen     │ ────► ViewModel.onEvent()               │
│  │  HistoryScreen       │       (sealed events ou lambdas)        │
│  │  SettingsScreen      │                                         │
│  └──────────────────────┘                                         │
│  ┌──────────────────────┐                                         │
│  │  AppNavigation       │  NavHost + NavController                │
│  │  GuardianTheme       │  MaterialTheme wrapper                  │
│  └──────────────────────┘                                         │
└──────────────────────────────────────────────────────────────────┘
                              │ inject via hiltViewModel()
┌─────────────────────────────────────────────────────────────────┐
│                          DOMAIN LAYER                            │
│         DomainModel │ NetworkResult<T> │ Mapper                  │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                           DATA LAYER                             │
│    Repository ← Room │ Retrofit │ WorkManager │ DataStore        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6. Couche Model — Entités, DTO, DomainModel

### `domain/model/IncidentType.kt`
```kotlin
package com.guardian.track.domain.model

enum class IncidentType { FALL, BATTERY, MANUAL }
```

### `domain/model/Incident.kt` (DomainModel)
```kotlin
package com.guardian.track.domain.model

data class Incident(
    val id: Long = 0,
    val timestamp: Long,
    val type: IncidentType,
    val latitude: Double,
    val longitude: Double,
    val isSynced: Boolean = false,
    val formattedDate: String = "",
    val formattedTime: String = ""
)
```

### `domain/model/EmergencyContact.kt`
```kotlin
package com.guardian.track.domain.model

data class EmergencyContact(
    val id: Long = 0,
    val name: String,
    val phoneNumber: String
)
```

### `domain/util/NetworkResult.kt`
```kotlin
package com.guardian.track.domain.util

sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String, val code: Int? = null) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}
```

### `data/local/db/entity/IncidentEntity.kt`
```kotlin
package com.guardian.track.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "timestamp")  val timestamp: Long,
    @ColumnInfo(name = "type")       val type: String,
    @ColumnInfo(name = "latitude")   val latitude: Double,
    @ColumnInfo(name = "longitude")  val longitude: Double,
    @ColumnInfo(name = "is_synced")  val isSynced: Boolean = false
)
```

### `data/local/db/entity/EmergencyContactEntity.kt`
```kotlin
package com.guardian.track.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")          val id: Long = 0,
    @ColumnInfo(name = "name")         val name: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String
)
```

### `data/remote/api/dto/IncidentDto.kt`
```kotlin
package com.guardian.track.data.remote.api.dto

import com.google.gson.annotations.SerializedName

data class IncidentDto(
    @SerializedName("id")        val id: Long?,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("type")      val type: String,
    @SerializedName("latitude")  val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
```

### `domain/util/Mapper.kt`
```kotlin
package com.guardian.track.domain.util

import com.guardian.track.data.local.db.entity.EmergencyContactEntity
import com.guardian.track.data.local.db.entity.IncidentEntity
import com.guardian.track.data.remote.api.dto.IncidentDto
import com.guardian.track.domain.model.EmergencyContact
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun IncidentEntity.toDomain(): Incident {
    val date    = Date(timestamp)
    val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return Incident(
        id            = id,
        timestamp     = timestamp,
        type          = IncidentType.valueOf(type),
        latitude      = latitude,
        longitude     = longitude,
        isSynced      = isSynced,
        formattedDate = dateFmt.format(date),
        formattedTime = timeFmt.format(date)
    )
}

fun Incident.toEntity() = IncidentEntity(
    id = id, timestamp = timestamp, type = type.name,
    latitude = latitude, longitude = longitude, isSynced = isSynced
)

fun IncidentEntity.toDto() = IncidentDto(
    id = id, timestamp = timestamp, type = type,
    latitude = latitude, longitude = longitude
)

fun EmergencyContactEntity.toDomain() =
    EmergencyContact(id = id, name = name, phoneNumber = phoneNumber)

fun EmergencyContact.toEntity() =
    EmergencyContactEntity(id = id, name = name, phoneNumber = phoneNumber)
```

---

## 7. Room Database

### `data/local/db/dao/IncidentDao.kt`
```kotlin
package com.guardian.track.data.local.db.dao

import androidx.room.*
import com.guardian.track.data.local.db.entity.IncidentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity): Long

    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM incidents WHERE is_synced = 0")
    suspend fun getUnsyncedIncidents(): List<IncidentEntity>

    @Query("UPDATE incidents SET is_synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Long)

    @Query("DELETE FROM incidents WHERE id = :id")
    suspend fun deleteIncidentById(id: Long)
}
```

### `data/local/db/dao/EmergencyContactDao.kt`
```kotlin
package com.guardian.track.data.local.db.dao

import androidx.room.*
import com.guardian.track.data.local.db.entity.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContactEntity): Long

    @Query("DELETE FROM emergency_contacts WHERE _id = :id")
    suspend fun deleteContactById(id: Long)

    @Query("SELECT * FROM emergency_contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts")
    suspend fun getAllContactsSync(): List<EmergencyContactEntity>
}
```

### `data/local/db/GuardianDatabase.kt`
```kotlin
package com.guardian.track.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guardian.track.data.local.db.dao.EmergencyContactDao
import com.guardian.track.data.local.db.dao.IncidentDao
import com.guardian.track.data.local.db.entity.EmergencyContactEntity
import com.guardian.track.data.local.db.entity.IncidentEntity

@Database(
    entities     = [IncidentEntity::class, EmergencyContactEntity::class],
    version      = 1,
    exportSchema = false
)
abstract class GuardianDatabase : RoomDatabase() {
    abstract fun incidentDao(): IncidentDao
    abstract fun contactDao(): EmergencyContactDao
    companion object { const val DATABASE_NAME = "guardian_track.db" }
}
```

---

## 8. DataStore Preferences

```kotlin
package com.guardian.track.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "guardian_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val KEY_THRESHOLD      = floatPreferencesKey("fall_threshold")
        val KEY_DARK_MODE      = booleanPreferencesKey("dark_mode")
        val KEY_SMS_SIMULATION = booleanPreferencesKey("sms_simulation")
        const val DEFAULT_THRESHOLD = 15.0f
    }

    val fallThreshold: Flow<Float> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_THRESHOLD] ?: DEFAULT_THRESHOLD }

    val darkMode: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_DARK_MODE] ?: false }

    val smsSimulationEnabled: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_SMS_SIMULATION] ?: true }   // ACTIF par défaut

    suspend fun setFallThreshold(v: Float) =
        context.dataStore.edit { it[KEY_THRESHOLD] = v }
    suspend fun setDarkMode(v: Boolean) =
        context.dataStore.edit { it[KEY_DARK_MODE] = v }
    suspend fun setSmsSimulation(v: Boolean) =
        context.dataStore.edit { it[KEY_SMS_SIMULATION] = v }
}
```

---

## 9. Retrofit & API

### `data/remote/api/GuardianApi.kt`
```kotlin
package com.guardian.track.data.remote.api

import com.guardian.track.data.remote.api.dto.IncidentDto
import retrofit2.Response
import retrofit2.http.*

interface GuardianApi {
    @POST("incidents")
    suspend fun postIncident(@Body incident: IncidentDto): Response<IncidentDto>

    @GET("incidents")
    suspend fun getIncidents(): Response<List<IncidentDto>>

    @DELETE("incidents/{id}")
    suspend fun deleteIncident(@Path("id") id: Long): Response<Unit>
}
```

### `data/remote/interceptor/ApiKeyInterceptor.kt`
```kotlin
package com.guardian.track.data.remote.interceptor

import com.guardian.track.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(req)
    }
}
```

---

## 10. Injection de Dépendances — Hilt Modules

### `GuardianTrackApplication.kt`
```kotlin
package com.guardian.track

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GuardianTrackApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()
}
```

### `di/DatabaseModule.kt`
```kotlin
package com.guardian.track.di

import android.content.Context
import androidx.room.Room
import com.guardian.track.data.local.db.GuardianDatabase
import com.guardian.track.data.local.db.dao.EmergencyContactDao
import com.guardian.track.data.local.db.dao.IncidentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): GuardianDatabase =
        Room.databaseBuilder(ctx, GuardianDatabase::class.java, GuardianDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides fun provideIncidentDao(db: GuardianDatabase): IncidentDao = db.incidentDao()
    @Provides fun provideContactDao(db: GuardianDatabase): EmergencyContactDao = db.contactDao()
}
```

### `di/NetworkModule.kt`
```kotlin
package com.guardian.track.di

import com.guardian.track.BuildConfig
import com.guardian.track.data.remote.api.GuardianApi
import com.guardian.track.data.remote.interceptor.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideOkHttp(interceptor: ApiKeyInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): GuardianApi = retrofit.create(GuardianApi::class.java)
}
```

### `di/LocationModule.kt`
```kotlin
package com.guardian.track.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides @Singleton
    fun provideFusedLocation(@ApplicationContext ctx: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(ctx)
}
```

### `di/RepositoryModule.kt`
```kotlin
package com.guardian.track.di

import com.guardian.track.data.repository.ContactRepositoryImpl
import com.guardian.track.data.repository.IncidentRepositoryImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Les implémentations sont directement injectables via @Singleton
    // Pas besoin de Binds si on injecte les Impl directement dans les ViewModels
}
```

---

## 11. Repositories

### `data/repository/IncidentRepositoryImpl.kt`
```kotlin
package com.guardian.track.data.repository

import android.content.Context
import androidx.work.*
import com.guardian.track.data.local.db.dao.IncidentDao
import com.guardian.track.data.local.db.entity.IncidentEntity
import com.guardian.track.data.remote.api.GuardianApi
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.util.NetworkResult
import com.guardian.track.domain.util.toDomain
import com.guardian.track.domain.util.toDto
import com.guardian.track.domain.util.toEntity
import com.guardian.track.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentRepositoryImpl @Inject constructor(
    private val incidentDao: IncidentDao,
    private val api: GuardianApi,
    @ApplicationContext private val context: Context
) {
    fun getAllIncidents(): Flow<List<Incident>> =
        incidentDao.getAllIncidents().map { it.map { e -> e.toDomain() } }

    suspend fun insertIncident(incident: Incident): NetworkResult<Unit> {
        val entity = incident.toEntity()
        val id     = incidentDao.insertIncident(entity)
        return try {
            val response = api.postIncident(entity.copy(id = id).toDto())
            if (response.isSuccessful) {
                incidentDao.markAsSynced(id)
                NetworkResult.Success(Unit)
            } else {
                scheduleSyncWorker()
                NetworkResult.Error("HTTP ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            scheduleSyncWorker()
            NetworkResult.Error(e.message ?: "Network error")
        }
    }

    suspend fun deleteIncident(id: Long) = incidentDao.deleteIncidentById(id)

    fun scheduleSyncWorker() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("sync_incidents", ExistingWorkPolicy.KEEP, request)
    }

    suspend fun getUnsyncedIncidents(): List<IncidentEntity> = incidentDao.getUnsyncedIncidents()
    suspend fun markAsSynced(id: Long) = incidentDao.markAsSynced(id)
}
```

---

## 12. ViewModels

### `ui/dashboard/DashboardViewModel.kt`
```kotlin
package com.guardian.track.ui.dashboard

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.track.data.local.datastore.UserPreferencesDataStore
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isServiceRunning : Boolean   = false,
    val currentMagnitude : Float     = 0f,
    val batteryLevel     : Int       = 100,
    val isGpsActive      : Boolean   = false,
    val lastLocation     : Location? = null,
    val alertMessage     : String?   = null,
    val isLoading        : Boolean   = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incidentRepo: IncidentRepositoryImpl,
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun updateMagnitude(v: Float)  { _uiState.update { it.copy(currentMagnitude = v) } }
    fun updateBattery(v: Int)      { _uiState.update { it.copy(batteryLevel = v) } }
    fun updateServiceStatus(v: Boolean) { _uiState.update { it.copy(isServiceRunning = v) } }

    fun updateGpsStatus(active: Boolean, location: Location? = null) {
        _uiState.update { it.copy(isGpsActive = active, lastLocation = location) }
    }

    fun triggerManualAlert(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            incidentRepo.insertIncident(
                Incident(
                    timestamp = System.currentTimeMillis(),
                    type      = IncidentType.MANUAL,
                    latitude  = latitude,
                    longitude = longitude
                )
            )
            _uiState.update { it.copy(isLoading = false, alertMessage = "Alerte manuelle enregistrée") }
        }
    }

    fun clearAlertMessage() { _uiState.update { it.copy(alertMessage = null) } }
}
```

### `ui/history/HistoryViewModel.kt`
```kotlin
package com.guardian.track.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.model.Incident
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val incidents : List<Incident> = emptyList(),
    val isLoading : Boolean        = false,
    val message   : String?        = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: IncidentRepositoryImpl
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> =
        repo.getAllIncidents()
            .map { HistoryUiState(incidents = it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState(isLoading = true))

    fun deleteIncident(id: Long) { viewModelScope.launch { repo.deleteIncident(id) } }
}
```

### `ui/settings/SettingsViewModel.kt`
```kotlin
package com.guardian.track.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.track.data.local.datastore.UserPreferencesDataStore
import com.guardian.track.data.local.security.EncryptedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val threshold      : Float   = 15.0f,
    val darkMode       : Boolean = false,
    val smsSimulation  : Boolean = true,
    val emergencyNumber: String  = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore      : UserPreferencesDataStore,
    private val encryptedPrefs : EncryptedPreferencesManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        dataStore.fallThreshold,
        dataStore.darkMode,
        dataStore.smsSimulationEnabled
    ) { threshold, dark, sms ->
        SettingsUiState(threshold, dark, sms, encryptedPrefs.getEmergencyNumber())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setThreshold(v: Float)     { viewModelScope.launch { dataStore.setFallThreshold(v) } }
    fun setDarkMode(v: Boolean)    { viewModelScope.launch { dataStore.setDarkMode(v) } }
    fun setSmsSimulation(v: Boolean){ viewModelScope.launch { dataStore.setSmsSimulation(v) } }
    fun setEmergencyNumber(v: String) { encryptedPrefs.saveEmergencyNumber(v) }
}
```

---

## 13. Navigation Compose — AppNavigation

### `ui/navigation/Screen.kt`
```kotlin
package com.guardian.track.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Radar",   Icons.Outlined.TrackChanges)
    object History   : Screen("history",   "Journal", Icons.Outlined.History)
    object Settings  : Screen("settings",  "Config",  Icons.Outlined.Settings)

    companion object {
        val bottomNavItems = listOf(Dashboard, History, Settings)
    }
}
```

### `ui/navigation/AppNavigation.kt`
```kotlin
package com.guardian.track.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guardian.track.ui.dashboard.DashboardScreen
import com.guardian.track.ui.history.HistoryScreen
import com.guardian.track.ui.settings.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Dashboard.route,
        // ── Transitions Material 3 entre screens ──────────────
        enterTransition  = {
            fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.95f)
        },
        exitTransition   = {
            fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.95f)
        },
        popEnterTransition = {
            fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 1.05f)
        },
        popExitTransition  = {
            fadeOut(tween(200))
        }
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen() }
        composable(Screen.History.route)   { HistoryScreen() }
        composable(Screen.Settings.route)  { SettingsScreen() }
    }
}
```

---

## 14. MainActivity — Point d'entrée Compose

```kotlin
package com.guardian.track.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guardian.track.service.SurveillanceService
import com.guardian.track.ui.components.BottomNavBar
import com.guardian.track.ui.navigation.AppNavigation
import com.guardian.track.ui.navigation.Screen
import com.guardian.track.ui.settings.SettingsViewModel
import com.guardian.track.ui.theme.GuardianTheme
import com.guardian.track.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsVm: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Démarrer le service de surveillance
        ContextCompat.startForegroundService(this, Intent(this, SurveillanceService::class.java))

        // Demander les permissions
        PermissionManager(this).requestAllPermissions()

        setContent {
            // Observer le mode sombre depuis DataStore
            val settings by settingsVm.uiState.collectAsStateWithLifecycle()

            GuardianTheme(darkTheme = settings.darkMode) {
                GuardianApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianApp() {
    val navController = rememberNavController()
    val navBackStack  by navController.currentBackStackEntryAsState()
    val currentRoute  = navBackStack?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavBar(
                items        = Screen.bottomNavItems,
                currentRoute = currentRoute,
                onItemClick  = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController)
        }
    }
}
```

---

## 15. Screen 1 — DashboardScreen

```kotlin
package com.guardian.track.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.guardian.track.ui.components.*
import com.guardian.track.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Affichage du message d'alerte via Snackbar
    LaunchedEffect(uiState.alertMessage) {
        uiState.alertMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearAlertMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Header animé ─────────────────────────────────────
            DashboardHeader(isActive = uiState.isServiceRunning)

            // ── Carte radar avec magnitude ────────────────────────
            // Entrée décalée (stagger animation)
            AnimatedVisibility(
                visible = true,
                enter   = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 2 }
            ) {
                ScanLineCard(isActive = uiState.isServiceRunning) {
                    AccelerometerCard(magnitude = uiState.currentMagnitude)
                }
            }

            // ── Row batterie + GPS ────────────────────────────────
            AnimatedVisibility(
                visible = true,
                enter   = fadeIn(tween(500, delayMillis = 100)) +
                          slideInVertically(tween(500, delayMillis = 100)) { it / 2 }
            ) {
                Row(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BatteryCard(
                        level    = uiState.batteryLevel,
                        modifier = Modifier.weight(1f)
                    )
                    GpsCard(
                        isActive = uiState.isGpsActive,
                        lat      = uiState.lastLocation?.latitude,
                        lon      = uiState.lastLocation?.longitude,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // ── Bouton d'alerte manuelle ──────────────────────────
            AnimatedVisibility(
                visible = true,
                enter   = fadeIn(tween(600, delayMillis = 200)) +
                          slideInVertically(tween(600, delayMillis = 200)) { it }
            ) {
                AlertButton(
                    isLoading = uiState.isLoading,
                    onClick = {
                        scope.launch {
                            val fused = LocationServices.getFusedLocationProviderClient(context)
                            val hasPerm = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPerm) {
                                fused.lastLocation.addOnSuccessListener { loc ->
                                    viewModel.triggerManualAlert(loc?.latitude ?: 0.0, loc?.longitude ?: 0.0)
                                }
                            } else {
                                viewModel.triggerManualAlert(0.0, 0.0)
                            }
                        }
                    }
                )
            }
        }

        // ── Snackbar Host en bas ──────────────────────────────────
        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter)
        ) { data ->
            Snackbar(
                snackbarData    = data,
                containerColor  = BgCardElev,
                contentColor    = TextPrimary,
                actionColor     = CyanElectric
            )
        }
    }
}

// ── Sous-composants du Dashboard ──────────────────────────────────

@Composable
private fun DashboardHeader(isActive: Boolean) {
    val statusColor by animateColorAsState(
        targetValue   = if (isActive) CyanElectric else TextSecondary,
        animationSpec = tween(600),
        label         = "statusColor"
    )
    Column {
        Text(
            text  = "GUARDIAN",
            style = MaterialTheme.typography.displayMedium,
            color = CyanElectric
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Point pulsant
            val infiniteTransition = rememberInfiniteTransition(label = "statusPulse")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue  = 0.3f,
                targetValue   = 1f,
                animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
                label         = "pulseAlpha"
            )
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(statusColor.copy(alpha = if (isActive) pulseAlpha else 0.4f), shape = MaterialTheme.shapes.extraLarge)
            )
            Text(
                text  = if (isActive) "SURVEILLANCE ACTIVE" else "SERVICE INACTIF",
                style = MaterialTheme.typography.labelMedium,
                color = statusColor
            )
        }
    }
}

@Composable
private fun AccelerometerCard(magnitude: Float) {
    // Animation spring sur la valeur numérique
    val animatedMagnitude by animateFloatAsState(
        targetValue   = magnitude,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label         = "magnitude"
    )

    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text  = "ACCÉLÉROMÈTRE",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            // Affichage style 7-segments — DM Mono grande taille
            Text(
                text  = "%05.2f".format(animatedMagnitude),
                style = MaterialTheme.typography.titleLarge,   // DM Mono 56sp
                color = when {
                    animatedMagnitude > 15f -> RedAlert
                    animatedMagnitude > 8f  -> AmberWarn
                    else                   -> CyanElectric
                }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text     = "m/s²",
                style    = MaterialTheme.typography.bodyMedium,
                color    = TextSecondary,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        // Barre de niveau magnitude
        val progress = (animatedMagnitude / 30f).coerceIn(0f, 1f)
        val barColor by animateColorAsState(
            targetValue   = when {
                progress > 0.5f -> RedAlert
                progress > 0.27f -> AmberWarn
                else            -> CyanElectric
            },
            animationSpec = tween(300),
            label         = "barColor"
        )
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            progress         = { progress },
            modifier         = Modifier.fillMaxWidth().height(3.dp),
            color            = barColor,
            trackColor       = BorderSubtle,
        )
    }
}

@Composable
private fun BatteryCard(level: Int, modifier: Modifier = Modifier) {
    val batteryColor by animateColorAsState(
        targetValue = when {
            level <= 15 -> RedAlert
            level <= 30 -> AmberWarn
            else        -> GreenSecure
        },
        animationSpec = tween(500),
        label = "batteryColor"
    )

    GlassCard(modifier = modifier.height(130.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("BATTERIE", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Text(
                text  = "$level%",
                style = MaterialTheme.typography.titleMedium,   // DM Mono 32sp
                color = batteryColor
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress      = { level / 100f },
                modifier      = Modifier.fillMaxWidth().height(4.dp),
                color         = batteryColor,
                trackColor    = BorderSubtle
            )
        }
    }
}

@Composable
private fun GpsCard(isActive: Boolean, lat: Double?, lon: Double?, modifier: Modifier = Modifier) {
    val gpsColor by animateColorAsState(
        targetValue   = if (isActive) GreenSecure else TextSecondary,
        animationSpec = tween(500),
        label         = "gpsColor"
    )

    GlassCard(modifier = modifier.height(130.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.MyLocation,
                    contentDescription = null,
                    tint        = gpsColor,
                    modifier    = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text  = if (isActive) "GPS ACTIF" else "GPS INACTIF",
                    style = MaterialTheme.typography.labelSmall,
                    color = gpsColor
                )
            }
            Spacer(Modifier.height(8.dp))
            if (lat != null && lon != null && !(lat == 0.0 && lon == 0.0)) {
                Text("%.5f".format(lat), style = MaterialTheme.typography.titleSmall, color = TextSecondary)
                Text("%.5f".format(lon), style = MaterialTheme.typography.titleSmall, color = TextSecondary)
            } else {
                Text("-- , --", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}
```

---

## 16. Screen 2 — HistoryScreen

```kotlin
package com.guardian.track.ui.history

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guardian.track.domain.model.Incident
import com.guardian.track.ui.components.GlassCard
import com.guardian.track.ui.components.IncidentItem
import com.guardian.track.ui.theme.*
import com.guardian.track.util.ExportHelper
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel    : HistoryViewModel = hiltViewModel(),
    exportHelper : ExportHelper     = TODO("Inject via CompositionLocal")
) {
    val uiState          by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost   = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(data, containerColor = BgCardElev, contentColor = TextPrimary, actionColor = CyanElectric)
        }},
        floatingActionButton = {
            // Bouton export CSV — FAB glassmorphique
            ExtendedFloatingActionButton(
                onClick          = {
                    val filename = exportHelper.exportToCsv(uiState.incidents)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (filename != null) "Exporté : $filename" else "Erreur lors de l'export"
                        )
                    }
                },
                containerColor   = BgCard,
                contentColor     = CyanElectric,
                icon             = { Icon(Icons.Outlined.FileDownload, null) },
                text             = { Text("Exporter CSV", style = MaterialTheme.typography.labelLarge) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color    = CyanElectric
                    )
                }
                uiState.incidents.isEmpty() -> {
                    // État vide
                    Column(
                        modifier              = Modifier.align(Alignment.Center),
                        horizontalAlignment   = Alignment.CenterHorizontally,
                        verticalArrangement   = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("JOURNAL VIDE", style = MaterialTheme.typography.headlineSmall, color = TextSecondary)
                        Text(
                            text      = "Aucun incident enregistré.\nLe service de surveillance est actif.",
                            style     = MaterialTheme.typography.bodyMedium,
                            color     = TextDisabled,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier            = Modifier.fillMaxSize(),
                        contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Header avec compteur
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Text(
                                    "JOURNAL D'INCIDENTS",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextPrimary
                                )
                                Text(
                                    "${uiState.incidents.size} événements",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondary
                                )
                            }
                        }

                        itemsIndexed(
                            items = uiState.incidents,
                            key   = { _, incident -> incident.id }
                        ) { index, incident ->
                            // Animation d'entrée décalée pour chaque item
                            AnimatedVisibility(
                                visible      = true,
                                enter        = fadeIn(tween(300, delayMillis = index * 50)) +
                                               slideInHorizontally(tween(300, delayMillis = index * 50)) { -it / 4 }
                            ) {
                                IncidentItem(
                                    incident      = incident,
                                    onDeleteClick = { viewModel.deleteIncident(incident.id) }
                                )
                            }
                        }

                        // Espace pour le FAB
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}
```

---

## 17. Screen 3 — SettingsScreen

```kotlin
package com.guardian.track.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guardian.track.ui.components.GlassCard
import com.guardian.track.ui.theme.*

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var numberInput by remember(uiState.emergencyNumber) { mutableStateOf(uiState.emergencyNumber) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost   = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "CONFIGURATION",
                style = MaterialTheme.typography.headlineMedium,
                color = CyanElectric
            )

            // ── Section Capteur ───────────────────────────────────
            SettingsSectionHeader("CAPTEUR")

            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Seuil de détection", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                            Text("Paramètre impact (m/s²)", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Text(
                            "%.1f".format(uiState.threshold),
                            style = MaterialTheme.typography.titleSmall,   // DM Mono
                            color = CyanElectric
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Slider(
                        value         = uiState.threshold,
                        onValueChange = viewModel::setThreshold,
                        valueRange    = 5f..30f,
                        steps         = 24,
                        colors        = SliderDefaults.colors(
                            thumbColor              = CyanElectric,
                            activeTrackColor        = CyanElectric,
                            inactiveTrackColor      = BorderSubtle,
                            activeTickColor         = CyanDim,
                            inactiveTickColor       = Transparent
                        )
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("5 m/s²", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                        Text("30 m/s²", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                    }
                }
            }

            // ── Section Affichage ─────────────────────────────────
            SettingsSectionHeader("AFFICHAGE")

            GlassCard {
                SettingsToggleRow(
                    icon        = Icons.Outlined.DarkMode,
                    title       = "Mode Sombre",
                    subtitle    = "Interface nuit profonde",
                    checked     = uiState.darkMode,
                    onChecked   = viewModel::setDarkMode
                )
            }

            // ── Section Urgence ───────────────────────────────────
            SettingsSectionHeader("URGENCE")

            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.PhoneInTalk, null, tint = CyanElectric, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Numéro d'urgence", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                    }
                    Text(
                        "Chiffré localement via AES-256",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value          = numberInput,
                        onValueChange  = { numberInput = it.filter { c -> c.isDigit() } },
                        modifier       = Modifier.fillMaxWidth(),
                        label          = { Text("Ex: 25123456") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine     = true,
                        colors         = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor    = CyanElectric,
                            unfocusedBorderColor  = BorderSubtle,
                            focusedLabelColor     = CyanElectric,
                            cursorColor           = CyanElectric,
                            focusedTextColor      = TextPrimary,
                            unfocusedTextColor    = TextSecondary
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            viewModel.setEmergencyNumber(numberInput)
                            scope.launch { snackbarHostState.showSnackbar("Numéro sauvegardé (chiffré)") }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = CyanDim,
                            contentColor   = CyanElectric
                        )
                    ) {
                        Icon(Icons.Outlined.Save, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("SAUVEGARDER", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            // ── Section SMS ───────────────────────────────────────
            SettingsSectionHeader("COMMUNICATIONS")

            GlassCard {
                Column {
                    SettingsToggleRow(
                        icon      = Icons.Outlined.Sms,
                        title     = "Mode Simulation SMS",
                        subtitle  = "Remplace les SMS par des notifications — Recommandé",
                        checked   = uiState.smsSimulation,
                        onChecked = viewModel::setSmsSimulation,
                        activeColor = GreenSecure
                    )

                    if (!uiState.smsSimulation) {
                        HorizontalDivider(color = Divider, modifier = Modifier.padding(horizontal = 16.dp))
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Outlined.Warning, null, tint = AmberWarn, modifier = Modifier.size(16.dp))
                            Text(
                                "Mode réel activé. Les SMS seront envoyés au numéro d'urgence configuré.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AmberWarn
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text     = title,
        style    = MaterialTheme.typography.labelMedium,
        color    = TextSecondary,
        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
    )
}

@Composable
private fun SettingsToggleRow(
    icon        : androidx.compose.ui.graphics.vector.ImageVector,
    title       : String,
    subtitle    : String,
    checked     : Boolean,
    onChecked   : (Boolean) -> Unit,
    activeColor : androidx.compose.ui.graphics.Color = CyanElectric
) {
    val trackColor by animateColorAsState(
        targetValue   = if (checked) activeColor.copy(alpha = 0.3f) else BorderSubtle,
        animationSpec = tween(300),
        label         = "trackColor"
    )

    Row(
        modifier  = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = if (checked) activeColor else TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onChecked,
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = activeColor,
                checkedTrackColor   = activeColor.copy(alpha = 0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = BorderSubtle
            )
        )
    }
}
```

---

## 18. Composants Réutilisables — UI Kit

### `ui/components/GlassCard.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.guardian.track.ui.theme.BgCard
import com.guardian.track.ui.theme.BorderSubtle

@Composable
fun GlassCard(
    modifier : Modifier = Modifier,
    content  : @Composable () -> Unit
) {
    Surface(
        modifier      = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, BorderSubtle, MaterialTheme.shapes.medium),
        color         = BgCard,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        content       = content
    )
}
```

### `ui/components/ScanLineCard.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.guardian.track.ui.theme.CyanElectric

@Composable
fun ScanLineCard(
    isActive : Boolean,
    modifier : Modifier = Modifier,
    content  : @Composable () -> Unit
) {
    GlassCard(modifier = modifier) {
        Box {
            content()

            // Scan line animée — seulement si le service est actif
            if (isActive) {
                val transition = rememberInfiniteTransition(label = "scanLine")
                val scanY by transition.animateFloat(
                    initialValue  = 0f,
                    targetValue   = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "scanY"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val y = size.height * scanY
                    // Ligne lumineuse principale
                    drawLine(
                        color       = CyanElectric.copy(alpha = 0.4f),
                        start       = Offset(0f, y),
                        end         = Offset(size.width, y),
                        strokeWidth = 1.5f
                    )
                    // Halo sous la ligne
                    for (i in 1..8) {
                        drawLine(
                            color       = CyanElectric.copy(alpha = 0.04f * (8 - i)),
                            start       = Offset(0f, y + i * 3),
                            end         = Offset(size.width, y + i * 3),
                            strokeWidth = 1f
                        )
                    }
                }
            }
        }
    }
}
```

### `ui/components/RadarPulse.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.guardian.track.ui.theme.CyanElectric

/**
 * Composant Canvas — Cercles concentriques qui pulsent vers l'extérieur.
 * Utilisable en overlay sur le Dashboard pour renforcer l'esthétique radar.
 */
@Composable
fun RadarPulse(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "radar")

    // 3 ondes décalées
    val scales = (0..2).map { i ->
        transition.animateFloat(
            initialValue  = 0f,
            targetValue   = 1f,
            animationSpec = infiniteRepeatable(
                animation  = tween(2400, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(i * 800)
            ),
            label = "radarScale$i"
        )
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxR   = minOf(size.width, size.height) / 2f

        scales.forEach { scaleState ->
            val scale = scaleState.value
            val r     = maxR * scale
            val alpha = (1f - scale).coerceAtLeast(0f) * 0.5f
            drawCircle(
                color  = CyanElectric.copy(alpha = alpha),
                radius = r,
                center = center,
                style  = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth = 1.5f)
            )
        }
    }
}
```

### `ui/components/AlertButton.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.guardian.track.ui.theme.*

@Composable
fun AlertButton(
    isLoading : Boolean,
    onClick   : () -> Unit,
    modifier  : Modifier = Modifier
) {
    // Pulsation continue — scale spring
    val infiniteTransition = rememberInfiniteTransition(label = "alertPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue  = 1f,
        targetValue   = 1.03f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alertScale"
    )

    Button(
        onClick  = { if (!isLoading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .scale(if (isLoading) 1f else pulseScale),
        shape  = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = RedAlert,
            contentColor   = TextPrimary,
            disabledContainerColor = RedDim
        ),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color    = TextPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Icon(Icons.Outlined.Warning, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                "ALERTE MANUELLE",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
```

### `ui/components/IncidentItem.kt` — SwipeToDismiss natif Compose
```kotlin
package com.guardian.track.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType
import com.guardian.track.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentItem(
    incident      : Incident,
    onDeleteClick : () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDeleteClick()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state            = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            // Fond rouge révélé au swipe
            val color by animateColorAsState(
                targetValue   = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                                    RedAlert else RedDim,
                animationSpec = tween(200),
                label         = "swipeBg"
            )
            Box(
                modifier          = Modifier.fillMaxSize()
                    .background(color, MaterialTheme.shapes.medium),
                contentAlignment  = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.padding(end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Supprimer", style = MaterialTheme.typography.labelLarge, color = TextPrimary)
                    Icon(Icons.Outlined.DeleteForever, null, tint = TextPrimary, modifier = Modifier.size(20.dp))
                }
            }
        }
    ) {
        // Contenu normal de l'item
        GlassCard {
            Row(
                modifier          = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icône + couleur selon type
                val (icon, color) = when (incident.type) {
                    IncidentType.FALL    -> Icons.Outlined.PersonalInjury to RedAlert
                    IncidentType.BATTERY -> Icons.Outlined.BatteryAlert   to AmberWarn
                    IncidentType.MANUAL  -> Icons.Outlined.Warning         to CyanElectric
                }

                Box(
                    modifier         = Modifier
                        .size(40.dp)
                        .background(color.copy(alpha = 0.15f), MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                }

                // Informations incidents
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        incident.type.name,
                        style      = MaterialTheme.typography.labelLarge,
                        color      = color,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${incident.formattedDate}  ${incident.formattedTime}",
                        style = MaterialTheme.typography.titleSmall,   // DM Mono
                        color = TextSecondary
                    )
                    val coordsText = if (incident.latitude == 0.0 && incident.longitude == 0.0)
                        "Localisation indisponible"
                    else "%.5f, %.5f".format(incident.latitude, incident.longitude)
                    Text(coordsText, style = MaterialTheme.typography.bodySmall, color = TextDisabled)
                }

                // Badge sync
                StatusBadge(isSynced = incident.isSynced)
            }
        }
    }
}
```

### `ui/components/StatusBadge.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guardian.track.ui.theme.*

@Composable
fun StatusBadge(isSynced: Boolean) {
    val bgColor by animateColorAsState(
        targetValue   = if (isSynced) GreenDim else AmberDim,
        animationSpec = tween(400),
        label         = "badgeBg"
    )
    val textColor by animateColorAsState(
        targetValue   = if (isSynced) GreenSecure else AmberWarn,
        animationSpec = tween(400),
        label         = "badgeText"
    )

    Box(
        modifier = Modifier
            .background(bgColor, MaterialTheme.shapes.extraSmall)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text  = if (isSynced) "SYNC" else "PENDING",
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}
```

### `ui/components/BottomNavBar.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guardian.track.ui.navigation.Screen
import com.guardian.track.ui.theme.*

@Composable
fun BottomNavBar(
    items        : List<Screen>,
    currentRoute : String?,
    onItemClick  : (Screen) -> Unit
) {
    NavigationBar(
        modifier       = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle),
        containerColor = BgSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { screen ->
            val selected = currentRoute == screen.route

            val iconColor by animateColorAsState(
                targetValue   = if (selected) CyanElectric else TextSecondary,
                animationSpec = tween(250),
                label         = "navColor_${screen.route}"
            )

            NavigationBarItem(
                selected = selected,
                onClick  = { onItemClick(screen) },
                icon     = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        tint = iconColor
                    )
                },
                label    = {
                    Text(
                        screen.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = iconColor
                    )
                },
                colors   = NavigationBarItemDefaults.colors(
                    selectedIndicatorColor   = CyanDim,
                    indicatorColor           = CyanDim,
                    unselectedIconColor      = TextSecondary,
                    unselectedTextColor      = TextSecondary
                )
            )
        }
    }
}
```

### `ui/components/PermissionRationaleDialog.kt`
```kotlin
package com.guardian.track.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.guardian.track.ui.theme.*

@Composable
fun PermissionRationaleDialog(
    permission  : String,
    rationale   : String,
    onConfirm   : () -> Unit,
    onDismiss   : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon    = { Icon(Icons.Outlined.Shield, null, tint = CyanElectric) },
        title   = { Text("Autorisation requise", style = MaterialTheme.typography.headlineSmall, color = TextPrimary) },
        text    = { Text(rationale, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) },
        containerColor = BgCard,
        titleContentColor = TextPrimary,
        textContentColor  = TextSecondary,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Continuer", color = CyanElectric, style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Plus tard", color = TextSecondary)
            }
        }
    )
}
```

---

## 19. Animations Material 3 — Catalogue complet

Récapitulatif de toutes les animations Compose utilisées dans l'application :

```kotlin
// ── 1. Transition entre screens (NavHost) ────────────────────────
enterTransition  = { fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.95f) }
exitTransition   = { fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.95f) }

// ── 2. Spring physics sur la magnitude accéléromètre ─────────────
animateFloatAsState(
    targetValue   = magnitude,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness    = Spring.StiffnessLow
    )
)

// ── 3. Couleurs adaptatives (batterie, GPS, sync badges) ─────────
animateColorAsState(targetValue = color, animationSpec = tween(400))

// ── 4. Stagger d'entrée des items HistoryScreen ──────────────────
AnimatedVisibility(
    enter = fadeIn(tween(300, delayMillis = index * 50)) +
            slideInHorizontally(tween(300, delayMillis = index * 50))
)

// ── 5. Point pulsant status service ──────────────────────────────
rememberInfiniteTransition().animateFloat(
    0.3f → 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse)
)

// ── 6. Scan line sur les cartes actives ──────────────────────────
rememberInfiniteTransition().animateFloat(
    0f → 1f, infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart)
)

// ── 7. Radar pulse — cercles Canvas ──────────────────────────────
3x infiniteTransition.animateFloat avec StartOffset décalés de 800ms chacun

// ── 8. Bouton alerte — scale pulsation ───────────────────────────
rememberInfiniteTransition().animateFloat(
    1f → 1.03f, infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse)
)

// ── 9. SwipeToDismiss fond rouge animé ───────────────────────────
animateColorAsState(RedDim → RedAlert selon la cible du swipe)

// ── 10. Slides d'entrée des cartes Dashboard ─────────────────────
AnimatedVisibility(enter = fadeIn(tween(400)) + slideInVertically { it/2 })
// Stagger : delayMillis = 0, 100, 200 pour Dashboard, Battery+GPS, Button

// ── 11. Navigation bar icon colors ───────────────────────────────
animateColorAsState(TextSecondary → CyanElectric, tween(250))
```

---

## 20. SurveillanceService — Foreground Service

```kotlin
package com.guardian.track.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.guardian.track.R
import com.guardian.track.data.local.datastore.UserPreferencesDataStore
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType
import com.guardian.track.util.NotificationHelper
import com.guardian.track.util.SmsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.math.sqrt
import javax.inject.Inject

@AndroidEntryPoint
class SurveillanceService : Service(), SensorEventListener {

    companion object {
        const val CHANNEL_ID_SERVICE   = "guardian_service_channel"
        const val NOTIFICATION_ID      = 1001
        private const val FREE_FALL_DURATION_MS = 100L
        private const val IMPACT_WINDOW_MS      = 200L
        private const val TAG                   = "SurveillanceService"
    }

    @Inject lateinit var incidentRepo      : IncidentRepositoryImpl
    @Inject lateinit var dataStore         : UserPreferencesDataStore
    @Inject lateinit var fusedLocation     : FusedLocationProviderClient
    @Inject lateinit var smsHelper         : SmsHelper
    @Inject lateinit var notificationHelper: NotificationHelper

    private lateinit var sensorManager       : SensorManager
    private var accelerometer                : Sensor? = null
    private lateinit var sensorHandlerThread : HandlerThread
    private val serviceScope                 = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // État algorithme détection chute
    private var freeFallStartTime = 0L
    private var isInFreeFall      = false
    private val freeFallThreshold = 3f
    private var impactThreshold   = 15f   // Paramétrable depuis Settings

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        serviceScope.launch { impactThreshold = dataStore.fallThreshold.first() }
        setupSensor()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null

    // ── Capteur sur HandlerThread dédié ──────────────────────────
    private fun setupSensor() {
        sensorHandlerThread = HandlerThread("SensorThread", Process.THREAD_PRIORITY_MORE_FAVORABLE)
        sensorHandlerThread.start()
        val handler = Handler(sensorHandlerThread.looper)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME, handler)
        } ?: Log.w(TAG, "Pas d'accéléromètre disponible")
    }

    // ── Algorithme de détection de chute (2 phases) ──────────────
    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val ax = event.values[0]; val ay = event.values[1]; val az = event.values[2]
        val magnitude = sqrt(ax * ax + ay * ay + az * az)
        val now = System.currentTimeMillis()

        when {
            // Phase 1 — Free-fall : magnitude quasi nulle
            magnitude < freeFallThreshold && !isInFreeFall -> {
                isInFreeFall      = true
                freeFallStartTime = now
            }
            // Phase 2 — Impact : pic après free-fall dans la fenêtre temporelle
            isInFreeFall && magnitude > impactThreshold -> {
                val duration = now - freeFallStartTime
                if (duration in FREE_FALL_DURATION_MS..(IMPACT_WINDOW_MS + FREE_FALL_DURATION_MS)) {
                    handleFallDetected()
                }
                isInFreeFall = false
            }
            // Timeout de la fenêtre — fausse alerte
            isInFreeFall && (now - freeFallStartTime) > IMPACT_WINDOW_MS + FREE_FALL_DURATION_MS -> {
                isInFreeFall = false
            }
        }

        // Diffuser la magnitude au Dashboard via LocalBroadcast
        sendBroadcast(Intent("com.guardian.track.MAGNITUDE_UPDATE").putExtra("magnitude", magnitude))
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun handleFallDetected() {
        serviceScope.launch {
            val (lat, lon) = getLastKnownLocation()
            incidentRepo.insertIncident(
                Incident(timestamp = System.currentTimeMillis(), type = IncidentType.FALL, latitude = lat, longitude = lon)
            )
            notificationHelper.showFallAlert()
            smsHelper.sendEmergencySms(IncidentType.FALL)
        }
    }

    private suspend fun getLastKnownLocation(): Pair<Double, Double> =
        try {
            val loc = com.google.android.gms.tasks.Tasks.await(fusedLocation.lastLocation)
            Pair(loc?.latitude ?: 0.0, loc?.longitude ?: 0.0)
        } catch (e: Exception) { Pair(0.0, 0.0) }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
            .setContentTitle("GuardianTrack actif")
            .setContentText("Surveillance des capteurs en cours...")
            .setSmallIcon(R.drawable.ic_shield_notification)
            .setOngoing(true).setPriority(NotificationCompat.PRIORITY_LOW).build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID_SERVICE, "Service de surveillance", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        sensorHandlerThread.quitSafely()
        serviceScope.cancel()
    }
}
```

---

## 21. BroadcastReceivers

### `receiver/BatteryReceiver.kt`
```kotlin
package com.guardian.track.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType
import com.guardian.track.util.NotificationHelper
import com.guardian.track.util.SmsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class BatteryReceiver : BroadcastReceiver() {
    @Inject lateinit var incidentRepo: IncidentRepositoryImpl
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var smsHelper: SmsHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BATTERY_LOW) return
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                incidentRepo.insertIncident(
                    Incident(timestamp = System.currentTimeMillis(), type = IncidentType.BATTERY, latitude = 0.0, longitude = 0.0)
                )
                notificationHelper.showBatteryAlert()
                smsHelper.sendEmergencySms(IncidentType.BATTERY)
            } finally { pendingResult.finish() }
        }
    }
}
```

### `receiver/BootReceiver.kt`
```kotlin
package com.guardian.track.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.guardian.track.worker.BootStartWorker

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action !in listOf(Intent.ACTION_BOOT_COMPLETED, "android.intent.action.LOCKED_BOOT_COMPLETED")) return
        context ?: return

        // Android 12+ : WorkManager.setExpedited() au lieu de startForegroundService directement
        val request = OneTimeWorkRequestBuilder<BootStartWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("boot_start_service", ExistingWorkPolicy.KEEP, request)
    }
}
```

---

## 22. ContentProvider

```kotlin
package com.guardian.track.provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.guardian.track.data.local.db.GuardianDatabase
import com.guardian.track.di.ContentProviderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking

class EmergencyContactProvider : ContentProvider() {

    companion object {
        const val AUTHORITY     = "com.guardian.track.provider"
        const val PATH_CONTACTS = "emergency_contacts"
        val CONTENT_URI: Uri    = Uri.parse("content://$AUTHORITY/$PATH_CONTACTS")

        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_CONTACTS, 1)
            addURI(AUTHORITY, "$PATH_CONTACTS/#", 2)
        }
    }

    private lateinit var database: GuardianDatabase

    override fun onCreate(): Boolean {
        database = EntryPointAccessors.fromApplication(
            context!!.applicationContext, ContentProviderEntryPoint::class.java
        ).guardianDatabase()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        // Protection anti-injection : on ignore selection externe, on utilise Room typé
        val contacts = runBlocking { database.contactDao().getAllContactsSync() }
        val cursor = MatrixCursor(arrayOf("_id", "name", "phone_number"))

        when (URI_MATCHER.match(uri)) {
            1 -> contacts.forEach { cursor.addRow(arrayOf(it.id, it.name, it.phoneNumber)) }
            2 -> contacts.find { it.id == ContentUris.parseId(uri) }
                    ?.let { cursor.addRow(arrayOf(it.id, it.name, it.phoneNumber)) }
        }

        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri) = when (URI_MATCHER.match(uri)) {
        1 -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_CONTACTS"
        2 -> "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_CONTACTS"
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    // Lecture seule pour les clients externes
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, s: String?, a: Array<String>?) = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, a: Array<String>?) = 0
}
```

### `di/ContentProviderEntryPoint.kt`
```kotlin
package com.guardian.track.di

import com.guardian.track.data.local.db.GuardianDatabase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ContentProviderEntryPoint {
    fun guardianDatabase(): GuardianDatabase
}
```

---

## 23. WorkManager — Synchronisation Différée

### `worker/SyncWorker.kt`
```kotlin
package com.guardian.track.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.util.NetworkResult
import com.guardian.track.domain.util.toDomain
import dagger.assisted.*

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: IncidentRepositoryImpl
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val unsynced = repo.getUnsyncedIncidents()
            Log.d("SyncWorker", "Syncing ${unsynced.size} incidents")
            var allOk = true
            unsynced.forEach { entity ->
                val result = repo.insertIncident(entity.toDomain())
                if (result is NetworkResult.Error) allOk = false
            }
            if (allOk) Result.success() else Result.retry()
        } catch (e: Exception) {
            Log.e("SyncWorker", e.message ?: "error")
            Result.retry()
        }
    }
}
```

### `worker/BootStartWorker.kt`
```kotlin
package com.guardian.track.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.guardian.track.service.SurveillanceService
import dagger.assisted.*

@HiltWorker
class BootStartWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            ContextCompat.startForegroundService(context, Intent(context, SurveillanceService::class.java))
            Log.i("BootStartWorker", "SurveillanceService démarré après boot")
            Result.success()
        } catch (e: Exception) {
            Log.e("BootStartWorker", e.message ?: "error")
            Result.failure()
        }
    }
}
```

---

## 24. Permissions Dynamiques — Compose Style

```kotlin
package com.guardian.track.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import android.content.pm.PackageManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionManager(private val activity: ComponentActivity) {

    companion object {
        val REQUIRED = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.SEND_SMS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                add(Manifest.permission.POST_NOTIFICATIONS)
        }.toTypedArray()
    }

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results.entries.filter { !it.value }.forEach { (perm, _) ->
            if (!activity.shouldShowRequestPermissionRationale(perm)) {
                showGoToSettingsDialog(perm)
            }
        }
    }

    fun requestAllPermissions() {
        val missing = REQUIRED.filter {
            checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isEmpty()) return
        showRationaleDialog(missing) { launcher.launch(missing.toTypedArray()) }
    }

    private fun showRationaleDialog(missing: List<String>, onConfirm: () -> Unit) {
        val msg = missing.joinToString("\n\n") { perm ->
            when (perm) {
                Manifest.permission.ACCESS_FINE_LOCATION -> "📍 GPS : Pour géolocaliser les incidents et vous aider en cas d'urgence."
                Manifest.permission.SEND_SMS             -> "📱 SMS : Pour envoyer une alerte automatique à votre contact d'urgence."
                Manifest.permission.POST_NOTIFICATIONS   -> "🔔 Notifications : Pour maintenir la surveillance en arrière-plan."
                else -> perm
            }
        }
        MaterialAlertDialogBuilder(activity)
            .setTitle("Autorisations requises")
            .setMessage(msg)
            .setPositiveButton("Continuer") { _, _ -> onConfirm() }
            .setNegativeButton("Plus tard", null)
            .show()
    }

    private fun showGoToSettingsDialog(permission: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Permission bloquée")
            .setMessage("La permission a été refusée définitivement.\nActivez-la dans : Paramètres → Applications → GuardianTrack → Permissions.")
            .setPositiveButton("Ouvrir les Paramètres") { _, _ ->
                activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                })
            }
            .setNegativeButton("Ignorer", null)
            .show()
    }
}
```

---

## 25. Sécurité — EncryptedSharedPreferences

```kotlin
package com.guardian.track.data.local.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME    = "guardian_secure_prefs"
        private const val KEY_EMERGENCY = "encrypted_emergency_number"
        private const val KEY_API       = "encrypted_api_key"
    }

    private val sharedPrefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context, PREFS_NAME, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveEmergencyNumber(n: String) = sharedPrefs.edit().putString(KEY_EMERGENCY, n).apply()
    fun getEmergencyNumber(): String   = sharedPrefs.getString(KEY_EMERGENCY, "") ?: ""
    fun saveApiKey(k: String)          = sharedPrefs.edit().putString(KEY_API, k).apply()
    fun getApiKey(): String            = sharedPrefs.getString(KEY_API, "") ?: ""
}
```

---

## 26. Export CSV/TXT — Scoped Storage

```kotlin
package com.guardian.track.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.guardian.track.domain.model.Incident
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun exportToCsv(incidents: List<Incident>): String? {
        val ts       = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "GuardianTrack_Export_$ts.csv"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, filename)
                    put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/GuardianTrack")
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { os ->
                        os.write(buildCsv(incidents).toByteArray(Charsets.UTF_8))
                    }
                    filename
                }
            } else {
                val dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                dir.mkdirs()
                java.io.File(dir, filename).apply { writeText(buildCsv(incidents), Charsets.UTF_8) }
                filename
            }
        } catch (e: Exception) { Log.e("ExportHelper", e.message ?: "error"); null }
    }

    private fun buildCsv(incidents: List<Incident>): String {
        val sb  = StringBuilder()
        val fmt = SimpleDateFormat("dd/MM/yyyy,HH:mm:ss", Locale.getDefault())
        sb.appendLine("Date,Heure,Type d'incident,Latitude,Longitude,Statut de synchronisation")
        incidents.forEach { i ->
            val lat  = if (i.latitude  == 0.0) "N/A" else "%.6f".format(i.latitude)
            val lon  = if (i.longitude == 0.0) "N/A" else "%.6f".format(i.longitude)
            val sync = if (i.isSynced) "Synchronisé" else "Non synchronisé"
            sb.appendLine("${fmt.format(Date(i.timestamp))},${i.type.name},$lat,$lon,$sync")
        }
        return sb.toString()
    }
}
```

---

## 27. SMS Manager — Mode Simulation

```kotlin
package com.guardian.track.util

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import com.guardian.track.data.local.datastore.UserPreferencesDataStore
import com.guardian.track.data.local.security.EncryptedPreferencesManager
import com.guardian.track.domain.model.IncidentType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: UserPreferencesDataStore,
    private val encryptedPrefs: EncryptedPreferencesManager,
    private val notificationHelper: NotificationHelper
) {
    fun sendEmergencySms(type: IncidentType) {
        val isSimulation = runBlocking { dataStore.smsSimulationEnabled.first() }
        val phoneNumber  = encryptedPrefs.getEmergencyNumber()
        if (phoneNumber.isBlank()) { Log.w("SmsHelper", "Aucun numéro configuré"); return }

        val message = when (type) {
            IncidentType.FALL    -> "⚠ GuardianTrack : Chute détectée ! Contactez ce numéro immédiatement."
            IncidentType.BATTERY -> "🔋 GuardianTrack : Batterie critique. L'appareil peut s'éteindre."
            IncidentType.MANUAL  -> "📍 GuardianTrack : Alerte manuelle déclenchée."
        }

        if (isSimulation) {
            // MODE SIMULATION (par défaut) → notification + log
            Log.i("SmsHelper", "SMS SIMULATION → $phoneNumber : $message")
            notificationHelper.showSimulatedSms(phoneNumber, message)
        } else {
            try {
                val sms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    context.getSystemService(SmsManager::class.java)
                else @Suppress("DEPRECATION") SmsManager.getDefault()
                sms?.sendTextMessage(phoneNumber, null, message, null, null)
                Log.i("SmsHelper", "SMS réel envoyé à $phoneNumber")
            } catch (e: Exception) { Log.e("SmsHelper", "Erreur SMS: ${e.message}") }
        }
    }
}
```

### `util/NotificationHelper.kt`
```kotlin
package com.guardian.track.util

import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.guardian.track.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val CHANNEL_ALERTS = "guardian_alerts_channel"
        const val ID_FALL = 2001; const val ID_BATT = 2002; const val ID_SMS = 2003
    }

    private val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init { createChannels() }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_ALERTS, "Alertes de sécurité", NotificationManager.IMPORTANCE_HIGH)
                    .apply { enableVibration(true) }
            )
        }
    }

    fun showFallAlert() = nm.notify(ID_FALL,
        NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("⚠ Chute détectée !")
            .setContentText("Chute enregistrée. SMS d'urgence envoyé.")
            .setSmallIcon(R.drawable.ic_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true).build()
    )

    fun showBatteryAlert() = nm.notify(ID_BATT,
        NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("🔋 Batterie critique")
            .setContentText("Niveau critique. Incident enregistré.")
            .setSmallIcon(R.drawable.ic_battery_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true).build()
    )

    fun showSimulatedSms(phone: String, msg: String) = nm.notify(ID_SMS,
        NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("📱 SMS simulé → $phone")
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setSmallIcon(R.drawable.ic_sms_simulated)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true).build()
    )
}
```

---

## 28. AndroidManifest.xml Complet

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.guardian.track">

    <!-- ══ PERMISSIONS ══════════════════════════════════════════ -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_HEALTH" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <uses-feature android:name="android.hardware.sensor.accelerometer" android:required="true" />

    <!-- ══ PERMISSION ContentProvider (signature|privileged) ════ -->
    <permission
        android:name="com.guardian.track.READ_EMERGENCY_CONTACTS"
        android:protectionLevel="signature|privileged"
        android:label="Lire les contacts d'urgence GuardianTrack" />

    <uses-permission android:name="com.guardian.track.READ_EMERGENCY_CONTACTS" />

    <!-- ══ APPLICATION ══════════════════════════════════════════ -->
    <application
        android:name=".GuardianTrackApplication"
        android:allowBackup="false"
        android:icon="@mipmap/ic_launcher"
        android:label="GuardianTrack"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.GuardianTrack"
        tools:targetApi="35">

        <!-- MainActivity — Compose setContent -->
        <activity
            android:name=".ui.MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- SurveillanceService — Foreground -->
        <service
            android:name=".service.SurveillanceService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="location|health"
            android:stopWithTask="false" />

        <!-- BroadcastReceivers -->
        <receiver android:name=".receiver.BatteryReceiver" android:enabled="true" android:exported="false">
            <intent-filter>
                <action android:name="android.intent.action.BATTERY_LOW" />
            </intent-filter>
        </receiver>

        <receiver android:name=".receiver.BootReceiver" android:enabled="true" android:exported="false">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.LOCKED_BOOT_COMPLETED" />
            </intent-filter>
        </receiver>

        <!-- ContentProvider sécurisé -->
        <provider
            android:name=".provider.EmergencyContactProvider"
            android:authorities="com.guardian.track.provider"
            android:enabled="true"
            android:exported="true"
            android:readPermission="com.guardian.track.READ_EMERGENCY_CONTACTS"
            android:writePermission="com.guardian.track.READ_EMERGENCY_CONTACTS" />

    </application>
</manifest>
```

> ⚠️ **Thème dans Manifest** : Le `android:theme="@style/Theme.GuardianTrack"` doit hériter de `Theme.Material3.DayNight.NoActionBar` dans `res/values/themes.xml` (ce fichier XML reste nécessaire même avec Compose). Pas de layout XML, mais les ressources de thème système le sont.

---

## 29. Rapport Technique — Réponses aux 6 Questions

### Q1 — Flow vs LiveData pour Room/ViewModel

Dans GuardianTrack, `Flow<List<IncidentEntity>>` est préféré pour trois raisons contextualisées :

1. **Transformations chaînées** : `incidentDao.getAllIncidents().map { list -> list.map { it.toDomain() } }` se compose naturellement en Kotlin. Avec LiveData, cela nécessite `Transformations.map()` qui est moins idiomatique.

2. **`stateIn()` pour Compose** : `collectAsStateWithLifecycle()` dans les composables fonctionne directement avec `StateFlow`. LiveData nécessite `.observeAsState()` avec un import supplémentaire et le bridge `lifecycle-livedata-compose`.

3. **Tests Compose** : Avec `Turbine`, on teste les flows directement : `viewModel.uiState.test { assertThat(awaitItem().incidents).isEmpty() }`. LiveData nécessiterait `InstantTaskExecutorRule`.

**Cas LiveData pertinent dans ce projet** : L'état booléen `isServiceRunning` aurait pu être une `LiveData<Boolean>` simple observée par le receiver de magnitude — cas sans transformation et sans besoin de composition.

---

### Q2 — GPS refusé définitivement

Stratégie de repli implémentée dans `PermissionManager` :

1. `shouldShowRequestPermissionRationale()` retourne `false` → `showGoToSettingsDialog()` → `Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)`.
2. Dans `SurveillanceService.getLastKnownLocation()`, le bloc `try/catch` retourne `Pair(0.0, 0.0)` — valeur sentinelle.
3. L'`IncidentEntity` est enregistré avec `latitude = 0.0, longitude = 0.0`.
4. Dans `IncidentItem` Compose : `if (lat == 0.0 && lon == 0.0)` → affichage "Localisation indisponible".
5. Dans le CSV exporté : colonnes `N/A` pour ces incidents.

L'incident est toujours enregistré et synchronisé. La sécurité primaire (alerte + SMS) n'est pas bloquée par l'absence de GPS.

---

### Q3 — Sécurité du ContentProvider & Content Provider Injection

**Attaque Content Provider Injection** : Si notre `query()` utilisait `database.rawQuery(selection, selectionArgs)` en passant directement les arguments de l'appelant externe, une app malveillante pourrait appeler `query(uri, null, "1=1; DROP TABLE incidents;--", null, null)` et corrompre la base.

**Notre protection dans `EmergencyContactProvider.query()`** :
- Les paramètres `selection` et `selectionArgs` reçus sont **ignorés**.
- On appelle `database.contactDao().getAllContactsSync()` — requête Room précompilée avec paramètres typés, sans SQL dynamique externe.
- L'appelant n'a aucune influence sur la requête réelle.

**Limites résiduelles** :
- `runBlocking` dans `query()` bloque le Binder thread — acceptable pour des listes petites, problème pour des volumes importants.
- La protection `signature|privileged` signifie qu'une app système préinstallée par le fabricant pourrait accéder au provider.

---

### Q4 — Restrictions Android 12+ et BOOT_COMPLETED

Android 12 (API 31) interdit `startForegroundService()` ou `startService()` depuis un `BroadcastReceiver` sauf exceptions listées (appel entrant, PendingIntent de notification, etc.). `BOOT_COMPLETED` n'est pas dans ces exceptions.

**Notre implémentation** :
- `BootReceiver.onReceive()` → `WorkManager.enqueueUniqueWork("boot_start_service", KEEP, BootStartWorker avec setExpedited())`.
- `BootStartWorker.doWork()` → `ContextCompat.startForegroundService()`. WorkManager est lui-même considéré comme un contexte d'exécution "premier plan" par l'OS.
- `OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST` : si le quota expedited est épuisé (rare au démarrage), le worker s'exécute en mode standard — légèrement différé mais sans crash.
- `LOCKED_BOOT_COMPLETED` est aussi capturé pour démarrer avant le déverrouillage (Direct Boot mode).

---

### Q5 — Séparation Entity / DTO / DomainModel

| Couche | Classe | Rôle spécifique GuardianTrack |
|--------|--------|-------------------------------|
| Room | `IncidentEntity` | `@ColumnInfo(name = "is_synced")` — schéma SQL exact |
| Réseau | `IncidentDto` | `@SerializedName("timestamp")` — mapping JSON MockAPI |
| UI/Domain | `Incident` | `formattedDate`, `formattedTime`, `IncidentType` enum |

**Exemple concret de valeur** : Le composant `IncidentItem` Compose reçoit un `Incident` (DomainModel). Il affiche `incident.formattedDate` et `incident.type` (enum). Si MockAPI change son format de date → on modifie uniquement `IncidentDto` + `Mapper`. Room et Compose ne changent pas. Inversement, si on change le style d'affichage de la date dans `Incident.formattedDate` → Room et Retrofit ne changent pas.

---

### Q6 — WorkManager vs JobScheduler vs AlarmManager

| | AlarmManager | JobScheduler | WorkManager |
|--|--|--|--|
| Survie redémarrage | Non (sans setup) | Oui | Oui |
| Mode Doze | Non | Partiel | Oui (expedited) |
| Contrainte réseau | Non native | Oui | Oui |
| Compatibilité Hilt | Non | Non | `@HiltWorker` |
| API minimale | 1 | 21 | 14+ |

**Choix WorkManager pour GuardianTrack** : Un incident de chute peut être capturé hors ligne. WorkManager garantit l'envoi différé même après redémarrage ou Doze. `@HiltWorker` avec `@AssistedInject` permet d'injecter `IncidentRepositoryImpl` directement dans `SyncWorker` sans factory manuelle. `JobScheduler` aurait nécessité du code boilerplate sans bénéfice. `AlarmManager` ne gère pas les contraintes réseau nativement et ne survit pas au redémarrage sans `BOOT_COMPLETED` additionnel.

---

## 30. README.md du Projet

````markdown
# 🛡️ GuardianTrack

Application Android de sécurité personnelle — Kotlin + Jetpack Compose + MVVM + Hilt

## ⚡ Tech Stack

- **UI** : Jetpack Compose 100% (zéro XML layout) — BONUS +5 pts
- **Architecture** : MVVM + Hilt DI + Repository Pattern
- **Async** : Coroutines + StateFlow (zéro AsyncTask, zéro RxJava)
- **Local** : Room + DataStore + EncryptedSharedPreferences
- **Réseau** : Retrofit + OkHttp + WorkManager (offline-first)
- **Capteurs** : SensorManager sur HandlerThread dédié
- **4 Piliers** : Activity, Service (Foreground), BroadcastReceiver × 2, ContentProvider

## 🚀 Configuration

### 1. `local.properties` (NON versionné — dans .gitignore)

```properties
sdk.dir=/path/to/your/Android/sdk
API_BASE_URL=https://YOUR_PROJECT.mockapi.io/
API_KEY=your_api_key_here
```

### 2. Backend MockAPI

Créez un endpoint `/incidents` sur [mockapi.io](https://mockapi.io) avec :
`id`, `timestamp` (Long), `type` (String), `latitude` (Double), `longitude` (Double)

### 3. Build

```bash
./gradlew assembleDebug    # Debug
./gradlew assembleRelease  # Release (minifié)
```

## 🔒 Sécurité

- `local.properties` dans `.gitignore` — clé API jamais versionnée
- Numéro d'urgence chiffré AES-256-GCM via `EncryptedSharedPreferences`
- ContentProvider protégé `signature|privileged`
- Mode simulation SMS **ACTIF par défaut**

## 📱 Mode Simulation SMS

Par défaut, les SMS sont remplacés par des notifications. Pour le mode réel : `Config → Mode Simulation SMS → désactiver`. Tout envoi de SMS réel est de la responsabilité de l'utilisateur.
````

---

## 31. Checklist Finale

### ✅ Compose — Spécifique au Bonus
- [ ] `compose = true` dans `buildFeatures` (pas de `viewBinding`)
- [ ] Plugin `compose.compiler` ajouté pour Kotlin 2.0+
- [ ] BOM Compose utilisé pour gérer les versions
- [ ] `GuardianTheme` wrappant `MaterialTheme` avec `ColorScheme`, `Typography`, `Shapes` personnalisés
- [ ] `collectAsStateWithLifecycle()` utilisé dans tous les screens (pas `collectAsState()`)
- [ ] `hiltViewModel()` utilisé dans les screens (pas constructeur direct)
- [ ] Aucun fichier XML de layout dans `res/layout/`
- [ ] `setContent { GuardianTheme { GuardianApp() } }` dans `MainActivity`
- [ ] `NavHost` Compose avec transitions `fadeIn + scaleIn`

### ✅ Animations Material 3
- [ ] `rememberInfiniteTransition` pour radar pulse (Canvas)
- [ ] `rememberInfiniteTransition` pour scan line sur cartes actives
- [ ] `animateFloatAsState` + `spring()` pour magnitude accéléromètre
- [ ] `animateColorAsState` pour batterie, GPS, badges de sync
- [ ] `AnimatedVisibility` avec stagger (delayMillis) pour cartes Dashboard
- [ ] `AnimatedVisibility` avec délai par index pour items History
- [ ] `animateFloatAsState` pour scale du bouton alerte (pulsation)
- [ ] `SwipeToDismissBox` pour suppression incidents
- [ ] Transitions `enterTransition / exitTransition` dans NavHost

### ✅ Architecture & Code
- [ ] `@HiltAndroidApp` sur `GuardianTrackApplication`
- [ ] `@AndroidEntryPoint` sur `MainActivity`, `SurveillanceService`, `BatteryReceiver`
- [ ] Aucun `AsyncTask`, `Thread`, `RxJava` — uniquement Coroutines/Flow
- [ ] `viewModelScope` dans tous les ViewModels
- [ ] `StateFlow` exposé en immuable depuis `MutableStateFlow` privé
- [ ] `stateIn(WhileSubscribed(5000))` pour flows Room → StateFlow
- [ ] Séparation Entity / DTO / DomainModel dans `Mapper.kt`
- [ ] Aucune logique métier dans les Composables (UI only)

### ✅ Les 4 Piliers Android
- [ ] **Activity** : `MainActivity` → `setContent` → `GuardianApp()`
- [ ] **Screen 1** : `DashboardScreen` — magnitude animée, batterie, GPS, bouton alerte pulsant
- [ ] **Screen 2** : `HistoryScreen` — `LazyColumn` + `SwipeToDismissBox` + FAB export
- [ ] **Screen 3** : `SettingsScreen` — Slider, Switch, TextField, badges avertissement
- [ ] **Service** : `SurveillanceService` Foreground + algorithme chute 2 phases + HandlerThread
- [ ] **Receiver 1** : `BatteryReceiver` statique + `goAsync()`
- [ ] **Receiver 2** : `BootReceiver` → `WorkManager.setExpedited()`
- [ ] **ContentProvider** : URI contractuelle + anti-injection + permission `signature|privileged`

### ✅ Persistance & Réseau
- [ ] Room : 2 entités, 2 DAO, getAllIncidents() retourne `Flow<List<>>`
- [ ] DataStore : seuil Float, darkMode Boolean, smsSimulation Boolean (default true)
- [ ] `EncryptedSharedPreferences` AES-256-GCM pour numéro urgence
- [ ] Retrofit : `suspend fun` + `NetworkResult<T>` sealed class
- [ ] Stratégie offline-first : réseau → Retrofit → markAsSynced ; pas de réseau → WorkManager
- [ ] Export CSV via `MediaStore` dans `Documents/GuardianTrack/`

### ✅ Sécurité & Permissions
- [ ] `ACCESS_FINE_LOCATION` dynamique avec rationale pédagogique
- [ ] `SEND_SMS` dynamique avec rationale pédagogique
- [ ] `POST_NOTIFICATIONS` dynamique sur Android 13+ avec rationale
- [ ] Refus définitif → dialog → `Settings.ACTION_APPLICATION_DETAILS_SETTINGS`
- [ ] GPS refusé → latitude = 0.0, longitude = 0.0 (valeur sentinelle)
- [ ] `local.properties` dans `.gitignore`

### ✅ Android 12+ Contraintes
- [ ] `BootReceiver` → WorkManager + `setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)`
- [ ] `android:foregroundServiceType="location|health"` dans Manifest
- [ ] `POST_NOTIFICATIONS` demandée dynamiquement (API 33+)
- [ ] `LOCKED_BOOT_COMPLETED` en plus de `BOOT_COMPLETED`

### ✅ Livrables
- [ ] Code GitHub (commits réguliers, messages clairs)
- [ ] `.gitignore` correct (`local.properties` exclu)
- [ ] `README.md` : config API, build, modules, note sur simulation SMS
- [ ] Rapport PDF 5-10 pages : 6 questions contextualisées + diagramme MVVM + schéma Room
- [ ] Vidéo 3-5 min : (1) simulation chute → (2) SMS simulé notification → (3) Room dans History → (4) export CSV
- [ ] **Deadline : Samedi 18 Avril 2026**

---

> 🎯 **Score visé avec le bonus Compose** : 100 + 5 = **105/100**
>
> *Document d'implémentation — GuardianTrack — ISET Rades 2025/2026*
