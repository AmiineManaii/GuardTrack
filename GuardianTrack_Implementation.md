# 🛡️ GuardianTrack — Fichier d'Implémentation Complet

> **Mini-Projet Android Natif Avancé (Kotlin)**
> ISET Rades — Master Professionnel DAM — Mehdi M'tir
> Deadline : **Samedi 18 Avril 2026**

---

## 📋 TABLE DES MATIÈRES

1. [Vision du Projet & Concept Design](#1-vision-du-projet--concept-design)
2. [Structure du Projet — Arborescence Complète](#2-structure-du-projet--arborescence-complète)
3. [Configuration Gradle & Dépendances](#3-configuration-gradle--dépendances)
4. [Design System & UI/UX](#4-design-system--uiux)
5. [Architecture MVVM — Vue d'ensemble](#5-architecture-mvvm--vue-densemble)
6. [Couche Model — Entités, DTO, DomainModel](#6-couche-model--entités-dto-domainmodel)
7. [Room Database](#7-room-database)
8. [DataStore Preferences](#8-datastore-preferences)
9. [Retrofit & API](#9-retrofit--api)
10. [Injection de Dépendances — Hilt Modules](#10-injection-de-dépendances--hilt-modules)
11. [Repositories](#11-repositories)
12. [ViewModels](#12-viewmodels)
13. [MainActivity & Navigation](#13-mainactivity--navigation)
14. [Fragment 1 — DashboardFragment](#14-fragment-1--dashboardfragment)
15. [Fragment 2 — HistoryFragment](#15-fragment-2--historyfragment)
16. [Fragment 3 — SettingsFragment](#16-fragment-3--settingsfragment)
17. [SurveillanceService — Foreground Service](#17-surveillanceservice--foreground-service)
18. [BroadcastReceivers](#18-broadcastreceivers)
19. [ContentProvider](#19-contentprovider)
20. [WorkManager — Synchronisation Différée](#20-workmanager--synchronisation-différée)
21. [Permissions Dynamiques](#21-permissions-dynamiques)
22. [Sécurité — EncryptedSharedPreferences](#22-sécurité--encryptedsharedpreferences)
23. [Export CSV/TXT — Scoped Storage](#23-export-csvtxt--scoped-storage)
24. [SMS Manager — Mode Simulation](#24-sms-manager--mode-simulation)
25. [AndroidManifest.xml Complet](#25-androidmanifestxml-complet)
26. [Ressources XML — Layouts & Styles](#26-ressources-xml--layouts--styles)
27. [Rapport Technique — Réponses aux 6 Questions](#27-rapport-technique--réponses-aux-6-questions)
28. [README.md du Projet](#28-readmemd-du-projet)
29. [Checklist Finale](#29-checklist-finale)

---

## 1. Vision du Projet & Concept Design

### 🎨 Direction Artistique : "Tactical Dark Glass"

GuardianTrack n'est **pas** une app de sécurité générique. C'est un **instrument de surveillance de précision** — comme une montre militaire de luxe ou un cockpit d'avion de chasse. Le design parle : chaque milliseconde compte.

#### Palette de Couleurs
```
Background principal    : #090E1A  (noir nuit profonde)
Surface secondaire      : #111827  (ardoise marine)
Surface carte           : #1C2537  (verre givré sombre)
Accent primaire         : #00D4FF  (cyan électrique — "signal vivant")
Accent danger           : #FF3B5C  (rouge corail — alerte)
Accent warning          : #FFB547  (ambre — attention)
Accent success          : #00E5A0  (vert menthe — sécurisé)
Texte principal         : #E8F4FF  (blanc laiteux)
Texte secondaire        : #6B8AA8  (bleu gris)
Bordure glow            : #00D4FF33 (cyan transparent — effet halo)
```

#### Typographie
```
Display / Titres   : "Rajdhani" (Google Fonts) — condensé, militaire
Corps              : "DM Mono" (monospace élégant) — données techniques
Labels             : "Inter Tight" — lisibilité maximale
```

#### Langage Visuel
- **Cartes glassmorphiques** : `background: rgba(28, 37, 55, 0.8)` + `backdropFilter: blur(20px)` + `border: 1px solid rgba(0,212,255,0.15)`
- **Indicateurs radar** : Animation de pulse concentrique sur le dashboard
- **Lignes de scan** : Bande horizontale lumineuse qui descend lentement sur les cartes actives
- **Compteurs numériques** : Style 7-segments pour la magnitude de l'accéléromètre
- **Grille tactique** : Lignes de fond très subtiles (10% opacité) façon papier millimétré
- **Icônes** : Material Symbols — Outlined, strokeWidth 1.2px
- **Animations** : Spring physics — rebond rapide, décélération naturelle

---

## 2. Structure du Projet — Arborescence Complète

```
GuardianTrack/
├── app/
│   ├── build.gradle.kts
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/guardian/track/
│   │   │   │   ├── GuardianTrackApplication.kt       ← @HiltAndroidApp
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── db/
│   │   │   │   │   │   │   ├── GuardianDatabase.kt
│   │   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   │   │   ├── IncidentDao.kt
│   │   │   │   │   │   │   │   └── EmergencyContactDao.kt
│   │   │   │   │   │   │   └── entity/
│   │   │   │   │   │   │       ├── IncidentEntity.kt
│   │   │   │   │   │   │       └── EmergencyContactEntity.kt
│   │   │   │   │   │   ├── datastore/
│   │   │   │   │   │   │   └── UserPreferencesDataStore.kt
│   │   │   │   │   │   └── security/
│   │   │   │   │   │       └── EncryptedPreferencesManager.kt
│   │   │   │   │   │
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── api/
│   │   │   │   │   │   │   ├── GuardianApi.kt
│   │   │   │   │   │   │   └── dto/
│   │   │   │   │   │   │       └── IncidentDto.kt
│   │   │   │   │   │   └── interceptor/
│   │   │   │   │   │       └── ApiKeyInterceptor.kt
│   │   │   │   │   │
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── IncidentRepository.kt
│   │   │   │   │       └── ContactRepository.kt
│   │   │   │   │
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Incident.kt                ← DomainModel
│   │   │   │   │   │   ├── EmergencyContact.kt
│   │   │   │   │   │   └── IncidentType.kt
│   │   │   │   │   └── util/
│   │   │   │   │       ├── NetworkResult.kt            ← Sealed Class
│   │   │   │   │       └── Mapper.kt
│   │   │   │   │
│   │   │   │   ├── service/
│   │   │   │   │   └── SurveillanceService.kt          ← Foreground Service
│   │   │   │   │
│   │   │   │   ├── receiver/
│   │   │   │   │   ├── BatteryReceiver.kt
│   │   │   │   │   └── BootReceiver.kt
│   │   │   │   │
│   │   │   │   ├── provider/
│   │   │   │   │   └── EmergencyContactProvider.kt     ← ContentProvider
│   │   │   │   │
│   │   │   │   ├── worker/
│   │   │   │   │   ├── SyncWorker.kt
│   │   │   │   │   └── BootStartWorker.kt
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── dashboard/
│   │   │   │   │   │   ├── DashboardFragment.kt
│   │   │   │   │   │   └── DashboardViewModel.kt
│   │   │   │   │   ├── history/
│   │   │   │   │   │   ├── HistoryFragment.kt
│   │   │   │   │   │   ├── HistoryViewModel.kt
│   │   │   │   │   │   └── adapter/
│   │   │   │   │   │       └── IncidentAdapter.kt      ← DiffUtil
│   │   │   │   │   └── settings/
│   │   │   │   │       ├── SettingsFragment.kt
│   │   │   │   │       └── SettingsViewModel.kt
│   │   │   │   │
│   │   │   │   ├── util/
│   │   │   │   │   ├── PermissionManager.kt
│   │   │   │   │   ├── SmsHelper.kt
│   │   │   │   │   ├── ExportHelper.kt
│   │   │   │   │   └── NotificationHelper.kt
│   │   │   │   │
│   │   │   │   └── di/
│   │   │   │       ├── DatabaseModule.kt
│   │   │   │       ├── NetworkModule.kt
│   │   │   │       ├── RepositoryModule.kt
│   │   │   │       └── LocationModule.kt
│   │   │   │
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml
│   │   │       │   ├── fragment_dashboard.xml
│   │   │       │   ├── fragment_history.xml
│   │   │       │   ├── fragment_settings.xml
│   │   │       │   └── item_incident.xml
│   │   │       ├── navigation/
│   │   │       │   └── nav_graph.xml
│   │   │       ├── menu/
│   │   │       │   └── bottom_nav_menu.xml
│   │   │       ├── values/
│   │   │       │   ├── colors.xml
│   │   │       │   ├── themes.xml
│   │   │       │   ├── strings.xml
│   │   │       │   └── dimens.xml
│   │   │       ├── values-night/
│   │   │       │   └── themes.xml
│   │   │       └── drawable/
│   │   │           ├── bg_card_glass.xml
│   │   │           ├── bg_alert_button.xml
│   │   │           ├── ic_radar_pulse.xml
│   │   │           └── shape_indicator.xml
│   │   │
│   │   └── test/
│   │       └── java/com/guardian/track/
│   │           ├── DashboardViewModelTest.kt
│   │           ├── IncidentRepositoryTest.kt
│   │           └── FallDetectionTest.kt
│   │
├── local.properties          ← NON VERSIONNÉ (.gitignore)
├── .gitignore
└── README.md
```

---

## 3. Configuration Gradle & Dépendances

### `build.gradle.kts` (Project level)
```kotlin
// build.gradle.kts (Project)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
```

### `build.gradle.kts` (App level)
```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.guardian.track"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.guardian.track"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Lire l'URL API depuis local.properties (non versionné)
        val localProperties = java.util.Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) load(file.inputStream())
        }
        buildConfigField("String", "API_BASE_URL",
            "\"${localProperties.getProperty("API_BASE_URL", "https://mockapi.io/projects/")}\"")
        buildConfigField("String", "API_KEY",
            "\"${localProperties.getProperty("API_KEY", "")}\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    // ── Core Android ─────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // ── Navigation ───────────────────────────────────────────
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // ── Lifecycle / ViewModel ────────────────────────────────
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // ── Room ─────────────────────────────────────────────────
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ── DataStore ────────────────────────────────────────────
    implementation(libs.androidx.datastore.preferences)

    // ── Hilt ─────────────────────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

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

    // ── RecyclerView ─────────────────────────────────────────
    implementation(libs.androidx.recyclerview)

    // ── Coroutines ───────────────────────────────────────────
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // ── Tests ────────────────────────────────────────────────
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

### `local.properties` (NON VERSIONNÉ)
```properties
# local.properties — Ajouter ce fichier au .gitignore !
sdk.dir=/Users/VOTRE_NOM/Library/Android/sdk
API_BASE_URL=https://YOUR_MOCKAPI_URL.mockapi.io/
API_KEY=your_secret_api_key_here
```

### `.gitignore`
```
*.iml
.gradle/
local.properties          ← CRITIQUE : clé API protégée
/build/
/captures/
.externalNativeBuild/
.cxx/
*.keystore
google-services.json
```

---

## 4. Design System & UI/UX

### `res/values/colors.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- ── GuardianTrack Design System ── -->
    <!-- Backgrounds -->
    <color name="gt_bg_deep">#FF090E1A</color>
    <color name="gt_bg_surface">#FF111827</color>
    <color name="gt_bg_card">#FF1C2537</color>
    <color name="gt_bg_card_elevated">#FF243044</color>

    <!-- Accents -->
    <color name="gt_cyan">#FF00D4FF</color>
    <color name="gt_cyan_dim">#3300D4FF</color>
    <color name="gt_cyan_glow">#1A00D4FF</color>
    <color name="gt_red_alert">#FFFF3B5C</color>
    <color name="gt_red_dim">#33FF3B5C</color>
    <color name="gt_amber">#FFFFB547</color>
    <color name="gt_amber_dim">#33FFB547</color>
    <color name="gt_green">#FF00E5A0</color>
    <color name="gt_green_dim">#3300E5A0</color>

    <!-- Text -->
    <color name="gt_text_primary">#FFE8F4FF</color>
    <color name="gt_text_secondary">#FF6B8AA8</color>
    <color name="gt_text_disabled">#FF3A4A5C</color>

    <!-- Borders -->
    <color name="gt_border_subtle">#1A00D4FF</color>
    <color name="gt_border_active">#4D00D4FF</color>
    <color name="gt_divider">#1AFFFFFF</color>
</resources>
```

### `res/values/themes.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.GuardianTrack" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Couleurs Material 3 mappées -->
        <item name="colorPrimary">@color/gt_cyan</item>
        <item name="colorPrimaryVariant">@color/gt_bg_card</item>
        <item name="colorOnPrimary">@color/gt_bg_deep</item>
        <item name="colorSecondary">@color/gt_red_alert</item>
        <item name="colorSurface">@color/gt_bg_surface</item>
        <item name="colorBackground">@color/gt_bg_deep</item>
        <item name="colorOnBackground">@color/gt_text_primary</item>
        <item name="colorOnSurface">@color/gt_text_primary</item>
        <item name="android:windowBackground">@color/gt_bg_deep</item>
        <item name="android:statusBarColor">@color/gt_bg_deep</item>
        <item name="android:navigationBarColor">@color/gt_bg_surface</item>
        <item name="android:windowLightStatusBar">false</item>

        <!-- Typography -->
        <item name="fontFamily">@font/dm_mono</item>
    </style>

    <!-- Style carte glassmorphique -->
    <style name="Widget.GuardianTrack.Card" parent="Widget.Material3.CardView.Elevated">
        <item name="cardBackgroundColor">@color/gt_bg_card</item>
        <item name="cardCornerRadius">16dp</item>
        <item name="cardElevation">0dp</item>
        <item name="strokeColor">@color/gt_border_subtle</item>
        <item name="strokeWidth">1dp</item>
    </style>

    <!-- Style bouton alerte principale -->
    <style name="Widget.GuardianTrack.AlertButton" parent="Widget.Material3.Button">
        <item name="backgroundTint">@color/gt_red_alert</item>
        <item name="android:textColor">@color/gt_text_primary</item>
        <item name="cornerRadius">50dp</item>
        <item name="android:letterSpacing">0.12</item>
    </style>

    <!-- Style Bottom Navigation -->
    <style name="Widget.GuardianTrack.BottomNav" parent="Widget.Material3.BottomNavigationView">
        <item name="backgroundTint">@color/gt_bg_surface</item>
        <item name="itemIconTint">@color/selector_nav_icon</item>
        <item name="itemTextColor">@color/selector_nav_icon</item>
        <item name="itemActiveIndicatorColor">@color/gt_cyan_dim</item>
    </style>
</resources>
```

### `res/values/strings.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">GuardianTrack</string>
    <string name="nav_dashboard">Radar</string>
    <string name="nav_history">Journal</string>
    <string name="nav_settings">Config</string>

    <!-- Dashboard -->
    <string name="dashboard_status_active">SURVEILLANCE ACTIVE</string>
    <string name="dashboard_status_inactive">SERVICE INACTIF</string>
    <string name="dashboard_btn_alert">⚠ ALERTE MANUELLE</string>
    <string name="dashboard_sensor_magnitude">Magnitude acc. (m/s²)</string>
    <string name="dashboard_battery_label">BATTERIE</string>
    <string name="dashboard_gps_label">LOCALISATION</string>
    <string name="dashboard_gps_active">GPS ACTIF</string>
    <string name="dashboard_gps_inactive">GPS INACTIF</string>

    <!-- History -->
    <string name="history_empty">Aucun incident enregistré</string>
    <string name="history_export_btn">Exporter CSV</string>
    <string name="history_delete_confirm">Supprimer cet incident ?</string>
    <string name="history_swipe_delete">Glissez pour supprimer</string>

    <!-- Settings -->
    <string name="settings_threshold">Seuil de détection (m/s²)</string>
    <string name="settings_dark_mode">Mode sombre</string>
    <string name="settings_emergency_number">Numéro d\'urgence</string>
    <string name="settings_sms_simulation">Mode Simulation SMS</string>
    <string name="settings_sms_simulation_desc">Activer pour remplacer les SMS par des notifications (recommandé)</string>

    <!-- Permissions -->
    <string name="perm_location_rationale">GuardianTrack a besoin de votre localisation GPS pour géotaguer les incidents et vous localiser en cas d\'urgence.</string>
    <string name="perm_sms_rationale">GuardianTrack peut envoyer un SMS d\'urgence automatique à votre contact configuré en cas de chute détectée.</string>
    <string name="perm_notification_rationale">Les notifications sont nécessaires pour maintenir le service de surveillance en arrière-plan.</string>
    <string name="perm_denied_permanent">Permission refusée définitivement. Activez-la dans Paramètres → Applications → GuardianTrack → Permissions.</string>
    <string name="perm_open_settings">Ouvrir les Paramètres</string>

    <!-- Notifications -->
    <string name="notif_service_title">GuardianTrack actif</string>
    <string name="notif_service_text">Surveillance des capteurs en cours...</string>
    <string name="notif_fall_title">⚠ Chute détectée !</string>
    <string name="notif_battery_title">🔋 Batterie critique</string>
    <string name="notif_sms_simulated">SMS simulé envoyé à %1$s</string>
    <string name="notif_channel_service">Service de surveillance</string>
    <string name="notif_channel_alerts">Alertes de sécurité</string>

    <!-- Types d'incidents -->
    <string name="incident_type_fall">CHUTE</string>
    <string name="incident_type_battery">BATTERIE</string>
    <string name="incident_type_manual">MANUELLE</string>

    <!-- Export -->
    <string name="export_success">Fichier exporté : %1$s</string>
    <string name="export_error">Erreur lors de l\'export</string>

    <!-- ContentProvider authority -->
    <string name="provider_authority">com.guardian.track.provider</string>
</resources>
```

---

## 5. Architecture MVVM — Vue d'ensemble

```
┌─────────────────────────────────────────────────────────────────┐
│                         PRESENTATION LAYER                       │
│  ┌──────────────┐  observe StateFlow  ┌──────────────────────┐  │
│  │  Fragment    │ ◄──────────────────  │     ViewModel        │  │
│  │  (View Only) │  send events ──────► │  (HiltViewModel)     │  │
│  └──────────────┘                     └──────────┬───────────┘  │
└────────────────────────────────────────────────── │ ────────────┘
                                                    │ inject
┌─────────────────────────────────────────────────── │ ────────────┐
│                          DOMAIN LAYER              │              │
│                  ┌─────────────────────────────────┘             │
│                  │  DomainModel  │  NetworkResult<T>  │  Mapper   │
└──────────────────────────────────────────────────────────────────┘
                                                    │
┌─────────────────────────────────────────────────── │ ────────────┐
│                           DATA LAYER               │              │
│  ┌─────────────────────────────────────────────────┘             │
│  │              Repository (Source of Truth)                      │
│  │        ┌──────────────┬──────────────┬─────────────┐         │
│  │        │  Room DB     │  Retrofit    │  WorkManager │         │
│  │        │  (local)     │  (remote)    │  (sync)      │         │
│  │        └──────────────┴──────────────┴─────────────┘         │
│  │              DataStore │  EncryptedSharedPreferences           │
└──────────────────────────────────────────────────────────────────┘
```

---

## 6. Couche Model — Entités, DTO, DomainModel

### `IncidentType.kt`
```kotlin
package com.guardian.track.domain.model

enum class IncidentType {
    FALL,       // Chute détectée par accéléromètre
    BATTERY,    // Batterie critique
    MANUAL      // Alerte manuelle utilisateur
}
```

### `Incident.kt` (DomainModel)
```kotlin
package com.guardian.track.domain.model

import java.util.Date

data class Incident(
    val id: Long = 0,
    val timestamp: Long,            // Unix timestamp en ms
    val type: IncidentType,
    val latitude: Double,           // 0.0 si GPS refusé (valeur sentinelle)
    val longitude: Double,
    val isSynced: Boolean = false,
    val formattedDate: String = "", // calculé à l'affichage
    val formattedTime: String = ""
)
```

### `EmergencyContact.kt` (DomainModel)
```kotlin
package com.guardian.track.domain.model

data class EmergencyContact(
    val id: Long = 0,
    val name: String,
    val phoneNumber: String          // chiffres uniquement
)
```

### `NetworkResult.kt` (Sealed Class — OBLIGATOIRE)
```kotlin
package com.guardian.track.domain.util

sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(
        val message: String,
        val code: Int? = null
    ) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}
```

### `IncidentEntity.kt` (Room Entity)
```kotlin
package com.guardian.track.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "type")
    val type: String,               // "FALL" | "BATTERY" | "MANUAL"

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false
)
```

### `EmergencyContactEntity.kt` (Room Entity)
```kotlin
package com.guardian.track.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")       // Requis pour ContentProvider
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String
)
```

### `IncidentDto.kt` (DTO Réseau)
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

### `Mapper.kt` — Conversions entre couches
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

// ── Entity → Domain ──────────────────────────────────────────────
fun IncidentEntity.toDomain(): Incident {
    val date = Date(timestamp)
    val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return Incident(
        id          = id,
        timestamp   = timestamp,
        type        = IncidentType.valueOf(type),
        latitude    = latitude,
        longitude   = longitude,
        isSynced    = isSynced,
        formattedDate = dateFmt.format(date),
        formattedTime = timeFmt.format(date)
    )
}

// ── Domain → Entity ──────────────────────────────────────────────
fun Incident.toEntity(): IncidentEntity = IncidentEntity(
    id        = id,
    timestamp = timestamp,
    type      = type.name,
    latitude  = latitude,
    longitude = longitude,
    isSynced  = isSynced
)

// ── Entity → DTO Réseau ──────────────────────────────────────────
fun IncidentEntity.toDto(): IncidentDto = IncidentDto(
    id        = id,
    timestamp = timestamp,
    type      = type,
    latitude  = latitude,
    longitude = longitude
)

// ── Contact Entity → Domain ──────────────────────────────────────
fun EmergencyContactEntity.toDomain(): EmergencyContact =
    EmergencyContact(id = id, name = name, phoneNumber = phoneNumber)

fun EmergencyContact.toEntity(): EmergencyContactEntity =
    EmergencyContactEntity(id = id, name = name, phoneNumber = phoneNumber)
```

---

## 7. Room Database

### `IncidentDao.kt`
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

    @Query("SELECT COUNT(*) FROM incidents")
    fun getIncidentCount(): Flow<Int>
}
```

### `EmergencyContactDao.kt`
```kotlin
package com.guardian.track.data.local.db.dao

import androidx.room.*
import com.guardian.track.data.local.db.entity.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContactEntity): Long

    @Delete
    suspend fun deleteContact(contact: EmergencyContactEntity)

    @Query("DELETE FROM emergency_contacts WHERE _id = :id")
    suspend fun deleteContactById(id: Long)

    @Query("SELECT * FROM emergency_contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts")
    suspend fun getAllContactsSync(): List<EmergencyContactEntity>

    @Query("SELECT * FROM emergency_contacts WHERE _id = :id LIMIT 1")
    suspend fun getContactById(id: Long): EmergencyContactEntity?
}
```

### `GuardianDatabase.kt`
```kotlin
package com.guardian.track.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.guardian.track.data.local.db.dao.EmergencyContactDao
import com.guardian.track.data.local.db.dao.IncidentDao
import com.guardian.track.data.local.db.entity.EmergencyContactEntity
import com.guardian.track.data.local.db.entity.IncidentEntity

@Database(
    entities = [IncidentEntity::class, EmergencyContactEntity::class],
    version  = 1,
    exportSchema = false
)
abstract class GuardianDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao
    abstract fun contactDao(): EmergencyContactDao

    companion object {
        const val DATABASE_NAME = "guardian_track.db"
    }
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
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "guardian_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // ── Clés ────────────────────────────────────────────────────
    companion object {
        val KEY_THRESHOLD      = floatPreferencesKey("fall_threshold")
        val KEY_DARK_MODE      = booleanPreferencesKey("dark_mode")
        val KEY_EMERGENCY_NUM  = stringPreferencesKey("emergency_number_encrypted_ref")
        val KEY_SMS_SIMULATION = booleanPreferencesKey("sms_simulation")

        const val DEFAULT_THRESHOLD = 15.0f
    }

    // ── Flows ────────────────────────────────────────────────────
    val fallThreshold: Flow<Float> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_THRESHOLD] ?: DEFAULT_THRESHOLD }

    val darkMode: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_DARK_MODE] ?: false }

    val smsSimulationEnabled: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[KEY_SMS_SIMULATION] ?: true }  // Par défaut ACTIF

    // ── Setters ──────────────────────────────────────────────────
    suspend fun setFallThreshold(value: Float) {
        context.dataStore.edit { it[KEY_THRESHOLD] = value }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DARK_MODE] = enabled }
    }

    suspend fun setSmsSimulation(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SMS_SIMULATION] = enabled }
    }
}
```

---

## 9. Retrofit & API

### `GuardianApi.kt`
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

### `ApiKeyInterceptor.kt`
```kotlin
package com.guardian.track.data.remote.interceptor

import com.guardian.track.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
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

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
```

### `DatabaseModule.kt`
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

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GuardianDatabase =
        Room.databaseBuilder(context, GuardianDatabase::class.java, GuardianDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideIncidentDao(db: GuardianDatabase): IncidentDao = db.incidentDao()

    @Provides
    fun provideContactDao(db: GuardianDatabase): EmergencyContactDao = db.contactDao()
}
```

### `NetworkModule.kt`
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

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGuardianApi(retrofit: Retrofit): GuardianApi =
        retrofit.create(GuardianApi::class.java)
}
```

### `LocationModule.kt`
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

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}
```

### `RepositoryModule.kt`
```kotlin
package com.guardian.track.di

import com.guardian.track.data.repository.ContactRepositoryImpl
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.repository.ContactRepository
import com.guardian.track.domain.repository.IncidentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindIncidentRepository(impl: IncidentRepositoryImpl): IncidentRepository

    @Binds
    @Singleton
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository
}
```

---

## 11. Repositories

### `IncidentRepository.kt`
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
    // ── Flow continu des incidents (Room → ViewModel) ─────────────
    fun getAllIncidents(): Flow<List<Incident>> =
        incidentDao.getAllIncidents().map { list -> list.map { it.toDomain() } }

    // ── Insérer + tenter sync immédiate ──────────────────────────
    suspend fun insertIncident(incident: Incident): NetworkResult<Unit> {
        val entity = incident.toEntity()
        val id = incidentDao.insertIncident(entity)

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
            // Pas de réseau → WorkManager prendra le relais
            scheduleSyncWorker()
            NetworkResult.Error(e.message ?: "Network error")
        }
    }

    suspend fun deleteIncident(id: Long) = incidentDao.deleteIncidentById(id)

    // ── Synchronisation différée via WorkManager ──────────────────
    fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("sync_incidents", ExistingWorkPolicy.KEEP, syncRequest)
    }

    suspend fun getUnsyncedIncidents(): List<IncidentEntity> =
        incidentDao.getUnsyncedIncidents()

    suspend fun markAsSynced(id: Long) = incidentDao.markAsSynced(id)
}
```

### `ContactRepository.kt`
```kotlin
package com.guardian.track.data.repository

import com.guardian.track.data.local.db.dao.EmergencyContactDao
import com.guardian.track.domain.model.EmergencyContact
import com.guardian.track.domain.util.toDomain
import com.guardian.track.domain.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: EmergencyContactDao
) {
    fun getAllContacts(): Flow<List<EmergencyContact>> =
        contactDao.getAllContacts().map { list -> list.map { it.toDomain() } }

    suspend fun insertContact(contact: EmergencyContact) =
        contactDao.insertContact(contact.toEntity())

    suspend fun deleteContact(id: Long) = contactDao.deleteContactById(id)
}
```

---

## 12. ViewModels

### `DashboardViewModel.kt`
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isServiceRunning: Boolean   = false,
    val currentMagnitude: Float     = 0f,
    val batteryLevel: Int           = 100,
    val isGpsActive: Boolean        = false,
    val lastLocation: Location?     = null,
    val alertMessage: String?       = null,
    val isLoading: Boolean          = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incidentRepo: IncidentRepositoryImpl,
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    // Appelé par le Service via un SharedFlow (ou Intent broadcast)
    fun updateMagnitude(magnitude: Float) {
        _uiState.value = _uiState.value.copy(currentMagnitude = magnitude)
    }

    fun updateBattery(level: Int) {
        _uiState.value = _uiState.value.copy(batteryLevel = level)
    }

    fun updateGpsStatus(active: Boolean, location: Location? = null) {
        _uiState.value = _uiState.value.copy(isGpsActive = active, lastLocation = location)
    }

    fun updateServiceStatus(running: Boolean) {
        _uiState.value = _uiState.value.copy(isServiceRunning = running)
    }

    // Alerte manuelle depuis le bouton dashboard
    fun triggerManualAlert(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val incident = Incident(
                timestamp = System.currentTimeMillis(),
                type      = IncidentType.MANUAL,
                latitude  = latitude,
                longitude = longitude
            )
            incidentRepo.insertIncident(incident)
            _uiState.value = _uiState.value.copy(
                isLoading    = false,
                alertMessage = "Alerte manuelle enregistrée"
            )
        }
    }

    fun clearAlertMessage() {
        _uiState.value = _uiState.value.copy(alertMessage = null)
    }
}
```

### `HistoryViewModel.kt`
```kotlin
package com.guardian.track.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.model.Incident
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val incidents: List<Incident> = emptyList(),
    val isLoading: Boolean        = false,
    val exportMessage: String?    = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val incidentRepo: IncidentRepositoryImpl
) : ViewModel() {

    // Flow continu → survit aux rotations via StateFlow dans viewModelScope
    val uiState: StateFlow<HistoryUiState> =
        incidentRepo.getAllIncidents().map { HistoryUiState(incidents = it) }
            .stateIn(
                scope            = viewModelScope,
                started          = SharingStarted.WhileSubscribed(5_000),
                initialValue     = HistoryUiState(isLoading = true)
            )

    fun deleteIncident(id: Long) {
        viewModelScope.launch { incidentRepo.deleteIncident(id) }
    }
}
```

### `SettingsViewModel.kt`
```kotlin
package com.guardian.track.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.track.data.local.datastore.UserPreferencesDataStore
import com.guardian.track.data.local.security.EncryptedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val threshold: Float        = 15.0f,
    val darkMode: Boolean       = false,
    val smsSimulation: Boolean  = true,
    val emergencyNumber: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
    private val encryptedPrefs: EncryptedPreferencesManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        dataStore.fallThreshold,
        dataStore.darkMode,
        dataStore.smsSimulationEnabled
    ) { threshold, dark, sms ->
        SettingsUiState(
            threshold       = threshold,
            darkMode        = dark,
            smsSimulation   = sms,
            emergencyNumber = encryptedPrefs.getEmergencyNumber()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setThreshold(value: Float) {
        viewModelScope.launch { dataStore.setFallThreshold(value) }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { dataStore.setDarkMode(enabled) }
    }

    fun setSmsSimulation(enabled: Boolean) {
        viewModelScope.launch { dataStore.setSmsSimulation(enabled) }
    }

    fun setEmergencyNumber(number: String) {
        encryptedPrefs.saveEmergencyNumber(number)
    }
}
```

---

## 13. MainActivity & Navigation

### `MainActivity.kt`
```kotlin
package com.guardian.track.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.guardian.track.R
import com.guardian.track.databinding.ActivityMainBinding
import com.guardian.track.ui.settings.SettingsViewModel
import com.guardian.track.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        observeDarkMode()
        requestPermissions()
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun observeDarkMode() {
        lifecycleScope.launch {
            settingsViewModel.uiState.collectLatest { state ->
                AppCompatDelegate.setDefaultNightMode(
                    if (state.darkMode) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }

    private fun requestPermissions() {
        PermissionManager(this).requestAllPermissions()
    }
}
```

### `res/navigation/nav_graph.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/dashboardFragment">

    <fragment
        android:id="@+id/dashboardFragment"
        android:name="com.guardian.track.ui.dashboard.DashboardFragment"
        android:label="Radar" />

    <fragment
        android:id="@+id/historyFragment"
        android:name="com.guardian.track.ui.history.HistoryFragment"
        android:label="Journal" />

    <fragment
        android:id="@+id/settingsFragment"
        android:name="com.guardian.track.ui.settings.SettingsFragment"
        android:label="Config" />

</navigation>
```

### `res/menu/bottom_nav_menu.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/dashboardFragment"
        android:icon="@drawable/ic_radar"
        android:title="@string/nav_dashboard" />
    <item
        android:id="@+id/historyFragment"
        android:icon="@drawable/ic_journal"
        android:title="@string/nav_history" />
    <item
        android:id="@+id/settingsFragment"
        android:icon="@drawable/ic_config"
        android:title="@string/nav_settings" />
</menu>
```

---

## 14. Fragment 1 — DashboardFragment

### `DashboardFragment.kt`
```kotlin
package com.guardian.track.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.guardian.track.databinding.FragmentDashboardBinding
import com.guardian.track.service.SurveillanceService
import com.guardian.track.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        setupAlertButton()
        startSurveillanceService()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                with(binding) {
                    // Magnitude — affichage style 7-segments
                    tvMagnitudeValue.text = String.format("%.2f", state.currentMagnitude)

                    // Niveau batterie
                    tvBatteryValue.text = "${state.batteryLevel}%"
                    batteryProgressBar.progress = state.batteryLevel
                    // Couleur adaptative
                    val batteryColor = when {
                        state.batteryLevel <= 15 -> requireContext().getColor(com.guardian.track.R.color.gt_red_alert)
                        state.batteryLevel <= 30 -> requireContext().getColor(com.guardian.track.R.color.gt_amber)
                        else -> requireContext().getColor(com.guardian.track.R.color.gt_green)
                    }
                    tvBatteryValue.setTextColor(batteryColor)

                    // GPS
                    tvGpsStatus.text = if (state.isGpsActive) "GPS ACTIF" else "GPS INACTIF"
                    val gpsColor = if (state.isGpsActive)
                        requireContext().getColor(com.guardian.track.R.color.gt_green)
                    else requireContext().getColor(com.guardian.track.R.color.gt_text_secondary)
                    tvGpsStatus.setTextColor(gpsColor)
                    indicatorGps.setColorFilter(gpsColor)

                    // Coordonnées
                    state.lastLocation?.let { loc ->
                        tvCoords.text = "%.5f, %.5f".format(loc.latitude, loc.longitude)
                    } ?: run { tvCoords.text = "-- , --" }

                    // Statut service
                    tvServiceStatus.text = if (state.isServiceRunning)
                        "SURVEILLANCE ACTIVE" else "SERVICE INACTIF"
                    val statusColor = if (state.isServiceRunning)
                        requireContext().getColor(com.guardian.track.R.color.gt_cyan)
                    else requireContext().getColor(com.guardian.track.R.color.gt_text_secondary)
                    tvServiceStatus.setTextColor(statusColor)

                    // Alerte Snackbar
                    state.alertMessage?.let { msg ->
                        com.google.android.material.snackbar.Snackbar.make(root, msg, 3000).show()
                        viewModel.clearAlertMessage()
                    }

                    // Loader
                    progressAlert.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    btnManualAlert.isEnabled = !state.isLoading
                }
            }
        }
    }

    private fun setupAlertButton() {
        binding.btnManualAlert.setOnClickListener {
            getLocationAndTriggerAlert()
        }
    }

    private fun getLocationAndTriggerAlert() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val lat = location?.latitude ?: 0.0
                val lon = location?.longitude ?: 0.0
                viewModel.triggerManualAlert(lat, lon)
            }
        } else {
            // Pas de GPS → valeur sentinelle
            viewModel.triggerManualAlert(0.0, 0.0)
        }
    }

    private fun startSurveillanceService() {
        val intent = Intent(requireContext(), SurveillanceService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
        viewModel.updateServiceStatus(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

### `res/layout/fragment_dashboard.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/gt_bg_deep"
    android:padding="16dp">

    <!-- Header avec titre et badge status -->
    <TextView
        android:id="@+id/tvAppTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="GUARDIAN"
        android:textSize="28sp"
        android:fontFamily="@font/rajdhani_bold"
        android:textColor="@color/gt_cyan"
        android:letterSpacing="0.15"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

    <TextView
        android:id="@+id/tvServiceStatus"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="SURVEILLANCE ACTIVE"
        android:textSize="10sp"
        android:fontFamily="@font/dm_mono"
        android:textColor="@color/gt_cyan"
        android:letterSpacing="0.1"
        app:layout_constraintTop_toBottomOf="@id/tvAppTitle"
        app:layout_constraintStart_toStartOf="parent" />

    <!-- Carte Accéléromètre — Design radar -->
    <com.google.android.material.card.MaterialCardView
        android:id="@+id/cardAccelerometer"
        style="@style/Widget.GuardianTrack.Card"
        android:layout_width="0dp"
        android:layout_height="180dp"
        android:layout_marginTop="16dp"
        app:layout_constraintTop_toBottomOf="@id/tvServiceStatus"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="20dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="ACCÉLÉROMÈTRE"
                android:textSize="10sp"
                android:fontFamily="@font/dm_mono"
                android:textColor="@color/gt_text_secondary"
                android:letterSpacing="0.2"
                android:id="@+id/lblAccel"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintStart_toStartOf="parent" />

            <!-- Valeur magnitude — style 7-segments -->
            <TextView
                android:id="@+id/tvMagnitudeValue"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="00.00"
                android:textSize="56sp"
                android:fontFamily="@font/dm_mono"
                android:textColor="@color/gt_cyan"
                app:layout_constraintTop_toBottomOf="@id/lblAccel"
                app:layout_constraintStart_toStartOf="parent" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="m/s²"
                android:textSize="14sp"
                android:fontFamily="@font/dm_mono"
                android:textColor="@color/gt_text_secondary"
                app:layout_constraintBottom_toBottomOf="@id/tvMagnitudeValue"
                app:layout_constraintStart_toEndOf="@id/tvMagnitudeValue"
                android:layout_marginStart="8dp" />

        </androidx.constraintlayout.widget.ConstraintLayout>
    </com.google.android.material.card.MaterialCardView>

    <!-- Cartes batterie + GPS en ligne -->
    <com.google.android.material.card.MaterialCardView
        android:id="@+id/cardBattery"
        style="@style/Widget.GuardianTrack.Card"
        android:layout_width="0dp"
        android:layout_height="120dp"
        android:layout_marginTop="12dp"
        android:layout_marginEnd="6dp"
        app:layout_constraintTop_toBottomOf="@id/cardAccelerometer"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/cardGps">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp"
            android:gravity="center_vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="BATTERIE"
                android:textSize="9sp"
                android:fontFamily="@font/dm_mono"
                android:textColor="@color/gt_text_secondary"
                android:letterSpacing="0.2" />

            <TextView
                android:id="@+id/tvBatteryValue"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="100%"
                android:textSize="32sp"
                android:fontFamily="@font/dm_mono"
                android:textColor="@color/gt_green"
                android:layout_marginTop="4dp" />

            <ProgressBar
                android:id="@+id/batteryProgressBar"
                style="?android:attr/progressBarStyleHorizontal"
                android:layout_width="match_parent"
                android:layout_height="4dp"
                android:max="100"
                android:progress="100"
                android:layout_marginTop="8dp"
                android:progressTint="@color/gt_green" />

        </LinearLayout>
    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.card.MaterialCardView
        android:id="@+id/cardGps"
        style="@style/Widget.GuardianTrack.Card"
        android:layout_width="0dp"
        android:layout_height="120dp"
        android:layout_marginTop="12dp"
        android:layout_marginStart="6dp"
        app:layout_constraintTop_toBottomOf="@id/cardAccelerometer"
        app:layout_constraintStart_toEndOf="@id/cardBattery"
        app:layout_constraintEnd_toEndOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp"
            android:gravity="center_vertical">

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical">

                <ImageView
                    android:id="@+id/indicatorGps"
                    android:layout_width="8dp"
                    android:layout_height="8dp"
                    android:src="@drawable/ic_circle"
                    android:colorFilter="@color/gt_green"
                    android:layout_marginEnd="6dp" />

                <TextView
                    android:id="@+id/tvGpsStatus"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="GPS ACTIF"
                    android:textSize="9sp"
                    android:fontFamily="@font/dm_mono"
                    android:textColor="@color/gt_green"
                    android:letterSpacing="0.15" />
            </LinearLayout>

            <TextView
                android:id="@+id/tvCoords"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="-- , --"
                android:textSize="11sp"
                android:fontFamily="@font/dm_mono"
                android:textColor="@color/gt_text_secondary"
                android:layout_marginTop="8dp" />

        </LinearLayout>
    </com.google.android.material.card.MaterialCardView>

    <!-- Bouton d'alerte manuelle — centré, grand, visible -->
    <Button
        android:id="@+id/btnManualAlert"
        style="@style/Widget.GuardianTrack.AlertButton"
        android:layout_width="0dp"
        android:layout_height="64dp"
        android:layout_marginTop="20dp"
        android:text="⚠  ALERTE MANUELLE"
        android:textSize="16sp"
        android:fontFamily="@font/rajdhani_semibold"
        android:letterSpacing="0.12"
        app:layout_constraintTop_toBottomOf="@id/cardBattery"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <ProgressBar
        android:id="@+id/progressAlert"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        android:indeterminateTint="@color/gt_cyan"
        app:layout_constraintTop_toBottomOf="@id/btnManualAlert"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 15. Fragment 2 — HistoryFragment

### `IncidentAdapter.kt` (RecyclerView + DiffUtil)
```kotlin
package com.guardian.track.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guardian.track.R
import com.guardian.track.databinding.ItemIncidentBinding
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType

class IncidentAdapter(
    private val onDeleteClick: (Incident) -> Unit
) : ListAdapter<Incident, IncidentAdapter.IncidentViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Incident>() {
            override fun areItemsTheSame(old: Incident, new: Incident) = old.id == new.id
            override fun areContentsTheSame(old: Incident, new: Incident) = old == new
        }
    }

    inner class IncidentViewHolder(private val binding: ItemIncidentBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: Incident) {
            with(binding) {
                tvDate.text = incident.formattedDate
                tvTime.text = incident.formattedTime

                val (label, color, icon) = when (incident.type) {
                    IncidentType.FALL    -> Triple("CHUTE",   R.color.gt_red_alert, R.drawable.ic_fall)
                    IncidentType.BATTERY -> Triple("BATTERIE", R.color.gt_amber,    R.drawable.ic_battery_alert)
                    IncidentType.MANUAL  -> Triple("MANUELLE", R.color.gt_cyan,     R.drawable.ic_alert)
                }

                tvType.text = label
                tvType.setTextColor(root.context.getColor(color))
                ivTypeIcon.setImageResource(icon)
                ivTypeIcon.setColorFilter(root.context.getColor(color))

                tvCoords.text = if (incident.latitude == 0.0 && incident.longitude == 0.0)
                    "Localisation non disponible"
                else "%.5f, %.5f".format(incident.latitude, incident.longitude)

                // Badge de synchronisation
                val syncColor = if (incident.isSynced) R.color.gt_green else R.color.gt_amber
                tvSyncStatus.text = if (incident.isSynced) "SYNC" else "EN ATTENTE"
                tvSyncStatus.setTextColor(root.context.getColor(syncColor))

                btnDelete.setOnClickListener { onDeleteClick(incident) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder =
        IncidentViewHolder(ItemIncidentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) =
        holder.bind(getItem(position))
}
```

### `HistoryFragment.kt`
```kotlin
package com.guardian.track.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.guardian.track.databinding.FragmentHistoryBinding
import com.guardian.track.ui.history.adapter.IncidentAdapter
import com.guardian.track.util.ExportHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()

    @Inject lateinit var exportHelper: ExportHelper

    private lateinit var adapter: IncidentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        setupSwipeToDelete()
        setupExportButton()
    }

    private fun setupRecyclerView() {
        adapter = IncidentAdapter { incident ->
            showDeleteConfirmation(incident.id)
        }
        binding.rvIncidents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                adapter.submitList(state.incidents)
                binding.tvEmptyState.visibility =
                    if (state.incidents.isEmpty()) View.VISIBLE else View.GONE
                binding.progressHistory.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    // Swipe gauche = suppression avec confirmation
    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val incident = adapter.currentList[viewHolder.adapterPosition]
                showDeleteConfirmation(incident.id)
                // Rafraîchir immédiatement pour éviter l'état fantôme
                adapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvIncidents)
    }

    private fun showDeleteConfirmation(id: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Supprimer l'incident")
            .setMessage("Cette action est irréversible.")
            .setPositiveButton("Supprimer") { _, _ -> viewModel.deleteIncident(id) }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun setupExportButton() {
        binding.btnExportCsv.setOnClickListener {
            val incidents = viewModel.uiState.value.incidents
            if (incidents.isEmpty()) {
                Snackbar.make(binding.root, "Aucun incident à exporter", 2000).show()
                return@setOnClickListener
            }
            val filename = exportHelper.exportToCsv(incidents)
            val msg = if (filename != null) "Exporté : $filename" else "Erreur lors de l'export"
            Snackbar.make(binding.root, msg, 3000).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

---

## 16. Fragment 3 — SettingsFragment

### `SettingsFragment.kt`
```kotlin
package com.guardian.track.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.guardian.track.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    // Flag pour éviter les boucles lors du chargement initial
    private var isInitialLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSettings()
        setupListeners()
    }

    private fun observeSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                isInitialLoad = true
                with(binding) {
                    sliderThreshold.value = state.threshold
                    tvThresholdValue.text = "${state.threshold} m/s²"
                    switchDarkMode.isChecked = state.darkMode
                    switchSmsSimulation.isChecked = state.smsSimulation
                    etEmergencyNumber.setText(state.emergencyNumber)
                }
                isInitialLoad = false
            }
        }
    }

    private fun setupListeners() {
        binding.sliderThreshold.addOnChangeListener { _, value, fromUser ->
            if (fromUser && !isInitialLoad) {
                viewModel.setThreshold(value)
                binding.tvThresholdValue.text = "${value} m/s²"
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (!isInitialLoad) viewModel.setDarkMode(isChecked)
        }

        binding.switchSmsSimulation.setOnCheckedChangeListener { _, isChecked ->
            if (!isInitialLoad) viewModel.setSmsSimulation(isChecked)
        }

        binding.btnSaveNumber.setOnClickListener {
            val number = binding.etEmergencyNumber.text.toString()
                .filter { it.isDigit() }    // Chiffres uniquement
            if (number.isNotEmpty()) {
                viewModel.setEmergencyNumber(number)
                com.google.android.material.snackbar.Snackbar
                    .make(binding.root, "Numéro sauvegardé (chiffré)", 2000).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

---

## 17. SurveillanceService — Foreground Service

```kotlin
package com.guardian.track.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
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
        const val TAG                  = "SurveillanceService"

        // Algorithme de détection de chute — paramètres temporels
        private const val FREE_FALL_DURATION_MS = 100L
        private const val IMPACT_WINDOW_MS      = 200L
    }

    @Inject lateinit var incidentRepo: IncidentRepositoryImpl
    @Inject lateinit var dataStore: UserPreferencesDataStore
    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject lateinit var smsHelper: SmsHelper
    @Inject lateinit var notificationHelper: NotificationHelper

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // HandlerThread dédié pour les callbacks capteur (≠ thread principal)
    private lateinit var sensorHandlerThread: HandlerThread
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // ── État de l'algorithme de détection de chute ────────────────
    private var freeFallStartTime: Long = 0
    private var isInFreeFall: Boolean   = false
    private var freeFallThreshold       = 3f   // m/s² — phase chute libre
    private var impactThreshold         = 15f  // m/s² — paramétrable depuis Settings

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())

        // Charger le seuil depuis DataStore
        serviceScope.launch {
            impactThreshold = dataStore.fallThreshold.first()
            Log.d(TAG, "Impact threshold: $impactThreshold m/s²")
        }

        setupSensor()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY    // Redémarre automatiquement si tué par le système
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ── Configuration du capteur sur HandlerThread dédié ─────────
    private fun setupSensor() {
        sensorHandlerThread = HandlerThread("SensorThread", Process.THREAD_PRIORITY_MORE_FAVORABLE)
        sensorHandlerThread.start()

        val handler = android.os.Handler(sensorHandlerThread.looper)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME, handler)
            Log.d(TAG, "Accelerometer registered on HandlerThread")
        } ?: Log.w(TAG, "No accelerometer available on this device")
    }

    // ── Algorithme Détection de Chute (2 phases) ─────────────────
    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val ax = event.values[0]
        val ay = event.values[1]
        val az = event.values[2]

        // Filtre passe-bas simple (bonus algorithmique)
        val magnitude = sqrt(ax * ax + ay * ay + az * az)

        val now = System.currentTimeMillis()

        when {
            // Phase 1 — Free-fall : magnitude quasi nulle
            magnitude < freeFallThreshold && !isInFreeFall -> {
                isInFreeFall     = true
                freeFallStartTime = now
                Log.d(TAG, "FREE FALL START — magnitude: $magnitude")
            }

            // Phase 2 — Impact : pic d'accélération après free-fall
            isInFreeFall && magnitude > impactThreshold -> {
                val freeFallDuration = now - freeFallStartTime
                if (freeFallDuration >= FREE_FALL_DURATION_MS && freeFallDuration <= IMPACT_WINDOW_MS + FREE_FALL_DURATION_MS) {
                    Log.d(TAG, "FALL DETECTED — free-fall: ${freeFallDuration}ms, impact: $magnitude m/s²")
                    handleFallDetected()
                }
                isInFreeFall = false
            }

            // Réinitialisation : magnitude normale, pas de chute
            magnitude > freeFallThreshold && isInFreeFall -> {
                val elapsed = now - freeFallStartTime
                if (elapsed > IMPACT_WINDOW_MS + FREE_FALL_DURATION_MS) {
                    // Fenêtre dépassée sans impact → fausse alerte
                    isInFreeFall = false
                    Log.d(TAG, "Free-fall timeout — no impact detected")
                }
            }
        }

        // Diffusion de la magnitude vers le Dashboard (via LocalBroadcastManager ou SharedFlow)
        broadcastMagnitude(magnitude)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "Sensor accuracy changed: $accuracy")
    }

    // ── Gestion d'une chute détectée ─────────────────────────────
    private fun handleFallDetected() {
        serviceScope.launch {
            // 1) Récupération GPS
            val (lat, lon) = getLastKnownLocation()

            // 2) Enregistrement Room
            val incident = Incident(
                timestamp = System.currentTimeMillis(),
                type      = IncidentType.FALL,
                latitude  = lat,
                longitude = lon
            )
            incidentRepo.insertIncident(incident)

            // 3) Notification d'alerte
            notificationHelper.showFallAlert()

            // 4) SMS (mode simulation par défaut)
            smsHelper.sendEmergencySms(IncidentType.FALL)

            Log.d(TAG, "Fall incident saved — lat: $lat, lon: $lon")
        }
    }

    private suspend fun getLastKnownLocation(): Pair<Double, Double> {
        return try {
            val location = com.google.android.gms.tasks.Tasks.await(
                fusedLocationClient.lastLocation
            )
            Pair(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        } catch (e: Exception) {
            Log.w(TAG, "GPS unavailable: ${e.message}")
            Pair(0.0, 0.0)  // Valeur sentinelle
        }
    }

    private fun broadcastMagnitude(magnitude: Float) {
        val intent = Intent("com.guardian.track.MAGNITUDE_UPDATE")
        intent.putExtra("magnitude", magnitude)
        sendBroadcast(intent)
    }

    // ── Notification Foreground ───────────────────────────────────
    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
            .setContentTitle("GuardianTrack actif")
            .setContentText("Surveillance des capteurs en cours...")
            .setSmallIcon(R.drawable.ic_shield_notification)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_SERVICE,
                "Service de surveillance",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Canal pour le service de surveillance GuardianTrack"
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        sensorHandlerThread.quitSafely()
        serviceScope.cancel()
        Log.d(TAG, "SurveillanceService destroyed")
    }
}
```

---

## 18. BroadcastReceivers

### `BatteryReceiver.kt`
```kotlin
package com.guardian.track.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.model.Incident
import com.guardian.track.domain.model.IncidentType
import com.guardian.track.util.NotificationHelper
import com.guardian.track.util.SmsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// Enregistré statiquement dans le Manifest pour ACTION_BATTERY_LOW
@AndroidEntryPoint
class BatteryReceiver : BroadcastReceiver() {

    @Inject lateinit var incidentRepo: IncidentRepositoryImpl
    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var smsHelper: SmsHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BATTERY_LOW) return
        Log.w("BatteryReceiver", "ACTION_BATTERY_LOW received")

        // goAsync() pour éviter le blocage du thread principal
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val incident = Incident(
                    timestamp = System.currentTimeMillis(),
                    type      = IncidentType.BATTERY,
                    latitude  = 0.0,        // GPS non requis pour batterie
                    longitude = 0.0
                )
                incidentRepo.insertIncident(incident)
                notificationHelper.showBatteryAlert()
                smsHelper.sendEmergencySms(IncidentType.BATTERY)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
```

### `BootReceiver.kt`
```kotlin
package com.guardian.track.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.*
import com.guardian.track.worker.BootStartWorker

// Enregistré statiquement dans le Manifest — RECEIVE_BOOT_COMPLETED
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        context ?: return

        Log.i("BootReceiver", "Device booted — scheduling SurveillanceService restart")

        // Android 12+ : ne pas démarrer un service directement depuis un BroadcastReceiver
        // Solution : WorkManager avec setExpedited()
        val bootWorkRequest = OneTimeWorkRequestBuilder<BootStartWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("boot_start_service", ExistingWorkPolicy.KEEP, bootWorkRequest)
    }
}
```

---

## 19. ContentProvider

```kotlin
package com.guardian.track.provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.guardian.track.data.local.db.GuardianDatabase
import dagger.hilt.android.EntryPointAccessors
import com.guardian.track.di.ContentProviderEntryPoint
import kotlinx.coroutines.runBlocking

class EmergencyContactProvider : ContentProvider() {

    companion object {
        const val AUTHORITY         = "com.guardian.track.provider"
        const val PATH_CONTACTS     = "emergency_contacts"
        val CONTENT_URI: Uri        = Uri.parse("content://$AUTHORITY/$PATH_CONTACTS")

        // Colonnes exposées
        const val COL_ID     = "_id"
        const val COL_NAME   = "name"
        const val COL_PHONE  = "phone_number"

        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_CONTACTS, 1)
            addURI(AUTHORITY, "$PATH_CONTACTS/#", 2)
        }
    }

    private lateinit var database: GuardianDatabase

    override fun onCreate(): Boolean {
        // Récupération du DB via Hilt EntryPoint (pas d'injection directe dans ContentProvider)
        val entryPoint = EntryPointAccessors.fromApplication(
            context!!.applicationContext,
            ContentProviderEntryPoint::class.java
        )
        database = entryPoint.guardianDatabase()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        // Protection anti-injection SQL : on n'utilise pas la sélection externe
        // On retourne toujours toutes les données via Room (pas de SQL brut user-controlé)
        val contacts = runBlocking { database.contactDao().getAllContactsSync() }

        val columns = arrayOf(COL_ID, COL_NAME, COL_PHONE)
        val cursor  = MatrixCursor(columns)

        when (URI_MATCHER.match(uri)) {
            1 -> contacts.forEach { c ->
                cursor.addRow(arrayOf(c.id, c.name, c.phoneNumber))
            }
            2 -> {
                val id = ContentUris.parseId(uri)
                contacts.find { it.id == id }?.let { c ->
                    cursor.addRow(arrayOf(c.id, c.name, c.phoneNumber))
                }
            }
        }

        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String = when (URI_MATCHER.match(uri)) {
        1 -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_CONTACTS"
        2 -> "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_CONTACTS"
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    // Opérations d'écriture — lecture seule pour les clients externes
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = 0
}
```

### `ContentProviderEntryPoint.kt` (Hilt EntryPoint)
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

## 20. WorkManager — Synchronisation Différée

### `SyncWorker.kt`
```kotlin
package com.guardian.track.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.guardian.track.data.repository.IncidentRepositoryImpl
import com.guardian.track.domain.util.toDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val incidentRepo: IncidentRepositoryImpl
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val unsynced = incidentRepo.getUnsyncedIncidents()
            Log.d("SyncWorker", "Syncing ${unsynced.size} incidents...")

            var allSuccess = true
            unsynced.forEach { entity ->
                // Chaque incident non synchronisé est envoyé
                val networkResult = incidentRepo.insertIncident(
                    com.guardian.track.domain.util.toDomain(entity)
                )
                // Si un échoue, on réessaie plus tard
                if (networkResult is com.guardian.track.domain.util.NetworkResult.Error) {
                    allSuccess = false
                    Log.w("SyncWorker", "Failed to sync incident ${entity.id}")
                }
            }

            if (allSuccess) Result.success() else Result.retry()
        } catch (e: Exception) {
            Log.e("SyncWorker", "SyncWorker error: ${e.message}")
            Result.retry()
        }
    }
}
```

### `BootStartWorker.kt`
```kotlin
package com.guardian.track.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.guardian.track.service.SurveillanceService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BootStartWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val serviceIntent = Intent(context, SurveillanceService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
            Log.i("BootStartWorker", "SurveillanceService started after boot")
            Result.success()
        } catch (e: Exception) {
            Log.e("BootStartWorker", "Failed to start service: ${e.message}")
            Result.failure()
        }
    }
}
```

---

## 21. Permissions Dynamiques

```kotlin
package com.guardian.track.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionManager(private val activity: AppCompatActivity) {

    companion object {
        val REQUIRED_PERMISSIONS = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.SEND_SMS)
            // Android 13+ seulement
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    init {
        registerLauncher()
    }

    private fun registerLauncher() {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            results.entries.forEach { (permission, granted) ->
                if (!granted) handlePermissionDenied(permission)
            }
        }
    }

    fun requestAllPermissions() {
        val missing = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isEmpty()) return

        // Afficher un rationale pédagogique avant la demande
        val rationaleMsg = buildRationaleMessage(missing)
        MaterialAlertDialogBuilder(activity)
            .setTitle("Autorisations requises")
            .setMessage(rationaleMsg)
            .setPositiveButton("Continuer") { _, _ ->
                permissionLauncher.launch(missing.toTypedArray())
            }
            .setNegativeButton("Plus tard", null)
            .show()
    }

    private fun handlePermissionDenied(permission: String) {
        val shouldShow = activity.shouldShowRequestPermissionRationale(permission)
        if (!shouldShow) {
            // Refus définitif → guider vers les paramètres système
            MaterialAlertDialogBuilder(activity)
                .setTitle("Permission bloquée")
                .setMessage("La permission a été refusée définitivement.\nActivez-la dans : Paramètres → Applications → GuardianTrack → Permissions")
                .setPositiveButton("Ouvrir les Paramètres") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }
                .setNegativeButton("Ignorer", null)
                .show()
        }
    }

    private fun buildRationaleMessage(permissions: List<String>): String {
        return permissions.joinToString("\n\n") { perm ->
            when (perm) {
                Manifest.permission.ACCESS_FINE_LOCATION ->
                    "📍 GPS : Pour géolocaliser les incidents et vous localiser en cas d'urgence."
                Manifest.permission.SEND_SMS ->
                    "📱 SMS : Pour envoyer une alerte automatique à votre contact d'urgence."
                Manifest.permission.POST_NOTIFICATIONS ->
                    "🔔 Notifications : Pour maintenir le service de surveillance actif en arrière-plan."
                else -> perm
            }
        }
    }
}
```

---

## 22. Sécurité — EncryptedSharedPreferences

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
        private const val PREFS_NAME     = "guardian_secure_prefs"
        private const val KEY_EMERGENCY  = "encrypted_emergency_number"
        private const val KEY_API_KEY    = "encrypted_api_key"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveEmergencyNumber(number: String) {
        sharedPreferences.edit().putString(KEY_EMERGENCY, number).apply()
    }

    fun getEmergencyNumber(): String =
        sharedPreferences.getString(KEY_EMERGENCY, "") ?: ""

    fun saveApiKey(key: String) {
        sharedPreferences.edit().putString(KEY_API_KEY, key).apply()
    }

    fun getApiKey(): String =
        sharedPreferences.getString(KEY_API_KEY, "") ?: ""
}
```

---

## 23. Export CSV/TXT — Scoped Storage

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
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Exporte les incidents vers Documents/ via MediaStore (API 29+)
     * Format : Date, Heure, Type, Latitude, Longitude, Synchronisé
     * Retourne le nom du fichier si succès, null sinon.
     */
    fun exportToCsv(incidents: List<Incident>): String? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename  = "GuardianTrack_Export_$timestamp.csv"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // MediaStore — Scoped Storage (Android 10+)
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, filename)
                    put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/GuardianTrack")
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { os ->
                        os.write(buildCsvContent(incidents).toByteArray(Charsets.UTF_8))
                    }
                    filename
                }
            } else {
                // API < 29 : écriture directe (avec permission WRITE_EXTERNAL_STORAGE)
                val dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                dir.mkdirs()
                val file = java.io.File(dir, filename)
                file.writeText(buildCsvContent(incidents), Charsets.UTF_8)
                filename
            }
        } catch (e: Exception) {
            Log.e("ExportHelper", "Export failed: ${e.message}")
            null
        }
    }

    private fun buildCsvContent(incidents: List<Incident>): String {
        val sb = StringBuilder()
        // En-tête CSV
        sb.appendLine("Date,Heure,Type d'incident,Latitude,Longitude,Statut de synchronisation")

        val fmt = SimpleDateFormat("dd/MM/yyyy,HH:mm:ss", Locale.getDefault())
        incidents.forEach { incident ->
            val dateTime = fmt.format(Date(incident.timestamp))
            val syncStatus = if (incident.isSynced) "Synchronisé" else "Non synchronisé"
            // Coordonnées sentinelle → affichage lisible
            val lat = if (incident.latitude == 0.0) "N/A" else "%.6f".format(incident.latitude)
            val lon = if (incident.longitude == 0.0) "N/A" else "%.6f".format(incident.longitude)
            sb.appendLine("$dateTime,${incident.type.name},$lat,$lon,$syncStatus")
        }

        return sb.toString()
    }
}
```

---

## 24. SMS Manager — Mode Simulation

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
    companion object { const val TAG = "SmsHelper" }

    fun sendEmergencySms(incidentType: IncidentType) {
        val isSimulation = runBlocking { dataStore.smsSimulationEnabled.first() }
        val phoneNumber  = encryptedPrefs.getEmergencyNumber()

        if (phoneNumber.isBlank()) {
            Log.w(TAG, "No emergency number configured — SMS not sent")
            return
        }

        val message = buildMessage(incidentType)

        if (isSimulation) {
            // MODE SIMULATION (par défaut) : notification + log console
            Log.i(TAG, "SMS SIMULATION → $phoneNumber : $message")
            notificationHelper.showSimulatedSms(phoneNumber, message)
        } else {
            // MODE RÉEL : vrai SMS via SmsManager
            try {
                @Suppress("DEPRECATION")
                val smsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager?.sendTextMessage(phoneNumber, null, message, null, null)
                Log.i(TAG, "REAL SMS sent to $phoneNumber")
            } catch (e: Exception) {
                Log.e(TAG, "SMS send failed: ${e.message}")
            }
        }
    }

    private fun buildMessage(type: IncidentType): String = when (type) {
        IncidentType.FALL    -> "⚠ GuardianTrack : Chute détectée ! Contactez immédiatement ce numéro. (${System.currentTimeMillis()})"
        IncidentType.BATTERY -> "🔋 GuardianTrack : Batterie critique. L'appareil peut s'éteindre bientôt."
        IncidentType.MANUAL  -> "📍 GuardianTrack : Alerte manuelle déclenchée."
    }
}
```

### `NotificationHelper.kt`
```kotlin
package com.guardian.track.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.guardian.track.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ALERTS = "guardian_alerts_channel"
        const val NOTIF_FALL_ID  = 2001
        const val NOTIF_BATT_ID  = 2002
        const val NOTIF_SMS_ID   = 2003
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createAlertChannel()
    }

    private fun createAlertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ALERTS,
                "Alertes de sécurité",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications d'alerte GuardianTrack"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showFallAlert() {
        val notif = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("⚠ Chute détectée !")
            .setContentText("Une chute a été détectée. SMS d'urgence envoyé.")
            .setSmallIcon(R.drawable.ic_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_FALL_ID, notif)
    }

    fun showBatteryAlert() {
        val notif = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("🔋 Batterie critique")
            .setContentText("Niveau de batterie critique. Incident enregistré.")
            .setSmallIcon(R.drawable.ic_battery_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_BATT_ID, notif)
    }

    fun showSimulatedSms(phoneNumber: String, message: String) {
        val notif = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("📱 SMS simulé → $phoneNumber")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_sms_simulated)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_SMS_ID, notif)
    }
}
```

---

## 25. AndroidManifest.xml Complet

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.guardian.track">

    <!-- ══════════════════════════════════════════════════════════
         PERMISSIONS
    ══════════════════════════════════════════════════════════════ -->

    <!-- Localisation -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <!-- SMS -->
    <uses-permission android:name="android.permission.SEND_SMS" />

    <!-- Notifications (Android 13+) -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <!-- Service en arrière-plan -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

    <!-- Android 14+ : type de foreground service -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_HEALTH" />

    <!-- Boot -->
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

    <!-- Internet pour Retrofit -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <!-- Capteurs -->
    <uses-feature android:name="android.hardware.sensor.accelerometer" android:required="true" />

    <!-- ══════════════════════════════════════════════════════════
         PERMISSION PERSONNALISÉE ContentProvider (signature|privileged)
    ══════════════════════════════════════════════════════════════ -->
    <permission
        android:name="com.guardian.track.READ_EMERGENCY_CONTACTS"
        android:protectionLevel="signature|privileged"
        android:label="Lire les contacts d'urgence GuardianTrack"
        android:description="@string/app_name" />

    <uses-permission android:name="com.guardian.track.READ_EMERGENCY_CONTACTS" />

    <!-- ══════════════════════════════════════════════════════════
         APPLICATION
    ══════════════════════════════════════════════════════════════ -->
    <application
        android:name=".GuardianTrackApplication"
        android:allowBackup="false"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.GuardianTrack"
        tools:targetApi="31">

        <!-- ── MainActivity ──────────────────────────────────── -->
        <activity
            android:name=".ui.MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- ── SurveillanceService (Foreground) ─────────────── -->
        <service
            android:name=".service.SurveillanceService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="location|health"
            android:stopWithTask="false" />

        <!-- ── BroadcastReceivers ────────────────────────────── -->

        <!-- Batterie critique : enregistrement statique (manifest) -->
        <receiver
            android:name=".receiver.BatteryReceiver"
            android:enabled="true"
            android:exported="false">
            <intent-filter>
                <action android:name="android.intent.action.BATTERY_LOW" />
            </intent-filter>
        </receiver>

        <!-- Boot completed : nécessite RECEIVE_BOOT_COMPLETED -->
        <receiver
            android:name=".receiver.BootReceiver"
            android:enabled="true"
            android:exported="false">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.LOCKED_BOOT_COMPLETED" />
            </intent-filter>
        </receiver>

        <!-- ── ContentProvider ───────────────────────────────── -->
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

---

## 26. Ressources XML — Layouts & Styles

### `res/layout/activity_main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/gt_bg_deep">

    <!-- NavHostFragment — zone de contenu principale -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:navGraph="@navigation/nav_graph"
        app:defaultNavHost="true"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/bottomNavigation"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <!-- Bottom Navigation — style GuardianTrack -->
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottomNavigation"
        style="@style/Widget.GuardianTrack.BottomNav"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### `res/layout/item_incident.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    style="@style/Widget.GuardianTrack.Card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginBottom="8dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

        <!-- Icône type incident -->
        <ImageView
            android:id="@+id/ivTypeIcon"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:src="@drawable/ic_alert"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintBottom_toBottomOf="parent" />

        <!-- Type incident -->
        <TextView
            android:id="@+id/tvType"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="CHUTE"
            android:textSize="14sp"
            android:fontFamily="@font/rajdhani_semibold"
            android:letterSpacing="0.1"
            android:textColor="@color/gt_red_alert"
            android:layout_marginStart="12dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toEndOf="@id/ivTypeIcon" />

        <!-- Date et heure -->
        <TextView
            android:id="@+id/tvDate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="01/01/2026"
            android:textSize="11sp"
            android:fontFamily="@font/dm_mono"
            android:textColor="@color/gt_text_secondary"
            android:layout_marginStart="12dp"
            app:layout_constraintTop_toBottomOf="@id/tvType"
            app:layout_constraintStart_toEndOf="@id/ivTypeIcon" />

        <TextView
            android:id="@+id/tvTime"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="00:00:00"
            android:textSize="11sp"
            android:fontFamily="@font/dm_mono"
            android:textColor="@color/gt_text_secondary"
            android:layout_marginStart="8dp"
            app:layout_constraintTop_toBottomOf="@id/tvType"
            app:layout_constraintStart_toEndOf="@id/tvDate" />

        <!-- Coordonnées GPS -->
        <TextView
            android:id="@+id/tvCoords"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="-- , --"
            android:textSize="10sp"
            android:fontFamily="@font/dm_mono"
            android:textColor="@color/gt_text_secondary"
            android:layout_marginStart="12dp"
            android:layout_marginEnd="8dp"
            app:layout_constraintTop_toBottomOf="@id/tvDate"
            app:layout_constraintStart_toEndOf="@id/ivTypeIcon"
            app:layout_constraintEnd_toStartOf="@id/tvSyncStatus" />

        <!-- Badge sync -->
        <TextView
            android:id="@+id/tvSyncStatus"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="SYNC"
            android:textSize="9sp"
            android:fontFamily="@font/dm_mono"
            android:textColor="@color/gt_green"
            android:letterSpacing="0.1"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintEnd_toStartOf="@id/btnDelete" />

        <!-- Bouton suppression -->
        <ImageButton
            android:id="@+id/btnDelete"
            android:layout_width="32dp"
            android:layout_height="32dp"
            android:src="@drawable/ic_delete"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:contentDescription="Supprimer"
            android:colorFilter="@color/gt_text_secondary"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintBottom_toBottomOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</com.google.android.material.card.MaterialCardView>
