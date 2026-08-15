package com.processo.pagamento.demo.adapter.out.persistence.pagamento

import com.processo.pagamento.demo.domain.model.Pagamento


fun Pagamento.toEntity() =
    PagamentoEntity(
        idCliente = this.idUser,
        valor = this.valor,
        moeda = this.moeda,
        metodoPagamento = this.metodoPagamento,
        origem = this.origem,
        destino = this.destino
    )

fun PagamentoEntity.toDomain() =
    Pagamento(
        idPagamento = this.id?.toString(),
        idUser = this.idCliente,
        valor = this.valor,
        moeda = this.moeda,
        metodoPagamento = this.metodoPagamento,
        origem = this.origem,
        destino = this.destino
    )