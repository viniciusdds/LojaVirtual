package br.com.aurora.lojavirtual.model

data class Produto(
    val id: Int,
    val produto: String,
    val imagemUrl: String,
    val preco: Double,
    val categoriaId: Int,
    var quantidade: Int = 0
)
