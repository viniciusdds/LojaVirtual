package br.com.aurora.lojavirtual.model

data class ItemPedido(
    val codigo: String,
    val produto: String,
    val quantidade: Int,
    val preco: Double,
    val dataPedido: String
)

data class PedidoCompleto(
    val codigo: String,
    val dataPedido: String,
    val total: Double,
    val itens: List<ItemPedido>
)