package com.processo.pagamento.demo.adapter.out.persistence.pagamento

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PagamentoRepository : JpaRepository<PagamentoEntity, UUID>