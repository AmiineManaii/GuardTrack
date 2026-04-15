package com.example.guardiantrack.data.remote.api

import com.example.guardiantrack.data.remote.api.dto.IncidentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GuardianApi {

    @GET("Incident")
    suspend fun getIncidents(): Response<List<IncidentDto>>

    @POST("Incident")
    suspend fun postIncident(@Body incident: IncidentDto): Response<IncidentDto>
}
