package br.com.aurora.lojavirtual.network

data class UsuarioCadastroRequest(
    val nome: String,
    val cpf: String,
    val email: String,
    val senha: String,
    val telefone: String
)
