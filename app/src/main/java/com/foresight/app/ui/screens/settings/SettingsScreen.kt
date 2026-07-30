package com.foresight.app.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresight.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onPremiumClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Premium banner (if not premium)
            if (!uiState.isPremium) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable(onClick = onPremiumClick),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                stringResource(R.string.settings_premium_banner),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                stringResource(R.string.settings_premium_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Notifications toggle
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_notifications)) },
                supportingContent = { Text(stringResource(R.string.settings_notifications_desc)) },
                trailingContent = {
                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotifications(it) }
                    )
                },
                leadingContent = {
                    Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.primary)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Default alert days
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_alert_time)) },
                supportingContent = { Text(stringResource(R.string.settings_alert_time_desc, uiState.defaultAlertDays)) },
                leadingContent = {
                    Icon(Icons.Default.Alarm, null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.clickable { showAlertDialog = true }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // About
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_about)) },
                supportingContent = { Text(stringResource(R.string.settings_version, "1.0.0")) },
                leadingContent = {
                    Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.primary)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Data management
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_export)) },
                supportingContent = { Text(stringResource(R.string.settings_export_desc)) },
                leadingContent = {
                    Icon(Icons.Default.FileDownload, null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }

    // Alert days picker dialog
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = { Text(stringResource(R.string.settings_dialog_title)) },
            text = {
                Column {
                    listOf(1, 3, 5, 7, 14, 30).forEach { days ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateDefaultAlertDays(days)
                                    showAlertDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            RadioButton(
                                selected = uiState.defaultAlertDays == days,
                                onClick = {
                                    viewModel.updateDefaultAlertDays(days)
                                    showAlertDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (days == 1) stringResource(R.string.settings_day_singular, days)
                                else stringResource(R.string.settings_day_plural, days)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAlertDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
