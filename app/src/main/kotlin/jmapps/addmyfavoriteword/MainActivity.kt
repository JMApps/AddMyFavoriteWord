package jmapps.addmyfavoriteword

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties

    private var valNightMode: Boolean = false
    private lateinit var nightModeItem: MenuItem

    companion object {
        private const val KEY_NIGHT_MODE = "key_night_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedLocalPreferences = SharedLocalProperties(preferences)
        valNightMode = sharedLocalPreferences.getBooleanValue(KEY_NIGHT_MODE, false)
        isNightMode(valNightMode)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = findViewById(R.id.main_navigator_view)

        val navController = findNavController(R.id.navigator_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tasks, R.id.navigation_dictionary, R.id.navigation_notes
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        nightModeItem = menu.findItem(R.id.action_night_mode)
        nightModeItem.isChecked = valNightMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
          R.id.action_night_mode -> {
              getNightMode(!nightModeItem.isChecked)
          }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getNightMode(state: Boolean) {
        isNightMode(state)
        sharedLocalPreferences.saveBooleanValue("key_night_mode", state)
    }

    private fun isNightMode(state: Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}