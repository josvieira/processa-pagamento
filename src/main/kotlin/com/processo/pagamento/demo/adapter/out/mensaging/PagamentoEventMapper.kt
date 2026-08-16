package com.processo.pagamento.demo.adapter.out.mensaging

import com.processo.pagamento.demo.aplicacao_consumer.domain.service.PagamentoCriadoEvent
import com.processo.pagamento.demo.config.enum.MetodoPagamento
import com.processo.pagamento.demo.domain.model.Pagamento
import java.util.UUID

fun Pagamento.toPagamentoCriadoEvent(): PagamentoCriadoEvent {
    return PagamentoCriadoEvent(
        id = UUID.fromString(this.idPagamento!!),
        idUser = this.idUser,
        valor = this.valor.toDouble(),
        metodoPagamento = MetodoPagamento.valueOf(this.metodoPagamento),
        status = this.status ?: "CREATED"
    )
}