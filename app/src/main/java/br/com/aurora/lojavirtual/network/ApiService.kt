package br.com.aurora.lojavirtual.network

import PedidoRequest
import br.com.aurora.lojavirtual.model.Produto
import br.com.aurora.lojavirtual.model.Usuario
import br.com.aurora.lojavirtual.model.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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

    @GET("produtos")
    suspend fun getProdutos(): List<Produto>

    @POST("salvar_pedido.php")
    suspend fun salvarPedido(@Body pedido: PedidoRequest): Response<Unit>
}
