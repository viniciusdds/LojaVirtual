package br.com.aurora.lojavirtual.repository

import br.com.aurora.lojavirtual.model.PagamentoRequest
import br.com.aurora.lojavirtual.model.Pedidos
import br.com.aurora.lojavirtual.network.ApiService

class PagamentoRepository(private val api: ApiService){

    suspend fun efetuarPagamento(pedidos: List<Pedidos>): Boolean {
        val request  = PagamentoRequest(pedidos)
        val response = api.efetuarPagamento(request)
        return response.isSuccessful && response.body()?.success == true
    }

}