package com.example.safewalk

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton // Adicionado
import com.google.android.material.floatingactionbutton.FloatingActionButton // Adicionado
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

        adapter = AlertaAdapter()

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAlertas)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter


        val btnLogout = view.findViewById<ImageButton>(R.id.btnLogout)

        val btnCriar = view.findViewById<FloatingActionButton>(R.id.btnCriar)

        val btnMapa = view.findViewById<Button>(R.id.btnMapa)


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