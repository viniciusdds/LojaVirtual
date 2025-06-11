package br.com.aurora.lojavirtual.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.repository.PagamentoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PagamentoViewModel(private val repository: PagamentoRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _pagamentoEfetuado = MutableStateFlow(false)

    val errorMessage = _errorMessage.asStateFlow()
    val isLoading = _isLoading.asStateFlow()
    val pagamentoEfetuado = _pagamentoEfetuado.asStateFlow()

    fun efetuarPagamento(idPedido: String){
        viewModelScope.launch {

            try {
                val listaIds = idPedido.split("-")
                val resultados = listaIds.map { id ->
                    repository.efetuarPagamento(id)
                }

                if (resultados.all { it }) {
                    _pagamentoEfetuado.value = true
                } else {
                    _errorMessage.value = "Alguns pedidos não foram processados"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}