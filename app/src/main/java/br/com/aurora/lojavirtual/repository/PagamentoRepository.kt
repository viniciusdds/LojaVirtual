package br.com.aurora.lojavirtual.repository

import br.com.aurora.lojavirtual.network.ApiService

class PagamentoRepository(private val api: ApiService){

    suspend fun efetuarPagamento(idPedido: String): Boolean {
        val response = api.efetuarPagamento(idPedido)
        return response.isSuccessful && response.body()?.success == true
    }

}