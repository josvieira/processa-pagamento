package com.processo.pagamento.demo.adapter.`in`.web

data class PedidoPagamentoRequestDto(
    val idUser: String,
    val valor: String,
    val moeda: String,
    val metodoPagamento: String,
    val origem: String,
    val destino: String
)