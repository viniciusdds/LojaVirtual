package br.com.aurora.lojavirtual.model

// Seria o mesmo que a API response
data class UsuarioResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val usuario: Usuario? = null
)
