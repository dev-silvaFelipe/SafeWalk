package com.example.safewalk.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.MapViewModel

class MapViewModelFactory(
    private val repository: AlertaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
