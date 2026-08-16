package com.processo.pagamento.demo.adapter.out.persistence.statusPagamento

import com.processo.pagamento.demo.adapter.out.persistence.pagamento.PagamentoEntity
import com.processo.pagamento.demo.adapter.out.persistence.statusPagamento.StatusPagamentoRepositoryPort
import org.springframework.stereotype.Component

@Component
class StatusPagamentoRepositoryAdapter(
    private val repository: StatusPagamentoRepository
) : StatusPagamentoRepositoryPort {


    override fun adicionarStatusPagamento(pagamentoEntity: PagamentoEntity, status: String, mensagem: String) {
        val statusPagamentoEntity =
            StatusPagamentoEntity(
                pagamento = pagamentoEntity,
                status = status,
                mensagem = mensagem
        )
        repository.save(statusPagamentoEntity)
    }
}