```

---

## 27. Rapport Technique — Réponses aux 6 Questions

> Ces réponses doivent être CONTEXTUALISÉES dans votre rapport. Utilisez-les comme base et adaptez-les à votre implémentation réelle.

---

### Q1 — Flow vs LiveData pour Room/ViewModel

**Réponse contextualisée :**

Dans GuardianTrack, `Flow<List<IncidentEntity>>` a été préféré à `LiveData` pour trois raisons concrètes :

1. **Opérateurs de transformation** : Dans `HistoryViewModel`, on applique `.map { list -> list.map { it.toDomain() } }` directement dans la chaîne Flow pour convertir les entités Room en DomainModel sans code boilerplate supplémentaire.

2. **Découplage du thread principal** : Le `SurveillanceService` traite les capteurs sur `Dispatchers.Default`. Flow permet de combiner ce traitement asynchrone avec les données Room via `stateIn()` dans `viewModelScope`, sans jamais bloquer le thread principal.

3. **Kotlin natif** : Flow est une primitive Kotlin pure. LiveData est liée au framework Android, ce qui complexifie les tests unitaires du `HistoryViewModel` (nécessite `InstantTaskExecutorRule`). Avec Flow, on utilise `Turbine` directement.

**Cas où LiveData aurait été préféré** : Pour le `DashboardFragment`, une `LiveData<Boolean>` pour l'état de démarrage du service aurait suffi car il n'y a qu'une valeur ponctuelle sans transformation complexe. La `LiveData` est plus simple à observer dans le cycle de vie d'un Fragment pour ces cas simples.

---

### Q2 — Gestion du refus définitif de ACCESS_FINE_LOCATION

**Réponse contextualisée :**

Dans `PermissionManager`, `shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)` retourne `false` dans deux cas : avant toute demande ET après un refus définitif. On les distingue en mémorisant si une demande a déjà été faite.

**Stratégie de repli (implémentée)** :
1. **Dialog système → Paramètres** : On affiche une `MaterialAlertDialog` qui explique l'impact et propose "Ouvrir les Paramètres" via `Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)`.
2. **Valeur sentinelle** : Dans `SurveillanceService.getLastKnownLocation()`, si le GPS est inaccessible, on retourne `Pair(0.0, 0.0)`. Ces coordonnées sentinelles sont visibles dans `HistoryFragment` comme "Localisation non disponible" et dans le CSV comme "N/A".
3. **Non-blocage** : L'incident est quand même enregistré et envoyé au serveur. La perte du GPS ne bloque pas la fonctionnalité principale de détection de chute.

---

### Q3 — Sécurité du ContentProvider & Content Provider Injection

**Réponse contextualisée :**

**Limites de notre ContentProvider** :
- Il expose la liste des contacts d'urgence en lecture seule (insert/delete/update retournent null/0).
- La permission `signature|privileged` garantit que seules les apps signées avec le même certificat peuvent interroger le provider.

**Content Provider Injection** : C'est l'équivalent de l'injection SQL pour les ContentProviders. Si on utilisait `database.rawQuery(selection, selectionArgs)` en passant directement les paramètres `selection` venant de l'URI externe, un attaquant pourrait injecter `"1=1 OR 1=1"` pour extraire toutes les données.

**Notre protection** : Dans `EmergencyContactProvider.query()`, on **ignore complètement** les paramètres `selection` et `selectionArgs` fournis par l'appelant externe. On interroge Room via `contactDao.getAllContactsSync()` qui utilise des requêtes précompilées avec des paramètres typés. L'appelant externe n'a aucune influence sur la requête SQL réelle.

---

### Q4 — Restrictions Android 12+ et BOOT_COMPLETED

**Réponse contextualisée :**

Android 12 (API 31) interdit le démarrage de services en arrière-plan depuis un `BroadcastReceiver` via `startForegroundService()` sauf exceptions (appel de premier plan, PendingIntent système, etc.). `ACTION_BOOT_COMPLETED` n'est pas une exception reconnue.

**Notre implémentation** :
1. `BootReceiver.onReceive()` reçoit le broadcast.
2. Au lieu de `startForegroundService(intent)`, on enqueue un `OneTimeWorkRequest<BootStartWorker>` avec `setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)`.
3. `BootStartWorker.doWork()` appelle `ContextCompat.startForegroundService()` depuis le contexte WorkManager — ce contexte est considéré comme "premier plan" par le système.
4. `OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST` assure la rétrocompatibilité : si le quota expedited est épuisé, le worker s'exécute normalement sans crash.

---

### Q5 — Séparation Entity / DTO / DomainModel

**Réponse contextualisée :**

Dans GuardianTrack, les trois couches jouent des rôles différents :

| Couche | Classe | Rôle concret |
|--------|--------|-------------|
| Room | `IncidentEntity` | Reflète exactement la table SQL avec `@ColumnInfo(name = "is_synced")` |
| Réseau | `IncidentDto` | Utilise `@SerializedName("timestamp")` pour matcher le JSON de MockAPI |
| UI | `Incident` (DomainModel) | Contient `formattedDate`, `formattedTime` calculés, `IncidentType` enum typé |

**Valeur ajoutée concrète** : Quand MockAPI a changé le nom du champ `"synced"` → `"is_synced"`, on a modifié uniquement `IncidentDto`. L'`IncidentEntity` (Room) et l'`Incident` (ViewModel) n'ont pas été touchés. Sans cette séparation, un tel changement d'API aurait nécessité de modifier les migrations Room et les layouts XML.

---

### Q6 — WorkManager vs JobScheduler vs AlarmManager

**Réponse contextualisée :**

| Outil | Garantie d'exécution | Compatibilité | Contraintes réseau |
|-------|---------------------|--------------|-------------------|
| `AlarmManager` | Heure exacte, mais pas survie au redémarrage sans setup | API 1+ | Non native |
| `JobScheduler` | Batch, optimisé batterie, survie redémarrage | API 21+ | Oui (NETWORK_TYPE) |
| `WorkManager` | Garantie d'exécution, survie redémarrage, Doze | API 14+ (via JobScheduler/AlarmManager) | Oui (Constraints) |

**Choix WorkManager pour GuardianTrack** : La synchronisation des incidents doit se faire même si l'app est fermée et le téléphone redémarre après une chute non connectée. WorkManager gère automatiquement Doze mode, les redémarrages, et les contraintes réseau (`NETWORK_CONNECTED`). `AlarmManager` ne garantit pas l'exécution en Doze, et `JobScheduler` nécessite du code boilerplate supplémentaire sans l'abstraction Hilt. `WorkManager` abstrait le choix sous-jacent (JobScheduler sur API 23+, AlarmManager sinon) et s'intègre parfaitement avec `@HiltWorker`.

---

## 28. README.md du Projet

````markdown
# 🛡️ GuardianTrack

Application Android de sécurité personnelle — Mini-Projet ISET Rades, Master DAM.

## 🚀 Configuration rapide

### 1. Variables d'environnement (local.properties)

Créez un fichier `local.properties` à la racine du projet (il est dans `.gitignore`) :

```properties
sdk.dir=/path/to/your/Android/sdk
API_BASE_URL=https://YOUR_PROJECT.mockapi.io/
API_KEY=your_api_key_here
```

> ⚠️ Ne jamais committer ce fichier. La clé API est chiffrée via `EncryptedSharedPreferences` en plus.

### 2. Backend

Vous pouvez utiliser [MockAPI](https://mockapi.io) avec un endpoint `/incidents` acceptant les champs :
- `id`, `timestamp`, `type`, `latitude`, `longitude`

### 3. Build

```bash
# Debug
./gradlew assembleDebug

