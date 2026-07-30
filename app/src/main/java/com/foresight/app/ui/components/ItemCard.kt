package com.foresight.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.foresight.app.R
import com.foresight.app.data.local.relations.ItemWithCategory

@Composable
fun ItemCard(
    itemWithCategory: ItemWithCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = itemWithCategory.item
    val category = itemWithCategory.category

    val categoryColor = try {
        Color(android.graphics.Color.parseColor(category.colorHex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.large,
                ambientColor = categoryColor.copy(alpha = 0.15f),
                spotColor = categoryColor.copy(alpha = 0.10f)
            )
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            categoryColor.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category color indicator with gradient
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = MaterialTheme.shapes.medium,
                        ambientColor = categoryColor.copy(alpha = 0.3f)
                    ),
                shape = MaterialTheme.shapes.medium,
                color = categoryColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = category.name.first().toString().uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = categoryColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = categoryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            ExpiryIndicator(expiryDate = item.expiryDate, compact = true)

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
