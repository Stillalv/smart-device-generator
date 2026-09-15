package com.example.smartdevicegenerator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdevicegenerator.data.AndroidVersionDataset
import com.example.smartdevicegenerator.data.DeviceDataset
import com.example.smartdevicegenerator.model.AndroidVersion
import com.example.smartdevicegenerator.model.DeviceProfile
import com.example.smartdevicegenerator.model.GeneratedDevice
import com.example.smartdevicegenerator.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

data class FilterState(
    val selectedBrand: String? = null,
    val selectedArchitecture: String? = null,
    val selectedEra: String? = null,
    val minRam: String? = null,
    val minRefreshRate: Int? = null
)

class DeviceGeneratorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DeviceRepository(application.applicationContext)

    init {
        DeviceDataset.initialize(application.applicationContext)
    }

    // Selected Android version (defaults to Android 14 API 34)
    private val _selectedVersion = MutableStateFlow(AndroidVersionDataset.getByApiLevel(34))
    val selectedVersion: StateFlow<AndroidVersion> = _selectedVersion.asStateFlow()

    // Include Legacy Devices toggle (OFF by default: only API 21+)
    private val _includeLegacy = MutableStateFlow(false)
    val includeLegacy: StateFlow<Boolean> = _includeLegacy.asStateFlow()

    // Current generated device
    private val _generatedDevice = MutableStateFlow<GeneratedDevice?>(null)
    val generatedDevice: StateFlow<GeneratedDevice?> = _generatedDevice.asStateFlow()

    // Loading state
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    // Search query & results
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<DeviceProfile>>(emptyList())
    val searchResults: StateFlow<List<DeviceProfile>> = _searchResults.asStateFlow()

    // Filter sheet state
    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // History from repository
    val history: StateFlow<List<GeneratedDevice>> = repository.history
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Theme Mode
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun selectVersion(version: AndroidVersion) {
        _selectedVersion.value = version
    }

    fun toggleLegacyMode(enabled: Boolean) {
        _includeLegacy.value = enabled
        // If legacy disabled and current version is legacy, fallback to Android 5.0 (API 21)
        if (!enabled && _selectedVersion.value.isLegacy) {
            _selectedVersion.value = AndroidVersionDataset.getByApiLevel(21)
        }
    }

    fun generateDevice(targetProfile: DeviceProfile? = null) {
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val device = repository.generateDevice(_selectedVersion.value, targetProfile)
                _generatedDevice.value = device
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applySearchAndFilter()
    }

    fun updateFilters(newFilters: FilterState) {
        _filterState.value = newFilters
        applySearchAndFilter()
    }

    fun resetFilters() {
        _filterState.value = FilterState()
        applySearchAndFilter()
    }

    private fun applySearchAndFilter() {
        val q = _searchQuery.value.trim().lowercase()
        val filter = _filterState.value
        val all = repository.getAllProfiles()

        _searchResults.value = all.filter { p ->
            val matchesQuery = q.isEmpty() ||
                p.deviceName.lowercase().contains(q) ||
                p.model.lowercase().contains(q) ||
                p.brand.lowercase().contains(q) ||
                p.soc.lowercase().contains(q) ||
                p.deviceCodename.lowercase().contains(q)

            val matchesBrand = filter.selectedBrand == null || p.brand.equals(filter.selectedBrand, ignoreCase = true)
            val matchesArch = filter.selectedArchitecture == null || p.cpuArchitecture.equals(filter.selectedArchitecture, ignoreCase = true)
            val matchesRefresh = filter.minRefreshRate == null || p.refreshRate >= filter.minRefreshRate

            matchesQuery && matchesBrand && matchesArch && matchesRefresh
        }
    }

    fun deleteHistoryItem(id: String) {
        repository.deleteHistoryItem(id)
    }

    fun clearAllHistory() {
        repository.clearHistory()
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun viewDeviceDetail(device: GeneratedDevice) {
        _generatedDevice.value = device
    }
}
