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
import com.example.safewalk.viewModel.UsuarioViewModel
import com.example.safewalk.viewModel.factory.UsuarioViewModelFactory

class CadastroFragment : Fragment(R.layout.fragment_cadastro) {

    private val viewModel: UsuarioViewModel by viewModels {
        UsuarioViewModelFactory(
            UsuarioRepository(
                AppDatabase.getInstance(requireContext()).usuarioDao()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nome = view.findViewById<EditText>(R.id.etNome)
        val telefone = view.findViewById<EditText>(R.id.etTelefone)
        val senha = view.findViewById<EditText>(R.id.etSenha)
        val btnCadastrar = view.findViewById<Button>(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            viewModel.cadastrarUsuario(
                nome.text.toString(),
                telefone.text.toString(),
                senha.text.toString()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Usuário cadastrado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().popBackStack()
            }
        }

    }
}
