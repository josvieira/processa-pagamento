package com.processo.pagamento.demo.adapter.out.mensaging

import com.processo.pagamento.demo.domain.model.Pagamento

fun Pagamento.toPedidoPagamentoEvento(): PedidoPagamentoEvento {
    return PedidoPagamentoEvento(
        idPagamento = this.idPagamento.toString(),
        idUser = this.idUser,
        valor = this.valor,
        moeda = this.moeda,
        metodoPagamento = this.metodoPagamento,
        origem = this.origem,
        destino = this.destino,
        status = this.status.toString()
    )
}