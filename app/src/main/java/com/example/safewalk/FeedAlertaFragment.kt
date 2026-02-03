package com.example.safewalk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.ui.adapter.AlertaAdapter
import com.example.safewalk.viewModel.AlertaViewModel
import com.example.safewalk.viewModel.factory.AlertaViewModelFactory
import kotlinx.coroutines.launch

class FeedAlertaFragment : Fragment(R.layout.fragment_feed_alertas) {

    private val viewModel: AlertaViewModel by viewModels {
        AlertaViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            )
        )
    }

    private lateinit var adapter: AlertaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlertaAdapter()

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAlertas)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.alertas.collect {
                adapter.submitList(it)
            }
        }
    }
}

