package com.example.safewalk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.TipoAlerta
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.session.SessionManager
import com.example.safewalk.viewModel.AlertaViewModel
import com.example.safewalk.viewModel.factory.AlertaViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CriarAlertaFragment : Fragment(R.layout.fragment_alerta) {

    private val viewModel: AlertaViewModel by viewModels {
        AlertaViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            ),
            SessionManager.usuarioLogadoId!!
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitudeAtual: Double? = null
    private var longitudeAtual: Double? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        verificarPermissaoLocalizacao()

        val etTitulo = view.findViewById<EditText>(R.id.etTitulo)
        val etDescricao = view.findViewById<EditText>(R.id.etDescricao)
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvar)

        btnSalvar.setOnClickListener {

            val usuarioId = SessionManager.usuarioLogadoId ?: return@setOnClickListener
            val lat = latitudeAtual
            val lng = longitudeAtual

            if (lat == null || lng == null) {
                Toast.makeText(requireContext(), "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val alerta = AlertaInfraestrutura(
                titulo = etTitulo.text.toString(),
                descricao = etDescricao.text.toString(),
                latitude = lat,
                longitude = lng,
                tipo = TipoAlerta.OUTRO,
                usuarioId = usuarioId
            )

            viewModel.criarAlerta(alerta)
            findNavController().popBackStack()
        }
    }

    private fun verificarPermissaoLocalizacao() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                100
            )
        } else {
            obterLocalizacao()
        }
    }

    private fun obterLocalizacao() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    latitudeAtual = location.latitude
                    longitudeAtual = location.longitude
                } else {
                    Toast.makeText(requireContext(), "Não foi possível obter a localização. Ative a localização do seu dispositivo.", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            obterLocalizacao()
        } else {
            Toast.makeText(requireContext(), "Permissão de localização negada.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}