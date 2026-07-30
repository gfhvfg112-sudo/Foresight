package com.foresight.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.foresight.app.R
import com.foresight.app.util.ExpiryUtils

@Composable
fun ExpiryIndicator(
    expiryDate: Long,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val daysLeft = com.foresight.app.util.DateUtils.daysUntil(expiryDate)
    val color = ExpiryUtils.getExpiryColor(daysLeft)
    val label = getExpiryLabel(daysLeft)

    if (compact) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.08f))
                .border(
                    width = 0.5.dp,
                    color = color.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.08f))
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

/**
 * Resolve expiry label using string resources for proper localization.
 */
@Composable
private fun getExpiryLabel(daysLeft: Long): String {
    return when {
        daysLeft < -1 -> stringResource(R.string.expiry_days_ago, -daysLeft.toInt())
        daysLeft == -1L -> stringResource(R.string.expiry_yesterday)
        daysLeft == 0L -> stringResource(R.string.expiry_today)
        daysLeft == 1L -> stringResource(R.string.expiry_tomorrow)
        daysLeft in 2..30 -> stringResource(R.string.expiry_days_left, daysLeft.toInt())
        daysLeft in 31..365 -> stringResource(R.string.expiry_months_left, (daysLeft / 30).toInt())
        else -> stringResource(R.string.expiry_years_left, (daysLeft / 365).toInt())
    }
}
