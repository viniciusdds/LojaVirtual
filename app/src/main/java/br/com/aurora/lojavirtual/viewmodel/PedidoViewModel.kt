import androidx.lifecycle.ViewModel
import br.com.aurora.lojavirtual.model.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PedidoViewModel(private val repository: PedidoRepository) : ViewModel() {
    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

//    fun confirmarPedido(cpfUsuario: String, produtos: List<Produto>) {
//        viewModelScope.launch {
//            try{
//                val codigoPedido = "PED-${(1000..9999).random()}"
//                val total = produtos.sumOf { it.preco * it.quantidade }
//
//                val itens = produtos
//                    .filter { it.quantidade > 0 }
//                    .map { ItemPedidoRequest(it.id, it.quantidade) }
//
//                val pedido = PedidoRequest(
//                    codigo = codigoPedido,
//                    cpfUsuario = cpfUsuario,
//                    total = total,
//                    itens = itens
//                )
//
//                val response = repository.enviarPedido(pedido)
//                _status.value = if(response.sucesso) "Pedido confirmado!" else "Erro: ${response.mensagem}"
//            }catch (e: Exception){
//                _status.value = "Erro: ${e.message}"
//            }
//        }
//    }
}
