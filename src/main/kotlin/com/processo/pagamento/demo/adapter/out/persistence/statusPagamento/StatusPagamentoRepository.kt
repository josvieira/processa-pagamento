package com.processo.pagamento.demo.adapter.out.persistence.statusPagamento

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StatusPagamentoRepository: JpaRepository<StatusPagamentoEntity, UUID>{
    fun findByPagamento_IdOrderByCreatedAtAsc(pagamentoId: UUID): MutableList<StatusPagamentoEntity>
}