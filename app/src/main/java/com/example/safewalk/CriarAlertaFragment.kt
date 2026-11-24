package com.example.safewalk

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.TipoAlerta
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.geo.Geocodificador
import com.example.safewalk.session.SessionManager
import com.example.safewalk.viewModel.AlertaViewModel
import com.example.safewalk.viewModel.factory.AlertaViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

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
    private lateinit var geocodificador: Geocodificador

    private lateinit var toggleLocal: MaterialButtonToggleGroup
    private lateinit var grupoEndereco: View
    private lateinit var etEndereco: EditText
    private lateinit var btnBuscarEndereco: Button
    private lateinit var tvLocalSelecionado: TextView

    private var latitudeSelecionada: Double? = null
    private var longitudeSelecionada: Double? = null
    private var cancelamentoGps: CancellationTokenSource? = null

    private val pedirPermissaoLocalizacao =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultado ->
            if (resultado.values.any { it }) {
                obterLocalizacaoAtual()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissão de localização negada. Informe o endereço do problema.",
                    Toast.LENGTH_LONG
                ).show()
                toggleLocal.check(R.id.btnLocalEndereco)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocodificador = Geocodificador(requireContext())

        val etTitulo = view.findViewById<EditText>(R.id.etTitulo)
        val etDescricao = view.findViewById<EditText>(R.id.etDescricao)
        val chipGroupTipo = view.findViewById<ChipGroup>(R.id.chipGroupTipo)
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvar)

        toggleLocal = view.findViewById(R.id.toggleLocal)
        grupoEndereco = view.findViewById(R.id.grupoEndereco)
        etEndereco = view.findViewById(R.id.etEndereco)
        btnBuscarEndereco = view.findViewById(R.id.btnBuscarEndereco)
        tvLocalSelecionado = view.findViewById(R.id.tvLocalSelecionado)

        toggleLocal.addOnButtonCheckedListener { _, botaoId, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            limparLocalSelecionado()
            if (botaoId == R.id.btnLocalAtual) usarLocalizacaoAtual() else usarEnderecoDigitado()
        }

        btnBuscarEndereco.setOnClickListener { buscarEndereco() }

        etEndereco.setOnEditorActionListener { _, acao, _ ->
            if (acao == EditorInfo.IME_ACTION_SEARCH) {
                buscarEndereco()
                true
            } else {
                false
            }
        }

        etEndereco.doAfterTextChanged {
            if (modoEndereco() && latitudeSelecionada != null) {
                limparLocalSelecionado()
                tvLocalSelecionado.text = "Toque em \"Buscar endereço\" para confirmar o local."
            }
        }

        usarLocalizacaoAtual()

        btnSalvar.setOnClickListener {

            val usuarioId = SessionManager.usuarioLogadoId ?: return@setOnClickListener
            val titulo = etTitulo.text.toString().trim()
            val lat = latitudeSelecionada
            val lng = longitudeSelecionada

            if (titulo.isEmpty()) {
                etTitulo.error = "Informe o que houve"
                return@setOnClickListener
            }

            if (lat == null || lng == null) {
                val mensagem = if (modoEndereco()) {
                    "Busque e confirme o endereço antes de publicar."
                } else {
                    "Ainda não foi possível obter sua localização."
                }
                Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alerta = AlertaInfraestrutura(
                titulo = titulo,
                descricao = etDescricao.text.toString().trim(),
                latitude = lat,
                longitude = lng,
                tipo = tipoSelecionado(chipGroupTipo.checkedChipId),
                usuarioId = usuarioId
            )

            viewModel.criarAlerta(alerta)
            findNavController().popBackStack()
        }
    }

    private fun modoEndereco(): Boolean = toggleLocal.checkedButtonId == R.id.btnLocalEndereco

    private fun tipoSelecionado(chipId: Int): TipoAlerta = when (chipId) {
        R.id.chipIluminacao -> TipoAlerta.ILUMINACAO
        R.id.chipAsfalto -> TipoAlerta.ASFALTO
        R.id.chipEsgoto -> TipoAlerta.ESGOTO
        R.id.chipVazamento -> TipoAlerta.VAZAMENTO
        else -> TipoAlerta.OUTRO
    }

    private fun limparLocalSelecionado() {
        latitudeSelecionada = null
        longitudeSelecionada = null
    }

    private fun usarLocalizacaoAtual() {
        grupoEndereco.isVisible = false

        if (temPermissaoLocalizacao()) {
            obterLocalizacaoAtual()
        } else {
            tvLocalSelecionado.text = "Aguardando permissão de localização..."
            pedirPermissaoLocalizacao.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun usarEnderecoDigitado() {
        cancelamentoGps?.cancel()
        grupoEndereco.isVisible = true
        tvLocalSelecionado.text = "Digite o endereço e toque em \"Buscar endereço\"."
        etEndereco.requestFocus()
    }

    private fun temPermissaoLocalizacao(): Boolean =
        listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .any {
                ContextCompat.checkSelfPermission(requireContext(), it) ==
                    PackageManager.PERMISSION_GRANTED
            }

    @SuppressLint("MissingPermission")
    private fun obterLocalizacaoAtual() {
        if (!temPermissaoLocalizacao()) return

        tvLocalSelecionado.text = "Obtendo sua localização..."

        cancelamentoGps?.cancel()
        val cancelamento = CancellationTokenSource()
        cancelamentoGps = cancelamento

        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelamento.token)
            .addOnSuccessListener { location ->
                if (view == null || modoEndereco()) return@addOnSuccessListener

                if (location == null) {
                    tvLocalSelecionado.text =
                        "Não foi possível obter sua localização. Ative o GPS ou digite o endereço."
                    return@addOnSuccessListener
                }

                latitudeSelecionada = location.latitude
                longitudeSelecionada = location.longitude
                tvLocalSelecionado.text = "Local: sua localização atual"

                mostrarEnderecoDaLocalizacao(location.latitude, location.longitude)
            }
            .addOnFailureListener {
                if (view == null || modoEndereco()) return@addOnFailureListener
                tvLocalSelecionado.text =
                    "Não foi possível obter sua localização. Ative o GPS ou digite o endereço."
            }
    }

    private fun mostrarEnderecoDaLocalizacao(latitude: Double, longitude: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            val endereco = geocodificador.buscarCoordenada(latitude, longitude)
            if (endereco != null && !modoEndereco() && latitudeSelecionada == latitude) {
                tvLocalSelecionado.text = "Local: $endereco"
            }
        }
    }

    private fun buscarEndereco() {
        val texto = etEndereco.text.toString().trim()
        if (texto.length < 5) {
            etEndereco.error = "Digite o endereço com rua e cidade"
            return
        }

        btnBuscarEndereco.isEnabled = false
        tvLocalSelecionado.text = "Buscando endereço..."

        viewLifecycleOwner.lifecycleScope.launch {
            val local = geocodificador.buscarEndereco(texto)
            btnBuscarEndereco.isEnabled = true

            if (!modoEndereco() || etEndereco.text.toString().trim() != texto) return@launch

            if (local == null) {
                limparLocalSelecionado()
                tvLocalSelecionado.text =
                    "Endereço não encontrado. Confira a rua, o número e a cidade."
                return@launch
            }

            latitudeSelecionada = local.latitude
            longitudeSelecionada = local.longitude
            tvLocalSelecionado.text = "Local: ${local.endereco}"
        }
    }

    override fun onDestroyView() {
        cancelamentoGps?.cancel()
        cancelamentoGps = null
        super.onDestroyView()
    }
}
