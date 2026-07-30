package com.foresight.app.util

import android.content.Context
import com.foresight.app.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {

    private val displayFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    private val relativeFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    fun formatDate(millis: Long): String = displayFormat.format(Date(millis))

    fun formatDateShort(millis: Long): String = shortFormat.format(Date(millis))

    fun formatRelativeDate(millis: Long): String = relativeFormat.format(Date(millis))

    fun daysUntil(millis: Long): Long {
        val now = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(millis - now)
    }

    fun isExpired(millis: Long): Boolean = daysUntil(millis) < 0

    fun isExpiringSoon(millis: Long, withinDays: Int = 7): Boolean {
        val days = daysUntil(millis)
        return days in 0..withinDays
    }

    /** Localized expiry label using Context resources */
    fun getExpiryLabel(context: Context, millis: Long): String {
        val days = daysUntil(millis)
        return when {
            days < -1 -> context.getString(R.string.expiry_days_ago, -days)
            days == -1L -> context.getString(R.string.expiry_yesterday)
            days == 0L -> context.getString(R.string.expiry_today)
            days == 1L -> context.getString(R.string.expiry_tomorrow)
            days in 2..30 -> context.getString(R.string.expiry_days_left, days)
            days in 31..365 -> context.getString(R.string.expiry_months_left, days / 30)
            else -> context.getString(R.string.expiry_years_left, days / 365)
        }
    }

    fun startOfDay(millis: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = millis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun daysFromNow(days: Int): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, days)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis
    }
}
