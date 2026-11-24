package com.example.safewalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.safewalk.session.SessionManager
import com.example.safewalk.viewModel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

/**
 * BottomSheet para interação com o alerta selecionado.
 *
 * Implementa controle de acesso baseado em papéis (RBAC):
 * - Cidadãos podem reconfirmar a persistência do problema (validação comunitária).
 * - Apenas a equipe oficial da Prefeitura possui autorização para marcar incidentes como RESOLVIDOS.
 */
class AlertaDetalheBottomSheet(
    private val alertaId: Int,
    private val viewModel: MapViewModel
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.bottomsheet_alerta,
            container,
            false
        )

        val ivHeaderIcon = view.findViewById<ImageView>(R.id.ivHeaderIcon)
        val tvPergunta = view.findViewById<TextView>(R.id.tvPergunta)
        val tvSubtitulo = view.findViewById<TextView>(R.id.tvSubtitulo)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)
        val btnResolvido = view.findViewById<MaterialButton>(R.id.btnResolvido)

        if (SessionManager.isPrefeitura) {
            // Perfil oficial da Prefeitura
            ivHeaderIcon.setImageResource(R.drawable.ic_governo)
            ivHeaderIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.sw_accent))

            tvPergunta.text = "Gestão de Infraestrutura Urbana"
            tvSubtitulo.text = "Como órgão oficial, você tem o poder de atestar o reparo e encerrar este chamado no mapa."

            btnConfirmar.text = "Reforçar Prioridade"
            btnConfirmar.setOnClickListener {
                viewModel.confirmarAlerta(alertaId)
                Toast.makeText(requireContext(), "Prioridade do chamado reforçada.", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            // Destaque para o botão de resolução oficial (exclusivo para Prefeitura)
            btnResolvido.visibility = View.VISIBLE
            btnResolvido.text = "Oficializar como Resolvido"
            btnResolvido.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.sw_accent)
            btnResolvido.setTextColor(ContextCompat.getColor(requireContext(), R.color.sw_accent))
            btnResolvido.iconTint = ContextCompat.getColorStateList(requireContext(), R.color.sw_accent)

            btnResolvido.setOnClickListener {
                viewModel.encerrarAlerta(alertaId)
                Toast.makeText(
                    requireContext(),
                    "Incidente marcado como RESOLVIDO pela Prefeitura!",
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }
        } else {
            // Perfil Cidadão Comum
            ivHeaderIcon.setImageResource(R.drawable.ic_info)
            tvPergunta.text = "O problema ainda persiste?"
            tvSubtitulo.text = "Sua confirmação comunitária atualiza a relevância do alerta e ajuda outros pedestres."

            btnConfirmar.text = "Sim, ainda existe"
            btnConfirmar.setOnClickListener {
                viewModel.confirmarAlerta(alertaId)
                Toast.makeText(requireContext(), "Confirmação comunitária registrada!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            // O botão de marcar como resolvido é exclusivo da prefeitura e fica oculto para o cidadão
            btnResolvido.visibility = View.GONE
        }

        return view
    }
}
