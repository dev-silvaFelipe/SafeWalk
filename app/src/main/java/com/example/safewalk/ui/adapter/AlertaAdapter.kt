package com.example.safewalk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safewalk.R
import com.example.safewalk.database.db.entity.AlertaInfraestrutura

class AlertaAdapter (
    private var alertas: List<AlertaInfraestrutura> = emptyList()
) : RecyclerView.Adapter<AlertaAdapter.AlertaViewHolder>(){
    fun submitList(novaLista: List<AlertaInfraestrutura>){
        alertas = novaLista
        notifyDataSetChanged()
    }
    
    class AlertaViewHolder(view: View): RecyclerView.ViewHolder(view){
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descricao: TextView = view.findViewById(R.id.tvDescricao)
        val data: TextView = view.findViewById(R.id.tvData)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertaViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta, parent, false)
        return AlertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertaViewHolder, position: Int){
        val alerta = alertas[position]
        holder.titulo.text = alerta.titulo
        holder.descricao.text = alerta.descricao
        holder.data.text = "Confirmados: ${alerta.confirmacoes}"
    }
    override fun getItemCount() = alertas.size
    
}