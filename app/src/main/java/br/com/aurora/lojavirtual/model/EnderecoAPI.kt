package br.com.aurora.lojavirtual.model

data class EnderecoAPI(
    val logradouro: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val cep: String
)

data class DadosCompra(
    val pedidosIds: List<String>,
    val email: String,
    val formaPagamento: String,
    val endereco: EnderecoCompleto
)

data class EnderecoCompleto(
    val logradouro: String,
    val numero: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val cep: String
)
