package com.example.safewalk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.TipoAlerta
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.MapViewModel
import com.example.safewalk.viewModel.factory.MapViewModelFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

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

        habilitarLocalizacaoUsuario()
        observarAlertas()
        configurarClickMarker()

        googleMap.setOnMarkerClickListener { marker ->

            val alerta = marker.tag as? AlertaInfraestrutura

            alerta?.let {
                abrirDetalheAlerta(it)
            }

            true
        }

    }

    private fun observarAlertas() {
        lifecycleScope.launch {
            viewModel.alertas.collect { lista ->

                googleMap.clear()

                lista.forEach { alerta ->

                    val posicao = LatLng(
                        alerta.latitude,
                        alerta.longitude
                    )

                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(posicao)
                            .title(alerta.titulo)
                            .snippet(alerta.descricao)
                    )

                    marker?.tag = alerta.id

                }
            }
        }
    }
    private fun habilitarLocalizacaoUsuario() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        googleMap.isMyLocationEnabled = true

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {

                val userLatLng = LatLng(it.latitude, it.longitude)

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)
                )
            }
        }
    }


    private fun getMarkerColor(tipo: TipoAlerta): Float {
        return when (tipo) {
            TipoAlerta.ASFALTO -> BitmapDescriptorFactory.HUE_RED
            TipoAlerta.ILUMINACAO -> BitmapDescriptorFactory.HUE_YELLOW
            TipoAlerta.ESGOTO -> BitmapDescriptorFactory.HUE_ORANGE
            TipoAlerta.OUTRO -> BitmapDescriptorFactory.HUE_CYAN
            TipoAlerta.VAZAMENTO -> BitmapDescriptorFactory.HUE_BLUE
        }
    }
    private fun abrirDetalheAlerta(alerta: AlertaInfraestrutura) {

        val bottomSheet = DetalheAlertaBottomSheet.newInstance(alerta)
        bottomSheet.show(childFragmentManager, "DetalheAlerta")
    }
    private fun configurarClickMarker() {

        googleMap.setOnMarkerClickListener { marker ->

            val alertaId = marker.tag as? Int ?: return@setOnMarkerClickListener true

            abrirDetalheAlerta(alertaId)

            true
        }
    }
    private fun abrirDetalheAlerta(alertaId: Int) {

        val sheet = AlertaDetalheBottomSheet(
            alertaId,
            viewModel
        )

        sheet.show(parentFragmentManager, "detalhe_alerta")
    }


}
