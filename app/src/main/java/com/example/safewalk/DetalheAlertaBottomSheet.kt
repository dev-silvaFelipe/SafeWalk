package com.example.safewalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetalheAlertaBottomSheet : BottomSheetDialogFragment() {

    private lateinit var alerta: AlertaInfraestrutura

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alerta = requireArguments().getSerializable("alerta") as AlertaInfraestrutura
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_detalhe_alerta, container, false)

        view.findViewById<TextView>(R.id.txtTitulo).text = alerta.titulo
        view.findViewById<TextView>(R.id.txtDescricao).text = alerta.descricao
        view.findViewById<TextView>(R.id.txtTipo).text = alerta.tipo.name
        view.findViewById<TextView>(R.id.txtConfirmacoes)
            .text = "Confirmações: ${alerta.confirmacoes}"
        view.findViewById<TextView>(R.id.txtStatus)
            .text = alerta.status.name

        return view
    }

    companion object {
        fun newInstance(alerta: AlertaInfraestrutura): DetalheAlertaBottomSheet {
            val fragment = DetalheAlertaBottomSheet()
            val args = Bundle()
            args.putSerializable("alerta", alerta)
            fragment.arguments = args
            return fragment
        }
    }
}