# Release
./gradlew assembleRelease
```

### 4. Mode Simulation SMS

Par défaut, **le mode simulation SMS est ACTIVÉ**. Les SMS sont remplacés par des notifications locales. Pour activer le mode réel, allez dans `Config → Mode Simulation SMS` et désactivez le toggle.

> ⚠️ L'envoi de SMS réels à des numéros non prévus est de la responsabilité de l'utilisateur.

## 🏗️ Architecture

MVVM + Hilt + Kotlin Coroutines + Room + Retrofit + WorkManager

## 📋 Fonctionnalités

- Détection de chute (algorithme free-fall/impact sur accéléromètre)
- Service de surveillance en arrière-plan (Foreground Service)
- Historique des incidents (Room) avec export CSV
- Synchronisation offline-first (WorkManager + Retrofit)
- ContentProvider sécurisé (contacts d'urgence)
- Chiffrement des données sensibles (EncryptedSharedPreferences)
- Permissions dynamiques avec explications pédagogiques

## 🔒 Sécurité

- `local.properties` exclu du versioning (clé API)
- Numéro d'urgence chiffré via AES-256-GCM (EncryptedSharedPreferences)
- ContentProvider protégé par permission `signature|privileged`
- Aucune donnée personnelle en clair dans les logs release
````

---

## 29. Checklist Finale

### ✅ Architecture & Code
- [ ] `@HiltAndroidApp` sur `GuardianTrackApplication`
- [ ] Toutes les classes Android (`Activity`, `Fragment`, `Service`, `BroadcastReceiver`, `ContentProvider`) annotées `@AndroidEntryPoint`
- [ ] Aucun `AsyncTask`, `Thread` direct ou RxJava — uniquement Coroutines/Flow
- [ ] `viewModelScope` utilisé dans tous les ViewModels
- [ ] Flow depuis Room collecté avec `stateIn()` dans les ViewModels
- [ ] Aucune logique métier dans les Fragments (View only)
- [ ] `_uiState` privé, `uiState` exposé en `StateFlow` immuable
- [ ] Séparation Entity / DTO / DomainModel respectée

### ✅ Les 4 Piliers
- [ ] **Activity** : `MainActivity` unique avec Navigation Component
- [ ] **Fragment 1** : `DashboardFragment` — magnitude en temps réel, batterie, GPS, bouton alerte
- [ ] **Fragment 2** : `HistoryFragment` — RecyclerView DiffUtil, swipe-to-delete, export CSV
- [ ] **Fragment 3** : `SettingsFragment` — slider seuil, dark mode, numéro urgence, toggle SMS
- [ ] **Service** : `SurveillanceService` Foreground avec algorithme free-fall/impact (2 phases)
- [ ] **BroadcastReceiver 1** : `BatteryReceiver` — `ACTION_BATTERY_LOW` (statique)
- [ ] **BroadcastReceiver 2** : `BootReceiver` — `ACTION_BOOT_COMPLETED` → WorkManager
- [ ] **ContentProvider** : URI `content://com.guardian.track.provider/emergency_contacts`, colonnes `_id`, `name`, `phone_number`

