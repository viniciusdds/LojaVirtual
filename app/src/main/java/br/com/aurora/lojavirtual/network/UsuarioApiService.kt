package br.com.aurora.lojavirtual.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val email: String, val senha: String)
data class LoginResponse(val sucesso: Boolean, val mensagem: String, val usuarioId: Int?)

interface UsuarioApiService {
    @POST("login.php") // ajuste conforme o nome do seu endpoint PHP
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
