package br.com.aurora.lojavirtual.viewmodel

import DadosRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EnderecoViewModelFactory(
    private val repository: DadosRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnderecoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnderecoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}