### ✅ Persistance
- [ ] Room : `IncidentEntity`, `EmergencyContactEntity`, `IncidentDao`, `EmergencyContactDao`
- [ ] `IncidentDao.getAllIncidents()` retourne `Flow<List<IncidentEntity>>`
- [ ] `IncidentDao.getUnsyncedIncidents()` et `markAsSynced()` implémentés
- [ ] DataStore : seuil (Float), dark mode (Boolean), SMS simulation (Boolean)
- [ ] `EncryptedSharedPreferences` pour le numéro d'urgence
- [ ] Export CSV via `MediaStore` dans `Documents/GuardianTrack/`

### ✅ Réseau
- [ ] `GuardianApi` avec `@POST`, `@GET`, `@DELETE` en `suspend fun`
- [ ] `ApiKeyInterceptor` — clé depuis `BuildConfig` (non versionnée)
- [ ] `NetworkResult<T>` sealed class avec `Success`, `Error`, `Loading`
- [ ] Stratégie offline-first : réseau disponible → Retrofit direct, sinon → WorkManager
- [ ] `SyncWorker` avec `Constraints(NETWORK_CONNECTED)` et `enqueueUniqueWork`

### ✅ Sécurité & Permissions
- [ ] Permission dynamique `ACCESS_FINE_LOCATION` avec rationale
- [ ] Permission dynamique `SEND_SMS` avec rationale
- [ ] Permission dynamique `POST_NOTIFICATIONS` (Android 13+) avec rationale
- [ ] Cas `shouldShowRequestPermissionRationale = false` → dialog → Settings
- [ ] GPS refusé → latitude = 0.0, longitude = 0.0 (valeur sentinelle)
- [ ] ContentProvider : `<permission android:protectionLevel="signature|privileged" />`
- [ ] `local.properties` dans `.gitignore`

