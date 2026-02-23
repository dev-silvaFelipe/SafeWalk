package com.example.safewalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.safewalk.viewModel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)

        btnConfirmar.setOnClickListener {
            viewModel.confirmarAlerta(alertaId)
            dismiss()
        }

        return view
    }
}

