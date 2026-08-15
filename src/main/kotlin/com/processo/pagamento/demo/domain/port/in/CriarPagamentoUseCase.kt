package com.processo.pagamento.demo.domain.port.`in`

import com.processo.pagamento.demo.domain.model.Pagamento

interface CriarPagamentoUseCase {

    fun criarPagamento(request: Pagamento): Pagamento
}