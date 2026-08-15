package com.processo.pagamento.demo.adapter.`in`.web

import com.processo.pagamento.demo.domain.model.Pagamento


fun PedidoPagamentoRequestDto.toDomain() =
    Pagamento(
        idUser = this.idUser,
        valor = this.valor,
        moeda = this.moeda,
        metodoPagamento = this.metodoPagamento,
        origem = this.origem,
        destino = this.destino
    )

fun Pagamento.toResponseDto() =
    PedidoPagamentoResponseDto(
        idPagamento = this.idPagamento ?: "",
        status = this.status.toString(),
        mensagem = "Pagamento criado com sucesso"
    )