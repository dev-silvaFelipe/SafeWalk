package com.example.safewalk

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
 * Utiliza o layout refinado com header flutuante escuro,
 * bottom navigation dock e FAB amarelo vibrante.
 */
class FeedAlertaFragment : Fragment(R.layout.fragment_feed_alertas) {

    private val viewModel: AlertaViewModel by viewModels {
        AlertaViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            ),
            SessionManager.usuarioLogadoId!!
        )
    }

    private lateinit var adapter: AlertaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SessionManager.usuarioLogadoId == null) {
            findNavController().navigate(R.id.loginFragment)
            return
        }

        adapter = AlertaAdapter { alerta ->
            val detalheSheet = DetalheAlertaBottomSheet.newInstance(alerta) { alertaId ->
                viewModel.confirmarAlerta(alertaId)
            }
            detalheSheet.show(childFragmentManager, "DetalheAlertaBottomSheet")
        }

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAlertas)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // Botão de logout no header flutuante
        val btnLogout = view.findViewById<ImageButton>(R.id.btnLogout)

        // FAB de criar alerta
        val btnCriar = view.findViewById<FloatingActionButton>(R.id.btnCriar)

        // Aba "Mapa" na bottom navigation dock
        // O btnMapa agora é um TextView dentro da tab, mas o ID é mantido
        val btnMapa = view.findViewById<TextView>(R.id.btnMapa)

        btnCriar.setOnClickListener {
            findNavController()
                .navigate(R.id.action_feedAlertasFragment_to_criarAlertaFragment)
        }

        btnLogout.setOnClickListener {
            SessionManager.usuarioLogadoId = null
            findNavController().navigate(R.id.loginFragment)
        }

        btnMapa.setOnClickListener {
            findNavController().navigate(R.id.mapHeatFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.alertas.collect {
                adapter.submitList(it)
            }
        }
    }
}
