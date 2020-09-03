package io.github.quwac.how_to_use_jetpack_datastore.ui.main

import android.content.Context
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.example.application.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class Repository(context: Context) {
    companion object {
        private val EXAMPLE_COUNTER: Preferences.Key<String>
                = preferencesKey<String>("example_counter")
    }

    private val preferenceDataStore: DataStore<Preferences> = context.createDataStore(
        name = "preferenceSettings"
    )

    fun getExampleTextFlow_preference(): Flow<String?> = preferenceDataStore.data
        .map { preferences ->
            preferences[EXAMPLE_COUNTER]
        }

    suspend fun save_preference(newValue: String) {
        preferenceDataStore.edit { settings ->
            settings[EXAMPLE_COUNTER] = newValue
        }
    }

    object SettingsSerializer : Serializer<Settings> {
        override fun readFrom(input: InputStream): Settings {
            try {
                return Settings.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override fun writeTo(
            t: Settings,
            output: OutputStream
        ) = t.writeTo(output)
    }

    private val protoDataStore: DataStore<Settings> = context.createDataStore(
        fileName = "settings.pb",
        serializer = SettingsSerializer
    )

    fun getExampleNumberFlow_proto(): Flow<Int> = protoDataStore.data
        .map { settings ->
            settings.exampleCounter
        }

    suspend fun save_proto(newValue: Int) {
        protoDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setExampleCounter(newValue)
                .build()
        }
    }
}
