package com.processo.pagamento.demo.adapter.`in`.web

import com.processo.pagamento.demo.domain.model.Pagamento


fun Pagamento.toResponseDto() =
    PedidoPagamentoResponseDto(
        idPagamento = this.idPagamento ?: "",
        status = this.status.toString(),
        mensagem = "Pagamento criado com sucesso"
    )