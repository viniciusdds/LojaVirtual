data class PedidoRequest(
    val codigo: String,
    val cpfUsuario: String,
    val total: Double,
    val itens: List<ItemPedidoRequest>
)

data class ItemPedidoRequest(
    val idProduto: Int,
    val quantidade: Int
)