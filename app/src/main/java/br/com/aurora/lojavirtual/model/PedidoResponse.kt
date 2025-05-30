package br.com.aurora.lojavirtual.model

data class PedidoResponse(
    val success: Boolean,
    val mensagem: String,
    val codigoPedido: String? = null
)