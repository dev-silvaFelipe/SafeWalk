package com.example.safewalk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.TipoAlerta
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.MapViewModel
import com.example.safewalk.viewModel.factory.MapViewModelFactory
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.File

class MapsFragment : Fragment(R.layout.fragment_map) {

    private var mapView: MapView? = null
    private var myLocationOverlay: MyLocationNewOverlay? = null

    private val viewModel: MapViewModel by viewModels {
        MapViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            )
        )
    }

    private val pedirPermissaoLocalizacao =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedida ->
            if (concedida) habilitarLocalizacaoUsuario()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarOsmdroid()

        val map = view.findViewById<MapView>(R.id.map)
        mapView = map

        map.setTileSource(fonteDoMapa())
        map.setMultiTouchControls(true)
        map.overlays.add(CopyrightOverlay(requireContext()))
        map.controller.setZoom(ZOOM_INICIAL)
        map.controller.setCenter(GeoPoint(LATITUDE_PADRAO, LONGITUDE_PADRAO))

        if (temPermissaoLocalizacao()) {
            habilitarLocalizacaoUsuario()
        } else {
            pedirPermissaoLocalizacao.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        observarAlertas()
    }

    private fun configurarOsmdroid() {
        val context = requireContext().applicationContext
        Configuration.getInstance().apply {
            userAgentValue = USER_AGENT
            osmdroidBasePath = File(context.filesDir, "osmdroid")
            osmdroidTileCache = File(context.cacheDir, "osmdroid/tiles")
        }
    }

    private fun fonteDoMapa(): OnlineTileSourceBase {
        val chave = BuildConfig.MAPTILER_KEY
        if (chave.isBlank()) return TileSourceFactory.MAPNIK

        return object : OnlineTileSourceBase(
            "MapTilerStreets",
            0,
            20,
            256,
            ".png",
            arrayOf("https://api.maptiler.com/maps/streets-v2/256/"),
            "© MapTiler © OpenStreetMap contributors"
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String =
                baseUrl +
                    MapTileIndex.getZoom(pMapTileIndex) + "/" +
                    MapTileIndex.getX(pMapTileIndex) + "/" +
                    MapTileIndex.getY(pMapTileIndex) + mImageFilenameEnding +
                    "?key=" + chave
        }
    }

    private fun temPermissaoLocalizacao(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun observarAlertas() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alertas.collect { lista -> desenharAlertas(lista) }
            }
        }
    }

    private fun desenharAlertas(lista: List<AlertaInfraestrutura>) {
        val map = mapView ?: return

        map.overlays.removeAll { it is Marker }

        lista.forEach { alerta ->
            val marker = Marker(map).apply {
                position = GeoPoint(alerta.latitude, alerta.longitude)
                title = alerta.titulo
                snippet = alerta.descricao
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = icon?.mutate()?.apply {
                    setTint(ContextCompat.getColor(requireContext(), corDoMarcador(alerta.tipo)))
                }
                setOnMarkerClickListener { _, _ ->
                    abrirDetalheAlerta(alerta.id)
                    true
                }
            }
            map.overlays.add(marker)
        }

        map.invalidate()
    }

    private fun habilitarLocalizacaoUsuario() {
        val map = mapView ?: return
        if (!temPermissaoLocalizacao()) return

        if (myLocationOverlay == null) {
            val overlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
            overlay.enableMyLocation()
            map.overlays.add(overlay)
            myLocationOverlay = overlay
        }

        LocationServices.getFusedLocationProviderClient(requireActivity())
            .lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    mapView?.controller?.setZoom(ZOOM_USUARIO)
                    mapView?.controller?.animateTo(GeoPoint(it.latitude, it.longitude))
                }
            }
    }

    @ColorRes
    private fun corDoMarcador(tipo: TipoAlerta): Int = when (tipo) {
        TipoAlerta.ASFALTO -> R.color.sw_marker_red
        TipoAlerta.ILUMINACAO -> R.color.sw_marker_yellow
        TipoAlerta.ESGOTO -> R.color.sw_marker_orange
        TipoAlerta.VAZAMENTO -> R.color.sw_marker_purple
        TipoAlerta.OUTRO -> R.color.sw_marker_cyan
    }

    private fun abrirDetalheAlerta(alertaId: Int) {
        AlertaDetalheBottomSheet(alertaId, viewModel)
            .show(parentFragmentManager, "detalhe_alerta")
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        myLocationOverlay?.enableMyLocation()
    }

    override fun onPause() {
        myLocationOverlay?.disableMyLocation()
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        myLocationOverlay?.disableMyLocation()
        myLocationOverlay = null
        mapView?.onDetach()
        mapView = null
        super.onDestroyView()
    }

    companion object {
        private const val LATITUDE_PADRAO = -8.0476
        private const val LONGITUDE_PADRAO = -34.8770
        private const val ZOOM_INICIAL = 12.0
        private const val ZOOM_USUARIO = 16.0
        private const val USER_AGENT = "SafeWalk/1.0 (+https://github.com/dev-silvaFelipe/SafeWalk)"
    }
}
