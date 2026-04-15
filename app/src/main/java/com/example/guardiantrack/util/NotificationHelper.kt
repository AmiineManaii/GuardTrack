package com.example.guardiantrack.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ALERTS = "guardian_alerts_channel"
        const val NOTIF_FALL_ID = 2001
        const val NOTIF_BATT_ID = 2002
        const val NOTIF_SMS_ID = 2003
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
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_FALL_ID, notif)
    }

    fun showBatteryAlert() {
        val notif = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("🔋 Batterie critique")
            .setContentText("Niveau de batterie critique. Incident enregistré.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_BATT_ID, notif)
    }

    fun showSimulatedSms(phoneNumber: String, message: String) {
        val notif = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setContentTitle("📱 SMS simulé → $phoneNumber")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_SMS_ID, notif)
    }
}