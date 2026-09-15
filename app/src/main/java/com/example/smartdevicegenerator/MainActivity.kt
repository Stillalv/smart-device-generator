package com.example.smartdevicegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.smartdevicegenerator.ui.navigation.AppNavigation
import com.example.smartdevicegenerator.ui.theme.SmartDeviceGeneratorTheme
import com.example.smartdevicegenerator.viewmodel.DeviceGeneratorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DeviceGeneratorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()

            SmartDeviceGeneratorTheme(themeMode = themeMode) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
