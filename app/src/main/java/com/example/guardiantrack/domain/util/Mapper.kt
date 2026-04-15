package com.example.guardiantrack.domain.util

import com.example.guardiantrack.data.local.db.entity.EmergencyContactEntity
import com.example.guardiantrack.data.local.db.entity.IncidentEntity
import com.example.guardiantrack.data.remote.api.dto.IncidentDto
import com.example.guardiantrack.domain.model.EmergencyContact
import com.example.guardiantrack.domain.model.Incident
import com.example.guardiantrack.domain.model.IncidentType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun IncidentEntity.toDomain(): Incident {
    val date = Date(timestamp)
    val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return Incident(
        id = id,
        timestamp = timestamp,
        type = IncidentType.valueOf(type),
        latitude = latitude,
        longitude = longitude,
        isSynced = isSynced,
        formattedDate = dateFmt.format(date),
        formattedTime = timeFmt.format(date)
    )
}

fun Incident.toEntity(): IncidentEntity = IncidentEntity(
    id = id,
    timestamp = timestamp,
    type = type.name,
    latitude = latitude,
    longitude = longitude,
    isSynced = isSynced
)

fun IncidentEntity.toDto(): IncidentDto = IncidentDto(
    id = id,
    timestamp = timestamp,
    type = type,
    latitude = latitude,
    longitude = longitude
)

fun EmergencyContactEntity.toDomain(): EmergencyContact =
    EmergencyContact(id = id, name = name, phoneNumber = phoneNumber)

fun EmergencyContact.toEntity(): EmergencyContactEntity =
    EmergencyContactEntity(id = id, name = name, phoneNumber = phoneNumber)