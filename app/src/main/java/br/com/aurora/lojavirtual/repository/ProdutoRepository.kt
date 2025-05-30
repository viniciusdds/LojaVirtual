package br.com.aurora.lojavirtual.repository

import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.network.ApiService

class ProdutoRepository(private val api: ApiService) {
    suspend fun getProdutos(categoriaId: Int?): List<Produto> {
        return api.getProdutos(categoriaId)
    }
}