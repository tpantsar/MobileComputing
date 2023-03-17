package com.codemave.mobilecomputing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.codemave.mobilecomputing.ui.login.SharedPreferences
import com.codemave.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correct username and password are stored in SharedPreferences class
        val credentials = SharedPreferences(this)
        credentials.username = "matti"
        credentials.password = "123"

        setContent {
            MobileComputingTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavigation(context = applicationContext)
                }
            }
        }
    }
}