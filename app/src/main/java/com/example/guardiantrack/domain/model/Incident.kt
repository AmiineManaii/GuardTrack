package com.example.guardiantrack.domain.model

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