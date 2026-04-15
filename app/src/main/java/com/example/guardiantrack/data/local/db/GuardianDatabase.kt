package com.example.guardiantrack.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.guardiantrack.data.local.db.dao.EmergencyContactDao
import com.example.guardiantrack.data.local.db.dao.IncidentDao
import com.example.guardiantrack.data.local.db.entity.EmergencyContactEntity
import com.example.guardiantrack.data.local.db.entity.IncidentEntity

@Database(
    entities = [IncidentEntity::class, EmergencyContactEntity::class],
    version = 2, // PASSÉ DE 1 À 2
    exportSchema = false
)
abstract class GuardianDatabase : RoomDatabase() {
    abstract fun incidentDao(): IncidentDao
    abstract fun contactDao(): EmergencyContactDao

    companion object {
        const val DATABASE_NAME = "guardian_track.db"
    }
}