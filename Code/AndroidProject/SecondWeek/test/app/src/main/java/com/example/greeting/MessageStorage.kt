package com.example.greeting

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
data class Message(
    val id: Int,
    val text: String
)

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

object MessageStorage {
    private val KEY_MESSAGES = stringPreferencesKey("messages_json")

    // JSON 配置：忽略未知字段，避免以后字段变更导致崩
    private val json = Json { ignoreUnknownKeys = true }

    fun messagesFlow(context: Context): Flow<List<Message>> =
        context.dataStore.data.map { prefs ->
            val raw = prefs[KEY_MESSAGES] ?: return@map emptyList()
            runCatching { json.decodeFromString<List<Message>>(raw) }.getOrElse { emptyList() }
        }

    suspend fun save(context: Context, messages: List<Message>) {
        val raw = json.encodeToString(messages)
        context.dataStore.edit { prefs ->
            prefs[KEY_MESSAGES] = raw
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_MESSAGES)
        }
    }
}