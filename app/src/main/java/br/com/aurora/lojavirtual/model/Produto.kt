package br.com.aurora.lojavirtual.model

data class Produto(
    val id: Int,
    val nome: String,
    val imagemUrl: Int,
    val preco: Double,
    var quantidade: Int = 0
)