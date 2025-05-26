package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    init {
        fetchProdutos()
    }

    private fun fetchProdutos() {
        viewModelScope.launch {
            try {
                _produtos.value = repository.getProdutos()
            } catch (e: Exception) {
                // tratamento de erro
            }
        }
    }
}