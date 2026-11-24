package com.example.safewalk.geo

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.example.safewalk.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale
import kotlin.coroutines.resume

data class LocalEncontrado(
    val latitude: Double,
    val longitude: Double,
    val endereco: String
)

class Geocodificador(context: Context) {

    private val geocoder = Geocoder(context.applicationContext, Locale("pt", "BR"))

    suspend fun buscarEndereco(texto: String): LocalEncontrado? =
        buscarEnderecoAndroid(texto) ?: buscarEnderecoMapTiler(texto)

    suspend fun buscarCoordenada(latitude: Double, longitude: Double): String? =
        buscarCoordenadaAndroid(latitude, longitude) ?: buscarCoordenadaMapTiler(latitude, longitude)

    private suspend fun buscarEnderecoAndroid(texto: String): LocalEncontrado? {
        if (!Geocoder.isPresent()) return null
        val endereco = runCatching { enderecosPorNome(texto).firstOrNull() }.getOrNull()
            ?: return null
        return LocalEncontrado(endereco.latitude, endereco.longitude, formatar(endereco))
    }

    private suspend fun buscarCoordenadaAndroid(latitude: Double, longitude: Double): String? {
        if (!Geocoder.isPresent()) return null
        return runCatching { enderecosPorCoordenada(latitude, longitude).firstOrNull() }
            .getOrNull()
            ?.let { formatar(it) }
    }

    private suspend fun enderecosPorNome(texto: String): List<Address> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocationName(texto, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) = cont.resume(addresses)
                    override fun onError(errorMessage: String?) = cont.resume(emptyList())
                })
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                geocoder.getFromLocationName(texto, 1).orEmpty()
            }
        }

    private suspend fun enderecosPorCoordenada(latitude: Double, longitude: Double): List<Address> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) = cont.resume(addresses)
                    override fun onError(errorMessage: String?) = cont.resume(emptyList())
                })
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1).orEmpty()
            }
        }

    private fun formatar(endereco: Address): String =
        endereco.getAddressLine(0)
            ?: listOfNotNull(endereco.thoroughfare, endereco.subLocality, endereco.locality)
                .joinToString(", ")

    private suspend fun buscarEnderecoMapTiler(texto: String): LocalEncontrado? {
        val consulta = URLEncoder.encode(texto, "UTF-8").replace("+", "%20")
        val json = consultarMapTiler("$consulta.json?limit=1&country=br&language=pt") ?: return null
        val local = json.optJSONArray("features")?.optJSONObject(0) ?: return null
        val centro = local.optJSONArray("center") ?: return null
        return LocalEncontrado(
            latitude = centro.getDouble(1),
            longitude = centro.getDouble(0),
            endereco = local.optString("place_name", texto)
        )
    }

    private suspend fun buscarCoordenadaMapTiler(latitude: Double, longitude: Double): String? {
        val json = consultarMapTiler("$longitude,$latitude.json?limit=1&language=pt") ?: return null
        return json.optJSONArray("features")
            ?.optJSONObject(0)
            ?.optString("place_name")
            ?.takeIf { it.isNotBlank() }
    }

    private suspend fun consultarMapTiler(caminho: String): JSONObject? {
        val chave = BuildConfig.MAPTILER_KEY
        if (chave.isBlank()) return null

        return withContext(Dispatchers.IO) {
            runCatching {
                val url = URL("https://api.maptiler.com/geocoding/$caminho&key=$chave")
                val conexao = url.openConnection() as HttpURLConnection
                try {
                    conexao.connectTimeout = 10_000
                    conexao.readTimeout = 10_000
                    if (conexao.responseCode != HttpURLConnection.HTTP_OK) return@runCatching null
                    JSONObject(conexao.inputStream.bufferedReader().use { it.readText() })
                } finally {
                    conexao.disconnect()
                }
            }.getOrNull()
        }
    }
}
