package com.example.smartdevicegenerator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdevicegenerator.data.AndroidVersionDataset
import com.example.smartdevicegenerator.data.DeviceDataset
import com.example.smartdevicegenerator.ui.components.CopyableField
import com.example.smartdevicegenerator.ui.components.DeviceSection
import com.example.smartdevicegenerator.ui.components.LucideIcons
import com.example.smartdevicegenerator.ui.components.SectionDivider
import com.example.smartdevicegenerator.viewmodel.DeviceGeneratorViewModel
import com.example.smartdevicegenerator.viewmodel.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: DeviceGeneratorViewModel
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Theme Section
            DeviceSection(title = "Appearance") {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Theme Mode",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Choose light, dark, or system default.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeMode.values().forEach { mode ->
                            val isSelected = themeMode == mode
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setThemeMode(mode) },
                                label = {
                                    Text(
                                        when (mode) {
                                            ThemeMode.SYSTEM -> "System"
                                            ThemeMode.LIGHT -> "Light"
                                            ThemeMode.DARK -> "Dark"
                                        }
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }

            // Database Statistics
            DeviceSection(title = "Database & Generator") {
                CopyableField(
                    label = "Total Device Profiles",
                    value = "${DeviceDataset.profiles.size} authentic profiles"
                )
                SectionDivider()
                CopyableField(
                    label = "Android Versions",
                    value = "Android 1.0 (API 1) - Android 17 (API 37)"
                )
                SectionDivider()
                CopyableField(
                    label = "Offline Status",
                    value = "100% Offline-First (No API/Network needed)"
                )
            }

            // About Section
            DeviceSection(title = "About Application") {
                CopyableField(label = "Application", value = "Smart Device Generator")
                SectionDivider()
                CopyableField(label = "Version", value = "1.0.0 (Build 1)")
                SectionDivider()
                CopyableField(label = "Min SDK", value = "API 21 (Android 5.0 Lollipop)")
                SectionDivider()
                CopyableField(label = "Target SDK", value = "API 35 (Android 15)")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
