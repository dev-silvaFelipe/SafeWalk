package com.example.safewalk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.safewalk.session.SessionManager

/**
 * MainActivity — Activity principal que hospeda o NavHostFragment.
 *
 * Utiliza edge-to-edge para que o tema dark mode se aplique de forma
 * consistente sobre as barras do sistema, garantindo a experiência
 * visual unificada em toda a interface.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
