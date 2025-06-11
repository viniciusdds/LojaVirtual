package br.com.aurora.lojavirtual.viewmodel

import PedidoRepository
import PedidoRequest
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.ItemCarrinho
import br.com.aurora.lojavirtual.model.PedidoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.model.PedidoCompleto
import br.com.aurora.lojavirtual.model.StatusPedido

class PedidoViewModel(private val repository: PedidoRepository) : ViewModel() {
    private val _resposta = MutableStateFlow<String?>(null)
    val resposta: StateFlow<String?> = _resposta.asStateFlow()

    private val _pedidos = MutableStateFlow<List<PedidoCompleto>>(emptyList())
    val pedidos = _pedidos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Adicione um StateFlow para controlar os pedidos selecionados
    private val _pedidosSelecionados = MutableStateFlow<Set<String>>(emptySet())
    val pedidosSelecionados: StateFlow<Set<String>> = _pedidosSelecionados.asStateFlow()

    // Adicione um StateFlow para o Snackbar
    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar = _showSnackbar.asStateFlow()

    // Função para alternar seleção do pedido
    fun toggleSelecaoPedido(codigoPedido: String) {
        _pedidosSelecionados.value = if (codigoPedido in _pedidosSelecionados.value) {
            _pedidosSelecionados.value - codigoPedido
        } else {
            _pedidosSelecionados.value + codigoPedido
        }
    }

    // Adicione esta função no seu PedidoViewModel
    fun getIdsSelecionadosParaNavegacao(pedidos: List<PedidoCompleto>): String {
        return pedidos.filter { _pedidosSelecionados.value.contains(it.codigo) }
            .joinToString(",") { pedido ->
                val itensStr = pedido.itens.joinToString(";") { it.produto }
                "${pedido.codigo}-${pedido.total}-$itensStr"
            }
    }

    fun confirmarPedido(idUsuario: String, itens: List<ItemCarrinho>, navController: NavController){
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
                    navController.navigate("pedidos/${idUsuario}")
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
                        itens = itens,
                        status = when(itens.first().status){
                            "A" -> StatusPedido.PENDENTE
                            "P" -> StatusPedido.PAGO
                            "E" -> StatusPedido.ENVIADO
                            "D" -> StatusPedido.ENTREGUE
                            "C" -> StatusPedido.CANCELADO
                            else -> StatusPedido.PENDENTE
                        }
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

    fun StatusPedido.corStatus(): Color {
        return when (this){
            StatusPedido.PENDENTE -> Color(0xFF8A2BE2)
            StatusPedido.PAGO -> Color.Blue
            StatusPedido.ENVIADO -> Color(0xFFFF8C00)
            StatusPedido.ENTREGUE -> Color.Green
            StatusPedido.CANCELADO -> Color.Red
        }
    }

    fun cancelarPedido(idPedido: String, idUsuario: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {

            try{
                val sucesso = repository.cancelarPedido(idPedido)
                if(sucesso){
                    onResult(true)
                }else{
                    onResult(false)
                }
            }catch (e: Exception){
                onResult(false)
            }
        }
    }

    fun finalizarPedido(idPedido: String, idUsuario: String){
        viewModelScope.launch {

            try{
                val sucesso = repository.finalizarPedido(idPedido)
                if(sucesso){
                   carregarPedidos(idUsuario)
                }
            }catch (e: Exception){
                _errorMessage.value = e.message
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
