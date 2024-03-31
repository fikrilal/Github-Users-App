package com.fikrilal.githubuserapps.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import com.fikrilal.githubuserapps.databinding.ActivityMainBinding
import com.fikrilal.githubuserapps.util.SettingsPref
import com.fikrilal.githubuserapps.util.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()

        SettingsPref.getInstance(this.dataStore).getThemeSetting().asLiveData().observe(this) { isDarkTheme ->
            val mode = if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            Log.d("ThemeChange", "Night mode set to ${if (isDarkTheme) "Yes" else "No"}")
        }
    }
}