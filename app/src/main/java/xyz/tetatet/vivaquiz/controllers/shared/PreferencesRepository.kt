package xyz.tetatet.vivaquiz.controllers.shared

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.tetatet.vivaquiz.BuildConfig

/**
 * A simple data repository for in-app settings.
 */
class PreferencesRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NIGHT_MODE = "prefs_night_mode"
        private const val PREFS_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_NO
        private const val PREFS_CATEGORY_NUMBER = "prefs_category_number"
        private const val PREFS_CATEGORY_NUMBER_DEF_VAL = 1

    }
    // ----------------- Theme Controller ------------------------------------------------------------>
    val nightMode: Int get() = sharedPreferences.getInt(PREFS_NIGHT_MODE, PREFS_NIGHT_MODE_DEF_VAL)
    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int> get() = _nightModeLive

    var isDarkTheme: Boolean = false
        get() = nightMode == AppCompatDelegate.MODE_NIGHT_YES
        set(value) {
            sharedPreferences.edit().putInt(
                PREFS_NIGHT_MODE, if (value) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            ).apply()
            field = value
        }
    private val _isDarkThemeLive: MutableLiveData<Boolean> = MutableLiveData()
    val isDarkThemeLive: LiveData<Boolean> get() = _isDarkThemeLive

    val categoryNumber: Int get() = sharedPreferences.getInt(PREFS_CATEGORY_NUMBER, PREFS_CATEGORY_NUMBER_DEF_VAL)
    private val _categoryNumberLive: MutableLiveData<Int> = MutableLiveData()
    val categoryNumberLive: LiveData<Int> get() = _categoryNumberLive
    var category: Int = PREFS_CATEGORY_NUMBER_DEF_VAL
        get() = categoryNumber
        set(value) {
            sharedPreferences.edit().putInt(PREFS_CATEGORY_NUMBER, value).apply()
            field = value
        }

    // -----------------  RequestsController ------------------------------------------------------------>

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFS_NIGHT_MODE -> {
                    _nightModeLive.value = nightMode
                    _isDarkThemeLive.value = isDarkTheme
                }
                PREFS_CATEGORY_NUMBER -> {
                    _categoryNumberLive.value = categoryNumber
                }
            }
        }
    //  Constructor
    init {
        // Init preference LiveData objects.
        _nightModeLive.value = nightMode
        _isDarkThemeLive.value = isDarkTheme
        _categoryNumberLive.value = categoryNumber
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }
}
