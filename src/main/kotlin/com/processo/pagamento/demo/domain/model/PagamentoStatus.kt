package com.processo.pagamento.demo.domain.model

import java.time.LocalDateTime

class PagamentoStatus(
    val idCliente: String,
    val idPagamento: String,
    val valor: String,
    val moeda: String,
    val metodoPagamento: String,
    val origem: String,
    val destino: String,
    val dataCriacao: LocalDateTime,
    val historicoStatus: MutableList<StatusPagamento>
)

class StatusPagamento(
    val id: String,
    val status: String,
    val mensagem: String,
    val dataCriacao: LocalDateTime
)