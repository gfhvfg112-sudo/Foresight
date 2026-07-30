package com.foresight.app.util

import androidx.compose.ui.graphics.Color

object ExpiryUtils {

    val ColorGreen = Color(0xFF0D652D)
    val ColorYellow = Color(0xFFE8710A)
    val ColorOrange = Color(0xFFC45200)
    val ColorRed = Color(0xFFB3261E)
    val ColorExpired = Color(0xFF8C1D18)

    fun getExpiryColor(daysUntilExpiry: Long): Color {
        return when {
            daysUntilExpiry < 0 -> ColorExpired
            daysUntilExpiry == 0L -> ColorRed
            daysUntilExpiry <= 3 -> ColorOrange
            daysUntilExpiry <= 7 -> ColorYellow
            daysUntilExpiry <= 30 -> Color(0xFFB06000)
            else -> ColorGreen
        }
    }

    fun getExpirySeverity(daysUntilExpiry: Long): ExpirySeverity {
        return when {
            daysUntilExpiry < 0 -> ExpirySeverity.EXPIRED
            daysUntilExpiry <= 3 -> ExpirySeverity.CRITICAL
            daysUntilExpiry <= 7 -> ExpirySeverity.WARNING
            daysUntilExpiry <= 30 -> ExpirySeverity.APPROACHING
            else -> ExpirySeverity.SAFE
        }
    }

    enum class ExpirySeverity {
        SAFE, APPROACHING, WARNING, CRITICAL, EXPIRED
    }
}
