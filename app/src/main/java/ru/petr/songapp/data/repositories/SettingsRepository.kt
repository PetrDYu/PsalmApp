package ru.petr.songapp.data.repositories

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import ru.petr.songapp.data.models.datastore.settings.SettingsStore

class SettingsRepository(private val settingsStore: SettingsStore) {
    val settingsMap = mapOf<Settings, Any>(
        Settings.SONG_FONT_SIZE to 14,
    )

    private val scope = MainScope()

    val songFontSize: Flow<Int>
        get() {
            val defaultValue = settingsMap.getOrElse(Settings.SONG_FONT_SIZE) {
                throw IllegalStateException("The default value of ${Settings.SONG_FONT_SIZE} setting is not defined")
            }
            return settingsStore.getIntSetting(Settings.SONG_FONT_SIZE.settingName, defaultValue as Int)
        }

    fun storeSongFontSize(value: Int) {
        scope.launch {
            settingsStore.storeIntSetting(Settings.SONG_FONT_SIZE.settingName, value)
        }
    }
}

private data class Setting <T> (val settingName: Settings, val defaultValue: T)

enum class Settings(val settingName: String) {
    SONG_FONT_SIZE("song_font_size"),
}

