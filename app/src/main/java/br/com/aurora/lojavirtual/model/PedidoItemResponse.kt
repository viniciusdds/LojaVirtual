package br.com.aurora.lojavirtual.model

data class PedidoItemResponse(
    val codigo: String,
    val dataPedido: String,
    val produto: String,
    val quantidade: Int,
    val preco: Double
)
