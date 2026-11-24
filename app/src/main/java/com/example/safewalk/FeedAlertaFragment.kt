package com.example.safewalk

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.session.SessionManager
import com.example.safewalk.ui.adapter.AlertaAdapter
import com.example.safewalk.viewModel.AlertaViewModel
import com.example.safewalk.viewModel.factory.AlertaViewModelFactory
import kotlinx.coroutines.launch

/**
 * FeedAlertaFragment — Tela principal com a lista de alertas.
 *
 * Exibe a lista de chamados de infraestrutura urbana, adaptando a experiência
 * caso o usuário autenticado seja um Cidadão ou a equipe oficial da Prefeitura.
 */
class FeedAlertaFragment : Fragment(R.layout.fragment_feed_alertas) {

    private val viewModel: AlertaViewModel by viewModels {
        AlertaViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            ),
            SessionManager.usuarioLogadoId ?: 0,
            SessionManager.isPrefeitura
        )
    }

    private lateinit var adapter: AlertaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SessionManager.usuarioLogadoId == null) {
            findNavController().navigate(R.id.loginFragment)
            return
        }

        // Configuração da lista de alertas com ação de clique
        adapter = AlertaAdapter { alerta ->
            val detalheSheet = DetalheAlertaBottomSheet.newInstance(
                alerta = alerta,
                onConfirmar = { alertaId ->
                    viewModel.confirmarAlerta(alertaId)
                },
                onResolver = { alertaId ->
                    viewModel.encerrarAlerta(alertaId)
                }
            )
            detalheSheet.show(childFragmentManager, "DetalheAlertaBottomSheet")
        }

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAlertas)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // Identificação visual no header flutuante de acordo com o perfil
        val tvSubtitleHeader = view.findViewById<TextView>(R.id.tvSubtitleHeader)
        val ivHeaderLogo = view.findViewById<ImageView>(R.id.ivHeaderLogo)

        if (SessionManager.isPrefeitura) {
            tvSubtitleHeader?.text = "Prefeitura (Gestão Urbana)"
            ivHeaderLogo?.setImageResource(R.drawable.ic_governo)
        } else {
            tvSubtitleHeader?.text = "Alertas Comunitários"
            ivHeaderLogo?.setImageResource(R.drawable.ic_safewalk_logo)
        }

        // Botão de logout no header
        val btnLogout = view.findViewById<ImageButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            SessionManager.logout()
            findNavController().navigate(R.id.loginFragment)
        }

        // FAB de criar alerta
        val btnCriar = view.findViewById<FloatingActionButton>(R.id.btnCriar)
        btnCriar.setOnClickListener {
            findNavController().navigate(R.id.action_feedAlertasFragment_to_criarAlertaFragment)
        }

        // Aba "Mapa" na bottom navigation dock
        val btnMapa = view.findViewById<TextView>(R.id.btnMapa)
        btnMapa.setOnClickListener {
            findNavController().navigate(R.id.mapHeatFragment)
        }

        // Coleta reativa da lista de alertas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.alertas.collect {
                adapter.submitList(it)
            }
        }
    }
}
