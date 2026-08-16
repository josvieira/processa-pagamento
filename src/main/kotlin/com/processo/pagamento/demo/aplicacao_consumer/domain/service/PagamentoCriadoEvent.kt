package com.processo.pagamento.demo.aplicacao_consumer.domain.service

import com.processo.pagamento.demo.config.enum.MetodoPagamento
import java.util.UUID

data class PagamentoCriadoEvent(
    val id: UUID,
    val idUser: String,
    val valor: Double,
    val metodoPagamento: MetodoPagamento,
    val status: String
)

data class ResultadoAutorizacao(
    val aprovado: Boolean,
    val motivo: String
)