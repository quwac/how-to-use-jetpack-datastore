package io.github.quwac.how_to_use_jetpack_datastore.ui.main

import android.content.Context
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.example.application.Settings
import com.squareup.moshi.Moshi
import com.squareup.wire.WireJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.buffer
import okio.sink
import okio.source
import java.io.InputStream
import java.io.OutputStream

class Repository(context: Context) {
    companion object {
        private val EXAMPLE_COUNTER: Preferences.Key<String> =
            preferencesKey<String>("example_counter")
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
    private val moshi = Moshi.Builder()
        .add(WireJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(Settings::class.java)

    override val defaultValue: Settings
        get() = Settings()

    override fun readFrom(input: InputStream): Settings {
        return try {
            input.source().buffer().use { source ->
                adapter.fromJson(source)!!
            }
        } catch (t: Throwable) {
            // failure
            Log.w("readFrom", "readFrom failed.", t)
            Settings()
        }
    }

    override fun writeTo(
        t: Settings,
        output: OutputStream
    ) {
        Log.e("writeTo", "writeTo invoked.")
        try {
            output.sink().buffer().use { sink ->
                adapter.toJson(sink, t)
            }
        } catch (t: Throwable) {
            // failure
            Log.e("writeTo", "writeTo failed.", t)
        }
    }
}

    private val protoDataStore: DataStore<Settings> = context.createDataStore(
        fileName = "settings.pb.json",
        serializer = SettingsSerializer
    )

    fun getExampleNumberFlow_proto(): Flow<Int> = protoDataStore.data
        .map { settings ->
            settings.example_counter
        }

    suspend fun save_proto(newValue: Int) {
        protoDataStore.updateData { currentSettings ->
            currentSettings.copy(example_counter = newValue)
        }
    }
}
