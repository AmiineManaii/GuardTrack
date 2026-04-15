# Rapport Technique — GuardianTrack

## 1. Flow vs LiveData : Choix et Justification
**Pourquoi Flow ?**
Dans ce projet, nous avons privilégié **Kotlin Flow** (et `StateFlow`) pour plusieurs raisons :
- **Programmation réactive moderne** : Flow fait partie des coroutines Kotlin, ce qui permet une intégration native et fluide avec les couches Repository et DataSource (Room supporte nativement Flow).
- **Opérateurs puissants** : Flow offre des opérateurs de transformation riches (`map`, `filter`, `combine`, `flatMapLatest`) bien plus puissants que ceux de LiveData.
- **Indépendance de la plateforme** : Contrairement à LiveData qui est lié au cycle de vie Android, Flow est une bibliothèque pure Kotlin, facilitant le test unitaire et la réutilisation dans des modules non-Android.
- **Jetpack Compose** : Flow se convertit très naturellement en état Compose via `collectAsStateWithLifecycle()`.

**Dans quel cas opter pour LiveData ?**
Nous aurions pu opter pour **LiveData** si le projet utilisait encore des **Vues XML traditionnelles** (DataBinding) sans Coroutines, car LiveData est extrêmement simple à l'ier aux fichiers XML et gère automatiquement l'arrêt de l'observation lors du `onStop` de l'activité.

---

## 2. Gestion du refus définitif de Localisation
**Comportement face au refus :**
Lorsque `shouldShowRequestPermissionRationale` retourne `false`, cela signifie que l'utilisateur a coché "Ne plus demander". 

**Stratégie de repli :**
1. **Dialogue Pédagogique** : Nous affichons un dialogue via `PermissionManager` expliquant que la détection d'incident perd 90% de son utilité sans GPS (car l'alerte ne contiendra pas la position).
2. **Redirection Paramètres** : Nous proposons un bouton "Paramètres" qui ouvre directement la page d'infos de l'application (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`) pour que l'utilisateur puisse activer manuellement la permission.
3. **Valeur Sentinelle** : Si l'utilisateur refuse toujours, l'incident est enregistré avec les coordonnées `0.0 / 0.0` (valeur sentinelle). Cela permet de garder une trace de l'incident (heure/date) même sans position géographique.

---

## 3. Sécurité du ContentProvider
**Limites de sécurité :**
Notre `EmergencyContactProvider` est protégé par une permission personnalisée définie sous le package de l'application (`com.example.guardiantrack.READ_EMERGENCY_CONTACTS`) avec un `protectionLevel="signature|privileged"`. Cela limite l'accès uniquement aux applications signées avec la même clé ou installées dans la partition système.

**Injection de Content Provider :**
Une attaque de type **injection** se produit lorsqu'un attaquant tente de passer des commandes SQL malveillantes via les paramètres `selection` ou `selectionArgs` pour accéder à des données non autorisées (ex: `1=1`).

**Protection :**
Dans notre code ([EmergencyContactProvider.kt](file:///c:/Users/amine/OneDrive/Bureau/developpement%20android/GuardianTrack/app/src/main/java/com/example/guardiantrack/provider/EmergencyContactProvider.kt)), nous nous protégeons en :
- **Ignorant les clauses `selection` externes** : Notre méthode `query` utilise directement les méthodes Room typées (`getAllContactsSync()`) sans concaténer de chaînes SQL dynamiques.
- **Utilisant MatrixCursor** : Nous reconstruisons manuellement le curseur retourné, ce qui sépare totalement la structure interne de la base Room de l'interface exposée.

---

## 4. Android 12+ et Services d'arrière-plan (Boot & Relance)
**Restrictions API 31+ :**
Depuis Android 12, les applications ne peuvent plus lancer de services foreground (`startForegroundService`) lorsqu'elles sont en arrière-plan, notamment après un broadcast `BOOT_COMPLETED`, sous peine de `ForegroundServiceStartNotAllowedException`.

**Solution de repli (WorkManager) :**
Nous utilisons **WorkManager** avec la méthode `setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)`. 
Dans notre `BootReceiver.kt`, nous lançons un `BootStartWorker` marqué comme **Expedited**. Android autorise WorkManager à s'exécuter immédiatement même en arrière-plan après un boot, et c'est ce Worker qui lance ensuite le `SurveillanceService` de manière sécurisée.

**Optimisations spécifiques (Infinix/XOS & Direct Boot) :**
Pour garantir le fonctionnement sur des appareils à surcouche agressive (Infinix, Xiaomi) et sous Android 14+, nous avons implémenté :
- **Direct Boot Aware** : Marquage du Receiver et du Service avec `android:directBootAware="true"` dans le Manifest pour permettre l'exécution dès que le système démarre, même si l'utilisateur n'a pas encore déverrouillé son écran.
- **QuickBoot Actions** : Ajout de l'action `QUICKBOOT_POWERON` pour supporter les chipsets MediaTek/Infinix.
- **FGS Type Health (Android 14)** : Déclaration de `foregroundServiceType="location|health"` accompagnée de la permission `HIGH_SAMPLING_RATE_SENSORS`, obligatoire pour surveiller l'accéléromètre en arrière-plan sur API 34+.

---

## 5. Séparation Entity / DTO / DomainModel
**Pourquoi séparer ?**
- **Entity (Room)** : Structure optimisée pour le stockage local (ex: clés primaires auto-incrémentées).
- **DTO (Retrofit)** : Structure imposée par l'API distante (ex: noms de champs en anglais ou JSON spécifique).
- **DomainModel** : Modèle pur utilisé par l'UI et la logique métier, indépendant de la base ou du réseau.

**Exemple concret (Code) :**
Dans [Incident.kt](file:///c:/Users/amine/OneDrive/Bureau/developpement%20android/GuardianTrack/app/src/main/java/com/example/guardiantrack/domain/model/Incident.kt), notre modèle de domaine contient des propriétés formatées comme `formattedDate`. 
Si l'API distante changeait le format de date (ex: de Timestamp à ISO-String), nous n'aurions qu'à modifier le **DTO** et son mapper, sans jamais impacter nos **ViewModels** ou nos **Écrans Compose**. Cela respecte le principe de responsabilité unique (SRP).

---

## 6. WorkManager vs JobScheduler vs AlarmManager
**Différences :**
- **AlarmManager** : Pour des actions à une heure précise (ex: réveil). Très coûteux en batterie.
- **JobScheduler** : API Android système pour différer des tâches, mais complexe et dépend de la version d'Android.
- **WorkManager** : La recommandation moderne. Il choisit intelligemment entre JobScheduler ou AlarmManager selon l'API du téléphone.

**Justification pour GuardianTrack :**
Nous avons choisi **WorkManager** pour la synchronisation car :
- **Persistance** : Si le téléphone s'éteint, la tâche de synchronisation est conservée et reprendra au redémarrage.
- **Contraintes** : Il permet de définir facilement la contrainte `NETWORK_CONNECTED`, garantissant que la synchronisation ne s'exécute que lorsque l'utilisateur a internet, économisant ainsi la batterie et les tentatives inutiles.
