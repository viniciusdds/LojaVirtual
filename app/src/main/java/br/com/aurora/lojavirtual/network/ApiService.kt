package br.com.aurora.lojavirtual.network

import PedidoRequest
import br.com.aurora.lojavirtual.model.ApiResponse
import br.com.aurora.lojavirtual.model.DadosCompra
import br.com.aurora.lojavirtual.model.ItemPedido
import br.com.aurora.lojavirtual.model.PedidoCompleto
import br.com.aurora.lojavirtual.model.PedidoItemResponse
import br.com.aurora.lojavirtual.model.PedidoResponse
import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.model.Usuario
import br.com.aurora.lojavirtual.model.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("cadastrar.php") // ajuste conforme o nome correto
    suspend fun cadastrarUsuario(@Body usuario: Usuario): Response<Void>

    @FormUrlEncoded
    @POST("login.php") // ajuste conforme o nome do seu endpoint PHP
    suspend fun loginUsuario(
        @Field("email") email: String,
        @Field("senha") senha: String
    ): UsuarioResponse

    @FormUrlEncoded
    @POST("enviar_link_redefinicao.php")
    suspend fun enviarLinkRedefinicao(
        @Field("email") email: String
    ): UsuarioResponse

    @GET("produtos.php")
    suspend fun getProdutos(@Query("categoria_id") categoriaId: Int?): List<Produto>

    @POST("salvar_pedido.php")
    suspend fun salvarPedido(@Body pedido: PedidoRequest): Response<Unit>

    @POST("confirmar_pedido.php")
    suspend fun confirmarPedido(@Body pedido: PedidoRequest): Response<PedidoResponse>

    @GET("buscar_pedidos.php")
    suspend fun buscarPedidosPorUsuario(@Query("id_usuario") idUsuario: String): List<ItemPedido>

    @FormUrlEncoded
    @POST("cancelar_pedido.php")
    suspend fun cancelarPedido(
        @Field("id_pedido") id: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("finalizar_pedido.php")
    suspend fun finalizarPedido(
        @Field("id_pedido") id: String
    ): Response<ApiResponse>

    @POST("dados_compra.php")
    suspend fun enviarDadosCompra(
        @Body dadosCompra: DadosCompra
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("efetuar_pagamento.php")
    suspend fun efetuarPagamento(
        @Field("id_pedido") idPedido: String
    ): Response<ApiResponse>
}