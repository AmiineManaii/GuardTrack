package com.example.guardiantrack.data.remote.api.dto

import com.google.gson.annotations.SerializedName

data class IncidentDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("type") val type: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)