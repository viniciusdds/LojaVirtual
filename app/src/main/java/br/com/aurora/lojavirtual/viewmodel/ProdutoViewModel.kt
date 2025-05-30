package br.com.aurora.lojavirtual.viewmodel

import PedidoRequest
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.aurora.lojavirtual.R
import br.com.aurora.lojavirtual.model.ItemCarrinho
import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _produtos = mutableStateListOf<Produto>()
    val produtos: List<Produto> get() = _produtos

    private val _resposta = MutableStateFlow<String?>(null)
    val resposta: StateFlow<String?> = _resposta

//    Para pegar a lista da API
//    fun carregarProdutos(categoriaId: Int?) {
//        viewModelScope.launch {
//            val produtosDaApi = repository.buscarProdutos(categoriaId)
//            _produtos.clear()
//            _produtos.addAll(produtosDaApi)
//        }
//    }

    fun carregarProdutos(categoriaId: Int?){
        _produtos.clear()
        val lista = listOf(
            Produto(1, "Camisa Polo", R.drawable.produto1, 59.90, categoriaId = 2),
            Produto(2, "Tênis Esportivo", R.drawable.produto2, 189.90, categoriaId = 2),
            Produto(3, "Calça Jeans", R.drawable.produto3, 129.90, categoriaId = 2),
            Produto(4, "Boné Estiloso", R.drawable.produto4, 49.90, categoriaId = 2),
            Produto(5, "Celular", R.drawable.produto5, 139.90, categoriaId = 1),
            Produto(6, "NoteBook", R.drawable.produto6, 3500.90, categoriaId = 1),
            Produto(7, "Harry Potter", R.drawable.produto7, 300.90, categoriaId = 3),
            Produto(8, "Educativo", R.drawable.produto8, 100.00, categoriaId = 3),
            Produto(9, "Bolacha Trakinas", R.drawable.produto9, 6.90, categoriaId = 4)
        )

        val produtosFiltrados = if(categoriaId == null || categoriaId == 0){
            lista
        }else{
            lista.filter { it.categoriaId == categoriaId }
        }
        _produtos.addAll(produtosFiltrados)
    }

    fun confirmarPedido(): List<ItemCarrinho>{
        val itensCarrinho = produtos.filter {
            it.quantidade > 0
        }.map { produto ->
            ItemCarrinho(
                id = produto.id,
                nome = produto.nome,
                quantidade = produto.quantidade,
                preco = produto.quantidade * produto.preco // preco total de cada item
            )
        }
        produtos.forEach { it.quantidade = 0 }
        return itensCarrinho
    }

    fun adicionarQuantidade(produto: Produto){
        val index = _produtos.indexOf(produto)
        if(index != -1){
            _produtos[index] = _produtos[index].copy(quantidade = _produtos[index].quantidade + 1)
        }
    }

    fun removerQuantidade(produto: Produto){
        val index = _produtos.indexOf(produto)
        if(index != -1 && _produtos[index].quantidade > 0){
            _produtos[index] = _produtos[index].copy(quantidade = _produtos[index].quantidade - 1)
        }
    }

    fun quantidadeTotal(): Int = produtos.sumOf { it.quantidade }

}