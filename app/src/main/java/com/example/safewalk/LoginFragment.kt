package com.example.safewalk

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.safewalk.database.db.AppDatabase
import com.example.safewalk.database.repository.UsuarioRepository
import com.example.safewalk.session.SessionManager
import com.example.safewalk.viewModel.UsuarioViewModel
import com.example.safewalk.viewModel.factory.UsuarioViewModelFactory

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: UsuarioViewModel by viewModels {
        UsuarioViewModelFactory(
            UsuarioRepository(
                AppDatabase.getInstance(requireContext()).usuarioDao()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tel = view.findViewById<EditText>(R.id.etTelefone)
        val senha = view.findViewById<EditText>(R.id.etSenha)
        val btn = view.findViewById<Button>(R.id.btnLogin)

        btn.setOnClickListener {
            viewModel.login(
                tel.text.toString(),
                senha.text.toString()
            ) { usuario ->
                if (usuario != null) {
                    SessionManager.usuarioLogadoId = usuario.id
                    findNavController()
                        .navigate(R.id.action_loginFragment_to_feedAlertasFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Telefone ou senha inválidos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val btnCadastrar = view.findViewById<Button>(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            findNavController()
                .navigate(R.id.action_loginFragment_to_cadastroFragment)
        }


    }
}
