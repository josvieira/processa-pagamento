package com.processo.pagamento.demo.domain.port.out

import com.processo.pagamento.demo.domain.model.Pagamento

interface PagamentoRepositoryPort {

    fun salvarPagamento(pagamento: Pagamento): Pagamento
}