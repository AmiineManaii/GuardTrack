package com.example.guardiantrack.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.guardiantrack.domain.model.Incident
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
    fun exportToCsv(incidents: List<Incident>): String? {
        return exportToFile(incidents, "csv", "text/csv")
    }

    fun exportToTxt(incidents: List<Incident>): String? {
        return exportToFile(incidents, "txt", "text/plain")
    }

    private fun exportToFile(incidents: List<Incident>, extension: String, mimeType: String): String? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "GuardianTrack_Export_$timestamp.$extension"

        return try {
            val content = if (extension == "csv") buildCsvContent(incidents) else buildTxtContent(incidents)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, filename)
                    put(MediaStore.Downloads.MIME_TYPE, mimeType)
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/GuardianTrack")
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { os ->
                        os.write(content.toByteArray(Charsets.UTF_8))
                    }
                    filename
                }
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!dir.exists()) dir.mkdirs()
                val file = java.io.File(dir, filename)
                file.writeText(content, Charsets.UTF_8)
                filename
            }
        } catch (e: Exception) {
            Log.e("ExportHelper", "Export to $extension failed: ${e.message}", e)
            null
        }
    }

    private fun buildCsvContent(incidents: List<Incident>): String {
        val sb = StringBuilder()
        sb.appendLine("Date,Heure,Type d'incident,Latitude,Longitude,Statut")

        val dateFmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        incidents.forEach { incident ->
            val date = dateFmt.format(Date(incident.timestamp))
            val time = timeFmt.format(Date(incident.timestamp))
            val syncStatus = if (incident.isSynced) "Synchronisé" else "Non synchronisé"
            val lat = if (incident.latitude == 0.0) "N/A" else "%.6f".format(incident.latitude)
            val lon = if (incident.longitude == 0.0) "N/A" else "%.6f".format(incident.longitude)
            sb.appendLine("$date,$time,${incident.type.name},$lat,$lon,$syncStatus")
        }

        return sb.toString()
    }

    private fun buildTxtContent(incidents: List<Incident>): String {
        val sb = StringBuilder()
        sb.appendLine("=== RAPPORT D'INCIDENTS GUARDIANTRACK ===")
        sb.appendLine("Généré le : ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}")
        sb.appendLine("-------------------------------------------")

        val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        incidents.forEach { incident ->
            sb.appendLine("ID        : ${incident.id}")
            sb.appendLine("DATE      : ${fmt.format(Date(incident.timestamp))}")
            sb.appendLine("TYPE      : ${incident.type.name}")
            sb.appendLine("LATITUDE  : ${if (incident.latitude == 0.0) "N/A" else incident.latitude}")
            sb.appendLine("LONGITUDE : ${if (incident.longitude == 0.0) "N/A" else incident.longitude}")
            sb.appendLine("STATUT    : ${if (incident.isSynced) "SYNCHRONISÉ" else "NON SYNCHRONISÉ"}")
            sb.appendLine("-------------------------------------------")
        }

        return sb.toString()
    }
}
