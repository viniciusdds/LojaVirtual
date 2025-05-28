package br.com.aurora.lojavirtual.model

import androidx.compose.runtime.mutableStateListOf

data class Pedido(
    val codigo: String,
    val produtos: List<Produto>,
    val total: Double
)

object PedidoRepository{
    val pedidos = mutableStateListOf<Pedido>()

    fun adicionarPedido(produtosSelecionados: List<Produto>){
        val produtosFiltrados = produtosSelecionados.filter { it.quantidade > 0 }
        if(produtosFiltrados.isNotEmpty()){
            val codigo = "PED-${System.currentTimeMillis()}" // código único baseado no tempo
            val total = produtosFiltrados.sumOf { it.preco * it.quantidade }
            pedidos.add(Pedido(codigo, produtosFiltrados, total))
        }
    }
}