package com.processo.pagamento.demo.adapter.`in`.web

import com.processo.pagamento.demo.domain.model.Pagamento


fun PedidoPagamentoRequestDto.toPagamento() =
    Pagamento(
        idUser = this.idUser,
        valor = this.valor,
        moeda = this.moeda,
        metodoPagamento = this.metodoPagamento.name,
        origem = this.origem,
        destino = this.destino
    )