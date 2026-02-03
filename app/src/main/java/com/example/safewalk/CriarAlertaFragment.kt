package com.example.safewalk

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.TipoAlerta
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.AlertaViewModel
import com.example.safewalk.viewModel.factory.AlertaViewModelFactory

class CriarAlertaFragment : Fragment(R.layout.fragment_alerta) {

    private val viewModel: AlertaViewModel by viewModels {
        AlertaViewModelFactory(
            AlertaRepository(
                AppDatabase.getInstance(requireContext()).alertaDao()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitulo = view.findViewById<EditText>(R.id.etTitulo)
        val etDescricao = view.findViewById<EditText>(R.id.etDescricao)
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvar)

        btnSalvar.setOnClickListener {
            val alerta = AlertaInfraestrutura(
                titulo = etTitulo.text.toString(),
                descricao = etDescricao.text.toString(),
                latitude = 0.0,
                longitude = 0.0,
                tipo = TipoAlerta.OUTRO,
                usuarioId = 1
            )
            viewModel.criarAlerta(alerta)
        }
    }
}
