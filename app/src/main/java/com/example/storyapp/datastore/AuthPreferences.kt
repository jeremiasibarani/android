package com.example.storyapp.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences (private val dataStore : DataStore<Preferences>){

    private val TOKEN_KEY = stringPreferencesKey("token_key")

    fun getToken() : Flow<String> {
        return dataStore.data.map {preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveToken(token : String){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }


    companion object{
        @Volatile
        private var INSTANCE : AuthPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) : AuthPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = AuthPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}