package com.getir.patika.foodmap.data

interface PreferencesRepository {
    suspend fun getGreetingDialogState(): Boolean
    suspend fun setGreetingDialogState()
}
