import br.com.aurora.lojavirtual.network.ApiService

class PedidoRepository(private val api: ApiService) {
    suspend fun salvarPedido(pedido: PedidoRequest): Boolean {
        val response = api.salvarPedido(pedido)
        return response.isSuccessful
    }
}
