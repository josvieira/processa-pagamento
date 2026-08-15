package com.processo.pagamento.demo.adapter.out.mensaging

class PedidoPagamentoEvento(
    val idPagamento: String,
    val idUser: String,
    val valor: String,
    val moeda: String,
    val metodoPagamento: String,
    val origem: String,
    val destino: String,
    val status: String,
) {
}