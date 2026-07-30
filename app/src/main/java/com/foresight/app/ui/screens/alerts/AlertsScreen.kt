package com.foresight.app.ui.screens.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresight.app.R
import com.foresight.app.ui.components.EmptyState
import com.foresight.app.ui.components.ItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    onItemClick: (Long) -> Unit,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasAnyItems = uiState.expiredItems.isNotEmpty() || uiState.expiringItems.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.alerts_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        if (!hasAnyItems && !uiState.isLoading) {
            EmptyState(
                icon = Icons.Default.CheckCircle,
                title = stringResource(R.string.alerts_clear_title),
                subtitle = stringResource(R.string.alerts_clear_desc),
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (uiState.expiredItems.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.alerts_expired, uiState.expiredItems.size),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(uiState.expiredItems, key = { "expired_${it.item.id}" }) { item ->
                        ItemCard(
                            itemWithCategory = item,
                            onClick = { onItemClick(item.item.id) }
                        )
                    }
                }

                if (uiState.expiringItems.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.alerts_expiring, uiState.expiringItems.size),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(uiState.expiringItems, key = { "expiring_${it.item.id}" }) { item ->
                        ItemCard(
                            itemWithCategory = item,
                            onClick = { onItemClick(item.item.id) }
                        )
                    }
                }
            }
        }
    }
}
