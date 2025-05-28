package br.com.aurora.lojavirtual.model

data class Produto(
    val id: Int,
    val nome: String,
    val imagemResId: Int,
    val preco: Double,
    val categoriaId: Int,
    var quantidade: Int = 0
)
