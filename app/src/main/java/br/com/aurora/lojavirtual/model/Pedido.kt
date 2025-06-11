package br.com.aurora.lojavirtual.model

enum class StatusPedido(val descricao: String) {
    PENDENTE("Pendente de pagamento"),
    PAGO("Pago"),
    ENVIADO("Enviado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado")
}

data class ItemPedido(
    val codigo: String,
    val produto: String,
    val quantidade: Int,
    val preco: Double,
    val dataPedido: String,
    val status: String? = null  // Adicionando o campo status
)

data class PedidoCompleto(
    val codigo: String,
    val dataPedido: String,
    val total: Double,
    val itens: List<ItemPedido>,
    val status: StatusPedido = StatusPedido.PENDENTE,
    var selecionado: Boolean = false
)

data class Pedidos(
    val codigo: String,  // Ex: PED_683dba0d58721
    val valor: Double,   // Ex: 13.8
    val descricao: String
)

data class PagamentoRequest(
    val pedidos: List<Pedidos>
)
