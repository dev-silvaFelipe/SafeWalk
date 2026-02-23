package com.example.safewalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.safewalk.session.SessionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()

        if (SessionManager.usuarioLogadoId != null) {
            val navController = findNavController(R.id.nav_host_fragment)

            navController.navigate(R.id.feedAlertasFragment)
        }
    }
}
