# GuardianTrack 🛡️

**Application Android de Sécurité Personnelle avec Détection de Chute Intelligente.**

Projet réalisé dans le cadre du module Développement Android — ISET Rades (2025/2026).

---

## ✨ Fonctionnalités (Mode BONUS +10 pts)

- **🚀 Jetpack Compose & Material 3** : Interface "Magic" moderne 100% déclarative avec thèmes personnalisés (Cosmic Violet, Electric Cyan).
- **🎨 Thème Intelligent** : Mode sombre "Cockpit" et mode clair "Pure White" gérés directement dans l'application (indépendamment du système).
- **🧪 Détection de Chute en 2 Phases** : Algorithme optimisé avec **Filtre Passe-Bas**, alerte sonore instantanée (**Bip**) et icône de notification circulaire.
- **📱 Alerte SMS Automatique** : Envoi de SMS d'urgence réels avec **gestion intelligente des messages longs** (Multipart) ou mode simulation.
- **⚙️ Sensibilité Radio** : Ajustement ultra-précis via un **Slider Circulaire** interactif style radio vintage.
- **📍 Géolocalisation Précise** : Tagging GPS des incidents avec lien Google Maps dans les SMS.
- **📊 Export de Données** : Exportation du journal des incidents (CSV/TXT) avec **Toasts de confirmation** et gestion d'erreurs.
- **🔄 Synchronisation Cloud** : Offline-first avec Room et synchronisation automatique via Retrofit + WorkManager.
- **🧪 Tests Unitaires** : Couverture de test robuste avec JUnit, MockK et Turbine.

---

## 🛠️ Stack Technique

- **Langage** : Kotlin
- **UI** : Jetpack Compose, Material 3, Animations (Pulse, Spring, Transitions)
- **Injection de Dépendances** : Hilt (Dagger)
- **Persistence** : Room Database, DataStore (Preferences)
- **Réseau** : Retrofit 2, OkHttp 4 (Logging & Auth Interceptors)
- **Tâches de fond** : WorkManager, Foreground Services, BroadcastReceivers
- **Localisation** : Google Play Services Location
- **Tests** : JUnit 4, MockK, Turbine (Flow testing)

---

## 🚀 Installation & Configuration

### 1. Cloner le projet
```bash
git clone https://github.com/votre-username/GuardianTrack.git
```

### 2. Configurer les clés API
Créez ou modifiez le fichier `local.properties` à la racine du projet :

```properties
API_BASE_URL=https://votre-api-mock.io/
API_KEY=votre_cle_api_bearer
```

### 3. Build & Run
Ouvrez le projet dans **Android Studio Ladybug (ou +)** et lancez la compilation.

---

## 📂 Structure du Projet

- `com.guardian.track.domain` : Modèles de données, Interfaces Repository, Cas d'utilisation.
- `com.guardian.track.data` : Implémentations Room, Retrofit, DataStore, Repositories.
- `com.guardian.track.ui` : Écrans Compose, ViewModels, Thème.
- `com.guardian.track.service` : Service de surveillance en premier plan.
- `com.guardian.track.worker` : Tâches de synchronisation WorkManager.
- `com.guardian.track.util` : Filtres capteurs, Helpers (SMS, Export, Permissions).

---

## 🧪 Tests Unitaires

Pour exécuter les tests unitaires :
```bash
./gradlew test
```
Les tests couvrent notamment le filtrage des données capteurs et la logique de lissage.

---

## ⚖️ Licence

Projet académique — ISET Rades.
Analysé et optimisé par Trae IDE.
