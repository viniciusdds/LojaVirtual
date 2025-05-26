package br.com.aurora.lojavirtual.repository

import br.com.aurora.lojavirtual.model.Usuario
import br.com.aurora.lojavirtual.model.UsuarioResponse
import br.com.aurora.lojavirtual.network.ApiService
import retrofit2.Response

class UsuarioRepository(private val api: ApiService) {
    suspend fun cadastrar(usuario: Usuario): Response<Void> {
        return api.cadastrarUsuario(usuario)
    }

    suspend fun loginUsuario(email: String, senha: String): UsuarioResponse {
        return api.loginUsuario(email, senha)
    }

    suspend fun enviarLinkRedefinicao(email: String): UsuarioResponse{
        return api.enviarLinkRedefinicao(email)
    }
}
