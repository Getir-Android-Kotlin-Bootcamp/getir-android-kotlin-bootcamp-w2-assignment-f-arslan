package com.getir.patika.foodmap.data

/**
 * Interface for managing user preferences related to the greeting dialog state.
 */
interface PreferencesRepository {
    /**
     * Retrieves the current state indicating whether the greeting dialog should be shown.
     *
     * @return A [Boolean] value representing the state; `true` if the dialog should be shown,
     *         `false` otherwise.
     */
    suspend fun getGreetingDialogState(): Boolean

    /**
     * Persists the state indicating that the greeting dialog has been shown.
     */
    suspend fun setGreetingDialogState()
}
