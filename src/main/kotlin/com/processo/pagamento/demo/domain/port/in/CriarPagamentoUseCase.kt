package com.processo.pagamento.demo.domain.port.`in`

import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.model.PagamentoStatus
import java.util.UUID

interface CriarPagamentoUseCase {

    fun criarPagamento(request: Pagamento): Pagamento

    fun buscarPagamento(idPagamento: UUID): PagamentoStatus?
}