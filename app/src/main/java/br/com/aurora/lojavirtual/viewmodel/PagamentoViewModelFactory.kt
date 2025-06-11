package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.aurora.lojavirtual.repository.PagamentoRepository

class PagamentoViewModelFactory(private val repository: PagamentoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PagamentoViewModel(repository) as T
    }
}