package com.ndozdev.deuche.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ndozdev.deuche.dormain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val android.content.Context.dataStore by preferencesDataStore(
    name = "userPref"
)
class DataStoreRepositoryImpl(
    private val context: Context
) : DataStoreRepository {
    override suspend fun putKeyValuePair(key: String, value: String) {
        val prefKey= stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }
    override fun getKeyValuePair(key: String): Flow<String?> {
        val prefKey= stringPreferencesKey(key)
        return context.dataStore.data.map { preferences -> preferences[prefKey] }
    }
}