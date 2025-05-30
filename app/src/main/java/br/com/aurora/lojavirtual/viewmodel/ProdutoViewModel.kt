package br.com.aurora.lojavirtual.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aurora.lojavirtual.model.ItemCarrinho
import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _produtos = mutableStateListOf<Produto>()
    val produtos: List<Produto> get() = _produtos

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    private val _resposta = MutableStateFlow<String?>(null)
    val resposta: StateFlow<String?> = _resposta

    // Para pegar a lista da API
    fun carregarProdutos(categoriaId: Int?){
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try{
                val bruta = repository.getProdutos(categoriaId)
                Log.d("ProdutosBrutos", "Recebido: ${bruta.size} itens")

                val lista = repository.getProdutos(categoriaId)

                Log.d("ListaP", "ID do usuário: $lista - $categoriaId")

                _produtos.clear()
                _produtos.addAll(lista)

            }catch (e: Exception){
                Log.e("ProdutoViewModel", "Erro ao carregar produtos", e)
                errorMessage = "Erro ao carregar produtos: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    fun confirmarPedido(): List<ItemCarrinho>{
        val itensCarrinho = produtos.filter {
            it.quantidade > 0
        }.map { produto ->
            ItemCarrinho(
                id = produto.id,
                nome = produto.produto,
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