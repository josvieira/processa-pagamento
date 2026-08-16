package com.processo.pagamento.demo.domain.port.out

import com.processo.pagamento.demo.domain.model.Pagamento
import com.processo.pagamento.demo.domain.model.PagamentoStatus
import java.util.UUID

interface PagamentoRepositoryPort {

    fun salvarPagamento(pagamento: Pagamento): Pagamento

    fun adicionarStatus(pagamentoId: UUID, status: String, mensagem: String)

    fun buscarComHistorico(id: UUID): PagamentoStatus?
}