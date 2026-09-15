package com.example.smartdevicegenerator.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.smartdevicegenerator.data.AndroidVersionDataset
import com.example.smartdevicegenerator.data.DeviceDataset
import com.example.smartdevicegenerator.generator.DeviceGenerator
import com.example.smartdevicegenerator.model.AndroidVersion
import com.example.smartdevicegenerator.model.DeviceProfile
import com.example.smartdevicegenerator.model.GeneratedDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray

class DeviceRepository(private val context: Context? = null) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences("sdg_history_prefs", Context.MODE_PRIVATE)
    private val PREF_KEY_HISTORY = "saved_generated_devices"

    private val _history = MutableStateFlow<List<GeneratedDevice>>(emptyList())
    val history: StateFlow<List<GeneratedDevice>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    suspend fun generateDevice(
        version: AndroidVersion,
        profile: DeviceProfile? = null
    ): GeneratedDevice = withContext(Dispatchers.Default) {
        val device = DeviceGenerator.generateDevice(version, profile)
        saveToHistory(device)
        device
    }

    fun getAllProfiles(): List<DeviceProfile> = DeviceDataset.profiles

    fun searchProfiles(query: String, minApi: Int? = null, maxApi: Int? = null, brand: String? = null): List<DeviceProfile> {
        val q = query.trim().lowercase()
        return DeviceDataset.profiles.filter { profile ->
            val matchesQuery = q.isEmpty() ||
                profile.deviceName.lowercase().contains(q) ||
                profile.model.lowercase().contains(q) ||
                profile.brand.lowercase().contains(q) ||
                profile.soc.lowercase().contains(q) ||
                profile.deviceCodename.lowercase().contains(q)

            val matchesMinApi = minApi == null || profile.maximumKnownAndroidApi >= minApi
            val matchesMaxApi = maxApi == null || profile.minimumAndroidApi <= maxApi
            val matchesBrand = brand.isNullOrEmpty() || profile.brand.equals(brand, ignoreCase = true)

            matchesQuery && matchesMinApi && matchesMaxApi && matchesBrand
        }
    }

    private fun loadHistory() {
        val jsonStr = prefs?.getString(PREF_KEY_HISTORY, null) ?: return
        try {
            val array = JSONArray(jsonStr)
            val list = mutableListOf<GeneratedDevice>()
            for (i in 0 until array.length()) {
                val itemStr = array.getString(i)
                val dev = GeneratedDevice.fromJson(itemStr) { api ->
                    AndroidVersionDataset.getByApiLevel(api)
                }
                list.add(dev)
            }
            _history.value = list
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToHistory(device: GeneratedDevice) {
        val current = _history.value.toMutableList()
        current.add(0, device)
        if (current.size > 100) {
            current.removeAt(current.size - 1)
        }
        _history.value = current
        persistHistory(current)
    }

    fun deleteHistoryItem(id: String) {
        val current = _history.value.filter { it.id != id }
        _history.value = current
        persistHistory(current)
    }

    fun clearHistory() {
        _history.value = emptyList()
        persistHistory(emptyList())
    }

    private fun persistHistory(devices: List<GeneratedDevice>) {
        if (prefs == null) return
        try {
            val array = JSONArray()
            devices.forEach { array.put(it.toJsonString()) }
            prefs.edit().putString(PREF_KEY_HISTORY, array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
