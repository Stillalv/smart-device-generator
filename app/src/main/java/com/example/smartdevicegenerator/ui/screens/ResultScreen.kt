package com.example.smartdevicegenerator.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdevicegenerator.ui.components.CopyableField
import com.example.smartdevicegenerator.ui.components.DeviceSection
import com.example.smartdevicegenerator.ui.components.GenerateButton
import com.example.smartdevicegenerator.ui.components.LucideIcons
import com.example.smartdevicegenerator.ui.components.SectionDivider
import com.example.smartdevicegenerator.viewmodel.DeviceGeneratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: DeviceGeneratorViewModel,
    onBack: () -> Unit
) {
    val deviceState by viewModel.generatedDevice.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val context = LocalContext.current
    var showExportDialog by remember { mutableStateOf(false) }

    val device = deviceState ?: run {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No device generated yet.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) { Text("Back") }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generated Device", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(LucideIcons.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(LucideIcons.Share, contentDescription = "Export")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header card with device name and icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = LucideIcons.Smartphone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = device.deviceName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${device.brand} • ${device.model}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 1. Device Info
            DeviceSection(title = "Device Info") {
                CopyableField(label = "Model", value = device.model)
                SectionDivider()
                CopyableField(label = "Brand", value = device.brand)
                SectionDivider()
                CopyableField(label = "Product Name", value = device.productName)
                SectionDivider()
                CopyableField(label = "Device Name", value = device.deviceName)
                SectionDivider()
                CopyableField(label = "Manufacturer", value = device.manufacturer)
            }

            // 2. Android Info
            DeviceSection(title = "Android Info") {
                CopyableField(label = "Android Version", value = device.androidVersion.displayName)
                SectionDivider()
                CopyableField(label = "SDK Version", value = device.sdkVersion.toString())
                SectionDivider()
                CopyableField(label = "Build ID", value = device.buildId)
                SectionDivider()
                CopyableField(label = "Device Codename", value = device.deviceCodename)
            }

            // 3. Processor Info
            DeviceSection(title = "Processor Info") {
                CopyableField(label = "SoC", value = device.soc)
                SectionDivider()
                CopyableField(label = "CPU", value = device.cpu)
                SectionDivider()
                CopyableField(label = "GPU", value = device.gpu)
                SectionDivider()
                CopyableField(label = "Architecture", value = device.cpuArchitecture)
                SectionDivider()
                CopyableField(label = "ABI", value = device.abi)
            }

            // 4. Memory & Storage
            DeviceSection(title = "Memory & Storage") {
                CopyableField(label = "RAM", value = device.ram)
                SectionDivider()
                CopyableField(label = "Storage", value = device.storage)
            }

            // 5. Display
            DeviceSection(title = "Display") {
                CopyableField(label = "Resolution", value = device.formattedResolution)
                SectionDivider()
                CopyableField(label = "Screen Size", value = device.screenSize)
                SectionDivider()
                CopyableField(label = "Density", value = "${device.density} dpi")
                SectionDivider()
                CopyableField(label = "Refresh Rate", value = "${device.refreshRate} Hz")
            }

            // 6. Device ID
            DeviceSection(title = "Device ID") {
                CopyableField(label = "Serial Number", value = device.serialNumber)
                SectionDivider()
                CopyableField(label = "Android ID", value = device.androidId)
            }

            // 7. Build Fingerprint
            DeviceSection(title = "Build Fingerprint") {
                CopyableField(label = "Fingerprint", value = device.buildFingerprint)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            GenerateButton(
                text = "Generate Another",
                icon = LucideIcons.Refresh,
                isLoading = isGenerating,
                onClick = { viewModel.generateDevice() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showExportDialog = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = LucideIcons.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Profile", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Export Options Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Device Profile", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Choose an export format to copy to clipboard or share via other applications.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Device JSON", device.toJsonString()))
                        Toast.makeText(context, "JSON copied to clipboard", Toast.LENGTH_SHORT).show()
                        showExportDialog = false
                    }
                ) {
                    Text("Copy JSON")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Device Profile", device.toShareText()))
                            Toast.makeText(context, "Full profile copied", Toast.LENGTH_SHORT).show()
                            showExportDialog = false
                        }
                    ) {
                        Text("Copy All")
                    }
                    TextButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, device.toJsonString())
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Device Profile"))
                            showExportDialog = false
                        }
                    ) {
                        Text("Share")
                    }
                }
            }
        )
    }
}
