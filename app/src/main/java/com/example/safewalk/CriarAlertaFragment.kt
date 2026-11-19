package com.example.safewalk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

/**
 * CriarAlertaFragment — Tela para abertura de novos chamados de infraestrutura urbana.
 *
 * Oferece uma lista pré-selecionada de ocorrências categorizadas de acordo com
 * as atribuições de obras, iluminação e saneamento da prefeitura municipal,
 * permitindo ao mesmo tempo personalização do texto.
 */
class CriarAlertaFragment : Fragment(R.layout.fragment_alerta) {

    private val viewModel: AlertaViewModel by viewModels {
        AlertaViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            ),
            SessionManager.usuarioLogadoId ?: 0,
            SessionManager.isPrefeitura
        )
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    // Lista padronizada de ocorrências típicas de infraestrutura urbana municipal (frases claras e sem barras '/')
    private val problemasInfraestrutura = listOf(
        "Poste Apagado na Via Pública",
        "Falha na Iluminação Pública",
        "Lâmpada Acesa Durante o Dia",
        "Buraco no Asfalto",
        "Pavimentação Asfáltica Danificada",
        "Calçada Intransitável ou com Desnível",
        "Bueiro Entupido",
        "Boca de Lobo Obstruída",
        "Esgoto a Céu Aberto",
        "Vazamento de Água Potável na Via",
        "Árvore com Risco de Queda",
        "Necessidade de Poda Urgente",
        "Semáforo Apagado ou com Defeito",
        "Placa de Trânsito Danificada",
        "Outro Problema de Infraestrutura Urbana"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        verificarPermissaoLocalizacao()

        val actvTitulo = view.findViewById<AutoCompleteTextView>(R.id.actvTitulo)
        val etDescricao = view.findViewById<EditText>(R.id.etDescricao)
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvar)

        // Configuração do adaptador para a lista suspensa pré-selecionada
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_infraestrutura,
            problemasInfraestrutura
        )
        actvTitulo.setAdapter(adapter)

        // Pré-seleciona a ocorrência mais comum de infraestrutura municipal
        actvTitulo.setText(problemasInfraestrutura.first(), false)

        btnSalvar.setOnClickListener {
            val usuarioId = SessionManager.usuarioLogadoId ?: return@setOnClickListener
            val lat = currentLatitude
            val lng = currentLongitude

            if (lat == null || lng == null) {
                Toast.makeText(
                    requireContext(),
                    "Aguardando coordenadas de GPS... Certifique-se de que a localização está ativada.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val tituloDigitado = actvTitulo.text.toString().trim()
            val descricaoDigitada = etDescricao.text.toString().trim()

            if (tituloDigitado.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, selecione ou informe o que houve.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Identifica o TipoAlerta correspondente de acordo com o texto da prefeitura
            val tipoMapeado = mapearTipoAlerta(tituloDigitado)

            val alerta = AlertaInfraestrutura(
                titulo = tituloDigitado,
                descricao = if (descricaoDigitada.isNotEmpty()) descricaoDigitada else "Sem observações adicionais.",
                latitude = lat,
                longitude = lng,
                tipo = tipoMapeado,
                usuarioId = usuarioId
            )

            viewModel.criarAlerta(alerta)
            Toast.makeText(requireContext(), "Chamado registrado no mapa com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    /**
     * Mapeia automaticamente a ocorrência selecionada para a categoria funcional do mapa.
     */
    private fun mapearTipoAlerta(opcao: String): TipoAlerta {
        val texto = opcao.lowercase()
        return when {
            texto.contains("iluminação") || texto.contains("poste") || texto.contains("lâmpada") -> TipoAlerta.ILUMINACAO
            texto.contains("asfalto") || texto.contains("buraco") || texto.contains("calçada") || texto.contains("pavimentação") -> TipoAlerta.ASFALTO
            texto.contains("esgoto") || texto.contains("bueiro") || texto.contains("boca de lobo") -> TipoAlerta.ESGOTO
            texto.contains("vazamento") || texto.contains("água") -> TipoAlerta.VAZAMENTO
            else -> TipoAlerta.OUTRO
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

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLatitude = location.latitude
                currentLongitude = location.longitude
            } else {
                // Posição de fallback para ambiente de desenvolvimento/emulador caso o GPS não retorne
                currentLatitude = -8.0476
                currentLongitude = -34.8770
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
            Toast.makeText(requireContext(), "Permissão de localização não concedida.", Toast.LENGTH_SHORT).show()
        }
    }
}
