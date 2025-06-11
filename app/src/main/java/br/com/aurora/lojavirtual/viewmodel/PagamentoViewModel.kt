package br.com.aurora.lojavirtual.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.Pedidos
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
            _isLoading.value = true
            try {
                val pedidosList = idPedido.split(",").mapNotNull { pedidoRaw ->
                    val partes = pedidoRaw.split("-")
                    if (partes.size >= 3) {
                        val id = partes[0]
                        val valor = partes[1].toDoubleOrNull()
                        val descricao = partes.subList(2, partes.size).joinToString("-") // caso a descrição contenha "-"

                        if (valor != null) {
                            Pedidos(codigo = id, valor = valor, descricao = descricao)
                        } else null
                    } else {
                        null
                    }
                }

                val sucesso = repository.efetuarPagamento(pedidosList)

                if (sucesso) {
                    _pagamentoEfetuado.value = true
                    Log.d("Pagamento", "IDs processados: $pedidosList")
                } else {
                    _errorMessage.value = "Alguns pedidos não foram processados"
                    Log.d("Pagamento", "Erro: ${_errorMessage.value}")
                }

            } catch (e: Exception) {
                _errorMessage.value = "Erro: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}