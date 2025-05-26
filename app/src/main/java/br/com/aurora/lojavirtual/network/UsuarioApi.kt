package br.com.aurora.lojavirtual.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApi {
    @POST("cadastro.php")
    suspend fun cadastrarUsuario(@Body usuario: UsuarioCadastroRequest): Response<Void>
}
