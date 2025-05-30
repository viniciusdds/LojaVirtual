package com.seuapp.viewmodel

import PedidoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.aurora.lojavirtual.viewmodel.PedidoViewModel

class PedidoViewModelFactory(private val repository: PedidoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PedidoViewModel(repository) as T
    }
}
