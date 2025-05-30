package br.com.aurora.lojavirtual.viewmodel

import PedidoRepository
import PedidoRequest
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.ItemCarrinho
import br.com.aurora.lojavirtual.model.PedidoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import br.com.aurora.lojavirtual.model.ItemPedido
import br.com.aurora.lojavirtual.model.PedidoCompleto
import br.com.aurora.lojavirtual.model.PedidoItemResponse

class PedidoViewModel(private val repository: PedidoRepository) : ViewModel() {
    private val _resposta = MutableStateFlow<String?>(null)
    val resposta: StateFlow<String?> = _resposta.asStateFlow()

    private val _pedidos = MutableStateFlow<List<PedidoCompleto>>(emptyList())
    val pedidos = _pedidos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun confirmarPedido(idUsuario: String, itens: List<ItemCarrinho>){
        viewModelScope.launch {
            try{
                val pedido = PedidoRequest(
                    id_usuario = idUsuario,
                    itens = itens
                )

                val response = repository.confirmarPedido(pedido)
                if(response.isSuccessful){
                    _resposta.value = response.body()?.mensagem ?: "Pedido confirmado"
                    Log.d("Pedido", "Código do pedido: ${response.body()?.codigoPedido}")
                    Log.d("Debug", "ID do usuário: $pedido")
                }else{
                    _resposta.value = "Erro ${response.code()}"
                }
            }catch (e: Exception){
                _resposta.value = "Erro ${e.message}"
            }
        }
    }

    fun carregarPedidos(idUsuario: String){
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val lista = repository.getPedidos(idUsuario)

                // Agrupar por código do pedido
                val agrupado = lista.groupBy { it.codigo }.map { (codigo, itens) ->
                    val dataPedido = itens.first().dataPedido
                    val total = itens.sumOf { it.preco }

                    PedidoCompleto(
                        codigo = codigo,
                        dataPedido = dataPedido,
                        total = total,
                        itens = itens
                    )
                }

                _pedidos.value = agrupado

                Log.d("Lista", "ID do usuário: $pedidos - $idUsuario")
            }catch (e: Exception){
                _errorMessage.value = e.message
            }finally {
                _isLoading.value = false
            }
        }
    }
}

// Estados do pedido
sealed class PedidoState {
    object Idle : PedidoState()
    object Loading : PedidoState()
    data class Success(val response: PedidoResponse?) : PedidoState()
    data class Error(val exception: Exception) : PedidoState()
}
