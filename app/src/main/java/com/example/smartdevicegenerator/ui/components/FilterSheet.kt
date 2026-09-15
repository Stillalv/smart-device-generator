package com.example.smartdevicegenerator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdevicegenerator.viewmodel.FilterState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSheet(
    filterState: FilterState,
    availableBrands: List<String>,
    onApply: (FilterState) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentFilter by remember { mutableStateOf(filterState) }
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Detailed Device Filters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Release Era Filter
            Text(
                text = "RELEASE ERA",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val eras = listOf(
                "2008-2012" to "Legacy (2008-2012)",
                "2013-2017" to "4G Early (2013-2017)",
                "2018-2022" to "Modern 5G (2018-2022)",
                "2023+" to "Current Gen (2023+)"
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                eras.forEach { (key, label) ->
                    val isSelected = currentFilter.selectedEra == key
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentFilter = currentFilter.copy(
                                selectedEra = if (isSelected) null else key
                            )
                        },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. SoC Family Filter
            Text(
                text = "SOC CHIPSET FAMILY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val socFamilies = listOf("Snapdragon", "Dimensity", "Exynos", "Tensor", "Helio", "Kirin", "Intel")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                socFamilies.forEach { soc ->
                    val isSelected = currentFilter.selectedSoC == soc
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentFilter = currentFilter.copy(
                                selectedSoC = if (isSelected) null else soc
                            )
                        },
                        label = { Text(soc, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. CPU Architecture Filter
            Text(
                text = "CPU ARCHITECTURE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val architectures = listOf("ARM64", "ARM", "x86")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                architectures.forEach { arch ->
                    val isSelected = currentFilter.selectedArchitecture == arch
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentFilter = currentFilter.copy(
                                selectedArchitecture = if (isSelected) null else arch
                            )
                        },
                        label = { Text(arch) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Minimum Refresh Rate
            Text(
                text = "MINIMUM REFRESH RATE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val rates = listOf(60, 90, 120, 144)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rates.forEach { rate ->
                    val isSelected = currentFilter.minRefreshRate == rate
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentFilter = currentFilter.copy(
                                minRefreshRate = if (isSelected) null else rate
                            )
                        },
                        label = { Text("${rate}Hz+") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Brand Filter
            Text(
                text = "MANUFACTURER BRAND",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableBrands.forEach { brand ->
                    val isSelected = currentFilter.selectedBrand.equals(brand, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentFilter = currentFilter.copy(
                                selectedBrand = if (isSelected) null else brand
                            )
                        },
                        label = { Text(brand, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onReset()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = {
                        onApply(currentFilter)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }
}