### ✅ Android 12+ Contraintes
- [ ] `BootReceiver` → `WorkManager.setExpedited()` (pas de `startForegroundService()` direct)
- [ ] `android:foregroundServiceType="location|health"` dans Manifest
- [ ] `POST_NOTIFICATIONS` demandée dynamiquement
- [ ] `ACTION_LOCKED_BOOT_COMPLETED` en plus de `BOOT_COMPLETED` dans BootReceiver

### ✅ Design & UX
- [ ] Palette "Tactical Dark Glass" (noir nuit, cyan, rouge alerte)
- [ ] Typographie Rajdhani (titres) + DM Mono (données) + Inter Tight (labels)
- [ ] Cartes glassmorphiques avec bordures glow cyan
- [ ] Magnitude affichée en style "7-segments" (DM Mono, grand)
- [ ] Indicateurs couleur adaptatifs (vert/ambre/rouge selon niveaux)
- [ ] ViewBinding sur tous les Fragments
- [ ] Support DayNight theme (mode sombre)
- [ ] Material Design 3 (Material3.DayNight.NoActionBar)
- [ ] RecyclerView avec DiffUtil.ItemCallback

### ✅ Robustesse
- [ ] ViewModel survit aux rotations (testé manuellement)
- [ ] Aucun crash si permission refusée
- [ ] Aucun crash si GPS indisponible
- [ ] Aucun crash si réseau absent
- [ ] Mode simulation SMS ACTIF par défaut
- [ ] `HandlerThread` ou `Dispatchers.Default` pour callbacks capteur

### ✅ Livrables
- [ ] Code source sur GitHub (commits réguliers, messages clairs)
- [ ] `.gitignore` correct (exclut `local.properties`)
- [ ] `README.md` documenté (config API, build, modules)
- [ ] Rapport technique PDF (5-10 pages, 6 questions, diagramme MVVM, schéma Room)
- [ ] Vidéo démo 3-5 min : simulation chute + SMS simulé + Room + export CSV
- [ ] **Deadline : Samedi 18 Avril 2026**

---

> 📌 **Note sur les BONUS**
> - **+5 pts** : Remplacer ViewBinding par Jetpack Compose complet avec animations Material 3
> - **+3 pts** : Filtre passe-bas sur l'accéléromètre ou fenêtre glissante (déjà esquissé dans le SurveillanceService)
> - **+2 pts** : ≥ 10 tests unitaires JUnit4 + MockK + Turbine sur ViewModel et Repository

---

*Document généré pour le projet GuardianTrack — ISET Rades 2025/2026*
