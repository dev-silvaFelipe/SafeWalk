package com.example.safewalk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.MapViewModel
import com.example.safewalk.viewModel.factory.MapViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(R.layout.fragment_map),
    OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private val viewModel: MapViewModel by viewModels {
        MapViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        observarAlertas()
    }
    private fun observarAlertas() {
        lifecycleScope.launchWhenStarted {
            viewModel.alertas.collect { lista ->

                googleMap.clear()

                lista.forEach { alerta ->

                    val posicao = LatLng(
                        alerta.latitude,
                        alerta.longitude
                    )

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(posicao)
                            .title(alerta.titulo)
                            .snippet(alerta.descricao)
                    )
                }
            }
        }
    }

}