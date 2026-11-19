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

/**
 * LoginFragment — Tela de autenticação com suporte a Cidadão e Acesso Rápido para Prefeitura.
 */
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

        val etTelefone = view.findViewById<EditText>(R.id.etTelefone)
        val etSenha = view.findViewById<EditText>(R.id.etSenha)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnLoginPrefeitura = view.findViewById<Button>(R.id.btnLoginPrefeitura)
        val btnCadastrar = view.findViewById<Button>(R.id.btnCadastrar)

        // Login padrão (Cidadão ou credenciais digitadas)
        btnLogin.setOnClickListener {
            val tel = etTelefone.text.toString().trim()
            val pass = etSenha.text.toString().trim()

            if (tel.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, preencha telefone e senha.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(tel, pass) { usuario ->
                if (usuario != null) {
                    SessionManager.usuarioLogadoId = usuario.id
                    SessionManager.usuarioLogadoNome = usuario.nome
                    SessionManager.isPrefeitura = usuario.isPrefeitura

                    val mensagem = if (usuario.isPrefeitura) {
                        "Conectado como Prefeitura Municipal (Gestão Urbana)"
                    } else {
                        "Bem-vindo, ${usuario.nome}!"
                    }
                    Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.action_loginFragment_to_feedAlertasFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Telefone ou senha inválidos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Login Pré-selecionado: Acesso Oficial da Prefeitura
        btnLoginPrefeitura.setOnClickListener {
            viewModel.loginPrefeitura { prefeitura ->
                SessionManager.usuarioLogadoId = prefeitura.id
                SessionManager.usuarioLogadoNome = prefeitura.nome
                SessionManager.isPrefeitura = true

                Toast.makeText(
                    requireContext(),
                    "Conectado como Prefeitura Municipal (Gestão Urbana)",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_loginFragment_to_feedAlertasFragment)
            }
        }

        // Clique longo no botão da prefeitura para preencher os campos visíveis (opcional)
        btnLoginPrefeitura.setOnLongClickListener {
            etTelefone.setText("0800156")
            etSenha.setText("admin123")
            Toast.makeText(requireContext(), "Credenciais da Prefeitura preenchidas nos campos.", Toast.LENGTH_SHORT).show()
            true
        }

        // Navegação para tela de cadastro
        btnCadastrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_cadastroFragment)
        }
    }
}
