package com.processo.pagamento.demo.adapter.`in`.web

import com.processo.pagamento.demo.config.enum.MetodoPagamento

data class PedidoPagamentoRequestDto(
    val idUser: String,
    val valor: String,
    val moeda: String,
    val metodoPagamento: MetodoPagamento,
    val origem: String,
    val destino: String
)