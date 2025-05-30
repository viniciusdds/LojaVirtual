package br.com.aurora.lojavirtual.model

data class ItemCarrinho(
    var id: Int,
    val nome: String,
    val quantidade: Int,
    val preco: Double
)