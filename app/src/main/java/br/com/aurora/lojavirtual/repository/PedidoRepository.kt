import br.com.aurora.lojavirtual.model.ItemPedido
import br.com.aurora.lojavirtual.model.PedidoCompleto
import br.com.aurora.lojavirtual.model.PedidoItemResponse
import br.com.aurora.lojavirtual.network.ApiService

class PedidoRepository(private val api: ApiService) {

    suspend fun confirmarPedido(pedido: PedidoRequest) = api.confirmarPedido(pedido)

    suspend fun getPedidos(idUsuario: String): List<ItemPedido> {
        return api.buscarPedidosPorUsuario(idUsuario)
    }
}
